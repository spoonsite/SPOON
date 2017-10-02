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

/* global Ext, CoreUtil, CoreService */

Ext.define('OSF.component.StandardComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.StandardComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	width: 175,
	margin: '0 20 0 0',
	valueField: 'code',
	displayField: 'description',
	typeAhead: true,
	forceSelection: true,	
	queryMode: 'local',
	labelAlign: 'top',	
	
	initComponent: function() {
		var me = this;	
		
		if (me.storeConfig)
		{
			var cbStore = CoreUtil.lookupStore(me.storeConfig);
			me.store = cbStore;
		}
		
		this.callParent();
	}	
	
});

Ext.define('OSF.component.SecurityComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.SecurityComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	fieldLabel: 'Security Marking',
	name: 'securityMarkingType',
	width: '100%',	
	valueField: 'code',
	displayField: 'description',
	typeAhead: false,
	editable: false,
	forceSelection: true,
	hidden: true,
	queryMode: 'local',
	labelAlign: 'top',
	addSelect: true,
	store: {
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/lookuptypes/SecurityMarkingType'			
		},
		listeners: {
			load: function(store, records, opts) {
				if (store.addSelect) {
					store.add({
						code: null,
						description: 'Select'
					});
				}				
			}
		}
	},	
	initComponent: function() {
		var combo = this;	
		combo.callParent();
		combo.getStore().addSelect = combo.addSelect;
		
		//check branding to see it should show
		
		CoreService.brandingservice.getCurrentBranding().then(function(branding){
			if (branding.allowSecurityMarkingsFlg) {
				combo.setHidden(false);
			}
		});
		
	}	
	
});

Ext.define('OSF.component.DataSensitivityComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.DataSensitivityComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	fieldLabel: 'Data Sensitivity',
	name: 'dataSensitivity',
	width: '100%',	
	valueField: 'dataSensitivity',
	displayField: 'dataSensitivityDesc',
	typeAhead: false,
	editable: false,
	forceSelection: true,
	addSelect: true,
	hidden: true,
	queryMode: 'local',
	labelAlign: 'top',
	store: {
		autoLoad: false,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/lookuptypes/DataSensitivity'			
		}
	},		
	initComponent: function() {
		var combo = this;	
		combo.callParent();
		
		CoreService.userservice.getCurrentUser().then(function(user){			
			var data = [];		
			Ext.Ajax.request({
				url: 'api/v1/resource/lookuptypes/DataSensitivity',
				success: function(response, opts) {
					var lookups = Ext.decode(response.responseText);
				
					Ext.Array.each(user.roles, function(securityRole){
						Ext.Array.each(securityRole.dataSecurity, function(item){
							if (item.dataSensitivity) {
								var found = Ext.Array.findBy(lookups, function(lookup){
									if (item.dataSensitivity === lookup.code) {
										return true;
									}
								});
								var add = true;
								Ext.Array.each(data, function(dataItem){
									if (dataItem.dataSensitivity === item.dataSensitivity) {
										add = false;
									}
								});
								
								if (add) {
									data.push({
										dataSensitivity: item.dataSensitivity,
										dataSensitivityDesc: found.description
									});
								}
							}
						});						
					});
					
					if (data.length > 0) {
						combo.setHidden(false);
					}
					
					if (combo.addSelect) {
						data.push({
							dataSensitivity: null,
							dataSensitivityDesc: 'Select'
						});
					}

					combo.getStore().loadData(data);
					combo.fireEvent('ready', combo);
				}
			});						
		});	
	}	
	
});

Ext.define('OSF.component.DataSourceComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.DataSourceComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	fieldLabel: 'Data Source',
	name: 'dataSource',
	width: '100%',	
	valueField: 'dataSource',
	displayField: 'dataSourceDesc',
	typeAhead: false,
	editable: false,
	forceSelection: true,
	addSelect: true,
	hidden: false,
	hideOnNoData: false,
	queryMode: 'local',
	labelAlign: 'top',
	store: {
		autoLoad: false,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/lookuptypes/DataSource'			
		}
	},	
	initComponent: function() {
		var combo = this;	
		combo.callParent();
		
		CoreService.userservice.getCurrentUser().then(function(user){			
			var data = [];
			
			Ext.Ajax.request({
				url: 'api/v1/resource/lookuptypes/DataSource',
				success: function(response, opts) {
					var lookups = Ext.decode(response.responseText);
				
					Ext.Array.each(user.roles, function(securityRole){
						Ext.Array.each(securityRole.dataSecurity, function(item){
							if (item.dataSource) {
								var found = Ext.Array.findBy(lookups, function(lookup){
									if (item.dataSource === lookup.code) {
										return true;
									}
								});

								var add = true;
								Ext.Array.each(data, function(dataItem){
									if (dataItem.dataSource === item.dataSource) {
										add = false;
									}
								});
								
								if (add) {
									data.push({
										dataSource: item.dataSource,
										dataSourceDesc: found.description
									});
								}
							}
						});
					});
					if (combo.hideOnNoData && data.length === 0) {
						combo.setHidden(true);
					}					
					
					if (combo.addSelect) {
						data.push({
							dataSource: null,
							dataSourceDesc: 'Select'
						});
					}

					combo.getStore().loadData(data);
					combo.fireEvent('ready', combo);
				}
			});			
		});		
		
	}	
	
});


