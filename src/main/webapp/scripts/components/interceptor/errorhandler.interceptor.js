'use strict';

angular.module('brewnoteApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('brewnoteApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });