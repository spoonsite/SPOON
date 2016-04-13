<%--
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%-- 
    Document   : branding
    Created on : Mar 29, 2016, 4:31:55 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''					
				});				
				
				var previewWin = Ext.create('Ext.window.Window', {
					width: '70%',
					height: '80%',
					maximizable: true,
					title: 'Preview',
					iconCls: 'fa fa-eye',
					modal: true,
					layout: 'fit',
					items: [
						previewContents
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
									iconCls: 'fa fa-2x fa-close',
									scale: 'medium',
									handler: function() {
										previewWin.close();
									}
								},
								{
									xtype: 'tbfill'
								}								
							]
						}
					]
				});


				var addEditBrandingWin = Ext.create('Ext.window.Window', {
					id: 'addEditBrandingWin',
					title: 'Add/Edit Branding',
					iconCls: 'fa fa-sliders',
					modal: true,
					width: '80%',
					height: '80%',
					maximizable: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'brandingForm',
							scrollable: true,
							items: [
								{
									xtype: 'panel',
									title: 'General',
									width: '100%',
									collapsible: true,
									titleCollapse: true,
									margin: '0 0 20 0',
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',
									defaults: {
										labelAlign: 'top',
										labelSeparator: ''
									},
									items: [
										{
											xtype: 'hidden',
											name: 'brandingId'
										},									
										{
											xtype: 'textfield',
											fieldLabel: 'Name<span class="field-required" />',
											name: 'name',
											width: '100%',
											allowBlank: false,
											maxLength: 255
										},
										{
											xtype: 'textfield',
											fieldLabel: 'Application Name <i class="fa fa-question-circle"  data-qtip="Defaults to config property." ></i>',
											name: 'applicationName',
											width: '100%',
											allowBlank: true,
											maxLength: 255
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Login Warning <i class="fa fa-question-circle"  data-qtip="Warning on login page (if applicable)" ></i>',
											name: 'loginWarning',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 16000
										},										
										{
											xtype: 'htmleditor',
											fieldLabel: 'Landing Page Title <i class="fa fa-question-circle"  data-qtip="This is the title at the top of the landing page" ></i>',
											name: 'landingPageTitle',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 255
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Landing Stats Text <i class="fa fa-question-circle"  data-qtip="This is the Browsing X text" ></i>',
											name: 'landingStatsText',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 255
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Landing Banner <i class="fa fa-question-circle"  data-qtip="This is the quote on the landing page." ></i>',
											name: 'landingPageBanner',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 255
										},
										{
											xtype: 'panel',
											html: '<b>Landing Page Footer</b> <i class="fa fa-question-circle"  data-qtip="This is the footer on the landing page." ></i>'
										},
										{
											xtype: 'tinymce_textarea',
											fieldStyle: 'font-family: Courier New; font-size: 12px;',
											style: {border: '0'},
											name: 'landingPageFooter',
											width: '100%',
											height: 300,
											maxLength: 65536,
											tinyMCEConfig: CoreUtil.tinymceConfig()
										},
										{
											xtype: 'checkbox',
											name: 'hideArchitectureSearchFlg',
											boxLabel: 'Hide Architechture Search'
										},										
										{
											xtype: 'textfield',
											fieldLabel: 'Architecture Search <i class="fa fa-question-circle"  data-qtip="This is the name of the architecure on the search tool." ></i>',
											name: 'architectureSearchLabel',
											width: '100%',
											allowBlank: true,
											maxLength: 255
										},
										{
											xtype: 'combobox',
											name: 'architectureSearchType',
											width: '100%',
											fieldLabel: 'Architecture Search Type <i class="fa fa-question-circle"  data-qtip="This is the architecture to use on the search tools." ></i>',
											queryMode: 'local',
											displayField: 'description',
											valueField: 'attributeType',
											editable: false,
											typeAhead: false,
											store: {
												autoLoad: true,
												proxy: {
													type: 'ajax',
													url: '../api/v1/resource/attributes/attributetypes',
													reader: {
														type: 'json',
														rootProperty: 'data'
													}
												},
												listeners: {
													load: function(store, records, successful, opts) {
														store.filterBy(function(record) {															
															return record.get('architectureFlg');
														});
													}
												}
											}
										}

									]
								},
								{
									xtype: 'panel',
									title: 'Support',
									width: '100%',
									collapsible: true,
									titleCollapse: true,
									margin: '0 0 20 0',
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',									
									defaults: {
										labelAlign: 'top',
										labelSeparator: ''
									},
									items: [
										{
											xtype: 'combobox',
											name: 'feedbackHandler',
											width: '100%',
											fieldLabel: 'Feeback Handling <i class="fa fa-question-circle"  data-qtip="This is the method to handle feedback capture by the application. (Default: Jira)<br>  Note: Email used is set in the system configuration properties." ></i>',
											queryMode: 'local',
											displayField: 'description',
											valueField: 'code',
											editable: false,
											typeAhead: false,											
											store: {
												autoLoad: true,
												proxy: {
													type: 'ajax',
													url: '../api/v1/resource/lookuptypes/FeedbackHandleType/view'													
												}
											}
										},
										{
											xtype: 'textarea',
											fieldLabel: 'Analytics Tracking Code <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
											name: 'analyticsTrackingCode',
											width: '100%',	
											grow: true,
											allowBlank: true,
											maxLength: 16000										
										}										
									]
								},
								{
									xtype: 'panel',
									title: 'Security',
									width: '100%',
									collapsible: true,
									titleCollapse: true,
									margin: '0 0 20 0',
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',
									defaults: {
										labelAlign: 'top',
										labelSeparator: ''
									},
									items: [
										{
											xtype: 'checkbox',
											name: 'allowSecurityMarkingsFlg',
											boxLabel: 'Allow Security Markings <i class="fa fa-question-circle"  data-qtip="Allows the capture and display of security markings." ></i>'
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Security Banner Text <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
											name: 'securityBannerText',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 4000										
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Security Banner Text Color',
											name: 'securityBannerTextColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Security Banner Background Color',
											name: 'securityBannerBackgroundColor'
										},										
										{
											xtype: 'htmleditor',
											fieldLabel: 'User Input Warning <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
											name: 'userInputWarning',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 4000										
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Submission Form Warning <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
											name: 'submissionFormWarning',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 4000										
										},
										{
											xtype: 'htmleditor',
											fieldLabel: 'Change Request Form Warning <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
											name: 'changeRequestWarning',
											width: '100%',
											fieldBodyCls: 'form-comp-htmleditor-border',
											allowBlank: true,
											maxLength: 4000										
										}											
									]
								},
								{
									xtype: 'panel',
									title: 'Colors/Logos',
									width: '100%',
									collapsible: true,
									titleCollapse: true,
									margin: '0 0 20 0',
									bodyStyle: 'padding: 10px;',
									layout: 'anchor',
									defaults: {
										labelAlign: 'top',
										labelSeparator: ''
									},
									items: [
										{
											xtype: 'textfield',
											fieldLabel: 'Primary Logo URL <i class="fa fa-question-circle"  data-qtip="Home page Logo (625w x 200h)" ></i>',
											name: 'primaryLogoUrl',
											width: '100%',
											allowBlank: false,
											emptyText: 'Media.action?GeneralMedia&name=logo',											
											maxLength: 255
										},
										{
											xtype: 'textfield',
											fieldLabel: 'Secondary Logo URL <i class="fa fa-question-circle"  data-qtip="Top corner Logo (181w x 53h)" ></i>',
											name: 'secondaryLogoUrl',
											width: '100%',
											allowBlank: false,
											emptyText: 'Media.action?GeneralMedia&name=logo',											
											maxLength: 255
										},										
										{
											xtype: 'colorfield',
											width: '100%',
											fieldLabel: 'Primary Color',
											format: '#hex6',
											name: 'primaryColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											fieldLabel: 'Primary Text Color',
											format: '#hex6',
											name: 'primaryTextColor'
										},										
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Accent Color',
											name: 'accentColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Quote Color',
											name: 'quoteColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Link Color',
											name: 'linkColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Link Visited Color',
											name: 'linkVisitedColor'
										},										
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Link Hover Color',
											name: 'linkhoverColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Panel Header Color',
											name: 'panelHeaderColor'
										},
										{
											xtype: 'colorfield',
											width: '100%',
											format: '#hex6',
											fieldLabel: 'Panel Header Text Color',
											name: 'panelHeaderTextColor'
										},										
										{
											xtype: 'textarea',
											width: '100%',
											fieldLabel: 'Override Css <i class="fa fa-question-circle"  data-qtip="Enter CSS to override existing look not covered by the color set." ></i>',
											name: 'overrideCSS',
											grow: true,
											maxLength: 1048576
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
											text: 'Save',
											iconCls: 'fa fa-2x fa-save',
											scale: 'medium',
											formBind: true,
											handler: function () {
												var win = this.up('window');
												var form = this.up('form');
												
												actionSaveBranding(form, function(response, opt){
													Ext.toast('Saved Successfully');
													actionRefresh();
													win.close();
												});
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Preview',
											tooltip: 'Saves and preview',
											iconCls: 'fa fa-2x fa-eye',
											scale: 'medium',
											formBind: true,
											hidden: true,
											handler: function () {
												var addEditWin = this.up('window');
												var form = this.up('form');
												
												actionSaveBranding(form, function(response, opt){
													var branding = Ext.decode(response.responseText);
													
													previewWin.show();													
													//previewContents.load('/openstorefront/client/index.jsp');
												});												
												
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-2x fa-close',
											scale: 'medium',
											handler: function () {
												this.up('window').close();
											}
										}
									]
								}
							]

						}
					]
				});
				
				var actionSaveBranding = function(form, successHandler) {
					var data = form.getValues();
					
					var method='POST';
					var endUrl = '';
					if (data.brandingId) {
						method = 'PUT',
						endUrl = '/' + data.brandingId;		
					}
					
					Ext.Object.each(data, function (key, value, dataLocal) {
						if (value === "") {
							delete data[key];
						}
					});
					
					CoreUtil.submitForm({						
						url: '../api/v1/resource/branding' + endUrl,
						method: method,
						form: form,
						data: {
							branding: data
						},
						success: function(response, opts){
							successHandler(response, opts);
						},
						failure: function(response, opts) {
							Ext.Msg.show({
								title:'Validation Error',
								message: 'Please check your input for missing required information.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.Error,
								fn: function(btn) {
								}
							});							
						}
					});
				};
				

				var brandingGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Branding <i class="fa fa-question-circle"  data-qtip="This tool allows the ability to set the graphic design and theme characteristics for the site." ></i>',
					id: 'brandingGrid',
					columnLines: true,
					store: {
						sorters: [
							new Ext.util.Sorter({
								property: 'name',
								direction: 'ASC'
							})
						],
						fields: [
							{
								name: 'createDts',
								type: 'date',
								dateFormat: 'c'
							},
							{
								name: 'updateDts',
								type: 'date',
								dateFormat: 'c'
							}
						],
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: '../api/v1/resource/branding/'
						}
					},
					columns: [
						{text: 'Name', dataIndex: 'name', minWidth: 200, flex: 1, 
							renderer: function(value, meta, record){
								if (record.get('activeStatus') === 'A') {
									meta.tdCls = 'alert-success';
									return value + " (ACTIVE)";
								} else {						
									return value;
								}
							}
						},
						{text: 'Application Name', dataIndex: 'applicationName', width: 200},
						{text: 'Status', align: 'center', dataIndex: 'activeStatus', width: 150},
						{text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
						{text: 'Update User', dataIndex: 'updateUser', width: 150},
						{text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s', hidden: true},
						{text: 'Create User', dataIndex: 'createUser', width: 150, hidden: true}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
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
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAdd();
									}
								},
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										var record = this.up('grid').getSelectionModel().getSelection()[0];
										actionEdit(record);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Activate',
									itemId: 'activate',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									handler: function () {
										var record = this.up('grid').getSelectionModel().getSelection()[0];
										actionActivate(record);
									}
								},
								{
									text: 'Duplicate',
									itemId: 'duplicate',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-clone',
									menu: {
										items: [
											{
												text: 'Selected Branding',	
												id: 'duplicateSelected',
												handler: function() {
													var record = this.up('grid').getSelectionModel().getSelection()[0];	
													actionDuplicate(record);
												}
											},
											{
												xtype: 'menuseparator'
											},
											{
												text: 'Current Branding',	
												handler: function() {														
													actionDuplicate();
												}												
											}
										]
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Reset To Default',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-undo',
									handler: function () {
										actionResetToDefault();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									handler: function () {
										var record = this.up('grid').getSelectionModel().getSelection()[0];
										actionDelete(record);
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							actionEdit(record);
						},
						selectionchange: function (selectionModel, selection, index, opts) {
							var tools = Ext.getCmp('brandingGrid').getComponent('tools');
							if (selectionModel.getCount() > 0) {
								tools.getComponent('edit').setDisabled(false);
								Ext.getCmp('duplicateSelected').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);

								var record = selection[0];
								if (record.get('activeStatus') === 'I') {
									tools.getComponent('activate').setDisabled(false);
								} else {
									tools.getComponent('activate').setDisabled(true);
								}
							} else {
								tools.getComponent('edit').setDisabled(true);
								Ext.getCmp('duplicateSelected').setDisabled(true);
								tools.getComponent('delete').setDisabled(false);
								tools.getComponent('activate').setDisabled(true);
							}
						}
					}
				});

				var actionRefresh = function () {
					Ext.getCmp('brandingGrid').getStore().reload();
				};

				var actionAdd = function () {
					addEditBrandingWin.show();
					Ext.defer(function(){
						addEditBrandingWin.updateLayout(true, true);
					}, 250);
					addEditBrandingWin.getComponent('brandingForm').reset();
				};

				var actionEdit = function (record) {
					addEditBrandingWin.show();
					Ext.defer(function(){
						addEditBrandingWin.updateLayout(true, true);
					}, 250);
					
					addEditBrandingWin.getComponent('brandingForm').loadRecord(record);
				};

				var actionActivate = function (record) {

					var brandingId = record.get('brandingId');
					var method = 'PUT';
					var urlEnd = '/active';

					Ext.getCmp('brandingGrid').setLoading('Activating...');
					Ext.Ajax.request({
						url: '../api/v1/resource/branding/' + brandingId + urlEnd,
						method: method,
						callback: function () {
							Ext.getCmp('brandingGrid').setLoading(false);
						},
						success: function (response, opts) {
							window.top.location.reload();
						}
					});

				};

				var actionDuplicate = function (record) {
					var cloneObj = {};			
					
					var endUrl = 'current';
					if (record) {
						endUrl = record.get('brandingId');
					}

					Ext.getCmp('brandingGrid').setLoading('Copying...');
					Ext.Ajax.request({
						url: '../api/v1/resource/branding/' + endUrl,
						method: 'GET',
						success: function (response, opts) {

							cloneObj = Ext.decode(response.responseText);
							cloneObj.name += '_copy';
							cloneObj.brandingId = null;

							Ext.Ajax.request({
								url: '../api/v1/resource/branding/',
								method: 'POST',
								jsonData: {
									branding: cloneObj
								},
								callback: function () {
									Ext.getCmp('brandingGrid').setLoading(false);
								},
								success: function (response, opts) {									
									actionRefresh();
								}
							});
						}
					});
				};

				var actionResetToDefault = function () {
					Ext.Msg.show({
						title: 'Reset To Default?',
						message: 'Are you sure you want to reset to default branding?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {

								Ext.getCmp('brandingGrid').setLoading('Resetting to default...');
								Ext.Ajax.request({
									url: '../api/v1/resource/branding/current/default',
									method: 'PUT',
									callback: function() {
										Ext.getCmp('brandingGrid').setLoading(false);
									},
									success: function (response, opts) {
										window.top.location.reload();
									}
								});
							}
						}
					});
				};

				var actionDelete = function (record) {

					var brandingId = record.get('brandingId');
					var name = record.get('name');

					Ext.Msg.show({
						title: 'Delete Branding Entry?',
						message: 'Are you sure you want to delete:  ' + name + ' ?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {

								Ext.getCmp('brandingGrid').setLoading('Deleting branding...');
								Ext.Ajax.request({
									url: '../api/v1/resource/branding/' + brandingId,
									method: 'DELETE',
									callback: function() {
										Ext.getCmp('brandingGrid').setLoading(false);
									},
									success: function (response, opts) {										
										actionRefresh();
									}
								});
							}
						}
					});
				};

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						brandingGrid
					]
				});

			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>