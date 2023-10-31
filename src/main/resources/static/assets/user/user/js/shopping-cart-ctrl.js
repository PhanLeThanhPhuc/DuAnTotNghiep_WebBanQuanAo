app.controller("cart-ctrl", function($scope, $http) {
	// quản lý giỏ hàng
	
	$scope.soluong=$('.soluong').text();
	$scope.sizeClick = function(size) {
		$scope.soluong = size;
	};


	$scope.sizeid = function(idff) {
		$scope.sizeidf = idff;
	};


	var $cart = $scope.cart = {
		qtyyy: 1,
		items: [],
		add(id) { // thêm sản phẩm vào giỏ hàng
			var sizeid = $scope.sizeidf;
			if (sizeid == null) {
				alert("Vui Lòng chọn size !!");
			}
			var item = this.items.find(item => item.product.id == id && item.size.id == $scope.sizeidf);
			var qtt = this.qtyyy;
			var sol = $scope.soluong;

			if (item) {
				var qtt2 = item.qty + qtt;
				if (qtt2 > sol) {
					alert("Vượt quá số lượng cho phép !!!");
				} else {
					item.qty += qtt;
					this.saveToLocalStorage();
				}
			}
			else {
				$http.get(`/rest/productsDetail/size/${id}/` + sizeid).then(resp => {
					if (qtt > sol) {
						alert("Vượt quá số lượng cho phép !!!");
					} else {
						resp.data.priceBeforeSale=$('.priceBeforeSale').text();
						resp.data.price=$('.price').text();
						resp.data.qty = qtt;
						this.items.push(resp.data);
						this.saveToLocalStorage();
						alert("Thêm sản phẩm thành công");
					}



				})
			}
		},
		remove(id, size) { // xóa sản phẩm khỏi giỏ hàng
			var index = this.items.findIndex(item => item.product.id == id && item.size.id == size);
			this.items.splice(index, 1);
			this.saveToLocalStorage();
		},
		clear() { // Xóa sạch các mặt hàng trong giỏ
			this.items = []
			this.saveToLocalStorage();
		},
		amt_of(item) { // tính thành tiền của 1 sản phẩm
			return item.product.price * item.qty;
		},
		get count() { // tính tổng số lượng các mặt hàng trong giỏ
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total += qty, 0);
		},
		get amount() { // tổng thành tiền các mặt hàng trong giỏ
			return this.items
				.map(item => this.amt_of(item))
				.reduce((total, amt) => total += amt, 0);
		},
		cartChange(size, product, qti) {

			var item = this.items.find(item => item.product.id == product && item.size.id == size);
			if (qti > item.quantity) {
				item.qty = item.quantity;
				alert("Vượt quá số lượng cho phép !!!");
			}

			this.saveToLocalStorage();

		},
		saveToLocalStorage() { // lưu giỏ hàng vào local storage

			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},
		loadFromLocalStorage() { // đọc giỏ hàng từ local storage
			var json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		}
	}

	$cart.loadFromLocalStorage();

	// Đặt hàng
	$scope.order = {
		get account() {
			return { username: $auth.user.username }
		},
		createDate: new Date(),
		address: "",
		get orderDetails() {
			return $cart.items.map(item => {
				return {
					product: { id: item.id },
					price: item.price,
					quantity: item.qty
				}
			});
		},
		purchase() {
			var order = angular.copy(this);
			// Thực hiện đặt hàng
			$http.post("/rest/orders", order).then(resp => {
				alert("Đặt hàng thành công!");
				$cart.clear();
				location.href = "/order/detail/" + resp.data.id;
			}).catch(error => {
				alert("Đặt hàng lỗi!")
				console.log(error)
			})
		}
	}


})