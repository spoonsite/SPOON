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
/* global Ext */

Ext.define('OSF.customSubmission.SubmissionBaseGrid', {
	extend: 'Ext.grid.Panel',
	
	fieldTemplate: {
		fieldType: null,
		mappingType: 'COMPLEX',
		questionNumber: null,
		label: null,
		labelTooltip: null,
		required: null
	},
	columnLines: true,
	frame: true,
	width: '100%',	
	store: {},
	listeners: {
		selectionchange: function (selectionModel, records, opts) {
		
			var deleteButton = this.queryById('deleteBtn');
			var editButton = this.queryById('editBtn');

			if (records.length > 0) {
				editButton.setDisabled(false);
				deleteButton.setDisabled(false);
			} else {
				editButton.setDisabled(true);
				deleteButton.setDisabled(true);
			}
		}
	},	
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					text: 'Add',
					iconCls: 'fa fa-lg fa-plus icon-button-color-save',
					handler: function () {

						var grid = this.up('grid');
						grid.actionAddEdit();
					}
				},
				{
					text: 'Edit',
					iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
					disabled: true,
					itemId: 'editBtn',
					handler: function () {

						var grid = this.up('grid');
						grid.actionAddEdit(grid.getSelection()[0]);						
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					disabled: true,
					itemId: 'deleteBtn',
					handler: function () {

						var grid = this.up('grid');
						grid.store.remove(grid.getSelection()[0]);

						var deleteButton = grid.queryById('deleteBtn');
						var editButton = grid.queryById('editBtn');
						editButton.setDisabled(true);
						deleteButton.setDisabled(true);
					}
				}
			]
		}
	],
	
	initComponent: function () {
		var submissionGrid = this;
		submissionGrid.callParent();
		submissionGrid.display();
	},
	
	display: function() {
		var submissionGrid = this;
		
		
		var toolbar = {
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					xtype: 'tbtext',
					text: submissionGrid.createQuestionLabel()
				}
			]
		};

		submissionGrid.addDocked(toolbar, 0);
	},
	
	/**
	 * Override to add options
	 */
	getOptionPanel: function() {
		return Ext.create('Ext.panel.Panel', {});
	},
	
	/**
	 * Override to handle validation
	 */	
	isValid: function() {
		var submissionGrid = this;
		var valid = true;
		if (submissionGrid.fieldTemplate.required) {
			//must have at least one entry
			if (submissionGrid.getStore().getCount() === 0) {
				valid = false;
			}
		}
		return valid;
	},
	
	/**
	 * Always call this as it may be overriden 
	 */
	getTemplateData: function() {
		var submissionGrid = this;
		return submissionGrid.submissionTemplateData;
	},
	
	setTemplateData: function(editData) {
		var submissionGrid = this;
		submissionGrid.submissionTemplateData = editData;
		submissionGrid.display();
	},
	
	/**
	 * Override to match what server expects
	 */	
	getUserData: function() {
		return {};
	},

	reviewDisplayValue: function(){
		var grid = this;
		
		if (grid.getStore().getCount() === 0) {
			return '(No Data Entered)';
		}		
		
		var template = new Ext.XTemplate(
			'<table class="submission-review-table">' + 
			'<thead>' +
			'	<tpl for="columns">'+
			'		<th class="submission-review-header">' + 
			'			{title}' + 
			'		</th>' + 			
			'	</tpl>'+
			'</thead>' +
			'<tbody>' + 
			'	<tpl for="data">'+
			'		<tr class="submission-review-row">' +
			'			<tpl for="fields">'+
			'				<td class="submission-review-data">' +
			'					{value}' +
			'				</td>' +
			'			</tpl>' +			
			'		</tr>' +
			'	</tpl>'+
			'</tbody>' +
			'</table>'
		);
		
		var data = {
			columns: [],
			data: []
		};
		
		Ext.Array.each(grid.getColumns(), function(column) {
			if (!column.isHidden()) {
				data.columns.push({
					title: column.text
				});
			}
		});
		
		
		grid.getStore().each(function(record){
			var fields = [];
			
			Ext.Array.each(grid.getColumns(), function(column) {
				if (!column.isHidden()) {
					fields.push({
						value: record.get(column.dataIndex)
					});
				}
			});
			
			data.data.push({
				fields: fields
			});
		});
		
		return template.apply(data);
	}
		
	
});

