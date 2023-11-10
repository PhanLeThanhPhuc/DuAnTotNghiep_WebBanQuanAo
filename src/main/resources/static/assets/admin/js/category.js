app.controller("category-ctrl", function($scope, $http){
	$scope.initialize = function(){
		$http.get("/rest/categories").then(resp => {
			$scope.listCategory = resp.data;
			$scope.listCategory.forEach(item => {
				item.dateInsert = new Date(item.dateInsert);
				item.dateUpdate = new Date(item.dateUpdate);
			})
		});
		$http.get("/rest/categorydetail").then(resp => {
			$scope.listCategoryDetail = resp.data;
			$scope.listCategoryDetail.forEach(item => {
				item.dateInsert = new Date(item.dateInsert)
				item.dateUpdate = new Date(item.dateUpdate)
			})
		});
		$scope.reset();
		$scope.resetCategoryDetail();
	}

	$scope.fillCategoryDetail = (id) =>{
		$scope.idCategory = id;
		var index = $scope.listCategory.findIndex(c => c.id == id);
		$scope.listCategoryDetails = $scope.listCategory[index].category_detail;
	}

	$scope.resetCategoryDetail = function(){
		$scope.formCategoryDetail = {
			name : "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.reset = function(){
		$scope.formCategory = {
			name : "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.editCategory = function(item){
		$scope.itemCategory = item;
		console.log('edit', item)
		$scope.titleModel ="Cập nhật danh mục";
		$scope.formCategory = angular.copy(item);
	}

	$scope.createCategory = function(){
		var categoryInsert = {
			dateInsert: new Date(),
			dateUpdate: new Date(),
			status: $scope.formCategory.status,
			name: $scope.formCategory.name,
		}
		$http.post(`/rest/categories`, categoryInsert).then(resp => {
			if(resp.status == 200){
				$scope.listCategory.push(resp.data);
				$scope.message("Thêm mới danh mục thành công!");
			}
			$scope.reset();
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.updateCategory = function(){
		var item = angular.copy($scope.formCategory);

		var categoryUpdate = {
			dateInsert: $scope.itemCategory.dateInsert,
			dateUpdate: new Date(),
			status: item.status,
			name: item.name,
			id: item.id
		}

		$http.put(`/rest/categories/${item.id}`, categoryUpdate).then(resp => {
			if(resp.status == 200){
				var index = $scope.listCategory.findIndex(p => p.id == item.id);
				$scope.listCategory[index] = categoryUpdate;
				$scope.message("Cập nhật danh mục thành công!");
			}
			$scope.reset();
		})
		.catch(error => {
			alert("Lỗi cập nhật sản phẩm!");
			console.log("Error", error);
		});
	}
	
	$scope.listCategory = [];
	$scope.pager = {
		page: 0,
		size: 10,
		get listCategory(){
			if(this.page < 0){
				this.last();
			}
			if(this.page >= this.count){
				this.first();
			}
			var start = this.page*this.size;
			return $scope.listCategory.slice(start, start + this.size)
		},
		get count(){
			return Math.ceil(1.0 * $scope.listCategory.length / this.size);
		},
		first(){
			this.page = 0;
		},
		last(){
			this.page = this.count - 1;
		},
		next(){
			this.page++;
		},
		prev(){
			this.page--;
		}
	}

	$scope.resetCategoryDetail = function(){
		$scope.formCategoryDetail = {
			name : "",
			dateInsert: new Date(),
			status: true,
		}
	}

	$scope.createCategoryDetail = () => {

		var categoryDetailInsert = {
			dateInsert: new Date(),
			dateUpdate: new Date(),
			status: $scope.formCategoryDetail.status,
			name: $scope.formCategoryDetail.name,
			category:{
				id: $scope.idCategory
			}
		}

		$http.post(`/rest/categorydetail`, categoryDetailInsert).then(resp => {
			if(resp.status === 200){
				var index = $scope.listCategory.findIndex(c => c.id === $scope.idCategory);
				$scope.listCategory[index].category_detail.push(resp.data);
				$scope.message("Thêm mới danh mục con thành công!");
			}
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}


	$scope.editCategoryDetail = function(item){
		$scope.titleModel ="Cập nhật danh mục";
		$scope.formCategoryDetail = angular.copy(item);
	}

	$scope.updateCategoryDetail = (item) =>{
		var categoryDetailUpdate = {
			id : $scope.formCategoryDetail.id,
			dateInsert: $scope.formCategoryDetail.dateInsert,
			dateUpdate: new Date(),
			status: $scope.formCategoryDetail.status,
			name: $scope.formCategoryDetail.name,
			category:{
				id: $scope.idCategory
			}
		}

		$http.put(`/rest/categorydetail/${$scope.formCategoryDetail.id}`, categoryDetailUpdate).then(resp => {
			if(resp.status === 200){
				var index = $scope.listCategory.findIndex(c => c.id === $scope.idCategory);
				var detailIndex = $scope.listCategory[index].category_detail.findIndex(detail => detail.id === categoryDetailUpdate.id);
				$scope.listCategory[index].category_detail[detailIndex] = angular.copy(categoryDetailUpdate);
				console.log("uopdate",resp.data);
				$scope.message("Cập nhật danh mục thành công!");
			}
		})
		.catch(error => {
			alert("Lỗi cập nhật sản phẩm!");
			console.log("Error", error);
		});

	}


	$scope.message = (mes) =>{
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
			beforeShow: function () {}, // will be triggered before the toast is shown
			afterShown: function () {}, // will be triggered after the toat has been shown
			beforeHide: function () {}, // will be triggered before the toast gets hidden
			afterHidden: function () {}  // will be triggered after the toast has been hidden
		});
	}

	$scope.initialize();
}
);