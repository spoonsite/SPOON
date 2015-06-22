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

app.directive('message', ['$uiModal', '$draggable', 'business', function ($uiModal, $draggable, Business) {
  return {
    transclude: true,
    restrict: 'EA',
    template: '<div></div>',
    scope: {
      modal: '@'
    },
    link: function(scope, element, attrs) {
      scope.$on('$OPENADMINMESSAGE', function(event, type, contacts, subject, message, modal) {
        scope.open('lg', type, contacts, subject, message, modal);
      })

      scope.open = function(size, type, contacts, subject, message, modal){
        console.log('here we go again');
        
        modal?
        (function(){
          var draggableInstance = $draggable.open({
            alwaysontop: true,
            templateUrl: 'views/admin/component/messagesubmitter.html',
            controller: 'adminMessageCtrlDraggable',
            size: size,
            top: 100,
            left: 100,
            closeTarget: 'theTarget',
            moveTarget: 'draggableTarget',
            id: 'messageSubmitter',
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
              },
              disabledContacts: function(){
                return modal;
              }
            }
          });

          draggableInstance.result.then(function (selectedItem) {
            scope.selected = selectedItem;
          }, function (result) {
          });
        })(size, type, contacts, subject, message)
        :
        (function (size, type, contacts, subject, message) {
          var modalInstance = $uiModal.open({
            templateUrl: 'views/admin/message/adminMessageContent.html',
            controller: 'adminMessageCtrlModal',
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
              },
              disabledContacts: function(){
                return modal;
              }
            }
          });

          modalInstance.result.then(function (selectedItem) {
            scope.selected = selectedItem;
          }, function () {
          // console.log('Modal dismissed at: ' + new Date());
        });
        })(size, type, contacts, subject, message)
      }
    }
  };
}]);

