'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calculators', {
                parent: 'site',
                url: '/calculators',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/calculators/calculators.html',
                        controller: 'CalculatorController'
                    }
                },
                resolve: {

                }
            });
    });
