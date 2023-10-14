app = angular.module("app", ["ngRoute"]);

app.config(function ($routeProvider) {
  $routeProvider
    
    .when("/category", {
      templateUrl: "/assets/admin/category/index.html",
      controller: "category-ctrl"
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