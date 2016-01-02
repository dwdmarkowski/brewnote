'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupSharedRecipe', {
                parent: 'entity',
                url: '/groupSharedRecipes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'GroupSharedRecipes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupSharedRecipe/groupSharedRecipes.html',
                        controller: 'GroupSharedRecipeController'
                    }
                },
                resolve: {
                }
            })
            .state('groupSharedRecipe.detail', {
                parent: 'entity',
                url: '/groupSharedRecipe/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'GroupSharedRecipe'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupSharedRecipe/groupSharedRecipe-detail.html',
                        controller: 'GroupSharedRecipeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'GroupSharedRecipe', function($stateParams, GroupSharedRecipe) {
                        return GroupSharedRecipe.get({id : $stateParams.id});
                    }]
                }
            })
            .state('groupSharedRecipe.new', {
                parent: 'groupSharedRecipe',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupSharedRecipe/groupSharedRecipe-dialog.html',
                        controller: 'GroupSharedRecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    sharingDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('groupSharedRecipe', null, { reload: true });
                    }, function() {
                        $state.go('groupSharedRecipe');
                    })
                }]
            })
            .state('groupSharedRecipe.edit', {
                parent: 'groupSharedRecipe',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupSharedRecipe/groupSharedRecipe-dialog.html',
                        controller: 'GroupSharedRecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['GroupSharedRecipe', function(GroupSharedRecipe) {
                                return GroupSharedRecipe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('groupSharedRecipe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('groupSharedRecipe.delete', {
                parent: 'groupSharedRecipe',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupSharedRecipe/groupSharedRecipe-delete-dialog.html',
                        controller: 'GroupSharedRecipeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['GroupSharedRecipe', function(GroupSharedRecipe) {
                                return GroupSharedRecipe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('groupSharedRecipe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
