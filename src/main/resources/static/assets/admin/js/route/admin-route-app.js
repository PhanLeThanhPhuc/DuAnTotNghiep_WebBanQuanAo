app = angular.module("app", ["ngRoute"]);

app.config(function ($routeProvider) {
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
    .when("/categorydetail-table", {
      templateUrl: "/assets/admin/category_detail/form.html",
      controller: "categorydetail-ctrl"
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
    
 
    .otherwise({
      template: "<h1 class='text-center'>FPT Polytechnic Administration</h1>"
    });
});