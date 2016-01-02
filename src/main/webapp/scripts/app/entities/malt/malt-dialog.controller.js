'use strict';

angular.module('brewnoteApp').controller('MaltDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Malt', 'Recipe', 'Mashing',
        function($scope, $stateParams, $modalInstance, entity, Malt, Recipe, Mashing) {

        $scope.malt = entity;
        $scope.recipes = Recipe.query();
        $scope.mashings = Mashing.query();
        $scope.load = function(id) {
            Malt.get({id : id}, function(result) {
                $scope.malt = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:maltUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.malt.id != null) {
                Malt.update($scope.malt, onSaveSuccess, onSaveError);
            } else {
                Malt.save($scope.malt, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
