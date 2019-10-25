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

/* global Ext, CoreUtil */

Ext.define('OSF.landing.DefaultSearch', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultsearch',

	width: '100%',
	layout: 'hbox',
	id: 'defaultSearch',
	listeners: {
		resize: function (mainPanel, width, height, oldWidth, oldHeight, eOpts) {
			var searchBar = mainPanel.queryById('searchBar');
			if (width < 1024) {
				searchBar.setWidth('100%');
			} else {
				searchBar.setWidth('78%');
			}
		},
		afterrender: function () {

			// close entry type button on scroll
			var entryTypeButton = Ext.ComponentQuery.query('[itemId=entryType]')[0];
			this.up().up().body.on('scroll', function (e) {

				if (!entryTypeButton.canCloseMenu) {
					entryTypeButton.click();
				}
			});
		}
	},
	items: [
		{
			flex: 1
		},
		{
			xtype: 'panel',
			itemId: 'searchBar',
			width: '75%',
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			performSearch: function (query) {

				//save search options
				var searchOptions = {
					canUseOrganizationsInSearch: true,
					canUseNameInSearch: true,
					canUseDescriptionInSearch: true,
					canUseTagsInSearch: true,
					canUseAttributesInSearch: true
				}

				let checkboxgroup = Ext.getCmp('defaultSearch').queryById('searchOptionsGroup');
				let values = checkboxgroup.getValue();

				if (!values.canUseOrganizationsInSearch) {
					searchOptions.canUseOrganizationsInSearch = false;
				}
				if (!values.canUseNameInSearch) {
					searchOptions.canUseNameInSearch = false;
				}
				if (!values.canUseDescriptionInSearch) {
					searchOptions.canUseDescriptionInSearch = false;
				}
				if (!values.canUseTagsInSearch) {
					searchOptions.canUseTagsInSearch = false;
				}
				if (!values.canUseAttributesInSearch) {
					searchOptions.canUseAttributesInSearch = false;
				}

				if (!query || Ext.isEmpty(query)) {
					query = '*';
				}

				var containerPanel = this;
				var entryTypeCB = containerPanel.getComponent('entryType');

				var searchElements = [
					{
						"searchType": 'INDEX',
						"field": null,
						"value": query,
						"mergeCondition": "AND"
					}
				];

				Ext.Array.forEach(entryTypeCB.selectedItems, function (entryType) {
					searchElements.push({
						"searchType": "COMPONENT",
						"field": 'componentType',
						"value": entryType,
						"caseInsensitive": false,
						"numberOperation": "EQUALS",
						"stringOperation": "EQUALS",
						"mergeCondition": "OR"
					});
				});

				var searchRequest;
				if (entryTypeCB.selectedItems.length > 0) {
					var searchObj = {
						"sortField": null,
						"sortDirection": "ASC",
						"startOffset": 0,
						"max": 2147483647,
						"searchElements": searchElements
					};

					searchRequest = {
						type: 'Advance',
						query: searchObj
					};
				} else {
					searchRequest = {
						type: 'SIMPLE',
						query: CoreUtil.searchQueryAdjustment(query)
					};
				}

				// Save the searchOptions in the searchRequest so they can be retrieved from
		 		// session storage instead of only being retrievable by the api endpoint in the request.
		 		searchRequest.searchOptions = searchOptions;
 
		 		Ext.Ajax.request({
					url: "api/v1/resource/searchoptions/user",
					jsonData: searchOptions,
					method: 'PUT',
					success: function (response, opt) {
						CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));

						window.location.href = 'searchResults.jsp';
					},
					failure: function (response, opt) {
						console.log(response);
						Ext.toast('Failed to apply the search options.', '', 'tr');
					}
				});
			},
			items: [
				{
					xtype: 'button',
					text: 'Search Options',
					itemId: 'entryType',
					margin: 0,
					enableToggle: true,
					iconCls: 'fa fa-chevron-down',
					cls: 'entry-type-search-button',
					iconAlign: 'right',
					style: 'overflow: visible;',
					canCloseMenu: true,
					selectedItems: [],
					setCheckedDisplay: function (treeStore) {

						var itemsSelected = 0;

						Ext.Array.forEach(treeStore.getData().items, function (item) {
							if (item.getData().checked) {
								itemsSelected += 1;
							}
						});
					},
					setItemsSelected: function (treeStore) {

						var selectedItems = [];

						Ext.Array.forEach(treeStore.getData().items, function (item) {
							if (item.getData().checked) {
								selectedItems.push(item.getData().componentType.componentType);
							}
						});

						this.selectedItems = selectedItems;
					},
					listeners: {
						toggle: function (button, isPressed) {
							if (isPressed) {
								button.setIconCls('fa fa-chevron-up');
								this.canCloseMenu = false;
							}
							else {
								button.setIconCls('fa fa-chevron-down');
								this.canCloseMenu = true;
							}
						}
					},
					menu: {
						width: 400,
						itemId: 'searchOptionMenu',
						listeners: {
							beforehide: function () {
								return this.up().canCloseMenu;
							}
						},
						items: [
							{
								xtype: 'label',
								text: 'Search Options',
								style: {
									'font-weight': 'bold'
								}
							},
							{
								xtype: 'checkboxgroup',
								itemId: 'searchOptionsGroup',
								columns: 1,
								vertical: true,
								allowBlank: false,
								items: [
									{
										xtype: 'checkbox',
										boxLabel: 'Organizations',
										id: 'organizationsCheckbox',
										name: 'canUseOrganizationsInSearch',
										inputValue: true,
										uncheckedValue: false
									},
									{
										xtype: 'checkbox',
										boxLabel: 'Component Names',
										id: 'componentNameCheckbox',
										name: 'canUseNameInSearch',
										inputValue: true,
										uncheckedValue: false
									},
									{
										xtype: 'checkbox',
										boxLabel: 'Component Descriptions',
										id: 'componentDescriptionCheckbox',
										name: 'canUseDescriptionInSearch',
										inputValue: true,
										uncheckedValue: false
									},
									{
										xtype: 'checkbox',
										boxLabel: 'Component Tags',
										id: 'componentTagsCheckbox',
										name: 'canUseTagsInSearch',
										inputValue: true,
										uncheckedValue: false
									},
									{
										xtype: 'checkbox',
										boxLabel: 'Component Vitals',
										id: 'componentAttributesCheckbox',
										name: 'canUseAttributesInSearch',
										inputValue: true,
										uncheckedValue: false
									}
								],
								listeners: {
									added: function () {
										Ext.Ajax.request({
											url: 'api/v1/resource/searchoptions/user',
											method: 'GET',
											success: function (response, opts) {
												var data = Ext.decode(response.responseText);

												if (Ext.getCmp('organizationsCheckbox')) {
													Ext.getCmp('organizationsCheckbox').setValue(data.canUseOrganizationsInSearch);
												}
												if (Ext.getCmp('componentNameCheckbox')) {
													Ext.getCmp('componentNameCheckbox').setValue(data.canUseNameInSearch);
												}
												if (Ext.getCmp('componentDescriptionCheckbox')) {
													Ext.getCmp('componentDescriptionCheckbox').setValue(data.canUseDescriptionInSearch);
												}
												if (Ext.getCmp('componentTagsCheckbox')) {
													Ext.getCmp('componentTagsCheckbox').setValue(data.canUseTagsInSearch);
												}
												if (Ext.getCmp('componentAttributesCheckbox')) {
													Ext.getCmp('componentAttributesCheckbox').setValue(data.canUseAttributesInSearch);
												};
											},
											failure: function (response, opts) {
												Ext.getCmp('organizationsCheckbox').setValue(true);
												Ext.getCmp('componentNameCheckbox').setValue(true);
												Ext.getCmp('componentDescriptionCheckbox').setValue(true);
												Ext.getCmp('componentTagsCheckbox').setValue(true);
												Ext.getCmp('componentAttributesCheckbox').setValue(true);
											}
										});
									},
									change: function (checkBox, newVal, oldVal) {
										var searchOptions = {
											canUseOrganizationsInSearch: true,
											canUseNameInSearch: true,
											canUseDescriptionInSearch: true,
											canUseTagsInSearch: true,
											canUseAttributesInSearch: true
										}
										values = checkBox.getValue();
										var isMyObjectEmpty = !Object.keys(values).length;
										var searchButton = Ext.getCmp('defaultSearch').queryById('searchButton');
										if (isMyObjectEmpty) {
											searchButton.disable();
											Ext.Msg.show({
												title: 'Search Options',
												message: 'No search options are selected, please select at least one option.',
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.ERROR,
												fn: function (btn) {
												}
											});
										} else {
											searchButton.enable();
										}

										if (!values.canUseOrganizationsInSearch) {
											searchOptions.canUseOrganizationsInSearch = false;
										}
										if (!values.canUseNameInSearch) {
											searchOptions.canUseNameInSearch = false;
										}
										if (!values.canUseDescriptionInSearch) {
											searchOptions.canUseDescriptionInSearch = false;
										}
										if (!values.canUseTagsInSearch) {
											searchOptions.canUseTagsInSearch = false;
										}
										if (!values.canUseAttributesInSearch) {
											searchOptions.canUseAttributesInSearch = false;
										}
									}
								}
							},
							{
								xtype: 'label',
								text: 'Entry Types',
								style: {
									'font-weight': 'bold'
								}
							},
							{
								xtype: 'treepanel',
								cls: 'entry-type-tree-panel-menu',
								label: 'something',
								maxHeight: 500,
								scrollable: true,
								rootVisible: false,
								checkPropagation: 'both',
								listeners: {
									beforeitemcollapse: function () {
										return false;
									},
									itemclick: function (view, record, element, index, e, opts) {
										if (e.target.className.indexOf('checkbox') === -1) {
											if (record.get('checked')) {
												record.set('checked', false);
											} else {
												record.set('checked', true);
											}
										}

										var entryTypeButton = this.up('[itemId=entryType]');
										entryTypeButton.setCheckedDisplay(this.getStore());
										entryTypeButton.setItemsSelected(this.getStore());

									}
								},
								store: {
									type: 'tree',
									fields: ['componentType'],
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/componenttypes/nested',
										extraParams: {
											all: true
										}
									},
									listeners: {
										load: function (self, records) {

											var setChildrenLayout = function (children) {
												if (children.length === 0) {
													return;
												}

												Ext.Array.forEach(children, function (child) {

													if (child.childNodes.length === 0) {
														child.data.leaf = true;
														child.triggerUIUpdate();
													}
													child.expand();
													child.data.checked = false;
													child.data.text = child.data.componentType.label;
													setChildrenLayout(child.childNodes);
												});
											};

											setChildrenLayout(records);
										}
									}
								}
							}
						]
					}
				},
				{
					xtype: 'combobox',
					itemId: 'searchText',
					flex: 1,
					fieldCls: 'home-search-field-new',
					emptyText: 'Search',
					queryMode: 'remote',
					hideTrigger: true,
					valueField: 'query',
					displayField: 'name',
					autoSelect: false,
					style: 'border-left: none;',
					maxLength: 200,
					enforceMaxLength: true,
					store: {
						autoLoad: false,
						proxy: {
							type: 'ajax',
							url: 'api/v1/service/search/suggestions'
						},
						listeners: {
							beforeload: function (store, operation, opts) {
								store.getProxy().extraParams = {
									componentType: CoreUtil.tempComponentType ? CoreUtil.tempComponentType : null
								};
							}
						}
					},
					listeners: {

						expand: function (field, opts) {

							field.getPicker().clearHighlight();
						},

						specialkey: function (field, e) {

							var value = this.getValue();

							if (!Ext.isEmpty(value)) {

								switch (e.getKey()) {

									case e.ENTER:
										var query = value;
										field.up('panel').performSearch(query);
										break;

									case e.HOME:
										field.setValue(field.lastQuery);
										field.selectText(0, 0);
										field.expand();
										break;

									case e.END:
										field.setValue(field.lastQuery);
										field.selectText(field.getValue().length, field.getValue().length);
										field.expand();
										break;
								}
							}
						}
					}
				},
				{
					xtype: 'button',
					tooltip: 'Keyword Search',
					iconCls: 'fa fa-2x fa-search icon-search-adjustment',
					style: 'border-radius: 0px 3px 3px 0px;',
					itemId: 'searchButton',
					width: 50,
					handler: function () {
						var query = this.up('panel').getComponent('searchText').getValue();
						this.up('panel').performSearch(query);
					}
				}
			]
		},
		{
			flex: 1
		}
	],

	initComponent: function () {
		this.callParent();
	}

});
