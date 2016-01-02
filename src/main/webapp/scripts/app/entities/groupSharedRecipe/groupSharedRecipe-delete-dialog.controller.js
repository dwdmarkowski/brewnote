'use strict';

angular.module('brewnoteApp')
	.controller('GroupSharedRecipeDeleteController', function($scope, $modalInstance, entity, GroupSharedRecipe) {

        $scope.groupSharedRecipe = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            GroupSharedRecipe.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });