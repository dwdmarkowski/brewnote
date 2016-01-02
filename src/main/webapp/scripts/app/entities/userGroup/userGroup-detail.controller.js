'use strict';

angular.module('brewnoteApp')
    .controller('UserGroupDetailController', function ($scope, $rootScope, $stateParams, entity, UserGroup, User, FriendGroup) {
        $scope.userGroup = entity;
        $scope.load = function (id) {
            UserGroup.get({id: id}, function(result) {
                $scope.userGroup = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:userGroupUpdate', function(event, result) {
            $scope.userGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
