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
	queryMode: 'local',
	labelAlign: 'top',
	store: {
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/lookuptypes/SecurityMarkingType'			
		}
	},	
	initComponent: function() {
		var me = this;	
		me.callParent();
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

								data.push({
									dataSensitivity: item.dataSensitivity,
									dataSensitivityDesc: found.description
								});
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

								data.push({
									dataSource: item.dataSource,
									dataSourceDesc: found.description
								});
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
				}
			});			
		});		
		
	}	
	
});


Ext.define('OSF.component.UserMenu', {
    extend: 'Ext.button.Button',
	alias: 'osf.widget.UserMenu',
		
	scale: 'large',
	ui: 'default',	
	minWidth: 170,
	maxWidth: 250,
	initCallBack: null,
	showUserTools: true,
	showAdminTools: true,
	showHelp: true,
	menu: {},
	helpWin: Ext.create('OSF.component.HelpWindow', {}),
	feedbackWin: Ext.create('OSF.component.FeedbackWindow',{}),
	
	initComponent: function() {
		this.callParent();
		
		var userMenu = this;	
		
		var menu = userMenu.getMenu();
				
		var menuItems = [];
		
		menuItems.push({
			text: 'Home',
			iconCls: 'fa fa-2x fa-home icon-button-color-default',
			href: 'index.jsp'			
		});
		
		if (userMenu.showAdminTools) {
			menuItems.push({
				text: 'Admin Tools',
				itemId: 'menuAdminTools',
				iconCls: 'fa fa-2x fa-gear icon-button-color-default',
				hidden: true,
				href: 'AdminTool.action'	
			});
		}		
		
		if (userMenu.showUserTools) {
			menuItems.push({
				text: 'User Tools',
				iconCls: 'fa fa-2x fa-user icon-button-color-default',
				href: 'UserTool.action'		
			});
		}	
		menuItems.push({
			xtype: 'menuseparator'		
		});	
		
		if (userMenu.showHelp) {
			menuItems.push({
				text: '<b>Help</b>',
				iconCls: 'fa fa-2x fa-question-circle icon-button-color-default',
				handler: function() {
					userMenu.helpWin.show();
				}			
			});		
		}
		
		menuItems.push({
			text: '<b>Feedback / issues</b>',
			iconCls: 'fa fa-2x fa-commenting icon-button-color-default',
			handler: function() {
				userMenu.feedbackWin.show();
			}		
		});
		
		menuItems.push({
			xtype: 'menuseparator'		
		});		
		
		menuItems.push({
			text: 'Logout',
			iconCls: 'fa fa-2x fa-sign-out icon-button-color-default',
			href: 'Login.action?Logout'			
		});
		
		menu.add(menuItems);
		
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
					userMenu.getMenu().getComponent('menuAdminTools').setHidden(false);
				}				
				
				if (userMenu.initCallBack) {
					userMenu.initCallBack(usercontext);	
				}
		});
	}	
	
});