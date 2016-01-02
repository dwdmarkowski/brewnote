'use strict';

angular.module('brewnoteApp').controller('YeastDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Yeast', 'Recipe',
        function($scope, $stateParams, $modalInstance, entity, Yeast, Recipe) {

        $scope.yeast = entity;
        $scope.recipes = Recipe.query();
        $scope.load = function(id) {
            Yeast.get({id : id}, function(result) {
                $scope.yeast = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:yeastUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.yeast.id != null) {
                Yeast.update($scope.yeast, onSaveSuccess, onSaveError);
            } else {
                Yeast.save($scope.yeast, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
