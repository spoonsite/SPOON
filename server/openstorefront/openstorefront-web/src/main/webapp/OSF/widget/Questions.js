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

Ext.define('OSF.widget.Questions', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.Questions',

	layout: 'fit',

	initComponent: function () {
		this.callParent();

		var questionPanel = this;

		questionPanel.grid = Ext.create('Ext.grid.Panel', {
			columnLines: true,
			store: {
				sorters: [{
						property: 'componentName',
						direction: 'ASC'
					}],
				autoLoad: false,
				fields: [
					{
						name: 'createDts',
						type: 'date',
						dateFormat: 'c'
					},
					{
						name: 'questionUpdateDts',
						type: 'date',
						dateFormat: 'c'
					}
				],
				proxy: {
					type: 'ajax',
					url: 'api/v1/resource/componentquestions/'
				}
			},
			columns: [
				{text: 'Entry', dataIndex: 'componentName', width: 275},
				{text: 'Question', dataIndex: 'question', flex: 1, minWidth: 200},
				{text: 'Post Date', dataIndex: 'createDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
				{text: 'Update Date', dataIndex: 'questionUpdateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'}
			],
			listeners: {
				itemdblclick: function (grid, record, item, index, e, opts) {
					viewResponse(record);
				},
				selectionchange: function (selectionModel, selected, opts) {
					var tools = this.getComponent('tools');

					if (selected.length > 0) {
						tools.getComponent('view').setDisabled(false);
					} else {
						tools.getComponent('view').setDisabled(true);
					}
				}
			},
			dockedItems: [
				{
					dock: 'top',
					xtype: 'toolbar',
					itemId: 'tools',
					items: [
						{
							xtype: 'combobox',
							itemId: 'question-activeStatus',
							value: 'A',
							editable: false,
							fieldLabel: 'Show Entries with:',
							labelWidth: '250px',
							name: 'question-activeStatus',
							displayField: 'description',
							valueField: 'code',
							listeners: {
								change: function (filter, newValue, oldValue, opts) {
									CoreService.userservice.getCurrentUser().then(function (userProfile) {
										questionPanel.grid.getStore().load({
											url: 'api/v1/resource/componentquestions/' + userProfile.username + '?status=' + newValue
										});
									});
								}
							},
							store: Ext.create('Ext.data.Store', {
								fields: [
									'code',
									'description'
								],
								data: [
									{
										code: 'A',
										description: 'Active questions'
									},
									{
										code: 'P',
										description: 'Pending questions'
									}
								]
							})
						},
						{
							text: 'View Responses',
							itemId: 'view',
							scale: 'medium',
							width: '170px',
							disabled: true,
							iconCls: 'fa fa-2x fa-eye icon-vertical-correction-view icon-button-color-view',
							handler: function () {
								var record = this.up('grid').getSelectionModel().getSelection()[0];
								viewResponse(record);
							}
						}
					]
				}
			]
		});

		var viewResponse = function (record) {

			var responseWin = Ext.create('Ext.window.Window', {
				title: 'Responses',
				modal: true,
				width: '70%',
				height: '60%',
				maximizable: true,
				closeMode: 'destroy',
				layout: 'fit',
				items: [
					{
						xtype: 'grid',
						columnLines: true,
						store: {
							sorters: [{
									property: 'componentName',
									direction: 'ASC'
								}],
							autoLoad: true,
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
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/componentquestions/' + record.get('questionId') + '/responses'
							}
						},
						columns: [
							{text: 'Entry', dataIndex: 'componentName', width: 275},
							{text: 'Response', dataIndex: 'response', flex: 1, minWidth: 200},
							{text: 'Post Date', dataIndex: 'answeredDate', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
							{text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'}
						],
						dockedItems: [
							{
								dock: 'top',
								xtype: 'panel',
								data: record.data,
								tpl: '<div style="padding: 10px; font-weight: bold;">{question}</div>'
							},
							{
								dock: 'top',
								xtype: 'toolbar',
								itemId: 'tools',
								items: [
									{
										xtype: 'combobox',
										itemId: 'answer-activeStatus',
										value: 'A',
										editable: false,
										fieldLabel: 'Show Entries with:',
										labelWidth: '250px',
										name: 'answer-activeStatus',
										displayField: 'description',
										valueField: 'code',
										listeners: {
											change: function (filter, newValue, oldValue, opts) {
												CoreService.userservice.getCurrentUser().then(function (userProfile) {
													questionPanel.grid.getStore().load({
														url: 'api/v1/resource/componentquestions/' + record.get('questionId') + '/responses?status=' + Ext.ComponentQuery.query('[itemId=answer-activeStatus]')[0].getSelection().getData().code
													});
												});
											}
										},
										store: Ext.create('Ext.data.Store', {
											fields: [
												'code',
												'description'
											],
											data: [
												{
													code: 'A',
													description: 'Active response'
												},
												{
													code: 'P',
													description: 'Pending response'
												}
											]
										})
									}
								]
							}
						]
					}
				]
			});

			responseWin.show();
		};

		questionPanel.add(questionPanel.grid);
		CoreService.userservice.getCurrentUser().then(function (userProfile) {
			questionPanel.grid.getStore().load({
				url: 'api/v1/resource/componentquestions/' + userProfile.username + '?status=' + Ext.ComponentQuery.query('[itemId=question-activeStatus]')[0].getSelection().getData().code
			});
		});

	},
	refresh: function () {
		var questionPanel = this;
		CoreService.userservice.getCurrentUser().then(function (userProfile) {
			questionPanel.grid.getStore().load({
				url: 'api/v1/resource/componentquestions/' + userProfile.username + '?status=' + Ext.ComponentQuery.query('[itemId=question-activeStatus]')[0].getSelection().getData().code
			});
		});
	}

});
