'use strict';

angular.module('brewnoteApp')
    .controller('FriendGroupController', function ($scope, $state, $modal, FriendGroup, ParseLinks) {
      
        $scope.friendGroups = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            FriendGroup.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.friendGroups = result;
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
            $scope.friendGroup = {
                name: null,
                id: null
            };
        };
    });
