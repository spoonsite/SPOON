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
	maxWidth: 1024,	
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
		
		var questionNumber = null;
		if (submissionGrid.fieldTemplate.questionNumber) {
			questionNumber = {
				xtype: 'tbtext',
				text: submissionGrid.fieldTemplate.questionNumber,
				cls: 'submission-question-number'
			};
		}
		
		var label = null;
		if (submissionGrid.fieldTemplate.label) {
			var tooltip = '';
			if (submissionGrid.fieldTemplate.labelTooltip) {
				tooltip = ' <i class="fa fa-lg fa-question-circle"  data-qtip="'+submissionGrid.fieldTemplate.labelTooltip+'"></i>';
			}
			
			label = {
				xtype: 'tbtext',
				text: submissionGrid.fieldTemplate.label + tooltip,
				cls: 'submission-label'
			};
		}		
		
		if (questionNumber || label) {
			
			var toolbar = {
				xtype: 'toolbar',
				dock: 'top',
				items: []
			};
			if (questionNumber) {
				toolbar.items.push(questionNumber);
			}
			if (label) {
				toolbar.items.push(label);
			}			
			submissionGrid.addDocked(toolbar, 0);
		}			
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
		return true;
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
	}
	
	
	
});

