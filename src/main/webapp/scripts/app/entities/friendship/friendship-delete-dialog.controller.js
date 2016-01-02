'use strict';

angular.module('brewnoteApp')
	.controller('FriendshipDeleteController', function($scope, $modalInstance, entity, Friendship) {

        $scope.friendship = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Friendship.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });