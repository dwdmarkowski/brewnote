'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('friendship', {
                parent: 'entity',
                url: '/friendships',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Friendships'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendship/friendships.html',
                        controller: 'FriendshipController'
                    }
                },
                resolve: {
                }
            })
            .state('notifications', {
                parent: 'entity',
                url: '/friendshipNotifications',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Friendships'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendship/friendship-notifications.html',
                        controller: 'FriendshipNotificationsController'
                    }
                },
                resolve: {
                }
            })
            .state('friendship.detail', {
                parent: 'entity',
                url: '/friendship/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Friendship'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendship/friendship-detail.html',
                        controller: 'FriendshipDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Friendship', function($stateParams, Friendship) {
                        return Friendship.get({id : $stateParams.id});
                    }]
                }
            })
            .state('friendship.new', {
                parent: 'friendship',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendship/friendship-dialog.html',
                        controller: 'FriendshipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('friendship', null, { reload: true });
                    }, function() {
                        $state.go('friendship');
                    })
                }]
            })
            .state('friendship.edit', {
                parent: 'friendship',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendship/friendship-dialog.html',
                        controller: 'FriendshipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Friendship', function(Friendship) {
                                return Friendship.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('friendship', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('friendship.delete', {
                parent: 'friendship',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/friendship/friendship-delete-dialog.html',
                        controller: 'FriendshipDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Friendship', function(Friendship) {
                                return Friendship.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('friendship', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            }).state('friendship.userDetail', {
                parent: 'entity',
                url: '/friendship/userDetail/{login}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Friendship'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/friendship/friendship-user-detail.html',
                        controller: 'FriendshipUserDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'User', function($stateParams, User) {
                        return User.get({login : $stateParams.login});
                    }]
                }
            });
    });
