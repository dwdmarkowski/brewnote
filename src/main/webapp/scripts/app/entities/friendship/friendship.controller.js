'use strict';

angular.module('brewnoteApp')
    .controller('FriendshipController', function ($scope, $state, $modal, Friendship, ParseLinks) {
      
        $scope.friendships = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Friendship.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.friendships = result;
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
            $scope.friendship = {
                status: null,
                id: null
            };
        };
    });
