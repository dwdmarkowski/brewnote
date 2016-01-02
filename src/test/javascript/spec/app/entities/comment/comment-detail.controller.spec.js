'use strict';

describe('Comment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockComment, MockRecipe, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockComment = jasmine.createSpy('MockComment');
        MockRecipe = jasmine.createSpy('MockRecipe');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Comment': MockComment,
            'Recipe': MockRecipe,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("CommentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'brewnoteApp:commentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
