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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var highlightTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'highlightTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/lookuptypes/HighlightType/view'
					},
					listeners: {
						load: function(store, records) {
							highlightStore.load();
						}
					}
				});

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
					autoLoad: false,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/highlights?sortField=orderingPosition'
					}
				});


				var highlightGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Highlights <i class="fa fa-question-circle"  data-qtip="Highlights are pieces of information of interest to all users that show up on the front page"></i>',
					id: 'highlightGrid',
					store: highlightStore,
					columnLines: true,
					columns: {
						defaults: {
							sortable: false
						},
						items: [
							{text: 'Ordering Position', dataIndex: 'orderingPosition', flex: 1, hidden: true},
							{text: 'Title', dataIndex: 'title', flex: 1},
							{text: 'Description', dataIndex: 'description', flex: 4, 
								renderer: function (value, metaData, record) {
									return Ext.util.Format.stripTags(value);
								}
							},
							{
								text: 'Type',
								dataIndex: 'highlightType',
								flex: 1,
								renderer: function (value, metaData, record) {
									return getHighlightType(value);
								}
							},
							{text: 'Link', dataIndex: 'link', flex: 1},
							{ text: 'Security Marking',  dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg} }
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
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									autoEl: {
										'data-test': 'highlightRefreshBtn'
									},
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
									width: '100px',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
									handler: function () {
										actionAddHighlight();
									}
								},
								{
									text: 'Edit',
									id: 'highlightGrid-tools-edit',
									scale: 'medium',
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
									disabled: true,
									handler: function () {
										actionEditHighlight(Ext.getCmp('highlightGrid').getSelection()[0]);
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									xtype: 'label',
									html: '<strong>Display Position:</strong>'
								},
								{
									text: 'Up',
									id: 'highlightGrid-tools-up',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-arrow-circle-o-up icon-vertical-correction icon-button-color-default',
									disabled: true,
									handler: function () {
										actionUpHighlight(Ext.getCmp('highlightGrid').getSelection()[0]);
									}
								},
								{
									text: 'Down',
									id: 'highlightGrid-tools-down',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-arrow-circle-o-down icon-vertical-correction icon-button-color-default',
									disabled: true,
									handler: function () {
										actionDownHighlight(Ext.getCmp('highlightGrid').getSelection()[0]);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'highlightGrid-tools-delete',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-vertical-correction icon-button-color-warning',
									disabled: true,
									handler: function () {
										actionDeleteHighlight();
									}
								}
							]
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							actionEditCodes(record);
						},
						selectionchange: function (grid, record, eOpts) {
							if (Ext.getCmp('highlightGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('highlightGrid-tools-edit').enable();
								Ext.getCmp('highlightGrid-tools-delete').enable();
								var index = Ext.getCmp('highlightGrid').getSelectionModel().getCurrentPosition().rowIdx;
								var maxIndex = highlightStore.getCount()-1;
								if (index === 0) {
									Ext.getCmp('highlightGrid-tools-up').disable();
								}
								else {
									Ext.getCmp('highlightGrid-tools-up').enable();
								}
								if (index === maxIndex) {
									Ext.getCmp('highlightGrid-tools-down').disable();
								}
								else {
									Ext.getCmp('highlightGrid-tools-down').enable();
								}
								
							} else {
								Ext.getCmp('highlightGrid-tools-edit').disable();
								Ext.getCmp('highlightGrid-tools-delete').disable();
								Ext.getCmp('highlightGrid-tools-up').disable();
								Ext.getCmp('highlightGrid-tools-down').disable();
							}
						}
					}
				});

				addComponentToMainViewPort(highlightGrid);

				var actionEditHighlight = function actionEditHighlight(record) {
					Ext.getCmp('editHighlightForm').reset();
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

				var actionUpHighlight = function actionUpHighlight(record) {
					actionMoveHighlight(record, -2);
				};

				var actionDownHighlight = function actionDownHighlight(record) {
					actionMoveHighlight(record, 1);
				};


				var actionMoveHighlight = function actionMoveHighlight(record, direction) {

					// Move the record on the grid to the new location (up or down)
					var index = highlightGrid.getSelectionModel().getCurrentPosition().rowIdx+1;
					highlightGrid.getStore().insert(index+direction, record);

					// Each time a record is moved on the grid, it is necessary 
					// to update all other records with their proper positions. Otherwise,
					// you may end up with records that share the same orderingPosition,
					// which will lead to unexpected results for the user.
					 
					// To do this, we loop through all records in the store and send
					// an API request setting the new orderingPosition equal to that of 
					// the record's grid row index position.
					var storeData = highlightStore.getData();
					var success = true;
					for (var i = 0; i < storeData.length; i++) {
						var thisRecord = storeData.getAt(i);
						thisRecord.data.orderingPosition = highlightStore.indexOf(thisRecord);
						delete thisRecord.data.storageVersion;
						var url = 'api/v1/resource/highlights';
						url += '/' + thisRecord.data.highlightId;
						var method = 'PUT';
						var successes = 0;

						Ext.Ajax.request({
							url: url,
							method: method,
							jsonData: thisRecord.data,
							success: function (response, opts) {
								successes++;
								if (successes === storeData.length) {
									if (success === true) {
										var message = 'Successfully moved "' + record.data.title + '"';
										Ext.toast(message, '', 'tr');
									}
									// If issues arise, loading the store will always help.
									// Ext.getCmp('highlightGrid').getStore().load();
								}
							},
							failure: function (response, opts) {
								success = false;
							}
						});
					}

					// Finally, it is useful for buttons to think that 
					// the selection has changed
					highlightGrid.fireEvent('selectionchange');

				};

				var actionDeleteHighlight = function actionDeleteHighlight() {
					var record = highlightGrid.getSelection()[0];
					Ext.Msg.show({
						title: '<i class="fa fa-warning icon-horizontal-correction-right"></i>' + ' ' + '<span class="shift-window-text-right">Delete Highlight?</span>',
						message: 'Are you sure you want to delete <b>"' + record.data.title + '"</b>?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						minHeight: '175',
						autoEl: {
							'data-test': 'deleteHighlightWindow'
						},
						fn: function(btn) {
							if (btn === 'yes') {
								var highlightId = record.data.highlightId;
								var url = 'api/v1/resource/highlights';
								url += '/' + highlightId + '/delete';
								var method = 'DELETE';
								Ext.Ajax.request({
									url: url,
									method: method,
									success: function (response, opts) {
										var message = 'Successfully deleted highlight: "'+ record.data.title + '"';
										Ext.toast(message, '', 'tr');
										Ext.getCmp('highlightGrid').getStore().load();
										Ext.getCmp('highlightGrid-tools-edit').disable();
										Ext.getCmp('highlightGrid-tools-delete').disable();
									},
									failure: function (response, opts) {
										Ext.MessageBox.alert('Failed to delete highlight',
										'Error: Could not delete highlight: "' + record.data.title + '"');
									}
								});
							}
						}
					});
				};

				var highlightAddEditWin = Ext.create('Ext.window.Window', {
					id: 'highlightAddEditWin',
					title: 'Add/Edit Highlight',
					minHeight: 750,
					minWidth: 700,
					modal: true,
					scrollable: true,
					maximizable: true,
					y: '10em',
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
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
									name: 'title',
									maxLength: 255
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
									height: 400,
									maxLength: 65536,
									tinyMCEConfig: Ext.apply(CoreUtil.tinymceSearchEntryConfig(), {
										mediaSelectionUrl: MediaUtil.generalMediaUrl,
										mediaUploadHandler: MediaUtil.generalMediaUnloadHandler
									})
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
								Ext.create('OSF.component.SecurityComboBox', {	
									hidden: !${branding.allowSecurityMarkingsFlg}
								})							
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Save',
											iconCls: 'fa fa-lg fa-save icon-button-color-save',
											formBind: true,
											handler: function () {
												var form = Ext.getCmp('editHighlightForm');
												// Submit Data
												if (form.isValid()) {
													var formData = form.getValues();
													var highlightId = form.highlightId;
													var url = 'api/v1/resource/highlights';
													var method = 'POST';
													// Set the ordering position to the last spot.
													formData.orderingPosition = highlightStore.getCount();
													if (form.edit) {
														url += '/' + highlightId;
														method = 'PUT';
														// Set the orderingPosition to where it currently is on the grid.
														formData.orderingPosition = Ext.getCmp('highlightGrid').getSelectionModel().getCurrentPosition().rowIdx;
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
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',
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