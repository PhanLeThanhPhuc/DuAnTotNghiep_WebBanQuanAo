app.controller("order-ctrl", function($scope, $filter, $http) {

	$scope.initialize = () => {

		$http.get("/rest/orders").then(resp => {
			$scope.listOrders = resp.data;
			// console.log("list order",$scope.listOrders);
		});
		// $scope.reset();
	}

	$scope.initialize();

	$scope.editOrder = (order) => {
		$scope.objectOrder = order;
		console.log(" order", $scope.objectOrder);
	}

	$scope.confirmOrder = (orderId) => {
		$http.get(`/rest/orderGhn?orderId=${orderId}`).then(resp => {
			if (resp.status === 200) {
				var index = $scope.listOrders.findIndex(p => p.id == orderId);
				$scope.listOrders[index] = resp.data;
				console.log("Data xac nhạn: ", resp.data);
				$scope.message(`Xác nhận đơn hàng ${orderId} thành công`)
			}
		});
	}

	$scope.cancelOrder = (orderId) => {
		$http.get(`/rest/cancel-order?orderId=${orderId}`).then(resp => {
			if (resp.status === 200) {
				var index = $scope.listOrders.findIndex(p => p.id == orderId);
				$scope.listOrders[index] = resp.data.order;
				console.log("ssss", resp.data);
				$scope.message(`Hủy đơn hàng ${orderId} thành công`)
			}
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




	$scope.searchText = {};
	$scope.listOrders = [];
	$scope.pager = {
		page: 0,
		size: 10,
		sortColumn: 'item.id',
		sortDirection: 'desc',
		get filteredlistOrders() {
			var filteredlistOrders = $filter('filter')($scope.listOrders, $scope.searchText);
			if ($scope.pager.sortColumn === 'item.id') {
				filteredlistOrders = $filter('orderBy')(filteredlistOrders, 'item.id', $scope.pager.sortDirection === 'asc');
			}
			return filteredlistOrders;
		},
		get listOrders() {
			if (this.page < 0) {
				this.last();
			}
			if (this.page >= this.count) {
				this.first();
			}
			var filteredlistOrders = this.filteredlistOrders;
			if ($scope.pager.sortColumn) {
				filteredlistOrders = $filter('orderBy')(filteredlistOrders, $scope.pager.sortColumn, $scope.pager.sortDirection === 'desc');
			}
			var start = this.page * this.size;
			return filteredlistOrders.slice(start, start + this.size)
		},
		get count() {
			return Math.ceil(1.0 * this.filteredlistOrders.length / this.size);
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
		if ($scope.pager.sortColumn === column) {
			$scope.pager.sortDirection = $scope.pager.sortDirection === 'asc' ? 'desc' : 'asc';
		} else {
			$scope.pager.sortColumn = column;
			$scope.pager.sortDirection = 'asc';
		}
	};
	$scope.getSortIconClass = function(column) {
		if ($scope.pager.sortColumn === column) {
			return 'sort-icon ' + ($scope.pager.sortDirection === 'asc' ? 'asc' : 'desc');
		}
	};

	$scope.isSortedBy = function(column) {
		return $scope.pager.sortColumn === column;
	};

});