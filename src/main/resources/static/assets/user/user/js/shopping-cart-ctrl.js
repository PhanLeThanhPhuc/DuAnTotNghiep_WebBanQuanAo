app.controller("cart-ctrl", function($scope, $http) {

	$scope.initialize = () => {

		$http.get("/user/province").then(resp => {
			$scope.listProvince = resp.data.data;
		})

		//user
		$http.get("/rest/users/userid").then(resp => {
			if(resp.status == 200){
				$scope.dataLogin = resp.data;
			}
		})

		$http.get("/rest/address").then(resp => {
			$scope.listAddress = resp.data;
		})

		$scope.form();
		$scope.shipFee = 0;
	}


	$scope.form = () =>{
		$scope.formInformationOrder = {
			payment : '1',
		}
	}

	methodDistrict = (province_id) =>{
		if(province_id !== "default") {
			$http.get(`/user/district?province_id=${province_id}`).then(resp => {
				$scope.listDistrict = resp.data.data;
			})
			clearCbbWard();
		}
		$scope.shipFee = 0;
	}

	methodWard = (district_id) =>{
		if(district_id !== "default") {
			$http.get(`/user/ward?district_id=${district_id}`).then(resp => {
				$scope.listWard = resp.data.data;
				// console.log($scope.listWard)
			})
		}else{
			clearCbbWard();
		}
		// checkCbbAddres();
		$scope.shipFee = 0;
	}

	clearCbbWard = () =>{
		var wardSelect = document.getElementById('wardSelect');
		wardSelect.options.length = 0;
		const option = document.createElement("option");
		option.text = "Xã";
		option.value = "default";
		wardSelect.add(option);
	}

	$scope.initialize();

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
						resp.data.sale=$('.sale').text();
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
			return item.price.replace(/,/g, "") * item.qty;
		},
		get count() { // tính tổng số lượng các mặt hàng trong giỏ
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total += qty, 0);
		},
		get totalWeights() { // tính tổng số lượng các mặt hàng trong giỏ
			return this.items
				.map(item => item.product.weight * item.qty)
				.reduce((total, weight) => total += weight, 0);
		},

		get amount() { // tổng thành tiền các mặt hàng trong giỏ
			return this.items
				.map(item => this.amt_of(item))
				.reduce((total, amt) => total += amt, 0);
		},

		get totalAmount () {
			return this.amount + $scope.shipFee;
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

	checkCbbAddres = () => {
		var cboAddress = document.querySelectorAll(".country_select");

		var province = cboAddress[0].options[cboAddress[0].selectedIndex].value;

		var district = cboAddress[1].options[cboAddress[1].selectedIndex].value;

		var ward = cboAddress[2].options[cboAddress[2].selectedIndex].value;

		if(province !== "default" && district !== "default" && ward !== "default"){
			$scope.getShippingFee();
		}
	}

	$scope.getShippingFee = () =>{

		var cboAddress = document.querySelectorAll(".country_select");

		$scope.provinceId = cboAddress[0].options[cboAddress[0].selectedIndex].value;

		$scope.districtId = cboAddress[1].options[cboAddress[1].selectedIndex].value;

		$scope.wardId = cboAddress[2].options[cboAddress[2].selectedIndex].value;

		$scope.provinceName = cboAddress[0].options[cboAddress[0].selectedIndex].text;

		$scope.districtName = cboAddress[1].options[cboAddress[1].selectedIndex].text;

		$scope.wardName = cboAddress[2].options[cboAddress[2].selectedIndex].text;

		var bodyProduct = {
			service_type_id : 2,
			to_ward_code: $scope.wardId ,
			to_district_id: parseInt($scope.districtId),
			weight: $scope.cart.totalWeights,
			items: $cart.items.map(item => {
				return {
					name: item.product.name ,
					quantity: item.qty
				}
			})
		}

		$http.post(`/user/shipfee`, bodyProduct).then(resp => {
			if(resp.status === 200){
				$scope.shipFee = resp.data.data.total;
				console.log(resp.data.data.expected_delivery_time);
			}
		}).catch(error => {
			alert("Lỗi thêm mới !");
			console.log("Error", error);
		});

	}

	getShipAddress = () =>{
		var idAddress = $scope.formInformationOrder.address;
		var index = $scope.listAddress.findIndex(p => p.id == parseInt(idAddress));
		var addressChecked = $scope.listAddress[index];

		var bodyProduct = {
			service_type_id : 2,
			to_ward_code: addressChecked.wardId ,
			to_district_id: parseInt(addressChecked.districtId),
			weight: $scope.cart.totalWeights,
			items: $cart.items.map(item => {
				return {
					name: item.product.name ,
					quantity: item.qty
				}
			})
		}

		$http.post(`/user/shipfee`, bodyProduct).then(resp => {
			if(resp.status === 200){
				$scope.shipFee = resp.data.data.total;
				console.log(resp.data.data.expected_delivery_time);
			}
		}).catch(error => {
			alert("Lỗi thêm mới !");
			console.log("Error", error);
		});
	}

	addAddress = () =>{
		var cboAddress = document.querySelectorAll(".country_select_add");

		var provinceId = cboAddress[0].options[cboAddress[0].selectedIndex].value;

		var districtId = cboAddress[1].options[cboAddress[1].selectedIndex].value;

		var wardId = cboAddress[2].options[cboAddress[2].selectedIndex].value;

		var provinceName = cboAddress[0].options[cboAddress[0].selectedIndex].text;

		var districtName = cboAddress[1].options[cboAddress[1].selectedIndex].text;

		var wardName = cboAddress[2].options[cboAddress[2].selectedIndex].text;

		var objectAddress = {
			provinceName: provinceName,
			districtName: districtName,
			wardName: wardName,
			addressDetail: $scope.formInformationOrder.addressDetail,
			provinceId: parseInt(provinceId),
			districtId: parseInt(districtId),
			wardId: wardId,
			user: {
				id :$scope.dataLogin.idUser
			}
		}

		$http.post("/rest/insert-address", objectAddress).then(resp => {
			if(resp.status === 200){
				$scope.listAddress.push(resp.data);
				$scope.message("Thêm địa chỉ thành công");
			}
		}).catch(error => {
			// alert("Lỗi")
			console.log(error)
		})
	}

	purchase = () =>{
		$scope.data = {
			province: $scope.provinceName,
			district: $scope.districtName,
			ward: $scope.wardName,
			detailAddress: $scope.formInformationOrder.addressDetail,
			nameUser: $scope.formInformationOrder.name,
			orderDate: new Date(),
			// status: 0,
			phone : $scope.formInformationOrder.phone,
			payment: $scope.formInformationOrder.payment ,
			shipCode: "",
			shipFee: $scope.shipFee,
			email: $scope.formInformationOrder.email,
			total: $scope.cart.totalAmount,
			totalDiscount: $scope.cart.totalAmount,
			weight : $scope.cart.totalWeights,
			wardCode: $scope.wardId ,
			districtId: parseInt($scope.districtId),
			// user: { id: 1 },
			// voucher: { id: 2 },
			orderDetails: $cart.items.map(item => {
				return {
					productDetails: { id: item.id },
					price: item.price.replace(/,/g, ""),
					discountPrice: item.priceBeforeSale.replace(/,/g, ""),
					quantity: item.qty,
				}
			}),
		};

		$http.post("/user/order", $scope.data,).then(resp => {
			if(resp.status === 200){
				alert("Đặt hàng thành công!");
				if(resp.data.payment === 1){
					console.log(resp.data.urlVnPay);
					$cart.clear();
					location.href = resp.data.urlVnPay
				}else{
					var idorder =resp.data.order.id;
					$cart.clear();
					location.href = `/user/information-order?idorder=${idorder}`
				}
			}
		}).catch(error => {
			alert("Đặt hàng lỗi!")
			console.log(error)
		})
	}

	$scope.message = (mes) =>{
		$.toast({
			text: mes, // Text that is to be shown in the toast
			heading: 'Thông báo', // Optional heading to be shown on the toast
			icon: 'success', // Type of toast icon
			showHideTransition: 'fade', // fade, slide or plain
			allowToastClose: true, // Boolean value true or false
			hideAfter: 2000, // false to make it sticky or number representing the miliseconds as time after which toast needs to be hidden
			stack: 5, // false if there should be only one toast at a time or a number representing the maximum number of toasts to be shown at a time
			position: 'top-right', // bottom-left or bottom-right or bottom-center or top-left or top-right or top-center or mid-center or an object representing the left, right, top, bottom values
			textAlign: 'left',  // Text alignment i.e. left, right or center
			loader: true,  // Whether to show loader or not. True by default
			loaderBg: '#9EC600',  // Background color of the toast loader
			beforeShow: function () {}, // will be triggered before the toast is shown
			afterShown: function () {}, // will be triggered after the toat has been shown
			beforeHide: function () {}, // will be triggered before the toast gets hidden
			afterHidden: function () {}  // will be triggered after the toast has been hidden
		});
	}

})