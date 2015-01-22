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

app.controller('adminEditArticlesCtrl',['$scope','business', '$uiModal', '$timeout', function ($scope, Business, $uiModal, $timeout) {
  $scope.type = $scope.$parent.type || null;
  $scope.code = $scope.$parent.code || null;
  $scope.attributeType = {};
  $scope.attributeCode = {};
  $scope.codes = {};

  // /***************************************************************
  // * If we don't have a landing page, we're going to set up one for now so that
  // * there will always be one in the editor when we look, unless we click on a button
  // * that says 'add landing page'
  // ***************************************************************/

  var compare = function (a, b) {
    return ((a.label == b.label) ? 0 : ((a.label > b.label) ? 1 : -1));
  }

  $scope.getAttributeCodes = function(override) {
    Business.articleservice.getType($scope.attributeType.type, true, true).then(function(result){
      result.codes.sort(compare);
      $scope.codes.codes = (result && result.codes)? angular.copy(result.codes): [];
      console.log('$scope.codes', $scope.codes.codes);
    }, function(){
      $scope.codes.codes = [];
    });
  }
  $scope.$watch('attributeType', function(){
    if ($scope.attributeType.type){
      console.log('$scope.attributeType.type', $scope.attributeType.type);
      $scope.getAttributeCodes();
    }
  }, true)

  $scope.getAttributes = function(override) {
    Business.getFilters(override, true).then(function(result){
      $scope.attributes = result? angular.copy(result): [];
      console.log('$scope.attributes', $scope.attributes);
      
      $timeout(function(){
        $scope.$emit('$TRIGGEREVENT', '$TRIGGERUNLOAD', 'adminAttributes');
      })
    }, function(){
      $scope.attributes = [];
    });
  }
  $scope.getAttributes(true);

  $scope.getArticles = function(override){
    Business.articleservice.getArticles(override, true).then(function(result){
      $scope.articles = result? angular.copy(result): [];
      console.log('$scope.articles', $scope.articles);
    }, function(){
      $scope.articles = [];
    })
  }
  $scope.getArticles(true);


  $scope.getArticleDesc = function(desc){
    if (desc && desc !== undefined  && desc !== null && desc !== '') {
      return desc;
    }
    return ' ';
  }

  $scope.editContent = function(article){
    var modalInstance = $uiModal.open({
      templateUrl: 'views/admin/editlandingform.html',
      controller: 'AdminEditLandingCtrl',
      size: 'lg',
      resolve: {
        article: function () {
          return article;
        }
      }
    });

    modalInstance.result.then(function (result) {
      console.log('result', result);
      $scope.getArticles(true);
      
    }, function (result) {
      console.log('result', result);
      $scope.getArticles(true);
      
    });
  }
  
  $scope.newArticle = function(){
    var found = _.find($scope.articles, {'attributeType': $scope.attributeType.type, 'attributeCode': $scope.attributeCode.code});
    var article;
    if (!found && $scope.attributeType.type && $scope.attributeCode.code){
      article = {
        attributeCode: $scope.attributeCode.code,
        attributeType: $scope.attributeType.type,
        description: '',
        title: '',
      };
    } else {
      article = found;
    }
    $scope.editContent(article);
  }

  $scope.deleteArticle = function(article){
    Business.articleservice.deleteArticle(article).then(function(result){
      console.log('result', result);
      triggerAlert('The article was deleted.', 'articleAlert', 'body', 6000)
      $scope.getArticles(true);

    })
  }

  if ($scope.type && $scope.code) {
    $scope.editContent($scope.type, $scope.code);
  }


  $timeout(function() {
    $('[data-toggle=\'tooltip\']').tooltip();
  }, 300);

}]);

app.controller('AdminEditLandingCtrl',['$scope', '$uiModalInstance', 'article', 'business', '$location', function ($scope, $uiModalInstance, article, Business, $location) {

  $scope.article = angular.copy(article);
  $scope.type = $scope.article.attributeType;
  $scope.code = $scope.article.attributeCode;
  $scope.editorContent = '';
  $scope.editorOptions = getCkConfig();

  $scope.preview = function(){
    $scope.article.html = $scope.getEditorContent();
    console.log('$scope.article', $scope.article);
    
    Business.articleservice.previewArticle($scope.article).then(function(result){
      console.log('Preview result', result);
      var url = $location.absUrl().substring(0, $location.absUrl().length - $location.url().length);
      url = url + '/landing' + '?' + $.param({
        type: 'preview',
        code: result
      });
      window.open(url, 'Aritlce_Preview_' + $scope.article.attributeType + $scope.article.attributeCode, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840');
    }, function(){
      console.log('We didnt get a preview');
      
    })
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
    console.log('$scope.editorcontent', html);
    console.log('temp', temp);
    
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
    Business.articleservice.saveArticle($scope.article).then(function(result){
      console.log('$scope.article', $scope.article);
      $uiModalInstance.close();
    }, function(){
      triggerAlert('There was an error saving your article, please try again.', 'articleAlert', 'articleEditModal', 6000);
    })
  };

  $scope.cancel = function () {
    $uiModalInstance.dismiss('cancel');
  };

  $scope.getContent = function(){
    if ($scope.type && $scope.code && !$scope.article.html) {
      console.log('type/code', $scope.type + '---' + $scope.code);
      Business.articleservice.getArticle($scope.type, $scope.code, true).then(function (result) { /*jshint unused:false*/
        $scope.editorContent = result || ' ';
      }, function(){
        $scope.editorContent = ' ';
      });
    } else {
      $scope.editorContent = $scope.article.html;
    }
  }

  $scope.getContent();

}]);

