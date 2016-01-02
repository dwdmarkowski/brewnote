'use strict';

angular.module('brewnoteApp')
	.controller('AdditionalDeleteController', function($scope, $modalInstance, entity, Additional) {

        $scope.additional = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Additional.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });