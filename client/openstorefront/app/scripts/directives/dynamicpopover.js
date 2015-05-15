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

app.directive('dynamicPopover', ['$compile', function($compile){
  return {
    scope: {
      content: '=',
      contentString: '@'
    },
    link: function(scope, element, attrs) {
      console.log('attrs', attrs);
      console.log('scope', scope);

      // define popover for this element
      scope.$watch('content', function() {
        if (scope.content || scope.contentString) {
          var content = scope.content || scope.contentString;          
          $(element).popover({
            html: true,
            container: attrs['container'] || 'body',
            title: attrs['title'] || '', 
            content: $compile(content)(scope),
            placement: attrs['placement'] || 'top',
            trigger: attrs['trigger'] || 'click',
            // grab popover content from the next element
          });
        }
      })
    }
  }
}])
$('body').on('click', function (e) {
  $('[dynamic-popover]').each(function () {
    //the 'is' for buttons that trigger popups
    //the 'has' for icons within a button that triggers a popup
    if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
      $(this).popover('hide');
    }
  });
});