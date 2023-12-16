app.controller("voucher-ctrl", function($scope, $filter, $http, $timeout) {
		$scope.initialize = function() {
			$http.get("/rest/products").then(resp => {
				$scope.products = resp.data;
			});
			$http.get("/rest/voucher").then(resp => {
				$scope.items = resp.data;
				$scope.items.forEach(item => {
					item.startDate = new Date(item.startDate)
					item.endDate = new Date(item.endDate)
				})
			})
			$scope.reset();
		}

		$scope.reset = function() {
			$scope.form = {
			}
			$('#mySelect').selectpicker('deselectAll');
			$('#mySelect').selectpicker('refresh');
		}

		closePopup = () =>{
			document.getElementById('button-close').click()
		}

		$scope.edit = function(item) {
			$scope.clearValidate()
			$scope.form = angular.copy(item);
			$(".nav-tabs a:eq(0)").tab("show");
			checkboxAllProduct();
		}

		$scope.create = function() {
			if (!validateFormInsert()) {
				return;
			}

			$scope.showSelectedOptions2();
			var item = angular.copy($scope.form);
			$http.post(`/rest/voucher`, item).then(resp => {
				resp.data.startDate = new Date(resp.data.startDate)
				resp.data.endDate = new Date(resp.data.endDate)
				$scope.items.push(resp.data);
				$scope.reset();
				closePopup();
				$scope.messege("Thêm mới mã giảm giá thành công!");
			}).catch(error => {
				alert("Lỗi thêm mới mã giảm giá!");
				console.log("Error", error);
			});

		}

		$scope.update = function() {
			if (!validateFormUpdate()) {
				return;
			}
			$scope.showSelectedOptions2();
			var item = angular.copy($scope.form);
			$http.put(`/rest/voucher/${item.id}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items[index] = item;
				closePopup();
				$scope.messege("Cập nhật mã giảm giá thành công!");
			})
				.catch(error => {
					alert("Lỗi cập nhật mã giảm giá!");
					console.log("Error", error);
				});
		}



		$scope.updateStatus = function(size) {
			var item = angular.copy(size);
			if (item.status == false) {
				item.status = true;
			} else {
				item.status = false;
			}
			$http.put(`/rest/voucher/${item.id}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items[index] = item;

				$scope.messege("Cập nhật trạng thái thành công");
			})
				.catch(error => {
					alert("Lỗi cập nhật!");
					console.log("Error", error);
				});

		}

		$scope.initialize();

		$scope.searchText = {};
		$scope.items = [];
		$scope.pager = {
			page: 0,
			size: 10,
			sortColumn: 'item.id',
			sortDirection: 'desc',
			get filteredItems() {
				var filteredItems = $filter('filter')($scope.items, $scope.searchText);
				if ($scope.pager.sortColumn === 'item.id') {
					filteredItems = $filter('orderBy')(filteredItems, 'item.id', $scope.pager.sortDirection === 'asc');
				}
				return filteredItems;
			},
			get items() {
				if (this.page < 0) {
					this.last();
				}
				if (this.page >= this.count) {
					this.first();
				}
				var filteredItems = this.filteredItems;
				if ($scope.pager.sortColumn) {
					filteredItems = $filter('orderBy')(filteredItems, $scope.pager.sortColumn, $scope.pager.sortDirection == 'desc');
				}
				var start = this.page * this.size;
				return filteredItems.slice(start, start + this.size)
			},
			get count() {
				return Math.ceil(1.0 * this.filteredItems.length / this.size);
			},
			first() {
				this.page = 0;
			},
			last() {
				this.page = this.count - 1;
			},
			next() {
				this.page++;
			},
			prev() {
				this.page--;
			}
		}
		$scope.toggleSort = function(column) {
			if ($scope.pager.sortColumn == column) {
				$scope.pager.sortDirection = ($scope.pager.sortDirection == 'asc') ? 'desc' : 'asc';
			} else {
				$scope.pager.sortColumn = column;
				$scope.pager.sortDirection = 'asc';
			}
		};
		$scope.getSortIconClass = function(column) {
			if ($scope.pager.sortColumn == column) {
				return 'sort-icon ' + ($scope.pager.sortDirection == 'asc' ? 'asc' : 'desc');
			}
		};

		$scope.isSortedBy = function(column) {
			return $scope.pager.sortColumn == column;
		};


		$scope.messege = (mes) => {
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
				beforeShow: function() { }, // will be triggered before the toast is shown
				afterShown: function() { }, // will be triggered after the toat has been shown
				beforeHide: function() { }, // will be triggered before the toast gets hidden
				afterHidden: function() { }  // will be triggered after the toast has been hidden
			});
		}




		$scope.generateRandomString = function() {
			var length = Math.floor(Math.random() * 3) + 6; // Random length between 6 and 8
			var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
			var result = '';

			for (var i = 0; i < length; i++) {
				var randomIndex = Math.floor(Math.random() * characters.length);
				result += characters.charAt(randomIndex);
			}

			$scope.form.voucher = result.toUpperCase();
		};



		$scope.$watch('products', function() {
			$timeout(function() {
				$('#mySelect').selectpicker('refresh');
			});
		});

		$scope.form.productID = "";
		$scope.showSelectedOptions = function() {
			if (!$scope.form.productID == "") {
				var selectedOptionsIds = $scope.form.productID.split(/\s*,\s*/);
				var selectedOptions = [];

				$('#mySelect option').each(function() {
					var optionId = $(this).val();
					if (selectedOptionsIds.includes(optionId)) {
						selectedOptions.push(optionId);
					}
				});
			}



			$('#mySelect').selectpicker('val', selectedOptions);
		}

		$scope.$watch('form.productID', function() {
			$scope.showSelectedOptions();

		});

		$scope.showSelectedOptions2 = function() {
			var selectedOptions = $('#mySelect').val();
			var selectedOptionsText = $('#mySelect option:selected').map(function() {
				return $(this).val();
			}).get().join(', ');

			$scope.form.productID = selectedOptionsText;
		}

		$scope.clearSelect = function() {
			$('#mySelect').selectpicker('deselectAll');
			$('#mySelect').selectpicker('refresh');
		}

		checkboxAllProduct = () => {
			var checkbox = document.getElementById('all-product');
			if (checkbox.checked) {
				console.log('Checkbox is checked');
				$scope.clearSelect();
				document.getElementById('mySelect').disabled = true;
			} else {
				document.getElementById('mySelect').disabled = false;
			}
		}

		validateFormInsert = () => {
			var isvalid = true;

			var voucherform = document.getElementById("voucherForm").value
			var discountpriceform = document.getElementById("discountPriceForm").value
			var quantityform = document.getElementById("quantityForm").value
			var minform = document.getElementById("minForm").value
			// var maxPriceform = document.getElementById("maxPriceForm").value
			var minOrderProductform = document.getElementById("minOrderProductForm").value
			//var myselect = document.getElementById("mySelect").text
			//var selectedTextproductColor = myselect.options[myselect.selectedIndex].text;
			var descriptionform = document.getElementById("descriptionForm").value
			var selectedOptions = $('#mySelect').val();
			var checkbox = document.getElementById('all-product');
			var startDate = document.getElementById('start-date').value
			var endDate = document.getElementById('end-date').value

			//date
			var currentDate = new Date();
			currentDate.setHours(0, 0, 0, 0);
			if (startDate === '') {
				document.getElementById('start-date-error').innerText = 'Không được để trống ngày bắt đầu';
				isvalid = false;
			} else {
				var startDateObj = new Date(startDate);
				startDateObj.setHours(0, 0, 0, 0);

				if (startDateObj < currentDate) {
					document.getElementById('start-date-error').innerText = 'Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại';
					isvalid = false;
				} else {
					document.getElementById('start-date-error').innerText = '';
				}
			}


			if(endDate === ''){
				document.getElementById('end-date-error').innerText = 'Không được để trống ngày kết thúc';
				isvalid = false;
			}else if(new Date(endDate) <= new Date(startDate)){
				document.getElementById('end-date-error').innerText = 'Ngày kết thúc phải lớn hơn ngày bắt đầu';
				isvalid = false;
			}else{
				document.getElementById('end-date-error').innerText = '';
			}

			if (voucherform === "") {
				isvalid = false;
				document.getElementById("voucherFormError").innerText = "Mã giảm giá không bỏ trống";
			} else {
				document.getElementById("voucherFormError").innerText = "";
			}

			if (discountpriceform === "") {
				isvalid = false;
				document.getElementById("discountPriceFormError").innerText = "Số tiền giảm giá không bỏ trống";
			} else if (/[^0-9]/.test(discountpriceform)) {

				isvalid = false;
				document.getElementById("discountPriceFormError").innerText = "Không được chứa ký tự đặt biệt hoặc chữ";
			} else {
				document.getElementById("discountPriceFormError").innerText = "";
			}

			if (quantityform === "") {
				isvalid = false;
				document.getElementById("quantityFormError").innerText = "Số lượng không bỏ trống";
			} else {
				document.getElementById("quantityFormError").innerText = "";
			}

			if (minform === "") {
				isvalid = false;
				document.getElementById("minFormError").innerText = "Số tiền thấp nhất không bỏ trống";
			} else {
				document.getElementById("minFormError").innerText = "";
			}


			if (minOrderProductform === "") {
				isvalid = false;
				document.getElementById("minOrderProductFormError").innerText = "Sản phẩm tối thiểu không bỏ trống";
			} else {
				document.getElementById("minOrderProductFormError").innerText = "";
			}

			if (!checkbox.checked) {
				if (selectedOptions === null) {
					isvalid = false;
					document.getElementById("mySelectError").innerText = "Chọn ít nhất một sản phẩm";
				} else {
					document.getElementById("mySelectError").innerText = "";
				}
			} if (checkbox.checked) {
				document.getElementById("mySelectError").innerText = "";

			}
			return isvalid;
		}
	validateFormUpdate = () => {
		var isvalid = true;

		var voucherform = document.getElementById("voucherForm").value
		var discountpriceform = document.getElementById("discountPriceForm").value
		var quantityform = document.getElementById("quantityForm").value
		var minform = document.getElementById("minForm").value
		// var maxPriceform = document.getElementById("maxPriceForm").value
		var minOrderProductform = document.getElementById("minOrderProductForm").value
		//var myselect = document.getElementById("mySelect").text
		//var selectedTextproductColor = myselect.options[myselect.selectedIndex].text;
		var descriptionform = document.getElementById("descriptionForm").value
		var selectedOptions = $('#mySelect').val();
		var checkbox = document.getElementById('all-product');
		var startDate = document.getElementById('start-date').value
		var endDate = document.getElementById('end-date').value

		//date
		var currentDate = new Date();
		currentDate.setHours(0, 0, 0, 0);
		if (startDate === '') {
			document.getElementById('start-date-error').innerText = 'Không được để trống ngày bắt đầu';
			isvalid = false;
		} else {

				document.getElementById('start-date-error').innerText = '';

		}


		if(endDate === ''){
			document.getElementById('end-date-error').innerText = 'Không được để trống ngày kết thúc';
			isvalid = false;
		}else if(new Date(endDate) <= new Date(startDate)){
			document.getElementById('end-date-error').innerText = 'Ngày kết thúc phải lớn hơn ngày bắt đầu';
			isvalid = false;
		}else{
			document.getElementById('end-date-error').innerText = '';
		}

		if (voucherform === "") {
			isvalid = false;
			document.getElementById("voucherFormError").innerText = "Mã giảm giá không bỏ trống";
		} else {
			document.getElementById("voucherFormError").innerText = "";
		}

		if (discountpriceform === "") {
			isvalid = false;
			document.getElementById("discountPriceFormError").innerText = "Số tiền giảm giá không bỏ trống";
		} else if (/[^0-9]/.test(discountpriceform)) {

			isvalid = false;
			document.getElementById("discountPriceFormError").innerText = "Không được chứa ký tự đặt biệt hoặc chữ";
		} else {
			document.getElementById("discountPriceFormError").innerText = "";
		}

		if (quantityform === "") {
			isvalid = false;
			document.getElementById("quantityFormError").innerText = "Số lượng không bỏ trống";
		} else {
			document.getElementById("quantityFormError").innerText = "";
		}

		if (minform === "") {
			isvalid = false;
			document.getElementById("minFormError").innerText = "Số tiền thấp nhất không bỏ trống";
		} else {
			document.getElementById("minFormError").innerText = "";
		}


		if (minOrderProductform === "") {
			isvalid = false;
			document.getElementById("minOrderProductFormError").innerText = "Sản phẩm tối thiểu không bỏ trống";
		} else {
			document.getElementById("minOrderProductFormError").innerText = "";
		}

		if (!checkbox.checked) {
			if (selectedOptions === null) {
				isvalid = false;
				document.getElementById("mySelectError").innerText = "Chọn ít nhất một sản phẩm";
			} else {
				document.getElementById("mySelectError").innerText = "";
			}
		} if (checkbox.checked) {
			document.getElementById("mySelectError").innerText = "";

		}
		return isvalid;
	}
		$scope.messege = (mes) => {
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

				beforeShow: function() { }, // will be triggered before the toast is shown
				afterShown: function() { }, // will be triggered after the toat has been shown
				beforeHide: function() { }, // will be triggered before the toast gets hidden
				afterHidden: function() { }  // will be triggered after the toast has been hidden
			});
		}
	$scope.clearValidate = () =>{
		document.getElementById('start-date-error').innerText = '';
		document.getElementById('end-date-error').innerText = '';
		document.getElementById("voucherFormError").innerText = "";
		document.getElementById("discountPriceFormError").innerText = "";
		document.getElementById("quantityFormError").innerText = "";
		document.getElementById("minFormError").innerText = "";
		document.getElementById("minOrderProductFormError").innerText = "";
		document.getElementById("mySelectError").innerText = "";

	}
	}
);

