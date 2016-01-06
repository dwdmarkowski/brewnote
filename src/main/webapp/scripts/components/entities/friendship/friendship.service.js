'use strict';

angular.module('brewnoteApp')
    .factory('Friendship', function ($resource, DateUtils) {
        return $resource('api/friendships/:id', {}, {
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
    }).factory('Notifications', function ($resource, DateUtils) {
        return $resource('api/friendshipNotifications/', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
