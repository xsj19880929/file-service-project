(function (angular) {
    var module = angular.module('app');

    module.controller('IndexController', ['$scope', function ($scope) {
        $scope.message = 'message';
    }]);
})(angular);