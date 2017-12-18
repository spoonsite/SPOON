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
    Document   : faq
    Created on : Dec 15, 2017, 11:58:32 AM
    Author     : kbair
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>		
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){
						
				var addEditWindow = Ext.create('Ext.window.Window', {
					id: 'addEditWindow',
					title: 'Add/Edit Frequently Asked Questions',
					iconCls: 'fa fa-edit icon-button-color-edit',
					modal: true,
					width: '65%',
					height: 470,
					maximizable: true,
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							itemId: 'faqForm',
							layout: 'anchor',
							scrollable: 'true',
							bodyStyle: 'padding: 10px;',						
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{											
											text: 'Save',
											formBind: true,
											autoEl: {
												'data-test': 'saveBtnAddFAQ'
											},
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											handler: function(){	
												var win = this.up('window');
												var form = this.up('form');
												var data = form.getValues();

												var method = 'POST';
												var update = '';
												if (data.faqId) {
													update = '/' + data.faqId;
													method = 'PUT';
												}

												CoreUtil.submitForm({
													url: 'api/v1/resource/faq' + update,
													method: method,
													data: data,
													form: form,
													success: function(){
														actionRefresh();
														form.reset();
														win.close();
													}
												});
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											xtype: 'button',
											text: 'Cancel',										
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
											handler: function(){
												this.up('form').reset();
												this.up('window').close();
											}									
										}											
									]
								}
							],
							items: [
								{
									xtype: 'hidden',
									name: 'faqId'
								},
								Ext.create('OSF.component.StandardComboBox', {
									name: 'category',									
									allowBlank: false,									
									margin: '0 0 10 0',
									width: '100%',
									fieldLabel: 'Category <span class="field-required" />',
									forceSelection: false,
									valueField: 'description',
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/FaqCategoryType',
										sorters: [
											new Ext.util.Sorter({
												property: 'description',
												direction: 'ASC'
											})
										]
									}
								}),								
								{
									xtype: 'textfield',
									fieldLabel: 'Question <span class="field-required" />',									
									allowBlank: false,								
									maxLength: '80',
									name: 'question'
								},		
								{
									xtype: 'tinymce_textarea',
									fieldStyle: 'font-family: Courier New; font-size: 12px;',
									style: { border: '0' },
									name: 'answer',
									width: '100%',
									height: 300,
									maxLength: 65536,
									tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig("osfmediaretriever"), {
										mediaSelectionUrl: function(){
//											if (generalForm.componentRecord) {
//												var componentId = generalForm.componentRecord.get('componentId');
//												return 'api/v1/resource/faq/' + componentId + '/media/view';
//											} else {
//												return 'api/v1/resource/components/NEW/media/view';
//											}
										}
									})
								}							
							]
						}
					]					
				});
					
				var faqGrid = Ext.create('Ext.grid.Panel', {
					id: 'faqGrid',
					title: 'FAQ Management <i class="fa fa-question-circle" data-qtip="Allows for managing Frequently Asked Questions." ></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						id: 'faqGridStore',
						remoteSort: false,
						sorters: [
							new Ext.util.Sorter({
								property: 'question',
								direction: 'ASC'
							})
						],	
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
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/faq',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						},
						listeners: {
							beforeLoad: function(store, operation, eOpts){
								store.getProxy().extraParams = {
									status: Ext.getCmp('filterActiveStatus').getValue()									
								};
							}
						}							
					},
					columns: [					
						{ text: 'Category', dataIndex: 'category', width: 200 },
						{ text: 'Question', dataIndex: 'question', flex: 1, minWidth: 200 },
						{ text: 'Answer', dataIndex: 'answer', flex: 2, minWidth: 200 },
						{ text: 'Active Status', align: 'center', dataIndex: 'activeStatus', width: 150 },
						{ text: 'Create User', dataIndex: 'createUser', width: 200, hidden: true },
						{ text: 'Create Date', dataIndex: 'createDts', width: 200, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s'  },
						{ text: 'Update User', dataIndex: 'updateUser', width: 200, hidden: true },
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, hidden: true, xtype: 'datecolumn', format: 'm/d/y H:i:s'  }
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},						
						selectionchange: function(selModel, selected, opts) {
							var tools = Ext.getCmp('faqGrid').getComponent('tools');
							
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
								}),
								Ext.create('OSF.component.StandardComboBox', {
									id: 'filterFaqCategoryType',
									emptyText: 'All',
									fieldLabel: 'Category',
									name: 'categoryType',
									valueField: 'code',
									displayField: 'description',
									typeAhead: false,
									editable: false,
									width: 200,			
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											if(newValue) {
												Ext.getCmp('faqGrid').getStore().filter('category',newValue);
											}
											else {
												Ext.getCmp('faqGrid').getStore().clearFilter()
											}
										}
									},
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/FaqCategoryType',
										model: undefined,
										fields: [
											'code',
											'description'
										],
										addRecords: [
											{
												code: null,
												description: 'All'
											}
										]
									}
								})
							]
						},
						{
							xtype: 'toolbar',
							itemId: 'tools',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'faqRefreshBtn',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									itemId: 'add',
									id: 'faqMngAddBtn',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAdd();										
									}									
								},								
								{
									text: 'Edit',
									id: 'faqMngEditBtn',
									itemId: 'edit',
									scale: 'medium',
									width: '100px',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									handler: function () {
										actionEdit(Ext.getCmp('faqGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									itemId: 'togglestatus',
									autoEl: {
										'data-test': 'toggleStatusFAQBtn'
									},
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									handler: function() {
										actionToggleStatus(Ext.getCmp('faqGrid').getSelectionModel().getSelection()[0]);	
									}
								},						
								{
									xtype: 'tbfill'
								},									
								{
									text: 'Delete',
									itemId: 'delete',
									autoEl: {
										'data-test': 'faqsDeleteBtn'
									},
									disabled: true,
									scale: 'medium',									
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									handler: function() {
										actionDelete(Ext.getCmp('faqGrid').getSelectionModel().getSelection()[0]);	
									}
								}								
							]
						}					
					]					
				});
				
				
				var actionRefresh = function() {
					Ext.getCmp('faqGrid').getStore().load({
						url: 'api/v1/resource/faq'
					});
//					Ext.getCmp('faqGrid').getStore().filter('category',newValue);
				};		
				
				var actionAdd = function() {
					addEditWindow.show();
					addEditWindow.getComponent('faqForm').reset();
					
				};

				var actionEdit = function(record) {
					addEditWindow.show();
					addEditWindow.getComponent('faqForm').reset();
					addEditWindow.getComponent('faqForm').loadRecord(record);
				};
		
				var actionToggleStatus = function(record) {
					
//					var newStatus = 'inactivate';
//					var urlEnding = 'inactivate';
//					if (record.get('activeStatus') === 'I') {
//						newStatus = 'activate';
//						urlEnding = 'activate';
//					}
					faqGrid.setLoading('Updating Status...');
					Ext.Ajax.request({
						url: 'api/v1/resource/faq/' + record.get('faqId'),
						method: 'PUT',																		
						callback: function(){
							faqGrid.setLoading(false);
						},
						success: function(response, opts){
							actionRefresh();
						}
					});
				};
		
				var actionDelete = function(record) {					
					faqGrid.setLoading('Deleting FAQ...');
					Ext.Ajax.request({
						url: 'api/v1/resource/faq/' + record.get('faqId'),
						method: 'DELETE',
						callback: function(){
							faqGrid.setLoading(false);
						},
						success: function(response, opts){
							actionRefresh();
						}
					});
				};
				
				addComponentToMainViewPort(faqGrid);
				
			});
		
	</script>
    </stripes:layout-component>
</stripes:layout-render>