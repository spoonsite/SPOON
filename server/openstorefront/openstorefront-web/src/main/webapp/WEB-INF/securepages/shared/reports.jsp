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
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../layout/${actionBean.headerPage}">
		</stripes:layout-render>

        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {

				var optionsRender = function(v, meta) {
					if (v) {
						if (v.category) {
							return 'Category: ' + v.category;
						}
						else if (v.startDts) {
							var details = '';
							if (v.startDts) {
								details = details + 'Start Date: ' + Ext.util.Format.date(v.startDts, 'm/d/y H:i:s') + '<br>';
							}
							if (v.endDts) {
								details = details + 'End Date: ' + Ext.util.Format.date(v.endDts, 'm/d/y H:i:s') + '<br>';
							}
							return details;
						}
						else if (v.previousDays) {
							return 'Previous Days: ' + v.previousDays;
						}
						else if (v.maxWaitSeconds) {
							return 'Max Wait Seconds: ' + v.maxWaitSeconds;
						}
						return '';
					}
					return '';
				};

				var scheduleReportsGridStore = Ext.create('Ext.data.Store', {
					id: 'scheduleReportsGridStore',
					autoLoad: true,
					sorters: [
						new Ext.util.Sorter({
							property: 'createDts',
							direction: 'DESC'
						})
					],
					fields: [
						{
							name: 'createDts',
							type: 'date',
							dateFormat: 'c'
						},
						{
							name: 'lastRanDts',
							type: 'date',
							dateFormat: 'c'
						}
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/scheduledreports',
						method: 'GET',
						reader: {
							type: 'json',
							rootProperty: '',
							totalProperty: ''
						}
					})
				});

				var scheduledReportsWin = Ext.create('Ext.window.Window', {
					title: 'Scheduled Reports',
					iconCls: 'fa fa-calendar-plus-o',
					modal: true,
					width: '80%',
					height: '80%',
					maximizable: true,
					layout: 'fit',
					items: [
						Ext.create('Ext.grid.Panel', {
							id: 'scheduleReportsGrid',
							store: scheduleReportsGridStore,
							columnLines: true,
							bodyCls: 'border_accent',
							columns: [
								{text: 'Report Type', dataIndex: 'reportType', width: 200,
									renderer: function (value, meta, record) {
										return record.get('reportTypeDescription');
									}
								},
								{text: 'Format', dataIndex: 'reportFormat', width: 250,
									renderer: function (value, meta, record) {
										return record.get('reportFormatDescription');
									}
								},
								{text: 'Create User', dataIndex: 'createUser', width: 150},
								{text: 'Scheduled Interval', dataIndex: 'scheduleIntervalDays', width: 200,
									renderer: function (v, meta) {
										if (v === 1) {
											return 'Daily';
										}
										else if (v > 1 && v < 8) {
											return 'Weekly';
										}
										else if (v > 8) {
											return 'Monthly';
										}

									}
								},
								{text: 'Last Run Date', dataIndex: 'lastRanDts', width: 170, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
								{text: 'Email Addresses', dataIndex: 'emailAddresses', minWidth: 200, flex: 1,
									renderer: function (v, meta) {
										var emailStr = '';
										if (v && v.length) {
											for (index = 0; index < v.length; ++index) {

												emailStr += v[index].email + '<br/>';
											}
										}
										return emailStr;
									}
								},
								{text: 'Options', dataIndex: 'reportOption', minWidth: 200, flex: 1, sortable: false,
									renderer: optionsRender
								},
								{text: 'Active Status', dataIndex: 'activeStatus', width: 125,
									filter: {
										type: 'string'
									}
								}
							],
							bufferedRenderer: false,
							dockedItems: [
								{
									dock: 'top',
									xtype: 'toolbar',
									items: [
										Ext.create('OSF.component.StandardComboBox', {
											id: 'scheduleReportFilter-ActiveStatus',
											fieldLabel: 'Active Status',
											name: 'activeStatus',
											displayField: 'description',
											valueField: 'code',
											value: 'A',
											listeners: {
												change: function (filter, newValue, oldValue, opts) {
													scheduleReportRefreshGrid();
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
										})]
								},
								{
									dock: 'top',
									xtype: 'toolbar',
									items: [
										{
											text: 'Refresh',
											scale: 'medium',
											id: 'reportRefreshButton',
											iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
											tip: 'Refresh the list of records',
											handler: function () {
												scheduleReportRefreshGrid();
											},
											tooltip: 'Refresh the list of records'
										},
										{
											xtype: 'tbseparator'
										},
										{
											text: 'Add',
											id: 'reportAddButton',
											scale: 'medium',
											width: '100px',
											iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
											disabled: false,
											handler: function () {
												scheduleReportAdd();
											},
											tooltip: 'Add a record'
										},
										{
											text: 'Edit',
											id: 'reportEditButton',
											scale: 'medium',
											width: '100px',
											iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
											disabled: true,
											handler: function () {
												scheduleReportEdit();
											},
											tooltip: 'Edit a record'
										},
										{
											xtype: 'tbseparator'
										},
										{
											text: 'Toggle Status',
											id: 'reportActivateButton',
											scale: 'medium',
											iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
											disabled: true,
											handler: function () {
												scheduleReportActivate();
											},
											tooltip: 'Toggle activation of a record'
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Delete',
											id: 'reportDeleteButton',
											scale: 'medium',
											width: '110px',
											iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
											disabled: true,
											handler: function () {
												scheduleReportDelete();
											},
											tooltip: 'Delete the record'
										}
									]
								}
							],
							listeners: {
								itemdblclick: function (grid, record, item, index, e, opts) {
									scheduleReportEdit();
								},
								selectionchange: function (grid, record, index, opts) {
									scheduledReportCheckNavButtons();
								}
							}
						})
					]
				});

				var scheduledReportCheckNavButtons = function () {
					var cnt = Ext.getCmp('scheduleReportsGrid').getSelectionModel().getCount();
					if (cnt === 1) {
						Ext.getCmp('reportEditButton').setDisabled(false);
						Ext.getCmp('reportActivateButton').setDisabled(false);
						Ext.getCmp('reportDeleteButton').setDisabled(false);

					} else if (cnt > 1) {
						Ext.getCmp('reportEditButton').setDisabled(true);
						Ext.getCmp('reportActivateButton').setDisabled(true);
						Ext.getCmp('reportDeleteButton').setDisabled(true);
					} else {
						Ext.getCmp('reportEditButton').setDisabled(true);
						Ext.getCmp('reportActivateButton').setDisabled(true);
						Ext.getCmp('reportDeleteButton').setDisabled(true);
					}
				};

				var scheduleReportRefreshGrid = function () {
					Ext.getCmp('scheduleReportsGrid').getStore().load({
						params: {
							status: Ext.getCmp('scheduleReportFilter-ActiveStatus').getValue() ? Ext.getCmp('scheduleReportFilter-ActiveStatus').getValue() : ''
						}
					});
				};

				var scheduleReportAdd = function () {
					scheduleReportWin(null, true);
				};

				var scheduleReportEdit = function () {
					scheduleReportWin(Ext.getCmp('scheduleReportsGrid').getSelection()[0], true);
				};

				var scheduleReportActivate = function () {
					var selectedObj = Ext.getCmp('scheduleReportsGrid').getSelection()[0];
					var reportId = selectedObj.data.scheduleReportId;
					var newUrl = '';
					var newMethod = '';
					if (selectedObj.data.activeStatus === 'A') {
						newUrl = 'api/v1/resource/scheduledreports/' + reportId;
						newMethod = 'DELETE';
					}
					else {
						newUrl = 'api/v1/resource/scheduledreports/' + reportId + '/activate';
						newMethod = 'POST';
					}
					Ext.getCmp('scheduleReportsGrid').setLoading(true);
					Ext.Ajax.request({
						url: newUrl,
						method: newMethod,
						success: function (response, opts) {
							Ext.getCmp('scheduleReportsGrid').setLoading(false);
							scheduleReportRefreshGrid();
						},
						failure: function (response, opts) {
							Ext.getCmp('scheduleReportsGrid').setLoading(false);
						}
					});
				};

				var scheduleReportDelete = function () {

					var selectedObj = Ext.getCmp('scheduleReportsGrid').getSelection()[0];

					Ext.Msg.show({
						title: 'Delete Scheduled Report?',
						iconCls: 'fa fa-lg fa-warning icon-small-vertical-correction',
						message: 'Are you sure you want to delete the scheduled report?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								Ext.getCmp('scheduleReportsGrid').setLoading(true);
								var reportId = selectedObj.data.scheduleReportId;

								Ext.Ajax.request({
									url: 'api/v1/resource/scheduledreports/' + reportId + '/force',
									method: 'DELETE',
									success: function (response, opts) {
										Ext.getCmp('scheduleReportsGrid').setLoading(false);
										scheduleReportRefreshGrid();
									},
									failure: function (response, opts) {
										Ext.getCmp('scheduleReportsGrid').setLoading(false);
									}
								});
							}
						}
					});
				};


				var scheduleReportWin = function (scheduleData, reoccuring) {
					var scheduleReportId = null;
					//
					//This is for editing schedule report
					//
					if (scheduleData) {
						scheduleReportId = scheduleData.data.scheduleReportId;
					}

					//
					//  This formats the emails separated by ';' strng into an array
					//
					var createEmailAddressesList = function (emailStr) {
						if (emailStr === '' || typeof emailStr === 'undefined') {
							return null;
						}
						var emailArr = [];
						var eArr = String(emailStr).trim().split(';');
						for (ctr = 0; ctr < eArr.length; ctr++) {
							var tmpStr = String(eArr[ctr]).trim();
							if (tmpStr !== '') {
								emailArr.push({email: tmpStr});
							}
						}
						return emailArr;
					};

					var emailsArrayToString = function (emailArr) {
						if (emailArr === undefined) {
							return '';
						}
						var emailStr = '';
						for (ctr = 0; ctr < emailArr.length; ctr++) {
							emailStr += emailArr[ctr].email + "; ";
						}
						return emailStr;
					};

					//
					//  This is for the one time report run
					//
					var generateReport = function (data) {

						Ext.getCmp('scheduleReportForm').setLoading(true);

						CoreUtil.submitForm({
							url: 'api/v1/resource/reports',
							method: 'POST',
							data: data,
							removeBlankDataItems: true,
							form: Ext.getCmp('scheduleReportForm'),
							success: function (response, opts) {
								Ext.toast('Submitted report request.', '', 'tr');

								Ext.getCmp('scheduleReportWin').close();
								historyRefreshGrid();
							},
							failure: function (response, opts) {
								Ext.toast('Failed to submit report generation request.', '', 'tr');
							}
						});
					};

					//
					//   This is to schedule a report to run daily, monthly etc.
					//
					var scheduleReport = function (data) {

						Ext.getCmp('scheduleReportForm').setLoading(true);
						var url = '';
						var method = '';
						if (scheduleReportId) {
							url = 'api/v1/resource/scheduledreports/' + scheduleReportId;
							method = 'PUT';
						}
						else {
							url = 'api/v1/resource/scheduledreports';
							method = 'POST';
						}

						CoreUtil.submitForm({
							url: url,
							method: method,
							data: data,
							removeBlankDataItems: true,
							form: Ext.getCmp('scheduleReportForm'),
							success: function (response, opts) {
								Ext.toast('Saved Successfully', '', 'tr');

								Ext.getCmp('scheduleReportForm').setLoading(false);
								Ext.getCmp('scheduleReportForm').destroy();

								Ext.getCmp('scheduleReportWin').destroy();
								Ext.getCmp('scheduleReportsGrid').getStore().load();
							},
							failure: function (response, opts) {

								Ext.toast('Failed to Save', '', 'tr');
								Ext.getCmp('scheduleReportForm').setLoading(false);
							}
						});
					};

					//
					//  This is the store list for the Report Types
					//
					var reportTypesStore = Ext.create('Ext.data.Store', {
						id: 'reportTypesStore',
						autoLoad: true,
						pageSize: 100,
						remoteSort: true,
						listeners: {
							endupdate: function (opts) {
								if (scheduleReportId !== null) {
									//Edit data
									Ext.getCmp('reportType').setValue(scheduleData.data.reportType);
									Ext.getCmp('reportFormat').setValue(scheduleData.data.reportFormat);
									Ext.getCmp('scheduledHours').setValue(String(scheduleData.data.scheduleIntervalDays));
									Ext.getCmp('emailAddresses').setValue(emailsArrayToString(scheduleData.data.emailAddresses));

									if (scheduleData.data.reportOption.category) {
										Ext.getCmp('categorySelect').setValue(scheduleData.data.reportOption.category);
									}
									if (scheduleData.data.reportOption.assignedUser) {
										Ext.getCmp('assignedUser').setValue(scheduleData.data.reportOption.assignedUser);
									}
									if (scheduleData.data.reportOption.assignedGroup) {
										Ext.getCmp('assignedGroup').setValue(scheduleData.data.reportOption.assignedGroup);
									}
									if (scheduleData.data.reportOption.maxWaitSeconds) {
										Ext.getCmp('waitSeconds').setValue(scheduleData.data.reportOption.maxWaitSeconds);
									}
									if (scheduleData.data.reportOption.startDts) {
										Ext.getCmp('startDate').setValue(new Date(scheduleData.data.reportOption.startDts));
										Ext.getCmp('endDate').setValue(new Date(scheduleData.data.reportOption.endDts));

									}
									if (scheduleData.data.reportOption.previousDays) {
										Ext.getCmp('previousDaysSelect').setValue(scheduleData.data.reportOption.previousDays);
									}
								}
							}
						},
						sorters: [
							new Ext.util.Sorter({
								property: 'description',
								direction: 'DESC'
							})
						],
						proxy: CoreUtil.pagingProxy({
							url: 'api/v1/resource/reports/reporttypes',
							method: 'GET',
							reader: {
								type: 'json',
								rootProperty: '',
								totalProperty: ''
							}
						})
					});

					//
					//  This is the store list for the Report Formats
					//
					var reportFormatsStore = Ext.create('Ext.data.Store', {
						id: 'reportFormatsStore',
						autoLoad: false,
						sorters: [
							new Ext.util.Sorter({
								property: 'description',
								direction: 'DESC'
							})
						],
						proxy: CoreUtil.pagingProxy({
							url: '',
							method: 'GET',
							reader: {
								type: 'json',
								rootProperty: '',
								totalProperty: ''
							}
						})
					});

					//
					//  This is the store list for the Report Options
					//
					var scheduleOptionsStore = Ext.create('Ext.data.Store', {
						id: 'scheduleOptionsStore',
						autoLoad: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'description',
								direction: 'ASC'
							})
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/components/lookup'
						}
					});

					//
					//  This is the store list for the catagories
					//
					var scheduleCategoryStore = Ext.create('Ext.data.Store', {
						id: 'scheduleCategoryStore',
						autoLoad: true,
						pageSize: 100,
						remoteSort: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'description',
								direction: 'ASC'
							})
						],
						proxy: CoreUtil.pagingProxy({
							url: 'api/v1/resource/attributes/attributetypes',
							method: 'GET',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: ''
							}
						})
					});

					//
					//  This is the store list for the how often to schedule report combo
					//
					var scheduleHowOften = Ext.create('Ext.data.Store', {
						fields: ['code', 'description'],
						data: [
							{"code": "0", "description": "Now (One Time Only)"},
							{"code": "1", "description": "Daily"},
							{"code": "7", "description": "Weekly"},
							{"code": "28", "description": "Monthly"}
						]
					});


					//
					//  This is the store list for the Previous Days combo
					//
					var days = [];
					days.push({
						code: null,
						days: 'Select'
					});
					for (var i = 1; i<29; i++) {
						days.push({
							code: '' + i,
							days: '' + i
						});
					}
					var previousDaysStore = Ext.create('Ext.data.Store', {
						id: 'previousDaysStore',
						fields: ['code', 'days'],
						data: days
					});


					var handleReportOptions = function() {
						//Hide and clear data from all the form elements at first and turn them on based on what rtype is selected
						Ext.getCmp('filterForEntries').setHidden(true);
						Ext.getCmp('scheduleOptionsGrid').setHidden(true);
						Ext.getCmp('detailReportCategories').setHidden(true);
						Ext.getCmp('detailReportCol4').setHidden(true);
						Ext.getCmp('categorySelect').setHidden(true);
						Ext.getCmp('waitSeconds').setHidden(true);
						Ext.getCmp('startDate').setHidden(true);
						Ext.getCmp('endDate').setHidden(true);
						Ext.getCmp('previousDaysSelect').setHidden(true);
						Ext.getCmp('assignedUser').setHidden(true);
						Ext.getCmp('assignedGroup').setHidden(true);

						Ext.getCmp('waitSeconds').setValue('');
						Ext.getCmp('filterForEntries').setValue('');
						Ext.getCmp('scheduleOptionsGrid').getSelectionModel().clearSelections();
						var dt = new Date();
						Ext.getCmp('startDate').setValue(dt);
						Ext.getCmp('endDate').setValue(dt);
						Ext.getCmp('previousDaysSelect').clearValue();

						var rType = Ext.getCmp('reportType').value;

						if (rType === "COMPONENT" || rType === 'CMPORG' || rType === 'TYPECOMP') {

							Ext.getCmp('filterForEntries').setHidden(false);
							Ext.getCmp('scheduleOptionsGrid').setHidden(false);
							if (rType === 'TYPECOMP') {
								Ext.getCmp('detailReportCategories').setHidden(false);
								Ext.getCmp('detailReportCol4').setHidden(false);
							}
						}
						else if (rType === 'CATCOMP') {
							Ext.getCmp('categorySelect').setHidden(false);
						}
						else if (rType === 'LINKVALID') {

							Ext.getCmp('waitSeconds').setHidden(false);
						}
						else if (rType === 'SUBMISSION' || rType === 'USAGE') {

							Ext.getCmp('startDate').setHidden(false);
							Ext.getCmp('endDate').setHidden(false);
							Ext.getCmp('previousDaysSelect').setHidden(false);
						}
						else if (rType === 'EVALSTAT') {
							Ext.getCmp('assignedUser').setHidden(false);
							Ext.getCmp('assignedGroup').setHidden(false);
						}
						else if (rType === 'USER' || rType === 'ORGANIZATION') {
							//Do nothing just the base form which is already active.
						}
					};

					//
					//  scheduleReportWin
					//  The popup window to schedule are report to run now or on a set repeating schedule
					//
					//
					Ext.create('Ext.window.Window', {
						title: 'Schedule Report',
						id: 'scheduleReportWin',
						iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
						width: 700,
						minHeight: 500,
						y: 100,
						closeAction: 'destroy',
						modal: true,
						alwaysOnTop: true,
						layout: 'fit',
						items: [{
								xtype: 'form',
								id: 'scheduleReportForm',
								layout: 'vbox',
								scrollable: true,
								bodyStyle: 'padding: 10px;',
								submitEmptyText: false,
								defaults: {
									labelAlign: 'top'
								},
								dockedItems: [
									{
										dock: 'bottom',
										xtype: 'toolbar',
										items: [
											{
												text: 'Run Report',
												formBind: true,
												iconCls: 'fa fa-lg fa-bolt icon-button-color-run',
												handler: function () {

													var data = {};
													var reportOpt = {};

													data.reportType = Ext.getCmp('reportType').getValue();
													data.reportFormat = Ext.getCmp('reportFormat').getValue();
													data.reportOption = null;
													data.emailAddresses = createEmailAddressesList(Ext.getCmp('emailAddresses').getValue());
													data.scheduleIntervalDays = Ext.getCmp('scheduledHours').getValue();

													if (data.scheduleIntervalDays === '0')
													{
														data.scheduleIntervalDays = null;
													}

													if (Ext.getCmp('categorySelect').isVisible()) {
														reportOpt.category = Ext.getCmp('categorySelect').getValue();
													}
													if (Ext.getCmp('assignedUser').isVisible()) {
														reportOpt.assignedUser = Ext.getCmp('assignedUser').getValue();
													}
													if (Ext.getCmp('assignedGroup').isVisible()) {
														reportOpt.assignedGroup = Ext.getCmp('assignedGroup').getValue();
													}

													if (Ext.getCmp('startDate').isVisible()) {

														reportOpt.startDts = Ext.Date.format(Ext.getCmp('startDate').getValue(), 'Y-m-d\\TH:i:s.u');

														var endDate = Ext.getCmp('endDate').getValue();
														if (endDate) {
															endDate = Ext.Date.add(endDate, Ext.Date.DAY, 1);
															endDate = Ext.Date.subtract(endDate, Ext.Date.MILLI, 1);
														}
														reportOpt.endDts = Ext.Date.format(endDate, 'Y-m-d\\TH:i:s.u');
														reportOpt.previousDays = Ext.getCmp('previousDaysSelect').getValue();
													}

													if (Ext.getCmp('waitSeconds').isVisible()) {
														reportOpt.maxWaitSeconds = Ext.getCmp('waitSeconds').getValue();
													}

													data.reportOption = reportOpt;

													// retrieve each report category flag
													for (var ii = 1; ii < 5; ii += 1) {
														var detailCats = Ext.getCmp('detailReportCol' + ii).items.items;
														for (var jj = 0; jj < detailCats.length; jj += 1) {

															// mold the name of each value to match that of the API
															data.reportOption[detailCats[jj].id] = detailCats[jj].value;
														}
													}

													// make a list of the selected entries to run a report on
													var ids = [];
													var reportGrid = Ext.getCmp('scheduleOptionsGrid');
													if (reportGrid.isVisible()) {
														var gridSelections = reportGrid.getSelection();
														for (var ii = 0; ii < gridSelections.length; ii++) {
															ids.push({id: gridSelections[ii].data.code});
														}
													}

													// if the report is NOT scheduled
													if (data.scheduleIntervalDays === null)
													{
														generateReport({
															reportDataId: ids,
															report: data
														});
													}
													// if the report IS scheduled
													else {
														data.componentIds = ids;
														scheduleReport(data);
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
													Ext.getCmp('scheduleReportWin').destroy();
												}
											}
										]
									}
								],
								items: [
									{
										xtype: 'combobox',
										name: 'reportType',
										id: 'reportType',
										fieldLabel: 'Choose Report Type<span class="field-required" />',
										width: '100%',
										maxLength: 50,
										store: reportTypesStore,
										displayField: 'description',
										valueField: 'code',
										editable: false,
										allowBlank: false,
										listeners: {
											change: function (cb, newVal, oldVal, opts) {
												Ext.getCmp('reportFormat').getStore().removeAll();
												Ext.getCmp('reportFormat').clearValue();
												Ext.getCmp('reportFormat').getStore().getProxy().setUrl('api/v1/resource/reports/' + encodeURIComponent(newVal) + '/formats');
												Ext.getCmp('reportFormat').getStore().load({
													callback: function (records, operation, success) {
														if (records.length === 1) {
															Ext.getCmp('reportFormat').setValue(records[0].data.code);
														}
													}
												});

												if (reoccuring) {
													Ext.getCmp('scheduledHours').setValue('1');
												} else {
													Ext.getCmp('scheduledHours').setValue('0');
												}
												Ext.getCmp('reportFormat').setHidden(false);
												Ext.getCmp('scheduledHours').setHidden(false);
												//Ext.getCmp('emailAddresses').setHidden(true);

												handleReportOptions();
											}
										}


									},
									{
										xtype: 'combobox',
										name: 'reportFormat',
										id: 'reportFormat',
										fieldLabel: 'Choose Report Format<span class="field-required" />',
										width: '100%',
										maxLength: 50,
										store: reportFormatsStore,
										displayField: 'description',
										valueField: 'code',
										editable: false,
										hidden: true,
										allowBlank: false
									},
									{
										xtype: 'combobox',
										name: 'scheduledHours',
										id: 'scheduledHours',
										fieldLabel: 'How often to run the report?<span class="field-required" />',
										width: '100%',
										maxLength: 50,
										store: scheduleHowOften,
										displayField: 'description',
										valueField: 'code',
										editable: false,
										hidden: true,
										allowBlank: false,
										listeners: {
											change: function (cb, newVal, oldVal, opts) {
												var emailTA=Ext.getCmp('emailAddresses');
												if (oldVal !== null && newVal === '0' && scheduleReportId) {
													Ext.toast('You cannot run that report now, you are editing a scheduled report. Click the Add button to run a report now.');
													Ext.getCmp('scheduledHours').setValue(String(scheduleData.data.scheduleIntervalDays));
													return;
												} else if (newVal !== '0') {

													Ext.getCmp('filterForEntries').setHidden(true);
													Ext.getCmp('emailAddresses').setHidden(false);
													handleReportOptions();

												}
												else {
													Ext.getCmp('filterForEntries').setHidden(false);
													Ext.getCmp('emailAddresses').setHidden(true);
													handleReportOptions();

												}
											}
										}
									},
									{
										xtype: 'textarea',
										name: 'emailAddresses',
										id: 'emailAddresses',
										fieldLabel: 'Enter email addresses separated by semi-colons<br>(To recieve a notification when the report is ready.)',
										width: '100%',
										maxLength: 300,
										editable: true,
										hidden: true,
										allowBlank: true
									},
									{
										xtype: 'combobox',
										name: 'categorySelect',
										id: 'categorySelect',
										fieldLabel: 'Select Category<span class="field-required" />',
										width: '100%',
										maxLength: 50,
										store: scheduleCategoryStore,
										displayField: 'description',
										valueField: 'attributeType',
										editable: false,
										hidden: true,
										allowBlank: true
									},
									{
										xtype: 'numberfield',
										name: 'waitSeconds',
										id: 'waitSeconds',
										fieldLabel: 'Enter how many seconds to wait (default: 5 sec, (1 - 300 seconds))',
										width: '100%',
										maxLength: 3,
										minValue: 1,
										maxValue: 300,
										value: '5',
										editable: true,
										hidden: true,
										allowBlank: true,
										style: {
											marginTop: '20px'
										}
									},
									{
										xtype: 'datefield',
										name: 'startDate',
										id: 'startDate',
										fieldLabel: 'Start Date (Blank = Current Day)',
										width: '100%',
										format: 'm/d/Y',
										submitFormat: 'Y-m-d\\TH:i:s.u',
										editable: true,
										hidden: true,
										allowBlank: true,
										style: {
											marginTop: '20px'
										}
									},
									{
										xtype: 'datefield',
										name: 'endDate',
										id: 'endDate',
										fieldLabel: 'End Date (Blank = Current Day)',
										width: '100%',
										format: 'm/d/Y',
										editable: true,
										hidden: true,
										allowBlank: true
									},
									{
										xtype: 'combobox',
										name: 'previousDaysSelect',
										id: 'previousDaysSelect',
										fieldLabel: 'Previous Days',
										width: '100%',
										maxLength: 50,
										store: previousDaysStore,
										displayField: 'days',
										valueField: 'code',
										editable: false,
										hidden: true,
										allowBlank: true,
										listeners: {
											change: function(cb, newValue, oldValue, opts){
												if (newValue){
													Ext.getCmp('startDate').setValue(null);
													Ext.getCmp('endDate').setValue(null);
													Ext.getCmp('startDate').setDisabled(true);
													Ext.getCmp('endDate').setDisabled(true);
												} else {
													Ext.getCmp('startDate').setDisabled(false);
													Ext.getCmp('endDate').setDisabled(false);
												}
											}
										}
									},
									{
										xtype: 'combobox',
										id: 'assignedUser',
										name: 'assignedUser',
										fieldLabel: 'Assigned User',
										displayField: 'description',
										valueField: 'code',
										emptyText: 'All',
										labelAlign: 'top',
										width: '100%',
										typeAhead: true,
										editable: true,
										hidden: true,
										forceSelection: true,
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/userprofiles/lookup'
											},
											listeners: {
												load: function(store, records, opts) {
													store.add({
														code: null,
														description: 'All'
													});
												}
											}
										}
									},
									{
										xtype: 'combobox',
										id: 'assignedGroup',
										name: 'assignedGroup',
										fieldLabel: 'Assign to Group',
										displayField: 'description',
										valueField: 'code',
										emptyText: 'All',
										labelAlign: 'top',
										width: '100%',
										hidden: true,
										editable: false,
										forceSelection: true,
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/securityroles/lookup'
											},
											listeners: {
												load: function(store, records, opts) {
													store.add({
														code: null,
														description: 'All'
													});
												}
											}
										}
									},
									{
										xtype: 'fieldcontainer',
										fieldLabel: 'Included Report Categories',
										id: 'detailReportCategories',
										hidden: true,
										width: '100%',
										items: [
											{
												layout: 'column',
												items: [
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',
														id: 'detailReportCol1',
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'Description',
																inputValue: '1',
																id: 'displayDescription',
																name: 'description',
																value: true
															},
															{
																boxLabel: 'Contacts',
																inputValue: '1',
																id: 'displayContacts',
																name: 'contacts',
																value: true
															},
															{
																boxLabel: 'Resources',
																inputValue: '1',
																id: 'displayResources',
																name: 'resources',
																value: true
															},
															{
																boxLabel: 'Vitals',
																inputValue: '1',
																id: 'displayVitals',
																name: 'vitals',
																value: true
															}
														]
													},
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',
														id: 'detailReportCol2',
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'Dependencies',
																inputValue: '1',
																id: 'displayDependencies',
																name: 'dependencies',
																value: true
															},
															{
																boxLabel: 'Relationships',
																inputValue: '1',
																id: 'displayRelationships',
																name: 'relationships',
																value: true
															},
															{
																boxLabel: 'Tags',
																inputValue: '1',
																id: 'displayTags',
																name: 'tags',
																value: true
															},
															{
																boxLabel: 'Organization Data',
																inputValue: '1',
																id: 'displayOrgData',
																name: 'orgData',
																value: true,
																inputAttrTpl: 'data-qtip=Title,&nbsp;organization,&nbsp;etc.'
															}
														]
													},
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',
														id: 'detailReportCol3',
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'All Evaluation Versions',
																inputValue: '1',
																id: 'displayEvalVersions',
																name: 'evalVersions',
																inputAttrTpl: 'data-qtip=An&nbsp;evaluation&nbsp;category&nbsp;type&nbsp;must&nbsp;be&nbsp;specified'
															},
															{
																boxLabel: 'Reviews',
																inputValue: '1',
																id: 'displayReportReviews',
																name: 'reportReviews'
															},
															{
																boxLabel: 'Q/A',
																inputValue: '1',
																id: 'displayQA',
																name: 'QA'
															}
														]
													}
												]
											}
										]
									},
									{
										xtype: 'fieldcontainer',
										defaultType: 'radiofield',
										fieldLabel: 'Included Evaluation Category Type',
										id: 'detailReportCol4',
										hidden: true,
										width: '100%',
									    defaults: {
									        columnWidth: 0.32,
									        inputValue: '1'
									    },
									    layout: 'column',
										items: [
											{
												boxLabel: 'Evaluation Summary',
												name: 'evaluationType',
												id: 'displayEvalSummary',
												inputAttrTpl: 'data-qtip=Condensed&nbsp;evaluation&nbsp;overview',
												value: true
											},
											{
												boxLabel: 'Evaluation Details',
												name: 'evaluationType',
												inputAttrTpl: 'data-qtip=Detailed&nbsp;evaluation&nbsp;analysis',
												id: 'displayEvalDetails'
											},
											{
												boxLabel: 'None',
												inputAttrTpl: 'data-qtip=Exclude&nbsp;evaluations&nbsp;from&nbsp;this&nbsp;report',
												name: 'evaluationType'
											}
										]
									},
									{
										xtype: 'gridpanel',
										title: 'Restrict By Entry',
										id: 'scheduleOptionsGrid',
										store: 'scheduleOptionsStore',
										width: '100%',
										maxHeight: 250,
										columnLines: true,
										margin: '10 0 0 0',
										bodyCls: 'border_accent',
										selModel: {
											selType: 'checkboxmodel'
										},
										plugins: 'gridfilters',
										columns: [
											{text: 'Entry Name', dataIndex: 'description', flex: 1,
												filter: {
													type: 'string'
												}
											}
										],
										dockedItems: [
											{
												xtype: 'textfield',
												dock: 'top',
												name: 'filterForEntries',
												id: 'filterForEntries',
												emptyText: 'Filter entries by name',
												width: '100%',
												maxLength: 30,
												listeners: {
													change: function (tb, newVal, oldVal, opts) {
														Ext.getCmp('scheduleOptionsGrid').getStore().filter([
															{
																property: 'description',
																value: tb.value
															}
														]);
													}
												}
											}
										],
										hidden: true
									}

								]
							}]
					}).show();

				};


				var historyGridStore = Ext.create('Ext.data.Store', {
					id: 'historyGridStore',
					autoLoad: true,
					pageSize: 100,
					remoteSort: true,
					fields: [
						{
							name: 'createDts',
							type: 'date',
							dateFormat: 'c'
						}
					],
					sorters: [
						new Ext.util.Sorter({
							property: 'createDts',
							direction: 'DESC'
						})
					],
					proxy: CoreUtil.pagingProxy({
						url: 'api/v1/resource/reports',
						method: 'GET',
						reader: {
							type: 'json',
							rootProperty: 'data',
							totalProperty: 'totalNumber'
						}
					})
				});

				var historyGrid = Ext.create('Ext.grid.Panel', {
					id: 'historyGrid',
					title: 'Reports &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="System scheduled and hard reports" ></i>',
					store: historyGridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					selModel: {
						selType: 'checkboxmodel'
					},
					bufferedRenderer: false,
					columns: [
						{text: 'Report Type', dataIndex: 'reportType', width: 200,
							renderer: function (value, meta, record) {
								return record.get('reportTypeDescription');
							}
						},
						{text: 'Format', dataIndex: 'reportFormat', width: 250,
							renderer: function (value, meta, record) {
								return record.get('reportFormatDescription');
							}
						},
						{text: 'Run Status', dataIndex: 'runStatus', width: 150,
							renderer: function (value, meta, record) {
								if (value === 'E') {
									meta.tdCls = 'alert-danger';
								} else if (value === 'W') {
									meta.tdCls = 'alert-warning';
								} else {
									meta.tdCls = '';
								}
								return record.get('runStatusDescription');
							}
						},
						{text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
						{text: 'Create User', dataIndex: 'createUser', width: 150},
						{
							text: '<span data-qtip="Days until report is removed from the system">Days Until Cleanup</span>', 
							dataIndex: 'remainingReportLifetime', 
							width: 150,
							sortable: false, renderer: function (value, meta, record) {

								// Defined status color, info, and sybmol
								var maxHue = 85;
								var statusColor = (record.data.remainingReportLifetime/record.data.reportLifetimeMax)*maxHue;
								var statusInfo = ['This report will be removed soon', 'This report still has time before cleanup', 'This report was recently created'];
								var statusSymbol = ['fa fa-exclamation-circle', 'fa fa-exclamation-triangle', 'fa fa-check-circle'];
								var statusIndex = Math.floor(record.data.remainingReportLifetime/record.data.reportLifetimeMax*(statusInfo.length-0.1));
								statusInfo = record.data.remainingReportLifetime == 0 ? 'This report will be removed' : statusInfo[statusIndex];
								statusSymbol = statusSymbol[statusIndex];

								// Set the value of the cell
								record.data.remainingReportLifetime = '<span data-qtip="' + statusInfo + '"> <i style="color: hsla(' + statusColor + ', 70%, 50%, 1.0);" class="' + statusSymbol + '" aria-hidden="true"></i> ' + record.data.remainingReportLifetime + '</span>';
								return record.data.remainingReportLifetime;
							} 
						},
						{text: 'Scheduled', dataIndex: 'scheduled', width: 100, align: 'center',
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{text: 'Options', dataIndex: 'reportOption', minWidth: 200, flex: 1, sortable: false, renderer: optionsRender }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									id: 'historyRefreshButton',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									tip: 'Refresh the list of records',
									handler: function () {
										historyRefreshGrid();
									},
									tooltip: 'Refresh the list of records'
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'New Report',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon',
									scale: 'medium',
									handler: function () {
										scheduleReportWin();
									}
								},
								{
									text: 'View',
									scale: 'medium',
									id: 'historyViewButton',
									width: '100px',
									iconCls: 'fa fa-2x fa-eye icon-button-color-view icon-vertical-correction-view',
									disabled: true,
									handler: function () {
										viewHistory();
									},
									tooltip: 'View Report'
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Scheduled Reports',
									id: 'scheduledReportBtn',
									hidden: true,
									iconCls: 'fa fa-2x fa-clock-o icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									handler: function () {
										scheduledReportsWin.show();
									},
									tooltip: 'Schedule Reports'
								},
								{
									text: 'Download',
									id: 'historyExportButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-download icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										historyExport();
									},
									tooltip: 'Export report'
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									id: 'historyDeleteButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
									disabled: true,
									handler: function () {
										historyDelete();
									},
									tooltip: 'Delete the record'
								}
							]
						},
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: historyGridStore,
							displayInfo: true
						}
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							viewHistory();
						},
						selectionchange: function (grid, record, index, opts) {
							historyCheckNavButtons();
						}
					}
				});

				addComponentToMainViewPort(historyGrid);

				// Actions

				var historyCheckNavButtons = function () {
					var cnt = historyGrid.getSelectionModel().getCount();
					if (cnt === 1) {
						var record = historyGrid.getSelectionModel().getSelection()[0];
						if (record.get('runStatus') !== 'C') {
							Ext.getCmp('historyViewButton').setDisabled(true);
							Ext.getCmp('historyExportButton').setDisabled(true);
						} else {
							Ext.getCmp('historyViewButton').setDisabled(false);
							Ext.getCmp('historyExportButton').setDisabled(false);
						}

						if (record.get('runStatus') !== 'W') {
							Ext.getCmp('historyDeleteButton').setDisabled(false);
						} else {
							Ext.getCmp('historyDeleteButton').setDisabled(true);
						}

					} else if (cnt > 1) {
						Ext.getCmp('historyDeleteButton').setDisabled(false);
						Ext.getCmp('historyViewButton').setDisabled(true);
						Ext.getCmp('historyExportButton').setDisabled(true);
					} else {
						Ext.getCmp('historyViewButton').setDisabled(true);
						Ext.getCmp('historyDeleteButton').setDisabled(true);
						Ext.getCmp('historyExportButton').setDisabled(true);
					}
				};


				//
				//  HISTORY VIEW WINDOW CSV, HTML, or PDF
				//
				//
				var historyViewWin = function () {


					var contentData ='';
					var historyTitle='';
					var setHistoryContentData = function(){

						var selectedObj = Ext.getCmp('historyGrid').getSelection()[0];
						var formattedDate = Ext.util.Format.date(selectedObj.data.createDts,'m/d/y H:i:s');
						historyTitle="View Report Data - "+selectedObj.data.reportTypeDescription +' '+formattedDate;
						Ext.Ajax.request({
							url: 'api/v1/resource/reports/' + selectedObj.data.reportId + '/report',
							method: 'GET',
							success: function (response, opts) {
								var reportData = response.responseText;
								var reportFormat = selectedObj.data.reportFormat;
								if (reportFormat === 'text-html') {
									contentData = reportData;
								}
								else if (reportFormat === 'text-csv') {
									contentData = CoreUtil.csvToHTML(reportData);
								}
								else if (reportFormat === 'application-pdf') {
									contentData = '<object data="' + response.request.url + '?notAttach=true" type="application/pdf" style="width:100%; height:97%;">'
									+ 'Your browser does not support pdfs, <a href="' + response.request.url + '">click here to download the file.</a>'
									+ '</object>';
								}
								else{
									contentData = reportData;
								}
								var frame = Ext.getDom('contentFrame');
								frame.contentWindow.document.open();
								frame.contentWindow.document.write(contentData);
								frame.contentWindow.document.close();
							}
						});
					};
					setHistoryContentData();

					var actionPreviewNextRecord = function (next) {
						if (next) {
							Ext.getCmp('historyGrid').getSelectionModel().selectNext();
						} else {
							Ext.getCmp('historyGrid').getSelectionModel().selectPrevious();
						}

						var record = historyGrid.getSelectionModel().getSelection()[0];
						Ext.getCmp('previewWinTools-download').setDisabled(true);
						var formattedDate = Ext.util.Format.date(record.get('createDts'),'m/d/y H:i:s');
						Ext.getCmp('viewHistoryData').setTitle("View Report Data - "+record.get('reportTypeDescription') +' '+formattedDate);

						if (record.get('runStatus') === 'C') {
							Ext.getCmp('previewWinTools-download').setDisabled(false);
							setHistoryContentData();
						} else if (record.get('runStatus') === 'W') {
							Ext.getCmp('viewHistoryData').update("Generating...");
						} else if (record.get('runStatus') === 'E') {
							Ext.getCmp('viewHistoryData').update("Failed to generate.");
						}
						checkPreviewButtons();
					};

					var checkPreviewButtons = function () {
						if (Ext.getCmp('historyGrid').getSelectionModel().hasPrevious()) {
							Ext.getCmp('previewWinTools-previousBtn').setDisabled(false);
						} else {
							Ext.getCmp('previewWinTools-previousBtn').setDisabled(true);
						}

						if (Ext.getCmp('historyGrid').getSelectionModel().hasNext()) {
							Ext.getCmp('previewWinTools-nextBtn').setDisabled(false);
						} else {
							Ext.getCmp('previewWinTools-nextBtn').setDisabled(true);
						}
					};


					Ext.create('Ext.window.Window', {
						title: historyTitle,
						id: 'viewHistoryData',
						iconCls: 'fa fa-lg fa-eye',
						width: '50%',
						height: '60%',
						y: 40,
						closeAction: 'destroy',
						modal: true,
						maximizable: true,
						scrollable:true,
						layout: 'fit',
						bodyStyle: 'padding: 10px;',
						html: '<iframe id="contentFrame" style="width: 100%; height: 98%"></iframe>',
						dockedItems: [
						{
							dock: 'bottom',
							xtype: 'toolbar',
							items: [
								{
									xtype: 'button',
									text: 'Previous',
									id: 'previewWinTools-previousBtn',
									iconCls: 'fa fa-lg fa-arrow-left icon-button-color-default',
									handler: function () {
										actionPreviewNextRecord(false);
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',
									id: 'previewWinTools-download',
									text: 'Download',
									iconCls: 'fa fa-lg fa-download icon-button-color-default',
									handler: function () {
										historyExport();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'button',
									text: 'Next',
									id: 'previewWinTools-nextBtn',
									iconCls: 'fa fa-lg fa-arrow-right icon-button-color-default',
									iconAlign: 'right',
									handler: function () {
										actionPreviewNextRecord(true);
									}
								}
							]
						}]
					}).show();
				};


				var historyRefreshGrid = function () {
					Ext.getCmp('historyGrid').getStore().load();
				};

				var viewHistory = function () {
					var record = historyGrid.getSelectionModel().getSelection()[0];
					if (record.get('runStatus') === 'C') {
						historyViewWin();
					}
				};

				var historyDelete = function () {

					var selectedObj = Ext.getCmp('historyGrid').getSelection();

					if (selectedObj.length > 1) {
						var ids = [];

						for (ctr = 0; ctr < selectedObj.length; ctr++) {
							ids.push(selectedObj[ctr].data.reportId);
						}

						Ext.Msg.show({
							title: 'Delete Reports?',
							message: 'Are you sure you want to delete the selected reports?',
							buttons: Ext.Msg.YESNO,
							icon: Ext.Msg.QUESTION,
							fn: function (btn) {
								if (btn === 'yes') {
									Ext.getCmp('historyGrid').setLoading(true);

									Ext.Ajax.request({
										url: 'api/v1/resource/reports/delete',
										method: 'DELETE',
										jsonData: {entity: ids},
										headers: {'Accept': 'application/json, text/plain, */*', 'Content-Type': 'application/json;charset=UTF-8'},
										success: function (response, opts) {
											Ext.getCmp('historyGrid').setLoading(false);
											historyRefreshGrid();
										},
										failure: function (response, opts) {
											Ext.getCmp('historyGrid').setLoading(false);
										}
									});
								}
							}
						});
					}
					else {
						Ext.Msg.show({
							title: 'Delete Report?',
							message: 'Are you sure you want to delete the selected report?',
							buttons: Ext.Msg.YESNO,
							icon: Ext.Msg.QUESTION,
							fn: function (btn) {
								if (btn === 'yes') {
									Ext.getCmp('historyGrid').setLoading(true);
									var reportId = selectedObj[0].data.reportId;

									Ext.Ajax.request({
										url: 'api/v1/resource/reports/' + reportId,
										method: 'DELETE',
										success: function (response, opts) {
											Ext.getCmp('historyGrid').setLoading(false);
											historyRefreshGrid();
										},
										failure: function (response, opts) {
											Ext.getCmp('historyGrid').setLoading(false);
										}
									});
								}
							}
						});
					}
				};

				var historyExport = function () {
					Ext.toast('Exporting Report Data ...');
					var selectedObj = Ext.getCmp('historyGrid').getSelection()[0].data;
					window.location.href = 'api/v1/resource/reports/' + selectedObj.reportId + '/report';
				};

				CoreService.userservice.getCurrentUser().then(function(user){
					if (CoreService.userservice.userHasPermisson(user, "REPORTS-SCHEDULE")) {
						Ext.getCmp('scheduledReportBtn').setHidden(false);
					}
				});





			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
