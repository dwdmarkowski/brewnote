'use strict';

angular.module('brewnoteApp')
    .controller('AdditionalController', function ($scope, $state, $modal, Additional, ParseLinks) {
      
        $scope.additionals = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Additional.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.additionals = result;
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
            $scope.additional = {
                name: null,
                description: null,
                id: null
            };
        };
    });
