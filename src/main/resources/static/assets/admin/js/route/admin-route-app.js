app = angular.module("app", ["ngRoute"]);

app.config(function ($routeProvider) {
  $routeProvider
    .when("/category-table", {
      templateUrl: "/assets/admin/category/table.html",
      controller: "category-ctrl"
    })
    .when("/category-form", {
      templateUrl: "/assets/admin/category/form.html",
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
    .when("/categorydetail-table", {
      templateUrl: "/assets/admin/category_detail/form.html",
      controller: "categorydetail-ctrl"
    })
    .when("/user-table", {
      templateUrl: "/assets/admin/customer/table.html",
      controller: "user-ctrl"
    })
    .when("/user-form", {
      templateUrl: "/assets/admin/customer/form.html",
      controller: "user-ctrl"
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
 
    .otherwise({
      template: "<h1 class='text-center'>FPT Polytechnic Administration</h1>"
    });
});