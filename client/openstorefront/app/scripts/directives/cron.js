/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

app.directive('cron', ['$timeout', function ($timeout) {
  return {
    templateUrl: 'views/admin/configuration/cron.html',
    restrict: 'E',
    scope: {
      ngModel: '=',
      callback: '&'
    },
    link: function postLink(scope, element, attrs) {

      scope.check = {};
      scope.check.dayOfMonth = 1;
      scope.check.month = 1;
      scope.check.minDay = 1;
      scope.check.maxDay = 31;
      scope.$watch('check', function(value) {
        console.log('value', value);
        if (value && value.month) {
          var min = 1;
          var max = 31;
          switch(value.month){
            case "2":
            max = 29;
            break;
            case "4":
            case "6":
            case "9":
            case "11":
            max = 30;
            break;
            default:
            break;
          }
          scope.check.minDay = min;
          scope.check.maxDay = max;
        }
        if (value && value.dayOfMonth) {
          if (isNaN(value.dayOfMonth)) {
            value.dayOfMonth = 1;
          }
          if (value.dayOfMonth > scope.check.maxDay) {
            value.dayOfMonth = scope.check.maxDay;
          } else if(value.dayOfMonth < scope.check.minDay) {
            value.dayOfMonth = scope.check.minDay;
          }
        }
      }, true);
      scope.generate = function () {
        var activeTab = $(element).find('li.active').attr('heading');
        var results = "";

        switch (activeTab) {
          case "Minutes":
          results = "0 0/" + $(element).find("#MinutesInput").val() + " * 1/1 * ? *";
          break;
          case "Hourly":
          switch ($(element).find("input:radio[name=HourlyRadio]:checked").val()) {
            case "1":
            results = "0 0 0/" + $(element).find("#HoursInput").val() + " 1/1 * ? *";
            break;
            case "2":
            results = "0 " + Number($(element).find("#AtMinutes").val()) + " " + Number($(element).find("#AtHours").val()) + " 1/1 * ? *";
            break;
          }
          break;
          case "Daily":
          switch ($(element).find("input:radio[name=DailyRadio]:checked").val()) {
            case "1":
            results = "0 " + Number($(element).find("#DailyMinutes").val()) + " " + Number($(element).find("#DailyHours").val()) + " 1/" + $(element).find("#DaysInput").val() + " * ? *";
            break;
            case "2":
            results = "0 " + Number($(element).find("#DailyMinutes").val()) + " " + Number($(element).find("#DailyHours").val()) + " ? * MON-FRI *";
            break;
          }
          break;
          case "Weekly":
          var selectedDays = "";
          $(element).find("#Weekly input:checkbox:checked").each(function () { selectedDays += $(this).val() + ","; });
          if (selectedDays.length > 0)
            selectedDays = selectedDays.substr(0, selectedDays.length - 1);
          results = "0 " + Number($(element).find("#WeeklyMinutes").val()) + " " + Number($(element).find("#WeeklyHours").val()) + " ? * " + selectedDays + " *";
          break;
          case "Monthly":
          switch ($(element).find("input:radio[name=MonthlyRadio]:checked").val()) {
            case "1":
            results = "0 " + Number($(element).find("#MonthlyMinutes").val()) + " " + Number($(element).find("#MonthlyHours").val()) + " " + $(element).find("#DayOfMOnthInput").val() + " 1/" + $(element).find("#MonthInput").val() + " ? *";
            break;
            case "2":
            results = "0 " + Number($(element).find("#MonthlyMinutes").val()) + " " + Number($(element).find("#MonthlyHours").val()) + " ? 1/" + Number($(element).find("#EveryMonthInput").val()) + " " + $(element).find("#DayInWeekOrder").val() + "#" + $(element).find("#WeekDay").val() + " *";
            break;
          }
          break;
          case "Yearly":
          switch ($(element).find("input:radio[name=YearlyRadio]:checked").val()) {
            case "1":
            results = "0 " + Number($(element).find("#YearlyMinutes").val()) + " " + Number($(element).find("#YearlyHours").val()) + " " + $(element).find("#YearInput").val() + " " + $(element).find("#MonthsOfYear").val() + " ? *";
            break;
            case "2":
            results = "0 " + Number($(element).find("#YearlyMinutes").val()) + " " + Number($(element).find("#YearlyHours").val()) + " ? " + $(element).find("#MonthsOfYear2").val() + " " + $(element).find("#DayWeekForYear").val() + "#" + $(element).find("#DayOrderInYear").val() + " *";
            break;
          }
          break;
        }

        // console.log('results', results);
        
        scope.ngModel = results;
        $timeout(function(){
          scope.callback();
        });
      };
    }
  };
}]);
