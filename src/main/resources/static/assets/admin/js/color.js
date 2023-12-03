app.controller("color-ctrl", function($scope, $filter, $http) {
	$scope.initialize = function() {
		$http.get("/rest/colors").then(resp => {
			$scope.items = resp.data;
		});
		$scope.reset();
	}


	$scope.reset = function() {
		$scope.form = {
			dateInsert: new Date(),
			status: true,

		}
	}

	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab("show");
	}

	$scope.create = function() {
		if (!validateForm()) {
			return;
		}
	
		var item = angular.copy($scope.form);
		console.log(item);
		$http.post(`/rest/colors`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới màu sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới màu sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.update = function() {
		if (!validateForm()) {
			return;
		}
		var item = angular.copy($scope.form);
		$http.put(`/rest/colors/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật màu sản phẩm thành công!");
		})
			.catch(error => {
				alert("Lỗi cập nhật màu sản phẩm!");
				console.log("Error", error);
			});
	}

	$scope.delete = function(item) {
		if (confirm("Bạn muốn xóa màu sản phẩm này?")) {
			$http.delete(`/rest/colors/${item.id}`).then(resp => {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items.splice(index, 1);
				$scope.reset();
				alert("Xóa màu sản phẩm thành công!");
			}).catch(error => {
				alert("Lỗi màu xóa sản phẩm!");
				console.log("Error", error);
			})
		}
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


	validateForm = () => {

		var isvalid = true;

		var name = document.getElementById("colorName").value
		console.log(name)
		if (name === "") {
			isvalid = false;
			document.getElementById("colorNameError").innerText = "Không bỏ trống";
		} else if (/[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-\d]/.test(name)) {
			
			isvalid = false;
			document.getElementById("colorNameError").innerText = "Tên không được chứa ký tự đặt biệt hoặc số";
		} else {
			document.getElementById("colorNameError").innerText = "";
		}



		return isvalid;

	}


}
);