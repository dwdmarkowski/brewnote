'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hop', {
                parent: 'entity',
                url: '/hops',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Hops'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hop/hops.html',
                        controller: 'HopController'
                    }
                },
                resolve: {
                }
            })
            .state('hop.detail', {
                parent: 'entity',
                url: '/hop/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Hop'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hop/hop-detail.html',
                        controller: 'HopDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Hop', function($stateParams, Hop) {
                        return Hop.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hop.new', {
                parent: 'hop',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hop/hop-dialog.html',
                        controller: 'HopDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    alfaAcids: null,
                                    hopForm: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hop', null, { reload: true });
                    }, function() {
                        $state.go('hop');
                    })
                }]
            })
            .state('hop.edit', {
                parent: 'hop',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hop/hop-dialog.html',
                        controller: 'HopDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Hop', function(Hop) {
                                return Hop.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hop', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hop.delete', {
                parent: 'hop',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hop/hop-delete-dialog.html',
                        controller: 'HopDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Hop', function(Hop) {
                                return Hop.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hop', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
