'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('friendGroup', {
                parent: 'entity',
                url: '/friendGroups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FriendGroups'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendGroup/friendGroups.html',
                        controller: 'FriendGroupController'
                    }
                },
                resolve: {
                }
            })
            .state('friendGroup.detail', {
                parent: 'entity',
                url: '/friendGroup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FriendGroup'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendGroup/friendGroup-detail.html',
                        controller: 'FriendGroupDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'FriendGroup', function($stateParams, FriendGroup) {
                        return FriendGroup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('friendGroup.new', {
                parent: 'friendGroup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendGroup/friendGroup-dialog.html',
                        controller: 'FriendGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('friendGroup', null, { reload: true });
                    }, function() {
                        $state.go('friendGroup');
                    })
                }]
            })
            .state('friendGroup.edit', {
                parent: 'friendGroup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendGroup/friendGroup-dialog.html',
                        controller: 'FriendGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FriendGroup', function(FriendGroup) {
                                return FriendGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('friendGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('friendGroup.delete', {
                parent: 'friendGroup',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendGroup/friendGroup-delete-dialog.html',
                        controller: 'FriendGroupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FriendGroup', function(FriendGroup) {
                                return FriendGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('friendGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
