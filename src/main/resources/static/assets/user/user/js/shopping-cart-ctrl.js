app.controller("cart-ctrl", function($scope, $http) {

	$scope.initialize = async () => {
		// $scope.listAddress =[];
		//get province
		await $http.get("/user/province").then(resp => {
			$scope.listProvince = resp.data.data;
		})
		 $http.get("/rest/productsDetail/date").then(resp => {
			$scope.dateEnd = new Date(resp.data);
	
		})

		//get user or status login
		await $http.get("/rest/users/userid").then(resp => {
			if (resp.status == 200) {
				$scope.dataLogin = resp.data;
				console.log($scope.dataLogin)
			}
		})

		//get address user
		await $http.get("/rest/address").then(resp => {
			$scope.listAddress = resp.data;
			console.log("ADđress", $scope.listAddress)
		})

		//voucher
		await $http.get("/rest/voucher/date").then(resp => {
			$scope.listVoucherDate = resp.data;
			console.log("VOUCHER: ", $scope.listVoucherDate)
		})


		$scope.form();
		$scope.shipFee = 0;
		$scope.findInfoUser();
		$scope.discoutVoucher = 0;
		$scope.voucherId = '';
		console.log("san pham", $scope.cart)
	}

	$scope.findInfoUser = () => {
		console.log('Data from API:', $scope.dataLogin);
		if ($scope.dataLogin.statusLogin) {
			$scope.formInformationOrder.name = $scope.dataLogin.user.fullName;
			$scope.formInformationOrder.phone = $scope.dataLogin.user.phone;
			$scope.formInformationOrder.email = $scope.dataLogin.user.email;
			document.getElementById("phone").disabled = true;
			$scope.$apply();
			console.log("phone:",$scope.dataLogin.user.phone)
		}
		if($scope.dataLogin.user.phone === null){
			$('#exampleModalCenter3').modal('show');
		}

	}

	$scope.form = () => {
		$scope.formInformationOrder = {
			payment: '1',
		}
	}

	methodDistrict = (province_id) => {
		if (province_id !== "default") {
			$http.get(`/user/district?province_id=${province_id}`).then(resp => {
				$scope.listDistrict = resp.data.data;
			})
			clearCbbWard();
		}
		$scope.shipFee = 0;
	}

	methodWard = (district_id) => {
		if (district_id !== "default") {
			$http.get(`/user/ward?district_id=${district_id}`).then(resp => {
				$scope.listWard = resp.data.data;
			})
		} else {
			clearCbbWard();
		}
		// checkCbbAddres();
		$scope.shipFee = 0;
	}

	clearCbbWard = () => {
		var wardSelect = document.getElementById('wardSelect');
		wardSelect.options.length = 0;
		const option = document.createElement("option");
		option.text = "Xã";
		option.value = "default";
		wardSelect.add(option);
	}

	$scope.initialize();

	// quản lý giỏ hàng
	$scope.soluong = $('.soluong').text();

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
			if(item.product.sale==false){
				return item.product.price * item.qty;
			}else{
				return item.product.discountPrice  * item.qty;
			}
		},

		amtNoDiscount(item) { // tính thành tiền của 1 sản phẩm k discount
			return item.product.price * item.qty;
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

		get totalNoDiscount() {
			return this.items
				.map(item => this.amtNoDiscount(item))
				.reduce((total, amt) => total += amt, 0);
		},

		get totalDiscount() {
			return this.amount - $scope.discoutVoucher;
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

			var cartData = this.items.map(item => ({
				qty: item.qty,
				productId: item.product.id,
				sizeId: item.size.id
			}));
			var json = JSON.stringify(cartData);
			localStorage.setItem("cart", json);
		},
		loadFromLocalStorage() { // đọc giỏ hàng từ local storage
			var json = localStorage.getItem("cart");
			product = json ? JSON.parse(json) : [];
			
			product.forEach(product => {
            $http.get(`/rest/productsDetail/size/${product.productId}/` + product.sizeId).then(resp => {
				resp.data.qty =product.qty
                this.items.push(resp.data); 
            });
        });
		}
	}

	$cart.loadFromLocalStorage();

	getValueComboBoxAddress = () => {
		var cboAddress = document.querySelectorAll(".country_select");

		var province = cboAddress[0].options[cboAddress[0].selectedIndex].value;

		var district = cboAddress[1].options[cboAddress[1].selectedIndex].value;

		var ward = cboAddress[2].options[cboAddress[2].selectedIndex].value;

		if (province !== "default" && district !== "default" && ward !== "default") {
			$scope.getShippingFee();
		}
	}

	$scope.getShippingFee = () => {

		$scope.provinceId;

		$scope.districtId;

		$scope.wardId;

		$scope.provinceName;

		$scope.districtName;

		$scope.wardName;

		$scope.formInformationOrder.addressDetail;

		function shipFeeLogin() {

			///////////lay address từ users
			var idAddress = $scope.formInformationOrder.address;
			var index = $scope.listAddress.findIndex(p => p.id == parseInt(idAddress));
			var addressChecked = $scope.listAddress[index];

			$scope.provinceId = addressChecked.provinceId;

			$scope.districtId = addressChecked.districtId;

			$scope.wardId = addressChecked.wardId;

			$scope.provinceName = addressChecked.provinceName;

			$scope.districtName = addressChecked.districtName;

			$scope.wardName = addressChecked.wardName;

			$scope.formInformationOrder.addressDetail = addressChecked.addressDetail;
		}

		function shipFeeNoLogin() {
			var cboAddress = document.querySelectorAll(".country_select");

			$scope.provinceId = cboAddress[0].options[cboAddress[0].selectedIndex].value;

			$scope.districtId = cboAddress[1].options[cboAddress[1].selectedIndex].value;

			$scope.wardId = cboAddress[2].options[cboAddress[2].selectedIndex].value;

			$scope.provinceName = cboAddress[0].options[cboAddress[0].selectedIndex].text;

			$scope.districtName = cboAddress[1].options[cboAddress[1].selectedIndex].text;

			$scope.wardName = cboAddress[2].options[cboAddress[2].selectedIndex].text;
		}

		if ($scope.dataLogin.statusLogin) {
			shipFeeLogin();
		} else {
			shipFeeNoLogin();
		}

		var bodyProduct = {
			service_type_id: 2,
			to_ward_code: $scope.wardId,
			to_district_id: parseInt($scope.districtId),
			weight: $scope.cart.totalWeights,
			items: $cart.items.map(item => {
				return {
					name: item.product.name,
					quantity: item.qty
				}
			})
		}

		$http.post(`/user/shipfee`, bodyProduct).then(resp => {
			if (resp.status === 200) {
				$scope.shipFee = resp.data.data.total;
				console.log(resp.data.data.expected_delivery_time);
			}
		}).catch(error => {
			alert("Lỗi thêm mới !");
			console.log("Error", error);
		});

	}

	addAddress = () => {
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
				id: $scope.dataLogin.user.id
			}
		}

		$http.post("/rest/insert-address", objectAddress).then(resp => {
			if (resp.status === 200) {
				$scope.listAddress.push(resp.data);
				console.log("Địa chỉ res: ", $scope.listAddress)
				$scope.message("Thêm địa chỉ thành công");
			}
		}).catch(error => {
			// alert("Lỗi")
			console.log(error)
		})
	}

	purchase = () => {
		if(!validate()){
			return ;
		}
		$scope.data = {
			province: $scope.provinceName,
			district: $scope.districtName,
			ward: $scope.wardName,
			detailAddress: $scope.formInformationOrder.addressDetail,
			nameUser: $scope.formInformationOrder.name,
			orderDate: new Date(),
			// status: 0,
			phone: $scope.formInformationOrder.phone,
			payment: $scope.formInformationOrder.payment,
			shipCode: "",
			note: $scope.formInformationOrder.note,
			shipFee: $scope.shipFee,
			email: $scope.formInformationOrder.email,
			total: $scope.cart.amount + $scope.discoutVoucher,
			totalDiscount: $scope.cart.totalNoDiscount -  $scope.cart.amount + $scope.discoutVoucher,
			weight: $scope.cart.totalWeights,
			wardCode: $scope.wardId,
			districtId: parseInt($scope.districtId),
			// user: { id: 1 },
			voucher: { id: $scope.voucherId },
			orderDetails: $cart.items.map(item => {
				return {
					productDetails: { id: item.id },
					price: $cart.amt_of(item),
					discountPrice: item.product.sale === true ? (item.product.price * item.qty) - $cart.amt_of(item) : 0,
					quantity: item.qty,
				}
			}),
		};

		console.log("Order: ", $scope.data);

		$http.post("/user/order", $scope.data,).then(resp => {
			if (resp.status === 200) {
				alert("Đặt hàng thành công!");
				if (resp.data.payment === 1) {
					// console.log(resp.data.urlVnPay);
					$cart.clear();
					location.href = resp.data.urlVnPay
				} else {
					var idorder = resp.data.order.id;
					$cart.clear();
					location.href = `/user/information-order?idorder=${idorder}`
				}
			}
		}).catch(error => {
			alert("Đặt hàng lỗi!")
			console.log(error)
		})
	}


	// discout
	$scope.addVoucher = () => {
		///reset
		$scope.discoutVoucher =0;
		const voucher = document.getElementById("inputvoucher").value;
		const foundVoucher = $scope.listVoucherDate.find(v => v.voucher === voucher);
		if (foundVoucher) {
			console.log("CÓ voucher");
			var index = $scope.listVoucherDate.findIndex(v => v.voucher === voucher);
			$scope.objectVoucher = $scope.listVoucherDate[index];
			if (foundVoucher.quantity === 0) {
				var smallElement = document.getElementById("errorVoucher");
				smallElement.innerHTML = "Voucher đã hết số lượng!";
				console.log("voucher đã hết số lượng");
			} else if ($scope.cart.count < foundVoucher.minOrderProduct) {
				var smallElement = document.getElementById("errorVoucher");
				smallElement.innerHTML = "Số lượng mặt hàng không đủ để áp dụng voucher!";
				console.log("số lượng mặt hàng không đủ áp dụng voucher");
			} else if ($scope.cart.amount < foundVoucher.min) {
				var smallElement = document.getElementById("errorVoucher");
				smallElement.innerHTML = "Số tiền đơn hàng phải lớn hơn", foundVoucher.min, "để áp dụng voucher!";
				console.log("Số tiền đơn hàng phải lớn hơn", foundVoucher.min, "");
			} else {
				console.log("Đủ điều kiện áp dụng", foundVoucher.min, "");
				if (foundVoucher.productID === "") {
					console.log("Áp dụng cho tất cả sản phẩm");
					$scope.voucherId = $scope.objectVoucher.id;
					$scope.discoutVoucher = $scope.objectVoucher.discountPrice;
					$scope.totalVoucher = $scope.cart.totalAmount - $scope.discoutVoucher;
					var smallElement = document.getElementById("errorVoucher");
					smallElement.innerHTML = "Đã áp dụng voucher!";
					console.log("Total khi ap voucher: ", $scope.totalVoucher);
				} else {
					for (const item of $scope.cart.items) {
						const productIDs = foundVoucher.productID.split(',').map(id => id.trim());
						const cartItemId = String(item.product.id);
						console.log("cartItemId",cartItemId);
						console.log("productIDs",productIDs);
						if (productIDs.includes(cartItemId)) {
							$scope.voucherId = $scope.objectVoucher.id;
							console.log("Có sản phẩm: ", cartItemId);
							$scope.discoutVoucher = $scope.objectVoucher.discountPrice;
							$scope.totalVoucher = $scope.cart.totalAmount - $scope.discoutVoucher;
							var smallElement = document.getElementById("errorVoucher");
							smallElement.innerHTML = "Đã áp dụng voucher!";
						} else {
							var smallElement = document.getElementById("errorVoucher");
							smallElement.innerHTML = "Sản phẩm không được áp dụng mã giảm giá này!";
							// $scope.message("Sản phẩm không được áp dụng mã giảm giá!");
						}
					}
				}
			}
		} else {
			var smallElement = document.getElementById("errorVoucher");
			smallElement.innerHTML = "Mã voucher không khả dụng!";
		}
	};
	
	
	
	

	function updateCountdown() {
	  const currentDate = new Date();
	  const timeDifference = $scope.dateEnd - currentDate;
	  if (timeDifference > 0) {
		const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
		const hours = Math.floor((timeDifference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
		const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

		document.getElementById('countdown').innerHTML = `
		  ${days} ngày, ${hours} giờ, ${minutes} phút, ${seconds} giây
		`;

	  }
	}

	setInterval(updateCountdown, 1000);
	updateCountdown();

	$scope.registerPhoneNumber = async () => {

		const phoneNumber = "0"+$scope.registerPhone;
		const email = $scope.formInformationOrder.email;
		console.log("Phone: ",phoneNumber);
		await $http.get(`/rest/users/register-phone?phone=${phoneNumber}&email=${email}`).then(resp => {

			console.log("data",resp.data)
			if(resp.data.data == null){
				console.log("message",resp.data.message)
			}else{

				const currentTime = new Date();

				const otpCreationTime = new Date(resp.data.data.timeOtp)

				const elapsedTime = Math.floor((currentTime - otpCreationTime) / 1000);
				const remainingTime = 60 - elapsedTime;

				countdownotp(remainingTime)

				// console.log("Thời gian còn lại",remainingTime);
				$("#exampleModalCenter3").modal("hide");
				// Đóng modal sử dụng jQuery khi nút "Close" được click
				$("#exampleModalCenter4").modal("show");

			}
		})
	}



	$scope.comfirmOtp = async () => {
		const otp = $scope.otp;
		const phoneNumber = $scope.registerPhone;
		const email = $scope.formInformationOrder.email;
		await $http.get(`/rest/users/confirm-otp?otp=${otp}&phone=${phoneNumber}&email=${email}`).then(resp => {
			console.log("respose: ", resp.data);
			if(resp.status === 'timeout'){
				var smallElement = document.getElementById("messageOtp");
				smallElement.innerHTML = resp.data.message;
			}else if (resp.status === 'true'){
				var smallElement = document.getElementById("messageOtp");
				smallElement.innerHTML = resp.data.message;
			}else{
				var smallElement = document.getElementById("messageOtp");
				smallElement.innerHTML = resp.data.message;
			}
		})
	}

	countdownotp = (second) => {
		// Thời gian bắt đầu đếm ngược (theo giây)
		var initialSeconds = second; // Ví dụ: 5 phút = 300 giây

		// Lấy thẻ div để hiển thị đồng hồ đếm ngược
		var countdownElement = document.getElementById('countdown');

		// Tính tổng số giây
		var totalSeconds = initialSeconds;

		// Cập nhật đồng hồ đếm ngược sau mỗi giây
		var countdownInterval = setInterval(function () {
			// Kiểm tra nếu hết thời gian
			if (totalSeconds <= 0) {
				clearInterval(countdownInterval);
				countdownElement.innerHTML = "Hết giờ!";
			} else {
				// Tính phút và giây còn lại
				var remainingMinutes = Math.floor(totalSeconds / 60);
				var remainingSeconds = totalSeconds % 60;

				// Hiển thị đồng hồ đếm ngược
				countdownElement.innerHTML = remainingMinutes + ' phút ' + remainingSeconds + ' giây';

				// Giảm tổng số giây đi 1
				totalSeconds--;
			}
		}, 1000); // 1000 milliseconds = 1 giây
	}

	validate = () =>{
		var name = document.getElementById('name').value;
		var phone = document.getElementById('phone').value;
		var email = document.getElementById('email').value;
		var isValid = true;

		if (name === '') {
			document.getElementById('name_error').innerText = 'Vui lòng nhập tên người nhận hàng';
			isValid = false;
		} else {
			document.getElementById('name_error').innerText = '';
		}

		if (phone === '') {
			document.getElementById('phone_error').innerText = 'Vui lòng nhập số điện thoại';
			isValid = false;
		} else if (phone.length > 10) {
			document.getElementById('phone_error').innerText = 'Số điện thoại không vượt quá 10 số';
			isValid = false;
		} else if (phone.length < 10) {
			document.getElementById('phone_error').innerText = 'Số điện thoại ít nhất là 10 số';
			isValid = false;
		} else {
			document.getElementById('phone_error').innerText = '';
		}


		if (email === '') {
			document.getElementById('email_error').innerText = 'Vui lòng nhập email';
			isValid = false;
		} else {
			document.getElementById('email_error').innerText = '';
		}


		if (!$scope.dataLogin.statusLogin) {
			var province = document.getElementById("id-province");
			var district = document.getElementById("id-district");
			var ward = document.getElementById("wardSelect");
			var addressDetail = document.getElementById("address-detail").value;
			if (province.selectedIndex === 0) {
				document.getElementById('province_error').innerText = 'Vui lòng chọn tỉnh';
				isValid = false;
			} else {
				document.getElementById('province_error').innerText = '';
			}

			if (district.selectedIndex === 0) {
				document.getElementById('district_error').innerText = 'Vui lòng chọn huyện';
				isValid = false;
			} else {
				document.getElementById('district_error').innerText = '';
			}

			if (ward.selectedIndex === 0) {
				document.getElementById('ward_error').innerText = 'Vui lòng chọn xã';
				isValid = false;
			} else {
				document.getElementById('ward_error').innerText = '';
			}

			if (addressDetail === '') {
				document.getElementById('address_detail_error').innerText = 'Vui lòng điền địa chỉ cụ thể';
				isValid = false;
			} else {
				document.getElementById('address_detail_error').innerText = '';
			}
		} else {
			if($scope.listAddress.length === 0 ){
				document.getElementById('address_user').innerText = 'Chưa có địa chỉ. Vui lòng thêm ít nhất 1 địa chỉ nhận hàng';
			}else{

				var isAnyRadioChecked = false;
				var cboAddress = document.querySelectorAll(".radio-address");
				console.log("cboAddress: ",cboAddress);
				for (let i = 0; i < cboAddress.length; i++) {
					console.log("cboAddressn: ",cboAddress[i]);
					console.log("cboAddressc: ",cboAddress[i].checked);
					if (cboAddress[i].checked) {
						console.log('da check');
						isAnyRadioChecked = true;
						break;
					}
				}

				if (isAnyRadioChecked) {
					console.log('daaaa check');
					document.getElementById('list_address_user').innerText = '';
				} else {
					console.log('chua check');
					document.getElementById('list_address_user').innerText = 'Vui lòng chọn địa chỉ';
					isValid = false;
				}

			}

		}
		return isValid;
	}
})




