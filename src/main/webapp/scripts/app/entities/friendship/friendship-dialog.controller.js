'use strict';

angular.module('brewnoteApp').controller('FriendshipDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Friendship', 'User',
        function($scope, $stateParams, $modalInstance, entity, Friendship, User) {

        $scope.friendship = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Friendship.get({id : id}, function(result) {
                $scope.friendship = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:friendshipUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.friendship.id != null) {
                Friendship.update($scope.friendship, onSaveSuccess, onSaveError);
            } else {
                Friendship.save($scope.friendship, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
