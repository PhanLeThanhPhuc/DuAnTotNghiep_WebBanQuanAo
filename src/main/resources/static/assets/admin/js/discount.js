app.controller("discount-ctrl", function($scope, $filter, $http, $timeout) {
	$scope.initialize = function() {
		$http.get("/rest/products").then(resp => {
			$scope.products = resp.data;
		});
		$http.get("/rest/discount").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.startdate = new Date(item.startdate)
				item.enddate = new Date(item.enddate)
			})
			console.log($scope.items)
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
		$scope.clearValidateForm();
		$scope.form = angular.copy(item);
		$scope.idDiscount = item.id
	}

	closePopup = () =>{
		document.getElementById('button-close').click()
	}

	$scope.create = function() {
		if (!validateForm()) {
			return;
		}
		// document.getElementById("buttonclose").click()
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.post(`/rest/discount`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.items.push(resp.data);
			// console.log("data",$scope.items)
			$scope.reset();
			$scope.messege("Thêm đợt giảm giá thành công!");
			closePopup();
			$scope.initialize();
		}).catch(error => {
			alert("Lỗi thêm mới đợt giảm giá!");
			console.log("Error", error);
		});
	}

	$scope.update = function() {
		if (!validateFormUpdate()) {
			return;
		}
		closePopup();
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.put(`/rest/discount/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.messege("Cập nhật đợt giảm giá thành công!");
			closePopup();
		})
		.catch(error => {
			alert("Lỗi cập nhật đợt giảm giá!");
			console.log("Error", error);
		});


	}

	$scope.updateStatus = function(size) {
		var item = angular.copy(size);
		if (item.active == false) {
			item.active = true;
		} else {
			item.active = false;
		}
		$http.put(`/rest/discount/${item.id}`, item).then(resp => {
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

	$scope.$watch('products', function() {
		$timeout(function() {
			$('#mySelect').selectpicker('refresh');
		});
	});

	$scope.form.product_id = "";
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

	$scope.$watch('form.product_id', function() {
		$scope.showSelectedOptions();

	});

	$scope.showSelectedOptions2 = function() {
		var selectedOptions = $('#mySelect').val();
		var selectedOptionsText = $('#mySelect option:selected').map(function() {
			return $(this).val();
		}).get().join(', ');

		$scope.form.product_id = selectedOptionsText;
	}

	$scope.clearSelect = function() {
		$('#mySelect').selectpicker('deselectAll');
		$('#mySelect').selectpicker('refresh');
	}


	validateForm = () => {

		var isvalid = true;

		var nameform = document.getElementById("nameForm").value
		var discountform = document.getElementById("discountForm").value
		var selectedOptions = $('#mySelect').val();
		var checkbox = document.getElementById('allproductform');
		var startDate = document.getElementById('start-date').value
		var endDate = document.getElementById('end-date').value


		if (nameform === "") {
			isvalid = false;
			document.getElementById("nameFormError").innerText = "Tên mã giảm giá không bỏ trống";
		} else if (/[!@#$%^&*(),.?":{}|<>]/.test(nameform)) {
			isvalid = false;
			document.getElementById("nameFormError").innerText = "Tên mã giảm giá không được chứa ký tự đặt biệt";
		} else {
			document.getElementById("nameFormError").innerText = "";
		}

		///validate date
		// var
		var currentDate = new Date();
		var index = $scope.items.length -1;
		console.log(index)
		if(startDate === ''){
			console.log("test het1")
			document.getElementById('start-date-error').innerText = 'Không được để trống ngày bắt đầu';
			isvalid = false;
		}else if(new Date(startDate) < currentDate){
			console.log("test het2")
			document.getElementById('start-date-error').innerText = 'Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại ';
			isvalid = false;
		}else if(index !== -1){
			console.log("test het3")
			console.log(new Date(startDate))
			console.log($scope.items[index].enddate)
			if(new Date(startDate) <= $scope.items[index].enddate){
				console.log("START DATE: ", new Date(startDate))
				console.log("END DATE: ", $scope.items[index].enddate)
				document.getElementById('start-date-error').innerText = 'Ngày bắt đầu phải lớn hơn ngày kết thúc của đợt giảm giá trước đó. ' +
					'Ngày kết thúc đợt giảm giá trước đó là '+$filter('date')($scope.items[index].enddate, 'dd/MM/yyyy HH:mm');
				isvalid = false;
			}else{
				document.getElementById('start-date-error').innerText = '';
			}
		}else{
			console.log("test het")
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



		if (discountform === "") {
			isvalid = false;
			document.getElementById("discountFormError").innerText = "Số phần trăm giảm không bỏ trống";
		} else if (/[^0-9]/.test(discountform)) {
			isvalid = false;
			document.getElementById("discountFormError").innerText = "Số phần trăm giảm không được chứa ký tự đặt biệt hoặc chữ";
		} else {
			var discountPercentage = parseInt(discountform, 10);

			if (discountPercentage < 1 || discountPercentage > 100) {
				isvalid = false;
				document.getElementById("discountFormError").innerText = "Số phần trăm giảm phải nằm trong khoảng từ 1 đến 100";
			} else {
				document.getElementById("discountFormError").innerText = "";
			}
		}
		if (!checkbox.checked) {
			if (selectedOptions === null) {
				isvalid = false;
				document.getElementById("mySelectError").innerText = "Bắt buộc chọn ít nhất 1 sản phẩm";
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

		var nameform = document.getElementById("nameForm").value
		var discountform = document.getElementById("discountForm").value
		var selectedOptions = $('#mySelect').val();
		var checkbox = document.getElementById('allproductform');
		var startDate = document.getElementById('start-date').value
		var endDate = document.getElementById('end-date').value


		if (nameform === "") {
			isvalid = false;
			document.getElementById("nameFormError").innerText = "Tên mã giảm giá không bỏ trống";
		} else if (/[!@#$%^&*(),.?":{}|<>]/.test(nameform)) {
			isvalid = false;
			document.getElementById("nameFormError").innerText = "Tên mã giảm giá không được chứa ký tự đặt biệt";
		} else {
			document.getElementById("nameFormError").innerText = "";
		}

		///validate date
		// var
		var currentDate = new Date();
		var indexStartDate = $scope.items.findIndex(item => item.id == $scope.idDiscount)
		indexStartDate = indexStartDate -1;
		console.log("INDEX", indexStartDate);
		if(startDate === ''){
			document.getElementById('start-date-error').innerText = 'Không được để trống ngày bắt đầu';
			isvalid = false;
		}else if(indexStartDate !== -1){
			if(new Date(startDate) <= $scope.items[indexStartDate].enddate){
				document.getElementById('start-date-error').innerText = 'Ngày bắt đầu phải lớn hơn ngày kết thúc của đợt giảm giá trước đó. ' +
					'Ngày kết thúc đợt giảm giá trước' +
					'đó là '+$filter('date')($scope.items[indexStartDate].enddate, 'dd/MM/yyyy HH:mm');
				isvalid = false;
			}
		}else{
			document.getElementById('start-date-error').innerText = '';
		}

		var indexEndDate = $scope.items.findIndex(item => item.id == $scope.idDiscount)
		indexEndDate = indexEndDate + 1;
		console.log('mảng',$scope.items[indexEndDate]);
		if(endDate === ''){
			document.getElementById('end-date-error').innerText = 'Không được để trống ngày kết thúc';
			isvalid = false;
		}else if(new Date(endDate) <= new Date(startDate)){
			document.getElementById('end-date-error').innerText = 'Ngày kết thúc phải lớn hơn ngày bắt đầu';
			isvalid = false;
		}else if( typeof $scope.items[indexEndDate] !== 'undefined'){
			if(new Date(endDate) >= $scope.items[indexEndDate].startdate){
				document.getElementById('end-date-error').innerText = 'Ngày kết thúc phải bé hơn ngày bắt đầu của đợt giảm giá kế tiếp. ' +
					'Ngày bắt đầu giảm giá kế tiếp là '+$filter('date')($scope.items[indexEndDate].startdate, 'dd/MM/yyyy HH:mm');
				isvalid = false;
			}else{
				document.getElementById('end-date-error').innerText = '';
			}
		}else{
			document.getElementById('end-date-error').innerText = '';
		}



		if (discountform === "") {
			isvalid = false;
			document.getElementById("discountFormError").innerText = "Số phần trăm giảm không bỏ trống";
		} else if (/[^0-9]/.test(discountform)) {
			isvalid = false;
			document.getElementById("discountFormError").innerText = "Số phần trăm giảm không được chứa ký tự đặt biệt hoặc chữ";
		} else {
			var discountPercentage = parseInt(discountform, 10);

			if (discountPercentage < 1 || discountPercentage > 100) {
				isvalid = false;
				document.getElementById("discountFormError").innerText = "Số phần trăm giảm phải nằm trong khoảng từ 1 đến 100";
			} else {
				document.getElementById("discountFormError").innerText = "";
			}
		}
		if (!checkbox.checked) {
			if (selectedOptions === null) {
				isvalid = false;
				document.getElementById("mySelectError").innerText = "Bắt buộc chọn ít nhất 1 sản phẩm";
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

	$scope.clearValidateForm = () =>{
		document.getElementById("nameFormError").innerText = "";
		document.getElementById('start-date-error').innerText = '';
		document.getElementById('end-date-error').innerText = '';
		document.getElementById("discountFormError").innerText = "";
		document.getElementById("mySelectError").innerText = "";
	}
}
);

