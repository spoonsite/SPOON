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
  console.log('attribute', attribute);
  
  if (list && attribute) {
    _.each(list, function(item){
      if (attribute.key && attribute.type && (_.find(item.attributes, {'code': attribute.key, 'type': attribute.type}) !== undefined)){
        result.push({'data': item});
      } else if (attribute.type && !attribute.key && (_.find(item.attributes, {'type': attribute.type}) !== undefined)) {
        result.push({'data': item});
      }
    });
  } else {
    return [];
  }
  return result;
}

var search = function(searchKey, list) {
  console.log('searchKey', searchKey);
  
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
    if (searchKey.type.toLowerCase() !== 'search' && typeof searchKey.key === 'object') {
      type = searchKey.key.type.toLowerCase().replace(/^\s+|\s+$/g,'');
      key = searchKey.key.key? searchKey.key.key.toLowerCase().replace(/^\s+|\s+$/g,''): null;
      doStrict = true;
    } else if ((searchKey.type.toLowerCase() === 'search' && searchKey.key && searchKey.key.toLowerCase() === 'all') || !searchKey) {
      var result = {};
      result.data = list;
      return result;
    } else if (searchKey.type.toLowerCase() === 'search' && searchKey.key){
      key = searchKey.key.toLowerCase().replace(/^\s+|\s+$/g,'');
      score.text = 3;
      score.description = 2;
      score.attribute = 1;
    } 
  } else {
    key = searchKey.toLowerCase().replace(/^\s+|\s+$/g,'');
    score.text = 3;
    score.description = 2;
    score.attribute = 1;
  }



  console.log('keywords', keywords);

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

    for (i = 0; i < keywords.length; i++) {
      for (j = 0; j < list.length; j++) {
        var tempMatch = {}; 
        tempMatch.score = 0;
        if (list[j])
        {
          if (list[j].name.toLowerCase().indexOf(keywords[i]) > -1 ) {
            tempMatch.score = tempMatch.score + score.text;
          }
          if (list[j].description.toLowerCase().indexOf(keywords[i]) > -1 ) {
            tempMatch.score = tempMatch.score + score.description;
          }
          if (list[j].attributes){
            _.each(list[j].attributes, function(attribute){
              if (attribute.code.toLowerCase().indexOf(keywords[i]) > -1) {
                tempMatch.score = tempMatch.score + score.attribute;
              }
              if (attribute.type.toLowerCase().indexOf(keywords[i]) > -1) {
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
    console.log('matched', matched);
    // sort the matches  
    if (matched.length) {
      for (i = 0; i < matched.length - 1; i++) {
        for(j = i + 1; j < matched.length; j++) {
          if (matched[i].score < matched[j].score) {
            temp = matched[j];
            matched[j] = matched[i];
            matched[i] = temp;
          }
        }
      }
    }
    // end of sort
    result.keywords = keywords;
    result.found = matched;
    result.data = matched;
  }

  console.log('result', result);
  return result;
}