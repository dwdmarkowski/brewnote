'use strict';

angular.module('brewnoteApp')
	.controller('FriendGroupDeleteController', function($scope, $modalInstance, entity, FriendGroup) {

        $scope.friendGroup = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FriendGroup.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });