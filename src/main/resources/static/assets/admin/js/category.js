app.controller("category-ctrl", function($scope, $filter, $http) {
	$scope.initialize = function() {
		$http.get("/rest/categories").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.dateInsert = new Date(item.dateInsert);
				item.dateUpdate = new Date(item.dateUpdate);
			})
		});

		$scope.reset();
		$scope.resetCategoryDetail();
	}


	$scope.resetCategoryDetail = function() {
		$scope.formCategoryDetail = {
			name: "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.reset = function() {
		$scope.titleModel = "Thêm danh mục";
		$scope.formCategory = {
			name: "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.editCategory = function(item) {
		$scope.itemCategory = item;
		$scope.titleModel = "Cập nhật danh mục";
		$scope.formCategory = angular.copy(item);
	}



	$scope.createCategory = function() {
		var item = angular.copy($scope.formCategory);
		$http.post(`/rest/categories`, item).then(resp => {
			if (resp.status == 200) {
				$scope.items.push(resp.data);
				$scope.message("Thêm mới danh mục thành công!");
			}
			$scope.reset();
		}).catch(error => {
			alert("Lỗi thêm mới danh mục!");
			console.log("Error", error);
		});
	}




	$scope.updateCategory = function() {
		var item = angular.copy($scope.formCategory);
		item.dateUpdate = new Date();
		$http.put(`/rest/categories/${item.id}`, item).then(resp => {
			if (resp.status == 200) {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items[index] = item;
				$scope.message("Cập nhật danh mục thành công!");
			}
			$scope.reset();
		})
			.catch(error => {
				alert("Lỗi cập nhật danh mục!");
				console.log("Error", error);
			});
	}


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



	$scope.resetCategoryDetail = function() {
		$scope.formCategoryDetail = {
			name: "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.addCategoryDetail = function(item) {
		$scope.resetCategoryDetail();
		$scope.titleModel = "Thêm chi tiết danh mục";
		$scope.Category = item
	}

	$scope.createCategoryDetail = function() {
		$scope.formCategoryDetail.category = $scope.Category
		var item = angular.copy($scope.formCategoryDetail);
		item.dateUpdate = new Date();
		$http.post(`/rest/categorydetail`, item).then(resp => {
			if (resp.status == 200) {
				$scope.items.push(resp.data);
				$scope.message("Thêm mới danh mục thành công!");
				}
			$scope.reset();
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}


	$scope.editCategoryDetail = function(category, categorydetail) {
		$scope.titleModel = "Cập nhật danh mục";
		$scope.Category = category
		$scope.formCategoryDetail = categorydetail
	}

	$scope.updateCategoryDetail = function()  {
		
		var item = angular.copy($scope.formCategoryDetail);
		item.dateUpdate = new Date();
		$http.put(`/rest/categorydetail/${item.id}`, item).then(resp => {
			if (resp.status == 200) {					
				$scope.message("Cập nhật danh mục thành công!");
			}
			$scope.reset();
		})
			.catch(error => {
				alert("Lỗi cập nhật danh mục!");
				console.log("Error", error);
			});

	}




	$scope.message = (mes) => {
		$.toast({
			text: mes,
			heading: 'Note',
			icon: 'success',
			showHideTransition: 'fade',
			allowToastClose: true,
			hideAfter: 3000,
			stack: 5,
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

	$scope.initialize();



	$scope.toggleCollapse = function(item) {
		item.showDetails = !item.showDetails;
	};
}
);