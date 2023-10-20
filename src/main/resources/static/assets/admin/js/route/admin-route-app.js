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
    .when("/color-table", {
      templateUrl: "/assets/admin/color/table.html",
      controller: "color-ctrl"
    })
    .when("/color-form", {
      templateUrl: "/assets/admin/color/form.html",
      controller: "color-ctrl"
    })
    .when("/categorydetail-form", {
      templateUrl: "/assets/admin/category_detail/table.html",
      controller: "categorydetail-ctrl"
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
    .when("/authorize", {
      templateUrl: "/assets/admin/authority/index.html",
      controller: "authority-ctrl"
    })
    .when("/unauthorized", {
      templateUrl: "/assets/admin/authority/unauthorized.html",
      controller: "authority-ctrl"
    })
    .otherwise({
      template: "<h1 class='text-center'>FPT Polytechnic Administration</h1>"
    });
});