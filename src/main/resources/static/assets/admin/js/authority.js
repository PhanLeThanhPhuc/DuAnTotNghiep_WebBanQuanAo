app.controller("authority-ctrl", function($scope, $http,$filter, $location){
	$scope.initialize = function(){
		// load all roles
		$http.get("/rest/roles").then(resp => {
	    	$scope.roles = resp.data;
	    	console.log($scope.roles)
	    })
		// load staffs and directors (administrators)
		$http.get("/rest/users").then(resp => {
	    	$scope.items = resp.data;
	    })
	    // load authorites of staffs and directors
		$http.get("/rest/authorities").then(resp => {
	    	$scope.authorities = resp.data;
	    }).catch(error => {
	    	$location.path("/unauthorized");
	    })
	}

	$scope.authority_of = function (acc, role){
		if($scope.authorities){
			return $scope.authorities.find(ur => ur.user.id == acc.id && ur.role.id==role.id);
		}
	}
	
	$scope.authority_changed = function(acc, role){
		var authority = $scope.authority_of(acc, role);
		if(authority){ // đã cấp quyền => thu hồi quyền (xóa)
			$scope.revoke_authority(authority);
		}
		else { // chưa được cấp quyền => cấp quyền (thêm mới)
			authority = {user: acc, role: role};
			$scope.grant_authority(authority);
		}
	}
	
	// Thêm mới authority
	$scope.grant_authority = function(authority){
		$http.post(`/rest/authorities`, authority).then(resp => {
			$scope.authorities.push(resp.data)
			$scope.messege("Cấp quyền thành công");
		}).catch(error => {
			alert("Cấp quyền sử dụng thất bại");
			console.log("Error", error);
		})
	}
	// Xóa authority
	$scope.revoke_authority = function(authority){
		$http.delete(`/rest/authorities/${authority.id}`).then(resp => {
			var index = $scope.authorities.findIndex(a => a.id == authority.id);
			$scope.authorities.splice(index, 1);
			$scope.messege("Thu hồi quyền thành công");
		}).catch(error => {
			alert("Thu hồi quyền sử dụng thất bại");
			console.log("Error", error);
		})
	}
	$scope.initialize();
	$scope.messege = (mes) =>{
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
	    beforeShow: function () {}, // will be triggered before the toast is shown
	    afterShown: function () {}, // will be triggered after the toat has been shown
	    beforeHide: function () {}, // will be triggered before the toast gets hidden
	    afterHidden: function () {}  // will be triggered after the toast has been hidden
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
});