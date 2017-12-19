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
<%-- 
    Document   : branding
    Created on : Mar 29, 2016, 4:31:55 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
						
        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.require('OSF.landing.designer.Designer');

			Ext.onReady(function () {

				var previewContents = Ext.create('OSF.ux.IFrame', {
					src: ''					
				});				
				
				var previewWin = Ext.create('Ext.window.Window', {
					width: '70%',
					height: '80%',
					maximizable: true,
					title: 'Preview',
					iconCls: 'fa fa-lg fa-eye icon-button-color-view icon-small-vertical-correction',
					modal: true,
					minWidth: 500,
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
									iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
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
				
				var addEditBranding = function(record) {

					var addEditBrandingWin = Ext.create('Ext.window.Window', {
						id: 'addEditBrandingWin',
						title: 'Add/Edit Branding',
						iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
						modal: true,
						closeAction: 'destroy',
						width: '80%',
						height: '80%',
						maximizable: true,
						layout: 'fit',
						listeners: {
							show: function() {        
								this.removeCls("x-unselectable");    
							},
							beforeClose: function() {
								if (addEditBrandingWin.proceedWithClosing){
									return true;
								} else {
									actionCloseBranding();
									return false;
								}
							}
						},						
						items: [
							{
								xtype: 'tabpanel',
								itemId: 'tabpanel',
								items: [
									{
										xtype: 'form',
										title: 'Branding',
										itemId: 'brandingForm',
										scrollable: true,
										trackResetOnLoad: true,
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
														resizable: {
															handles: 's'
														},
														width: '100%',											
														allowBlank: true,
														maxLength: 16000
													},
													{
														xtype: 'htmleditor',
														fieldLabel: 'Login Logo Section <i class="fa fa-question-circle"  data-qtip="Logo Section that can use image map" ></i>',
														name: 'loginLogoBlock',
														resizable: {
															handles: 's'
														},
														width: '100%',											
														allowBlank: true,
														maxLength: 16000
													},														
													{
														xtype: 'htmleditor',
														fieldLabel: 'Landing Page Title <i class="fa fa-question-circle"  data-qtip="This is the title at the top of the landing page" ></i>',
														name: 'landingPageTitle',
														width: '100%',
														resizable: {
															handles: 's'
														},
														allowBlank: true,
														maxLength: 255
													},
													{
														xtype: 'htmleditor',
														fieldLabel: 'Landing Stats Text <i class="fa fa-question-circle"  data-qtip="This is the Browsing X text" ></i>',
														name: 'landingStatsText',
														width: '100%',
														resizable: {
															handles: 's'
														},
														allowBlank: true,
														maxLength: 255
													},
													{
														xtype: 'htmleditor',
														fieldLabel: 'Landing Banner <i class="fa fa-question-circle"  data-qtip="This is the quote on the landing page." ></i>',
														name: 'landingPageBanner',
														width: '100%',
														resizable: {
															handles: 's'
														},
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
														tinyMCEConfig: Ext.apply(CoreUtil.tinymceConfig(), {
															mediaSelectionUrl: MediaUtil.generalMediaUrl,
															mediaUploadHandler: MediaUtil.generalMediaUnloadHandler
														})
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
																url: 'api/v1/resource/attributes/attributetypes',
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
																url: 'api/v1/resource/lookuptypes/FeedbackHandleType/view'													
															}
														}
													},
													{
														xtype: 'checkbox',
														name: 'showSupportMedia',
														boxLabel: 'Show Tutorials Link'
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
														resizable: {
															handles: 's'
														},
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
														resizable: {
															handles: 's'
														},
														allowBlank: true,
														maxLength: 4000										
													},
													{
														xtype: 'htmleditor',
														fieldLabel: 'Submission Form Warning <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
														name: 'submissionFormWarning',
														width: '100%',
														resizable: {
															handles: 's'
														},
														allowBlank: true,
														maxLength: 4000										
													},
													{
														xtype: 'htmleditor',
														fieldLabel: 'Change Request Form Warning <i class="fa fa-question-circle"  data-qtip="Leave blank to not show" ></i>',
														name: 'changeRequestWarning',
														width: '100%',
														resizable: {
															handles: 's'
														},
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
														layout: 'hbox',
														width: '100%',
														margin: '5px 0 0 0',
														items: [
															{
																xtype: 'textfield',
																itemId: 'primaryLogoUrl',
																labelAlign: 'top',
																labelSeparator: '',
																fieldLabel: 'Primary Logo URL<span class="field-required" /> <i class="fa fa-question-circle"  data-qtip="Home page Logo (625w x 200h)" ></i>',
																name: 'primaryLogoUrl',
																allowBlank: false,
																emptyText: 'Media.action?GeneralMedia&name=logo',											
																maxLength: 255,																
																flex: 4
															},
															{
																xtype: 'button',
																text: 'Insert Media',
																flex: 1,
																margin: '30 0 0 0',
																handler: function() {
																	var primaryLogoUrl = this.up('panel').queryById('primaryLogoUrl');																	
																	var mediaWindow = Ext.create('OSF.component.MediaInsertWindow', {																		
																		isEditor: false,
																		mediaSelectionUrl: 'api/v1/resource/generalmedia',
																		closeAction: 'destroy',
																		mediaHandler: function(link) {
																			primaryLogoUrl.setValue(encodeURI(link));
																		}
																	});	
																	mediaWindow.show();
																}
															}
														]														
													},
													{
														layout: 'hbox',
														width: '100%',
														margin: '5px 0 0 0',
														items: [
															{
																xtype: 'textfield',
																itemId: 'secondaryLogoUrl',
																labelAlign: 'top',
																labelSeparator: '',
																fieldLabel: 'Secondary Logo URL<span class="field-required" /> <i class="fa fa-question-circle"  data-qtip="Top corner Logo (181w x 53h)" ></i>',
																name: 'secondaryLogoUrl',
																allowBlank: false,
																emptyText: 'Media.action?GeneralMedia&name=logo',											
																maxLength: 255,																
																flex: 4
															},
															{
																xtype: 'button',
																text: 'Insert Media',
																flex: 1,
																margin: '30 0 0 0',
																handler: function() {
																	var secondaryLogoUrl = this.up('panel').queryById('secondaryLogoUrl');																	
																	var mediaWindow = Ext.create('OSF.component.MediaInsertWindow', {																		
																		isEditor: false,
																		mediaSelectionUrl: 'api/v1/resource/generalmedia',
																		closeAction: 'destroy',
																		mediaHandler: function(link) {
																			secondaryLogoUrl.setValue(encodeURI(link));
																		}
																	});	
																	mediaWindow.show();
																}
															}
														]														
													},													
													{
														layout: 'hbox',
														width: '100%',
														margin: '5px 0 0 0',
														items: [
															{
																xtype: 'textfield',
																itemId: 'homebackSplashUrl',
																labelAlign: 'top',
																labelSeparator: '',
																fieldLabel: 'Backsplash URL<span class="field-required" /> <i class="fa fa-question-circle"  data-qtip="Top corner Logo ~(4000w x 1000h)" ></i>',
																name: 'homebackSplashUrl',
																allowBlank: false,
																emptyText: 'Media.action?GeneralMedia&name=logo',											
																maxLength: 255,																
																flex: 4
															},
															{
																xtype: 'button',
																text: 'Insert Media',
																flex: 1,
																margin: '30 0 0 0',
																handler: function() {
																	var homebackSplashUrl = this.up('panel').queryById('homebackSplashUrl');																	
																	var mediaWindow = Ext.create('OSF.component.MediaInsertWindow', {																		
																		isEditor: false,
																		mediaSelectionUrl: 'api/v1/resource/generalmedia',
																		closeAction: 'destroy',
																		mediaHandler: function(link) {
																			homebackSplashUrl.setValue(encodeURI(link));
																		}
																	});	
																	mediaWindow.show();
																}
															}
														]														
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
														fieldLabel: 'Secondary Color',
														name: 'secondaryColor'
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
													},
													{
														xtype: 'checkbox',
														width: '100%',
														boxLabel: 'Use Default Landing Page <i class="fa fa-exclamation-circle" data-qtip="When checked, will override and<br />delete the custom landing page."></i>',
														name: 'useDefaultLandingPage',
														listeners: {
															change: function(field, newValue, oldValue) {							
																if (record) {
																	if (newValue) {
																		addEditBrandingWin.queryById('landingPageTab').setDisabled(true);
																		addEditBrandingWin.queryById('landingPageTab').loadedTemplate = null;
																	} else {
																		addEditBrandingWin.queryById('landingPageTab').setDisabled(false);
																	}
																}
															}
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
														text: 'Save',
														iconCls: 'fa fa-lg fa-save icon-button-color-save icon-small-vertical-correction',
														scale: 'medium',
														formBind: true,
														handler: function () {
															var win = this.up('window');
															var form = this.up('form');

															actionSaveBranding(form, function(response, opt){	
																var rootItems = form.items.items;

																// When the form is saved, reset all original values for checkboxes
																for (var ii = 0; ii < rootItems.length; ii += 1) {
																	
																	var subItems = rootItems[ii].items.items;
																	for (var jj = 0; jj < subItems.length; jj += 1) {
																		if (form.items.items[ii].items.items[jj].xtype === 'checkbox') {
																			subItems[jj].originalValue = subItems[jj].getValue();
																		}
																	}
																}
															});
														}
													},
													{
														xtype: 'tbfill'
													},
													{
														text: 'Preview',
														tooltip: 'Saves and preview',
														iconCls: 'fa fa-lg fa-eye icon-button-color-view icon-small-vertical-correction',
														scale: 'medium',
														formBind: true,											
														handler: function () {												
															var form = this.up('form');

															actionSaveBranding(form, function(response, opt){
																var branding = Ext.decode(response.responseText);

																previewWin.show();													
																previewContents.load('Branding.action?Preview&brandingId=' + branding.brandingId);
															});												

														}
													},
													{
														xtype: 'tbfill'
													},
													{
														text: 'Close',
														iconCls: 'fa fa-lg fa-close icon-button-color-warning icon-small-vertical-correction',
														scale: 'medium',
														handler: function () {
															
															//check for unsaved state
															var form = this.up('form');
															
															this.up('window').close();
														}
													}
												]
											}
										]
									},
									{
										xtype: 'ofs-landingPageDesigner',
										title: 'Landing Page',
										itemId: 'landingPageTab',
										disabled: true,
										saveHandler: function(landingTemplate) {
											var form = addEditBrandingWin.queryById('brandingForm');
											var landingTab = addEditBrandingWin.queryById('landingPageTab');
											actionSaveBranding(form, function(response, opt){
												landingTab.loadedTemplate = landingTab.code.getFullTemplate();
											}, landingTemplate);
										},
										cancelHandler: function() {
											addEditBrandingWin.close();
										}
									},
									{
										xtype: 'panel',
										title: 'Current CSS',
										scrollable: true,
										bodyStyle: 'padding: 10px;',										
										loader: {
											 url: 'Branding.action?CSS&template=apptemplate.jsp&v=' + (Math.random() * 100000),
											 autoLoad: true,
											 success: function(loader, response, opts) {
												var data = response.responseText;
												 
												data = '<pre>' + data + '</pre>';	
												loader.getTarget().update(data);
											 }
										},
										dockedItems: [
											{
												xtype: 'panel',
												dock: 'top',
												html: '<b>Read-Only</b>'
											}
										]
									}
								]
							}
						]
					});
					addEditBrandingWin.show();
					Ext.defer(function(){
						addEditBrandingWin.updateLayout(true, true);
					}, 250);
					
					if (record) {
						var landingTab = addEditBrandingWin.queryById('landingPageTab');
						addEditBrandingWin.queryById('brandingForm').loadRecord(record);
						if (record.get('useDefaultLandingPage')) {
							landingTab.setDisabled(true);
						} else {
							landingTab.setDisabled(false);
							landingTab.initializeCallback = function() {
								landingTab.loadData(record.data);
							}
						}
						addEditBrandingWin.queryById('tabpanel').setActiveTab(landingTab);
						addEditBrandingWin.queryById('tabpanel').setActiveTab(0);
					}
					
				};
				
				var actionCloseBranding = function() {
					var brandingWin = Ext.getCmp('addEditBrandingWin');
					var form = brandingWin.queryById('brandingForm');
					var landingTab = brandingWin.queryById('landingPageTab');
					var formDirty = form.isDirty();
					var landingTabDirty = landingTab.isDirty();
					if (formDirty || landingTabDirty)  {
						Ext.Msg.show({
							title:'Save Changes?',
							message: 'You are closing a form that has unsaved changes. Would you like to save your changes?',
							buttons: Ext.Msg.YESNOCANCEL,
							icon: Ext.Msg.QUESTION,
							fn: function(btn) {
								if (btn === 'yes') {
									actionSaveBranding(form, function(response, opt){										
										brandingWin.proceedWithClosing = true;
										brandingWin.close();
									}, landingTab.getTemplate());
								} else if (btn === 'no') {
									brandingWin.proceedWithClosing = true;
									brandingWin.close();
								} 
							}
						});
			
					} else {
						brandingWin.proceedWithClosing = true;
						brandingWin.close();
					}
				}
				
				
				var actionSaveBranding = function(form, successHandler, template) {
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
					
					if (!template) {
						var landingTab = Ext.getCmp('addEditBrandingWin').queryById('landingPageTab');
						template = landingTab.getTemplate();
					}
					data.landingTemplate = template;
					
					CoreUtil.submitForm({						
						url: 'api/v1/resource/branding' + endUrl,
						method: method,
						form: form,
						data: {
							branding: data
						},
						success: function(response, opts){
							Ext.toast('Saved Successfully');
							form.getForm().setValues(form.getValues());
							actionRefresh();
							var landingTab = Ext.getCmp('addEditBrandingWin').queryById('landingPageTab');
							if (data.useDefaultLandingPage) {
								landingTab.setDisabled(true);
							} else {
								landingTab.setDisabled(false);														
							}	
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
					title: 'Manage Branding &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="This tool allows the ability to set the graphic design and theme characteristics for the site." ></i>',
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
							url: 'api/v1/resource/branding/'
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
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									width: '110px',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									width: '100px',
									handler: function () {
										actionAdd();
									}
								},
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									width: '100px',
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
									text: 'Toggle Branding Status',
									itemId: 'activate',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
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
									width: '150px',
									iconCls: 'fa fa-2x fa-clone icon-button-color-default icon-vertical-correction-edit',
									menu: {
										items: [
											{
												text: 'Selected Branding',	
												id: 'duplicateSelected',
												cls: 'menu-items-css',
												disabled: true,
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
												cls: 'menu-items-css',
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
									iconCls: 'fa fa-2x fa-undo icon-button-color-refresh icon-vertical-correction',
									width: '170px',
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
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
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
					addEditBranding();
				};

				var actionEdit = function (record) {
					addEditBranding(record);
				};

				var actionActivate = function (record) {

					var brandingId = record.get('brandingId');
					var method = 'PUT';
					var urlEnd = '/active';

					Ext.getCmp('brandingGrid').setLoading('Activating...');
					Ext.Ajax.request({
						url: 'api/v1/resource/branding/' + brandingId + urlEnd,
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
						url: 'api/v1/resource/branding/' + endUrl,
						method: 'GET',
						success: function (response, opts) {

							cloneObj = Ext.decode(response.responseText);
							cloneObj.name += '_copy';
							cloneObj.brandingId = null;

							Ext.Ajax.request({
								url: 'api/v1/resource/branding/',
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
									url: 'api/v1/resource/branding/current/default',
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
						title: 'Delete Branding?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: '<b>Are you sure you want to delete branding - "' + name + '"?</b>',
						minWidth: 400,
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {

								Ext.getCmp('brandingGrid').setLoading('Deleting branding...');
								Ext.Ajax.request({
									url: 'api/v1/resource/branding/' + brandingId,
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

				addComponentToMainViewPort(brandingGrid);
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>