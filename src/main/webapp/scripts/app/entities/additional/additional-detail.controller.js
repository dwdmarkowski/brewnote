'use strict';

angular.module('brewnoteApp')
    .controller('AdditionalDetailController', function ($scope, $rootScope, $stateParams, entity, Additional, Recipe) {
        $scope.additional = entity;
        $scope.load = function (id) {
            Additional.get({id: id}, function(result) {
                $scope.additional = result;
            });
        };
        var unsubscribe = $rootScope.$on('brewnoteApp:additionalUpdate', function(event, result) {
            $scope.additional = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
