<%-- 
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

    Document   : supportMedia.jsp
    Created on : Dec 18, 2017, 12:41:43 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">		
	
		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function() {
				
				var openAddEditWindow = function(record) {
					
					var addEditWin = Ext.create('Ext.window.Window', {
						title: 'Add/Edit Support Media',
						iconCls: 'fa fa-edit',
						modal: true,
						width: '60%',
						closeAction: 'destroy',
						height: 650,
						layout: 'fit',
						requiredPermissions: ['ADMIN-SUPPORT-MEDIA-CREATE', 'ADMIN-SUPPORT-MEDIA-UPDATE'],
						items: [
							{
								xtype: 'form',
								itemId: 'form',
								scrollable: true,
								layout: 'anchor',
								bodyStyle: 'padding: 20px;',
								defaults: {
									width: '100%',
									labelAlign: 'top'									
								},
								items: [
									{
										xtype: 'hidden',
										name: 'supportMedia.supportMediaId'
									},
									{
										xtype: 'textfield',
										fieldLabel: 'Title <span class="field-required" />',
										name: 'supportMedia.title',
										allowBlank: false,
										maxLength: 255
									},
									{
										xtype: 'numberfield',
										fieldLabel: 'Order Number <span class="field-required" />',
										name: 'supportMedia.orderNumber',
										minValue: 0,
										maxValue: 999999,
										maxLength: 6
									},
									Ext.create('OSF.component.StandardComboBox', {
										name: 'supportMedia.mediaType',									
										allowBlank: false,								
										margin: '0 0 5 0',
										editable: false,
										typeAhead: false,
										width: '100%',
										fieldLabel: 'Media Type <span class="field-required" />',
										value: 'VID',
										storeConfig: {
											url: 'api/v1/resource/lookuptypes/MediaType',
											listeners: {
												load: function(store, records, successful, operation, opts) {
													Ext.defer(function(){
														var mediaForm = addEditWin.queryById('form');
														mediaForm.isValid();
													}, 250);
												}
											}
										}
									}),									
									{
										xtype: 'textarea',
										fieldLabel: 'Description',
										name: 'supportMedia.description',
										height: 200,
										maxLength: 4000
									},
									{
										xtype: 'fileFieldMaxLabel',
										resourceLabel: 'Upload Media (Required on Add)',
										itemId: 'file',
										name: 'file',
										allowBlank: true
									}
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Save',
												formBind: true,
												scale: 'medium',
												iconCls: 'fa fa-2x fa-save icon-button-color-save icon-vertical-correction',
												handler: function() {
													var form = this.up('form');
													var data = form.getValues();
													
													var fieldFile = form.queryById('file');
													
													if (fieldFile.getValue()) {														
														if (data.supportMediaId !== '') {
															delete data.supportMediaId;
														}
														
														form.setLoading(true);	
														form.submit({
															url: 'Media.action?UploadSupportMedia',
															method: 'POST',
															callback: function() {
																form.setLoading(false);
															},
															success: function(formAction, opts) {
																Ext.toast('Uploaded Successfully');
																actionRefresh();
																addEditWin.close();
															},
															failure: function(action, opts){
																var data = Ext.decode(opts.response.responseText);
																
																if (data.success && data.success === false){
																	Ext.Msg.show({
																		title: 'Upload Failed',
																		msg: 'The file upload was not successful. Check that the file meets the requirements and try again.',
																		buttons: Ext.Msg.OK
																	});																														
																} else {
																	//false positive the return object doesn't have success
																	Ext.toast('Uploaded Successfully', '', 'tr');
																}
																actionRefresh();
																addEditWin.close();
															}
														});
														
														
													} else {
														if (data['supportMedia.supportMediaId'] && data['supportMedia.supportMediaId'] !== '') {
															form.setLoading('Saving Support Media...');
															
															var supportMedia = {	
																supportMediaId: data['supportMedia.supportMediaId'],
																title: data['supportMedia.title'],
																description: data['supportMedia.description'],
																mediaType: data['supportMedia.mediaType'],
																orderNumber: data['supportMedia.orderNumber']
															};
															
															Ext.Ajax.request({
																url: 'api/v1/resource/supportmedia/' + supportMedia.supportMediaId,
																method: 'PUT',
																jsonData: supportMedia,
																callback: function() {
																	form.setLoading(false);
																},
																success: function(response, opts) {
																	Ext.toast('Updated Successfully');
																	actionRefresh();
																	addEditWin.close();
																}
															});
														
														} else {															
															//file is required on add
															Ext.Msg.show({
																title:'Valiation',
																message: 'File is required when Adding',
																buttons: Ext.Msg.OK,
																icon: Ext.Msg.ERROR,
																fn: function(btn) {
																}
															});
														}
													}
													
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Cancel',
												scale: 'medium',
												iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
												handler: function() {
													addEditWin.close();
												}												
											}
										]
									}
								]
							}
						]
					});
					addEditWin.show();
					
					if (record) {
						var mediaForm = addEditWin.queryById('form');
						
						var formRecord = Ext.create('Ext.data.Record', {							
						});
						formRecord.set({
							'supportMedia.supportMediaId':  record.get('supportMediaId'),
							'supportMedia.title':  record.get('title'),
							'supportMedia.orderNumber':  record.get('orderNumber'),
							'supportMedia.mediaType':  record.get('mediaType'),
							'supportMedia.description':  record.get('description')
						});
						mediaForm.loadRecord(formRecord);						
					
					}
				};
				
				
				var supportGrid = Ext.create('Ext.grid.Panel', {
					id: 'supportGrid',
					title: 'Support Media <i class="fa fa-question-circle" data-qtip="Allows for managing media for training and tutorials." ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'orderNumber',
								direction: 'ASC'
							})
						],
						fields: [
							{ name: 'mimeType', mapping: function(data) {
								return data.file.mimeType;	
							}},
							{ name: 'originalName', mapping: function(data) {
								return data.file.originalName;	
							}},
							{
								name: 'updateDts',
								type:	'date',
								dateFormat: 'c'
							}						
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/supportmedia'
						}
					},
					columns: [	
						{ text: 'Id', dataIndex: 'supportMediaId', width: 200, hidden: true },
						{ text: 'Media Type', dataIndex: 'mediaTypeDescription', width: 200 },
						{ text: 'Title', dataIndex: 'title', width: 200 },
						{ text: 'Description', dataIndex: 'description', flex: 1, minWidth: 250 },
						{ text: 'Original Filename', dataIndex: 'originalName', width: 200 },
						{ text: 'Mime Type', dataIndex: 'mimeType', width: 200 },
						{ text: 'Order Number', dataIndex: 'orderNumber', width: 200 },
						{ text: 'Update User', dataIndex: 'updateUser', width: 150, hidden: true  },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s', hidden: true }
					],
					listeners: {
						selectionchange: function(selModel, selected, opts) {
							var tools = Ext.getCmp('supportGrid').getComponent('tools');
							if (selected.length > 0) {	
								tools.getComponent('view').setDisabled(false);
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('download').setDisabled(false);															
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('view').setDisabled(true);
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('download').setDisabled(true);							
								tools.getComponent('delete').setDisabled(true);
							}							
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									itemId: 'refresh',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									requiredPermissions: ['ADMIN-SUPPORT-MEDIA-CREATE'],
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									itemId: 'add',									
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									requiredPermissions: ['ADMIN-SUPPORT-MEDIA-CREATE'],
									handler: function () {
										actionAdd();										
									}									
								},								
								{
									text: 'Edit',								
									itemId: 'edit',
									scale: 'medium',
									width: '100px',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									requiredPermissions: ['ADMIN-SUPPORT-MEDIA-UPDATE'],
									handler: function () {
										actionEdit(Ext.getCmp('supportGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbseparator'
								},								
								{
									text: 'View Training',
									itemId: 'view',
									scale: 'medium',
									width: '180px',
									disabled: true,
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										actionView(Ext.getCmp('supportGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									text: 'Download',
									itemId: 'download',
									scale: 'medium',
									width: '180px',
									disabled: true,
									iconCls: 'fa fa-2x fa-download icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										actionDownload(Ext.getCmp('supportGrid').getSelectionModel().getSelection()[0]);										
									}									
								},								
								{
									requiredPermissions: ['ADMIN-SUPPORT-MEDIA-DELETE'],
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									autoEl: {
										'data-test': 'supportDeleteBtn'
									},
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									requiredPermissions: ['ADMIN-SUPPORT-MEDIA-DELETE'],
									handler: function() {
										actionDelete(Ext.getCmp('supportGrid').getSelectionModel().getSelection()[0]);	
									}
								}
							]
						}
					]
				});
				
				var actionRefresh = function() {
					supportGrid.getStore().load();					
				}; 
				
				var actionAdd = function() {
					openAddEditWindow();
				};

				var actionEdit = function(record) {
					openAddEditWindow(record);
				};
				
				var actionDelete = function(record) {
										
					Ext.Msg.show({
						title:'Delete Support Media',
						message: 'Are you sure want to delete support media: ' + record.get('title') + ' ?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {										
								supportGrid.setLoading('Deleting Support Media...');
								Ext.Ajax.request({
									url: 'api/v1/resource/supportmedia/' + record.get('supportMediaId'),
									method: 'DELETE',
									callback: function(){
										supportGrid.setLoading(false);
									},
									success: function(response, opts){
										actionRefresh();
									}
								});											
							}
						}
					});					
					
				};				
				
				var actionView = function(record) {
					
					//open the view component 
					var supportWin = Ext.create('OSF.component.SupportMediaWindow', {		
						selectedMediaId: record.get('supportMediaId'),
						collapseSelect: true
					});
					supportWin.show();
				};
				
				var actionDownload = function(record) {
					
					Ext.toast('Downloading Data ...');					
					window.location.href = 'Media.action?SupportMedia&mediaId=' + record.get('supportMediaId');
				};			
				
				addComponentToMainViewPort(supportGrid);
				
			});
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>				
