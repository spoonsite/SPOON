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
	alias: 'widget.StandardComboBox',

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

	initComponent: function () {
		var me = this;

		if (me.storeConfig)
		{
			var cbStore = CoreUtil.lookupStore(me.storeConfig);
			me.store = cbStore;
		}

		this.callParent();
	}

});

/***
 * Creates a combo box that allows selection of multiple users chosen via typing
 * in part of their names or choosing them from a list. 
 * Example usage:
 * 
 * var selectionCombo = Ext.create('OSF.component.UserMultiSelectComboBox', {
 *						addAll: true, //optionally adds an All option to end of list
 *						width: 375,
 *						margin: '50',
 *					});
 */
Ext.define('OSF.component.UserMultiSelectComboBox', {
	extend: 'Ext.form.field.Tag',
	alias: 'widget.UserMultiSelectComboBox',

	labelAlign: 'top',	
	labelSeparator: '',
	valueField: 'code',
	displayField: 'description',	
	typeAhead: true,
	anyMatch: true,
	editable: true,
	forceSelection: true,
	
	queryMode: 'remote',
	addAll: false,	
	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/userprofiles/lookup'
		}
	},	
	initComponent: function () {
		
		this.callParent();
		var selectBox = this;
		
		selectBox.getStore().on('load', function (store, records, opts) {
			if (selectBox.addAll) {
				store.add({
					code: null,
					description: 'All'
				});
			}
		});	
		
		if (selectBox.queryMode === 'local') {
			selectBox.getStore().load();
		}
		
	}
});

Ext.define('OSF.component.UserSingleSelectComboBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.UserSingleSelectComboBox',

	labelAlign: 'top',	
	labelSeparator: '',
	valueField: 'code',
	displayField: 'description',	
	typeAhead: true,
	anyMatch: true,
	editable: true,
	forceSelection: true,
	
	queryMode: 'remote',
	addAll: false,
	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/userprofiles/lookup'
		}
	},
	initComponent: function () {
		
		this.callParent();
		var selectBox = this;
		
		selectBox.getStore().on('load', function (store, records, opts) {
			if (selectBox.addAll) {
				store.add({
					code: null,
					description: 'All'
				});
			}
		});	
		
		if (selectBox.queryMode === 'local') {
			selectBox.getStore().load();
		}		
	}
});

Ext.define('OSF.component.RoleGroupMultiSelectComboBox', {
	extend: 'Ext.form.field.Tag',
	alias: 'widget.RoleGroupMultiSelectComboBox',

	labelAlign: 'top',	
	labelSeparator: '',
	valueField: 'code',
	displayField: 'description',	
	typeAhead: true,
	anyMatch: true,
	editable: true,
	forceSelection: true,

	queryMode: 'local',
	addAll: false,
	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/securityroles/lookup'
		}
	},	
	initComponent: function () {
		
		this.callParent();
		var selectBox = this;
				
		selectBox.getStore().on('load', function (store, records, opts) {
			if (selectBox.addAll) {
				store.add({
					code: null,
					description: 'All'
				});
			}
		});	
		
		if (selectBox.queryMode === 'local') {
			selectBox.getStore().load();
		}		
	}
});

Ext.define('OSF.component.ActiveOnMultiSelectComboBox', {
	extend: 'OSF.component.RoleGroupMultiSelectComboBox',
	alias: 'widget.ActiveOnMultiCombo',

	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/lookuptypes/EntityEventType'
		}
	}
});

Ext.define('OSF.component.EntryTypeMultiSelect', {
	extend: 'OSF.component.RoleGroupMultiSelectComboBox',
	alias: 'widget.EntryTypeMultiSelect',

	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/componenttypes/lookup'
		}
	}
});

Ext.define('OSF.component.UserCustomEmailCombo', {
	extend: 'Ext.form.field.Tag',
	alias: 'widget.UserCustomEmailCombo',

	displayField: 'email',
	valueField: 'email',
	queryMode: 'local',
	createNewOnEnter: true,
	width: '100%',
	store: {
		autoload: true,
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/userprofiles'
		},
		listeners: {
			load: function (store, records) {
				
				store.setData(records[0].getData().data);
			}
		}
	},
	initComponent: function () {
		this.callParent();
		this.getStore().load();
	}
});

