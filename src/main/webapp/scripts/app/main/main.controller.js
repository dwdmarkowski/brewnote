'use strict';

angular.module('brewnoteApp')
    .controller('MainController', function ($scope, $http, Principal) {
        $http.get('api/lastFriendsRecipes').success(function(response){
            $scope.friendsRecipes = response;
        });

        $http.get('api/lastPublicRecipes').success(function(response){
            $scope.publicRecipes = response;
        });

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
