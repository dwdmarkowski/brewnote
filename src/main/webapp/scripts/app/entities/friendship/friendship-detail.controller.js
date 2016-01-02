'use strict';

angular.module('brewnoteApp')
    .controller('FriendshipDetailController', function ($scope, $rootScope, $stateParams, entity, Friendship, User) {
        $scope.friendship = entity;
        $scope.load = function (id) {
            Friendship.get({id: id}, function(result) {
                $scope.friendship = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:friendshipUpdate', function(event, result) {
            $scope.friendship = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
