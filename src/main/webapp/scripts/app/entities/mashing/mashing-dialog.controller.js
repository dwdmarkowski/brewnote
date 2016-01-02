'use strict';

angular.module('brewnoteApp').controller('MashingDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Mashing', 'Malt',
        function($scope, $stateParams, $modalInstance, entity, Mashing, Malt) {

        $scope.mashing = entity;
        $scope.malts = Malt.query();
        $scope.load = function(id) {
            Mashing.get({id : id}, function(result) {
                $scope.mashing = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:mashingUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mashing.id != null) {
                Mashing.update($scope.mashing, onSaveSuccess, onSaveError);
            } else {
                Mashing.save($scope.mashing, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
