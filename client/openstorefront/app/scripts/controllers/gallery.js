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

/*app.controller('AdminCtrl', ['$scope', 'business', function ($scope, Business) {*/
app.controller('GalleryCtrl', ['$scope', 'business', 'localCache', '$location', 'Lightbox', function ($scope, Business, localCache, $location, Lightbox) {
/*app.controller('GalleryCtrl', function ($scope, Lightbox) {*/
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
    }
  ];
/*  $scope.images = [
    {
      'url': '1.jpg', // required property
      'width': 123,   // required property
      'height': 456,  // required property
      'thumbUrl': 'thumb1.jpg',
      'caption': 'Optional caption'
    },
    {
      'url': '2.gif',
      'width': 789,
      'height': 1012,
      'thumbUrl': 'thumb2.jpg'
    },
    {
      'url': '3.png',
      'width': 345,
      'height': 678,
      'thumbUrl': 'thumb3.png'
    }
  ];*/
  $scope.openLightboxModal = function (index) {
    console.log(Lightbox.templateUrl);
    Lightbox.openModal($scope.images, index);
  };
/*});*/


}]);