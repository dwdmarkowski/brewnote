'use strict';

angular.module('brewnoteApp')
    .controller('FriendshipUserDetailController', function ($scope, $rootScope, $stateParams, entity, User) {
        $scope.user = entity;
        $scope.load = function (login) {
            User.get({login: login}, function(result) {
                $scope.user = result;
            });
        };
    });
