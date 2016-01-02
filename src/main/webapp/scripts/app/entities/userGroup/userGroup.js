'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userGroup', {
                parent: 'entity',
                url: '/userGroups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserGroups'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userGroup/userGroups.html',
                        controller: 'UserGroupController'
                    }
                },
                resolve: {
                }
            })
            .state('userGroup.detail', {
                parent: 'entity',
                url: '/userGroup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserGroup'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userGroup/userGroup-detail.html',
                        controller: 'UserGroupDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserGroup', function($stateParams, UserGroup) {
                        return UserGroup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userGroup.new', {
                parent: 'userGroup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userGroup/userGroup-dialog.html',
                        controller: 'UserGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    role: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userGroup', null, { reload: true });
                    }, function() {
                        $state.go('userGroup');
                    })
                }]
            })
            .state('userGroup.edit', {
                parent: 'userGroup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userGroup/userGroup-dialog.html',
                        controller: 'UserGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserGroup', function(UserGroup) {
                                return UserGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userGroup.delete', {
                parent: 'userGroup',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userGroup/userGroup-delete-dialog.html',
                        controller: 'UserGroupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserGroup', function(UserGroup) {
                                return UserGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