Ext.define('OSF.component.UserMenu', {
    extend: 'Ext.button.Button',
	alias: 'widget.osf-UserMenu',
		
	scale: 'large',
	ui: 'default',		
	maxWidth: 250,
	initCallBack: null,
	showUserTools: true,
	showAdminTools: true,
	showEvaluatorTools: true,
	showHelp: true,
	showFeedback: true,
	menu: {
		minWidth: 200
	},
	helpWin: Ext.create('OSF.component.HelpWindow', {}),
	feedbackWin: Ext.create('OSF.component.FeedbackWindow',{}),
	customMenuItems: [],	
	initComponent: function() {
		this.callParent();
		
		var userMenu = this;	
		
		var menu = userMenu.getMenu();
				
		userMenu.loadMenu = function() {		
			menu.removeAll();
				
			var menuItems = [];

			menuItems.push({
				text: 'Home',
				iconCls: 'fa fa-2x fa-home icon-button-color-default',
				href: 'Landing.action',
				handler: function(){
					window.location.href = 'Landing.action';			
				}
			});

			if (userMenu.showAdminTools) {
				menuItems.push({
					text: 'Admin Tools',
					itemId: 'menuAdminTools',
					iconCls: 'fa fa-2x fa-gear icon-button-color-default',
					hidden: true,
					href: 'AdminTool.action',
					handler: function(){
						window.location.href = 'AdminTool.action';			
					}					
				});
			}		

			if (userMenu.showUserTools) {
				menuItems.push({
					text: 'User Tools',
					iconCls: 'fa fa-2x fa-user icon-button-color-default',
					href: 'UserTool.action',
					handler: function(){
						window.location.href = 'UserTool.action';			
					}					
				});
			}			

			if (userMenu.showEvaluatorTools) {
				menuItems.push({
					text: 'Evaluation Tools',
					itemId: 'menuEvalTools',
					hidden: true,
					iconCls: 'fa fa-2x fa-th-list icon-button-color-default',
					href: 'EvaluationTool.action',
					handler: function(){
						window.location.href = 'EvaluationTool.action';			
					}					
				});
			}	
			menuItems.push({
				xtype: 'menuseparator'		
			});	

			if (userMenu.customMenuItems && userMenu.customMenuItems.length > 0) {
				menuItems = menuItems.concat(userMenu.customMenuItems);
				menuItems.push({
					xtype: 'menuseparator'		
				});	
			}

			if (userMenu.showHelp) {
				menuItems.push({
					text: '<b>Help</b>',
					iconCls: 'fa fa-2x fa-question-circle icon-button-color-default',
					handler: function() {
						userMenu.helpWin.show();
					}			
				});		
			}

			if (userMenu.showFeedback) {
				menuItems.push({
					text: '<b>Feedback / issues</b>',
					iconCls: 'fa fa-2x fa-commenting icon-button-color-default',
					handler: function() {
						userMenu.feedbackWin.show();
					}		
				});
			}

			menuItems.push({
				xtype: 'menuseparator'		
			});		

			menuItems.push({
				text: 'Logout',
				iconCls: 'fa fa-2x fa-sign-out icon-button-color-default',
				href: 'Login.action?Logout',
				handler: function(){
					window.location.href = 'Login.action?Logout';			
				}				
			});
			menu.add(menuItems);
		};
		userMenu.loadMenu();
		
		
		menu.on('beforerender', function () {
			this.setWidth(this.up('button').getWidth());
		});
		
		CoreService.userservice.getCurrentUser().then(function(usercontext){
								
				var userMenuText = usercontext.username;
				if (usercontext.firstName && usercontext.lastName)
				{
					userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
				}
				userMenu.setText(userMenuText);	
				
				var permissions = [
					"ADMIN-USER-MANAGEMENT",
					"ADMIN-SYSTEM-MANAGEMENT",
					"ADMIN-ENTRY-MANAGEMENT",
					"ADMIN-MESSAGE-MANAGEMENT",
					"ADMIN-JOB-MANAGEMENT",
					"ADMIN-INTEGRATION",
					"ADMIN-DATA-IMPORT-EXPORT",
					"ADMIN-WATCHES",
					"ADMIN-TRACKING",
					"ADMIN-SEARCH",
					"ADMIN-USER-MANAGEMENT-PROFILES",
					"ADMIN-TEMPMEDIA-MANAGEMENT",
					"ADMIN-ORGANIZATION",
					"ADMIN-LOOKUPS",
					"ADMIN-HIGHLIGHTS",
					"ADMIN-MEDIA",
					"ADMIN-FEEDBACK",
					"ADMIN-EVALUATION-TEMPLATE",
					"API-DOCS",
					"ADMIN-BRANDING",
					"ADMIN-EVALUATION-TEMPLATE-SECTION",
					"ADMIN-CONTACT-MANAGEMENT",
					"ADMIN-ENTRY-TEMPLATES",
					"ADMIN-ENTRY-TYPES",
					"ADMIN-QUESTIONS",
					"ADMIN-REVIEW",
					"ADMIN-EVALUATION-TEMPLATE-CHECKLIST",
					"ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION",
					"ADMIN-ATTRIBUTE-MANAGEMENT",
					"ADMIN-ALERT-MANAGEMENT",
					"REPORTS-ALL",
					"ADMIN-EVALUATION-MANAGEMENT"
				];
				
				if (CoreService.userservice.userHasPermisson(usercontext, permissions, 'OR')) {
					var adminmenu = userMenu.getMenu().getComponent('menuAdminTools');
					if (adminmenu) {
						adminmenu.setHidden(false);
					}
				}				
				
				if (CoreService.userservice.userHasPermisson(usercontext, ['EVALUATIONS'])) {
					var evalmenu = userMenu.getMenu().getComponent('menuEvalTools');
					if (evalmenu) {
						userMenu.getMenu().getComponent('menuEvalTools').setHidden(false);
					}
				}					
				
				if (userMenu.initCallBack) {
					userMenu.initCallBack(usercontext);	
				}
		});
	},
	refreshMenu: function(customMenuItems) {
		var userMenu = this;
		if (customMenuItems) {
			userMenu.customMenuItems = customMenuItems;
		}
		userMenu.loadMenu();
	}
	
});

