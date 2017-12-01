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
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>
	
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				
				var archiveGrid = Ext.create('Ext.grid.Panel', {
					title: 'System Archives &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="Manage data archives"></i>',
					columnLines: true,
					store: {
						autoLoad: true,
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
							},
							{
								name: 'startDts',
								type:	'date',
								dateFormat: 'c'
							},
							{
								name: 'completedDts',
								type:	'date',
								dateFormat: 'c'
							},	
							{
								name: 'percentComplete',
								mapping: function (data) {
									var percent = 0;
									if (data.totalRecords > 0) {
										percent = data.recordsProcessed / data.totalRecords;
										if (percent === Infinity) percent = 0;
									}
									return percent;
								}
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/systemarchives'							
						},
						listeners: {
							load: function(store, records, opts) {
								var autoRefresh = false;
								store.each(function(record){
									if (record.get('runStatus') === 'P' || record.get('runStatus') === 'W') {
										autoRefresh = true;
									}
								});
								
								if (autoRefresh) {
									Ext.defer(function(){
										actionRefresh();
									}, 2000);
								}
							}
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'name', flex: 1, minWidth: 200 },
						{ text: 'Type', dataIndex: 'systemArchiveType', align: 'center', width: 150,
							renderer: function(value, meta, record) {
								return record.get('systemArchiveTypeDescription');
							}
						},
						{ text: 'Import/Export', dataIndex: 'ioDirectionType', align: 'center', width: 150,
							renderer: function(value, meta, record) {
								return record.get('ioDirectionTypeDescription');
							}
						},	
						{ text: 'Filename', dataIndex: 'originalArchiveFilename', width: 200, sortable: false,
							renderer: function(value, meta, record) {
								if (record.get('originalArchiveFilename')) {
									return record.get('originalArchiveFilename');
								} else {
									return record.get('archiveFilename');
								}
							}
						},						
						{ text: 'Run Status', dataIndex: 'runStatus', align: 'center', width: 150,							
							renderer: function(value, meta, record) {
								if (value === 'E') {
									meta.tdCls = 'alert-danger';
								}
								if (value === 'P') {
									meta.tdCls =  'alert-info';
								}								
								return record.get('runStatusDescription');
							}
						},
						{ text: 'Details', 
						  columns: [
							{ text: 'Start Date', dataIndex: 'startDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
							{ text: 'Complete Date', dataIndex: 'completedDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
							{ text: 'Status', dataIndex: 'statusDetails', width: 200 },
							{ text: 'recordsProcessed', dataIndex: 'recordsProcessed', width: 200, hidden: true },
							{ text: 'Total Records', dataIndex: 'totalRecords', width: 200, hidden: true },							
							{
								text: 'Progress', dataIndex: 'percentComplete', width: 200,
								xtype: 'widgetcolumn',
								widget: {
									xtype: 'progressbarwidget',
									textTpl: '{value:percent}'
								}
							}
							]
						},						
						{ text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format:'m/d/y H:i:s' },
						{ text: 'Create User', dataIndex: 'createUser', width: 200 }	
					],
					listeners: {
						selectionchange: function(selModel, selected, opts) {
							var tools = archiveGrid.queryById('tools');
							tools.getComponent('download').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
							tools.getComponent('view').setDisabled(true);
								
							if (selected.length > 0) {
								var record = selected[0];
								
								if (record.get('runStatus') === 'E' || record.get('runStatus') === 'C') {
									if (record.get('archiveFilename') && record.get('archiveFilename') !== "") {									
										tools.getComponent('download').setDisabled(false);
									}
								}
								if (record.get('runStatus') === 'E' || record.get('runStatus') === 'C') {
									tools.getComponent('delete').setDisabled(false);
								} 
								
								if (record.get('runStatus') === 'E') {
									tools.getComponent('view').setDisabled(false);
								}								
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
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}									
								},
								{
									text: 'Generate Archive',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction-add',
									handler: function () {
										actionGenerate();
									}									
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Import Archive',
									scale: 'medium',
									width: '160px',
									iconCls: 'fa fa-2x fa-upload icon-button-color-default icon-correction-load-port',
									handler: function () {
										actionImport();
									}									
								},								
								{
									xtype: 'tbseparator'
								},								
								{
									text: 'View Errors',
									itemId: 'view',
									disabled: true,
									scale: 'medium',
									width: '140px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									handler: function () {
										var record = archiveGrid.getSelection()[0];
										actionView(record);
									}									
								},								
								{
									text: 'Download',
									itemId: 'download',
									disabled: true,
									scale: 'medium',
									width: '130px',
									iconCls: 'fa fa-2x fa-download icon-button-color-default icon-correction-load-port',
									handler: function () {
										var record = archiveGrid.getSelection()[0];
										actionDownload(record);
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function () {
										var record = archiveGrid.getSelection()[0];
										actionDelete(record);
									}									
								}
							]
							
						}
					]
					
				});				
				addComponentToMainViewPort(archiveGrid);
				
				var actionRefresh = function() {
					archiveGrid.getStore().reload({
						callback: function(records, operation, success) {
							if (!success) {
								//system may not be available
								window.location.reload();
							}
						}
					});
				};
				
				var actionGenerate = function() {
					
					var generateWin = Ext.create('Ext.window.Window', {
						title: 'Generate Archive',
						modal: true,
						closeAction: 'destroy',
						width: 700,
						height: '90%',
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								layout: 'anchor',
								defaults: {
									labelAlign: 'top',
									labelSeparator: '',
									width: '100%'
								},
								items: [
									{
										xtype: 'textfield',
										name: 'name',
										fieldLabel: 'Name<span class="field-required" />',
										allowBlank: false,
										maxLength: 255
									},
									Ext.create('OSF.component.StandardComboBox', {
										name: 'systemArchiveType',	
										width: '100%',
										margin: '0 0 0 0',
										fieldLabel: 'Archive Type<span class="field-required" />',	
										allowBlank: false,
										editable: false,
										typeAhead: false,
										storeConfig: {
											url: 'api/v1/resource/lookuptypes/SystemArchiveType'
										},
										listeners: {
											change: function(field, newValue, oldValue, opts) {
												var form = field.up('form');
												if (newValue === 'GENERAL') {
													form.queryById('options').setDisabled(false);																									
												} else {													
													form.queryById('options').setDisabled(true);													
												}
												if (!form.queryById('component').getValue()) {
													form.queryById('entrySelectGrid').setDisabled(false);
													form.queryById('entrySelectGrid').setDisabled(true);
												}
												if (!form.queryById('branding').getValue()) {
													form.queryById('brandingSelectGrid').setDisabled(false);
													form.queryById('brandingSelectGrid').setDisabled(true);
												}
 											}
										}
									}),
									{
										xtype: 'panel',
										itemId: 'options',
										disabled: true,
										items: [
											{
												xtype: 'checkbox',
												itemId: 'component',
												name: 'component',
												margin: '0 0 0 0',												
												boxLabel: 'Entries (Related Data)',
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														var form = field.up('form');
														if (newValue) {
															form.queryById('entrySelectGrid').setDisabled(false);		
														} else {
															form.queryById('entrySelectGrid').setDisabled(true);		
														}
													}
												}
											},
											{
												xtype: 'grid',
												itemId: 'entrySelectGrid',
												title: 'Select Entries',
												maxHeight: 250,
												disabled: true,
												columnLines: true,
												selModel: {
													selType: 'checkboxmodel'
												},
												store: {
													autoLoad: true,
													sorters: [
														new Ext.util.Sorter({
															property: 'description',
															direction: 'ASC'
														})
													],
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/components/lookup?approvalState=ALL'							
													}											
												},
												columns: [
													{text: 'Entry Name', dataIndex: 'description', flex: 1,
														filter: {
															type: 'string'
														}
													}
												],										
												dockedItems: [
													{
														xtype: 'textfield',
														dock: 'top',
														name: 'filterForEntries',																					
														emptyText: 'Filter entries by name',
														width: '100%',
														maxLength: 30,
														listeners: {
															change: function (field, newVal, oldVal, opts) {
																var grid = field.up('grid');
																grid.getStore().filter([{
																		property: 'description',
																		value: newVal
																}]);
															}
														}
													}
												]
											},									
											{
												xtype: 'checkbox',
												itemId: 'highlight',
												name: 'highlight',												
												boxLabel: 'Highlights (Related Data)'										
											},
											{
												xtype: 'checkbox',
												itemId: 'organization',
												name: 'organization',											
												boxLabel: 'Organizations'										
											},
											{
												xtype: 'checkbox',
												itemId: 'attributes',
												name: 'attributes',
												boxLabel: 'Attributes'										
											},
											{
												xtype: 'checkbox',
												itemId: 'alerts',
												name: 'alerts',
												boxLabel: 'Alerts'										
											},
											{
												xtype: 'checkbox',
												itemId: 'contacts',
												name: 'contacts',
												boxLabel: 'Contacts'										
											},
											{
												xtype: 'checkbox',
												itemId: 'feedback',
												name: 'feedback',
												boxLabel: 'Feedback'										
											},
											{
												xtype: 'checkbox',
												itemId: 'userprofile',
												name: 'userprofile',
												boxLabel: 'User Profile'										
											},
											{
												xtype: 'checkbox',
												itemId: 'branding',
												name: 'branding',
												boxLabel: 'Branding',
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														var form = field.up('form');
														if (newValue) {
															form.queryById('brandingSelectGrid').setDisabled(false);		
														} else {
															form.queryById('brandingSelectGrid').setDisabled(true);		
														}
													}
												}												
											},
											{
												xtype: 'grid',
												itemId: 'brandingSelectGrid',
												title: 'Select Branding',
												maxHeight: 175,
												disabled: true,
												columnLines: true,
												selModel: {
													selType: 'checkboxmodel'
												},
												store: {
													autoLoad: true,
													sorters: [
														new Ext.util.Sorter({
															property: 'description',
															direction: 'ASC'
														})
													],
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/branding'							
													}											
												},
												columns: [
													{text: 'Branding', dataIndex: 'name', flex: 1,
														filter: {
															type: 'string'
														}
													}
												]												
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
												text: 'Generate',
												iconCls: 'fa fa-lg fa-plus icon-button-color-save',
												formBind: true,
												handler: function() {
													var win = this.up('window');
													var form = this.up('form');
													var data = form.getValues();
													
													var archiveOptions = [];
													if (data.component) {
														Ext.Array.each(generateWin.queryById('entrySelectGrid').getSelection(), function(record){
															archiveOptions.push({
																primaryEntity: 'Component',
																entityId: record.get('code')
															});	
														});
													}
													
													if (data.highlight) {
														archiveOptions.push({
															primaryEntity: 'Highlight'
														});
													}
													if (data.organization) {
														archiveOptions.push({
															primaryEntity: 'Organization'
														});
													}	
													if (data.attributes) {
														archiveOptions.push({
															primaryEntity: 'AttributeType'
														});
													}
													if (data.alerts) {
														archiveOptions.push({
															primaryEntity: 'Alert'
														});
													}
													if (data.contacts) {
														archiveOptions.push({
															primaryEntity: 'Contact'
														});
													}
													if (data.feedback) {
														archiveOptions.push({
															primaryEntity: 'FeedbackTicket'
														});
													}
													if (data.userprofile) {
														archiveOptions.push({
															primaryEntity: 'UserProfile'
														});
													}													
													if (data.branding) {
														Ext.Array.each(generateWin.queryById('brandingSelectGrid').getSelection(), function(record){
															archiveOptions.push({
																primaryEntity: 'Branding',
																entityId: record.get('brandingId')
															});	
														});
													}													
													data.archiveOptions = archiveOptions;
													
													CoreUtil.submitForm({
														url: 'api/v1/resource/systemarchives',
														method: 'POST',
														form: form,
														data: data,
														loadingText: 'Generating...',
														success: function(action, opt) {
															actionRefresh();
															win.close();
														}
													});
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',												
												handler: function() {
													this.up('window').close();
												}												
											}
										]
									}
								]
							}
						]
					});
					generateWin.show();
										
				};				
				
				var actionImport = function() {
					
					var importWin = Ext.create('Ext.window.Window', {
						title: 'Import Archive',
						modal: true,
						closeAction: 'destroy',
						width: 500,
						height: 360,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								scollable: true,
								bodyStyle: 'padding: 10px;',
								layout: 'anchor',
								defaults: {
									labelAlign: 'top',
									labelSeparator: '',
									width: '100%'
								},
								items: [
									Ext.create('OSF.component.StandardComboBox', {
										name: 'importModeType',																												
										margin: '0 0 0 0',
										width: '100%',
										fieldLabel: 'Import Mode<span class="field-required" />',	
										allowBlank: false,
										editable: false,
										typeAhead: false,
										value: 'MERGE',
										storeConfig: {
											url: 'api/v1/resource/lookuptypes/ImportModeType'
										}										
									}),
									{
										xtype: 'filefield',
										name: 'uploadFile',
										width: '100%',
										allowBlank: false,
										fieldLabel: 'Import',
										buttonText: 'Select Archive...'
									},
									{
										xtype: 'panel',
										html: '<i class="fa fa-lg fa-warning icon-button-color-warning"></i><b>Warning:</b><br><br>Importing database archives will temporarily interrupt the application and may cause some jobs to fail.<br>Please wait a few minutes after import for the database to stabilize.'
									}
								],
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												text: 'Import',
												iconCls: 'fa fa-lg fa-upload icon-button-color-default',
												formBind: true,
												handler: function() {
													
													var uploadForm = this.up('form');
													//var data = uploadForm.getValues();
													var progressMsg = Ext.MessageBox.show({
														title: 'Archive Import',
														msg: 'Importing archive please wait...',
														width: 300,
														height: 150,
														closable: false,
														progressText: 'Importing...',
														wait: true,
														waitConfig: {interval: 300}
													});
													
													uploadForm.submit({
														submitEmptyText: false,
														url: 'Upload.action?ImportArchive',														
														success: function(form, action) {
															Ext.toast('File has been queued for processing.', 'Upload Successfully', 'br');	
															actionRefresh();
															importWin.close();
															progressMsg.hide();
														}
													});	
												}
											},
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												iconCls: 'fa fa-lg fa-close icon-button-color-warning',											
												handler: function() {
													this.up('window').close();
												}												
											}
										]
									}
								]
							}							
						]
					});
					importWin.show();		
					
				};				
				
				var actionDownload = function(record) {
					window.location.href = 'api/v1/resource/systemarchives/' + record.get('archiveId') + '/download';					
				};	
				
				var actionView = function(record) {
					
					var errorWin = Ext.create('Ext.window.Window', {
						title: 'Errors',
						iconCls: 'fa fa-lg fa-info-circle icon-small-vertical-correction',
						closeAction: 'destroy',
						modal: true,
						layout: 'fit',
						width: '60%',
						height: '40%',
						maximizable: true,
						items: [
							{
								xtype: 'grid',
								itemId: 'detailGrid',
								columnLines: true,
								store: {									
									autoLoad: false,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/systemarchives/{archiveId}/errors'
									}
								}, 								
								columns: [
									{ text: 'Message', dataIndex: 'message', flex: 1 }
								]
							}
						],
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
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function() {
											this.up('window').hide();
										}
									},
									{
										xtype: 'tbfill'
									}
								]
							}
						]						
					});
					errorWin.show();
					errorWin.queryById('detailGrid').getStore().load({
						url: 'api/v1/resource/systemarchives/' + record.get('archiveId') + '/errors'
					});
					
				};
				
				var actionDelete = function(record) {
					
					Ext.Msg.show({
						title: 'Delete Archive?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete this archive? <br> This will NOT affect imported data.',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								archiveGrid.setLoading('Deleting Archive...');
								Ext.Ajax.request({
									url: 'api/v1/resource/systemarchives/' + record.get('archiveId'),
									method: 'DELETE',
									callback: function(){
										archiveGrid.setLoading(false);
									},
									success: function(response, opts) {
										actionRefresh();
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