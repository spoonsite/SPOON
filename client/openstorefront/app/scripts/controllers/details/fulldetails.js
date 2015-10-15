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


app.controller('DetailsFulldetailsCtrl', ['$rootScope', '$scope', 'business', '$location', 'Lightbox', '$timeout', '$q', '$uiModal', function ($rootScope, $scope, Business, $location, Lightbox, $timeout, $q, $uiModal) { /*jshint unused:false*/

  $scope.sendEvent          = $rootScope.sendEvent;
  $scope.user               = {};
  $scope.editQuestion       = [];
  $scope.currentTab         = null;
  $scope.sendAdminMessage   = $rootScope.openAdminMessage;
  $scope.currentAttribute   = null;

  resetUpdateNotify();
  $scope.predicate = [];
  $scope.reverse = [];
  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
    if (table === 'components') {
      $scope.pagination.control.changeSortOrder(predicate);
    }
  };
  $scope.setupTagList = $scope.setupTagList || function() {
    Business.getTagsList(true).then(function(result) {
      if (result) {
        $scope.tagsList       = result;
        $scope.tagsList.sort();
      } else {
        $scope.tagsList       = null;
      }
    });
  }
  $scope.setupTagList();

  $scope.checkTagsList = $scope.checkTagsList || function(query, list, source) {
    var deferred = $q.defer();
    var subList = null;
    if (query === ' ') {
      subList = _.reject(source, function(item) {
        return !!(_.where(list, {'text': item}).length);
      });
    } else {
      subList = _.filter(source, function(item) {
        return item.toLowerCase().indexOf(query.toLowerCase()) > -1;
      });
    }
    deferred.resolve(subList);
    return deferred.promise;
  };

  Business.userservice.getWatches().then(function(result) {
    if (result) {
      $scope.watches = result;
      if ($scope.details && $scope.details.details && $scope.details.details.componentId){
        var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});
        if (found) {
          $scope.details.details.watched = true;
        } else {
          $scope.details.details.watched = false;
        }
      }
    } else {
      $scope.watches = null;
    }
  });
  Business.lookupservice.getEvalLevels().then(function(result){
    if (result) {
      $scope.evalLevels = result;
    } else {
      $scope.evalLevels = null;
    }
  });

  Business.userservice.getCurrentUserProfile().then(function(result){
    if (result) {
      $scope.user.info = result;
    }
  });

  Business.lookupservice.getEvaluationSections().then(function(result) {
    $scope.evalSectionDescriptionMap = result? result : [];
    
  })

  $scope.goTo = function(location){
    
    $location.path(location.path);
    $location.search(location.search);
  }

  $scope.title = "DemoCtrl";
  $scope.d3Data = [
  {name: "Greg", score:98},
  {name: "Ari", score:96},
  {name: "Loser", score: 48}
  ];
  $scope.d3OnClick = function(item){
    alert(item.name);
  };

  $scope.setCurrentAttribute = function(row){
    $scope.currentAttribute = row;
  }

  /***************************************************************
  * This function is used by the reviews section in the details to remove
  * and add the ellipsis
  ***************************************************************/
  $scope.toggleclass = function(id, className) {
    toggleclass(id, className);
  };

  $scope.setComponentId = function(id) {
    var deferred = $q.defer();
    $rootScope.refId = id;
    deferred.resolve();
    return deferred.promise;
  }

  $scope.resetWatches = function(hard) {
    if ($scope.user.info && $scope.user.info.username) {
      Business.userservice.getWatches($scope.user.info.username, hard).then(function(result){
        if (result) {
          $scope.watches = result;
          if ($scope.details && $scope.details.details && $scope.details.details.componentId){
            var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});
            if (found) {
              $scope.details.details.watched = true;
            } else {
              $scope.details.details.watched = false;
            }
          }
        }
      }, function(){
      });
    } else {
      Business.userservice.getCurrentUserProfile().then(function(result){
        if (result) {
         Business.userservice.getWatches(result.username, hard).then(function(result){
          if (result) {
            if (!$scope.user) {
              $scope.user = {};
            }
            if (!$scope.user.info) {
              $scope.user.info = result;
            }
            $scope.watches = result;

            if ($scope.details && $scope.details.details && $scope.details.details.componentId){
              var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});
              if (found) {
                $scope.details.details.watched = true;
              } else {
                $scope.details.details.watched = false;
              }
            }
          }
        }); 
       } else {
        $scope.watches = {};
      }
    }, function(){
      
    })
    }
  }
  $scope.resetWatches(false);
  $scope.$on('$updatedWatches', function(event){/*jshint unused:false*/
    $scope.resetWatches(true);
  });

  if ($scope.modal) {
    $scope.$watch('modal.modalBody', function() {
      $scope.modalBody = $scope.modal.modalBody;
    });
  }

  /***************************************************************
  * This function does the route redirection to the user profile path in order
  * to allow the user to view their watches.
  ***************************************************************/
  $scope.getEvaluationState = function () {
    if ($scope.details && $scope.details.details && $scope.details.evaluationLevel !== undefined) {
      var code = $scope.details.details.evaluationLevel[0].code;
      var stateFilter = _.where($scope.filters, {'type': 'DI2ELEVEL'})[0];
      var item = _.where(stateFilter.codes, {'code': code})[0];
      return item.type;
    }
    return '';
  };
  $scope.getEvaluationState();

  /***************************************************************
  * This function is used by our image modal.
  ***************************************************************/
  $scope.openLightboxModal = function (index, imageArray) {
    Lightbox.openModal(imageArray, index);
  };

  $scope.openFilePreview = function(file, files){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/content/filepreview.html',
      controller: 'OpenFilePreviewCtrl',
      backdrop: 'static',
      backdropClass: 'noModalBackgrounds',
      windowClass: 'noModalBackgrounds',
      size: 'lg',
      resolve: {
        file: function(){
          return file;
        },
        files: function(){
          return files;
        }
      }
    });

    modalInstance.result.then(function (result) {
    }, function (result) {
    });
  }

  /***************************************************************
  * This function take the reviews array from the component details and sets up
  * all the information that is required for the summary. It is triggered by a change
  * in the details.details.reviews object. So it should be fairly resposive if a user
  * adds a review to the details. (which currently isn't implemented)
  ***************************************************************/
  $scope.setupReviewSummary = function(){
    var total = [0, 0, 0, 0, 0];
    var pros = {};
    var cons = {};
    var prosList = [];
    var consList = [];
    var recommend = 0;

    //count the stuff
    _.each($scope.details.details.reviews, function(review){
      total[review.rating] = total[review.rating]? total[review.rating] + 1: 1;
      if (review.recommend) {
        recommend = recommend + 1;
      }
      if (review.pros[0] !== undefined) {
        _.each(review.pros, function(pro){
          prosList.push(pro);
        });
      }
      if (review.cons[0] !== undefined) {
        _.each(review.cons, function(con){
          consList.push(con);
        });
      }
    });

    //Grab all of the different pros and cons
    if (prosList.length){
      prosList.map( function (a) {
        if (a.text in pros) {
          pros[a.text] ++;
        } else {
          pros[a.text] = 1;
        }
      });
    } else {
      pros = null;
    }
    if (consList.length){
      consList.map( function (a) {
        if (a.text in cons) {
          cons[a.text] ++;
        } else {
          cons[a.text] = 1;
        }
      });
    } else {
      cons = null;
    }

    // create our result object
    var result = {
      'total': 0,
      'count': 0
    };
    
    for ( var key = 0; key < total.length; key++) {
      result.total = result.total + (key * total[key]);
      result.count = result.count + total[key];
    }

    result.score = result.total / result.count;
    result.recommend = recommend;
    result.recommendPercent = recommend / result.count * 100;

    //set up our review summary
    $scope.reviewSummary = {
      'ratings': total,
      'total': result,
      'pros': pros,
      'cons': cons
    };
  };

  /***************************************************************
  * This function adds a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.addToWatches = function(id){
    var watch = {};
    watch.lastViewDts = new Date().toISOString();
    watch.notifyFlg = false;
    watch.componentId = id;
    if ($scope.user.info) {
      Business.userservice.saveWatch($scope.user.info.username, watch).then(function(result){
        if (result) {
          $scope.$emit('$TRIGGEREVENT', '$updatedWatches');
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', id);
        }
      });
    }
  };


  $scope.updateWatch = function(watch){
    var watchId = watch.watchId;
    delete watch.componentName;
    delete watch.lastUpdateDts;
    delete watch.watchId;
    delete watch.createDts;
    delete watch.$$hashKey;
    if ($scope.user.info) {
      Business.userservice.saveWatch($scope.user.info.username, watch, watchId).then(function(result){
        if (result) {
          $scope.$emit('$TRIGGEREVENT', '$updatedWatches');
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', watch.componentId);
        }
      });
    }
  }
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.saveTags = function(id, tags, tag){
    Business.componentservice.saveTags(id, tags).then(function(result){
      $scope.$emit('$TRIGGEREVENT', '$REFRESHTAGLIST');
      $scope.$emit('$TRIGGEREVENT', '$CHANGESEARCHRESULTTAGS', id, tags);
    }, function(result){

    });
    if ($scope.applyFilters){
      $scope.applyFilters();
    }
  };
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.addTag = function(id, tag, tags){
    var found = _.find($scope.details.details.tags, {'text': tag.text});
    if (!found) {
      Business.componentservice.addTag(id, tag).then(function(result){
        $scope.details.details.tags.push(tag);
        $scope.$emit('$TRIGGEREVENT', '$REFRESHTAGLIST');
        $scope.$emit('$TRIGGEREVENT', '$CHANGESEARCHRESULTTAGS', id, tags);
        $scope.tempTags = [];
        if ($scope.applyFilters){
          $scope.applyFilters();
        }
      }, function(result){
        if ($scope.applyFilters){
          $scope.applyFilters();
        }
        $scope.tempTags = [];
      });
    } else {
      $scope.tempTags = [];
    }
  };
  
  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.removeTag = function(id, tag, tags){
    var found = _.find($scope.details.details.tags, {'text': tag.text});
    if (found) {
      Business.componentservice.removeTag(id, tag).then(function(result){
        $scope.$emit('$TRIGGEREVENT', '$REFRESHTAGLIST');
        $scope.$emit('$TRIGGEREVENT', '$CHANGESEARCHRESULTTAGS', id, tags);
        var index = _.indexOf($scope.details.details.tags, found);

        if (index > -1) {
          $scope.details.details.tags.splice(index, 1);
        }
        $scope.tempTags = [];

        if ($scope.applyFilters){
          $scope.applyFilters();
        }
      }, function(result){
        $scope.tempTags = [];
        if ($scope.applyFilters){
          $scope.applyFilters();
        }
      });
    } else {
      $scope.tempTags = [];
    }
  };

  //Thing needs an property called 'username' that contains the userID for whoever
  // you want to send the message to.
  $scope.messageUser = function(thing) {
    Business.userservice.getUserByUsername(thing.username).then(function(result){
      if (result && typeof result !== 'array') {
        var temp = [];
        temp.push(result);
        result = temp;
      }
      if (result && result.length) {
        $scope.sendAdminMessage('users', result, '', '');
      } else {
        triggerAlert('You are unable to send a message to this user. (They could be deactivated or without an email address)', 'failedMessage', 'body', '8000')
      }
    })
  }

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.getEvalDescription = function(col){
    var section = _.find($scope.evalSectionDescriptionMap, {'description': col.name});
    return section? section.detailedDescription: '';
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.grabEvaluationMessage = function(statusCode, actual, estimated){
    var result = '';
    return utils.calcEvalStatus(statusCode, actual, estimated);
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.getEval = function(levelCode){
    var level;
    for(var i = 0; i < $scope.evalLevels.length; i++) {
      if ($scope.evalLevels[i].attributeCodePk && $scope.evalLevels[i].attributeCodePk.attributeCode === levelCode) {
        level = $scope.evalLevels[i];
        break;
      }
    }    
    return level;
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.checkForImportants = function(array){
    return _.some(array, function(item){
      return item.importantFlg;
    });
  };
  
  /***************************************************************
  * This function creates the image array required by the gallery
  ***************************************************************/
  $scope.getImages = function(imageArray){
    _.each(imageArray, function(image){
      var img = new Image();
      img.onload = function() {
        image.width = this.width;
        image.height = this.height;
      };
      img.src = image.link;
    });
    return imageArray;
  };

  /***************************************************************
  * This function converts a timestamp to a displayable date
  ***************************************************************/
  $scope.getDate = function(date, monthYear){
    return utils.getDate(date, monthYear);
  };

  /***************************************************************
  * This function saves a component's tags
  ***************************************************************/
  $scope.toggleTags = function(id){
    $('#data-collapse-tags').toggleClass('collapsed');
    $(id).collapse('toggle');
    $(id).on('hidden.bs.collapse', function(){
      $scope.tagsOn = false;
      $scope.$apply();
    })
    $(id).on('shown.bs.collapse', function(){
      $scope.tagsOn = true;
      $scope.$apply();
    })
  };

  /***************************************************************
  * This function removes a component to the watch list and toggles the buttons
  ***************************************************************/
  $scope.removeFromWatches = function(id){
    if ($scope.watches && $scope.watches.length > 0) {
      var watch = _.find($scope.watches, {componentId: id});
      if ($scope.user.info && watch.watchId) {
        Business.userservice.removeWatch($scope.user.info.username, watch.watchId).then(null, function(result){
          $scope.$emit('$TRIGGEREVENT', '$updatedWatches');
          $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', id);
        });
      }
    }
  };

  $scope.getTimes = function(n){
    var floated = parseFloat(n);
    if (!isNaN(floated)) {
      var num = Math.round(floated);
      if (!isNaN(num)){
        return new Array(num);
      } else {
        return new Array(0);
      }
    } else {
      return new Array(0);
    }
  };

  /***************************************************************
  * This function is triggered by the '$descModal' event.
  ***************************************************************/
  $scope.$on('$descModal', function(event) { /*jshint unused: false*/
    // re-initialize the modal content here if we must
    if ($scope.modal && $scope.modal.nav) {

      if ($rootScope.current) {
        $scope.modal.nav.current = $rootScope.current;
      } else {
        $scope.modal.nav.current = 'Write a Review';
      }
    } else if ($scope.modal){
      $scope.modal.nav = {};
      $scope.modal.nav.current = $rootScope.current;
    }
  });

  $scope.deleteQuestion = function(questionId) {
    Business.componentservice.deleteQuestion($scope.details.details.componentId, questionId).then(function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
    },function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
    });
  };

  $scope.deleteResponse = function(responseId, questionId) {
    Business.componentservice.deleteResponse($scope.details.details.componentId, responseId, questionId).then(function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
    },function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
    });
  };

  $scope.deleteReview = function(reviewId) {
    Business.componentservice.deleteReview($scope.details.details.componentId, reviewId).then(function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
      $scope.$emit('$TRIGGEREVENT', '$newReview');
    },function(result) {
      $scope.$emit('$TRIGGEREVENT', '$detailsUpdated', $scope.details.details.componentId);
      $scope.$emit('$TRIGGEREVENT', '$newReview');
    });
  };

  var updateList = [];

  $scope.getIsUpdated = function(item)
  {
    return _.contains(updateList, item);
  };
  
  /***************************************************************
  * This function sets up the 'show updated flags' and creates the tooltips.
  ***************************************************************/  
  var setupUpdateFlags = function(){
    // remove old tooltips so that when we setup the update flags, they'll be fresh
    _.each(updateList, function(list){
      $('#'+list+'Update').tooltip('destroy');
    });
    // reset the update list
    updateList = [];
    $scope.summaryUpdated = [];
    $scope.detailsUpdated = [];
    $scope.reviewsUpdated = [];
    $scope.commentsUpdated = [];

    if ($scope.details.details.lastViewedDts !== undefined) {
      var component = $scope.details.details;
      // use this date if you want to test last viewed stuff
      // var lastViewedDts = sqlToJsDate('2013-01-12T12:12:12.000');
      var lastViewedDts = sqlToJsDate(component.lastViewedDts);
      var shown = false;
      _.each(component.componentMedia, function(media){
        
        if (!shown && sqlToJsDate(media.updateDts) > lastViewedDts) {
          //media.updateDts is more recent. We should show it as updated
          updateList.push('media');
          if(notInCollection($scope.summaryUpdated, 'Media')) {
            $scope.summaryUpdated.push('Media');
          }
          shown = true;
        }
      });
      shown = false;
      _.each(component.tags, function(tag){
        if (!shown && sqlToJsDate(tag.updateDts) > lastViewedDts) {
          if (!$('#tagsUpdate').hasClass('in')) {
            $scope.toggleTags('#tagsUpdate');
          }
          updateList.push('tags');
          shown = true;
        }
      });
      _.each(component.reviews, function(review, index){
        if (sqlToJsDate(review.updateDate) > lastViewedDts) {
          updateList.push('reviews'+index);
          if(notInCollection($scope.reviewsUpdated, 'Review By: '+review.username)) {
            $scope.reviewsUpdated.push('Review By: '+review.username);
          }
        }
      });
      _.each(component.resources, function(resource, index){
        if (sqlToJsDate(resource.updateDts) > lastViewedDts) {
          updateList.push('resources'+index);
          if(notInCollection($scope.detailsUpdated, 'Component Artifacts')) {
            $scope.detailsUpdated.push('Component Artifacts');
          }
        }
      });
      _.each(component.questions, function(question, index){
        if (sqlToJsDate(question.updateDts) > lastViewedDts) {
          updateList.push('questions'+index);
          if(notInCollection($scope.commentsUpdated, 'Question By: '+question.username)) {
            $scope.commentsUpdated.push('Question By: '+question.username);
          }
        }
      });
      shown = false;
      _.each(component.metadata, function(data){
        if (!shown && sqlToJsDate(data.updateDts) > lastViewedDts) {
          updateList.push('metadata');
          if(notInCollection($scope.detailsUpdated, 'Component Vitals')) {
            $scope.detailsUpdated.push('Component Vitals');
          }
          shown = true;
        }
      });
      if (sqlToJsDate(component.evaluation.updateDts) > lastViewedDts) {
        _.each(component.evaluation.evaluationSections, function(section, index){
          if (sqlToJsDate(section.updateDts) > lastViewedDts) {
            updateList.push('evaluationSections');
            if(notInCollection($scope.summaryUpdated, 'Evaluation Sections')) {
              $scope.summaryUpdated.push('Evaluation Sections');
            }
          }
        });
        shown = false;
        _.each(component.evaluation.evaluationSchedule, function(schedule, index){
          if (!shown && sqlToJsDate(schedule.updateDts) > lastViewedDts) {
            updateList.push('evaluationSchedule');
            if(notInCollection($scope.detailsUpdated, 'Evaluation Schedule')) {
              $scope.detailsUpdated.push('Evaluation Schedule');
            }
            shown = true;
          }
        });
        updateList.push('evaluation');
      }
      _.each(component.dependencies, function(dependency, index){
        if (sqlToJsDate(dependency.updateDts) > lastViewedDts) {
          updateList.push('dependencies'+index);
        }
      });
      _.each(component.contacts, function(contact, index){
        if (sqlToJsDate(contact.updateDts) > lastViewedDts) {
          updateList.push('contacts'+index);
          if(notInCollection($scope.detailsUpdated, 'Contacts')) {
            $scope.detailsUpdated.push('Contacts');
          }
        }
      });

      _.each(component.attributes, function(attribute, index){
        if (sqlToJsDate(attribute.updateDts) > lastViewedDts) {
          updateList.push('attributes'+index);
          if(notInCollection($scope.summaryUpdated, 'Attributes')) {
            $scope.summaryUpdated.push('Attributes');
          }
          if(notInCollection($scope.detailsUpdated, 'Component Vitals')) {
            $scope.detailsUpdated.push('Component Vitals');
          }
        }
      })

      $timeout(function() {
        var settings={
          trigger: 'hover',
          toggle: 'tooltip',
          placement: 'left',
          container: 'body',
          title:'Updated!',
          template: '<div class="tooltip removeOnChange" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
        }
        _.each(updateList, function(updateId){
          if (updateId === 'evaluationSections')
          {
            settings.placement = 'top';
            $('#'+updateId+'Update').tooltip(settings);
            settings.placement = 'left';
          } else {
            $('#'+updateId+'Update').tooltip(settings);
          }
        })
        settings.placement = 'bottom';
        settings.html = true;
        if ($scope.summaryUpdated.length > 0){
          var title = 'The Summary Tab was updated: <br/>'+$scope.summaryUpdated.join('<br/>');
          settings.title = title;
          $('#summaryTab').tooltip(settings);
        }
        if ($scope.detailsUpdated.length > 0){
          var title = 'The Details Tab was updated: <br/>'+$scope.detailsUpdated.join('<br/>');
          settings.title = title;
          $('#detailsTab').tooltip(settings);
        }
        if ($scope.reviewsUpdated.length > 0){
          var title = 'The Reviews Tab was updated: <br/>'+$scope.reviewsUpdated.join('<br/>');
          settings.title = title;
          $('#reviewsTab').tooltip(settings);
        }
        if ($scope.commentsUpdated.length > 0){
          var title = 'The Q&A Tab was updated: <br/>'+$scope.commentsUpdated.join('<br/>');
          settings.title = title;
          $('#qaTab').tooltip(settings);
        }
      });




    }
  }

  $scope.changeTab = function(tab) {
    $scope.currentTab = tab.id;
  }

  $scope.$watch('currentTab', function() {
    if ($scope.currentTab && $scope.currentTab === 'detailsTab') {
      $scope.$emit('$TRIGGEREVENT', '$UPDATESCHEDULECOLUMNS');
    }
  });

  $scope.init = function() {
    $('.component-details [data-toggle=\'tooltip\']').tooltip();
  }

  var onlyOnce = null;

  /***************************************************************
  * This function watches the details object for changes
  ***************************************************************/
  $scope.$watch('details', function () {
    if ($scope.details){
      if ($scope.details.details) {
        var found = _.find($scope.watches, {'componentId': $scope.details.details.componentId});
        if (found) {
          $scope.details.details.watched = true;
        } else {
          $scope.details.details.watched = false;
        }
        if ($scope.details.details.reviews) {
          if ($scope.details.details.reviews[0] !== undefined) {
            $scope.setupReviewSummary();
          } else {
            $scope.reviewSummary = null;
          }
        }
        // setup the update list.
        if (onlyOnce !== $scope.details.details.componentId) {
          $timeout(function() {
            $scope.init();
          }, 300);
          setupUpdateFlags();
          onlyOnce = $scope.details.details.componentId;
          if ($scope.details.details.componentType === 'COMP' || !$scope.details.details.componentType) {
            $scope.detailResultsTabs = [
              //
              { title:'DETAILS', id:'detailsTab', content:'2', relpath:'views/details/details.html', class:$scope.detailsUpdated.length > 0? 'updatedTab' : ''},
              { title:'REVIEWS', id:'reviewsTab', content:'3', relpath:'views/details/reviews.html', class:$scope.reviewsUpdated.length > 0? 'updatedTab' : ''},
              { title:'Q&A', id:'qaTab', content:'4', relpath:'views/details/comments.html', class:$scope.commentsUpdated.length > 0? 'updatedTab' : ''}
              // { title:'QUESTIONS & ANSWERS', content:'4', relpath:'views/details/comments.html', class:"questionandanswer" },
            //
            ];
          }else if ($scope.details.details.componentType === 'ARTICLE') {
            $scope.detailResultsTabs = [
              //
              { title:'DETAILS', id:'detailsTab', content:'2', relpath:'views/details/detailsarticles.html', class:$scope.detailsUpdated.length > 0? 'updatedTab' : ''},
              // { title:'REVIEWS', id:'reviewsTab', content:'3', relpath:'views/details/reviews.html', class:$scope.reviewsUpdated.length > 0? 'updatedTab' : ''},
              { title:'Q&A', id:'qaTab', content:'4', relpath:'views/details/comments.html', class:$scope.commentsUpdated.length > 0? 'updatedTab' : ''}
              // { title:'QUESTIONS & ANSWERS', content:'4', relpath:'views/details/comments.html', class:"questionandanswer" },
            //
            ];
          }

          $scope.currentTab = $scope.detailResultsTabs[0].id;
          $scope.selectedTab = $scope.detailResultsTabs[0];
          $scope.evaluationDetails($scope.details.details.attributes);
        }
      }
    }
  }, true);

