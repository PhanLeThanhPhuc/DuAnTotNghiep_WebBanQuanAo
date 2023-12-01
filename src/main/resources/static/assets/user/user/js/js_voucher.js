app.controller("voucher-ctrl", function($scope, $http) {

	$scope.allVoucher = [];

	$scope.listVoucherAllProduct = [];

	$scope.listVoucherNoProduct = [];

	$scope.voucherDetail ={};

	$scope.initialize = async () => {

		await $http.get("/rest/voucher/date").then(resp => {

			$scope.allVoucher = resp.data;

			$scope.listVoucherAllProduct = resp.data.filter(function(item) {
				return item.productID === '';
			});

			$scope.listVoucherNoProduct = resp.data.filter(function(item) {
				return item.productID !== '';
			});

			console.log("listVoucherAllProduct: ",$scope.listVoucherAllProduct)
			console.log("listVoucherNoProduct: ",$scope.listVoucherNoProduct)
			console.log("allVoucher: ",$scope.allVoucher)
		})

		// $scope.popUpValue();

	}

	let copybtn = document.querySelector(".copybtn");


	copyIt = (button) => {
		var cardBody = button.closest('.card-body');
		var copyText = cardBody.querySelector('.copyvalue');

		if (copyText) {
			copyText.select();
			document.execCommand("copy");
			button.textContent = "COPIED";
		}
	}

	// $scope.popUpValue = () =>{
	// 	$scope.objectvoucherDetail = {
	//
	// 	}
	// }

	$scope.voucherDetail = (item) =>{
		$scope.objectvoucherDetail = angular.copy(item);
		console.log($scope.objectvoucherDetail);
		// $scope.$apply();
		$scope.findProductById(item.productID);
	}

	$scope.findProductById = (idProduct) => {
		var idArray = idProduct.split(',').map(Number);
		var params = { ids: idArray.join(',') };

		$http.get("/rest/products/idProduct", { params: params }).then(resp => {
			if(resp.status == 200){
				$scope.listProduct =  resp.data;
			}
		});
	}

	$scope.initialize();

})