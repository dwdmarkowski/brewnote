'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('yeast', {
                parent: 'entity',
                url: '/yeasts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Yeasts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/yeast/yeasts.html',
                        controller: 'YeastController'
                    }
                },
                resolve: {
                }
            })
            .state('yeast.detail', {
                parent: 'entity',
                url: '/yeast/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Yeast'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/yeast/yeast-detail.html',
                        controller: 'YeastDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Yeast', function($stateParams, Yeast) {
                        return Yeast.get({id : $stateParams.id});
                    }]
                }
            })
            .state('yeast.new', {
                parent: 'yeast',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/yeast/yeast-dialog.html',
                        controller: 'YeastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    temperature: null,
                                    days: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('yeast', null, { reload: true });
                    }, function() {
                        $state.go('yeast');
                    })
                }]
            })
            .state('yeast.edit', {
                parent: 'yeast',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/yeast/yeast-dialog.html',
                        controller: 'YeastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Yeast', function(Yeast) {
                                return Yeast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('yeast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('yeast.delete', {
                parent: 'yeast',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/yeast/yeast-delete-dialog.html',
                        controller: 'YeastDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Yeast', function(Yeast) {
                                return Yeast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('yeast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
