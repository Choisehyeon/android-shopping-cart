package woowacourse.shopping.ui.cart.contract.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.repository.CartRepository
import woowacourse.shopping.mapper.toUIModel
import woowacourse.shopping.model.CartItemsUIModel
import woowacourse.shopping.model.CartUIModel
import woowacourse.shopping.ui.cart.CartActivity.Companion.KEY_OFFSET
import woowacourse.shopping.ui.cart.contract.CartContract

class Offset(offset: Int, private val repository: CartRepository) {
    private var offset: Int = offset
        set(value) {
            field = when {
                value < 0 -> 0
                value > repository.getAll().size -> repository.getAll().size
                else -> value
            }
        }

    fun plus(value: Int): Offset = Offset(offset + value, repository)
    fun minus(value: Int): Offset = Offset(offset - value, repository)
    fun getOffset(): Int = offset
}

class CartPresenter(
    val view: CartContract.View,
    private val repository: CartRepository,
    offset: Int = 0,
) : CartContract.Presenter {
    private var cartOffset = Offset(offset, repository)
    private var cartItems: CartItemsUIModel =
        CartItemsUIModel(repository.getCheckCart().map { it.toUIModel() })
    private var _countLiveDatas: MutableMap<Long, MutableLiveData<Int>> = mutableMapOf()
    val countLiveDatas: Map<Long, LiveData<Int>> = _countLiveDatas

    private var _checkedLiveDatas: MutableMap<Long, MutableLiveData<Boolean>> = mutableMapOf()
    val checkedLiveDatas: Map<Long, LiveData<Boolean>> = _checkedLiveDatas

    init {
        setAllOrderCount()
        setAllCheckbox()
    }

    override fun setUpCarts() {
        val datas = repository.getSubList(cartOffset.getOffset(), STEP)
        datas.forEach {
            _countLiveDatas[it.product.id] = MutableLiveData(getCount(it.product.id))
            _checkedLiveDatas[it.product.id] = MutableLiveData(getChecked(it.product.id))
        }
        setCartItemsPrice()
        view.setCarts(
            datas.map { it.toUIModel() },
            CartUIModel(
                cartOffset.getOffset() + STEP < repository.getAll().size,
                0 < cartOffset.getOffset(),
                cartOffset.getOffset() / STEP + 1,
            ),
        )
    }

    override fun pageUp() {
        cartOffset = cartOffset.plus(STEP)
        setUpCarts()
    }

    override fun pageDown() {
        cartOffset = cartOffset.minus(STEP)
        setUpCarts()
    }

    override fun removeItem(id: Long) {
        repository.remove(id)
        if (cartOffset.getOffset() == repository.getAll().size) {
            cartOffset = cartOffset.minus(STEP)
        }
        setUpCarts()
        updateCartItems()
    }

    override fun navigateToItemDetail(id: Long) {
        val product = repository.getFindById(id)?.product
        product?.let { view.navigateToItemDetail(it.toUIModel()) }
    }

    override fun saveOffsetState(outState: MutableMap<String, Int>) {
        outState[KEY_OFFSET] = cartOffset.getOffset()
    }

    override fun restoreOffsetState(state: Map<String, Int>) {
        val savedOffset = state[KEY_OFFSET] ?: 0
        cartOffset = Offset(savedOffset, repository)
    }

    override fun onCheckChanged(id: Long, isChecked: Boolean) {
        _checkedLiveDatas[id]?.value = isChecked
        _checkedLiveDatas[id]?.value?.let { repository.updateCheckChanged(id, it) }
        updateCartItems()
    }

    override fun setCartItemsPrice() {
        view.setCartItemsPrice(cartItems.caculatePrice())
    }

    override fun onAllCheckboxClick(isChecked: Boolean) {
        repository.getSubList(cartOffset.getOffset(), STEP).map { it.toUIModel() }
            .forEach { product ->
                repository.updateCheckChanged(product.product.id, isChecked)
                //view.updateCheckboxItem(product.product.id, isChecked)
            }
        updateCartItems()
    }

    override fun setAllCheckbox() {
        val cartItems = repository.getSubList(cartOffset.getOffset(), STEP).map { it.toUIModel() }
        val isChecked = cartItems.filter { it.isChecked }.size == cartItems.size

        view.setAllCheckbox(isChecked)
    }

    override fun setAllOrderCount() {
        view.setAllOrderCount(cartItems.products.size)
    }

    override fun increaseCount(id: Long) {
        _countLiveDatas[id]?.value = _countLiveDatas[id]?.value?.plus(1)
        _countLiveDatas[id]?.value?.let { repository.updateCount(id, it) }
        updateCartItems()
    }

    override fun decreaseCount(id: Long) {
        _countLiveDatas[id]?.value = _countLiveDatas[id]?.value?.minus(1)
        _countLiveDatas[id]?.value?.let { repository.updateCount(id, it) }
        updateCartItems()
    }

    private fun updateCartItems() {
        cartItems = cartItems.updateCartItems(repository.getCheckCart().map { it.toUIModel() })
        setAllOrderCount()
        setCartItemsPrice()
    }

    private fun getCount(id: Long): Int {
        return repository.getFindById(id)?.count ?: 0
    }

    private fun getChecked(id: Long): Boolean {
        return repository.getFindById(id)?.isChecked ?: true
    }

    companion object {
        private const val STEP = 5
    }
}
