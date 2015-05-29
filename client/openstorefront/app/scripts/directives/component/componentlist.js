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




/***************************************************************
* TODO:: Make this directive work in the modal on the results page.
***************************************************************/
app.directive('componentList', ['localCache', 'business', '$timeout', '$location', function (localCache, Business, $timeout, $location) {/*jshint unused:false*/
  var uniqueId = 1;
  var getTemplateUrl = function(element, attrs) {
    var mode = attrs.mode || null;
    if (mode && mode === 'compare') {
      return 'views/componentList/compare.html';
    } else {
      return 'views/componentList/componentList.html';
    }
  };

  return {
    templateUrl: getTemplateUrl,
    restrict: 'AE',
    scope: {
      data: '=',
      filters: '=',
      clickCallback: '=',
      blankTarget: '=',
      hideMore: '@',
      // list: '=',
      // search: '&',
      // setFilters: '=',
    },
    link: function postLink(scope, element, attrs) {
      if (scope.data) {

      }
      
      scope.getShortDescription = getShortDescription;

      if (scope.search && (!scope.search.type || !scope.search.code)) {
        scope.search = {};
        scope.search.type = 'search';
        scope.search.code = 'all';
      }
      var list = [];

      /***************************************************************
      * Here we are setting up the id's of different sections so that our current
      * 'more' button implementation will work correctly.
      *
      * The problem is that directives shar scope unless implicitly told that they
      * don't, and then when they don't share scope, they just reset every time they're
      * made anyway, so they end up looking like copies... (which is a problem for id's)
      ***************************************************************/
      var item = 'componentList' + uniqueId++;
      //now we set up the scope variables
      scope.hasMoreThan3 = false;
      scope._scopename = 'componentList';
      scope.isShownClass = null;
      scope.isTitle = false;
      scope.listOfClasses = attrs.classList;

      var popupWin = false;

      if (scope.clickCallback === undefined) {
        scope.clickCallback =  function(id, article) {
          if (scope.blankTarget){
            return;
          }
          var root = '';
          var name = '';
          var args = '';
          root = $location.absUrl().replace($location.url(), '');
          if (root.slice(-1) === '/') {
            root = root.slice(0, -1);
          }
          if (article && article.listingType === 'Article') {
            localCache.save('type', article.articleAttributeType);
            localCache.save('code', article.articleAttributeCode);
            // $scope.$emit('$TRIGGERUNLOAD', 'fullDetailsLoader');
            root = root + '/landing';
            name = 'Landing_Page_' + id;
            args = 'scrollbars';
          } else {
            root = root + '/single?id=' + id;
            name = 'Component_' + id;
            args = 'window settings';
          }
          popupWin = utils.openWindow(root, name, args, popupWin);
        };
      } else if (scope.blankTarget) {
        scope.clickCallback = function(){
          return;
        }
      }

      scope.goToCompare = function() {
        $location.search({
          'id': list
        });
        $location.path('/compare');
      };

      scope.showitem = function(item){
        var type = (attrs.type !== null && attrs.type !== undefined && attrs.type !== '')? attrs.type: null;
        var code = (attrs.code !== null && attrs.code !== undefined && attrs.code !== '')? attrs.code: null;
        if (type && code && item.listingType === 'Article') {
          return !(item.articleAttributeType === type && item.articleAttributeCode === code);
        }
        return true;
      }

      scope.addToCompare = function(id) {
        if (_.contains(list, id)){
          var index = list.indexOf(id);
          if (index > -1) {
            list.splice(index, 1);
          }
        } else {
          list.push(id);
        }
        scope.list = list;
      };

      scope.getComponentScroll = function(item) {
        if (item.componentId) {
          return 'componentScroll'+item.componentId.replace(/\W/g, '');
        } else {
          return 'componentScroll'+item.attributes[0].type.replace(/\W/g, '')+item.attributes[0].code.replace(/\W/g, '');
        }
      }

      // scope.$watch('data', function() {
      //
      if (scope.data && scope.data.length) {
        element.find('.hideMore').attr('id', item);
        item = 'read_more' + uniqueId++;
        element.find('input.read_more').attr('id', item);
        element.find('label.read_more').attr('for', item);
        // $timeout(function(){
        //   // if (scope.filters && scope.setFilters) {
        //   //   if (scope.setFilters.length && scope.$parent.filters.length) {
        //   //     _.each(scope.setFilters, function(set) {
        //   //       var filter = _.find(scope.$parent.filters, {'type': set.type});
        //   //       if (filter && filter.codes.length) {
        //   //         var code = _.find(filter.codes, {'code': set.code});
        //   //         if (code) {
        //   //           code.checked = true;
        //   //         }
        //   //       }
        //   //     });
        //   //   }
        //   //   scope.data = scope.$parent.applyFilters(scope.data);
        //   //   if (scope.data) {
        //   //     if(scope.data.length > 3) {
        //   //       scope.hasMoreThan3 = true;
        //   //     } else {
        //   //       scope.hasMoreThan3 = false;
        //   //     }
        //   //     scope.addMore();
        //   //   }
        //   //   scope.init();
        //   // }
        // }, 10);
      } //
      // }, true);



      /***************************************************************
      * This function converts a timestamp to a displayable date
      ***************************************************************/
      scope.getDate = function(date){
        return utils.getDate(date);
      };


      var timer = null;
      attrs.$observe('type', function(value){
        scope.setupData();
      });
      attrs.$observe('code', function(value){
        scope.setupData();
      });

      /***************************************************************
      * Here we handle attribute set ups. If an attribute is set, it will most likely
      * override something else.
      ***************************************************************/
      scope.setupData = function() {
        if (timer) {
          clearTimeout(timer);
        } else { 
          setTimeout(function() {
            if (attrs.type !== null && attrs.type !== undefined && attrs.type !== '') {
              var code = (attrs.code !== null && attrs.code !== undefined && attrs.code !== '')? attrs.code: null;
              scope.search = {'type': 'attribute', code:{'type': attrs.type, 'key': code}};
              var architecture = null;

              var filter = _.find(scope.filters, {'type': attrs.type});

              if (filter){
                architecture = filter.architectureFlg;
              }
              Business.componentservice.doSearch(scope.search.type, scope.search.code, architecture).then(function(result){
                if (result)
                {
                  if (result.data && result.data.length > 0) { 
                    scope.data = angular.copy(result.data);
                  } else {
                    scope.data = [];
                  }
                } else {
                  scope.data = [];
                }
                $timeout(function(){
                  scope.init();
                  $timeout(function(){
                    scope.$apply();
                  })
                })
              }, function(){
                scope.data = [];
                $timeout(function(){
                  scope.init();
                  $timeout(function(){
                    scope.$apply();
                  })
                })
              });
            } else {
              $timeout(function(){
                scope.init();
                $timeout(function(){
                  scope.$apply();
                })
              })
            }
            if (attrs.title !== null && attrs.title !== undefined && attrs.title !== '') {
              scope.isTitle = true;
              scope.title = attrs.title;
            }
            if (attrs.list !== null && attrs.list !== undefined && attrs.list !== '') {
              scope.showCompare = true;
            }
          }, 100);
        } //
      }
      scope.setupData();

      scope.getWidth = function(item){
        var count = 0;
        _.each(item.attributes, function(thing){
          if (thing.badgeUrl) {
            count++;
          }
        })
        return count * 40;

      }

      /***************************************************************
      * This funciton gives the correct component list the active class that
      * will open the content to full status.
      ***************************************************************/
      scope.setShown = function() {
        var id = element.find('.hideMore').attr('id');
        $('#' + id).toggleClass('active');
      };

      /***************************************************************
      * This function watches the scope variable for 'hidemore', if it changes
      * we need to recall the 'addMore' function
      ***************************************************************/
      scope.$watch('hideMore', function() {
        scope.addMore();
      });

      /***************************************************************
      * This function sets up the classes that handle the 'readmore'
      * capabilities
      ***************************************************************/
      scope.addMore = function() {
        if (scope.hideMore !== undefined && scope.hideMore !== null && scope.hasMoreThan3){
          scope.listOfClasses = ' min ' + scope.listOfClasses;
          element.find('.hideMore').addClass('moreContent');
          element.find('.hideMoreArticle').addClass('moreContentArticle');
          element.find('.moreSection').each(function() {
            $(this).addClass('moreContentSection');
          });
          scope.showMore = true;
        } else {
          element.find('.hideMore').removeClass('moreContent');
          element.find('.hideMoreArticle').removeClass('moreContentArticle');
          element.find('.moreSection').each(function() {
            $(this).removeClass('moreContentSection');
          });
        }
      };

      /***************************************************************
      * This function sets up the tooltips that are available on this page.
      ***************************************************************/
      scope.init = function() {
        $('[data-toggle=\'tooltip\']').tooltip();
      };

      $timeout(function(){
        element.find('.shortDescription').dotdotdot({
          /*  The text to add as ellipsis. */
          ellipsis  : '... ',

          /*  How to cut off the text/html: 'word'/'letter'/'children' */
          wrap    : 'word',

          /*  Wrap-option fallback to 'letter' for long words */
          fallbackToLetter: true,

          /*  jQuery-selector for the element to keep and put after the ellipsis. */
          after   : null,

          /*  Whether to update the ellipsis: true/'window' */
          watch   : true,

          /*  Optionally set a max-height, if null, the height will be measured. */
          height    : 150,

          /*  Deviation for the height-option. */
          tolerance : 0,

          /*  Callback function that is fired after the ellipsis is added,
          receives two parameters: isTruncated(boolean), orgContent(string). */
          callback  : function( isTruncated, orgContent ) {},

          lastCharacter : {

            /*  Remove these characters from the end of the truncated text. */
            remove    : [ ' ', ',', ';', '.', '!', '?' ],

            /*  Don't add an ellipsis if this array contains 
            the last character of the truncated text. */
            noEllipsis  : []
          }
        })
      })//

    }//
  };
}]);
