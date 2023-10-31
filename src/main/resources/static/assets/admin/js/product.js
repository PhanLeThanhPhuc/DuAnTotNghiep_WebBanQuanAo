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
	$scope.closeCollapsibles = function() {
		// Close all collapsible elements
		var collapsibles = document.querySelectorAll('.collapse');
		for (var i = 0; i < collapsibles.length; i++) {
			var collapsible = collapsibles[i];
			if (collapsible.classList.contains('show')) {
				var collapseInstance = new bootstrap.Collapse(collapsible);
				collapseInstance.hide();
			}
		}
	};

	$scope.reset = function() {
		$scope.productSize = [];
		$scope.form = {
			dateUpdate: new Date(),
			dateInsert: new Date(),
			sale: false,
			status: true,
			thumbnail: "cloud-upload.jpg"
		}
		$scope.closeCollapsibles();
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

	$scope.oldDescriptionId = null;
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$scope.oldDescriptionId = $scope.form.description.id;
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

			$scope.selected.forEach(function(productDetail) {
				productdt = { size: productDetail, product: $scope.form }
				$http.post(`/rest/productsDetail`, productdt).then(resp => {
					$scope.productSize.push(resp.data);
				}).catch(error => {
					alert("Lỗi thêm size sản phẩm!");
					console.log("Error", error);
				});
			});
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

		if ($scope.form.description.id == null) {
			$scope.form.description.id = $scope.oldDescriptionId;
		}
		var item = angular.copy($scope.form);
		item.dateUpdate = new Date();
		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.oldDescriptionId = $scope.form.description.id;
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


	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/image', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
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




	$scope.$watch('form.description.id', function(newValue) {
		if (newValue) {
			$scope.descriptionDetail(newValue);
		}

	});

	$scope.descriptionDetail = function(selectedValue) {
		$http.get("/rest/description/" + selectedValue).then(function(response) {
			$scope.form.description = response.data;
		}).catch(function(error) {
			console.log("Error", error);
		});
	};


	$scope.createDescription = function() {
		var item = angular.copy($scope.form.description);
		$http.post(`/rest/description`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.description.push(resp.data);
			$scope.form.description = resp.data;
			alert("Thêm mới mô tả sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới !");
			console.log("Error", error);
		});
	}

	$scope.resetDescription = function() {
		$scope.form.description = {
		}
	}

	$scope.updateDescription = function() {
		var item = angular.copy($scope.form.description);
		$http.put(`/rest/description/${item.id}`, item).then(resp => {
			var index = $scope.description.findIndex(p => p.id == item.id);
			$scope.description[index] = item;

			alert("Cập nhật mô tả sản phẩm công!");
		})
			.catch(error => {
				alert("Lỗi cập nhật !");
				console.log("Error", error);
			});
	}



});