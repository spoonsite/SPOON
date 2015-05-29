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

app.directive('readMore', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive
  var nextId2 = 1;
  return {
    scope: {},
    restrict: 'A',
    link: function postlink(scope, element, attrs) {

      var destoryReadMore = function(){
        element.trigger('destroy');
        element.find('.readmore').remove();
        element.append('<a class="readmore" href="javascript:void(0);">&laquo;&nbsp;Hide</a>');
        $timeout(function(){
          element.find('.readmore').on('click', create);
        })
      }
      var create = function(){
        element.find('.readmore').remove();
        scope.init();
      }

      scope.init = function(){

        $timeout(function(){
          element.dotdotdot({
            ellipsis  : '... ',

            wrap    : 'word',

            fallbackToLetter: true,

            after   : "a.readmore",

            watch   : true,

            height    : 55,

            tolerance : 0,

            callback  : function( isTruncated, orgContent ) {
              scope.orgContent =  orgContent;
              if (isTruncated) {
                element.append('<a class="readmore" href="javascript:void(0);">Read More&nbsp;&raquo;</a>');
              }
              $timeout(function(){
                element.find('.readmore').on('click', destoryReadMore)
              })

            },

            lastCharacter : {

              remove    : [ ' ', ',', ';', '.', '!', '?' ],

              noEllipsis  : []
            }
          })
        })//
      }//
      scope.init();
    }
  };
}]);
