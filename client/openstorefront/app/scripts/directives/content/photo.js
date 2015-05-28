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

app.directive('photo', ['$timeout', function($timeout) {
  // id needs to be unique across all implementations of the directive.
  return {
    restrict: 'E',
    scope:{
      photos: '='
    },
    templateUrl: 'views/content/photo.html',
    link: function postlink(scope, element, attrs) {
      console.log('attrs', attrs);
      $timeout(function(){

        var carousel = element.find('.carousel');
        carousel.cycle({
          slides:'> div.item',
          fx: attrs['fx'],
          timeout: 0,
          caption:'.caption',
          next:"#next",
          prev:"#prev",
          allowWrap: (attrs['wrap'] === 'true')? true: false,
          slideCount: scope.photos.length || 0,
          _nextBoundry: scope.photos.length || 0
        });
        $timeout(function(){
          carousel.cycle('stop');
        })
      })
    }
  };
}]);
