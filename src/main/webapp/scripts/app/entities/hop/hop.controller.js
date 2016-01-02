'use strict';

angular.module('brewnoteApp')
    .controller('HopController', function ($scope, $state, $modal, Hop, ParseLinks) {
      
        $scope.hops = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Hop.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.hops = result;
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
            $scope.hop = {
                name: null,
                alfaAcids: null,
                hopForm: null,
                id: null
            };
        };
    });
