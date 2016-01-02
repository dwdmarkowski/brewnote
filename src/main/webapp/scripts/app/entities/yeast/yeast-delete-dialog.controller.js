'use strict';

angular.module('brewnoteApp')
	.controller('YeastDeleteController', function($scope, $modalInstance, entity, Yeast) {

        $scope.yeast = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Yeast.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });