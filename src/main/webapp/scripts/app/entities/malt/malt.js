'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('malt', {
                parent: 'entity',
                url: '/malts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Malts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/malt/malts.html',
                        controller: 'MaltController'
                    }
                },
                resolve: {
                }
            })
            .state('malt.detail', {
                parent: 'entity',
                url: '/malt/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Malt'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/malt/malt-detail.html',
                        controller: 'MaltDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Malt', function($stateParams, Malt) {
                        return Malt.get({id : $stateParams.id});
                    }]
                }
            })
            .state('malt.new', {
                parent: 'malt',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/malt/malt-dialog.html',
                        controller: 'MaltDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('malt', null, { reload: true });
                    }, function() {
                        $state.go('malt');
                    })
                }]
            })
            .state('malt.edit', {
                parent: 'malt',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/malt/malt-dialog.html',
                        controller: 'MaltDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Malt', function(Malt) {
                                return Malt.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('malt', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('malt.delete', {
                parent: 'malt',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/malt/malt-delete-dialog.html',
                        controller: 'MaltDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Malt', function(Malt) {
                                return Malt.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('malt', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
