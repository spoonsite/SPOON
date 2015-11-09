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
/* global Ext, CoreService */

Ext.define('OSF.component.NotificationWindow', {
  extend: 'Ext.window.Window',
  alias: 'osf.widget.NotificationWindow',
  
  title: 'Notifications',
  y: 40,
  width: '80%',
  modal: true,
  closeAction: 'hide',
  layout: 'fit',  
  height: '50%',
  maximizable: true,
  
  initComponent: function() {
     this.callParent();
     
    var notWin = this;
    notWin.notPanel = Ext.create('OSF.component.NotificationPanel', {      
    });
    notWin.notificationGrid = notWin.notPanel.notificationGrid;
      
    notWin.add(notWin.notPanel);
   
  },
  
  refreshData: function(){
    this.notificationGrid.getStore().load({
        params: {
          all: this.loadAll
        }
    });
  }
  

});
