app.controller("order-ctrl", function($scope, $filter, $http) {

	$scope.initialize = () => {
		$scope.listOrders = [];

		const eventSource = new EventSource("/sse", { withCredentials: true, cache: "no-store" });

		eventSource.addEventListener("order-update", function (event) {
			const order = JSON.parse(event.data);
			console.log("Đơn hàng đã cập nhật:", order);
			$scope.$apply(function () {
				$scope.listOrders.push(order);
				countOrder();
			});
		});

		eventSource.onerror = function (error) {
			console.error("EventSource failed:", error);
			eventSource.close();
		};

		$http.get("/rest/orders").then(resp => {
			$scope.listOrders = resp.data;
			console.log("Danh sách đơn hàng", $scope.listOrders);
			countOrder();
		});
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
				$scope.message(`Xác nhận đơn hàng ${orderId} thành công`)
				countOrder();
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
				countOrder();
			}
		});
	}

	$scope.delivering = (orderId) =>{
		$http.get(`/rest/orders/update-status?idOrder=${orderId}&statusOrder=4`).then(resp => {
			if (resp.status === 200) {
				var index = $scope.listOrders.findIndex(p => p.id == orderId);
				$scope.listOrders[index] = resp.data;
				console.log("ssss", resp.data);
				$scope.message(`Đang giao đơn hàng ${orderId}`)
				countOrder();
			}
		});
	}

	$scope.successOrder = (orderId) =>{
		$http.get(`/rest/orders/update-status?idOrder=${orderId}&statusOrder=5`).then(resp => {
			if (resp.status === 200) {
				var index = $scope.listOrders.findIndex(p => p.id == orderId);
				$scope.listOrders[index] = resp.data;
				console.log("ssss", resp.data);
				$scope.message(`Giao thành công đơn hàng ${orderId}`)
				countOrder();
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

	filterStatusOrder = async () =>{
		var status = document.getElementById("cbb-status").value;
		if(status === 'all'){
			await $scope.initialize();
			$scope.$apply();
		}else {
			await $scope.initialize();
			var listFilterStatus = [];
			listFilterStatus = $scope.listOrders.filter(o => {
				return o.status == status;
			});
			$scope.listOrders = []
			$scope.listOrders = angular.copy(listFilterStatus);
			$scope.$apply();
		}
	}

	searchIdOrder = () => {
		var valueSearch = document.getElementById("search").value;

		var listOrdersCopy = $scope.listOrders;

		console.log("Original array:", $scope.listOrders);

		if (valueSearch) {
			var foundOrder = $scope.listOrders.find(order => order.id === parseInt(valueSearch));

			console.log("Found order:", foundOrder);

			if (foundOrder) {
				$scope.listOrders = [foundOrder];
				$scope.$apply();
				$scope.listOrders = listOrdersCopy;
			} else {
				$scope.listOrders = [];
				$scope.$apply();
				$scope.listOrders = listOrdersCopy;
			}
		}else{
			$scope.listOrders = listOrdersCopy;
			$scope.$apply();
		}
	};

	buttonFilterByDate = () =>{

		var listOrdersCopy = $scope.listOrders;

		var startDate = document.getElementById("start-date").value;
		var endDate = document.getElementById("end-date").value;

		$scope.listOrders = listOrdersCopy.filter( order =>{
			return $scope.formatDate(startDate) <= $scope.formatDate(order.orderDate) && $scope.formatDate(order.orderDate) <= $scope.formatDate(endDate)
		})
		countOrder();
		$scope.$apply();

		$scope.listOrders = []
		$scope.listOrders = listOrdersCopy;
	}

	countOrder = () =>{
		//allorder
		$scope.quantityAllOrder = $scope.listOrders.length;

		$scope.pendingConfirmation= 0;
		$scope.pendingPickup = 0;
		$scope.inTransit = 0;
		$scope.delivered = 0;
		$scope.canceled = 0;

		$scope.listOrders.forEach(order => {
			var status = order.status;
			switch (status) {
				case 0:
					$scope.pendingConfirmation++;
					break;
				case 1:
					$scope.pendingPickup++;
					break;
				case 4:
					$scope.inTransit++;
					break;
				case 5:
					$scope.delivered++;
					break;
				case 2:
					$scope.canceled++;
					break;
			}
		});
		// $scope.$apply();
	}

	$scope.formatDate = function(date) {
		return $filter('date')(date, 'dd/MM/yyyy');
	};

});