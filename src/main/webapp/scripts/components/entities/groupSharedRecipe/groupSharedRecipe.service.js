'use strict';

angular.module('brewnoteApp')
    .factory('GroupSharedRecipe', function ($resource, DateUtils) {
        return $resource('api/groupSharedRecipes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.sharingDate = DateUtils.convertDateTimeFromServer(data.sharingDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
