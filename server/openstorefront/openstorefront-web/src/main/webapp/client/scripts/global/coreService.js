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
        url: '/openstorefront/api/v1/resource/userprofiles/currentuser'
      });
      
      return promise;
    }
    
  },
  systemservice: {
    
    getConfigProperties: function(){
      var me = this;     
            
      var promise = Ext.Ajax.request({
        url: '../api/v1/service/application/configproperties'
      });
      
      return promise;
    }    
    
  }
  
  
  
};

