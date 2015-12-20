 'use strict';

angular.module('brewnoteApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-brewnoteApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-brewnoteApp-params')});
                }
                return response;
            }
        };
    });
