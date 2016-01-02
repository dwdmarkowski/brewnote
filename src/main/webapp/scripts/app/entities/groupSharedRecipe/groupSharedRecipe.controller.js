'use strict';

angular.module('brewnoteApp')
    .controller('GroupSharedRecipeController', function ($scope, $state, $modal, GroupSharedRecipe, ParseLinks) {
      
        $scope.groupSharedRecipes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            GroupSharedRecipe.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.groupSharedRecipes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.groupSharedRecipe = {
                sharingDate: null,
                id: null
            };
        };
    });
