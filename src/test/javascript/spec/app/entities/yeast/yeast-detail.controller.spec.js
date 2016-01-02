'use strict';

describe('Yeast Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockYeast, MockRecipe;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockYeast = jasmine.createSpy('MockYeast');
        MockRecipe = jasmine.createSpy('MockRecipe');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Yeast': MockYeast,
            'Recipe': MockRecipe
        };
        createController = function() {
            $injector.get('$controller')("YeastDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:yeastUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
