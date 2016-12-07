(function (angular) {
    var module = angular.module('app', ['ui.router', 'ui.bootstrap',
        'ui.grid', 'ui.grid.pagination']);

    module.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/file/");
        $stateProvider
                .state('file', {
                    url: "/file",
                    templateUrl: "www/view/index.html"
                }).state('file.list', {
                    url: "/",
                    templateUrl: "www/view/file.list.html",
                    controller: 'FileListController'
                });
    });
})(angular);