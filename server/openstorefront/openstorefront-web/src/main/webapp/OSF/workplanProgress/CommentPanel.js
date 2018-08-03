/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanProgress.CommentPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf.wp.commentpanel',

	title: 'Workflow Comments',
	iconCls: 'fa fa-lg fa-comment',
	layout: 'fit',
	items: [
		{
			xtype: 'panel',
			itemId: 'comments',
			bodyStyle: 'padding: 10px;',
			scrollable: true,
			items: [
			],
			dockedItems: [
				{
					xtype: 'form',
					itemId: 'form',
					dock: 'bottom',
					layout: 'anchor',
					items: [
						{
							xtype: 'hidden',
							name: 'commentId'
						},
						{
							xtype: 'hidden',
							name: 'replyCommentId'
						},
						{
							xtype: 'htmleditor',
							name: 'comment',
							width: '100%',
							fieldBodyCls: 'form-comp-htmleditor-border',
							maxLength: 4000
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							layout: {
								vertical: true,
								type: 'hbox',
								align: 'stretch'
							},
							items: [
								{
									xtype: 'fieldcontainer',
									fieldLabel: 'Private',
									defaultType: 'checkboxfield',
									items: [
										{
											inputValue: '1',
											id: 'checkbox1'
										}
									]
								},
								{
									xtype: 'toolbar',
									items: [
										{
											text: 'Comment',
											iconCls: 'fa fa-lg fa-comment icon-button-color-save',
											handler: function () {
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											itemId: 'cancel',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function () {
											}
										}
									]
								}
							]
						}
					]
				}
			]
		}
	],
	listeners: {
		afterrender: function () {

		}
	}
});