Ext.define('OSF.component.ChangeLogWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.ChangeLogWindow',
	
	title: 'Change History',
	modal: false,	
	maximizable: true,
	width: '40%',
	height: '70%',
	layout: 'fit',
	alwaysOnTop: true,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Refresh',
					iconCls: 'fa fa-lg fa-refresh icon-button-color-refresh',
					handler: function() {
						this.up('window').refresh();
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
	],
	listeners:
	{
		show: function()   
		{        
			this.removeCls("x-unselectable");    
		}
	},	
	initComponent: function () {
		this.callParent();
		
		var changeWindow = this;		

		changeWindow.details = Ext.create('Ext.panel.Panel', {
			bodyStyle: 'padding: 10px;',
			scrollable: true,
			tpl: new Ext.XTemplate(				
				'<tpl for=".">',
				'	<tpl if="changeTypeDescription">',
				'	<h3>{changeTypeDescription} by {createUser} on {[Ext.util.Format.date(values.createDts, "m/d/y H:i:s")]} </h3>',
				'	{entity} <tpl if="field">Field: {field}</tpl> <tpl if="comment"><br>{comment}</tpl><br>',									
				'	<tpl if="newValue"><br><b>New Value: </b><br>',
				'	<b>{newValue}</b><br><br></tpl>',
				'	<tpl if="oldValue"><b>Old Value:</b> <br>',
				'	<span style="color: gray">{oldValue}</span></tpl>',
				'	<hr></tpl>',				
				'</tpl>'
			)			
		});
		changeWindow.add(changeWindow.details);		
	},
	load: function(loadInfo) {
		var changeWindow = this;

		changeWindow.setLoading(true);
		changeWindow.loadInfo  = loadInfo;
		
		Ext.Ajax.request({
			url: 'api/v1/resource/changelogs/' + loadInfo.entity + '/' + loadInfo.entityId + '?includeChildren=' + (loadInfo.includeChildren ? true : false),
			callback: function() {
				changeWindow.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				if (loadInfo.addtionalLoad) {
					loadInfo.addtionalLoad(data, changeWindow);
				} else {
					changeWindow.updateData(data);
				}
			}
		});
	},
	updateData: function(data) {
		var changeWindow = this;
		
		if (data && data.length > 0){
			changeWindow.details.update(data);
		} else {
			changeWindow.details.update("No Change History");
		}					
	},
	refresh: function() {
		var changeWindow = this;
		changeWindow.load(changeWindow.loadInfo);
	}
	
});