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


// <message>
//     <img src='img/content/car.png'/>
// </message>

app.directive('message', ['$uiModal', 'business', function ($uiModal, Business) {
  return {
    transclude: true,
    restrict: 'EA',
    template: '<div></div>',
    scope: {},
    link: function(scope, element, attrs) {
      scope.$on('$OPENADMINMESSAGE', function() {
        scope.open('lg');

      })
      scope.items = ['item1', 'item2', 'item3'];

      scope.open = function (size) {
        console.log('size', size);
        
        var modalInstance = $uiModal.open({
          templateUrl: 'views/admin/message/adminMessageContent.html',
          controller: 'adminMessageCtrl',
          size: size,
          resolve: {
            items: function () {
              return scope.items;
            }
          }
        });

        modalInstance.result.then(function (selectedItem) {
          scope.selected = selectedItem;
        }, function () {
          console.log('Modal dismissed at: ' + new Date());
        });
      };
    }
  };
}]);

app.directive('contactList', ['$uiModal', 'business', '$q', function ($uiModal, Business, $q) {
  return {
    restrict: 'EA',
    template: '<h5 style="display:inline-block">To:&nbsp;</h5><span style="width: calc(100% - 28px); display: inline-block; float:right"><select class="form-control" style="display:inline-block;" ng-change="checkForToContacts()" ng-model="toField"><option ng-repeat="(key,val) in toOptions">{{val.title}}<button style="float:right;" ng-click="checkForToContacts()" class="btn btn-default btn-small" ng-if="val.refresh"><i class="fa fa-refresh"></i><button></option></select></span>',
    scope: {},
    link: function(scope, element, attrs) {
      scope.getContactList = function() {
        var deferred = $q.defer();
        Business.userservice.getAllUserProfiles().then(function(result){
          deferred.resolve(result);
          console.log('result', result);
        }, function(){
          deferred.reject(false);
          console.log('There was an error getting the user profiles');
        }) 
        return deferred.promise;
      }
      scope.getContactList();
      scope.toOptions = [
        //
        {
          'title': 'All',
          'value': 'all'
        }, {
          'title': 'Specific User Group',
          'value': 'type',
          'refresh': true
        }, {
          'title': 'Select Users',
          'value': 'users',
          'refresh': true
        }
      //
      ]
      scope.toField = scope.toOptions[0];
      scope.checkForToContacts = function() {
        if (scope.toField && (scope.toField.value === 'users' || scope.toField.value === 'type')) {
          scope.getContactList().then(function(result) {
            var modalInstance = $uiModal.open({
              templateUrl: 'views/admin/message/contact.html',
              controller: 'contactCtrl',
              size: 'sm',
              resolve: {
                type: function () {
                  return scope.toField.value;
                }
              }
            });

            modalInstance.result.then(function (selectedItem) {
              console.log('selected', selectedItem);
            }, function () {
              console.log('Modal dismissed at: ' + new Date());
            });
          }, function(){
            console.log('we clicked on the contact list, but the list failed');
          });
        }
      }
    }
  };
}]);

app.controller('adminMessageCtrl',['$scope', '$uiModalInstance', 'items', 'business', function ($scope, $uiModalInstance, items, Business) {

  $scope.items = items;
  $scope.selected = {
    item: $scope.items[0]
  };

  $scope.ok = function () {
    $uiModalInstance.close($scope.selected.item);
    console.log('we sent an email', $scope.emails);
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);

app.controller('contactCtrl',['$scope', '$uiModalInstance', 'type', 'business', function ($scope, $uiModalInstance, type, Business) {
  $scope.userTypes = {};
  $scope.userTypes.types = [];
  $scope.type = type;
  if ($scope.type === 'type') {
    Business.lookupservice.getUserTypeCodes().then(function(result){
      $scope.userTypes.types = result? result : [];
      console.log('usertypes', result);
    }, function(){
      $scope.userTypes.types = [];
      console.log('There was an error getting the user profiles');
    }) 
  }

  $scope.ok = function (result) {
    $uiModalInstance.close(result);
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);