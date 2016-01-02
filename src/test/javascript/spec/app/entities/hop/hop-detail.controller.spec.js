'use strict';

describe('Hop Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHop, MockRecipe;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHop = jasmine.createSpy('MockHop');
        MockRecipe = jasmine.createSpy('MockRecipe');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Hop': MockHop,
            'Recipe': MockRecipe
        };
        createController = function() {
            $injector.get('$controller')("HopDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:hopUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