Ext.define('OSF.component.RoleGroupSingleSelectComboBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.RoleGroupSingleSelectComboBox',

	labelAlign: 'top',	
	labelSeparator: '',
	valueField: 'code',
	displayField: 'description',	
	typeAhead: true,
	anyMatch: true,
	editable: true,
	forceSelection: true,
	
	queryMode: 'local',
	addAll: false,
	store: {
		proxy: {
			type: 'ajax',
			url: 'api/v1/resource/securityroles/lookup'
		}
	},	
	initComponent: function () {
		
		this.callParent();
		var selectBox = this;
				
		selectBox.getStore().on('load', function (store, records, opts) {
			if (selectBox.addAll) {
				store.add({
					code: null,
					description: 'All'
				});
			}
		});	
		
		if (selectBox.queryMode === 'local') {
			selectBox.getStore().load();
		}		
	}
});

Ext.define('OSF.component.SecurityComboBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.SecurityComboBox',

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
			load: function (store, records, opts) {
				if (store.addSelect) {
					store.add({
						code: null,
						description: 'Select'
					});
				}
			}
		}
	},
	initComponent: function () {
		var combo = this;
		combo.callParent();
		combo.getStore().addSelect = combo.addSelect;

		//check branding to see it should show

		CoreService.brandingservice.getCurrentBranding().then(function (branding) {
			if (branding.allowSecurityMarkingsFlg) {
				combo.setHidden(false);
			}
		});

	}

});

