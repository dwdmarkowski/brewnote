'use strict';

angular.module('brewnoteApp').controller('UserGroupDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserGroup', 'User', 'FriendGroup',
        function($scope, $stateParams, $modalInstance, entity, UserGroup, User, FriendGroup) {

        $scope.userGroup = entity;
        $scope.users = User.query();
        $scope.friendgroups = FriendGroup.query();
        $scope.load = function(id) {
            UserGroup.get({id : id}, function(result) {
                $scope.userGroup = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:userGroupUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userGroup.id != null) {
                UserGroup.update($scope.userGroup, onSaveSuccess, onSaveError);
            } else {
                UserGroup.save($scope.userGroup, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
