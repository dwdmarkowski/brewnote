'use strict';

describe('Malt Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMalt, MockRecipe, MockMashing;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMalt = jasmine.createSpy('MockMalt');
        MockRecipe = jasmine.createSpy('MockRecipe');
        MockMashing = jasmine.createSpy('MockMashing');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Malt': MockMalt,
            'Recipe': MockRecipe,
            'Mashing': MockMashing
        };
        createController = function() {
            $injector.get('$controller')("MaltDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:maltUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
