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
	extend: 'Ext.panel.Grid',
	
	submissionTemplateData: {
		fieldType: null,
		mappingType: 'COMPLEX',
		questionNumber: null,
		label: null,
		labelTooltip: null,
		required: null
	},
	
	initComponent: function () {
		var submissionGrid = this;
		submissionGrid.callParent();
		submissionGrid.display();
	},
	
	display: function() {
		var submissionGrid = this;
		
		var questionNumber = null;
		if (submissionGrid.submissionTemplateData.questionNumber) {
			questionNumber = {
				xtype: 'tbtext',
				text: submissionGrid.submissionTemplateData.questionNumber,
				cls: 'submission-question-number'
			};
		}
		
		var label = null;
		if (submissionGrid.submissionTemplateData.label) {
			var tooltip = '';
			if (submissionGrid.submissionTemplateData.labelTooltip) {
				tooltip = ' <i class="fa fa-lg fa-question-circle"  data-qtip="'+submissionGrid.submissionTemplateData.labelTooltip+'"></i>';
			}
			
			label = {
				xtype: 'tbtext',
				text: submissionGrid.submissionTemplateData.label + tooltip,
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
				toolbar.push(questionNumber);
			}
			if (label) {
				toolbar.push(label);
			}			
			submissionGrid.addDock(toolbar);
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

