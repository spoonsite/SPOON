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
					columns: [
						{ text: 'Entry', dataIndex: 'componentName', flex: 1, minWidth: 200, 
							renderer: function(value, meta, record) {
								if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
									meta.tdCls = 'alert-success';
									return value + '<br>*Updated';
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
								if (record.get('lastUpdateDts') > record.get('lastViewDts')) {
									meta.tdCls = 'alert-success';
								}
								return Ext.util.Format.date(value, 'm/d/y H:i:s');
							}			
						},
						{ text: 'Send Email Notification', align: 'center', dataIndex: 'notifyFlg', width: 200,
							renderer: function(value, meta, record) {
								if (value) {
									meta.tdCls = 'alert-success';
									return '<i class="fa fa-lg fa-check"></i>';
								} else {
									meta.tdCls = 'alert-danger';
									return '<i class="fa fa-lg fa-close"></i>';
								}
							}
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
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Activate',
									itemId: 'activate',
									scale: 'medium',
									disabled: true,									
									handler: function () {
										actionSetStatus('/activate');;
									}									
								},
								{
									text: 'Inactivate',
									itemId: 'inactivate',
									scale: 'medium',
									disabled: true,									
									handler: function () {
										actionSetStatus('/inactivate');
									}									
								}								
							]
						}		
					],
					listeners: {	
						selectionchange: function(selectionModel, selected, opts){
							var tools = this.getComponent('tools');

							if (selected.length > 0) {								
								var countActive = 0;								
								Ext.Array.each(selected, function(record){
									if (record.get('activeStatus') === 'A') {
										countActive++;
									}
								});		
								
								tools.getComponent('activate').setDisabled(true);
								tools.getComponent('inactivate').setDisabled(true);
								
								if (countActive === selected.length) {
									//all active
									tools.getComponent('inactivate').setDisabled(false);
								} else if (countActive === 0) {
									//all inactive
									tools.getComponent('activate').setDisabled(false);
								}
								
							} else {								
								tools.getComponent('activate').setDisabled(true);
								tools.getComponent('inactivate').setDisabled(true);
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
					var records = Ext.getCmp('watchGrid').getSelectionModel().getSelection();
					
					var ids = [];
					Ext.Array.each(records, function(record){
						ids.push(record.get('watchId'));
					});
					
					var dataIds = {
						ids: ids
					}
										
					grid.setLoading("Updating...");
					Ext.Ajax.request({
						url:'api/v1/resource/userwatches' + newStatusAction,
						method: 'PUT',
						jsonData: dataIds,
						callback: function(){
							grid.setLoading(false);
						},
						success: function(){
							actionRefresh();
						}														
					});					
				};
				
				addComponentToMainViewPort(watchesGrid);
				
			});
			
		</script>	
				
	</stripes:layout-component>
</stripes:layout-render>