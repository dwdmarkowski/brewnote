'use strict';

angular.module('brewnoteApp').controller('RecipeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Recipe', 'User', 'GroupSharedRecipe', 'Comment', 'Malt', 'Hop', 'Yeast', 'Additional',
        function($scope, $stateParams, $modalInstance, entity, Recipe, User, GroupSharedRecipe, Comment, Malt, Hop, Yeast, Additional) {

        $scope.recipe = entity;
        $scope.users = User.query();
        $scope.groupsharedrecipes = GroupSharedRecipe.query();
        $scope.comments = Comment.query();
        $scope.malts = Malt.query();
        $scope.hops = Hop.query();
        $scope.yeasts = Yeast.query();
        $scope.additionals = Additional.query();
        $scope.load = function(id) {
            Recipe.get({id : id}, function(result) {
                $scope.recipe = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:recipeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.recipe.id != null) {
                Recipe.update($scope.recipe, onSaveSuccess, onSaveError);
            } else {
                Recipe.save($scope.recipe, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
