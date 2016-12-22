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

/* global Ext, CoreUtil */

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

Ext.define('OSF.component.UserMenu', {
    extend: 'Ext.button.Button',
	alias: 'osf.widget.UserMenu',
		
	scale: 'large',
	ui: 'default',	
	minWidth: 150,
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
			iconCls: 'fa fa-2x fa-home',
			href: 'index.jsp'			
		});
		
		if (userMenu.showAdminTools) {
			menuItems.push({
				text: 'Admin Tools',
				itemId: 'menuAdminTools',
				iconCls: 'fa fa-2x fa-gear',
				hidden: true,
				href: 'AdminTool.action'	
			});
		}		
		
		if (userMenu.showUserTools) {
			menuItems.push({
				text: 'User Tools',
				iconCls: 'fa fa-2x fa-user',
				href: 'UserTool.action'		
			});
		}	
		menuItems.push({
			xtype: 'menuseparator'		
		});	
		
		if (userMenu.showHelp) {
			menuItems.push({
				text: '<b>Help</b>',
				iconCls: 'fa fa-2x fa-question-circle',
				handler: function() {
					userMenu.helpWin.show();
				}			
			});		
		}
		
		menuItems.push({
			text: '<b>Feedback / issues</b>',
			iconCls: 'fa fa-2x fa-commenting',
			handler: function() {
				userMenu.feedbackWin.show();
			}		
		});
		
		menuItems.push({
			xtype: 'menuseparator'		
		});		
		
		menuItems.push({
			text: 'Logout',
			iconCls: 'fa fa-2x fa-sign-out',
			href: 'Login.action?Logout'			
		});
		
		menu.add(menuItems);
		
		menu.on('beforerender', function () {
			this.setWidth(this.up('button').getWidth());
		});
		
		CoreService.usersevice.getCurrentUser().then(function(response, opts){
				var usercontext = Ext.decode(response.responseText);
				
				var userMenuText = usercontext.username;
				if (usercontext.firstName && usercontext.lastName)
				{
					userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
				}
				userMenu.setText(userMenuText);	
				
				if (usercontext.admin) {
					userMenu.getMenu().getComponent('menuAdminTools').setHidden(false);
				}				
				
				if (userMenu.initCallBack) {
					userMenu.initCallBack(usercontext);	
				}
		});
	}	
	
});