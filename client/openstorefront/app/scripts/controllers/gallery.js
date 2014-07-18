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

app.controller('GalleryCtrl', ['$scope', 'business', 'localCache', '$location', 'Lightbox', function ($scope, Business, localCache, $location, Lightbox) {
  $scope.images = [
    {
      'url': 'images/pic1full.png', // required property
      'width': 1216,   // required property
      'height': 911,  // required property
      'thumbUrl': 'images/pic1thumb.png',
      'caption': 'Optional caption'
    },
    {
      'url': 'images/pic2full.png',
      'width': 908,
      'height': 856,
      'thumbUrl': 'images/pic2thumb.png'
    },
    {
      'url': 'images/pic3full.png',
      'width': 1805,
      'height': 968,
      'thumbUrl': 'images/pic3thumb.png'
    },
    {
      'url': 'images/pic4full.png',
      'width': 980,
      'height': 756,
      'thumbUrl': 'images/pic4thumb.png',
      'caption': 'This is a street map media resource.'
    }
  ];
  $scope.openLightboxModal = function (index) {
    Lightbox.openModal($scope.images, index);
  };

}]);