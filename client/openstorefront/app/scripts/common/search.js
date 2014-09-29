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

var hasAttribute = function(list, attribute){
  var result = [];
  if (list && attribute) {
    _.each(list, function(item){
      if (attribute.key && attribute.type && (_.find(item.attributes, {'code': attribute.key, 'type': attribute.type}) !== undefined)){
        result.push(item);
      } else if (attribute.type && !attribute.key && (_.find(item.attributes, {'type': attribute.type}) !== undefined)) {
        result.push(item);
      }
    });
  } else {
    return [];
  }
  return result;
}

var search = function(searchKey, list) {
  console.log('got to search function');
  
  if (list && searchKey) {
    var key;
    var type;
    var score       = {};
    var result      = {};
    var keywords    = [];
    var matched     = [];
    var found       = [];
    var config      = '';
    var matchCount  = 0;
    var check       = 0;
    var temp        = 0;
    var pos         = 0;
    var i           = 0;
    var j           = 0;
    var doStrict    = false;
    if (typeof searchKey === 'object'){
      if (searchKey.type && searchKey.key) {
        if (searchKey.type.toLowerCase() !== 'search' && typeof searchKey.key === 'object') {
          type = searchKey.key.type.toLowerCase().replace(/^\s+|\s+$/g,'');
          key = searchKey.key.key? searchKey.key.key.toLowerCase().replace(/^\s+|\s+$/g,''): null;
          doStrict = true;
          var result = {};
        } else if ((searchKey.type.toLowerCase().replace(/^\s+|\s+$/g,'') === 'search' && typeof searchKey.key === 'string' && searchKey.key.toLowerCase().replace(/^\s+|\s+$/g,'') === 'all') || !searchKey) {
          result.data = list;
          return result;
        } else if (searchKey.type.toLowerCase() === 'search' && searchKey.key){
          key = searchKey.key.toLowerCase().replace(/^\s+|\s+$/g,'');
          score.text = 3;
          score.description = 2;
          score.attribute = 1;
        } 
      } else {
        result.data = list;
        return result;
      }
    } else {
      key = searchKey.toLowerCase().replace(/^\s+|\s+$/g,'');
      score.text = 3;
      score.description = 2;
      score.attribute = 1;
    }

    // matching and storing the keywords in matched
    if (doStrict) {
      matched = hasAttribute(list, searchKey.key);
      result.found = matched;
      result.data = matched;
    } else {

      while (true) {
        if (key.indexOf('+') === -1  && key.indexOf(' ') === -1 ) {
          keywords[check] = key;
          break;
        }
        pos = key.indexOf('+');
        if (pos === -1){
          pos = key.indexOf(' ');
        }

        if (key !== '+' && key !== ' ')  {
          keywords[check] = key.substring(0, pos).toLowerCase();
          check++;
        }
        else {
          check--;
          break;
        }

        key = key.substring(pos + 1, key.length).toLowerCase().replace(/^\s+|\s+$/g,'');  
        if (key.length === 0) {
          check--;
          break;
        }
      }

      var index = 0;
      for (i = 0; i < keywords.length; i++) {
        for (j = 0; j < list.length; j++) {
          var tempMatch = {}; 
          tempMatch.score = 0;
          if (list[j])
          {
            index = list[j].name.toLowerCase().indexOf(keywords[i]);
            if (index > -1 ) {
              if (index === 0) {
                tempMatch.score = tempMatch.score + score.text;
              } else {
                tempMatch.score = tempMatch.score + (score.text * 0.2);
              }
            }
            index = list[j].description.toLowerCase().indexOf(keywords[i]);
            if (index > -1 ) {
              tempMatch.score = tempMatch.score + score.description;
            }
            if (list[j].attributes){
              _.each(list[j].attributes, function(attribute){
                index = attribute.code.toLowerCase().indexOf(keywords[i]);
                if (index > -1) {
                  tempMatch.score = tempMatch.score + score.attribute;
                }
                index = attribute.type.toLowerCase().indexOf(keywords[i]);
                if (index > -1) {
                  tempMatch.score = tempMatch.score + score.attribute;
                }
              });
            }
            if (tempMatch.score) {
              var found = _.find(matched, {'data': list[j]});
              if (!found) {
                tempMatch.data = list[j];
                matched[matchCount] = tempMatch;
                matchCount++;
              } else {
                var index = _.indexOf(matched, found);
                if (index) {
                  matched[index].score = matched[index].score + tempMatch.score;
                }
              }
            }
          }
        } 
      }
      matched = _.sortBy(matched, 'score').reverse();
      console.log('matched', matched);
      result.keywords = keywords;
      result.found = matched;
      result.data = _.pluck(matched, 'data');
      console.log('result.data', result.data);
    }
    console.log('finished search function');
    
    return result;
  } else {
    return {'data':[]};
  }
}