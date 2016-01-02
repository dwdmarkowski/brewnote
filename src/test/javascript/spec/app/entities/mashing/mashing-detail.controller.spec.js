'use strict';

describe('Mashing Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMashing, MockMalt;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMashing = jasmine.createSpy('MockMashing');
        MockMalt = jasmine.createSpy('MockMalt');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Mashing': MockMashing,
            'Malt': MockMalt
        };
        createController = function() {
            $injector.get('$controller')("MashingDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:mashingUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
