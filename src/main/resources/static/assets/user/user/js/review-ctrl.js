app.controller("review-ctrl", function($scope, $http) {
	$scope.review = {};
	$scope.product = [];
	$scope.user = []
	$scope.reviews = function(sessionUserId, productId) {
		$scope.reviewdetail = [];
		$scope.review = {};
		let star = $('.starRating span').eq(1 - 1);
		star.siblings().removeClass('active');
		star.addClass('active');
		star.parent().addClass('starRated');
		$http.get("/rest/review/product/" + sessionUserId + "/" + productId).then(resp => {
			if (resp.status === 200) {
				$scope.reviewdetail = resp.data;
				$scope.reviewdetail.dateReview = new Date(resp.data.dateReview)
				console.log("te", $scope.reviewdetail)
			}
		});
		$http.get("/rest/users/" + sessionUserId).then(resp => {
			if (resp.status === 200) {
				$scope.user = resp.data;
			}
		});
		$http.get("/rest/products/" + productId).then(resp => {
			if (resp.status === 200) {
				$scope.product = resp.data;
			}
		});
	}


	$scope.createReview = function() {
		var item = angular.copy($scope.review);
		var ratingValue = document.getElementById('currentRating').textContent.trim();
		item.dateReview = new Date();
		item.rating = 1;
		if (ratingValue) {
			item.rating = ratingValue;
		}
		item.product = $scope.product;
		item.user = $scope.user;
		$http.post(`/rest/review`, item).then(resp => {
			resp.data.dateReview = new Date(resp.data.dateReview)
			$scope.reviewdetail = resp.data;
			alert("Đánh giá thành công!");
		}).catch(error => {
			alert("Lỗi đánh giá!");
			console.log("Error", error);
		});
	}
	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('uploadfile', files[0]);
		$http.post('/rest/upload', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.review.image = resp.data.image;
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}

})