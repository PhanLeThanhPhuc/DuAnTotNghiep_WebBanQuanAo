app.controller("product-ctrl", function($scope, $http) {
	$scope.initialize = function() {
		$http.get("/rest/categorydetail").then(resp => {
			$scope.categories = resp.data;
		})
		$http.get("/rest/colors").then(resp => {
			$scope.colors = resp.data;
		})
		$http.get("/rest/description").then(resp => {
			$scope.description = resp.data;
		})
		$http.get("/rest/sizes").then(resp => {
			$scope.sizes = resp.data;
		})


		$http.get("/rest/products").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.dateInsert = new Date(item.dateInsert)
			})

			$scope.items.forEach(item => {
				item.dateUpdate = new Date(item.dateUpdate)
			})
		});
		$scope.reset();
	}

	$scope.reset = function() {
		$scope.productSize = [];
		$scope.form = {
			dateUpdate: new Date(),
			dateInsert: new Date(),
			sale: false,
			status: true,
			thumbnail: "cloud-upload.jpg"
		}
	}

	$scope.selected = [];
	$scope.test = function() {
		$scope.selected = [];
	}

	$scope.exist = function(size) {
		return $scope.selected.indexOf(size) > -1;
	}

	$scope.toggleSelection = function(item) {
		var idx = $scope.selected.indexOf(item);
		if (idx > -1) {
			$scope.selected.splice(idx, 1);
		}
		else {
			$scope.selected.push(item);
		}
	}
	$scope.size_of = function(form, size) {
		if ($scope.productSize) {
			return $scope.productSize.find(ur => ur.size.id == size.id);
		}
	}
	$scope.addsize = function(id) {
		$scope.selected.forEach(function(productDetail) {
			productdt = { size: productDetail, product: id }
			$http.post(`/rest/productsDetail`, productdt).then(resp => {
				$scope.productSize.push(resp.data);
			}).catch(error => {
				alert("Lỗi thêm size sản phẩm!");
				console.log("Error", error);
			});
		});
		$scope.selected = [];

	}

	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab("show");
		$http.get(`/rest/productsDetail/${item.id}`).then(resp => {
			$scope.productSize = resp.data;
		})
		$scope.selected = [];
	}



	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`, item).then(resp => {
			$scope.items.push(resp.data);
			$scope.form = angular.copy(resp.data);
			alert("Thêm mới sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}
	$scope.update = function() {
		var size = angular.copy($scope.productSize);
		size.forEach(function(size) {
			$http.put(`/rest/productsDetail/${size.id}`, size).then(resp => {
			}).catch(error => {
				alert("Lỗi cập nhật size sản phẩm!");
				console.log("Error", error);
			});
		});
		var item = angular.copy($scope.form);
		item.dateUpdate = new Date();
		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật sản phẩm thành công!!!!");
		}).catch(error => {
			alert("Lỗi cập nhật sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.delete = function(item) {
		if (confirm("Bạn muốn xóa sản phẩm này?")) {
			$http.delete(`/rest/products/${item.id}`).then(resp => {
				var index = $scope.items.findIndex(p => p.id == item.id);
				$scope.items.splice(index, 1);
				$scope.reset();
				alert("Xóa sản phẩm thành công!");
			}).catch(error => {
				alert("Lỗi xóa sản phẩm!");
				console.log("Error", error);
			})
		}
	}


		$scope.imageChanged = function(files){
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/image', data, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
        }).then(resp => {
			$scope.form.thumbnail = resp.data.name;
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}

	$scope.initialize();

	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			if (this.page < 0) {
				this.last();
			}
			if (this.page >= this.count) {
				this.first();
			}
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size)
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
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
});