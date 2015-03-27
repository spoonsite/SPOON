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

app.controller('adminEditArticlesCtrl',['$scope','business', '$uiModal', '$timeout', '$q', function ($scope, Business, $uiModal, $timeout, $q) {
  $scope.type = $scope.$parent.type || null;
  $scope.code = $scope.$parent.code || null;
  
  $scope.predicate = 'title';
  $scope.reverse = false;
  $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminArticlesEdit');

  // /***************************************************************
  // * If we don't have a landing page, we're going to set up one for now so that
  // * there will always be one in the editor when we look, unless we click on a button
  // * that says 'add landing page'
  // ***************************************************************/


  $scope.clearSort = function() {
    $scope.predicate = 'title'; 
    $scope.reverse = false;
    if(!$scope.$$phase) {
      $scope.$apply();
    }
  }

  $scope.setPredicate = function (predicate) {
    if ($scope.predicate === predicate) {
      $scope.reverse = !$scope.reverse;
    } else {
      $scope.predicate = predicate;
      $scope.reverse = false;
    }
  };


  $scope.getArticles = function(override){
    var deferred = $q.defer();
    Business.articleservice.getArticles(override, true).then(function(result){
      $scope.articles = result? angular.copy(result): [];
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
      deferred.resolve();
    }, function(){
      $scope.articles = [];
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
      deferred.resolve();
    })
    return deferred.promise;
  }
  $scope.getArticles(true);


  $scope.getArticleDesc = function(desc){
    if (desc && desc !== undefined  && desc !== null && desc !== '') {
      return desc;
    }
    return ' ';
  }

  $scope.editContent = function(type, code){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editlandingform.html',
      controller: 'AdminEditLandingCtrl',
      size: 'lg',
      backdrop: 'static',
      resolve: {
        type: function () {
          return type;
        },
        code: function () {
          return code;
        },
        articles: function () {
          return $scope.articles;
        }
      }
    });

    modalInstance.result.then(function (result) {
      $scope.getArticles(true);
    }, function (result) {
      $scope.getArticles(true);
    });
  }
  
  $scope.changeActivity = function(article){
    if (article && article.attributeType && article.attributeCode) {
      var message = "Warning: You are about to change the active status of an Attribute Code. This will activate or deactivate the code and all related metadata. Continue?";
      var conf = confirm(message);
      if (conf) {
        if (article.activeStatus === 'A') {
          $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminArticlesEdit');
          Business.articleservice.activateCode(article.attributeType, article.attributeCode).then(function(){
            $timeout(function(){
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
              $scope.getArticles(true);
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
              $scope.getArticles(true);
            }, 1000);
          })
        } else {
          Business.articleservice.deactivateCode(article.attributeType, article.attributeCode).then(function(){
            $timeout(function(){
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
              $scope.getArticles(true);
            }, 1000);
          }, function(){
            $timeout(function(){
              $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminArticlesEdit');
              $scope.getArticles(true);
            }, 1000);
          })
        }
      }
    }
  }

  $scope.importFile = function(){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/fileupload.html',
      controller: 'AdminAddMediaCtrl',
      backdrop: 'static',
      size: 'sm',
      resolve: {
        title: function () {
          return "Import Articles File";
        },
        url: function () {
          return "Upload.action?UploadArticles";
        },
        single: function () {
          return true;
        },
        alias: function () {
          return 'uploadFile';
        }
      }
    });

    modalInstance.result.then(function (result) {
      triggerAlert('Your changes to the article have been saved.', 'ArticleEditAlert', 'body', 6000);
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminArticlesEdit');
      $timeout(function(){
        $scope.getArticles(true);
      }, 1000);
    }, function (result) {
      $scope.$emit('$TRIGGEREVENT', '$TRIGGERLOAD', 'adminArticlesEdit');
      $timeout(function(){
        $scope.getArticles(true);
      }, 1000);
    });       
  };



  $scope.deleteArticle = function(article){
    var conf = confirm("You are about to remove an article permanently. Would you like to continue?");
    if (conf) {
      Business.articleservice.deleteArticle(article).then(function(result){
        triggerAlert('The article was deleted.', 'articleAlert', 'body', 6000)
        $scope.getArticles(true);
      })
    }
  }

  if ($scope.type && $scope.code) {
    $scope.getArticles(true).then(function(){
      $scope.editContent($scope.type, $scope.code, $scope.articles);
    })
  }

  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);

  var stickThatTable = function(){
    var offset = $('.top').outerHeight() + $('#editArticlesToolbar').outerHeight();
    $(".stickytable").stickyTableHeaders({
      fixedOffset: offset
    });
  }

  $(window).resize(stickThatTable);
  $timeout(stickThatTable, 100);

}]);

