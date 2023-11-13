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
		$('#mySelect').selectpicker('deselectAll');
		$('#mySelect').selectpicker('refresh');
		$scope.form = angular.copy(item);
	}

	$scope.create = function() {
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.post(`/rest/discount`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.update = function() {
		$scope.showSelectedOptions2();
		var item = angular.copy($scope.form);
		$http.put(`/rest/discount/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật sản phẩm thành công!");
		})
			.catch(error => {
				alert("Lỗi cập nhật sản phẩm!");
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







	$scope.$watch('products', function() {
		$timeout(function() {
			$('#mySelect').selectpicker('refresh');
		});
	});


	$scope.showSelectedOptions = function() {
		if ($scope.form.product_id) {
			var selectedOptionsIds = $scope.form.product_id.split(/\s*,\s*/);
		}
		var selectedOptions = [];

		$('#mySelect option').each(function() {
			var optionId = $(this).val();
			if (selectedOptionsIds.includes(optionId)) {
				selectedOptions.push(optionId);
			}
		});

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
	

}
);

