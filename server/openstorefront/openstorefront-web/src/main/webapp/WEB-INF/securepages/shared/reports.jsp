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
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../layout/${actionBean.headerPage}">
		</stripes:layout-render>

        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {

				var optionsRender = function(v, meta, record) {
					var recordDataNum = 0;
					var details = '';

					if (v) {
						if (v.category) {
							details += 'Category: ' + v.category + '<br />';
							recordDataNum += 1;
						}
						if (v.startDts) {
							if (v.startDts) {
								details = details + 'Start Date: ' + Ext.util.Format.date(v.startDts, 'm/d/y H:i:s') + '<br>';
								recordDataNum += 1;
							}
							if (v.endDts) {
								details = details + 'End Date: ' + Ext.util.Format.date(v.endDts, 'm/d/y H:i:s') + '<br>';
								recordDataNum += 1;
							}
							// return details;
						}
						if (v.previousDays) {
							details += 'Previous Days: ' + v.previousDays + '<br />';
							recordDataNum += 1;
						}
						if (v.maxWaitSeconds) {
							details += 'Max Wait Seconds: ' + v.maxWaitSeconds + '<br />';
							recordDataNum += 1;
						}
						if (record.data.reportType === 'TYPECOMP') {
							var options = record.data.reportOption;
							for (var key in options) {
								if (key !== 'storageVersion') {
									details += key + ': ' + options[key] + '<br />';
									recordDataNum += 1;
								}
							}
						}
						
						var classList = '';

						if (recordDataNum > 2) {
							classList = 'expandable-grid-cell-collapsed';
						}
						classList += ' expandable-grid-cell';
						return '<div class="' + classList + '" data-num="' + recordDataNum + '">' + details + '</div>';
					}
					return '';
				};
				
				var outputOptionRender = function(v, meta, record) {
					var outputs = '';
								
					if (record.data.reportOutputs) {
						var outputOpts = [];
						Ext.Array.each(record.data.reportOutputs, function(item){	
							outputOpts.push('<b>' + item.reportTransmissionType + '</b>');
							Ext.Object.each(item.reportTransmissionOption, function(key, value, myself) {
								if (key !== 'storageVersion') {
									if (key === 'emailAddresses') {										
										var allEmails = [];
										Ext.Array.each(value, function(email){
											allEmails.push(email.email);
										});
										
										outputOpts.push('<b>' + key + '</b>: <br>' + allEmails.join('<br>'));
									} else {
										outputOpts.push('<b>' + key + '</b>: ' + value);
									}
								}
							});
							outputOpts.push('<br>');
						});
						outputs += outputOpts.join('<br> ');
					}
					return outputs;
				};
				
				var formatRender = function(value, meta, record) {

					if (record.get('noViewAvaliable')) {
						return 'No View Available';
					} else {
						return record.get('reportFormatDescription');
					}
					
				};
				

				var scheduleReportsGridStore = Ext.create('Ext.data.Store', {
					id: 'scheduleReportsGridStore',
					autoLoad: true,
					pageSize: 100,
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
						},
						{
							name: 'schedule', mapping: function(data) {	
								var days = data.scheduleIntervalDays;
								if (data.scheduleIntervalDays) {
									if (days === 1) {
										return 'Daily';
									}
									else if (days == 7) {
										return 'Weekly';
									}
									else if (days == 28) {
										return 'Monthly' ;
									} else {
										return 'Every ' + days + ' days' 
									}
								} else if (data.scheduleIntervalMinutes) {
									return 'Every ' + data.scheduleIntervalMinutes + ' minutes';
								} else if (data.cronDescription) {
									return data.cronDescription;
								}								
							}
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
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								activeStatus: Ext.getCmp('scheduleReportFilter-ActiveStatus').getValue(),
								reportType: Ext.getCmp('scheduleReportFilter-reportType').getValue() ? Ext.getCmp('scheduleReportFilter-reportType').getValue() : null,								
								showAllUsers: Ext.getCmp('scheduleReportFilter-showAll').getValue() 
							};
						}
					}					
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
								{text: 'Id', dataIndex: 'scheduleReportId', width: 250, hidden: true },
								{text: 'Report Type', dataIndex: 'reportType', width: 200,
									renderer: function (value, meta, record) {
										return record.get('reportTypeDescription');
									}
								},
								{text: 'Format', dataIndex: 'reportFormat', width: 250,
									renderer: formatRender
								},
								{text: 'Create User', dataIndex: 'createUser', width: 150},
								{text: 'Scheduled Interval', dataIndex: 'schedule', width: 200 },
								{text: 'Last Run Date', dataIndex: 'lastRanDts', width: 170, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
								{text: 'Output Options', dataIndex: 'reportOutputs', minWidth: 200, flex: 1, sortable: false,
									renderer: outputOptionRender
								},
								{text: 'Options', dataIndex: 'reportOption', minWidth: 200, flex: 1, sortable: false,
									renderer: optionsRender
								},
								{text: 'Active Status', dataIndex: 'activeStatus', width: 125, align: 'center',
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
											labelAlign: 'left',
											width: 250,
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
										}),
										Ext.create('OSF.component.StandardComboBox', {
											id: 'scheduleReportFilter-reportType',
											emptyText: 'All',
											fieldLabel: 'Report Type',
											labelAlign: 'left',
											name: 'reportType',
											typeAhead: false,
											editable: false,
											width: 325,
											margin: '0 20 0 0',
											listeners: {
												change: function(filter, newValue, oldValue, opts){
													scheduleReportRefreshGrid();
												}
											},
											storeConfig: {
												url: 'api/v1/resource/lookuptypes/ReportType',
												addRecords: [
													{
														code: null,
														description: 'All'
													}
												]
											}
										}),										
										{
											xtype: 'checkbox',
											id: 'scheduleReportFilter-showAll',
											padding: '0 20 0 0',
											requiredPermissions: ['REPORTS-ALL'],
											listeners: {
												change: function(filter, newValue, oldValue, opts){
													scheduleReportRefreshGrid();
												}
											},
											boxLabel: 'Show All Users',
											name: 'showAllUsers'									
										}	
									]
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
											xtype: 'tbseparator',
											requiredPermissions: ['REPORTS-SCHEDULE-CREATE']
										},
										{
											text: 'Add',
											id: 'reportAddButton',
											scale: 'medium',
											width: '100px',
											iconCls: 'fa fa-2x fa-plus icon-button-color-save icon-vertical-correction',
											disabled: false,
											requiredPermissions: ['REPORTS-SCHEDULE-CREATE'],
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
											requiredPermissions: ['REPORTS-SCHEDULE-UPDATE'],
											handler: function () {
												scheduleReportEdit();
											},
											tooltip: 'Edit a record'
										},
										{
											xtype: 'tbseparator'
										},
										{
											text: 'Details',
											id: 'reportDetailButton',
											scale: 'medium',
											iconCls: 'fa fa-2x fa-list-alt icon-button-color-default icon-vertical-correction',
											disabled: true,
											handler: function () {
												var record = Ext.getCmp('scheduleReportsGrid').getSelection()[0];
												reportDetails(record.get('scheduleReportId'), true);
											},
											tooltip: 'Report Details'
										},
										{
											xtype: 'tbseparator',
											requiredPermissions: ['REPORTS-SCHEDULE-UPDATE']
										},
										{
											text: 'Toggle Status',
											id: 'reportActivateButton',
											scale: 'medium',
											iconCls: 'fa fa-2x fa-power-off icon-button-color-default',
											disabled: true,
											requiredPermissions: ['REPORTS-SCHEDULE-UPDATE'],
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
											requiredPermissions: ['REPORTS-DELETE'],
											handler: function () {
												scheduleReportDelete();
											},
											tooltip: 'Delete the record'
										}
									]
								},
								{
									xtype: 'pagingtoolbar',
									dock: 'bottom',
									store: scheduleReportsGridStore,
									displayInfo: true
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
						Ext.getCmp('reportDetailButton').setDisabled(false);

					} else if (cnt > 1 || cnt == 0) {
						Ext.getCmp('reportEditButton').setDisabled(true);
						Ext.getCmp('reportActivateButton').setDisabled(true);
						Ext.getCmp('reportDeleteButton').setDisabled(true);
						Ext.getCmp('reportDetailButton').setDisabled(true);
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
					showAddEditWin();
				};

				var scheduleReportEdit = function () {
					showAddEditWin(Ext.getCmp('scheduleReportsGrid').getSelection()[0]);
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


				var showAddEditWin = function(scheduleData) {
					
					var scheduleWin = Ext.create('Ext.window.Window', {
						title: 'Schedule Report',
						iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
						width: '80%',
						height: '80%',
						closeAction: 'destroy',
						modal: true,
						alwaysOnTop: true,
						maximizable: true,
						layout: 'fit',
						items: [
							{
								xtype: 'form',
								bodyStyle: 'padding: 20px;',
								scrollable: true,
								layout: 'anchor',								
								items: [
									{
										xtype: 'combobox',
										name: 'reportType',
										itemId: 'reportType',
										labelAlign: 'top',
										fieldLabel: 'Choose Report Type<span class="field-required" />',
										width: '100%',
										store: {
											autoLoad: true,
											proxy: {
												type: 'ajax',
												url: 'api/v1/resource/reports/reporttypes'
											}
										},
										displayField: 'description',
										valueField: 'code',
										editable: false,
										allowBlank: false,
										listeners: {
											change: function (cb, newVal, oldVal, opts) {
												var reportType = cb.getSelection();
												var form = cb.up('form');
												form.queryById('reportDescription').update(reportType.get('detailedDescription'));												
												
												scheduleOptionsShow(form);
												showReportOptions(form, newVal);
												
												if (cb.finishEditLoading) {
													cb.finishEditLoading();
												} else {
													//clear outputs
													outputs = [];
													outputs.push({
														outputId: Ext.id(),
														reportTransmissionType: 'VIEW',							
														reportTransmissionOption: {}
													});												
													showOutputs(form, newVal);
												}
												
											}
										}
									},
									{
										itemId: 'reportDescription',
										html: ''
									},
									{
										xtype: 'fieldset',
										title: 'Schedule',
										defaults: {
											labelAlign: 'top',
											width: '100%'
										},
										items: [
											{
												xtype: 'combobox',
												itemId: 'scheduleOptions',
												name: 'scheduleOption',												
												fieldLabel: 'How often to run the report?<span class="field-required" />',																							
												store: {
													data: [
														{ code: 'NOW', description: 'Now (One Time Only)'}
													]
												},
												displayField: 'description',
												valueField: 'code',
												value: 'NOW',
												editable: false,
												hidden: true,

												allowBlank: false,
												listeners: {
													
													change: function (cb, newVal, oldVal, opts) {
													
														//hide fields
														var fieldSet = cb.up('fieldset');
														var period = fieldSet.queryById('scheduleOptionPeriod').setHidden(true);
														var daily = fieldSet.queryById('scheduleOptionDays').setHidden(true);
														var minutes = fieldSet.queryById('scheduleOptionMinutes').setHidden(true);
														var cron = fieldSet.queryById('scheduleOptionCron').setHidden(true);
													
														//show options for time
														if ('PERIOD' === newVal) {
															period.setHidden(false);
														} else if ('DAYS' === newVal) {
															daily.setHidden(false);
														} else if ('MINUTES' === newVal) {	
															minutes.setHidden(false);
														} else if ('CUSTOM' === newVal) {	
															cron.setHidden(false);
														}
													}
												}
											},
											{
												xtype: 'combobox',
												fieldLabel: 'Schedule Period',
												itemId: 'scheduleOptionPeriod',
												name: 'scheduleOptionPeriod',
												editable: false,
												hidden: true,
												value: 'DAILY',
												displayField: 'description',
												valueField: 'code',
												store: {
													data: [
														{ code: 'DAILY', description: 'Daily'},
														{ code: 'WEEKLY', description: 'Weekly'},
														{ code: 'MONTHLY', description: 'Monthly'}
													]
												}
											},
											{
												xtype: 'numberfield',
												fieldLabel: 'Days Between Runs',
												itemId: 'scheduleOptionDays',
												name: 'scheduleOptionDays',
												hidden: true,
												value: 1,
												minValue: 1,
												maxValue: 30												
											},
											{
												xtype: 'numberfield',
												fieldLabel: 'Minutes Between Runs',
												itemId: 'scheduleOptionMinutes',
												name: 'scheduleOptionMinutes',
												hidden: true,
												value: 1,
												minValue: 1,
												maxValue: 525600												
											},
											{
												xtype: 'textfield',
												itemId: 'scheduleOptionCron',
												name: 'scheduleOptionCron',
												fieldLabel: 'Cron Expression (See <a href="https://www.freeformatter.com/cron-expression-generator-quartz.html\" target="_blank">Help</a>)',
												emptyText: 'Eg. 0 0 6 * * ? (Every day at 6am)',
												hidden: true,
												maxLength: 255												
											}
										]
									},
									{
										xtype: 'fieldset',
										itemId: 'reportOptionSet',
										title: 'Report Options',
										defaults: {
											labelAlign: 'top',
											width: '100%'
										},										
										items: [
										]
									},
									{
										xtype: 'fieldset',
										itemId: 'reportOutputs',
										title: 'Outputs',
										layout: 'anchor',
										items: [
											{
												xtype: 'panel',
												itemId: 'reportOutputs-inner',
												items: []
											}
										]
									}
								],
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
													var form = this.up('form');
																
		
													if (validateOutputs()){										

														var formData = form.getValues();

														//construct report object
														var reportData = {
															report: null,
															reportDataId: null
														};
													
														//unpack emails
														Ext.Array.each(outputs, function(reportOutput){
															if (reportOutput.reportTransmissionOption.emailAddressRaw &&
																reportOutput.reportTransmissionOption.emailAddressRaw !== '') {
																																
																var emails = CoreUtil.split(reportOutput.reportTransmissionOption.emailAddressRaw, [' ', ',', ';', '\n', '\r']);
																var emailAddresses = [];
																Ext.Array.each(emails, function(email){
																	emailAddresses.push({
																		email: email
																	});
																});
																reportOutput.reportTransmissionOption.emailAddresses = emailAddresses;
															}

															reportOutput.reportTransmissionOption.reportNotify = formData.reportNotify;
														});

														var report = {
															reportType: formData.reportType,															  
															reportOption: formData,
															scheduleIntervalDays: null,
															scheduleIntervalMinutes: null,
															scheduleIntervalCron: null,
															ids: null,
															reportOutputs: outputs
														};
														if (report.reportOption.evaluationType) {
															if (report.reportOption.evaluationType === 'summary') {
																report.reportOption.displayEvalSummary = true;
															} else if (report.reportOption.evaluationType === 'detail') {
																report.reportOption.displayEvalDetails = true;
															}
														}														
														
														reportData.report = report;
													
														//read in restriction ids
														var reportOptionSet = form.queryById('reportOptionSet');
														var entryselect = reportOptionSet.queryById('entryselect');														
														var entryRestrictions = [];
														if (entryselect) {
															Ext.Array.each(entryselect.getSelected(), function(selected) {
																entryRestrictions.push({
																	id: selected.componentId
																});
															});
															if (entryRestrictions.length > 0) {
																reportData.report.ids = entryRestrictions;														
																reportData.reportDataId = entryRestrictions;
															}
														}
														
														if (reportData.report.reportOption.previousDays === '') {
															reportData.report.reportOption.previousDays = null;
														}
															
													
														var scheduled = formData.scheduleOption !== 'NOW' ? true : false;
														if (!scheduled) {														
															reportData.report.reportOption.scheduleIntervalDays = null;															
															reportData.report.reportOption.scheduleIntervalMinutes = null;
															reportData.report.reportOption.scheduleIntervalCron = null;													
															
															
															CoreUtil.submitForm({
																url: 'api/v1/resource/reports',
																method: 'POST',
																data: reportData,
																removeBlankDataItems: true,
																form: form,
																loadingText: 'Submitting Report Request...',
																success: function (response, opts) {
																	Ext.toast('Submitted report request.', '', 'tr');

																	scheduleWin.close();
																	historyRefreshGrid();
																},
																failure: function (response, opts) {
																	Ext.toast('Failed to submit report generation request.', '', 'tr');
																}
															});														

														} else {														

															//resolve schedule
															switch(formData.scheduleOption){
																case 'PERIOD':
																	switch(formData.scheduleOptionPeriod){
																		case 'DAILY':
																			report.scheduleIntervalDays = 1;
																		break;	
																		case 'WEEKLY':
																			report.scheduleIntervalDays = 7;
																		break;																	
																		case 'MONTHLY':
																			report.scheduleIntervalDays = 28;
																		break;																	
																	}
																	break;
																case 'DAYS':
																	report.scheduleIntervalDays = formData.scheduleOptionDays;
																	break;
																case 'MINUTES':
																	report.scheduleIntervalMinutes = formData.scheduleOptionMinutes;
																	break;																
																case 'CUSTOM':
																	report.scheduleIntervalCron = formData.scheduleOptionCron;
																	break;																																
															}																

															var url = '';
															var method = '';
															if (scheduleWin.scheduleReportId) {
																url = 'api/v1/resource/scheduledreports/' + scheduleWin.scheduleReportId;
																method = 'PUT';
															}
															else {
																url = 'api/v1/resource/scheduledreports';
																method = 'POST';
															}

															CoreUtil.submitForm({
																url: url,
																method: method,
																data: report,
																removeBlankDataItems: true,
																form: form,															
																success: function (response, opts) {
																	Ext.toast('Saved Successfully', '', 'tr');
																	scheduleWin.close();
																	scheduleReportRefreshGrid();																
																},
																failure: function (response, opts) {
																	Ext.toast('Failed to Save', '', 'tr');																
																}
															});																												
														}
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
													scheduleWin.close();
												}												
											}
										]
									}
								]
							}
						]
					});
					scheduleWin.show();

					CoreService.userservice.getCurrentUser().then(function(user){
					if (CoreService.userservice.userHasPermission(user, "REPORTS-SCHEDULE-CREATE")) {

						var cbData = [
							{ code: 'PERIOD', description: 'Periodically'},
							{ code: 'DAYS', description: 'Every X Days'},
							{ code: 'MINUTES', description: 'Every X Minutes'},
							{ code: 'CUSTOM', description: 'Custom'}
						];
						
						scheduleWin.queryById('scheduleOptions').getStore().loadData(cbData, true);     
					}
				});
					
					var outputs = [];
				
					
					var validateOutputs = function() {
						var valid = true;
		
						if (!outputs || outputs.length === 0) {																										
							Ext.Msg.show({
								title: 'Validation',
								message: 'Please add at least one output.',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) {																
								}																
							});
							valid = false;
						} else {
							Ext.Array.each(outputs, function(output){
								//if (output.reportTransmissionType == 'CONFLUENCE') {
									//TODO: May need to check parent									
								//} 
							
								if (output.reportTransmissionOption.emailAddressRaw &&
									output.reportTransmissionOption.emailAddressRaw !== '') {

									var emails = CoreUtil.split(output.reportTransmissionOption.emailAddressRaw, [' ', ',', ';', '\n', '\r']);
									var badEmailAddresses = [];
									Ext.Array.each(emails, function(email){											
										if (CoreUtil.emailValidateStrict(email) === false) {
											badEmailAddresses.push(email);
										}
									});	

									if (badEmailAddresses.length > 0) {
										Ext.Msg.show({
											title: 'Validation Error',
											message: 'Check the following email addresses:<br><br>' + badEmailAddresses.join('<br>'),
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR,
											fn: function(btn) {																
											}																
										});
										valid = false;
									}
								}
							
							});			
						} 
		
						return valid;
					};
					
		
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
						fields: ['code', 'days'],
						data: days
					});					
					
					
					var scheduleOptionsShow = function(form) {
						form.queryById('scheduleOptions').setHidden(false);		
					};
					
					var	showReportOptions = function(form, reportType) {
						
						var reportOptionSet = form.queryById('reportOptionSet');
						reportOptionSet.removeAll();
						
						var optionsToAdd = [];
						if (reportType === 'COMPONENT' || 
							reportType === 'CMPORG' || 
							reportType === 'ENTRYLIST' ||
							reportType === 'TYPECOMP') {

							//add grid for entries							
							optionsToAdd.push({
								xtype: 'entryselect',
								itemId: 'entryselect',
								buttonTooltip: 'Select entries to report on.  Defaults to: All entries',
								allSelectedMessage: '<p>Report on ALL entries</p>'
							});
							
							if (reportType === 'TYPECOMP') {
								
								//add detail selection
								optionsToAdd.push({
										xtype: 'fieldcontainer',
										fieldLabel: 'Included Report Categories',										
										items: [
											{
												layout: 'column',
												items: [
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',														
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'Description',																															
																name: 'displayDescription',
																itemId: 'displayDescription',
																value: true
															},
															{
																boxLabel: 'Contacts',																														
																name: 'displayContacts',
																itemId: 'displayContacts',
																value: true
															},
															{
																boxLabel: 'Resources',																															
																name: 'displayResources',
																itemId: 'displayResources',
																value: true
															},
															{
																boxLabel: 'Vitals',
																name: 'displayVitals',
																itemId: 'displayVitals',
																value: true
															}
														]
													},
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',														
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'Dependencies',																																
																name: 'displayDependencies',
																itemId: 'displayDependencies',
																value: true
															},
															{
																boxLabel: 'Relationships',
																name: 'displayRelationships',
																itemId: 'displayRelationships',
																value: true
															},
															{
																boxLabel: 'Tags',																
																name: 'displayTags',
																itemId: 'displayTags',
																value: true
															},
															{
																boxLabel: 'Organization Data',																																
																name: 'displayOrgData',
																itemId: 'displayOrgData',
																value: true,
																inputAttrTpl: 'data-qtip=Title,&nbsp;organization,&nbsp;etc.'
															}
														]
													},
													{
														xtype: 'fieldcontainer',
														defaultType: 'checkboxfield',														
														columnWidth: 0.32,
														baseCls: 'detailReportColumn',
														items: [
															{
																boxLabel: 'All Evaluation Versions',
																name: 'displayEvalVersions',
																itemId: 'displayEvalVersions',
																inputAttrTpl: 'data-qtip=An&nbsp;evaluation&nbsp;category&nbsp;type&nbsp;must&nbsp;be&nbsp;specified'
															},
															{
																boxLabel: 'Reviews',
																name: 'displayReportReviews',
																itemId: 'displayReportReviews'
															},
															{
																boxLabel: 'Q/A',
																name: 'displayQA',
																itemId: 'displayQA'
															}
														]
													}
												]
											}
										]
									});
									
								optionsToAdd.push({
										xtype: 'fieldcontainer',
										defaultType: 'radiofield',
										itemId: 'evaluationTypeField',
										fieldLabel: 'Include Evaluation',																				
									    defaults: {
									        columnWidth: 0.32
									    },
									    layout: 'column',
										items: [
											{
												boxLabel: 'Evaluation Summary',
												name: 'evaluationType',													
												inputValue: 'summary',
												inputAttrTpl: 'data-qtip=Condensed&nbsp;evaluation&nbsp;overview',
												value: true
											},
											{
												boxLabel: 'Evaluation Details',	
												inputValue: 'detail',
												inputAttrTpl: 'data-qtip=Detailed&nbsp;evaluation&nbsp;analysis',												
												name: 'evaluationType'
											},
											{
												boxLabel: 'None',
												inputValue: 'none',
												inputAttrTpl: 'data-qtip=Exclude&nbsp;evaluations&nbsp;from&nbsp;this&nbsp;report',
												name: 'evaluationType'
											}
										]
									});

							}
						}
						else if (reportType === 'CATCOMP') {
							optionsToAdd.push({
								xtype: 'combobox',
								name: 'category',
								itemId: 'category',
								fieldLabel: 'Select Category<span class="field-required" />',
								store: {
									autoLoad: true,
									proxy: {
										type: 'ajax',
										url: 'api/v1/resource/attributes/attributetypes',
										reader: {
											type: 'json',
											rootProperty: 'data'
										}
									}
								},
								displayField: 'description',
								valueField: 'attributeType',
								editable: false,									
								allowBlank: true
							});							
						}
						else if (reportType === 'LINKVALID') {							
							optionsToAdd.push(
								{
									xtype: 'numberfield',
									name: 'maxWaitSeconds',
									itemId: 'maxWaitSeconds',
									fieldLabel: 'Enter how many seconds to wait (default: 5 sec, (1 - 300 seconds))',									
									maxLength: 3,
									minValue: 1,
									maxValue: 300,
									value: '5',
									editable: true,									
									allowBlank: true									
								}									
							);							
						}
						else if (reportType === 'SUBMISSION' || 
								reportType === 'USAGE' || 
								reportType === 'ENTRYSTATUS') {

							optionsToAdd.push({
								xtype: 'datefield',
								name: 'startDts',
								itemId: 'startDts',
								fieldLabel: 'Start Date (Blank = Current Day)',
								width: '100%',
								format: 'm/d/Y',
								value: new Date(),
								submitFormat: 'Y-m-d\\TH:i:s.u',																
								allowBlank: true,
								style: {
									marginTop: '20px'
								}
							});
							
							optionsToAdd.push({
								xtype: 'datefield',
								name: 'endDts',	
								itemId: 'endDts',
								fieldLabel: 'End Date (Blank = Current Day)',
								width: '100%',
								value: new Date(),
								format: 'm/d/Y',
								submitFormat: 'Y-m-d\\TH:i:s.u',
								allowBlank: true
							});
							
							optionsToAdd.push({
								xtype: 'combobox',
								name: 'previousDays',
								itemId: 'previousDays',
								fieldLabel: 'Previous Days',
								width: '100%',
								maxLength: 50,
								store: previousDaysStore,
								displayField: 'days',
								valueField: 'code',
								editable: false,
								listeners: {
									change: function(cb, newValue, oldValue, opts){
										if (newValue){
											form.queryById('startDts').setValue(null);
											form.queryById('endDts').setValue(null);
											form.queryById('startDts').setDisabled(true);
											form.queryById('endDts').setDisabled(true);
										} else {
											form.queryById('startDts').setDisabled(false);
											form.queryById('endDts').setDisabled(false);
										}
									}
								}
							});							
							
						}
						else if (reportType === 'EVALSTAT') {
							
							optionsToAdd.push({
								xtype: 'combobox',								
								name: 'assignedUser',
								itemId: 'assignedUser',
								fieldLabel: 'Assigned User',
								displayField: 'description',
								valueField: 'code',
								emptyText: 'All',
								typeAhead: true,
								editable: true,							
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
							});
							
							optionsToAdd.push({
								xtype: 'combobox',								
								name: 'assignedGroup',
								itemId: 'assignedGroup',
								fieldLabel: 'Assign to Group',
								displayField: 'description',
								valueField: 'code',
								emptyText: 'All',															
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
							});
						}
						reportOptionSet.add(optionsToAdd);						
						
					};
					
					
					var	showOutputs = function(form, reportType) {
						
						var reportOutputPanel = form.queryById('reportOutputs-inner');
						reportOutputPanel.removeAll();
						
						Ext.Array.each(reportOutputPanel.getDockedItems('button'), function(item){
							reportOutputPanel.removeDocked(item);
						});
						
						reportOutputPanel.setLoading(true);
						Ext.Ajax.request({
							url: 'api/v1/resource/reports/' + reportType + '/transmissiontypes',
							callback: function() {
								reportOutputPanel.setLoading(false);
							},
							success: function(response, opts) {
								var transmissionTypes = Ext.decode(response.responseText);
																
								
								var constructViewOutput = function(reportOutput) {

									var panel = {
										xtype: 'panel',
										title: 'Storefront Viewable Output',
										width: '100%',
										border: 1,
										closable: true,
										layout: 'anchor',
										bodyStyle: 'padding: 10px;',
										items: [
											{
												xtype: 'combobox',
												name: 'reportFormat',
												labelAlign: 'top',
												fieldLabel: 'Choose Report Format<span class="field-required" />',
												width: '100%',
												maxLength: 50,
												value: reportOutput.reportTransmissionOption.reportFormat,
												store: {
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/reports/' + reportType + '/' + reportOutput.reportTransmissionType + '/formats',											
													}
												},
												displayField: 'description',
												valueField: 'code',
												editable: false,								
												allowBlank: false,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.reportFormat = newValue;														
													}
												}
											},
											{
												xtype: 'checkbox',
												name: 'reportNotify',
												value: reportOutput.reportTransmissionOption.reportNotify,
												listeners: {
													change: function (self, newVal, oldVal) {
														reportOutput.reportTransmissionOption.reportNotify = newVal;
													}
												},
												boxLabel: '<b>Notify Me</b> <i class="fa fa-question-circle" data-qtip="If checked, sends an email when the report is viewable on the website"></i>'
											}
										],
										listeners: {
											close: function(p, opts) {
												removeOutputAction(reportOutput);
											}
										}
									}

									return panel;
								};
								
								var constructEmailOutput = function(reportOutput) {
									var panel = {
										xtype: 'panel',
										title: 'Email',
										width: '100%',
										border: 1,
										closable: true,
										layout: 'anchor',
										bodyStyle: 'padding: 10px;',
										items: [
											{
												xtype: 'combobox',
												name: 'reportFormat',
												labelAlign: 'top',
												fieldLabel: 'Choose Report Format<span class="field-required" />',
												width: '100%',
												maxLength: 50,
												value: reportOutput.reportTransmissionOption.reportFormat,
												store: {
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/reports/' + reportType + '/' + reportOutput.reportTransmissionType + '/formats',											
													}
												},
												displayField: 'description',
												valueField: 'code',
												editable: false,								
												allowBlank: false,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.reportFormat = newValue;														
													}
												}
											},
											{
												xtype: 'textarea',
												name: 'emailAddresses',
												labelAlign: 'top',
												fieldLabel: 'Enter email addresses <i class="fa fa-question-circle" data-qtip="You can separate multiple email addresses by a <b>comma</b>, <b>semicolon</b>, or <b>new line</b><br/><br/><b>Example:</b><br/>example1@domain.com,example2@domain.com;example3@domain.com"></i> <span class="field-required" />',
												emptyText: 'Enter one or more email addresses separated by a comma (,), semicolon (;), or a new line.',
												width: '100%',
												maxLength: 300,																	
												allowBlank: false,
												value: reportOutput.reportTransmissionOption.emailAddressRaw,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.emailAddressRaw = newValue;														
													}
												}																		
											},
											{
												xtype: 'checkbox',
												itemId: 'attachReport',
												hidden: true,
												boxLabel: 'Attach Report',
												name: 'attachReport',
												value: reportOutput.reportTransmissionOption.attachReport,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.attachReport = newValue;														
													}
												}										
											},								
											{
												xtype: 'checkbox',
												itemId: 'postEmailBody',
												hidden: true,
												boxLabel: 'Add Summary to Body',
												name: 'postToEmailBody',
												value: reportOutput.reportTransmissionOption.postToEmailBody,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.postToEmailBody = newValue;														
													}
												}									
											}
										],										
										listeners: {
											close: function(p, opts) {
												removeOutputAction(reportOutput);
											}
										}
									}

									//check permissions to hide options that require permissions
									CoreService.userservice.getCurrentUser().then(function(user){
										if (CoreService.userservice.userHasPermission(user, "REPORT-OUTPUT-EMAIL-ATTACH")) {
											var allAttached = reportOutputPanel.query('#attachReport');
											Ext.Array.each(allAttached, function(attached){
												attached.setHidden(false);
											});											
										}
										if (CoreService.userservice.userHasPermission(user, "REPORT-OUTPUT-EMAIL-BODY")) {
											var allAttached = reportOutputPanel.query('#postEmailBody');
											Ext.Array.each(allAttached, function(attached){
												attached.setHidden(false);
											});												
										}
									});						


									return panel;
								};
								
								var constructConfluenceOutput = function(reportOutput) {
									var panel = {
										xtype: 'panel',
										title: 'Confluence',
										width: '100%',
										border: 1,
										closable: true,
										layout: 'anchor',
										bodyStyle: 'padding: 10px;',
										items: [
											{
												xtype: 'combobox',
												name: 'reportFormat',
												labelAlign: 'top',
												fieldLabel: 'Choose Report Format<span class="field-required" />',
												width: '100%',
												maxLength: 50,
												value: reportOutput.reportTransmissionOption.reportFormat,
												store: {
													autoLoad: true,
													proxy: {
														type: 'ajax',
														url: 'api/v1/resource/reports/' + reportType + '/' + reportOutput.reportTransmissionType + '/formats',											
													}
												},
												displayField: 'description',
												valueField: 'code',
												editable: false,								
												allowBlank: false,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.reportFormat = newValue;														
													}
												}
											},
											{
												xtype: 'textfield',
												name: 'confluenceSpace',
												labelAlign: 'top',
												fieldLabel: 'Space Key<span class="field-required" /> <i class="fa fa-lg fa-question-circle"  data-qtip="Confluence Space Key as show Space Tools Overview" ></i>',
												width: '100%',
												allowBlank: false,
												maxLength: 255,
												value: reportOutput.reportTransmissionOption.confluenceSpace,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.confluenceSpace = newValue;														
													}
												}
											},
											{
												xtype: 'textfield',
												name: 'confluencePage',
												labelAlign: 'top',
												fieldLabel: 'Page Title <span class="field-required" />',
												width: '100%',
												allowBlank: false,
												maxLength: 255,
												value: reportOutput.reportTransmissionOption.confluencePage,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.confluencePage = newValue;														
													}
												}
											},
											{
												xtype: 'textfield',
												name: 'confluenceParentPageId',
												labelAlign: 'top',
												fieldLabel: 'Parent Page Title',
												width: '100%',
												allowBlank: true,
												maxLength: 255,
												value: reportOutput.reportTransmissionOption.confluenceParentPageId,
												listeners: {
													change: function(field, newValue, oldValue, opts) {
														reportOutput.reportTransmissionOption.confluenceParentPageId = newValue;														
													}
												}
											}
										],										
										listeners: {
											close: function(p, opts) {
												removeOutputAction(reportOutput);
											}
										}
									}
									return panel;						
								};							
																
								var addBtn;								
								var updateDisplay = function() {
									reportOutputPanel.removeAll();

									var hasEmailTransmissionType = false;
									var outputComponents = [];						
									Ext.Array.each(outputs, function(output){
										switch (output.reportTransmissionType) {
											case 'VIEW':
												outputComponents.push(constructViewOutput(output));
												break;
											case 'EMAIL':
												outputComponents.push(constructEmailOutput(output));
												hasEmailTransmissionType = true;
												break;
											case 'CONFLUENCE':
												outputComponents.push(constructConfluenceOutput(output));
												break;
										}										
									});
									
									reportOutputPanel.add(outputComponents);

									// 	if the an email transmission type has been specified, don't allow the user
									//		to request a notification on report completion (as they will be already recieving an email.)
									if (hasEmailTransmissionType) {
										var reportNotifyField = Ext.ComponentQuery.query('[name="reportNotify"]')[0];
										reportNotifyField.setDisabled(true);
										reportNotifyField.setValue(false);
									}
									
									//timing issue on format field reload
									Ext.defer(function(){
										form.getForm().checkValidity();
									}, 1000);
									
									outputToAdd = [];								
									Ext.Array.each(transmissionTypes, function(avaliable) {
										//check if already added and multiple is not allowed.
										var add = true;
										if (!avaliable.supportsMultiple) {
											//see if already added										
											Ext.Array.each(outputs, function(output){
												if (avaliable.code === output.reportTransmissionType) {
													add = false;
												}
											});
										} 

										if (add) {
											outputToAdd.push({
												text: avaliable.description,											
												transmissionType: avaliable.code,
												handler: function() {	
													outputs.push({
														outputId: Ext.id(),
														reportTransmissionType: avaliable.code,
														reportTransmissionOption: {}
													});
													updateDisplay();
												}
											});
										}

									});

									if (addBtn) {
										reportOutputPanel.removeDocked(addBtn);
									}

									// Place the 'Add' email button
									CoreService.userservice.getCurrentUser().then(function (user) {

										var userHasEmailPermission = CoreService.userservice.userHasPermission(user, 'REPORT-OUTPUT-EMAIL-ATTACH') || CoreService.userservice.userHasPermission(user, 'REPORT-OUTPUT-EMAIL-BODY');

										// remove the "email" option if it exists
										if (!userHasEmailPermission) {
											Ext.Array.forEach(outputToAdd, function (element, index) {
												if (typeof element.text !== 'undefined' && element.text == "Email") {
													outputToAdd.splice(index, 1);
												}
											});
										}

										if (outputToAdd.length > 0) {
											addBtn = Ext.create('Ext.button.Button', {
												dock: 'bottom',
												text: 'Add',
												maxWidth: 100,
												margin: '20 0 0 0',
												iconCls: 'fa fa-lg fa-plus  icon-button-color-save',
												menu: outputToAdd,
											});
											
											reportOutputPanel.addDocked(addBtn);
										}
									});
								};
								updateDisplay();
																
								var removeOutputAction = function(reportOutput) {
									var index = 0;
									var emailOutputTypes = 0;
									var notifyMeField = Ext.ComponentQuery.query('[name="reportNotify"]')[0];

									Ext.Array.each(outputs, function(item) {
										if (item.outputId === reportOutput.outputId) {
											return false;
										}

										if (item.reportTransmissionType === 'EMAIL') {
											emailOutputTypes += 1;
										}

										index++;
									});

									// if there are no email transmission types, ensure 'Notify Me' checkbox is enabled.
									if (emailOutputTypes === 0 && notifyMeField.length) {
										notifyMeField.setDisabled(false);
									}

									Ext.Array.removeAt(outputs, index);
									updateDisplay();
								};														
								
							}
						});						
						
					};
					
					
					if (scheduleData) {
						//edit
						var genform = scheduleWin.down('form');
						
						console.log(scheduleData);
						var data = scheduleData.data;
						
						scheduleWin.scheduleReportId = data.scheduleReportId
						
						//set report Type
						var reportTypeField = scheduleWin.queryById('reportType');
						reportTypeField.setValue(data.reportType);
						reportTypeField.finishEditLoading = function() {
						
							//update schedule options
							if (data.scheduleIntervalDays) {
								if (data.scheduleIntervalDays === 1) {
									genform.queryById('scheduleOptions').setValue('PERIOD');
									genform.queryById('scheduleOptionPeriod').setValue('DAILY');
								} else if (data.scheduleIntervalDays === 7) {								
									genform.queryById('scheduleOptions').setValue('PERIOD');
									genform.queryById('scheduleOptionPeriod').setValue('WEEKLY');
								} else if (data.scheduleIntervalDays === 28) {								
									genform.queryById('scheduleOptions').setValue('PERIOD');
									genform.queryById('scheduleOptionPeriod').setValue('MONTHLY');
								} else {
									genform.queryById('scheduleOptions').setValue('DAYS');
									genform.queryById('scheduleOptionDays').setValue(data.scheduleIntervalDays);
								}
							} else if (data.scheduleIntervalMinutes) {
								genform.queryById('scheduleOptions').setValue('MINUTES');
								genform.queryById('scheduleOptionMinutes').setValue(data.scheduleIntervalMinutes);								
							} else if (data.scheduleIntervalCron) {
								genform.queryById('scheduleOptions').setValue('CUSTOM');
								genform.queryById('scheduleOptionCron').setValue(data.scheduleIntervalCron);								
							}

							//unpack options and load	
							Ext.Object.each(data.reportOption, function(key, value){
								
								if (key === 'evaluationType') {
									var field = genform.queryById('evaluationTypeField');
									if (field) {
										field.setValue(value);
									}
								} else {
									var field = genform.queryById(key);
									if (field) {
										field.setValue(value);
									}
								}
								
							});							
							
							
							//load entry select (if available)
							var entrySelect = genform.queryById('entryselect');
							if (entrySelect) {
								entrySelect.loadCurrentSelection(data.ids);
							}

							//restore outputs						
							outputs = data.reportOutputs;						
							showOutputs(genform, data.reportType);
							
							reportTypeField.finishLoading = null;
						};						
						
					} 
					
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
					}),
					listeners: {
						beforeLoad: function(store, operation, eOpts){
							store.getProxy().extraParams = {
								reportType: Ext.getCmp('historyFilter-reportType').getValue() ? Ext.getCmp('historyFilter-reportType').getValue() : null,
								showScheduledOnly: Ext.getCmp('historyFilter-showScheduledOnly').getValue(),
								showAllUsers: Ext.getCmp('historyFilter-showAll').getValue() 
							};
						}
					}
				});

				var historyGrid = Ext.create('Ext.grid.Panel', {
					id: 'historyGrid',
					title: 'Reports &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="System scheduled and hard reports" ></i>',
					store: historyGridStore,
					columnLines: true,
					bodyCls: 'border_accent',
					previousSelection: null,
					selModel: {
						selType: 'checkboxmodel'
					},
					bufferedRenderer: false,
					columns: [
						{text: 'Report Id', dataIndex: 'reportId', width: 250, hidden: true },
						{text: 'Report Type', dataIndex: 'reportType', width: 200,
							renderer: function (value, meta, record) {
								return record.get('reportTypeDescription');
							}
						},
						{text: 'View Format', dataIndex: 'reportFormat', width: 250,
							renderer: formatRender
						},
						{text: 'Run Status', dataIndex: 'runStatus', width: 150, align: 'center',
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
						{text: 'Create User', dataIndex: 'createUser', width: 150, align: 'center'},
						{
							text: '<span data-qtip="Days until report is removed from the system">Days Until Cleanup</span>', 
							dataIndex: 'remainingReportLifetime', 
							width: 165,
							align: 'center',
							sortable: false, renderer: function (value, meta, record) {

								// Defined status color, info, and sybmol
								var maxHue = 85;
								var statusColor = (record.data.remainingReportLifetime/record.data.reportLifetimeMax)*maxHue;
								var statusInfo = ['This report will be removed soon', 'This report still has time before cleanup', 'This report was recently created'];
								var statusSymbol = ['fa fa-lg fa-exclamation-circle', 'fa fa-lg fa-exclamation-triangle', 'fa fa-lg fa-check-circle'];
								var statusIndex = Math.floor(record.data.remainingReportLifetime/record.data.reportLifetimeMax*(statusInfo.length-0.1));
								statusInfo = record.data.remainingReportLifetime == 0 ? 'This report will be removed' : statusInfo[statusIndex];
								record.data.remainingReportLifetime = record.data.remainingReportLifetime == 0 ? "Queued for removal" : record.data.remainingReportLifetime;

								statusSymbol = statusSymbol[statusIndex];

								// Set the value of the cell
								record.data.remainingReportLifetime = '<span data-qtip="' + statusInfo + '"> <i style="color: hsla(' + statusColor + ', 70%, 50%, 1.0);" class="' + statusSymbol + '" aria-hidden="true"></i> ' + record.data.remainingReportLifetime + '</span>';
								return record.data.remainingReportLifetime;
							} 
						},
						{text: 'Scheduled', dataIndex: 'scheduled', width: 100, align: 'center',
							renderer: CoreUtil.renderer.booleanRenderer
						},
						{text: 'Options', dataIndex: 'reportOption', minWidth: 270, flex: 1, sortable: false, renderer: optionsRender },
						{ text: 'Outputs', dataIdndex: 'reportOutput', sortable: false, width: 150, flex: 1, sortable: false,
							renderer: function(value, meta, record) {
								var outputs = 'VIEW';
								
								if (record.data.reportOutputs) {
									var outputTypes = [];
									Ext.Array.each(record.data.reportOutputs, function(item){									
										outputTypes.push(item.reportTransmissionType); 
									});
									outputs = outputTypes.join(', ');
								}
								return outputs;
							}
						},
						{text: 'Output Options', dataIndex: 'reportOutputs', minWidth: 200, flex: 2, hidden: true, sortable: false,
							renderer: outputOptionRender
						}						
						
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'historyFilter-reportType',
									emptyText: 'All',
									fieldLabel: 'Report Type',
									labelAlign: 'left',
									name: 'reportType',
									typeAhead: false,
									editable: false,
									width: 325,
									margin: '0 20 0 0',
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											historyRefreshGrid();
										}
									},
									storeConfig: {
										url: 'api/v1/resource/lookuptypes/ReportType',
										addRecords: [
											{
												code: null,
												description: 'All'
											}
										]
									}
								}),
								{
									xtype: 'checkbox',
									id: 'historyFilter-showScheduledOnly',
									boxLabel: 'Show Scheduled Only',									
									margin: '0 20 0 0',
									enableToggle: true,
									requiredPermissions: ['REPORTS-SCHEDULE-READ'],
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											historyRefreshGrid();
										}
									},
									name: 'showScheduledOnly'
								},
								{
									xtype: 'checkbox',
									id: 'historyFilter-showAll',
									padding: '0 20 0 0',
									requiredPermissions: ['REPORTS-ALL'],
									listeners: {
										change: function(filter, newValue, oldValue, opts){
											historyRefreshGrid();
										}
									},
									boxLabel: 'Show All Users',
									name: 'showAllUsers'									
								}								
							]
						}, 
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
									xtype: 'tbseparator',
									requiredPermissions: ['REPORTS-CREATE']
								},
								{
									text: 'New Report',
									iconCls: 'fa fa-2x fa-plus icon-button-color-save icon',
									scale: 'medium',
									requiredPermissions: ['REPORTS-CREATE'],
									handler: function () {										
										showAddEditWin();
									}
								},
								{
									text: 'Scheduled Reports',
									id: 'scheduledReportBtn',
									iconCls: 'fa fa-2x fa-clock-o icon-button-color-default icon-vertical-correction',
									scale: 'medium',
									requiredPermissions: ['REPORTS-SCHEDULE-READ'],
									handler: function () {
										scheduledReportsWin.show();
										scheduleReportRefreshGrid();
									},
									tooltip: 'Schedule Reports'
								},								
								{
									xtype: 'tbseparator'
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
									xtype: 'tbseparator'
								},
								{
									text: 'Details',
									id: 'historyDetailButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-list-alt icon-button-color-default icon-vertical-correction',
									disabled: true,
									handler: function () {
										var record = Ext.getCmp('historyGrid').getSelection()[0];
										reportDetails(record.get('reportId'), false);
									},
									tooltip: 'Report Details'
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
									requiredPermissions: ['REPORTS-DELETE'],
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
					viewConfig: {
						getRowClass: function (record) {
							return 'grid-panel-height-transition';
						}
					},
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							if (!record.get('noViewAvailable')){
								viewHistory();
							}
						},
						rowclick: function ( self, record, tr ) {

							// get all row cells that are expandable
							var expandableCells = tr.querySelectorAll('.expandable-grid-cell');

							// find the largest expandable cell (this will determine the max height of the row)
							var largetExpandableCell = null;
							Ext.Array.each(expandableCells, function (cell, index) {
								if (largetExpandableCell === null) {
									largetExpandableCell = cell;
								}
								else {
									if (parseInt(cell.getAttribute('data-num')) > parseInt(largetExpandableCell.getAttribute('data-num'))) {
										largetExpandableCell = cell;
									}
								}
							});

							// set the style and height of the cells that are expandable
							var dataNumAttr = parseInt(largetExpandableCell.getAttribute('data-num'));
							if (dataNumAttr || dataNumAttr === 0) {
								var switchedRecord = false;

								// if we have already selected an item
								if (!!self.previousSelection) {

									switchedRecord = self.previousSelection != tr;
									Ext.Array.each(self.previousSelection.querySelectorAll('.expandable-grid-cell'), function (cell, index) {
										cell.style.height = '2.8em';
										cell.className = cell.className.replace(new RegExp('(?:^|\\s)'+ 'expandable-grid-cell-expanded' + '(?:\\s|$)'), ' expandable-grid-cell-collapsed ');
									});
									self.previousSelection = tr;
								}

								// if we haven't selected an item
								else {
									switchedRecord = true;
									self.previousSelection = tr;
								}

								// if the data in the cell has more than 2 sets of data, make it expandable
								if (switchedRecord && dataNumAttr > 2) {
									Ext.Array.each(self.previousSelection.querySelectorAll('.expandable-grid-cell'), function (cell, index) {
										cell.style.height = (dataNumAttr * 1.5) + 'em';
										cell.className = cell.className.replace(new RegExp('(?:^|\\s)'+ 'expandable-grid-cell-collapsed' + '(?:\\s|$)'), ' expandable-grid-cell-expanded ');
									});
								}
								else {
									self.previousSelection = null;
								}
							}

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
						if (record.get('runStatus') !== 'C' || record.get('noViewAvailable')) {
							Ext.getCmp('historyViewButton').setDisabled(true);
							Ext.getCmp('historyExportButton').setDisabled(true);
							Ext.getCmp('historyDetailButton').setDisabled(false);
						} else {
							if (record.get('noViewAvailable')){
								Ext.getCmp('historyViewButton').setDisabled(true);							
								Ext.getCmp('historyExportButton').setDisabled(true);
							} else {
								Ext.getCmp('historyViewButton').setDisabled(false);							
								Ext.getCmp('historyExportButton').setDisabled(false);
							}							
							Ext.getCmp('historyDetailButton').setDisabled(false);
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
						Ext.getCmp('historyDetailButton').setDisabled(true);
					} else {
						Ext.getCmp('historyViewButton').setDisabled(true);
						Ext.getCmp('historyDeleteButton').setDisabled(true);
						Ext.getCmp('historyExportButton').setDisabled(true);
						Ext.getCmp('historyDetailButton').setDisabled(true);
					}
				};

				var reportDetails = function(recordId, scheduled) {
					
					var detailwin = Ext.create('Ext.window.Window', {
						title: 'Report Details',
						modal: true,
						alwaysOnTop: true,
						width: '50%',
						height: '50%',
						maximizable: true,
						closeAction: 'destroy',
						scrollable: true,
						bodyStyle: 'padding: 10px',
						listeners:	{
							show: function() {        
								this.removeCls("x-unselectable");    
							}
						},							
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										xtype: 'tbfill'
									},
									{
										text: 'Close',
										scale: 'medium',
										iconCls: 'fa fa-2x fa-close icon-button-color-warning icon-vertical-correction',
										handler: function() {
											detailwin.close();
										}
									},
									{
										xtype: 'tbfill'
									}
								]
							}
						],
						tpl: new Ext.XTemplate(
							'<h2>Report: {reportTypeDescription}</h2>',
							'{createUser} - {[Ext.util.Format.date(Ext.Date.parse(values.createDts, "c"), "m/d/Y H:i:s")]}<br>',
							'<h3 style="background: lightgrey; padding: 5px;">Options:</h3>',
							'<div style="padding: 10px;">',
								'{[this.displayOptions(values.options)]}',
							'</div>',
							
							'<h3 style="background: lightgrey; padding: 5px;">Entries Reported On:</h3>',
							'<div style="padding: 10px;">',
								'<tpl if="this.hasEntries(idsInReport)">',
									'<tpl for="idsInReport">',
									' {description}<br>',
									'</tpl>',
								'</tpl>',
								'<tpl if="!this.hasEntries(idsInReport)"> All Entries (If Appplicable) </tpl>',
							'</div>',							
							
							'<h3 style="background: lightgrey; padding: 5px;">Outputs:</h3>',							
							'<div style="padding: 10px;">',
								'{[this.displayOutputs(values.outputs)]}',
							'</div>',
							{
								hasEntries: function(idsInReport) {
									if (idsInReport && idsInReport.length > 0) {
										return true;
									} else {
										return false;
									}
								},
								displayOutputs: function(outputs) {
									var record = {
										data: {
											reportOutputs: outputs
										}
									};									
									return outputOptionRender(null, null, record);
								},
								displayOptions: function(options) {
									var results = '';
									Ext.Object.each(options, function(key, value, myself) {
										if (key !== 'storageVersion') {
											results += '<b>' + key + ':</b> ' + value + '<br>';
										}
									});
									return results;
								}
							}
							
						)
					});
					detailwin.show();
					
					var url = 'api/v1/resource/reports/' + recordId + '/detail';
					if (scheduled) {
						url = 'api/v1/resource/scheduledreports/' + recordId + '/detail';
					}
					
					detailwin.setLoading(true);
					Ext.Ajax.request({
						url: url,
						callback: function() {
							detailwin.setLoading(false);
						},
						success: function(response, opt) {
							var data = Ext.decode(response.responseText);							
							detailwin.update(data);
						}
					});
					
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
								var reportFormat = selectedObj.data.reportViewFormat;
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

			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>
