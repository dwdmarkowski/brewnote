'use strict';

angular.module('brewnoteApp')
    .controller('GroupSharedRecipeDetailController', function ($scope, $rootScope, $stateParams, entity, GroupSharedRecipe, FriendGroup, Recipe) {
        $scope.groupSharedRecipe = entity;
        $scope.load = function (id) {
            GroupSharedRecipe.get({id: id}, function(result) {
                $scope.groupSharedRecipe = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:groupSharedRecipeUpdate', function(event, result) {
            $scope.groupSharedRecipe = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
