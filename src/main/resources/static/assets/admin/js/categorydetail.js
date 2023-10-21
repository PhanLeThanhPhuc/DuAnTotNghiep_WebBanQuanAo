app.controller("categorydetail-ctrl", function($scope, $http){
	$scope.initialize = function(){
		$http.get("/rest/categorydetail").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.dateInsert = new Date(item.dateInsert)
				
			})
		});
		$scope.reset();
	}
	
	$scope.reset = function(){
		$scope.form = {
			dateInsert: new Date(),
			status: true,
			
		}
	}

	$scope.edit = function(item){
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab("show");
	}

	$scope.create = function(){
		var item = angular.copy($scope.form);
		$http.post(`/rest/categorydetail`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.update = function(){
		var item = angular.copy($scope.form);
		$http.put(`/rest/categorydetail/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật sản phẩm thành công!");
		})
		.catch(error => {
			alert("Lỗi cập nhật sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.delete = function(item){
		if(confirm("Bạn muốn xóa sản phẩm này?")){
			$http.delete(`/rest/categorydetail/${item.id}`).then(resp => {
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
		$http.post('/rest/upload/images', data, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
        }).then(resp => {
			$scope.form.image = resp.data.name;
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}

	$scope.initialize();

	$scope.pager = {
		page: 0,
		size: 10,
		get items(){
			if(this.page < 0){
				this.last();
			}
			if(this.page >= this.count){
				this.first();
			}
			var start = this.page*this.size;
			return $scope.items.slice(start, start + this.size)
		},
		get count(){
			return Math.ceil(1.0 * $scope.items.length / this.size);
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
}
);