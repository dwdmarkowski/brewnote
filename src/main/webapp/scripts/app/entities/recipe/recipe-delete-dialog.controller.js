'use strict';

angular.module('brewnoteApp')
	.controller('RecipeDeleteController', function($scope, $modalInstance, entity, Recipe) {

        $scope.recipe = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Recipe.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });