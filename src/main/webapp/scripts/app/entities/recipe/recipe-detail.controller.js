'use strict';

angular.module('brewnoteApp')
    .controller('RecipeDetailController', function ($scope, $rootScope, $stateParams, entity, Recipe, User, GroupSharedRecipe, Comment, Malt, Hop, Yeast, Additional) {
        $scope.recipe = entity;
        $scope.load = function (id) {
            Recipe.get({id: id}, function(result) {
                $scope.recipe = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:recipeUpdate', function(event, result) {
            $scope.recipe = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
