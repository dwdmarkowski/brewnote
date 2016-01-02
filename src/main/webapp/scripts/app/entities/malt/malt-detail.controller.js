'use strict';

angular.module('brewnoteApp')
    .controller('MaltDetailController', function ($scope, $rootScope, $stateParams, entity, Malt, Recipe, Mashing) {
        $scope.malt = entity;
        $scope.load = function (id) {
            Malt.get({id: id}, function(result) {
                $scope.malt = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:maltUpdate', function(event, result) {
            $scope.malt = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
