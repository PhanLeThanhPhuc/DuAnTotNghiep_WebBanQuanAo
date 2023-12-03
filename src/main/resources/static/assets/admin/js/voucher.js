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

	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab("show");
		checkboxAllProduct();
	}

	$scope.create = function() {
		if (!validateForm()) {
			return;
		}
		
			document.getElementById("buttonclose").click()
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.post(`/rest/voucher`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới mã giảm giá thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới mã giảm giá!");
			console.log("Error", error);
		});

	}

	$scope.update = function() {
		if (!validateForm()) {
			return;
		}
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.put(`/rest/voucher/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật mã giảm giá thành công!");
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
		sortColumn: '',
		sortDirection: '',
		get filteredItems() {
			return $filter('filter')($scope.items, $scope.searchText);
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

	validateForm = () => {
		var isvalid = true;

		var voucherform = document.getElementById("voucherForm").value
		var discountpriceform = document.getElementById("discountPriceForm").value
		var quantityform = document.getElementById("quantityForm").value
		var minform = document.getElementById("minForm").value
		var maxPriceform = document.getElementById("maxPriceForm").value
		var minOrderProductform = document.getElementById("minOrderProductForm").value
		//var myselect = document.getElementById("mySelect").text
		//var selectedTextproductColor = myselect.options[myselect.selectedIndex].text;
		var descriptionform = document.getElementById("descriptionForm").value
		var selectedOptions = $('#mySelect').val();
		var checkbox = document.getElementById('all-product');


		console.log(voucherform)
		console.log(discountpriceform)
		console.log(minform)
		console.log(quantityform)
		console.log(maxPriceform)
		console.log(minOrderProductform)
		console.log(selectedOptions, ",")
		console.log(descriptionform)

		if (voucherform === "") {
			isvalid = false;
			document.getElementById("voucherFormError").innerText = "Không bỏ trống";
		} else if (/[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-\d]/.test(voucherform)) {

			isvalid = false;
			document.getElementById("voucherFormError").innerText = "Tên không được chứa ký tự đặt biệt hoặc số";
		} else {
			document.getElementById("voucherFormError").innerText = "";
		}

		if (discountpriceform === "") {
			isvalid = false;
			document.getElementById("discountPriceFormError").innerText = "Không bỏ trống";
		} else if (/[^0-9]/.test(discountpriceform)) {

			isvalid = false;
			document.getElementById("discountPriceFormError").innerText = "Không được chứa ký tự đặt biệt hoặc chữ";
		} else {
			document.getElementById("discountPriceFormError").innerText = "";
		}

		if (quantityform === "") {
			isvalid = false;
			document.getElementById("quantityFormError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("quantityFormError").innerText = "";
		}

		if (minform === "") {
			isvalid = false;
			document.getElementById("minFormError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("minFormError").innerText = "";
		}

		if (maxPriceform === "") {
			isvalid = false;
			document.getElementById("maxPriceFormError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("maxPriceFormError").innerText = "";
		}

		if (minOrderProductform === "") {
			isvalid = false;
			document.getElementById("minOrderProductFormError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("minOrderProductFormError").innerText = "";
		}

		if (!checkbox.checked) {
			if (selectedOptions === null) {
				isvalid = false;
				document.getElementById("mySelectError").innerText = "Không bỏ trống";
			} else {
				document.getElementById("mySelectError").innerText = "";
			}
		} if (checkbox.checked) {
			document.getElementById("mySelectError").innerText = "";

		}
		return isvalid;

	}

}
);

