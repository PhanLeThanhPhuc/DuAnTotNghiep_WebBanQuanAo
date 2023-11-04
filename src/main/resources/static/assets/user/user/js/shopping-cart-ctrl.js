app.controller("cart-ctrl", function($scope, $http) {

	$scope.initialize = () => {
		$http.get("/user/province").then(resp => {
			$scope.listProvince = resp.data.data;
			console.log($cart.items.map(item => {
				return {
					name: item.product.name ,
					quantity: item.qty
				}
			}));
			console.log($scope.cart)
		})
		$scope.form();
		$scope.shipFee = 0;
	}

	$scope.form = () =>{
		$scope.formInformationOrder = {
			payment : '1'
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

	// $scope.methodOrder = () =>{

	purchase = () =>{
		// $scope.data = {
		// 	order :{
		// 		orderDate: new Date(),
		// 		detailAddress: $scope.formInformationOrder.name,
		// 		province: $scope.provinceName,
		// 		district: $scope.districtName,
		// 		ward: $scope.wardName,
		// 		email: $scope.formInformationOrder.email,
		// 		address: $scope.formInformationOrder.addressDetail +' '+ $scope.wardName+' '+ $scope.districtName+' '+ ' '+$scope.provinceName  ,
		// 		payment: $scope.formInformationOrder.payment ,
		// 		phone: $scope.formInformationOrder.phone,
		// 		total: $scope.cart.totalAmount,
		// 		totalDiscount: $scope.cart.amount,
		// 		weight : $scope.cart.totalWeights,
		// 		voucher: {
		// 			id: 1
		// 		},
		// 		orderDetails :
		// 			$cart.items.map(item => {
		// 				return {
		// 					productDetails: { id: item.id },
		// 					price: item.price.replace(/,/g, ""),
		// 					discountPrice: item.priceBeforeSale.replace(/,/g, ""),
		// 					quantity: item.qty,
		// 				}
		// 			}),
		// 	},
		// 	orderDataGHN : {
		// 		payment_type_id: 2,
		// 		to_name: $scope.formInformationOrder.name,
		// 		to_phone: $scope.formInformationOrder.phone,
		// 		to_address: $scope.formInformationOrder.addressDetail +' '+ $scope.wardName+' '+ $scope.districtName+' '+ ' '+$scope.provinceName  ,
		// 		to_ward_code: $scope.wardId ,
		// 		to_district_id: parseInt($scope.districtId),
		// 		cod_amount: $scope.cart.totalAmount,
		// 		// content: "Đơn hàng quần áo ba lỗ không dễ vỡ",
		// 		weight: $scope.cart.totalWeights,
		// 		service_type_id: 2,
		// 		note: $scope.formInformationOrder.note,
		// 		required_note: "CHOXEMHANGKHONGTHU",
		// 		items: $cart.items.map(item => {
		// 			return {
		// 				name: item.product.name ,
		// 				quantity: item.qty
		// 			}
		// 		})
		// 	}
		// }
		//
		// console.log('ddđ',$scope.data)

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
					console.log(resp.data.vnPayUrl);
					// $cart.clear();
					// location.href = resp.data.vnPayUrl;
				}else{
					location.href = resp.data.vnPayUrl;
				}
			}
		}).catch(error => {
			alert("Đặt hàng lỗi!")
			console.log(error)
		})
	}
})