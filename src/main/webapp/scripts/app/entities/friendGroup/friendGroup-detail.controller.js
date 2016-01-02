'use strict';

angular.module('brewnoteApp')
    .controller('FriendGroupDetailController', function ($scope, $rootScope, $stateParams, entity, FriendGroup, UserGroup, GroupSharedRecipe) {
        $scope.friendGroup = entity;
        $scope.load = function (id) {
            FriendGroup.get({id: id}, function(result) {
                $scope.friendGroup = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:friendGroupUpdate', function(event, result) {
            $scope.friendGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
