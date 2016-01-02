'use strict';

angular.module('brewnoteApp')
    .factory('Hop', function ($resource, DateUtils) {
        return $resource('api/hops/:id', {}, {
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
