angular.module('ang_rest', [])
	.controller('ClientData', function ($scope, $http) {
		$http.get('http://localhost:8080/me').then(function (response) {
			$scope.client = response.data;

			$http.get('http://localhost:8080/credit-limit?id=' + $scope.client.id).then(function (response) {
				$scope.credit = response.data;
			});
		});
		$http.get('http://localhost:8080/find-all').then(function (response) {
			$scope.clients = response.data;
		});
	});