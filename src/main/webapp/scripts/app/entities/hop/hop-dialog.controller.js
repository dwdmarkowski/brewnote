'use strict';

angular.module('brewnoteApp').controller('HopDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Hop', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, Hop, Recipe) {

        $scope.hop = entity;
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            Hop.get({id : id}, function(result) {
                $scope.hop = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:hopUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hop.id != null) {
                Hop.update($scope.hop, onSaveSuccess, onSaveError);
            } else {
                Hop.save($scope.hop, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
