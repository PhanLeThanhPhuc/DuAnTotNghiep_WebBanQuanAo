app.controller("review-ctrl", function($scope, $filter, $http) {
	$scope.initialize = function() {
		$http.get("/rest/products").then(resp => {
			$scope.products = resp.data;
		});
		$scope.reset();
	}
	$scope.searchText = {};
	$scope.handleClick = function(value) {
		$scope.searchText.rating = value;
	};
	$scope.reset = function() {
		$scope.form = {
		}
	}
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	}
	$scope.delete = function(item) {
		console.log(item.id)
		if (confirm("Bạn muốn xóa đánh giá này?")) {
			$http.delete(`/rest/review/${item.id}`).then(resp => {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items.splice(index, 1);
				$scope.reset();
				alert("Xóa đánh giá thành công!");
			}).catch(error => {
				alert("Lỗi xóa đánh giá!");
				console.log("Error", error);
			})
		}
	}
	$scope.initialize();
	$scope.getFilteredData = function(rating) {
		return $scope.items.filter(function(item) {
			return item.rating == rating;
		});
	};

	$scope.calculatePercentage = function(rating) {
		var totalCount = $scope.items.length;
		var matchingCount = 0;
		for (var i = 0; i < totalCount; i++) {
			if ($scope.items[i].rating == rating) {
				matchingCount++;
			}
		}
		var percentage = (matchingCount / totalCount) * 100;
		percentages = percentage.toFixed(1);
		if (percentages == 'NaN') {
			percentages = 0;
		}
		return percentages;
	};

	$scope.$watch('items', function() {
		$scope.calculateAverage();
	}, true);
	$scope.calculateAverage = function() {
		var sum = 0;
		for (var i = 0; i < $scope.items.length; i++) {
			sum += $scope.items[i].rating;
		}
		$scope.average = sum / $scope.items.length;
		$scope.average = $scope.average.toFixed(1);
		if ($scope.average == 'NaN') {
			$scope.average = 0;
		}
	};
	$scope.$watch('product.id', function(newValue) {
		var id = 1;
		if (newValue != 'all') {
			$http.get("/rest/review/product/" + newValue).then(resp => {
				$scope.items = resp.data;
				$scope.items.forEach(item => {
					item.dateReview = new Date(item.dateReview)
				})
			});
			$scope.reset();
		} else {
			$http.get("/rest/review").then(resp => {
				$scope.items = resp.data;
				$scope.items.forEach(item => {
					item.dateReview = new Date(item.dateReview)
				})
			});
			$scope.reset();
		}
	});
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


	/* star */
 $scope.stars = Array.from({ length: 5 }, (_, index) => index + 1);

});