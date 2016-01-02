'use strict';

angular.module('brewnoteApp')
    .controller('HopDetailController', function ($scope, $rootScope, $stateParams, entity, Hop, Recipe) {
        $scope.hop = entity;
        $scope.load = function (id) {
            Hop.get({id: id}, function(result) {
                $scope.hop = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:hopUpdate', function(event, result) {
            $scope.hop = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
