'use strict';

angular.module('brewnoteApp')
    .controller('NavbarController', function ($scope, $interval, $http, $location, $state, Auth, Principal, ENV) {

        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $interval(getNotificationsAmount, 1000);

        function getNotificationsAmount() {
            $http.get('api/notificationsAmount').success(function(response){
                $scope.notificationsAmount = response;
            });
        }

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
