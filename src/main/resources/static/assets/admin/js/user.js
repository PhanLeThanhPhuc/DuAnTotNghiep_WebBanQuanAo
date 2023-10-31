app.controller("user-ctrl", function($scope, $http){
	$scope.initialize = function(){
		$http.get("/rest/users").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.date_insert = new Date(item.date_insert)
				item.date_update = new Date(item.date_update)
				
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
		$http.post(`/rest/users`, item).then(resp => {
			resp.data.date_insert = new Date()
			resp.data.date_update = new Date()
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới người dùng thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới!");
			console.log("Error", error);
		});
	}

	$scope.update = function(){
		var item = angular.copy($scope.form);
		item.date_update = new Date();
		$http.put(`/rest/users/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật người dùng thành công!");
		})
		.catch(error => {
			alert("Lỗi cập nhật!");
			console.log("Error", error);
		});
	}

	$scope.delete = function(){
		var item = angular.copy($scope.form);
		item.date_update = new Date();
		item.status=false;
		$http.put(`/rest/users/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật trạng thái thành công!");
		})
		.catch(error => {
			alert("Lỗi cập nhật!");
			console.log("Error", error);
		});
		
	}
	
	$scope.imageChanged = function(files){
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/image', data, {
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