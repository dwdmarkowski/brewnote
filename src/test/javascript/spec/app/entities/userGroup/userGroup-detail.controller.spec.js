'use strict';

describe('UserGroup Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockUserGroup, MockUser, MockFriendGroup;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockUserGroup = jasmine.createSpy('MockUserGroup');
        MockUser = jasmine.createSpy('MockUser');
        MockFriendGroup = jasmine.createSpy('MockFriendGroup');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'UserGroup': MockUserGroup,
            'User': MockUser,
            'FriendGroup': MockFriendGroup
        };
        createController = function() {
            $injector.get('$controller')("UserGroupDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:userGroupUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
