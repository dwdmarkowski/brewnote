'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('additional', {
                parent: 'entity',
                url: '/additionals',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Additionals'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/additional/additionals.html',
                        controller: 'AdditionalController'
                    }
                },
                resolve: {
                }
            })
            .state('additional.detail', {
                parent: 'entity',
                url: '/additional/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Additional'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/additional/additional-detail.html',
                        controller: 'AdditionalDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Additional', function($stateParams, Additional) {
                        return Additional.get({id : $stateParams.id});
                    }]
                }
            })
            .state('additional.new', {
                parent: 'additional',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/additional/additional-dialog.html',
                        controller: 'AdditionalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('additional', null, { reload: true });
                    }, function() {
                        $state.go('additional');
                    })
                }]
            })
            .state('additional.edit', {
                parent: 'additional',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/additional/additional-dialog.html',
                        controller: 'AdditionalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Additional', function(Additional) {
                                return Additional.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('additional', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('additional.delete', {
                parent: 'additional',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/additional/additional-delete-dialog.html',
                        controller: 'AdditionalDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Additional', function(Additional) {
                                return Additional.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('additional', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
