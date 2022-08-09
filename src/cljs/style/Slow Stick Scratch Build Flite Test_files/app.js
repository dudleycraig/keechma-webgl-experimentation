var app = angular.module('flitetest',['ngResource','ngSanitize']);

app.controller('rootController',function($rootScope){
})

app.directive('articleList', function() {
	return{
		restrict: 'C',
		templateUrl: '/angular_templates/list-article.html',
		replace: true
	};
});
