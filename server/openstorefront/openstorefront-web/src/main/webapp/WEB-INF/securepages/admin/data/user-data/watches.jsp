<%-- 
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
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			
			var activeRequests = 0;
	
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				
				var watchStore = Ext.create('Ext.data.Store', {			
					sorters: [{
						property: 'componentName',
						direction: 'ASC'
					}],	
					pageSize: 200,
					autoLoad: true,
					remoteSort: true,
					fields: [
						{
							name: 'createDts',
							type:	'date',
							dateFormat: 'c'
						},
						{
							name: 'lastUpdateDts',
							type:	'date',
							dateFormat: 'c'
						},
						{
							name: 'lastViewDts',
							type:	'date',
							dateFormat: 'c'
						}	
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/userwatches?all=true',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}							
					})
				});
				
				var watchesGrid = Ext.create('Ext.grid.Panel', {
					title: 'Entry Watches  <i class="fa fa-question-circle"  data-qtip="Watches can be made on entries to provide notifications to users when update occur."></i>',
					id: 'watchGrid',
					columnLines: true,
					selModel: {
						   selType: 'checkboxmodel'        
					},					
					store: watchStore,
					bufferedRenderer: false,
					requiredPermissions: ['ADMIN-WATCHES-READ'],
					permissionCheckFailure: function () {
						Ext.toast({
							html: 'You do not have permissions to view the data on this page',
							title: 'Invalid Permissions',
							align: 'b'
						});
					},
					columns: [
						{ text: 'Entry', dataIndex: 'componentName', flex: 1, minWidth: 200, 
							renderer: function(value, meta, record) {
								if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
									return value + '<span class="updated-watch text-success"> UPDATED </span>';
								} else {
									return value;
								}
							}
						},	
						{ text: 'Entry Last Update Date', align: 'center', dataIndex: 'lastUpdateDts', width: 200,
							renderer: function(value, meta, record) {
								if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
									meta.tdCls = 'alert-success';
								}
								return Ext.util.Format.date(value, 'm/d/y H:i:s');
							}
						},
						{ text: 'Last View Date', align: 'center', dataIndex: 'lastViewDts', width: 200,
							renderer: function(value, meta, record) {
								
								return Ext.util.Format.date(value, 'm/d/y H:i:s');
							}			
						},
						{ text: 'Send Email Notification', align: 'center', dataIndex: 'notifyFlg', width: 200,
							
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{ text: 'Create Date', align: 'center', dataIndex: 'createDts', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Create User', dataIndex: 'createUser', width: 200 },
						{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 200 }													
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							itemId: 'tools',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '120px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									requiredPermissions: ['ADMIN-WATCHES-UPDATE'],
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									itemId: 'toggle',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									disabled: true,									
									requiredPermissions: ['ADMIN-WATCHES-UPDATE'],
									handler: function () {
										actionSetStatus();
									}									
								}							
							]
						}		
					],
					listeners: {	
						selectionchange: function(selectionModel, selected, opts){
							
							var tools = this.getComponent('tools');
							var toggleButton = tools.getComponent('toggle');
							
							if (selected.length > 0) {								
								
								toggleButton.setDisabled(false);

							} else {								

								toggleButton.setDisabled(true);
							}
						}
					},
					bbar: Ext.create('Ext.PagingToolbar', {
						
						store: watchStore,
						displayInfo: true,
						displayMsg: 'Displaying {0} - {1} of {2}',
						emptyMsg: "No watches to display"						
					})					
				});
				
				var actionRefresh = function() {
					
					Ext.getCmp('watchGrid').getStore().loadPage(1);
				};
				
				var actionSetStatus = function(newStatusAction) {
					
					var grid = Ext.getCmp('watchGrid');
					var records = grid.getSelectionModel().getSelection();
					
					var dataIds = [];
					
					dataIds[0] = {
						
						command: "activate",
						ids: []
					};
					
					dataIds[1] = {
						
						command: "inactivate",
						ids: []
					};

					
					Ext.Array.each(records, function(record){
						
						if (record.get("activeStatus") == 'A') {
							
							dataIds[1].ids.push(record.get('watchId'));
						} else {
							
							dataIds[0].ids.push(record.get('watchId'));
						}
					});
										
					grid.setLoading("Updating...");
					
					for (var i = 0; i < dataIds.length; i++) {
						
						if (dataIds[i].ids.length > 0) {
							
							activeRequests++;
							console.log(dataIds[i]);
							Ext.Ajax.request({

								url:'api/v1/resource/userwatches/' + dataIds[i].command,
								method: 'PUT',
								jsonData: dataIds[i],
								callback: function(){

									if (--activeRequests === 0) {
										
										grid.setLoading(false);
										actionRefresh();
									}	
								}														
							});
						}
					}					
				};
				
				addComponentToMainViewPort(watchesGrid);
				
			});
			
		</script>	
				
	</stripes:layout-component>
</stripes:layout-render>