<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {


				var securityTypeStore = Ext.create('Ext.data.Store', {
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


				var highlightTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'highlightTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {
						type: 'ajax',
						url: '/openstorefront/api/v1/resource/lookuptypes/HighlightType/view'
					}
				});

				var getSecurityType = function getSecurityType(code) {
					try {
						return securityTypeStore.getData().find('code', code).data.description;
					} catch (err) {
						if (code) {
							Ext.toast('Unable to find security type code: ' + code);
							return '';
						} else
							return '';
					}
				};

				var getHighlightType = function getHighlightType(code) {
					try {
						return highlightTypeStore.getData().find('code', code).data.description;
					} catch (err) {
						Ext.toast('Unable to find highlight type code: ' + code);
						return '';
					}
				};

				var highlightStore = Ext.create('Ext.data.Store', {
					storeId: 'highlightStore',
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: '/openstorefront/api/v1/resource/highlights?sortField=orderingPosition'
					}
				});


				var highlightGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Highlights <i class="fa fa-question-circle"  data-qtip="Highlights are pieces of information of interest to all users that show up on the front page"></i>',
					id: 'highlightGrid',
					store: highlightStore,
					columnLines: true,
					columns: [
						{text: 'No.', dataIndex: 'orderingPosition', flex: 0.2},
						{text: 'Title', dataIndex: 'title', flex: 1},
						{text: 'Description', dataIndex: 'description', flex: 4, cellWrap: true},
						{
							text: 'Type',
							dataIndex: 'highlightType',
							flex: 1,
							renderer: function (value, metaData, record) {
								return getHighlightType(value);
							}
						},
						{text: 'Link', dataIndex: 'link', flex: 1},
						{
							text: 'Security Type',
							dataIndex: 'securityMarkingType',
							flex: 1,
							renderer: function (value, metaData, record) {
								return getSecurityType(value);
							}
						}
					],
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
										Ext.getCmp('highlightGrid').getStore().load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									id: 'highlightGrid-tools-add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAddHighlight();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									id: 'highlightGrid-tools-edit',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-edit',
									disabled: true,
									handler: function () {
										actionEditHighlight(Ext.getCmp('highlightGrid').getSelection()[0]);
									}
								},
								{
									text: 'Delete',
									id: 'highlightGrid-tools-delete',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash',
									disabled: true,
									handler: function () {

									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							actionEditCodes(record);
						},
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('highlightGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('highlightGrid-tools-edit').enable();
							} else {
								Ext.getCmp('highlightGrid-tools-edit').disable();
							}
						}
					}
				});

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						highlightGrid
					]
				});


				var actionEditHighlight = function actionEditHighlight(record) {
					Ext.getCmp('editHighlightForm').loadRecord(record);
					Ext.getCmp('editHighlightForm').edit = true;
					Ext.getCmp('editHighlightForm').highlightId = record.data.highlightId;
					highlightAddEditWin.show();
				};

				var actionAddHighlight = function actionAddHighlight() {
					Ext.getCmp('editHighlightForm').reset();
					Ext.getCmp('editHighlightForm').edit = false;
					highlightAddEditWin.show();
				};

				var highlightAddEditWin = Ext.create('Ext.window.Window', {
					id: 'highlightAddEditWin',
					title: 'Add/Edit Highlight',
					modal: true,
					width: '55%',
					height: '70%',
					y: '10em',
					iconCls: 'fa fa-lg fa-edit',
					layout: 'fit',
					items: [
						{
							xtype: 'form',
							id: 'editHighlightForm',
							layout: 'vbox',
							scrollable: true,
							bodyStyle: 'padding: 10px;',
							defaults: {
								labelAlign: 'top',
								width: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									id: 'highlightEntryForm-Title',
									fieldLabel: 'Title<span class="field-required" />',
									name: 'title'
								},
								{
									xtype: 'panel',
									html: '<b>Description</b> <span class="field-required" />'
								},
								{
									xtype: 'tinymce_textarea',
									fieldStyle: 'font-family: Courier New; font-size: 12px;',
									style: {border: '0'},
									name: 'description',
									width: '100%',
									height: 300,
									maxLength: 32000,
									tinyMCEConfig: CoreUtil.tinymceConfig()
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Highlight Type<span class="field-required" />',
									id: 'highlightEntryForm-Type',
									forceSelection: true,
									name: 'highlightType',
									valueField: 'code',
									displayField: 'description',
									typeAhead: false,
									editable: false,
									store: highlightTypeStore
								},
								{
									xtype: 'textfield',
									id: 'highlightEntryForm-Link',
									fieldLabel: 'Link',
									name: 'link',
									vtype: 'url',
									allowBlank: true
								},
								{
									xtype: 'combobox',
									fieldLabel: 'Security Type',
									id: 'highlightEntryForm-SecurityType',
									name: 'securityMarkingType',
									valueField: 'code',
									displayField: 'description',
									typeAhead: false,
									editable: false,
									store: securityTypeStore
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
												var form = Ext.getCmp('editHighlightForm');
												// Submit Data
												if (form.isValid()) {
													var formData = form.getValues();
													console.log(formData);
													var highlightId = form.highlightId;
													var url = '/openstorefront/api/v1/resource/highlights';
													var method = 'POST';
													if (form.edit) {
														url += '/' + highlightId;
														method = 'PUT';
													}

													CoreUtil.submitForm({
														url: url,
														method: method,
														data: formData,
														removeBlankDataItems: true,
														form: Ext.getCmp('editHighlightForm'),
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
																Ext.getCmp('editHighlightForm').setLoading(false);
																Ext.getCmp('editHighlightForm').reset();
																Ext.getCmp('highlightAddEditWin').hide();
																Ext.getCmp('highlightGrid').getStore().load();
																Ext.getCmp('highlightGrid').getSelectionModel().deselectAll();
																Ext.getCmp('highlightGrid-tools-delete').disable();
															} else {
																// Validation failed

																// Compile an object to pass to ExtJS Form
																// that allows validation messages
																// using the markInvalid() method.
																var errorObj = {};
																Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
																	errorObj[item.key] = item.value;
																});
																var form = Ext.getCmp('editHighlightForm').getForm();
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
															var form = Ext.getCmp('editHighlightForm').getForm();
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
												Ext.getCmp('editHighlightForm').reset();
												Ext.getCmp('highlightAddEditWin').hide();
											}
										}
									]
								}
							]
						}
					]
				});



			});

		</script>
    </stripes:layout-component>
</stripes:layout-render>