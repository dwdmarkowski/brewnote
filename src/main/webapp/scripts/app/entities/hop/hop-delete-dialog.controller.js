'use strict';

angular.module('brewnoteApp')
	.controller('HopDeleteController', function($scope, $modalInstance, entity, Hop) {

        $scope.hop = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Hop.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });