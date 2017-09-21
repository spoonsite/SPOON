/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext, CoreUtil */

Ext.define('OSF.form.EvaluationInfo', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.EvaluationInfo',

	layout: 'anchor',
	bodyStyle: 'padding: 10px;margin: 10px',
	initComponent: function () {
		this.callParent();

		var evalForm = this;

		var formItems = [];

		formItems.push({
			xtype: 'textfield',
			fieldCls: 'eval-form-field',
			labelClsExtra: 'eval-form-field-label',
			labelAlign: 'right',
			name: 'version',
			allowBlank: false,
			maxLength: 255,
			labelSeparator: '',
			fieldLabel: 'Version <span class="field-required" />',
			labelWidth: 200,
			width: '100%',
			listeners: {
				change: {
					buffer: 2000,
					fn: function (field, newValue, oldValue) {
						evalForm.saveData();
					}
				}
			}
		});
		formItems.push(Ext.create('OSF.component.StandardComboBox', {
			name: 'workflowStatus',
			labelClsExtra: 'eval-form-field-label',
			fieldCls: 'eval-form-field',
			labelAlign: 'right',
			allowBlank: false,
			margin: '0 0 5 0',
			editable: false,
			typeAhead: false,
			width: '100%',
			fieldLabel: 'Status <span class="field-required" />',
			labelWidth: 200,
			storeConfig: {
				url: 'api/v1/resource/lookuptypes/WorkflowStatus'
			},
			listeners: {
				change: {
					buffer: 1000,
					fn: function (field, newValue, oldValue) {
						evalForm.saveData();
					}
				}
			}
		}));
		formItems.push(Ext.create('OSF.component.SecurityComboBox', {
			width: '100%',
			itemId: 'securityMarking',
			labelClsExtra: 'eval-form-field-label',
			fieldCls: 'eval-form-field',
			labelAlign: 'right',
			labelWidth: 200,
			listeners: {
				change: {
					buffer: 1000,
					fn: function (field, newValue, oldValue) {
						evalForm.saveData();
					}
				}
			}
		}));
		formItems.push(Ext.create('OSF.component.DataSensitivityComboBox', {
			width: '100%',
			itemId: 'dataSensitivity',
			labelClsExtra: 'eval-form-field-label',
			fieldCls: 'eval-form-field',
			labelAlign: 'right',
			labelWidth: 200,
			listeners: {
				change: {
					buffer: 1000,
					fn: function (field, newValue, oldValue) {
						evalForm.saveData();
					}
				}
			}
		}));

		evalForm.add(formItems);
	},

	loadData: function (evaluationId, componentId, data, opts) {
		var evalForm = this;

		evalForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/evaluations/' + evaluationId,
			callback: function () {
				evalForm.setLoading(false);
			},
			success: function (response, opt) {

				Ext.Ajax.request({
					url: 'api/v1/resource/evaluations/' + evaluationId + '/checkTemplateUpdate',
					success: function (response, opt) {
						if (Ext.decode(response.responseText).result === true) {
							evalForm.insert(0, {
								xtype: 'toolbar',
								itemId: 'updateNotice',
								cls: 'alert-warning',
								items: [
									{
										xtype: 'tbfill'
									},
									{
										xtype: 'panel',
										html: '<h1>There has been an update to the template this review is based on.</h1>'
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Update',
										itemId: 'updateTemplateBtn',
										iconCls: 'fa fa-2x fa-refresh icon-button-color-default icon-vertical-correction',
										scale: 'medium',
										handler: function () {
											Ext.Ajax.request({
												url: 'api/v1/resource/evaluations/' + evaluationId + '/updateTemplate',
												method: 'PUT',
												success: function () {
													evalForm.reloadEval();
													evalForm.remove(evalForm.getComponent("updateNotice"));
												}
											});
										}
									}
								]
							});
						}
					}
				});
				var evaluation = Ext.decode(response.responseText);
				var record = Ext.create('Ext.data.Model', {
				});
				record.set(evaluation);

				evalForm.loadRecord(record);

				evalForm.queryById('dataSensitivity').on('ready', function () {
					evalForm.loadRecord(record);

					Ext.defer(function () {
						evalForm.doneInitialLoad = true;
					}, 2000);
				});

				evalForm.queryById('securityMarking').on('ready', function () {
					evalForm.loadRecord(record);

					Ext.defer(function () {
						evalForm.doneInitialLoad = true;
					}, 2000);
				});

				evalForm.evaluation = evaluation;
				if (opts && opts.mainForm) {
					evalForm.refreshCallback = opts.mainForm.refreshCallback;
					evalForm.reloadEval = function () {
						opts.mainForm.loadEval(evaluationId, componentId);
					};
				}
			}
		});
		opts.commentPanel.loadComments(evaluationId, null, null);
	},

	saveData: function () {
		var evalForm = this;

		var data = evalForm.getValues();

		if (evalForm.doneInitialLoad) {
			if (evalForm.isValid() &&
					data.version &&
					data.version !== '' &&
					data.workflowStatus &&
					data.workflowStatus !== ''
					) {

				if (evalForm.evaluation.version !== data.version ||
						evalForm.evaluation.workflowStatus !== data.workflowStatus ||
						evalForm.evaluation.dataSensitivity !== data.dataSensitivity ||
						evalForm.evaluation.securityMarkingType !== data.securityMarkingType
						)
				{
					evalForm.evaluation.version = data.version;
					evalForm.evaluation.workflowStatus = data.workflowStatus;
					evalForm.evaluation.dataSensitivity = data.dataSensitivity;
					evalForm.evaluation.securityMarkingType = data.securityMarkingType;
					delete evalForm.evaluation.type;

					CoreUtil.submitForm({
						url: 'api/v1/resource/evaluations/' + evalForm.evaluation.evaluationId,
						method: 'PUT',
						data: evalForm.evaluation,
						form: evalForm,
						success: function (action, opts) {
							Ext.toast('Updated evaluation');
							if (evalForm.refreshCallback) {
								evalForm.refreshCallback();
							}
						}
					});
				}
			}
		}
	}

});
