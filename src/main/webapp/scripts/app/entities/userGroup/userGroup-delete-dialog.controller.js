'use strict';

angular.module('brewnoteApp')
	.controller('UserGroupDeleteController', function($scope, $modalInstance, entity, UserGroup) {

        $scope.userGroup = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserGroup.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });