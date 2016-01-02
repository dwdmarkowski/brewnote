'use strict';

angular.module('brewnoteApp')
    .factory('Mashing', function ($resource, DateUtils) {
        return $resource('api/mashings/:id', {}, {
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