Ext.define('OSF.component.DataSensitivityComboBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.DataSensitivityComboBox',

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
	initComponent: function () {
		var combo = this;
		combo.callParent();

		CoreService.userservice.getCurrentUser().then(function (user) {
			var data = [];
			Ext.Ajax.request({
				url: 'api/v1/resource/lookuptypes/DataSensitivity',
				success: function (response, opts) {
					var lookups = Ext.decode(response.responseText);

					Ext.Array.each(user.roles, function (securityRole) {
						Ext.Array.each(securityRole.dataSecurity, function (item) {
							if (item.dataSensitivity) {
								var found = Ext.Array.findBy(lookups, function (lookup) {
									if (item.dataSensitivity === lookup.code) {
										return true;
									}
								});
								var add = true;
								Ext.Array.each(data, function (dataItem) {
									if (dataItem.dataSensitivity === item.dataSensitivity) {
										add = false;
									}
								});

								if (add) {
									data.push({
										dataSensitivity: item.dataSensitivity,
										dataSensitivityDesc: found ? found.description : item.dataSensitivity
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
	alias: 'widget.DataSourceComboBox',

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
	initComponent: function () {
		var combo = this;
		combo.callParent();

		CoreService.userservice.getCurrentUser().then(function (user) {
			var data = [];

			Ext.Ajax.request({
				url: 'api/v1/resource/lookuptypes/DataSource',
				success: function (response, opts) {
					var lookups = Ext.decode(response.responseText);

					Ext.Array.each(user.roles, function (securityRole) {
						Ext.Array.each(securityRole.dataSecurity, function (item) {
							if (item.dataSource) {
								var found = Ext.Array.findBy(lookups, function (lookup) {
									if (item.dataSource === lookup.code) {
										return true;
									}
								});

								var add = true;
								Ext.Array.each(data, function (dataItem) {
									if (dataItem.dataSource === item.dataSource) {
										add = false;
									}
								});

								if (add) {
									data.push({
										dataSource: item.dataSource,
										dataSourceDesc: found ? found.description : item.dataSource
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

Ext.define('OSF.component.fileFieldMaxLabel', {
	alias: 'widget.fileFieldMaxLabel',
	extend: 'Ext.form.field.File',

	resourceLabel: 'Upload Resource',
	checkFileLimit: true,
	displayFileLimit: true,

	initComponent: function () {
		var fileField = this;
		fileField.callParent();

		var getFileSizeGB = function (fileSize) {
			return (fileSize / 1000000000).toFixed(2).replace(/\.0+0$/, '');
		};

		//	Handles file size limits
		var handleFileLimit = function (field, value, opts) {
			var el = field.fileInputEl.dom;
			var file = el.files[0];
			if (file && fileField.checkFileLimit) {

				// 	create error string
				var errorMessage = '<span class="fileUploadError" style="color: rgb(255,55,55); font-weight: bold"><br />'
						+ file.name + ' (' + getFileSizeGB(file.size) + ' GB) exceeded size limit'
						+ (!fileField.displayFileLimit ? ' of ' + getFileSizeGB(fileField.getMaxFileSize()) + ' GB' : '') + '</span>';

				field.setFieldLabel(field.getFieldLabel().replace(/<span class="fileUploadError".*<\/span>/, ''));

				if (el.files && el.files.length > 0) {
					if (file.size > this.getMaxFileSize()) {
						Ext.defer(function () {
							field.reset();
							field.markInvalid('File exceeds size limit.');
							field.setFieldLabel(field.getFieldLabel() + errorMessage);
							field.validate();
						}, 250);
					}
				}
			}
		};

		//	Request the max post size, then set the fieldLabel accordingly
		Ext.Ajax.request({
			url: 'api/v1/service/application/configproperties/max.post.size',
			success: function (response) {
				var sizeMB = 1048576;
				var maxFileSize = Number(Ext.decode(response.responseText).description) * sizeMB;
				fileField.getMaxFileSize = function () {
					return maxFileSize;
				};

				var fileLimitDisplay = fileField.displayFileLimit ? ('<br /> (Limit of ' + getFileSizeGB(maxFileSize) + ' GB)') : '';
				var requiredDisplay = !fileField.allowBlank ? '<span class="field-required" />' : '';
				fileField.setFieldLabel(fileField.resourceLabel + fileLimitDisplay + requiredDisplay);
			}
		});

		// add change listener
		fileField.addListener('change', handleFileLimit);
	}
});

Ext.define('OSF.component.UserMenu', {
	extend: 'Ext.button.Button',
	alias: 'widget.osf-UserMenu',

	scale: 'large',
	ui: 'default',
	maxWidth: 300,
	initCallBack: null,
	showUserTools: true,
	showAdminTools: true,
	showEvaluatorTools: true,
	showHelp: true,
	showFeedback: true,
	showSupportMedia: true,
	showFAQ: true,
	showLogout: true,
	menu: {
		minWidth: 300
	},
	helpWin: Ext.create('OSF.component.HelpWindow', {}),
	feedbackWin: Ext.create('OSF.component.FeedbackWindow', {}),
	customMenuItems: [],
	initComponent: function () {
		this.callParent();

		var userMenu = this;

		var menu = userMenu.getMenu();

		userMenu.loadMenu = function () {
			menu.removeAll();

			var menuItems = [];

			menuItems.push({
				text: 'Home',
				iconCls: 'fa fa-2x fa-home icon-button-color-default',
				href: 'Landing.action',
				handler: function () {
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
					handler: function () {
						window.location.href = 'AdminTool.action';
					}
				});
			}

			if (userMenu.showUserTools) {
				menuItems.push({
					text: 'User Tools',
					iconCls: 'fa fa-2x fa-user icon-button-color-default',
					href: 'UserTool.action',
					handler: function () {
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
					handler: function () {
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
					handler: function () {
						userMenu.helpWin.show();
					}
				});
			}
			
			if (userMenu.showSupportMedia) {
				menuItems.push({
					text: 'Tutorials',
					itemId: 'menuTutorials',
					iconCls: 'fa fa-2x fa-tv icon-button-color-default',
					hidden: true,
					handler: function() {
						var supportWin = Ext.create('OSF.component.SupportMediaWindow', {							
						});
						supportWin.show();
					}		
				});
			}
			
			if (userMenu.showFAQ) {
				menuItems.push({
					text: 'FAQ',
					itemId: 'menuFAQ',
					tooltip: 'Frequently Asked Questions',
					iconCls: 'fa fa-2x fa-info-circle icon-button-color-default',
					hidden: true,
					handler: function () {
						var faqWin = Ext.create('OSF.component.FaqWindow', {							
						});
						faqWin.show();
					}
				});
			}			

			if (userMenu.showFeedback) {
				menuItems.push({
					text: '<b>Contact Us</b>',
					iconCls: 'fa fa-2x fa-commenting icon-button-color-default',
					handler: function () {
						
						var contactWindow = Ext.create('OSF.component.FeedbackWindow', {

							isLoggedIn: true,
							fieldType: 'displayfield'
						});
						contactWindow.show();
					}
				});
			}
			
			menuItems.push({
				text: 'Switch To Mobile',
				itemId: 'menuSwitchToMobile',
				hidden: true,
				iconCls: 'fa fa-2x fa-mobile icon-button-color-default',
				href: 'mobile/index.html',
				handler: function () {
					window.location.href = 'mobile/index.html';
				}
			});			
			

			if (userMenu.showLogout){
				menuItems.push({
					xtype: 'menuseparator',
					itemId: 'LogoutMenuItemSeparator'
				});

				menuItems.push({
					text: 'Logout',
					itemId: 'LogoutMenuItem',
					iconCls: 'fa fa-2x fa-sign-out icon-button-color-default',
					href: 'Login.action?Logout',
					handler: function () {
						window.location.href = 'Login.action?Logout';
					}
				});
			}
		
			menu.add(menuItems);
		};
		userMenu.loadMenu();


		menu.on('beforerender', function () {
			this.setWidth(this.up('button').getWidth());
		});

		
		//check to for support media
		if (userMenu.showSupportMedia || userMenu.showFAQ) {		
			CoreService.brandingservice.getCurrentBranding().then(function(branding){
				if (branding.showSupportMedia) {
					var menuTutorials = userMenu.getMenu().queryById('menuTutorials');
					if (menuTutorials) {
						menuTutorials.setHidden(false);
					}
				}
				if (branding.showFAQ) {
					var menuFAQ = userMenu.getMenu().queryById('menuFAQ');
					if (menuFAQ) {
						menuFAQ.setHidden(false);
					}
				}
				if (branding.showLinkToMobile) {
					var menuMobile = userMenu.getMenu().queryById('menuSwitchToMobile');
					if (Ext.os.is.Tablet || Ext.os.is.Phone) {
						menuMobile.setHidden(false);
					}
				}
			});
		}
		
		CoreService.userservice.getCurrentUser().then(function (usercontext) {

			var userMenuText = usercontext.username;
			if (usercontext.firstName && usercontext.lastName)
			{
				userMenuText = usercontext.firstName + ' ' + usercontext.lastName;
			}
			userMenu.setText(userMenuText);
			if (usercontext.username === "ANONYMOUS") {
				userMenu.getMenu().getComponent('LogoutMenuItem').setHidden(true);
				userMenu.getMenu().getComponent('LogoutMenuItemSeparator').setHidden(true);
				userMenu.setText("Menu");
			}

			var permissions = [
				"ADMIN-ALERTS-PAGE",
				"ADMIN-API-PAGE",
				"ADMIN-ATTRIBUTE-PAGE",
				"ADMIN-BRANDING-PAGE",
				"ADMIN-CONTACTS-PAGE",
				"ADMIN-ENTRIES-PAGE",
				"ADMIN-ENTRYTEMPLATES-PAGE",
				"ADMIN-ENTRYTYPE-PAGE",
				"ADMIN-EVAL-TEMPLATES-PAGE",
				"ADMIN-EVAL-CHECKLIST-QUESTIONS-PAGE",
				"ADMIN-EVAL-CHECKLIST-TEMPLATES-PAGE",
				"ADMIN-EVAL-SECTION-PAGE",
				"ADMIN-EVAL-PAGE",
				"ADMIN-FAQ-PAGE",
				"ADMIN-FEEDBACK-PAGE",
				"ADMIN-HIGHLIGHTS-PAGE",
				"ADMIN-IMPORT-PAGE",
				"ADMIN-INTEGRATION-PAGE",
				"ADMIN-JOBS-PAGE",
				"ADMIN-LOOKUPS-PAGE",
				"ADMIN-SUPPORTMEDIA-PAGE",
				"ADMIN-MEDIA-PAGE",
				"ADMIN-MESSAGES-PAGE",
				"ADMIN-ORGANIZATION-PAGE",
				"ADMIN-USERPROFILES-PAGE",
				"ADMIN-QUESTIONS-PAGE",
				"ADMIN-RELATIONSHIPS-PAGE",
				"ADMIN-REVIEW-PAGE",
				"ADMIN-ROLES-PAGE",
				"ADMIN-SEARCHES-PAGE",
				"ADMIN-SECURITY-PAGE",
				"ADMIN-PARTIAL-SUBMISSIONS-PAGE",
				"ADMIN-SUBMISSION-FORM-SANDBOX-PAGE", // not defined in SecurityPermission.java
				"ADMIN-SUBMISSION-FORM-TEMPLATE-PAGE",
				"ADMIN-SYSTEM-PAGE",
				"ADMIN-SYSTEM-ARCHIVES-PAGE",
				"ADMIN-TAGS-PAGE",
				"ADMIN-TRACKING-PAGE",
				"ADMIN-USER-MANAGEMENT-PAGE",
				"ADMIN-WATCHES-PAGE"

			];

			if (CoreService.userservice.userHasPermission(usercontext, permissions, 'OR')) {
				var adminmenu = userMenu.getMenu().getComponent('menuAdminTools');
				if (adminmenu) {
					adminmenu.setHidden(false);
				}
			}

			if (CoreService.userservice.userHasPermission(usercontext, ['EVAL-PAGE'])) {
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
	refreshMenu: function (customMenuItems) {
		var userMenu = this;
		if (customMenuItems) {
			userMenu.customMenuItems = customMenuItems;
		}
		userMenu.loadMenu();
	}

});

Ext.define('OSF.component.ChangeLogWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.ChangeLogWindow',

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
					handler: function () {
						this.up('window').refresh();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',
					iconCls: 'fa fa-lg fa-close icon-button-color-warning',
					handler: function () {
						this.up('window').close();
					}
				}
			]
		}
	],
	listeners: {
		show: function () {
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
	load: function (loadInfo) {
		var changeWindow = this;

		changeWindow.setLoading(true);
		changeWindow.loadInfo = loadInfo;

		Ext.Ajax.request({
			url: 'api/v1/resource/changelogs/' + loadInfo.entity + '/' + loadInfo.entityId + '?includeChildren=' + (loadInfo.includeChildren ? true : false),
			callback: function () {
				changeWindow.setLoading(false);
			},
			success: function (response, opts) {
				var data = Ext.decode(response.responseText);
				if (loadInfo.addtionalLoad) {
					loadInfo.addtionalLoad(data, changeWindow);
				} else {
					changeWindow.updateData(data);
				}
			}
		});
	},
	updateData: function (data) {
		var changeWindow = this;

		if (data && data.length > 0) {
			changeWindow.details.update(data);
		} else {
			changeWindow.details.update("No Change History");
		}
	},
	refresh: function () {
		var changeWindow = this;
		changeWindow.load(changeWindow.loadInfo);
	}

});

Ext.define('OSF.component.EntrySelect', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.entryselect',

	bodyStyle: 'padding: 10px',
	tpl: new Ext.XTemplate(
			'<tpl if="allSelected">{allSelectedMessage}</tpl>',
			'<tpl for="data">',
			'	<p>{name}</p>',
			'</tpl>',
			'<tpl if="more"><p>... (<b>{count}</b> selected)</p></tpl>'
			),
	buttonTooltip: null,
	allSelectedMessage: '<p>All Entries</p>',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'bottom',
			style: 'background: transparent;',
			items: [
				{
					xtype: 'button',
					itemId: 'selectBtn',
					text: 'Select Entries',
					iconCls: 'fa fa-lg fa-list',
					handler: function () {
						var selectPanel = this.up('panel');

						var selectWin = Ext.create('Ext.window.Window', {
							title: 'Select Entries',
							modal: true,
							alwaysOnTop: true,
							closeMode: 'destroy',
							layout: 'fit',
							height: '70%',
							width: '70%',
							items: [
								{
									xtype: 'panel',
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [
										{
											xtype: 'grid',
											itemId: 'poolGrid',
											title: 'Entry Not Included - <span class="alert-warning"> drag to add <i class="fa fa-lg fa-arrow-right"></i> </span>',
											columnLines: true,
											bufferedRenderer: false,
											width: '50%',
											margin: '0 5 0 0',
											store: {
												sorters: [
													new Ext.util.Sorter({
														property: 'description',
														direction: 'ASC'
													})
												]
											},
											selModel: {
												selType: 'rowmodel',
												mode: 'MULTI'
											},
											plugins: 'gridfilters',
											viewConfig: {
												plugins: {
													ptype: 'gridviewdragdrop',
													dragText: 'Drag and drop to Add to template'
												}
											},
											columns: [
												{text: 'Name', dataIndex: 'description', flex: 1},
												{text: 'Type', dataIndex: 'componentTypeLabel', width: 175,
													filter: {
														type: 'list'
													}
												}
											],
											dockedItems: [
												{
													xtype: 'textfield',
													dock: 'top',
													width: '100%',
													emptyText: 'Filter entries by name',
													listeners: {
														change: function (tb, newVal, oldVal, opts) {
															var grid = tb.up('grid');
															grid.getStore().filter([
																{
																	property: 'description',
																	value: tb.value
																}
															]);
														}
													}
												}
											]
										},
										{
											xtype: 'grid',
											itemId: 'selectedGrid',
											title: 'Entries In Report - <span class="alert-warning"><i class="fa fa-lg fa-arrow-left"></i> drag to remove </span>',
											columnLines: true,
											bufferedRenderer: false,
											width: '50%',
											selModel: {
												selType: 'rowmodel',
												mode: 'MULTI'
											},
											store: {
												sorters: [
													new Ext.util.Sorter({
														property: 'description',
														direction: 'ASC'
													})
												]
											},
											plugins: 'gridfilters',
											viewConfig: {
												plugins: {
													ptype: 'gridviewdragdrop',
													dragText: 'Drag and drop to delete from template'
												}
											},
											columns: [
												{text: 'Name', dataIndex: 'description', flex: 1},
												{text: 'Type', dataIndex: 'componentTypeLabel', width: 175,
													filter: {
														type: 'list'
													}
												}
											],
											dockedItems: [
												{
													xtype: 'textfield',
													dock: 'top',
													width: '100%',
													emptyText: 'Filter entries by name',
													listeners: {
														change: function (tb, newVal, oldVal, opts) {
															var grid = tb.up('grid');
															grid.getStore().filter([
																{
																	property: 'description',
																	value: tb.value
																}
															]);
														}
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
											xtype: 'button',
											text: 'Select Entries',
											scale: 'medium',
											iconCls: 'fa fa-lg fa-check icon-button-color-save',
											handler: function () {

												var selectedGrid = selectWin.queryById('selectedGrid');

												selectPanel.selectedIds = [];
												selectedGrid.getStore().each(function (record) {
													selectPanel.selectedIds.push({
														componentId: record.get('code'),
														name: record.get('description')
													});
												});
												selectPanel.refreshDisplay();

												selectWin.close();
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											xtype: 'button',
											text: 'Cancel',
											scale: 'medium',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function () {
												selectWin.close();
											}
										}
									]
								}
							]
						});
						selectWin.show();

						//load current selection
						selectWin.setLoading(true);
						Ext.Ajax.request({
							url: 'api/v1/resource/components/lookup',
							callback: function () {
								selectWin.setLoading(false);
							},
							success: function (response, opts) {
								var data = Ext.decode(response.responseText);

								var pool = [];
								var selected = [];
								Ext.Array.each(data, function (item) {
									var selectedRecord = false;

									Ext.Array.each(selectPanel.selectedIds, function (selectedItem) {
										if (selectedItem.componentId === item.code) {
											selectedRecord = true;
										}
									});

									if (selectedRecord) {
										selected.push(item);
									} else {
										pool.push(item);
									}
								});

								var selectedGrid = selectWin.queryById('selectedGrid');
								var poolGrid = selectWin.queryById('poolGrid');

								selectedGrid.getStore().loadData(selected);
								poolGrid.getStore().loadData(pool);

							}
						});

					}
				}
			]
		}
	],

	initComponent: function () {
		this.callParent();
		var selectPanel = this;

		selectPanel.selectedIds = [];
		selectPanel.refreshDisplay();
		if (selectPanel.buttonTooltip) {
			selectPanel.queryById('selectBtn').setTooltip(selectPanel.buttonTooltip);
		}
	},

	refreshDisplay: function () {
		var selectPanel = this;

		var display = [];
		var more = false;
		if (selectPanel.selectedIds.length > 10) {
			more = true;
		}
		for (var i = 0; i < selectPanel.selectedIds.length && i < 10; i++) {
			display.push(selectPanel.selectedIds[i]);
		}
		var allSelected = true;
		if (selectPanel.selectedIds.length > 0) {
			allSelected = false;
		}

		selectPanel.update({
			count: selectPanel.selectedIds.length,
			data: display,
			more: more,
			allSelected: allSelected,
			allSelectedMessage: selectPanel.allSelectedMessage
		});
		selectPanel.updateLayout(true, true);
	},

	getSelected: function () {
		var selectPanel = this;
		return selectPanel.selectedIds;
	},

	loadCurrentSelection: function (selectedIds) {
		var selectPanel = this;

		selectPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/components/lookup',
			callback: function () {
				selectPanel.setLoading(false);
			},
			success: function (response, opts) {
				var data = Ext.decode(response.responseText);

				selectPanel.selectedIds = [];
				Ext.Array.each(data, function (componentLookup) {

					Ext.Array.each(selectedIds, function (item) {
						if (componentLookup.code === item) {
							selectPanel.selectedIds.push({
								componentId: item,
								name: componentLookup.description
							});
						}
					});

				});

				selectPanel.refreshDisplay();
			}
		});

	}

});

