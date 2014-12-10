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
      scope.$on('$OPENADMINMESSAGE', function(event, type, contacts, subject, message) {
        scope.open('lg', type, contacts, subject, message);
      })

      scope.open = function (size, type, contacts, subject, message) {
        var modalInstance = $uiModal.open({
          templateUrl: 'views/admin/message/adminMessageContent.html',
          controller: 'adminMessageCtrl',
          size: size,
          resolve: {
            type: function () {
              return type;
            },
            contacts: function () {
              return contacts;
            },
            subject: function () {
              return subject;
            },
            message: function () {
              return message;
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
    templateUrl: 'views/admin/message/contactTemplate.html',
    scope: {
      type: '=',
      contacts: '='
    },
    link: function(scope, element, attrs) {

      scope.disableTo = true;
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
          'title': 'User Group',
          'value': 'group'
        }, {
          'title': 'Select Users',
          'value': 'users'
        }
      //
      ]
      if (scope.type && (scope.type === 'all' || scope.type === 'group' || scope.type === 'users')) {
        scope.toField = _.find(scope.toOptions, {'value': scope.type});
        if (scope.toField) {
          scope.to = scope.contacts.description;
        }
      } else {
        scope.toField = scope.toOptions[0];
      }
      scope.checkForToContacts = function() {
        if (scope.toField && (scope.toField.value === 'users' || scope.toField.value === 'group')) {
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
              if (scope.toField.value === 'group') {
                scope.disableTo = true;
                scope.to = selectedItem.description;
                scope.contacts = selectedItem;
              } else {
                scope.disableTo = true;
                scope.to = selectedItem.description;
                scope.contacts = selectedItem;
              }
              scope.oldField = scope.toField;
              console.log('selected', selectedItem);
            }, function () {
              //return to old selection.
              scope.toField = scope.oldField;
              console.log('Modal dismissed at: ' + new Date());
            });
          }, function(){
            console.log('we clicked on the contact list, but the list failed');
          });
          //
        }
      }
    }
  };
}]);

app.controller('adminMessageCtrl',['$scope', '$uiModalInstance', 'type', 'contacts', 'subject', 'message', 'business', function ($scope, $uiModalInstance, type, contacts, subject, message, Business) {
  $scope.type = type;
  $scope.contacts = contacts;
  $scope.form = {};
  $scope.form.subjectField = subject? subject: '';
  $scope.editorContent = message? message: '';
  $scope.editorContentWatch;

  var getUserList = function(uers) {
    if (users && user.length) {

    }
    else return [];
  }

  $scope.ok = function () {
    var messageObj = {};
    messageObj.usersToEmail = [];
    messageObj.userTypeCode = null;

    if ($scope.form.subjectField !== '') {
      removeError();
      messageObj.subject = $scope.form.subjectField;
    } else {
      var errorObj = {};
      errorObj.errors = {};
      errorObj.errors.entry = [];
      errorObj.errors.entry[0] = {
        'key': 'subjectField',
        'value': 'The subject field is required!'
      };
      errorObj.success = false;
      triggerError(errorObj);
      return;
    }

    messageObj.message = $scope.editorContentWatch;

    

    if ($scope.type === 'group' && $scope.contacts && $scope.contacts.code) {
      messageObj.userTypeCode = $scope.contacts.code;
    } else if ($scope.type === 'users' && $scope.contacts && $scope.contacts.length){
      messageObj.usersToEmail = getUserList($scope.contacts);
    } else {
      // $uiModalInstance.dismiss('error');
    }
    // send message here
    console.log('messageObj', messageObj);
    Business.userservice.sendAdminMessage(messageObj).then(function(result){
      console.log('result', result);
      $uiModalInstance.close(messageObj);
      console.log('we sent an email', messageObj);
    }, function(result){
      console.log('result', result);
    });
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);

app.controller('contactCtrl',['$scope', '$uiModalInstance', 'type', 'business', function ($scope, $uiModalInstance, type, Business) {
  $scope.userTypes = {};
  $scope.userTypes.groups = [];
  $scope.type = type;
  if ($scope.type === 'group') {
    Business.lookupservice.getUserTypeCodes().then(function(result){
      $scope.userTypes.groups = result? result : [];
      // console.log('usertypes', result);
    }, function(){
      $scope.userTypes.groups = [];
      // console.log('There was an error getting the user profiles');
    }) 
  } else if ($scope.type === 'users') {
    Business.userservice.getAllUserProfiles().then(function(result) {
      $scope.userProfiles = result? result: [];
      console.log('userProfiles', result);
    }, function(){
      //there were no profiles?
    })
  }

  $scope.ok = function (result) {
    $uiModalInstance.close(result);
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}]);