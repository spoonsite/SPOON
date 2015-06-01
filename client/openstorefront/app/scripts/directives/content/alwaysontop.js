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

app.directive('alwaysontop', ['$document', '$timeout', function($document, $timeout) {
  return {
    restrict: 'A',
    link: function(scope, element, attr) {
      element.addClass('alwaysOnTop');
      var css = element.css('left');
      console.log('css', css);
      
      element.css({
        position: 'fixed',
        background: 'rgba(0, 0, 0, .6)',
        border: '1px solid black',
        zIndex: '20000'
      })
      scope.$on('$APPEND', function(event, args, elem){
        $timeout(function(){

          var first = $('body .alwaysOnTop:first');
          $('body .alwaysOnTop').last().nextAll().each(function(){
            console.log(this);
            $(this).insertBefore(first);
          });
        });
      })
    }
  };
}]);