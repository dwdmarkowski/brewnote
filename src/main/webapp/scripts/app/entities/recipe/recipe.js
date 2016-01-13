'use strict';

angular.module('brewnoteApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('recipe', {
                parent: 'entity',
                url: '/recipes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Recipes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recipe/recipes.html',
                        controller: 'RecipeController'
                    }
                },
                resolve: {
                }
            }).state('publicRecipe', {
                parent: 'entity',
                url: '/publicRecipes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PublicRecipes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recipe/public-recipes.html',
                        controller: 'PublicRecipeController'
                    }
                },
                resolve: {
                }
            })
            .state('recipe.detail', {
                parent: 'entity',
                url: '/recipe/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Recipe'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recipe/recipe-detail.html',
                        controller: 'RecipeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Recipe', function($stateParams, Recipe) {
                        return Recipe.get({id : $stateParams.id});
                    }]
                }
            })
            .state('recipe.new', {
                parent: 'recipe',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/recipe/recipe-dialog.html',
                        controller: 'RecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    style: null,
                                    date: null,
                                    abv: null,
                                    originalGravity: null,
                                    notes: null,
                                    volume: null,
                                    id: null,
                                    visibility: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('recipe', null, { reload: true });
                    }, function() {
                        $state.go('recipe');
                    })
                }]
            })
            .state('recipe.edit', {
                parent: 'recipe',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/recipe/recipe-dialog.html',
                        controller: 'RecipeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Recipe', function(Recipe) {
                                return Recipe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recipe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('recipe.delete', {
                parent: 'recipe',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/recipe/recipe-delete-dialog.html',
                        controller: 'RecipeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Recipe', function(Recipe) {
                                return Recipe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recipe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
