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

app.directive('dynamicPopover', ['$compile', '$timeout', function($compile, $timeout){
  return {
    scope: {
      content: '=',
      contentString: '@',
      styleOverride: '@'
    },
    link: function(scope, element, attrs) {
//        console.log("test style", scope.styleOverride);
      scope.$on('$routeChangeStart', function(){
//          console.log("Destroy Popover Route Change");
          element.popover('destroy');
      });
        
      var addPopover = function(){
//          console.log("Adding Popover");
          if(typeof scope.styleOverride === "undefined")
          {
            scope.styleOverride = 'z-index:500; min-width:400px;';    
          }
//            console.log("test style", scope);

          if (scope.content || scope.contentString) {
              var content = scope.content || scope.contentString;
          $(element).popover({
            html: true,
            container: attrs['container'] || 'body',
            title: function(){
              return attrs['title'] || ''
            }, 
            content: $compile(content)(scope),
            placement: attrs['placement'] || 'top',
            trigger: attrs['trigger'] || 'click',
            template: '<div class="popover" role="tooltip" style="'+scope.styleOverride+'"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
          });
          $timeout(function(){
            element.popover('show');
            // $timeout(function(){
            //   element.popover('hide');
            //   $timeout(function(){
            //     element.popover('show');
            //   }, 100)
            // },100)
          },500);
        }
        $('body').on('click', function (e) {
          if (!element.is(e.target) && element.has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
//             console.log("destroy popover");
             element.popover('destroy');
          }
        });
      };
      element.on('click', addPopover);
    }
  };
}]);
