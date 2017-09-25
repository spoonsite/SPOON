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
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){
				
				var mediaStore = Ext.create('Ext.data.Store', {
					storeId: 'mediaStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'name',
							direction: 'ASC'
						})
					],
					fields: [
						{
							name: 'updateDts',
							type:	'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						type: 'ajax',
						url: 'api/v1/resource/generalmedia',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					})
				});
				
				var mediaGrid = Ext.create('Ext.grid.Panel', {
					id: 'mediaGrid',
					title: 'Manage Media <i class="fa fa-question-circle"  data-qtip="Media that can be used for articles and badges." ></i>',
					store: mediaStore,
					columnLines: true,
					columns: [						
						{ text: 'Name', dataIndex: 'name', minWidth: 200},
						{ text: 'Resource URL', dataIndex: 'mediaLink', flex: 1, minWidth: 200, sortable: false },
						{ text: 'Original Filename', dataIndex: 'originalFileName', width: 300 },
						{ text: 'Mime Type', dataIndex: 'mimeType', width: 150},
						{ text: 'Update User', dataIndex: 'updateUser', width: 150},
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '115px',
									iconCls: 'fa fa-2x fa-refresh icon-vertical-correction-edit icon-button-color-refresh',
									tooltip: 'Refresh Grid',
									handler: function () {
										refreshGrid();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-plus icon-vertical-correction-edit icon-button-color-save',
									tooltip: 'Upload media file',
									handler: function () {
										addRecord();
									}
								}, 															
								{
									text: 'View',
									id: 'viewButton',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-eye icon-vertical-correction-view icon-button-color-view',
									disabled: true,
									tooltip: 'View media file in a viewer',
									handler: function () {
										viewRecord();
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Copy URL',
									id: 'copyButton',
									scale: 'medium',
									width: '150px',
									iconCls: 'fa fa-2x fa-clone icon-vertical-correction-edit icon-button-color-default',
									disabled: true,
									tooltip: 'Copy URL location of the media file',
									handler: function () {
										copyURL();
									}								
								},
								'->',{
									text: 'Download',
									id: 'downloadButton',
									scale: 'medium',
									width: '150px',
									iconCls: 'fa fa-2x fa-download icon-vertical-correction-edit icon-button-color-default',
									disabled: true,
									tooltip: 'Download media file',
									handler: function () {
										downloadRecord();
									}								
								},
								{
									xtype: 'tbseparator'
								},
								{	text: 'Delete',
									id: 'deleteButton',
									scale: 'medium',
									width: '110px',
									iconCls: 'fa fa-2x fa-trash icon-vertical-correction-edit icon-button-color-warning',
									disabled: true,
									tooltip: 'Delete media',
									handler: function () {
										deleteRecord();
								    }
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: mediaStore,
							displayInfo: true
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							//console.log("double click");
							downloadRecord();
						},
						selectionchange: function(grid, record, index, opts){
							//console.log("change selection");
							checkButtonChanges();
						}
					}
				});
				
				addComponentToMainViewPort(mediaGrid);									
		
				var selectedObj=null;
				
				var checkButtonChanges = function() {
					var cnt = Ext.getCmp('mediaGrid').getSelectionModel().getCount();
					if ( cnt === 1) {
						Ext.getCmp('downloadButton').setDisabled(false);
						Ext.getCmp('viewButton').setDisabled(false);
						Ext.getCmp('copyButton').setDisabled(false);
						Ext.getCmp('deleteButton').setDisabled(false);
					} else {
						Ext.getCmp('downloadButton').setDisabled(true);
						Ext.getCmp('viewButton').setDisabled(true);
						Ext.getCmp('copyButton').setDisabled(true);
						Ext.getCmp('deleteButton').setDisabled(true);						
					}
				};
				
				var refreshGrid = function() {
					Ext.getCmp('mediaGrid').getStore().load();				
				};
				
				var addRecord = function() {
					
					Ext.getCmp('addMediaForm').setLoading(false);
					addMediaWin.show();
					
					//reset form
					Ext.getCmp('addMediaForm').reset(true);
					
					
				};
				
				
				var downloadRecord = function() {
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					//download media 
					window.location.href = '' + selectedObj.mediaLink;
				};
				
				var viewRecord = function() {
				    selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					viewMediaWin.html=null;				
					
					//show display media based on mime type
					 if (selectedObj && selectedObj.mimeType){
						
						var type = selectedObj.mimeType;
					    if (type.match('video.*')) {
							viewMediaWin.setTitle('Video Preview');
					        viewMediaWin.update('<video autoplay="autoplay" controls="controls" src="'+ selectedObj.mediaLink+'" style="width: 100%;max-width:100%;" ></video>');
						}
						else if(type.match('audio.*')){
							viewMediaWin.setTitle('Audio Preview');
							viewMediaWin.setScrollable(false);
							viewMediaWin.update('<audio autoplay="autoplay" width="100%" controls="controls" src="'+ selectedObj.mediaLink+'"/>');
				
						}
						else if(type.match('image.*')) {
							// Limit to image mimes all browsers should be happy with.
							if (type.match('.*jpeg.*') || 
								type.match('.*png.*') || 
								type.match('.*gif.*') || 
								type.match('.*svg.*') ) {
								viewMediaWin.setTitle('<i class="fa fa-lg fa-eye small-vertical-correction"></i>' + ' ' + 'Image Preview');
								viewMediaWin.update('<img src="' + selectedObj.mediaLink + '" width="100%"/>');
							}
							else {
								Ext.Msg.show({
									title: 'No Preview Available',
									msg: 'No preview is available for this file format.',
									buttons: Ext.Msg.OK
								});
								return;
							}
						}
						else {
							Ext.Msg.show({
								title: 'No Preview Available',
								msg: 'No preview is available for this file format.',
								buttons: Ext.Msg.OK
							});
							return;
						}
						
						viewMediaWin.updateLayout();
						viewMediaWin.show();
						
					}
				};
				
				var copyURL = function(){
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					
					var textArea = document.createElement("textArea");
					
					textArea.style.position = 'fixed';
					textArea.style.top = 0;
					textArea.style.left = 0;
					textArea.style.width = '2em';
					textArea.style.height = '2em';
					textArea.style.padding = 0;
					textArea.style.border = 'none';
					textArea.style.outline = 'none';
					textArea.style.boxShadow = 'none';
					textArea.style.background = 'transparent';


					textArea.value = encodeURI(selectedObj.mediaLink);

					document.body.appendChild(textArea);
					
					textArea.select();
					try {
						var successful = document.execCommand('copy');
						var msg = successful ? 'successful' : 'unsuccessful';
						console.log('Copying text command was ' + msg);
					  } catch (err) {
						successful = false;
					  }

					  if (!successful) {
						var copyWindow = Ext.create('Ext.window.Window', {
							title: 'Copy Window - Select and Copy',
							modal: true,
							width: 500,
							height: 150,
							closeMode: 'destroy',
							bodyStyle: 'padding: 10px;',
							html: textArea.value,
							scrollable: true,
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											xtype: 'tbfill'
										},
										{
											text: 'Close',
											handler: function(){
												this.up('window').close();
											}
										},
										{
											xtype: 'tbfill'
										}
									]
								}
							]
						});
						copyWindow.show();						  
					  }
					  
					  document.body.removeChild(textArea);
					  Ext.toast('Link copied to clipboard.', '', 'tr');
				};
				
				var deleteRecord = function(){
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
				
					Ext.Msg.show({
						title: 'Delete Media?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete the selected media?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								Ext.getCmp('mediaGrid').setLoading(true);
								Ext.Ajax.request({
									url: 'api/v1/resource/generalmedia/'+encodeURIComponent(selectedObj.name),
									method: 'DELETE',
									success: function (response, opts) {
										Ext.getCmp('mediaGrid').setLoading(false);
										refreshGrid();
									},
									failure: function (response, opts) {
										Ext.getCmp('mediaGrid').setLoading(false);
									}
								});
							}
						}
					});
				};
				
				
				
				//
				//
				//  View Media WINDOW
				//
				//
				var viewMediaWin = Ext.create('Ext.window.Window', {
					id: 'viewMediaWin',
					title: '',
					modal: true,
					style: 'padding:5px;',
					maximizable: true,
					scrollable: true,	
					width: 600,	
					height: '50%',
					html:'',
					listeners:{
						close: function(){
							console.log("Closed Window");
							var theWin= Ext.getCmp('viewMediaWin');
							theWin.update(null);
						}
					}
				});
				
				
				//
				//
				//  Add Media Window
				//
				//
				var addMediaWin = Ext.create('Ext.window.Window', {
					id: 'addMediaWin',
					title: 'Add Media',
					iconCls: 'fa fa-lg fa-plus icon-small-vertical-correction',
					modal: true,
					width: '40%',
					height: 260,
					y: 40,
					resizable: false,
					layout: 'fit',
					items: [
						{	xtype: 'form',
							id: 'addMediaForm',
							layout:'fit',
							items: [
								{
									xtype: 'panel',
									style: 'padding: 10px;',
									layout: 'vbox',
									defaults: {
										labelAlign: 'top'
									},
									items:[
										{
											xtype: 'textfield',
											id: 'name',
											name: 'name',
											fieldLabel: 'Enter a unique name for the media file<span class="field-required" />',
											style: 'padding-top: 5px;',
											allowBlank: false,
											width: '100%'
										},
										{
											xtype: 'filefield',
											name: 'file',
											id: 'file',
											fieldLabel: 'Upload Media  (Limit of 1GB)<span class="field-required" />',
											width: '100%',
											allowBlank: false,
											listeners: {
												change: CoreUtil.handleMaxFileLimit
											}											
										}
									]
								}
							],
							 dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Upload',
										iconCls: 'fa fa-lg fa-upload icon-button-color-default',
										formBind: true,
										handler: function(){     
											Ext.getCmp('addMediaForm').setLoading(true);
											var data = Ext.getCmp('addMediaForm').getValues();

											// Check if name is unique
											var records = Ext.getStore('mediaStore').getData();
											if (records.find('name', data.name) !== null) {
												Ext.Msg.show({
													title: 'Name Not Valid',
													msg: 'Please choose a unique name for the file.',
													buttons: Ext.Msg.OK
												});
												Ext.getCmp('addMediaForm').setLoading(false);
												return;
											}

											Ext.getCmp('addMediaForm').submit({
												url: 'Media.action?UploadGeneralMedia&generalMedia.name='+data.name,
												method: 'POST',
												callback: function() {
													Ext.getCmp('addMediaForm').setLoading(false);
												},
												success: function(response, opts) {
													Ext.toast('Uploaded Successfully', '', 'tr');													
													Ext.getCmp('addMediaWin').close();													
													refreshGrid();												
												},
												failure: function(action, opts){
													var data = Ext.decode(opts.response.responseText);
													if (data.success && data.success === false){
														Ext.Msg.show({
															title: 'Upload Failed',
															msg: 'The file upload was not successful. Check that the file meets the requirements and try again.',
															buttons: Ext.Msg.OK
														});													
														refreshGrid();	
													} else {
														//false positive the return object doesn't have success
														Ext.toast('Uploaded Successfully', '', 'tr');													
														Ext.getCmp('addMediaWin').close();													
														refreshGrid();
													}
												}
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function(){
											Ext.getCmp('addMediaWin').close();
										}											
									}
								]
							}
						]
					}
					]				   
				});
			});

		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>
