'use strict';

angular.module('brewnoteApp')
    .controller('FriendshipNotificationsController', function ($scope, $state, $modal, Notifications, ParseLinks) {

        $scope.friendships = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Notifications.query({page: $scope.page, size: 20}, function(result, headers) {
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
