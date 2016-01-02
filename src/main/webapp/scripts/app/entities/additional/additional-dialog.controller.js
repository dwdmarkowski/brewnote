'use strict';

angular.module('brewnoteApp').controller('AdditionalDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Additional', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, Additional, Recipe) {

        $scope.additional = entity;
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            Additional.get({id : id}, function(result) {
                $scope.additional = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:additionalUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.additional.id != null) {
                Additional.update($scope.additional, onSaveSuccess, onSaveError);
            } else {
                Additional.save($scope.additional, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
