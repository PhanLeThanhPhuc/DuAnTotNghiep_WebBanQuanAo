app.controller("user-ctrl", function($scope, $filter, $http) {
	$scope.initialize = function() {
		$http.get("/rest/users").then(resp => {
			$scope.items = resp.data;
			console.log($scope.items);
			$scope.items.forEach(item => {
				item.date_insert = new Date(item.date_insert)
				item.date_update = new Date(item.date_update)

			})
		});
		$scope.reset();
	}

	$scope.reset = function() {
		$scope.form = {
			dateInsert: new Date(),
			status: true,

		}
	}
	$scope.showPassword = false;
	$scope.inputType = 'password';

	$scope.togglePasswordVisibility = function() {
		$scope.showPassword = !$scope.showPassword;
		$scope.inputType = $scope.showPassword ? 'text' : 'password';
	};


	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab("show");
	}

	$scope.create = function() {
		if (!validateForm()) {
			return;
		}
		var item = angular.copy($scope.form);
		$http.post(`/rest/users`, item).then(resp => {
			resp.data.date_insert = new Date()
			resp.data.date_update = new Date()
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới người dùng thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới!");
			console.log("Error", error);
		});
	}

	$scope.update = function() {
		if (!validateForm()) {
			return;
		}
		var item = angular.copy($scope.form);
		item.date_update = new Date();
		$http.put(`/rest/users/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật người dùng thành công!");

		})
			.catch(error => {
				alert("Lỗi cập nhật!");
				console.log("Error", error);
			});
	}

	$scope.updateStatus = function(user) {
		var item = angular.copy(user);
		item.date_update = new Date();
		if (item.status == false) {
			item.status = true;
		} else {
			item.status = false;
		}

		$http.put(`/rest/users/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.messege("Cập nhật trạng thái thành công");
		})
			.catch(error => {
				alert("Lỗi cập nhật!");
				console.log("Error", error);
			});

	}

	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('uploadfile', files[0]);
		$http.post('/rest/upload', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.form.image = resp.data.image;
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
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



	validateForm = () => {
		var isvalid = true;

		var fullNameform = document.getElementById("fullNameForm").value;
		var emailform = document.getElementById("emailForm").value;
		var phoneform = document.getElementById("phoneForm").value;
		var genderCheck = document.getElementsByName('gender');

		console.log(fullNameform);
		console.log(emailform);
		console.log(phoneform);

		if (fullNameform === "") {
			isvalid = false;
			document.getElementById("fullNameFormError").innerText = "Tên người dùng không bỏ trống";
		} else if (/[^a-zA-Zàáảãạăắằẳẵặâấầẩẫậèéẻẽẹêếềểễệđìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳỹỷỵ0-9\s]/ug.test(fullNameform)) {
			isvalid = false;
			document.getElementById("fullNameFormError").innerText = "Tên người dùng không được chứa ký tự đặt biệt";
		} else {
			document.getElementById("fullNameFormError").innerText = "";
		}


		if (emailform === "") {
			isvalid = false;
			document.getElementById("emailFormError").innerText = "Email không bỏ trống";
		} else if (!/^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+\.[A-Za-z0-9.]+$/.test(emailform)) {
			isvalid = false;
			document.getElementById("emailFormError").innerText = "Email không đúng định dạng";
		} else {
			document.getElementById("emailFormError").innerText = "";
		}


		if (phoneform === "") {
			isvalid = false;
			document.getElementById("phoneFormError").innerText = "Số điện thoại không bỏ trống";
		} else if (/[^\d]/.test(phoneform)) {
			isvalid = false;
			document.getElementById("phoneFormError").innerText = "Số điện thoại không được chứa ký tự đặc biệt hoặc chữ";
		} else if (/[^0-9]/.test(phoneform) || phoneform.length !== 10) {
			isvalid = false;
			document.getElementById("phoneFormError").innerText = "Số điện thoại không hợp lệ ";
		} else {
			document.getElementById("phoneFormError").innerText = "";
		}

		var isGenderSelected = false;
		for (var i = 0; i < genderCheck.length; i++) {
			if (genderCheck[i].checked) {
				isGenderSelected = true;
				break;
			}
		}

		if (!isGenderSelected) {
			isvalid = false;
			document.getElementById('genderCheckError').innerText = 'Vui lòng chọn giới tính.';
		} else {
			document.getElementById('genderCheckError').innerText = '';
		}

		return isvalid;
	}


}


);