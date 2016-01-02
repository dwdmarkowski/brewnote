'use strict';

angular.module('brewnoteApp')
	.controller('MashingDeleteController', function($scope, $modalInstance, entity, Mashing) {

        $scope.mashing = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Mashing.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });