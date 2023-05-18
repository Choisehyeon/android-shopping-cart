package woowacourse.shopping.database.cart

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.domain.model.CartProduct
import com.example.domain.model.Product
import com.example.domain.repository.CartRepository
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_CART_PRODUCT_COUNT
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_CART_PRODUCT_IS_CHECKED
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_PRODUCT_ID
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_PRODUCT_IMAGE_URL
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_PRODUCT_NAME
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_PRODUCT_PRICE
import woowacourse.shopping.database.cart.CartConstant.TABLE_COLUMN_PRODUCT_SAVE_TIME
import woowacourse.shopping.database.cart.CartConstant.TABLE_NAME

class CartDatabase(
    private val shoppingDb: SQLiteDatabase,
) : CartRepository {
    override fun getAll(): List<CartProduct> {
        val cartProducts = mutableListOf<CartProduct>()
        getCartCursor().use {
            while (it.moveToNext()) {
                cartProducts.add(getCartProduct(it))
            }
        }
        return cartProducts
    }

    override fun getCheckCart(): List<CartProduct> {
        val cartProducts = mutableListOf<CartProduct>()
        getCartCheckCursor().use {
            while (it.moveToNext()) {
                cartProducts.add(getCartProduct(it))
            }
        }
        return cartProducts
    }

    @SuppressLint("Range")
    private fun getCartProduct(cursor: Cursor): CartProduct {
        val productId = cursor.getLong(cursor.getColumnIndex(TABLE_COLUMN_PRODUCT_ID))
        val productTitle =
            cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_PRODUCT_NAME))
        val productPrice =
            cursor.getInt(cursor.getColumnIndex(TABLE_COLUMN_PRODUCT_PRICE))
        val productImgUrl =
            cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_PRODUCT_IMAGE_URL))
        val cartProductCount =
            cursor.getInt(cursor.getColumnIndex(TABLE_COLUMN_CART_PRODUCT_COUNT))
        val cartIsChecked =
            cursor.getInt(cursor.getColumnIndex(TABLE_COLUMN_CART_PRODUCT_IS_CHECKED))
        val product = Product(productId, productTitle, productPrice, productImgUrl)
        return CartProduct(product, cartProductCount, cartIsChecked != 0)
    }

    override fun insert(product: CartProduct) {
        val values = ContentValues().apply {
            put(TABLE_COLUMN_PRODUCT_ID, product.product.id)
            put(TABLE_COLUMN_PRODUCT_NAME, product.product.name)
            put(TABLE_COLUMN_PRODUCT_PRICE, product.product.price)
            put(TABLE_COLUMN_PRODUCT_IMAGE_URL, product.product.imageUrl)
            put(TABLE_COLUMN_CART_PRODUCT_COUNT, product.count)
            put(TABLE_COLUMN_CART_PRODUCT_IS_CHECKED, true)
            put(TABLE_COLUMN_PRODUCT_SAVE_TIME, System.currentTimeMillis())
        }
        shoppingDb.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    override fun getSubList(offset: Int, size: Int): List<CartProduct> {
        val lastIndex = getAll().lastIndex
        val endIndex = (lastIndex + 1).coerceAtMost(offset + size)
        return if (offset <= lastIndex) getAll().subList(offset, endIndex) else emptyList()
    }

    override fun remove(id: Long) {
        val query =
            "DELETE FROM $TABLE_NAME WHERE $TABLE_COLUMN_PRODUCT_ID = $id"
        shoppingDb.execSQL(query)
    }

    override fun updateCount(id: Long, count: Int) {
        val query =
            "UPDATE $TABLE_NAME SET $TABLE_COLUMN_CART_PRODUCT_COUNT = $count WHERE  $TABLE_COLUMN_PRODUCT_ID = $id"
        shoppingDb.execSQL(query)
    }

    override fun updateCheckChanged(id: Long, check: Boolean) {
        val query =
            "UPDATE $TABLE_NAME SET $TABLE_COLUMN_CART_PRODUCT_IS_CHECKED = $check WHERE  $TABLE_COLUMN_PRODUCT_ID = $id"
        shoppingDb.execSQL(query)
    }

    override fun getFindById(id: Long): CartProduct {
        val cartProducts = mutableListOf<CartProduct>()
        findByIdCursor(id).use {
            while (it.moveToNext()) {
                Log.d("cart", getCartProduct(it).toString())
                cartProducts.add(getCartProduct(it))
            }
        }
        return cartProducts[0]
    }

    private fun findByIdCursor(id: Long): Cursor {
        val query =
            "SELECT * FROM $TABLE_NAME WHERE $TABLE_COLUMN_PRODUCT_ID = $id"
        return shoppingDb.rawQuery(query, null)
    }

    private fun getCartCursor(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $TABLE_COLUMN_PRODUCT_SAVE_TIME"
        return shoppingDb.rawQuery(query, null)
    }

    private fun getCartCheckCursor(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME WHERE $TABLE_COLUMN_CART_PRODUCT_IS_CHECKED = 1"
        return shoppingDb.rawQuery(query, null)
    }
}
