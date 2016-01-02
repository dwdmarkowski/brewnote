'use strict';

angular.module('brewnoteApp').controller('GroupSharedRecipeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'GroupSharedRecipe', 'FriendGroup', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, GroupSharedRecipe, FriendGroup, Recipe) {

        $scope.groupSharedRecipe = entity;
        $scope.friendgroups = FriendGroup.query();
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            GroupSharedRecipe.get({id : id}, function(result) {
                $scope.groupSharedRecipe = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:groupSharedRecipeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.groupSharedRecipe.id != null) {
                GroupSharedRecipe.update($scope.groupSharedRecipe, onSaveSuccess, onSaveError);
            } else {
                GroupSharedRecipe.save($scope.groupSharedRecipe, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
