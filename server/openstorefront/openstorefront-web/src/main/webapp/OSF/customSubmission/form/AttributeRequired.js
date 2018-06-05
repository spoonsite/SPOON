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

/* global Ext, fieldPanel */

Ext.define('OSF.customSubmission.form.AttributeRequired', {
	extend: 'OSF.customSubmission.SubmissionBaseForm',	
	xtype: 'osf-submissionform-attributerequired',
	requires: [
		'OSF.common.AttributeCodeSelect'
	],
	
	width: '100%',
	layout: 'anchor',
	
	initComponent: function () {
		var formPanel = this;
		formPanel.callParent();
		
		//load required for entry type and generate form
		
		formPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/attributes/required?componentType=' + formPanel.componentType.componentType + '&submissionOnly=true',
			callback: function() {
				formPanel.setLoading(false);
			},
			success: function(response, opts) {
				var attributeTypes = Ext.decode(response.responseText);
				
				if (attributeTypes.length === 0) {
					formPanel.setHidden(true);
				}
				
				var fields = [];
				Ext.Array.each(attributeTypes, function(attributeType){
					
					fields.push({
						xtype: 'AttributeCodeSelect',
						attributeTypeView: attributeType
					});										
					
				});
				formPanel.add(fields);
				formPanel.updateLayout(true, true);
				
			}
		});
		
		
	},
	reviewDisplayValue: function() {
		var attributePanel = this;
		
		var template = new Ext.XTemplate(
			'<table class="submission-review-table">' + 
			'<tbody>' + 
			'	<tpl for=".">'+
			'		<tr class="submission-review-row">' +
			'			<td class="submission-review-label">'+
			'				{label}' +
			'			</td>' +
			'			<td class="submission-review-data" style="min-width: 150px">' +
			'				{value}' +
			'			</td>' +			
			'		</tr>' +
			'	</tpl>'+
			'</tbody>' +
			'</table>'
		);

		var data = [];
		
		Ext.Array.each(attributePanel.items.items, function(field) {
			data.push({
				label: field.attributeTypeView.description,
				value: field.getValue()
			});
		});
		
		return template.apply(data);
	}	
	
});
