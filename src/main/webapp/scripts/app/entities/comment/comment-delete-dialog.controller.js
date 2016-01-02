'use strict';

angular.module('brewnoteApp')
	.controller('CommentDeleteController', function($scope, $modalInstance, entity, Comment) {

        $scope.comment = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Comment.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });