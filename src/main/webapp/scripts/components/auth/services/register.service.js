'use strict';

angular.module('brewnoteApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


