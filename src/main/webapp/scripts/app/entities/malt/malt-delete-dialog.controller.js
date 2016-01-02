'use strict';

angular.module('brewnoteApp')
	.controller('MaltDeleteController', function($scope, $modalInstance, entity, Malt) {

        $scope.malt = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Malt.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });