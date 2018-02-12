<%--
/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

 Author: cyearsley
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">
	</stripes:layout-render>

	<link rel="stylesheet" href="css/customForms.css">	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.require('OSF.customSubmissionTool.Window');
		Ext.require('OSF.customSubmissionTool.FormBuilderPanel');
		Ext.require('OSF.customSubmissionTool.FormBuilderItem');
		Ext.onReady(function(){	
			
			var formTemplateGrid = Ext.create('Ext.grid.Panel', {
				id: 'formTemplateGrid',
				title: 'Manage Custom Submission Forms <i class="fa fa-question-circle"  data-qtip="Add, edit, and delete custom submission forms"></i>',
				columnLines: true,
				store: {
					autoLoad: true,
					fields: [
						{ name: 'createDts',
							type: 'date',
							dateFormat: 'c'
						},
						{ name: 'updateDts',
							type: 'date',
							dateFormat: 'c'
						}
					],
					// proxy: {
					// 	type: 'ajax',
					// 	url: 'api/v1/resource/evaluationtemplates'
					// },
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							// store.getProxy().extraParams = {
							// 	status: Ext.getCmp('filterActiveStatus').getValue()
							// };
						}
					}
				},
				listeners: {
					itemdblclick: function(grid, record, item, index, e, opts){
						actionAddEdit(record);
					},	
					selectionchange: function(selModel, selected, opts) {
						var tools = Ext.getCmp('formTemplateGrid').getComponent('tools');

						if (selected.length > 0) {
							tools.getComponent('edit').setDisabled(false);
							tools.getComponent('togglestatus').setDisabled(false);
							tools.getComponent('delete').setDisabled(false);
						} else {
							tools.getComponent('edit').setDisabled(true);
							tools.getComponent('togglestatus').setDisabled(true);
							tools.getComponent('delete').setDisabled(true);
						}
					}
				},
				columns: [
					{ text: 'Name', dataIndex: 'name', flex: 10 },
					{ text: 'Description', dataIndex: 'description', flex: 10 },
					{ text: 'Form Completion Status <i class="fa fa-question-circle" data-qtip="Indicates that a form is ready for use"></i>', dataIndex: 'completionStatus', flex: 5 },
					{ text: 'Active Status <i class="fa fa-question-circle" data-qtip="Indicates that a form is being used"></i>', dataIndex: 'status', flex: 4 },
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Create User', dataIndex: 'createUser', flex: 4 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  flex: 4 },
					{ text: 'Update User', dataIndex: 'updateUser', flex: 4 }
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						items: [
							Ext.create('OSF.component.StandardComboBox', {
								id: 'filterActiveStatus',
								emptyText: 'Active',
								value: 'A',
								fieldLabel: 'Active Status',
								name: 'activeStatus',
								typeAhead: false,
								editable: false,
								width: 200,
								listeners: {
									change: function(filter, newValue, oldValue, opts){
										actionRefresh();
									}
								},
								storeConfig: {
									customStore: {
										fields: [
											'code',
											'description'
										],
										data: [
											{
												code: 'A',
												description: 'Active'
											},
											{
												code: 'I',
												description: 'Inactive'
											}
										]
									}
								}
							})
						]
					},	
					{
						dock: 'top',
						xtype: 'toolbar',
						itemId: 'tools',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
								scale: 'medium',
								handler: function(){
									actionRefresh();
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Add Form',
								iconCls: 'fa fa-2x fa-plus icon-button-color-save',
								width: '150px',
								scale: 'medium',
								handler: function(){
									actionAddEdit();
								}
							},
							{
								text: 'Edit Form',
								iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
								itemId: 'edit',
								width: '150px',
								disabled: true,
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionAddEdit(record);
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Toggle Active Status',
								iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
								itemId: 'togglestatus',
								disabled: true,	
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionToggleStatus(record);
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
								itemId: 'delete',
								disabled: true,
								scale: 'medium',
								handler: function(){
									var record = Ext.getCmp('formTemplateGrid').getSelectionModel().getSelection()[0];
									actionDelete(record);
								}
							}
							
						]
					}
				]
			});
			
			addComponentToMainViewPort(formTemplateGrid);
			
			var actionRefresh = function() {
				Ext.getCmp('formTemplateGrid').getStore().reload();
			};
		
			var actionAddEdit = function(record) {
				record = record || {};
				Ext.create('Ext.window.Window', {
					width: 400,
					height: 200,
					modal: true,
					title: 'Form Name',
					itemId: 'createFormWindow',
					items: [
						{
							xtype: 'form',
							itemId: 'createFormWindowForm',
							layout: 'anchor',
						    defaults: {
						        anchor: '100%'
						    },
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'New Form Name <i class="fa fa-question-circle"  data-qtip="This is what the form template will be identified by"></i>',
									labelAlign: 'top',
									name: 'formName',
									margin: 15,
									flex: 1
								}
							]
						}
					],
					dockedItems: [{
				        xtype: 'toolbar',
				        dock: 'bottom',
				        items: [
				        	{
					            xtype: 'button',
					            text: 'Create Form',
					            listeners: {
					            	click: function () {
					            		var createFormWindow = this.up('[itemId=createFormWindow]');
					            		var form = createFormWindow.queryById('createFormWindowForm');
					            		form.setLoading(true);

										Ext.create('OSF.customSubmissionTool.Window', {
											recordItem: Ext.apply(record, {formName: form.getValues().formName}),
											listeners: {
												show: function () {
					            					createFormWindow.close();
												}
											}
										}).show();
					            		
					            	}
					            }
					        },
					        '->',
					        {
					            xtype: 'button',
					            text: 'Cancel',
					            listeners: {
					            	click: function () {
					            		this.up('[itemId=createFormWindow]').close();
					            	}
					            }
					        }
				        ]
				    }]
				}).show();
			};
			
			var actionDelete = function(record) {
				Ext.Msg.show({
					title:'Delete Form Template?',
					iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
					message: 'Are you sure you want to delete this form template?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
					}
				});
			};
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
