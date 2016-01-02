'use strict';

angular.module('brewnoteApp')
    .controller('CommentController', function ($scope, $state, $modal, Comment, ParseLinks) {
      
        $scope.comments = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Comment.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.comments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.comment = {
                content: null,
                id: null
            };
        };
    });