app.controller('AdminEditLandingCtrl',['$scope', '$uiModalInstance', 'type', 'code', 'articles', 'business', '$location', function ($scope, $uiModalInstance, type, code, articles, Business, $location) {

  $scope.type = {};
  $scope.type.type = type || null;
  $scope.code = {};
  $scope.code.code = code || null;
  $scope.articles = articles;
  $scope.article;
  $scope.attributes;
  $scope.codes;

  var compare = function (a, b) {
    return ((a.label == b.label) ? 0 : ((a.label > b.label) ? 1 : -1));
  }

  $scope.getAttributeCodes = function(override) {
    console.log('we\'re getting codes', $scope.type.type);
    if ($scope.type && $scope.type.type) {
      Business.articleservice.getType($scope.type.type, true, true).then(function(result){
        result.codes.sort(compare);
        console.log('result', result);

        $scope.codes = (result && result.codes)? angular.copy(result.codes): [];
      }, function(){
        $scope.codes = [];
      });
    } else {
      $scope.codes = [];
    }   
  }
  $scope.getAttributes = function(override) {
    Business.getFilters(override, true).then(function(result){
      $scope.attributes = result? angular.copy(result): [];
    }, function(){
      $scope.attributes = [];
    });
  }
  $scope.getAttributes(true);


  $scope.$watch('type', function(){
    if ($scope.type.type){
      console.log('The change triggered the code gathering');
      
      $scope.getAttributeCodes();
    }
  }, true)

  $scope.$watch('code', function(){
    if ($scope.code.code){
      console.log('The change triggered the code gathering');
      $scope.checkType($scope.type, $scope.code);
    }
  }, true)


  var popupWin;
  $scope.editorContent = '';
  $scope.editorOptions = getCkConfig();

  $scope.preview = function(){
    $scope.article.html = $scope.getEditorContent();
    $scope.article.attributeType = $scope.type.type;
    $scope.article.attributeCode = $scope.code.code;
    if ($scope.article && $scope.article.html && $scope.article.attributeType && $scope.article.attributeCode) {
      Business.articleservice.previewArticle($scope.article).then(function(result){
        var url = $location.absUrl().substring(0, $location.absUrl().length - $location.url().length);
        url = url + '/landing' + '?' + $.param({
          type: 'preview',
          code: result
        });
        popupWin = utils.openWindow(url, 'Aritlce_Preview_' + $scope.article.attributeType + $scope.article.attributeCode, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840', popupWin, 'articleEditModal');
      }, function(){

      })
    } else {
      triggerAlert('There is missing information required for previewing the article, please try again.', 'articleAlert', 'articleEditModal', 6000);
    }
  }

  $scope.getEditorContent = function(){
    var html = angular.copy($scope.editorContentWatch);
    var temp = null;
    if (_.contains(html, 'id="allTheDContent"')) {
      temp = $(html);
    }
    if (!temp || !temp.find('#allTheContent').length) {
      html = '<div id="allTheContent">' + html + '</div>';
      var temp = $(html);
    }
    
    var content = $('<div></div>');
    _.each(temp, function(e){
      content.append(e);
    })
    var lists = content.find('div.componentlistBody');
    if (lists.length) {
      _.each(lists, function(componentList, $index){
        // console.log('Component list $index --------------', $index);
        // console.log('componentList', componentList);
        var attributes = $(componentList).find('[data-attributeLabel]');
        var componentListElement = $('<component-list></component-list>');
        _.each(attributes, function(attribute){
          var attrib = $(attribute);
          var attr = attrib.attr('data-attributeLabel');
          var value = attrib.html();
          // console.log('label', attr);
          // console.log('value', value);
          componentListElement.attr(attr, value);
        });
        var alreadythere = $(componentList).parent().find('component-list');
        if (alreadythere.length) {
          alreadythere.remove();
        }
        $(componentList).parent().append(componentListElement);
      });
    }
    return content.find('#allTheContent').html();
  }

  $scope.ok = function () {
    // $scope.editorContentWatch = $scope.editorContent;
    $scope.article.html = $scope.getEditorContent();
    $scope.article.attributeType = $scope.type.type;
    $scope.article.attributeCode = $scope.code.code;
    if ($scope.article && $scope.article.html && $scope.article.attributeType && $scope.article.attributeCode) {
      Business.articleservice.saveArticle($scope.article).then(function(result){
        $uiModalInstance.close();
      }, function(){
        triggerAlert('There was an error saving your article, please try again.', 'articleAlert', 'articleEditModal', 6000);
      })
    } else {
      triggerAlert('There is missing information required for saving the article, please try again.', 'articleAlert', 'articleEditModal', 6000);
    }
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

  $scope.getContent = function(){
    if ($scope.type && $scope.type.type && $scope.code && $scope.code.code && $scope.article && !$scope.article.html) {
      Business.articleservice.getArticle($scope.type.type, $scope.code.code, true).then(function (result) { /*jshint unused:false*/
        $scope.editorContent = result || ' ';
      }, function(){
        $scope.editorContent = ' ';
      });
    } else if ($scope.article && $scope.article.html) {
      $scope.editorContent = $scope.article.html;
    } else {
      $scope.editorContent = ' ';
    }
  }

  $scope.checkType = function(type, code) {
    if (type && code) {
      var article = _.find($scope.articles, {'attributeCode': code, 'attributeType': type});
      if (article) {
        $scope.article = angular.copy(article);
      } else {
        $scope.article = {
          attributeCode: code,
          attributeType: type,
          description: '',
          title: '',
        };
      }
    }else {
      $scope.article = {
        attributeCode: code,
        attributeType: type,
        description: '',
        title: '',
      };
    }

    $scope.getContent();
  }
  $scope.checkType($scope.type.type, $scope.code.code);


}]);

