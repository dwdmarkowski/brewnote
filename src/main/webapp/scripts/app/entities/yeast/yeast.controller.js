'use strict';

angular.module('brewnoteApp')
    .controller('YeastController', function ($scope, $state, $modal, Yeast, ParseLinks) {
      
        $scope.yeasts = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Yeast.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.yeasts = result;
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
            $scope.yeast = {
                name: null,
                temperature: null,
                days: null,
                id: null
            };
        };
    });
