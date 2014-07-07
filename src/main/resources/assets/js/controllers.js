'use strict';

/* Controllers */

var gardenApp = angular.module('gardenApp', ["highcharts-ng"]);

function queryData($http, $scope, hid) {
	var responsePromise = $http.get('../garden/query/?hid=' + hid);
	
	responsePromise.success(function(data) {
		$scope.hid = data.hid;
		$scope.response = data;
		
		console.log("length " + data);
		var map = Array.prototype.map;

		var temperature1 = map.call(data.measurements, function(item) { 
			var dateParts = item.date.split("-");
			var date = Date.UTC(dateParts[0], dateParts[1], dateParts[2], item.hour);
			return [date, item.temperature1]; 
		});
		var temperature2 = map.call(data.measurements, function(item) { 
			var dateParts = item.date.split("-");
			var date = Date.UTC(dateParts[0], dateParts[1], dateParts[2], item.hour);
			return [date, item.temperature2]; 
		});
		
		temperature1.sort(function(a,b) {
			return a[0] - b[0];
		});
		temperature2.sort(function(a,b) {
			return a[0] - b[0];
		});

		$scope.temperature1Label = data.temperature1Label;
		$scope.temperature2Label = data.temperature2Label;

		$scope.temperature1Measurements = temperature1;
		$scope.temperature2Measurements = temperature2;	

		$scope.temperatureSeries = [{"name": data.temperature1Label, "data": temperature1},
		                            {"name": data.temperature2Label, "data": temperature2}];
		
		$scope.temperatureChart = {
				options: {
					chart: {
						type: 'spline'
					}
				},
				plotOptions: {
					series: {
						stacking: ""
					}
				},
				series: $scope.temperatureSeries,
				title: {
					text: data.householdLabel
				},
				xAxis: {
					type: 'datetime',
					dateTimeLabelFormats: { // don't display the dummy year
						month: '%e. %b',
						year: '%b'
					},
					title: {
						text: 'Date'
					}
				}
		}
	});
	
	responsePromise.error(function(data) {

	});
}

gardenApp.controller('GardenDataAll', function($scope, $http, $location) {
	$scope.$watch(function() {
		return $location.search();
	}, function() {
		$scope.temperatureChart = null;
		queryData($http, $scope, ($location.search()).hid);
    });
	
});
