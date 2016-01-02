'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mashing', {
                parent: 'entity',
                url: '/mashings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Mashings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mashing/mashings.html',
                        controller: 'MashingController'
                    }
                },
                resolve: {
                }
            })
            .state('mashing.detail', {
                parent: 'entity',
                url: '/mashing/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Mashing'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mashing/mashing-detail.html',
                        controller: 'MashingDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Mashing', function($stateParams, Mashing) {
                        return Mashing.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mashing.new', {
                parent: 'mashing',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mashing/mashing-dialog.html',
                        controller: 'MashingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    time: null,
                                    temperature: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mashing', null, { reload: true });
                    }, function() {
                        $state.go('mashing');
                    })
                }]
            })
            .state('mashing.edit', {
                parent: 'mashing',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mashing/mashing-dialog.html',
                        controller: 'MashingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Mashing', function(Mashing) {
                                return Mashing.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mashing', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('mashing.delete', {
                parent: 'mashing',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mashing/mashing-delete-dialog.html',
                        controller: 'MashingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Mashing', function(Mashing) {
                                return Mashing.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mashing', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
