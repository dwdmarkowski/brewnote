'use strict';

angular.module('brewnoteApp')
    .controller('MashingController', function ($scope, $state, $modal, Mashing, ParseLinks) {
      
        $scope.mashings = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Mashing.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.mashings = result;
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
            $scope.mashing = {
                time: null,
                temperature: null,
                id: null
            };
        };
    });
