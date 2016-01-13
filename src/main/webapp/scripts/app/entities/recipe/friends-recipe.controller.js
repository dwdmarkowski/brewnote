'use strict';

angular.module('brewnoteApp')
    .controller('FriendsRecipeController', function ($scope, $state, $modal, FriendsRecipes, ParseLinks) {

        $scope.recipes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            FriendsRecipes.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.recipes = result;
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
            $scope.recipe = {
                name: null,
                style: null,
                date: null,
                abv: null,
                originalGravity: null,
                notes: null,
                volume: null,
                id: null,
                visibility: null
            };
        };
    });