app.directive('contactList', ['$uiModal', 'business', '$q', function ($uiModal, Business, $q) {
  return {
    restrict: 'EA',
    templateUrl: 'views/admin/message/contactTemplate.html',
    scope: {
      type: '=',
      contacts: '=',
      disabled: '@'
    },
    link: function(scope, element, attrs) {

      console.log('attrs', attrs);
      
      var oldContacts;
      scope.disableTo = true;
      scope.getContactList = function() {
        var deferred = $q.defer();
        Business.userservice.getAllUserProfiles().then(function(result){
          deferred.resolve(result);
          // console.log('result', result);
        }, function(){
          deferred.reject(false);
          // console.log('There was an error getting the user profiles');
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
        if (scope.toField && scope.toField.value === 'group') {
          scope.to = scope.contacts.description;
        } else if (scope.toField && scope.toField.value === 'users') {
          scope.to = scope.contacts;
        }
        scope.oldField = scope.toField;
      } else {
        scope.toField = scope.toOptions[0];
      }


      scope.checkForToContacts = function() {
        // console.log('oldContacts', oldContacts);
        
        if (scope.toField && (scope.toField.value === 'users' || scope.toField.value === 'group')) {
          if (scope.toField.value !== scope.oldField.value) {
            oldContacts = scope.contacts;
            scope.contacts = null;
          }
          scope.getContactList().then(function(result) {
            var modalInstance = $uiModal.open({
              templateUrl: 'views/admin/message/contact.html',
              controller: 'contactCtrl',
              size: 'sm',
              resolve: {
                type: function () {
                  return scope.toField.value;
                },
                contacts: function () {
                  return scope.contacts;
                },
                size: function() {
                  return 'lg';
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
                scope.to = selectedItem;
                scope.contacts = selectedItem;
              }
              scope.oldField = scope.toField;
              oldContacts = scope.contacts;
              scope.type = scope.toField.value;
              // console.log('selected', scope.contacts);
            }, function () {
              //return to old selection.
              scope.toField = scope.oldField;
              scope.contacts = oldContacts;
              scope.type = scope.toField.value;
              // console.log('Modal dismissed at: ' + new Date());
            });
          }, function(){
            // console.log('we clicked on the contact list, but the list failed');
          });
          //
        } else {
          scope.oldField = scope.toField;
          scope.type = scope.toField.value;
        }
      }
    }
  };
}]);

var messageCtrl = function ($scope, $uiModalInstance, type, contacts, subject, message, disabledContacts, Business) {
  $scope.form = {};
  $scope.form.subjectField = subject? subject: '';
  $scope.form.type = type;
  $scope.form.contacts = contacts;
  $scope.form.disabledContacts = disabledContacts || false;
  $scope.prep = $scope.form.disabledContacts;
  $scope.form.templates = [];

  if (!$scope.form.disabledContacts) {
    $scope.editorContent = message? message: '';
    $scope.editorContentWatch;
  } else {
    $scope.message = message || [];
    $scope.editorContent = '';
    $scope.editorContentWatch;
  }


  $scope.$on('$CLOSEMSG', function(){
    $uiModalInstance.dismiss('cancel');
  })


  console.log('$scope.message', $scope.message);
  $scope.oldType;
  $scope.clearIncluded = function(type){
    if ($scope.oldType !== type) {
      _.each($scope.message.templates, function(mess){
        var found = false;
        if ($scope.form.templates && $scope.form.templates[type]){
          found = _.find($scope.form.templates[type].templates, {title: mess.title});
        }
        if (!found) {
          mess.included = false;
        } else {
          mess.included = true;
        }
      })
    } 
    $scope.oldType = type;
  }

  $scope.contin = function(){
    var template = []
    template.push('Dear ' + contacts[0].name + ',<br/><br/>');
    _.each($scope.form.templates, function(thing){
      if (thing && thing.message){
        template.push(thing.message);
      }
    });
    template.push('We appreciate your submission! Please contact us if you have any questions.<br/><br/>Sincerely,<br/>Clearinghouse Administration');
    $scope.form.editorContent = template.join('<br/>');
    $scope.editorContent = template.join('<br/>');
    $scope.form.editorContentWatch = template.join('<br/>');
    $scope.editorContentWatch = template.join('<br/>');
    $scope.prep = false;
  }

  $scope.addToTemplates = function(type, messages){
    var sections = _.pluck(messages.templates, function(item){
      if (item.included){
        return item;
      }
      return;
    });
    sections = sections.filter(function(e){return e});

    if (sections.length && messages && messages.types[type]) {
      $scope.form.templates[type] = {};
      $scope.form.templates[type].templates = sections;
      $scope.form.templates[type].message = '<strong><i>'+ messages.types[type].title + '</i></strong>';
      $scope.form.templates[type].message += '<ul>';
      _.each(sections, function(template){
        $scope.form.templates[type].message += '<li>';
        $scope.form.templates[type].message += template.title;
        $scope.form.templates[type].message += '</li>';
      })
      $scope.form.templates[type].message += '</ul>';
    } else {
      $scope.form.templates[type] = {};
    }
    $scope.clearIncluded(type);
  }

  var config = getCkConfig();
  config.height = '100px';
  $scope.editorOptions = config;

  var getUsernames = function(users) {
    var result = [];
    _.each(users, function(user) {
      result.push(user.username);
    })
    return result;
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

    

    if ($scope.form.type === 'group' && $scope.form.contacts && $scope.form.contacts.code) {
      messageObj.userTypeCode = $scope.form.contacts.code;
      messageObj.usersToEmail = [];
    } else if ($scope.form.type === 'users' && $scope.form.contacts && $scope.form.contacts.length){
      messageObj.usersToEmail = getUsernames($scope.form.contacts);
      messageObj.userTypeCode = null;
    } else {
      // we're sending to all, or there is an error
      // $uiModalInstance.dismiss('error');
    }
    // send message here
    // console.log('messageObj', messageObj);
    Business.userservice.sendAdminMessage(messageObj).then(function(result){
      // console.log('result', result);
      $uiModalInstance.close(messageObj);
      // console.log('we sent an email', messageObj);
    }, function(result){
      // console.log('result', result);
    });
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };
}
app.controller('adminMessageCtrlModal',['$scope', '$uiModalInstance', 'type', 'contacts', 'subject', 'message', 'disabledContacts', 'business', messageCtrl]);
app.controller('adminMessageCtrlDraggable',['$scope', '$draggableInstance', 'type', 'contacts', 'subject', 'message', 'disabledContacts', 'business', messageCtrl]);

app.controller('contactCtrl',['$scope', '$uiModalInstance', 'type','contacts', 'size', 'business', '$q', function ($scope, $uiModalInstance, type, contacts, size, Business, $q) {
  $scope.userTypes = {};
  $scope.userTypes.groups = [];
  $scope.type = type;
  $scope.data = {};
  $scope.size = size;
  $scope.data.selectedUsers = contacts? contacts: [];



  $scope.reverse = false;

  Business.lookupservice.getUserTypeCodes().then(function(result) {
    $scope.userTypeCodes = result;
  })

  $scope.getUserType = function(userCode) {
    var description = _.find($scope.userTypeCodes, {'code': userCode});
    if (description) {
      return description.description;
    }
    return 'End User';
  }

  $scope.data.tempTags = [];

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
      _.each($scope.userProfiles, function(user){
        console.log('user', user);
        if (user && typeof user === 'object') {
          user.text = user.firstName + ' ' + user.lastName + ' (' + user.organization + ')';
        }
      });
      if ($scope.data.selectedUsers) {
        _.each ($scope.data.selectedUsers, function(user){
          user.text = user.firstName + ' ' + user.lastName + ' (' + user.organization + ')';
          var found = _.find($scope.userProfiles, {'username': user.username});
          if (found) {
            var index = _.indexOf($scope.userProfiles, found);
            if (index > -1) {
              $scope.userProfiles.splice(index, 1);
            }
          }
        })
      }
      // console.log('userProfiles', result);
    }, function(){
      //there were no profiles?
    })
  }

  $scope.ok = function (result) {
    if (!result && $scope.type === 'users') {
      result = $scope.data.selectedUsers;
    }
    $uiModalInstance.close(result);
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.saveTags = function(tags, tag){
  };
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.addTag = function(tag, tags){
    var found;
    if ($scope.data.selectedUsers.length) {
      found = _.find($scope.data.selectedUsers, {'username': tag.username});
    } else {
      found = false;
    }
    if (!found) {
      var found = _.find($scope.userProfiles, {'username': tag.username});
      if (found) {
        var index = _.indexOf($scope.userProfiles, found);
        if (index > -1) {
          $scope.userProfiles.splice(index, 1);
        }
      }
      $scope.data.selectedUsers.push(tag);
      $scope.data.tempTags = [];
    }
  };
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.removeTag = function(tag, tags){
    var found = _.find($scope.data.selectedUsers, {'username': tag.username});
    if (found) {
      $scope.userProfiles.push(found);
      var index = _.indexOf($scope.data.selectedUsers, found);
      if (index > -1) {
        $scope.data.selectedUsers.splice(index, 1);
      }
    }
  };

  $scope.checkTagsList = function(query, list, source) {
    var deferred = $q.defer();
    var subList = null;
    if (query === ' ') {
      subList = _.reject(source, function(item) {
        return !!(_.find(list, {'text': item}));
      });
    } else {
      subList = _.filter(source, function(item) {
        if (item && typeof item === 'object'){
          return item.text.toLowerCase().indexOf(query.toLowerCase()) > -1;
        } else {
          console.log('item', item);
          return null;
        }
      });
    }
    deferred.resolve(subList);
    return deferred.promise;
  };

}]);