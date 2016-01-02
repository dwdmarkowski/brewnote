'use strict';

angular.module('brewnoteApp').controller('FriendGroupDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'FriendGroup', 'UserGroup', 'GroupSharedRecipe',
        function($scope, $stateParams, $modalInstance, entity, FriendGroup, UserGroup, GroupSharedRecipe) {

        $scope.friendGroup = entity;
        $scope.usergroups = UserGroup.query();
        $scope.groupsharedrecipes = GroupSharedRecipe.query();
        $scope.load = function(id) {
            FriendGroup.get({id : id}, function(result) {
                $scope.friendGroup = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:friendGroupUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.friendGroup.id != null) {
                FriendGroup.update($scope.friendGroup, onSaveSuccess, onSaveError);
            } else {
                FriendGroup.save($scope.friendGroup, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
