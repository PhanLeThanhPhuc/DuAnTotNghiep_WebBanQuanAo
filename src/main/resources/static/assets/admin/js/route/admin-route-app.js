app = angular.module("app", ["ngRoute"]);
app.config(function($routeProvider) {
	$routeProvider
		.when("/category", {
			templateUrl: "/assets/admin/category/index.html",
			controller: "category-ctrl"
		})

		.when("/color", {
			templateUrl: "/assets/admin/color/index.html",
			controller: "color-ctrl"
		})
		.when("/product", {
			templateUrl: "/assets/admin/product/index.html",
			controller: "product-ctrl"
		})

		.when("/size", {
			templateUrl: "/assets/admin/size/index.html",
			controller: "size-ctrl"
		})
		.when("/authority", {
			templateUrl: "/assets/admin/authority/index.html",
			controller: "authority-ctrl"
		})
		.when("/unauthorized", {
			templateUrl: "/assets/admin/authority/unauthorized.html",
			controller: "authority-ctrl"
		})
		.when("/user", {
			templateUrl: "/assets/admin/user/index.html",
			controller: "user-ctrl"
		})
		.when("/review", {
			templateUrl: "/assets/admin/review/index.html",
			controller: "review-ctrl"
		})

		.when("/voucher", {
			templateUrl: "/assets/admin/voucher/index.html",
			controller: "voucher-ctrl"
		})


		.when("/order", {
			templateUrl: "/assets/admin/order/index.html",
			controller: "order-ctrl"
		})

		.when("/discount", {
			templateUrl: "/assets/admin/discount/index.html",
			controller: "discount-ctrl"
		})
		.when("/revenue_statistic", {
			templateUrl: "/assets/admin/statistics/revenue_statistic.html",
			controller: "statistic-ctrl"
		})

		.when("/revenue_user", {
			templateUrl: "/assets/admin/statistics/revenue_user.html",
			controller: "user-statistic-ctrl"
		})

		.otherwise({
			template: "<h1 class='text-center'>FPT Polytechnic Administration</h1>"
		});
});