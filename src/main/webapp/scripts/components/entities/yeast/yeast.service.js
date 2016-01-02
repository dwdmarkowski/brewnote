'use strict';

angular.module('brewnoteApp')
    .factory('Yeast', function ($resource, DateUtils) {
        return $resource('api/yeasts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
