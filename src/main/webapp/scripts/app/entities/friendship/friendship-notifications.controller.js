'use strict';

angular.module('brewnoteApp')
    .controller('FriendshipNotificationsController', function ($scope, $http, Notifications, ParseLinks) {

        $scope.friendships = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Notifications.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.friendships = result;
            });
        };
        $scope.acceptInvitation = function(id) {
            $http.put('/api/friendships/accept/' + id).then(function(result){
                $scope.loadAll();
            });
        };
        $scope.rejectInvitation = function(id) {
            $http.put('/api/friendships/reject/' + id).then(function(result){
                $scope.loadAll();
            });
        };
        $scope.loadAll();
    });
