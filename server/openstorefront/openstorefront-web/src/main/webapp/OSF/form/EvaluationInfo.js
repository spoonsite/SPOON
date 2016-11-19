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

/* global Ext */

Ext.define('OSF.form.EvaluationInfo', {
	extend: 'Ext.form.Panel',
	alias: 'osf.form.EvaluationInfo',

	layout: 'anchor',
	bodyStyle: 'padding: 20px',
	initComponent: function () {		
		this.callParent();
		
		var evalForm = this;
		
		var version = Ext.create('Ext.form.field.Text', {			
			fieldCls: 'eval-form-field',
			labelClsExtra: 'eval-form-field-label',
			labelAlign: 'right',
			name: 'version',
			fieldLabel: 'Version',
			width: '100%'			
		});
		
		evalForm.add(version);
	},
	
	loadData: function(evaluationId, componentId, data, opts) {
		var evalForm = this;
		
		evalForm.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/evaluations/' + evaluationId,
			callback: function() {
				evalForm.setLoading(false);
			},
			success: function(response, opt) {
				var evaluation = Ext.decode(response.responseText);
				var record = Ext.create('Ext.data.Model',{					
				});
				record.set(evaluation);				
				evalForm.loadRecord(record);
			}
		});
		opts.commentPanel.loadComments(evaluationId, null, null);
	}	
	
});
