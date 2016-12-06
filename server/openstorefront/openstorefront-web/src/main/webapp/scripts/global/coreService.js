/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
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

