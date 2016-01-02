'use strict';

angular.module('brewnoteApp')
    .controller('UserGroupController', function ($scope, $state, $modal, UserGroup, ParseLinks) {
      
        $scope.userGroups = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            UserGroup.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userGroups = result;
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
            $scope.userGroup = {
                status: null,
                role: null,
                id: null
            };
        };
    });
