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

app.filter('componentFilter', function()  {
  var allChecked = function(collection) {
    var none = _.every(collection, function(item) {
      return !!!item.checked;
    });
    return none;
  };

  return function (input, filters) {
    var out = null;

    // if value passes with true it remains
    out = _.filter(input, function(item) {
      //we return true if it passes all of these filters
      return _.every(filters, function(filter) {
        var collection = filter.codes;
        var key = filter.attributeType;

        

        // if the filter isn't being used, or all are checked, we know its true
        if (!allChecked(collection)) {
          // otherwise we return true if it passes some portion of the filter
          return _.some(collection, function(checkedFilter) {
            if (checkedFilter.checked === true) {
              return _.some(item.attributes, function(attribute){
                if (attribute.type === key) {
                  return attribute.code === checkedFilter.code;
                } else {
                  return false;
                }
              });
            } else {
              // if it isn't checked we default to false
              return false;
            }
          });
        } else {
          return true;
        }
      });
    });
    return out;
  };
});
