/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* global Ext */

var CoreService = {
  
  usersevice: {    
    
    getCurrentUser: function(forceReload){
      var me = this;     
    
      //for now don't cache    
  
      var promise = Ext.Ajax.request({
        url: 'api/v1/resource/userprofiles/currentuser'
      });
      
      return promise;
    }
    
  },
  systemservice: {
    
    getConfigProperties: function(){
      var me = this;     
            
      var promise = Ext.Ajax.request({
        url: 'api/v1/service/application/configproperties'
      });
      
      return promise;
    }    
    	
  },
  brandingservice: {
	  
	  getCurrentBranding: function(){
		  var me = this;
		  
		  var promise = Ext.Ajax.request({
			url: 'api/v1/resource/branding/current' 
		  });
		  return promise;
	  },
	  
	  getBranding: function(brandingId){
	
		  var promise = Ext.Ajax.request({
			url: 'api/v1/resource/branding/' + brandingId 
		  });
		  return promise;		  
	  }
  },
  attributeservice: {
	  attributeCache: [],	  
	  
	  /**
	   * Always call this to warm the cache before use
	   * Due to async nature this may not be ready when it's need.  If timing is critical use the promise.
	   * @returns promise
	   */
	  warmCache: function() {
		var promise  = Ext.Ajax.request({
			url: 'api/v1/resource/attributes',
			success: function(response, opts) {
				var attributes = Ext.decode(response.responseText);
				CoreService.attributeservice.attributeCache = attributes;
			}
		});
		return promise;
	  },
	  
	  /**
	   * Translate type to description
	   * @param {type} attributeType
	   * @returns description
	   */
	  translateType: function(attributeType) {
		  var  attributeCache = CoreService.attributeservice.attributeCache;
		  
		  var attributeTypeTranslated = attributeType;
		  Ext.Array.each(attributeCache, function(type){
			  if (type.attributeType === attributeType) {
				  attributeTypeTranslated = type.description;
			  }
		  });
		  return attributeTypeTranslated;
	  },
	  
	  /**
	   * Translate code to label
	   * @param {type} attributeType
	   * @param {type} attributeCode
	   * @returns label
	   */
	  translateCode: function(attributeType, attributeCode) {
		  var  attributeCache = CoreService.attributeservice.attributeCache;
		  
		  var attributeCodeTranslated = attributeCode;
		  Ext.Array.each(attributeCache, function(type){
			  if (type.attributeType === attributeType) {
				  Ext.Array.each(type.codes, function(code){
					if (code.code === attributeCode) {
						attributeCodeTranslated = code.label; 
					}  
				  });
			  }
		  });		 
		  return attributeCodeTranslated;
	  }
	  
  }
  
  
};

