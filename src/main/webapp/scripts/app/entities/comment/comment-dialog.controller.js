'use strict';

angular.module('brewnoteApp').controller('CommentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Comment', 'Recipe', 'User',
        function($scope, $stateParams, $modalInstance, entity, Comment, Recipe, User) {

        $scope.comment = entity;
        $scope.recipes = Recipe.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Comment.get({id : id}, function(result) {
                $scope.comment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('brewnoteApp:commentUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.comment.id != null) {
                Comment.update($scope.comment, onSaveSuccess, onSaveError);
            } else {
                Comment.save($scope.comment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
