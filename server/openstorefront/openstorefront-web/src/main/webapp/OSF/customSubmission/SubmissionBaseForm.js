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

/* global Ext, data */

Ext.define('OSF.customSubmission.SubmissionBaseForm', {
	extend: 'Ext.form.Panel',
	
	
	
	reviewDisplayValue: function() {
		var baseFormPanel = this;		
		//var data = baseFormPanel.getValues();
		
		//display field and values
		var fields = baseFormPanel.getForm().getFields();
		
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
		Ext.Array.each(fields.items, function(field){
			if (field.fieldLabel && !field.hidden && !field.disabled) {
				
				var value;
				
				if (field.getSelection) {
					var record = field.getSelection();
					if (record) {
						value = record.get(field.getDisplayField());
					}
				} else {
					value = field.getValue();
				}
				
				data.push({
					label: field.fieldLabel,
					value: value
				});
			}
		});		
		
		return template.apply(data);
	}
		
	
});
