'use strict';

angular.module('brewnoteApp')
    .controller('MaltController', function ($scope, $state, $modal, Malt, ParseLinks) {
      
        $scope.malts = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Malt.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.malts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.malt = {
                name: null,
                amount: null,
                id: null
            };
        };
    });
