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
/* global Ext, CoreService, CoreUtil */

Ext.define('OSF.component.AdvancedSearchPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.AdvancedSearchPanel',
	layout: 'fit',
	
	initComponent: function () {
		this.callParent();

		var advancePanel = this;
		
		var stringOperationData = [
			{
				code: 'CONTAINS',
				description: 'Contains'
			},									
			{
				code: 'EQUALS',
				description: 'Equals'
			},
			{
				code: 'ENDS_LIKE',
				description: 'Ends Like'
			},									
			{
				code: 'STARTS_LIKE',
				description: 'Starts Like'
			}							
		];
		
		var numberOperationData = [
				{
					code: 'EQUALS',
					description: '='
				},
				{
					code: 'GREATERTHAN',
					description: '>'
				},
				{
					code: 'GREATERTHANEQUALS',
					description: '>='
				},
				{
					code: 'LESSTHAN',
					description: '<'
				},
				{
					code: 'LESSTHANEQUALS',
					description: '<='
				}							
		];
		
		var searchTypes = [
			{
				searchType: 'COMPONENT',
				label: 'Entry',
				options: function() {				
					var optPanel = 	Ext.create('Ext.panel.Panel', {					
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'field',
							width: '100%',
							name: 'field',
							fieldLabel: 'Field <span class="field-required" />',
							allowBlank: false,
							editable: false,
							displayField: 'label',
							valueField: 'code',					
							store: {
								data: [
									{code: 'name', label: 'Name'},
									{code: 'description', label: 'Description'},
									{code: 'componentType', label: 'Entry Type/Topic'},
									{code: 'organization', label: 'Organization'},
									{code: 'version', label: 'Version'},
									{code: 'releaseDate', label: 'Release Date'},
									{code: 'approvedDts', label: 'Approved Date'},
									{code: 'lastActivityDts', label: 'Last Activity Date'},
									{code: 'dataSource', label: 'Data Source'}
								]
							},
							listeners: {
								change: function(cb, newValue, oldValue, opt) {
									var optionPanel = cb.up('panel');
									
									optionPanel.getComponent('componentType').setHidden(true);
									optionPanel.getComponent('componentType').setDisabled(true);	
									optionPanel.getComponent('dataSource').setHidden(true);
									optionPanel.getComponent('dataSource').setDisabled(true);										
									optionPanel.getComponent('startDate').setHidden(true);
									optionPanel.getComponent('startDate').setDisabled(true);										
									optionPanel.getComponent('endDate').setHidden(true);
									optionPanel.getComponent('endDate').setDisabled(true);	
									
									optionPanel.getComponent('value').setHidden(false);
									optionPanel.getComponent('value').setDisabled(false);
									optionPanel.getComponent('stringOperation').setHidden(false);
									optionPanel.getComponent('stringOperation').setDisabled(false);	
									optionPanel.getComponent('caseInsensitive').setHidden(false);
									optionPanel.getComponent('caseInsensitive').setDisabled(false);										
									
									if (newValue === 'componentType') {
										optionPanel.getComponent('componentType').setHidden(false);
										optionPanel.getComponent('componentType').setDisabled(false);									
									} 
									if (newValue === 'dataSource') {
										optionPanel.getComponent('dataSource').setHidden(false);
										optionPanel.getComponent('dataSource').setDisabled(false);									
									} 									
									if (newValue === 'releaseDate' || 
										newValue === 'approvedDts' ||
										newValue === 'lastActivityDts') {
										optionPanel.getComponent('startDate').setHidden(false);
										optionPanel.getComponent('startDate').setDisabled(false);
										optionPanel.getComponent('endDate').setHidden(false);
										optionPanel.getComponent('endDate').setDisabled(false);										
									}
																		
									if (newValue === 'componentType' ||
										newValue === 'dataSource' ||
										newValue === 'releaseDate' ||
										newValue === 'approvedDts' ||
										newValue === 'lastActivityDts') 
									{
										optionPanel.getComponent('value').setHidden(true);
										optionPanel.getComponent('value').setDisabled(true);
										optionPanel.getComponent('stringOperation').setHidden(true);
										optionPanel.getComponent('stringOperation').setDisabled(true);
										optionPanel.getComponent('caseInsensitive').setHidden(true);
										optionPanel.getComponent('caseInsensitive').setDisabled(true);	
									}
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},	
						{
							xtype: 'combobox', 
							itemId: 'componentType',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/componenttypes/lookup'									
								}
							}							
						},
						{
							xtype: 'combobox', 
							itemId: 'dataSource',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/DataSource'									
								}
							}							
						},
						{
							xtype: 'datefield',
							itemId: 'startDate',
							name: 'startDate',
							allowBlank: false,
							disabled: true,
							hidden: true,								
							width: '100%',
							fieldLabel: 'Start Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('endDate').setMinValue(newValue);
								}
							}
						},						
						{
							xtype: 'datefield',
							itemId: 'endDate',
							name: 'endDate',
							allowBlank: false,
							width: '100%',
							disabled: true,
							hidden: true,							
							fieldLabel: 'End Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('startDate').setMaxValue(newValue);
								}
							}							
						},	
						{
							xtype: 'checkbox',
							itemId: 'caseInsensitive',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},						
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]					
				});
					return optPanel;
				}
			},
			{
				searchType: 'ATTRIBUTE',
				label: 'Attribute',			
				options: function() { 				
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'keyField',
							width: '100%',
							name: 'keyField',
							fieldLabel: 'Attribute Category <span class="field-required" />',
							editable: false,
							forceSelection: true,
							allowBlank: false,
							displayField: 'description',
							valueField: 'attributeType',
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes/attributetypes',
									reader: {
										type: 'json',
										rootProperty: 'data',
										totalProperty: 'totalNumber'
									}
								}
							},
							listeners: {
								change: function(cb, newValue, oldValue, opts){
									var panel = cb.up('panel');
									var attributeType = null;
									if (cb.getSelection()) {
										attributeType = cb.getSelection().data;										
									}									
									if (attributeType && attributeType.attributeValueType === 'NUMBER') {
											panel.queryById('keyValue').setHidden(true);
											panel.queryById('keyValue').setDisabled(true);
											
											panel.queryById('keyValueNumber').setHidden(false);										
											panel.queryById('numberOperation').setHidden(false);
											panel.queryById('keyValueNumber').setDisabled(false);
											panel.queryById('numberOperation').setDisabled(false);
										
											panel.queryById('keyValueNumber').reset();
											panel.queryById('numberOperation').reset();
									} else {
										
										var codeCb = panel.queryById('keyValue');
										codeCb.setHidden(false);
										panel.queryById('keyValue').setDisabled(false);
										
										codeCb.reset();
										codeCb.getStore().load({
											url: 'api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes'
										});	
									}							
									
								}
							} 
						},
						{
							xtype: 'numberfield',
							itemId: 'keyValueNumber',
							fieldLabel: 'Value',
							name: 'keyValue',
							width: '100%',
							hidden: true,
							allowDecimals: true
						},
						{
							xtype: 'combobox',
							itemId: 'numberOperation',
							width: '100%',
							name: 'numberOperation',
							fieldLabel: 'Number Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'EQUALS',					
							editable: false,
							hidden: true,
							store: {
								data: numberOperationData
							}							
						},
						{
							xtype: 'combobox', 
							itemId: 'keyValue',
							width: '100%',
							name: 'keyValue',
							fieldLabel: 'Specific Category',
							editable: false,
							forceSelection: true,
							displayField: 'label',
							valueField: 'code',	
							queryMode: 'local',
							store: {
								fields: [
									{
										name: 'code',
										mapping: function(data) {
											return data.attributeCodePk.attributeCode;
										}
									}
								],
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes/attributetypes',
									reader: {
										type: 'json',
										rootProperty: 'data',
										totalProperty: 'totalNumber'
									}
								}
							}
						}					
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'ARCHITECTURE',
				label: 'Architecture',
				options: function() {
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
					{
							xtype: 'combobox', 
							itemId: 'keyField',
							width: '100%',
							name: 'keyField',
							fieldLabel: 'Architecture <span class="field-required" />',
							editable: false,
							allowBlank: false,
							displayField: 'description',
							valueField: 'attributeType',
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes/attributetypes',
									reader: {
										type: 'json',
										rootProperty: 'data',
										totalProperty: 'totalNumber'
									}
								},
								listeners: {
									load: function(store, records, success, opts) {
										store.filterBy(function(record){
											return record.get('architectureFlg');
										});
									}
								}
							},
							listeners: {
								change: function(cb, newValue, oldValue, opts){
									var codeCb = cb.up('panel').getComponent('keyValue');
									codeCb.reset();
									codeCb.getStore().load({
										url: 'api/v1/resource/attributes/attributetypes/' + newValue + '/attributecodes'
									});
								}
							} 
						},
						{
							xtype: 'combobox', 
							itemId: 'keyValue',
							width: '100%',
							name: 'keyValue',
							fieldLabel: 'Category',						
							editable: false,
							displayField: 'label',
							valueField: 'architectureCode',	
							queryMode: 'local',
							store: {								
								autoLoad: false,
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/attributes/attributetypes',
									reader: {
										type: 'json',
										rootProperty: 'data',
										totalProperty: 'totalNumber'
									}
								}
							}
						},
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]					
				});
					return optPanel;
				}
			},
			{
				searchType: 'ENTRYTYPE',
				label: 'Entry Type',
				options: function () {
					var optPanel = Ext.create('Ext.panel.Panel', {
						defaults: {
							labelAlign: 'top',
							labelSeparator: ''
						},
						items: [
							{
								xtype: 'combobox',
								itemId: 'value',
								name: 'value',
								width: '100%',
								displayField: 'description',
								valueField: 'code',
								fieldLabel: 'Value',
								editable: false,
								store: {
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/componenttypes/lookup',
									},
									autoLoad: true
								}
							},
							{
								xtype: 'checkbox',
								itemId: 'searchChildren',
								name: 'searchChildren',
								fieldLabel: 'Search Children',
								value: false
							}
						]
					});

					return optPanel;
				}
			},
			{
				searchType: 'INDEX',
				label: 'Index',
				options: function(){ 				
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},										
					items: [
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value',							
							maxLength: 1024
						}						
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'TAG',
				label: 'Tag',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},					
					items: [
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},
						{
							xtype: 'checkbox',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]					
				});
					return optPanel;
				}
			},			
			{
				searchType: 'USER_RATING',
				label: 'User Rating',
				options: function(){ 
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'numberfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Rating (0-5) <span class="field-required" />',
							allowBlank: false,
							maxValue: 5,
							minValue: 0
						},
						{								
							xtype: 'combobox',
							itemId: 'numberOperation',
							width: '100%',
							name: 'numberOperation',
							fieldLabel: 'Number Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'EQUALS',					
							editable: false,
							store: {
								data: numberOperationData
							}					
						}						
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'CONTACT',
				label: 'Contact',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'field',
							width: '100%',
							name: 'field',
							fieldLabel: 'Field <span class="field-required" />',
							allowBlank: false,
							editable: false,
							displayField: 'label',
							valueField: 'code',					
							store: {
								data: [
									{code: 'contactType', label: 'Contact Type'},
									{code: 'firstName', label: 'First Name'},
									{code: 'lastName', label: 'Last Name'},
									{code: 'email', label: 'Email'},
									{code: 'phone', label: 'Phone'},
									{code: 'organization', label: 'Organization'}
								]
							},
							listeners: {
								change: function(cb, newValue, oldValue, opt) {
									var optionPanel = cb.up('panel');
									
									optionPanel.getComponent('contactType').setHidden(true);
									optionPanel.getComponent('contactType').setDisabled(true);									
									optionPanel.getComponent('value').setHidden(false);
									optionPanel.getComponent('value').setDisabled(false);
									optionPanel.getComponent('stringOperation').setHidden(false);
									optionPanel.getComponent('stringOperation').setDisabled(false);
									optionPanel.getComponent('caseInsensitive').setHidden(false);
									optionPanel.getComponent('caseInsensitive').setDisabled(false);
									
									if (newValue === 'contactType') {
										optionPanel.getComponent('contactType').setHidden(false);
										optionPanel.getComponent('contactType').setDisabled(false);
									
										optionPanel.getComponent('value').setHidden(true);
										optionPanel.getComponent('value').setDisabled(true);
										optionPanel.getComponent('stringOperation').setHidden(true);
										optionPanel.getComponent('stringOperation').setDisabled(true);
										optionPanel.getComponent('caseInsensitive').setHidden(true);
										optionPanel.getComponent('caseInsensitive').setDisabled(true);										
									} 
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},						
						{
							xtype: 'combobox', 
							itemId: 'contactType',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/ContactType'									
								}
							}							
						},
						{
							xtype: 'checkbox',
							itemId: 'caseInsensitive',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},						
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]
				});				
					return optPanel;
				}
			},
			{
				searchType: 'REVIEW',
				label: 'User Review',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'field',
							width: '100%',
							name: 'field',
							fieldLabel: 'Field <span class="field-required" />',
							allowBlank: false,
							editable: false,
							displayField: 'label',
							valueField: 'code',					
							store: {
								data: [
									{code: 'userTypeCode', label: 'User Type'},
									{code: 'title', label: 'Title'},
									{code: 'comment', label: 'Comment'},
									{code: 'organization', label: 'Organization'},
									{code: 'userTimeCode', label: 'Experience'},
									{code: 'lastUsed', label: 'Last Used'},
									{code: 'recommend', label: 'Recommend'},
									{code: 'createDts', label: 'Post Date'},
									{code: 'createUser', label: 'User'}
								]
							},
							listeners: {
								change: function(cb, newValue, oldValue, opt) {
									var optionPanel = cb.up('panel');
									
									optionPanel.getComponent('userTypeCode').setHidden(true);
									optionPanel.getComponent('userTypeCode').setDisabled(true);	
									optionPanel.getComponent('userTimeCode').setHidden(true);
									optionPanel.getComponent('userTimeCode').setDisabled(true);																				
									optionPanel.getComponent('startDate').setHidden(true);
									optionPanel.getComponent('startDate').setDisabled(true);										
									optionPanel.getComponent('endDate').setHidden(true);
									optionPanel.getComponent('endDate').setDisabled(true);	
									
									optionPanel.getComponent('value').setHidden(false);
									optionPanel.getComponent('value').setDisabled(false);
									optionPanel.getComponent('stringOperation').setHidden(false);
									optionPanel.getComponent('stringOperation').setDisabled(false);	
									optionPanel.getComponent('caseInsensitive').setHidden(false);
									optionPanel.getComponent('caseInsensitive').setDisabled(false);										
									
									if (newValue === 'userTypeCode') {
										optionPanel.getComponent('userTypeCode').setHidden(false);
										optionPanel.getComponent('userTypeCode').setDisabled(false);									
									} 
									if (newValue === 'userTimeCode') {
										optionPanel.getComponent('userTimeCode').setHidden(false);
										optionPanel.getComponent('userTimeCode').setDisabled(false);									
									} 									
									if (newValue === 'lastUsed' || newValue === 'createDts') {
										optionPanel.getComponent('startDate').setHidden(false);
										optionPanel.getComponent('startDate').setDisabled(false);
										optionPanel.getComponent('endDate').setHidden(false);
										optionPanel.getComponent('endDate').setDisabled(false);										
									}
																		
									if (newValue === 'userTypeCode' ||
										newValue === 'userTimeCode' ||
										newValue === 'lastUsed' ||
										newValue === 'createDts') 
									{
										optionPanel.getComponent('value').setHidden(true);
										optionPanel.getComponent('value').setDisabled(true);
										optionPanel.getComponent('stringOperation').setHidden(true);
										optionPanel.getComponent('stringOperation').setDisabled(true);
										optionPanel.getComponent('caseInsensitive').setHidden(true);
										optionPanel.getComponent('caseInsensitive').setDisabled(true);	
									}
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},	
						{
							xtype: 'combobox', 
							itemId: 'userTypeCode',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/UserTypeCode'									
								}
							}							
						},
						{
							xtype: 'combobox', 
							itemId: 'userTimeCode',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/ExperienceTimeType'									
								}
							}							
						},
						{
							xtype: 'datefield',
							itemId: 'startDate',
							name: 'startDate',
							allowBlank: false,
							disabled: true,
							hidden: true,								
							width: '100%',
							fieldLabel: 'Start Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('endDate').setMinValue(newValue);
								}
							}							
						},						
						{
							xtype: 'datefield',
							itemId: 'endDate',
							name: 'endDate',
							allowBlank: false,
							width: '100%',
							disabled: true,
							hidden: true,							
							fieldLabel: 'End Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('startDate').setMaxValue(newValue);
								}
							}							
						},
