# android-shopping-cart

### 기능 목록

- [X] 상품 목록을 화면에 보여준다.
- [X] 상품을 클릭하면 상세 페이지로 이동한다.
- [X] 상품 상세에서 장바구니에 상품을 담을 수 있다.
- [X] 장바구니에서 원하는 상품을 삭제할 수 있다.
- [X] 최근 본 상품 목록을 가져온다.
  - [X] 목록 상단에서 10개까지 확인할 수 있다.
  - [X] 최근 본 상품이 없으면 화면에 나오지 않는다.
  - [X] 상품을 클릭하면 상세 페이지로 이동한다.
  - [X] 상품을 클릭하면 최근 본 상품 목록에 추가 된다.
- [X] 앱이 종료돼도 최근 본 상품 목록과 장바구니 데이터는 유지돼야 한다.
- [X] 상품 목록에서 + 버튼을 클릭할 수 있다.
- [X] 상품 목록에서 + 버튼을 클릭하면 수량을 선택할 수 있는 뷰가 열린다.
  - [X] 상품 목록에서 보여지는 수량은 장바구니에 담겨있는 수량이다.
  - [ ] 수량을 선택할 수 있는 뷰에서 +/- 버튼을 누르면 수량이 1증가/1감소한다.
  - [ ] 수량을 선택할 수 있는 뷰에서 +/- 버튼을 누르면 수량이 장바구니에 저장된다.
- [X] 상품 상세에서 마지막으로 본 상품을 볼 수 있다.
  - [X] 상품 상세에서 마지막으로 본 상품을 누르면 마지막으로 본 상품 상세로 이동한다.
  - [X] 마지막으로 본 상품을 눌러 들어가면 마지막으로 본 상품을 볼 수 없다.
- [X] 상품 상세에서 장바구니 담기버튼을 누르면 상품 수량을 정할 수 있는 다이얼로그가 열린다.
  - [X] 다이얼로그에서 +/- 버튼으로 수량을 선택할 수 있다.
  - [X] 다이얼로그에서 담기 버튼을 누르면 장바구니에 담긴다.
    - [X] 이미 장바구니에 존재하는 상품이면 상품의 수량이 장바구니에 담긴 상품의 수량에 다이얼로그에서 선택한 수량이 더해져 저장된다.
    - [X] 장바구니에 없는 상품이면 선택한 수량으로 상품이 저장된다.
- [ ] 장바구니에 전체를 선택할 수 있는 체크박스가 있다.
  - [ ] 전체 체크박스를 선택하면 장바구니에 담긴 아이템들의 체크박스가 모두 선택된다.
  - [ ] 해제하면 장바구니에 담긴 아이템들의 체크박스가 모두 해제된다.
- [ ] 장바구니에 체크된 아이템들의 수량에 맞는 총 가격이 보여진다.
- [ ] 장바구니에 있는 아이템마다 수량을 +/-로 선택할 수 있다.
- [ ] 장바구니에 들어가 있는 아이템 개수가 몇 개인지 보여준다.
  - [ ] 툴바에도 보여진다.

- [x] Product
  - id, 이름, 금액, 사진을 가짐

- [x] Cart
  - [x] 상품을 담을 수 있다.
  - [x] 상품을 뺄 수 있다.
  - [x] 상품을 불러올 수 있다.
