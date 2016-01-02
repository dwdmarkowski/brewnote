'use strict';

angular.module('brewnoteApp')
    .factory('UserGroup', function ($resource, DateUtils) {
        return $resource('api/userGroups/:id', {}, {
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
