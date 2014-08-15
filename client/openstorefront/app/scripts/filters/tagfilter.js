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

/* global isEmpty, MOCKDATA2 */

app.filter('tagFilter', function () {
  return function (input, filters) {
    // if our list of tags isn't empty
    if (!isEmpty(filters)) {
      // start out with an empty result set
      var out = null;
      // and add everything that makes it through the filter
      out = _.filter(input, function(entry) {
        var temp = _.find(MOCKDATA2.componentList, {'componentId': entry.componentId});
        // it passes the filter if for every tag
        if (temp) {
          return _.every(filters, function(tagListItem) {
            // one of the tags in its tag list is the same as the one form the filter
            return _.some(temp.tags, function(tag) {
              return tagListItem.text  === tag.text;
            });
          });
        } else {
          return false;
        }
      });
      return out;
    } else {
      return input;
    }
  };
});