$scope.evaluationAttributes = {};
$scope.evaluationAttributes.exist = false;
$scope.evaluationDetails = function (attributes) {
  if (attributes) {
    _.forEach(attributes, function (item) {
      if (item.type === 'DI2ELEVEL') {
        $scope.evaluationAttributes.exist = true;
        $scope.evaluationAttributes.level = item;
      }
      if (item.type === 'DI2ESTATE') {
        $scope.evaluationAttributes.exist = true;
        $scope.evaluationAttributes.state = item;
      }
      if (item.type === 'DI2EINTENT') {
        $scope.evaluationAttributes.exist = true;
        $scope.evaluationAttributes.intent = item;
      }
    });
  }
};



}]);


app.controller('OpenFilePreviewCtrl',['$scope', '$uiModalInstance', 'file', 'files', 'business', '$location', function ($scope, $uiModalInstance, file, files, Business, $location) {

  $scope.file = file || {};
  $scope.files = files || [];

  $scope.current = _.find($scope.files, $scope.file);
  if ($scope.current) {
    var index = _.indexOf($scope.files, $scope.current);
    if (index) {
      $scope.initialSlide = index;
    }
  }

  $scope.ok = function () {
    $uiModalInstance.close();
  };

  $scope.cancel = function () {
    
    $uiModalInstance.dismiss('cancel');
  };

}]);