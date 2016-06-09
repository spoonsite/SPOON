
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				Ext.create('Ext.data.Store', {
					storeId: 'tagStore',
					autoLoad: true,
					fields: [
						'componentName',
						'tagId',
						'text',
						'createUser',
						'createDts',
						'securityMarkingType'
					],
					sorters: 'componentName',
					proxy: {
						id: 'tagStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/components/tagviews'
					}
				});


				Ext.create('Ext.data.Store', {
					storeId: 'securityTypeStore',
					autoLoad: true,
					fields: [
						'code',
						'description'
					],
					proxy: {
						id: 'securityTypeStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/lookuptypes/SecurityMarkingType/view'
					}
				});
			
				var tagGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Tags <i class="fa fa-question-circle"  data-qtip="Tags are user-definable labels that are associated with a component."></i>',
					id: 'tagGrid',
					store: Ext.data.StoreManager.lookup('tagStore'),
					columnLines: true,
					selModel: 'rowmodel',
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
							{text: 'Component', dataIndex: 'componentName', flex: 1, minWidth: 250},
							{text: 'Tag', dataIndex: 'text', width: 300},
							{text: 'Create User', dataIndex: 'createUser', width: 175},
							{
								text: 'Create Date',
								dataIndex: 'createDts',
								width: 275,
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s'
							},
							{
								text: 'Security Type', 
								dataIndex: 'securityMarkingDescription',
								width: 175,
								hidden: true,
								sortable: false
							}
						]
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										Ext.getCmp('tagGrid').getStore().load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAddTagForm();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Delete',
									id: 'tagGrid-tools-delete',
									iconCls: 'fa fa-2x fa-trash',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('tagGrid').getSelection()[0];
										actionDeleteTag(record);
									}
								}
							]
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('tagGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('tagGrid-tools-delete').enable(true);
							} else {
								Ext.getCmp('tagGrid-tools-delete').disable();
							}
						}
					}
				});


				var tagAddWin = Ext.create('Ext.window.Window', {
					id: 'tagAddWin',
					title: 'Add Tag',
					modal: true,
					width: '30%',
					y: '10em',
					iconCls: 'fa fa-lg fa-plus',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'addTagForm',
							layout: 'vbox',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								width: '100%'
							},
							items: [
								{
									xtype: 'combobox',
									fieldLabel: 'Component<span class="field-required"></span>',
									id: 'addTagForm-component',
									forceSelection: true,
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Select a Component',
									typeAhead: true,
									minChars: 3,
									allowBlank: false,
									name: 'component',
									store: Ext.create('Ext.data.Store', {
										storeId: 'componentStore',
										autoLoad: true,
										sorters: "description",
										fields: [
											'code',
											'description'
										],
										proxy: {
											id: 'componentStoreProxy',
											type: 'ajax',
											url: '../api/v1/resource/components/lookup'
										}
									})
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Tag<span class="field-required"></span>',
									id: 'adddTagForm-tag',
									name: 'text',
									allowBlank: false
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Security Type',
									id: 'addTagForm-securityType',
									displayField: 'description',
									valueField: 'code',
									emptyText: 'Select a Security Type',
									name: 'securityType',
									hidden: !${branding.allowSecurityMarkingsFlg},
									store: Ext.data.StoreManager.lookup('securityTypeStore')
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-save',
											formBind: true,
											handler: function () {
												var form = Ext.getCmp('addTagForm');
												// Submit Data
												if (form.isValid()) {
													var formData = form.getValues();
													var url = '/openstorefront/api/v1/resource/components/';
													url += formData.component + "/tags";
													var method = 'POST';

													// Compile properly formatted data
													data = {};
													data.text = formData.text;
													data.securityMarkingType = formData.securityType;

													CoreUtil.submitForm({
														url: url,
														method: method,
														data: data,
														removeBlankDataItems: true,
														form: Ext.getCmp('addTagForm'),
														success: function (response, opts) {
															// Server responded OK
															var errorResponse = Ext.decode(response.responseText);

															// Confusingly, you will only see the "success"
															// property in the response if the success
															// is success = false. Therefore
															// the appearance of the success property actually
															// means there was a failure.

															if (!errorResponse.hasOwnProperty('success')) {
																// Validation succeeded.
																Ext.toast('Saved Successfully', '', 'tr');
																Ext.getCmp('addTagForm').setLoading(false);
																Ext.getCmp('addTagForm').reset();
																Ext.getCmp('tagAddWin').hide();
																Ext.getCmp('tagGrid').getStore().load();
																Ext.getCmp('tagGrid').getSelectionModel().deselectAll();
																Ext.getCmp('tagGrid-tools-delete').setDisabled(true);
															} else {
																// Validation failed

																// Compile an object to pass to ExtJS Form
																// that allows validation messages
																// using the markInvalid() method.
																var errorObj = {};
																Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																	errorObj[item.key] = item.value;
																});
																var form = Ext.getCmp('addTagForm').getForm();
																form.markInvalid(errorObj);
															}
														},
														failure: function (response, opts) {
															// The same failure procedure as seen above
															var errorResponse = Ext.decode(response.responseText);
															var errorObj = {};
															Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																errorObj[item.key] = item.value;
															});
															var form = Ext.getCmp('addTagForm').getForm();
															form.markInvalid(errorObj);
														}
													});
												}
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Cancel',
											iconCls: 'fa fa-close',
											handler: function () {
												Ext.getCmp('addTagForm').reset();
												Ext.getCmp('tagAddWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});

				var actionAddTagForm = function () {
					tagAddWin.show();
					Ext.getCmp('addTagForm').reset(true);
				};

				var actionDeleteTag = function (record) {
					if (record) {
						var tagId = record.data.tagId;
						var componentId = record.data.componentId;
						var method = 'DELETE';
						var url = '/openstorefront/api/v1/resource/components/';
						url += componentId + '/tags/' + tagId;

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully deleted tag "' + record.data.text + '"';
								Ext.toast(message, '', 'tr');
								Ext.getCmp('tagGrid').getStore().load();
								Ext.getCmp('tagGrid').getSelectionModel().deselectAll();
								Ext.getCmp('tagGrid-tools-delete').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to delete',
										'Error: Could not delete tag "' + record.data.name + '"');
							}
						});

					} 
					else {
						Ext.MessageBox.alert('No Record Selected', 'Error: You have not selected a record.');
					}
				};


				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						tagGrid
					]
				});
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>