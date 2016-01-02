'use strict';

describe('GroupSharedRecipe Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockGroupSharedRecipe, MockFriendGroup, MockRecipe;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockGroupSharedRecipe = jasmine.createSpy('MockGroupSharedRecipe');
        MockFriendGroup = jasmine.createSpy('MockFriendGroup');
        MockRecipe = jasmine.createSpy('MockRecipe');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'GroupSharedRecipe': MockGroupSharedRecipe,
            'FriendGroup': MockFriendGroup,
            'Recipe': MockRecipe
        };
        createController = function() {
            $injector.get('$controller')("GroupSharedRecipeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:groupSharedRecipeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
