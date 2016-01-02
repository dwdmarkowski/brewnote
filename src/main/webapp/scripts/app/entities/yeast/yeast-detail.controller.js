'use strict';

angular.module('brewnoteApp')
    .controller('YeastDetailController', function ($scope, $rootScope, $stateParams, entity, Yeast, Recipe) {
        $scope.yeast = entity;
        $scope.load = function (id) {
            Yeast.get({id: id}, function(result) {
                $scope.yeast = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:yeastUpdate', function(event, result) {
            $scope.yeast = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
