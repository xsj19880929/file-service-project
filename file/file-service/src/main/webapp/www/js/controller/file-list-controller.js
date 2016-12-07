(function (angular) {
    var module = angular.module('app');

    module.controller('FileListController', ['$scope', '$http', '$location', '$stateParams', '$state', function ($scope, $http, $location, $stateParams, $state) {
        $scope.offset = 0;
        $scope.fetchSize = 25;


        var loadData = function (offset, fetchSize) {
            $http.get('file?offset=' + offset + '&fetchSize=' + fetchSize).success(function (data) {
                $scope.fileOptions.data = data.list;
                $scope.fileOptions.totalItems = data.total;
                $scope.offset = offset;
                $scope.fetchSize = fetchSize;
            });
        };
        loadData($scope.offset, $scope.fetchSize);

        $scope.fileOptions = {
            data: [],
            paginationPageSizes: [$scope.fetchSize, $scope.fetchSize * 2, $scope.fetchSize * 3],
            paginationPageSize: $scope.fetchSize,
            useExternalPagination: true,
            columnDefs: [
                {name: '名称', field: 'fileName'},
                {name: '来源host:', field: 'host'},
                {name: '路径', field: 'path'},
                {name: '大小', field: 'size'},
                {name: '创建时间', field: 'createTime'},
                {name: '更新时间', field: 'lastUpdateTime'}
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (page, fetchSize) {
                    loadData((page - 1) * fetchSize, fetchSize);
                });
            }
        };

        $scope.search = function () {
            $scope.offset = 0;
            $scope.fetchSize = 25;
            $http.get('file/search?search=' + $scope.searchText + '&offset= ' + $scope.offset + '&fetchSize=' + $scope.fetchSize).success(function (data) {
                $scope.fileOptions.data = data.list;
                $scope.fileOptions.totalItems = data.total;
            })
        };
    }]);
})(angular);