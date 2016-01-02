'use strict';

angular.module('brewnoteApp')
    .controller('MashingDetailController', function ($scope, $rootScope, $stateParams, entity, Mashing, Malt) {
        $scope.mashing = entity;
        $scope.load = function (id) {
            Mashing.get({id: id}, function(result) {
                $scope.mashing = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:mashingUpdate', function(event, result) {
            $scope.mashing = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
