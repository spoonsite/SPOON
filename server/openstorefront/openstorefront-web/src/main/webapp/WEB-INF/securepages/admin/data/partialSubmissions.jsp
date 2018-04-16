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

    Document   : entryTemplate
    Created on : Mar 21, 2016, 2:43:11 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>
				
		<script src="scripts/component/templateBlocks.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/mediaViewer.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/relationshipVisualization.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/reviewWindow.js?v=${appVersion}" type="text/javascript"></script>
		<script src="scripts/component/questionWindow.js?v=${appVersion}" type="text/javascript"></script>
		
		<div style="display:none; visibility: hidden;" id="templateHolder"></div>
		<form id="viewForm" action="Template.action?PreviewTemplate"  method="POST">		
			<input type="hidden" name="templateContents" id="viewContent" />
		</form>
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){			
				


				var templateGrid = Ext.create('Ext.grid.Panel', {
					id: 'templateGrid',
					title: 'Partial Submissions <i class="fa fa-question-circle"  data-qtip="Allows for managing partial entry submissions" ></i>',
					store: {
						fields: [
							{
								name: 'createDts',
								type:	'date',
								dateFormat: 'c'
							},								
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}							
						],						
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/componenttypetemplates', //GET partial submissions
							extraParams: {
								all: true
							}							
						}						
					},
					columnLines: true,
					columns: [						
						{ text: 'Submission Name', dataIndex: 'name', flex: 1, minWidth: 200 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Submission User', dataIndex: 'createUser', width: 150 },
						{ text: 'Submission Date', dataIndex: 'submissionDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },						
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'View',
									id: 'lookupGrid-tools-view',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction',
									handler: function () {
										actionView(Ext.getCmp('templateGrid').getSelection()[0]);
									}
								}, 								
								{
									text: 'Reassign',
									id: 'lookupGrid-tools-reassign',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									handler: function () {
										actionReassign(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'lookupGrid-tools-delete',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function () {
										actionDelete(Ext.getCmp('templateGrid').getSelection()[0]);
									}								
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},
						selectionchange: function(grid, record, index, opts){
							checkEntryGridTools();
						}
					}					
				});
				
				addComponentToMainViewPort(templateGrid);				

				var checkEntryGridTools = function() {
					if (Ext.getCmp('templateGrid').getSelectionModel().getCount() === 1) {
						Ext.getCmp('lookupGrid-tools-reassign').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-view').setDisabled(false);
						Ext.getCmp('lookupGrid-tools-delete').setDisabled(false);					
					} else {
						Ext.getCmp('lookupGrid-tools-reassign').setDisabled(true);
						Ext.getCmp('lookupGrid-tools-view').setDisabled(true);	
						Ext.getCmp('lookupGrid-tools-delete').setDisabled(true);
					}
				};
				
				var actionRefresh = function() {
					Ext.getCmp('templateGrid').getStore().reload();
				};

				var actionView = function(record) {
					console.log("Action view partial template");
				}

				var actionReassign = function(record) {
					console.log("Reassign view partial template");
				}

				var actionDelete = function(record){
					
					//check if delete is possible
					Ext.getCmp('templateGrid').setLoading("Checking delete...");
					Ext.Ajax.request({
						url: 'api/v1/resource/componenttypetemplates/' + record.get('templateId') + '/attached',
						method: 'GET',
						callback: function(){
							Ext.getCmp('templateGrid').setLoading(false);
						},
						success: function(response, opts){
							var attachedRecords = Ext.decode(response.responseText);
							if (attachedRecords.length > 0) {
								var entryTypeNames = [];
								Ext.Array.each(attachedRecords, function(entryType) {
									entryTypeNames.push(entryType.label);
								});
								Ext.Msg.alert('Template in Use', 'The template is being use by the following entry type(s):<br> ' + entryTypeNames.join());								
								
							} else {							
								Ext.Msg.show({
									title:'Delete Template?',
									iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
									message: 'Are you sure you want to delete ' + Ext.util.Format.ellipsis(record.get('name'), 20) + '?',
									buttons: Ext.Msg.YESNOCANCEL,
									icon: Ext.Msg.QUESTION,
									fn: function(btn) {
										if (btn === 'yes') {
											Ext.getCmp('templateGrid').setLoading("Deleting...");
											Ext.Ajax.request({
												url: 'api/v1/resource/componenttypetemplates/' + record.get('templateId') + '/force',
												method: 'DELETE',
												callback: function(){
													Ext.getCmp('templateGrid').setLoading(false);
												},
												success: function(response, opts){
													actionRefresh();
												}
											});
										}
									}
								});
							}
						}
					});
					
				};

			});
			
		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>