//There some Ext.js bug not reseting more than 10 field? This field disappears on reset of the first field.  						
//						{
//							xtype: 'checkbox',
//							itemId: 'recommend',
//							name: 'value',
//							uncheckedValue: 'false',
//							disabled: true,
//							hidden: true,
//							boxLabel: 'Recommended'
//						},
						{
							xtype: 'checkbox',
							itemId: 'caseInsensitive',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},						
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'REVIEWPRO',
				label: 'User Review Pro',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},
						{
							xtype: 'checkbox',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'REVIECON',
				label: 'User Review Con',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},
						{
							xtype: 'checkbox',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'QUESTION',
				label: 'Question',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'field',
							width: '100%',
							name: 'field',
							fieldLabel: 'Field <span class="field-required" />',
							allowBlank: false,
							editable: false,
							displayField: 'label',
							valueField: 'code',					
							store: {
								data: [
									{code: 'userTypeCode', label: 'User Type'},
									{code: 'question', label: 'Question'},
									{code: 'organization', label: 'Organization'},
									{code: 'createDts', label: 'Post Date'},
									{code: 'createUser', label: 'User'}
								]
							},
							listeners: {
								change: function(cb, newValue, oldValue, opt) {
									var optionPanel = cb.up('panel');
									
									optionPanel.getComponent('userTypeCode').setHidden(true);
									optionPanel.getComponent('userTypeCode').setDisabled(true);	
									optionPanel.getComponent('startDate').setHidden(true);
									optionPanel.getComponent('startDate').setDisabled(true);										
									optionPanel.getComponent('endDate').setHidden(true);
									optionPanel.getComponent('endDate').setDisabled(true);	
									
									optionPanel.getComponent('value').setHidden(false);
									optionPanel.getComponent('value').setDisabled(false);
									optionPanel.getComponent('stringOperation').setHidden(false);
									optionPanel.getComponent('stringOperation').setDisabled(false);	
									optionPanel.getComponent('caseInsensitive').setHidden(false);
									optionPanel.getComponent('caseInsensitive').setDisabled(false);										
									
									if (newValue === 'userTypeCode') {
										optionPanel.getComponent('userTypeCode').setHidden(false);
										optionPanel.getComponent('userTypeCode').setDisabled(false);									
									} 								
									if ( newValue === 'createDts') {
										optionPanel.getComponent('startDate').setHidden(false);
										optionPanel.getComponent('startDate').setDisabled(false);
										optionPanel.getComponent('endDate').setHidden(false);
										optionPanel.getComponent('endDate').setDisabled(false);										
									}									
																		
									if (newValue === 'userTypeCode' ||									
										newValue === 'createDts') 
									{
										optionPanel.getComponent('value').setHidden(true);
										optionPanel.getComponent('value').setDisabled(true);
										optionPanel.getComponent('stringOperation').setHidden(true);
										optionPanel.getComponent('stringOperation').setDisabled(true);
										optionPanel.getComponent('caseInsensitive').setHidden(true);
										optionPanel.getComponent('caseInsensitive').setDisabled(true);	
									}
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},	
						{
							xtype: 'combobox', 
							itemId: 'userTypeCode',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/UserTypeCode'									
								}
							}							
						},
						{
							xtype: 'datefield',
							itemId: 'startDate',
							name: 'startDate',
							allowBlank: false,
							disabled: true,
							hidden: true,								
							width: '100%',
							fieldLabel: 'Start Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('endDate').setMinValue(newValue);
								}
							}							
						},						
						{
							xtype: 'datefield',
							itemId: 'endDate',
							name: 'endDate',
							allowBlank: false,
							width: '100%',
							disabled: true,
							hidden: true,							
							fieldLabel: 'End Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('startDate').setMaxValue(newValue);
								}
							}							
						},	
						{
							xtype: 'checkbox',
							itemId: 'caseInsensitive',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},						
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'QUESTION_RESPONSE',
				label: 'Question Response',
				options: function(){
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'field',
							width: '100%',
							name: 'field',
							fieldLabel: 'Field <span class="field-required" />',
							allowBlank: false,
							editable: false,
							displayField: 'label',
							valueField: 'code',					
							store: {
								data: [
									{code: 'userTypeCode', label: 'User Type'},
									{code: 'response', label: 'Response'},
									{code: 'organization', label: 'Organization'},
									{code: 'createDts', label: 'Post Date'},
									{code: 'createUser', label: 'User'}
								]
							},
							listeners: {
								change: function(cb, newValue, oldValue, opt) {
									var optionPanel = cb.up('panel');
									
									optionPanel.getComponent('userTypeCode').setHidden(true);
									optionPanel.getComponent('userTypeCode').setDisabled(true);	
									optionPanel.getComponent('startDate').setHidden(true);
									optionPanel.getComponent('startDate').setDisabled(true);										
									optionPanel.getComponent('endDate').setHidden(true);
									optionPanel.getComponent('endDate').setDisabled(true);	
									
									optionPanel.getComponent('value').setHidden(false);
									optionPanel.getComponent('value').setDisabled(false);
									optionPanel.getComponent('stringOperation').setHidden(false);
									optionPanel.getComponent('stringOperation').setDisabled(false);	
									optionPanel.getComponent('caseInsensitive').setHidden(false);
									optionPanel.getComponent('caseInsensitive').setDisabled(false);										
									
									if (newValue === 'userTypeCode') {
										optionPanel.getComponent('userTypeCode').setHidden(false);
										optionPanel.getComponent('userTypeCode').setDisabled(false);									
									} 								
									if ( newValue === 'createDts') {
										optionPanel.getComponent('startDate').setHidden(false);
										optionPanel.getComponent('startDate').setDisabled(false);
										optionPanel.getComponent('endDate').setHidden(false);
										optionPanel.getComponent('endDate').setDisabled(false);										
									}									
																		
									if (newValue === 'userTypeCode' ||									
										newValue === 'createDts') 
									{
										optionPanel.getComponent('value').setHidden(true);
										optionPanel.getComponent('value').setDisabled(true);
										optionPanel.getComponent('stringOperation').setHidden(true);
										optionPanel.getComponent('stringOperation').setDisabled(true);
										optionPanel.getComponent('caseInsensitive').setHidden(true);
										optionPanel.getComponent('caseInsensitive').setDisabled(true);	
									}
								}
							}
						},
						{
							xtype: 'textfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							maxLength: 1024
						},	
						{
							xtype: 'combobox', 
							itemId: 'userTypeCode',
							width: '100%',
							name: 'value',
							fieldLabel: 'Value <span class="field-required" />',
							allowBlank: false,
							editable: false,
							disabled: true,
							hidden: true,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/UserTypeCode'									
								}
							}							
						},
						{
							xtype: 'datefield',
							itemId: 'startDate',
							name: 'startDate',
							allowBlank: false,
							disabled: true,
							hidden: true,								
							width: '100%',
							fieldLabel: 'Start Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('endDate').setMinValue(newValue);
								}
							}							
						},						
						{
							xtype: 'datefield',
							itemId: 'endDate',
							name: 'endDate',
							allowBlank: false,
							width: '100%',
							disabled: true,
							hidden: true,							
							fieldLabel: 'End Date <span class="field-required" />',
							listeners: {
								change: function(dateField, newValue, oldValue, opts) {
									var optionPanel = dateField.up('panel');
									optionPanel.getComponent('startDate').setMaxValue(newValue);
								}
							}							
						},	
						{
							xtype: 'checkbox',
							itemId: 'caseInsensitive',
							name: 'caseInsensitive',
							boxLabel: 'Case Insensitive'
						},						
						{								
							xtype: 'combobox',
							itemId: 'stringOperation',
							width: '100%',
							name: 'stringOperation',
							fieldLabel: 'String Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'CONTAINS',					
							editable: false,
							store: {
								data: stringOperationData
							}					
						}						
					]
				});
					return optPanel;
				}
			},
			{
				searchType: 'EVALUTATION_SCORE',
				label: 'Evaluation Score',
				options: function() { 									
					var optPanel = Ext.create('Ext.panel.Panel', {
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},	
					items: [
						{
							xtype: 'combobox', 
							itemId: 'evaluationSection',
							width: '100%',
							name: 'keyField',
							fieldLabel: 'Section <span class="field-required" />',
							allowBlank: false,	
							editable: false,
							displayField: 'description',
							valueField: 'code',					
							queryMode: 'remote',
							store: {
								proxy: {
									type: 'ajax',
									url: 'api/v1/resource/lookuptypes/EvaluationSection'									
								}
							}							
						},
						{
							xtype: 'numberfield',
							itemId: 'value',
							name: 'value',
							width: '100%',
							fieldLabel: 'Value  <span class="field-required" />',
							allowBlank: false,
							allowDecimal: true,
							maxValue: 5,
							minValue: 0,
							maxLength: 1
						},
						{								
							xtype: 'combobox',
							itemId: 'numberOperation',
							width: '100%',
							name: 'numberOperation',
							fieldLabel: 'Number Operation',
							queryMode: 'local',
							displayField: 'description',
							valueField: 'code',
							value: 'EQUALS',					
							editable: false,
							store: {
								data: numberOperationData
							}					
						}						
					]
				});
					return optPanel;
				}	
			}			
						
		];
		
		searchTypes.sort(function(a, b){
			return a.searchType.localeCompare(b.searchType);
		});
			
		advancePanel.resetEntryForm = function(){
			advancePanel.entryForm.getComponent('searchType').suspendEvents(true);
			advancePanel.entryForm.reset();
			advancePanel.entryForm.getComponent('searchType').resumeEvents();
		};
		
		advancePanel.entryForm = Ext.create('Ext.form.Panel', {
			layout: 'anchor',
			bodyStyle: 'padding: 10px;',
			scrollable: true,
			defaults: {
				labelAlign: 'top',
				labelSeparator: ''
			},			
			items: [
				{
					xtype: 'combobox',
					width: '100%',
					itemId: 'searchType',
					name: 'searchType',
					fieldLabel: 'Search Type',
					queryMode: 'local',
					displayField: 'label',
					valueField: 'searchType',					
					allowBlank: false,
					editable: false,
					value: 'COMPONENT',
					store: {
						data: searchTypes
					},
					listeners: {
						change: function(typeCB, newValue, oldValue, opts) {
							var optionsPanel = advancePanel.entryForm.getComponent('options');
							optionsPanel.removeAll();
							var optPanel = typeCB.getSelection().data.options();
							optionsPanel.add(optPanel);
						}
					}
				},
				{
					xtype: 'panel',
					itemId: 'options',
					layout: 'card',
					items: [								
					]
				},
				{								
					xtype: 'combobox',
					width: '100%',
					name: 'mergeCondition',
					fieldLabel: 'Merge Condition',
					queryMode: 'local',
					displayField: 'description',
					valueField: 'code',
					value: 'OR',
					allowBlank: false,
					editable: false,
					store: {
						data: [
							{
								code: 'OR',
								description: 'OR'
							},
							{
								code: 'AND',
								description: 'AND'
							},
							{
								code: 'NOT',
								description: 'NOT'
							}							
						]
					}					
				}, 
				{
					xtype: 'panel',
					itemId: 'buttonPanel',
					layout: 'hbox',
					items: [
						{
							xtype: 'button',
							itemId: 'saveButton',
							formBind: true,
							text: 'Add Criteria',
							minWidth: 175,
							iconCls: 'fa fa-lg fa-plus',
							handler: function() {
								var saveButton = this;
								
								var data = advancePanel.entryForm.getValues();						
								data.typeDescription = advancePanel.entryForm.getComponent('searchType').getSelection().data.label;
								if(data.startDate) {
									data.startDate = Ext.Date.parse(data.startDate, 'm/d/Y');
									data.startDate = Ext.Date.format(data.startDate, 'Y-m-d\\TH:i:s.u');
								}
								if(data.endDate) {
									data.endDate = Ext.Date.parse(data.endDate, 'm/d/Y');
									data.endDate = Ext.Date.add(data.endDate, Ext.Date.DAY, 1);
									data.endDate = Ext.Date.subtract(data.endDate, Ext.Date.MILLI, 1);
									data.endDate = Ext.Date.format(data.endDate, 'Y-m-d\\TH:i:s.u');
								}

								var search = {
									searchElements: [
										data
									]
								};

								CoreUtil.submitForm({
									url: 'api/v1/service/search/advance',
									method: 'POST',
									data: search,
									form: advancePanel.entryForm,
									loadingText: 'Adding Search Criteria...',
									success: function(response, opts) {
										advancePanel.resetEntryForm();
										
										saveButton.setText('Add Criteria');
										var grid = advancePanel.entryForm.getComponent('searchGrid');										
										if (advancePanel.entryForm.editRecord) {
											
											for (var i = 0; i < advancePanel.searchcriteriaRecords.length; i++) {
												if (advancePanel.searchcriteriaRecords[i].getId() === advancePanel.entryForm.editRecord.getId()) {
													advancePanel.searchcriteriaRecords[i] = data;
												}
											}											
											grid.getStore().loadData(advancePanel.searchcriteriaRecords);											
											advancePanel.entryForm.editRecord = null;											
										} else {
											grid.getStore().add(data);																		
										}
										advancePanel.changed = true;
									},
									failure: function(response, opts) {
										var errorResponse = Ext.decode(response.responseText);
										var errorMessage = '';
										Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
											errorMessage += '<b>' + item.key + ': </b> ' + item.value + '<br>';									
										});
										Ext.Msg.show({
											title:'Validation',
											message: errorMessage,
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR,
											fn: function(btn) {
											}
										});								
									}
								});

							}			
						},
						{
							xtype: 'button',
							text: 'Cancel',							
							iconCls: 'fa fa-lg fa-close',
							margin: '0 0 0 20',
							handler: function() {
								advancePanel.resetEntryForm();
								advancePanel.entryForm.editRecord = null;
								advancePanel.entryForm.getComponent('buttonPanel').getComponent('saveButton').setText('Add Criteria');
							}
						}						
					]
				},				
				{
					xtype: 'grid',
					itemId: 'searchGrid',
					title: 'Search Criteria',
					columnLines: true,
					width: '100%',
					//height: 250,
					style: 'margin-top: 20px;',
					store: {						
					},
					columns: [
						{ text: 'Type', dataIndex: 'typeDescription', width: 200, sortable: false },
						{ text: 'Criteria', dataIndex: 'value', flex: 1, minWidth: 200, sortable: false,
							renderer: function(value, meta, record) {
								var options = '';
								
								if (record.get('field')) {
									options += '<b>Field: </b>' + record.get('field') + '<br>';
								}
								if (record.get('value')) {
									options += '<b>Value: </b>' + record.get('value') + '<br>';
								}
								if (record.get('keyField')) {
									options += '<b>Key Field: </b>' + record.get('keyField') + '<br>';
								}
								if (record.get('keyValue')) {
									options += '<b>Key Value: </b>' + record.get('keyValue') + '<br>';
								}
								if (record.get('startDate')) {
									options += '<b>Start Date: </b>' + record.get('startDate') + '<br>';
								}
								if (record.get('endDate')) {
									options += '<b>End Date: </b>' + record.get('endDate') + '<br>';
								}								
								if (record.get('caseInsensitive')) {
									options += '<b>Case Insensitive: </b>' + record.get('caseInsensitive') + '<br>';
								}
								if (record.get('numberOperation')) {
									options += '<b>Number Operation: </b>' + record.get('numberOperation') + '<br>';
								}
								if (record.get('stringOperation')) {
									options += '<b>String Operation: </b>' + record.get('stringOperation') + '<br>';
								}								
								
								return options;
							}							
						},
						{ text: 'Operation', align: 'center', dataIndex: 'mergeCondition', width: 200, sortable: false,
							renderer: function(value, meta, record, rowIndex) {							
								//if (rowIndex >= 1) {
									return value;								
								//} else {
								//	return 'N/A';
								//}
							}
						},
						{ 
							xtype:'actioncolumn',
							width: 70,
							items: [
								{
									iconCls: 'fa fa-lg fa-edit icon-button-color-edit action-icon icon-small-horizontal-correction-right',
									tooltip: 'Edit',									
									handler: function(grid, rowIndex, colIndex) {
										var rec = grid.getStore().getAt(rowIndex);
										advancePanel.entryForm.editRecord = rec;
										
										advancePanel.searchcriteriaRecords = [];
										
										grid.getStore().each(function(item){
											advancePanel.searchcriteriaRecords.push(item);	
										});										
										
										//manually set
										advancePanel.entryForm.getComponent('searchType').setValue(rec.get('searchType'));
										
										advancePanel.entryForm.loadRecord(rec);

										//The rest is tricky since multiple value fields
										var optionsPanel = advancePanel.entryForm.getComponent('options').getLayout().getActiveItem();

										var searchType = rec.get('searchType');
										var value = rec.get('value');										
										if (searchType === 'COMPONENT') {
											var fieldValue = optionsPanel.getComponent('field').getValue();

											optionsPanel.getComponent('componentType').setValue(null);
											optionsPanel.getComponent('dataSource').setValue(null);	
											optionsPanel.getComponent('value').setValue(null);

											if (fieldValue === 'componentType') {
												optionsPanel.getComponent('componentType').setValue(value);
											} else if (fieldValue === 'dataSource') {
												optionsPanel.getComponent('dataSource').setValue(value);									
											} else {
												optionsPanel.getComponent('value').setValue(value);	
											}											

										} else if (searchType === 'ATTRIBUTE') {
											var keyField = optionsPanel.getComponent('keyField');
											keyField.getStore().load({
												callback: function() {
													advancePanel.entryForm.loadRecord(rec);
												}
											});
										} else if (searchType === 'ARCHITECTURE') {
											var keyField = optionsPanel.getComponent('keyField');
											keyField.getStore().load({
												callback: function() {
													advancePanel.entryForm.loadRecord(rec);
												}
											});
										} else if (searchType === 'CONTACT') {
											var fieldValue = optionsPanel.getComponent('field').getValue();

											optionsPanel.getComponent('contactType').setValue(null);											
											optionsPanel.getComponent('value').setValue(null);

											if (fieldValue === 'contactType') {
												optionsPanel.getComponent('contactType').setValue(value);
											} else {
												optionsPanel.getComponent('value').setValue(value);	
											}

										} else if (searchType === 'REVIEW') {
											var fieldValue = optionsPanel.getComponent('field').getValue();

											optionsPanel.getComponent('userTypeCode').setValue(null);	
											optionsPanel.getComponent('userTimeCode').setValue(null);
											//optionsPanel.getComponent('recommend').setValue(null);
											optionsPanel.getComponent('value').setValue(null);

											if (fieldValue === 'userTypeCode') {
												optionsPanel.getComponent('userTypeCode').setValue(value);
											} else if (fieldValue === 'userTimeCode') {
												optionsPanel.getComponent('userTimeCode').setValue(value);
//											} else if (fieldValue === 'recommend') {
//												optionsPanel.getComponent('recommend').setValue(value);
											} else {
												optionsPanel.getComponent('value').setValue(value);	
											}

										} else if (searchType === 'QUESTION') {
											var fieldValue = optionsPanel.getComponent('field').getValue();

											optionsPanel.getComponent('userTypeCode').setValue(null);											
											optionsPanel.getComponent('value').setValue(null);

											if (fieldValue === 'userTypeCode') {
												optionsPanel.getComponent('userTypeCode').setValue(value);
											} else {
												optionsPanel.getComponent('value').setValue(value);	
											}											

										} else if (searchType === 'QUESTION_RESPONSE') {
											var fieldValue = optionsPanel.getComponent('field').getValue();

											optionsPanel.getComponent('userTypeCode').setValue(null);											
											optionsPanel.getComponent('value').setValue(null);

											if (fieldValue === 'userTypeCode') {
												optionsPanel.getComponent('userTypeCode').setValue(value);
											} else {
												optionsPanel.getComponent('value').setValue(value);	
											}											
										}											
										

										
										//change button to update
										advancePanel.entryForm.getComponent('buttonPanel').getComponent('saveButton').setText('Update Criteria');										
										
									}									
								},
								{
									iconCls: 'fa fa-lg fa-trash action-icon icon-button-color-warning icon-small-horizontal-correction-right',
									tooltip: 'Delete',									
									handler: function(grid, rowIndex, colIndex) {
										var rec = grid.getStore().getAt(rowIndex);
										
										Ext.Msg.show({
											title:'Delete?',
											message: 'Delete search criteria?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													grid.getStore().removeAt(rowIndex);
													advancePanel.changed = true;
												} 
											}
										});																				
									}
								}
							]
						}
					]
				}
			]
		});		
		
		Ext.Array.each(searchTypes, function(type) {
			if (type.searchType === 'COMPONENT') {
				advancePanel.entryForm.getComponent('options').add(type.options());
			}			
		});		
		advancePanel.entryForm.getComponent('searchType').setValue('COMPONENT');
		
		advancePanel.add(advancePanel.entryForm);		
		
	},
	
	reset: function() {
		var advancePanel = this;		
		advancePanel.resetEntryForm();
		advancePanel.entryForm.getComponent('searchGrid').getStore().removeAll();
	},

	edit: function(searchElements) {
		var advancePanel = this;	
		advancePanel.reset();
		advancePanel.entryForm.getComponent('searchGrid').getStore().add(searchElements);		
	},
	
	getSearch: function(){
		var advancePanel = this;
		
		//return the search object
		if (advancePanel.checkForCriteria()) {
			var store = advancePanel.entryForm.getComponent('searchGrid').getStore();
			
			var searchElements = [];
			store.each(function(record){
				searchElements.push(record.data);
			});
			
			var search = {
				sortField: 'name',
				sortDirection: 'ASC',
				searchElements: searchElements
			};
			
			return search;
		}
		return null;
	},
	
	saveSearch: function(){
		var advancePanel = this;
		//prompt for name 
		//save
		
		if (advancePanel.checkForCriteria()) {
			var search = advancePanel.getSearch();
			
			var saveWindow = Ext.create('Ext.window.Window', {
				alwaysOnTop: true,
				modal: true,
				title: 'Save Search',
				width: 300,
				height: 170,
				closeAction: 'destroy',
				layout: 'fit',
				items: [
					{
						xtype: 'form',
						bodyStyle: 'padding: 10px',
						layout: 'anchor',
						items: [
							{
								xtype: 'textfield',
								width: '100%',
								name: 'searchName',
								allowBlank: false,
								maxLength: 255,
								labelAlign: 'top',
								fieldLabel: 'Name <span class="field-required" />',
								labelSeparator: ''
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
										iconCls: 'fa fa-lg fa-save icon-button-color-save',
										handler: function(){			
											var win = this.up('window');
											var form  = this.up('form');											
											var formData = form.getValues();
											
											var userSearch = {
												searchName: formData.searchName,
												searchRequest: Ext.encode(search)
											};
											
											CoreUtil.submitForm({
												url: 'api/v1/resource/usersavedsearches',
												method: 'POST',
												data: userSearch,
												form: form,
												success: function(response, opts){
													Ext.toast("Save Search Successfully.");
													if (advancePanel.saveHook) {
														advancePanel.saveHook(response, opts);
													}
													win.close();
												}
											});											
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										formBind: true,
										iconCls: 'fa fa-lg fa-close icon-button-color-warning',
										handler: function(){
											this.up('window').close();
										}										
									}
								]
							}
						]
					}
				]
			});
			saveWindow.show();
		}
	},	
	
	previewResults: function() {
		var advancePanel = this;
		
		//run search and show results window
		if (advancePanel.checkForCriteria()) {
			var search = advancePanel.getSearch();
			
			
			var resultsStore = Ext.create('Ext.data.Store', {
				fields: ['name', 'description'],
				pageSize: 50,
				autoLoad: false,
				remoteSort: true,
				sorters: [
					new Ext.util.Sorter({
						property: 'name',
						direction: 'ASC'
					})
				],
				proxy: CoreUtil.pagingProxy({
					actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}
				})
			});
			
			var previewWin = Ext.create('Ext.window.Window', {
				title: 'Search Results',
				iconCls: 'fa fa-lg fa-search icon-small-vertical-correction',
				modal: true,
				width: '70%',
				height: '50%',
				maximizable: true,
				closeAction: 'destory',
				layout: 'fit',
				items: [
					{
						xtype: 'grid',
						columnLines: true,
						store: resultsStore,
						columns: [
							{text: 'Name',
								cellWrap: true,
								dataIndex: 'name',
								width: 150,
								autoSizeColumn: false,
								renderer: function (value) {
									return '<span class="search-tools-column-orange-text">' + value + '</span>';
								}
							},
							{text: 'Description',
								dataIndex: 'description',
								flex: 1,
								autoSizeColumn: true,
								cellWrap: true,
								renderer: function (value) {
									value = Ext.util.Format.stripTags(value);
									var str = value.substring(0, 500);
									if (str === value) {
										return str;
									} else {
										str = str.substr(0, Math.min(str.length, str.lastIndexOf(' ')));
										return str += ' ... <br/>';
									}
								}
							}
						],
						dockedItems: [{
								xtype: 'pagingtoolbar',
								store: resultsStore,
								dock: 'bottom',
								displayInfo: true
						}]
					}
				]
			});
			previewWin.show();
			
			
			
			resultsStore.getProxy().buildRequest = function (operation) {
				var initialParams = Ext.apply({
					paging: true,
					sortField: operation.getSorters()[0].getProperty(),
					sortOrder: operation.getSorters()[0].getDirection(),
					offset: operation.getStart(),
					max: operation.getLimit()
				}, operation.getParams());
				params = Ext.applyIf(initialParams, resultsStore.getProxy().getExtraParams() || {});

				var request = new Ext.data.Request({
					url: 'api/v1/service/search/advance',
					params: params,
					operation: operation,
					action: operation.getAction(),
					jsonData: Ext.util.JSON.encode(search)
				});
				operation.setRequest(request);

				return request;
			};

			resultsStore.loadPage(1);
			
		}
	},
	
	checkForCriteria: function() {
		var advancePanel = this;
		
		var store = advancePanel.entryForm.getComponent('searchGrid').getStore();
		if (store.getCount() <= 0) {
			Ext.Msg.show({
				title:'Validation',
				message: 'Enter search criteria to continue.',
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				fn: function(btn) {
				}
			});			
			return false;
		}
		return true;
	}
	
});


