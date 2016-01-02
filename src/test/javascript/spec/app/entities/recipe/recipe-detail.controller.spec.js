'use strict';

describe('Recipe Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockRecipe, MockUser, MockGroupSharedRecipe, MockComment, MockMalt, MockHop, MockYeast, MockAdditional;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockRecipe = jasmine.createSpy('MockRecipe');
        MockUser = jasmine.createSpy('MockUser');
        MockGroupSharedRecipe = jasmine.createSpy('MockGroupSharedRecipe');
        MockComment = jasmine.createSpy('MockComment');
        MockMalt = jasmine.createSpy('MockMalt');
        MockHop = jasmine.createSpy('MockHop');
        MockYeast = jasmine.createSpy('MockYeast');
        MockAdditional = jasmine.createSpy('MockAdditional');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Recipe': MockRecipe,
            'User': MockUser,
            'GroupSharedRecipe': MockGroupSharedRecipe,
            'Comment': MockComment,
            'Malt': MockMalt,
            'Hop': MockHop,
            'Yeast': MockYeast,
            'Additional': MockAdditional
        };
        createController = function() {
            $injector.get('$controller')("RecipeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:recipeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
