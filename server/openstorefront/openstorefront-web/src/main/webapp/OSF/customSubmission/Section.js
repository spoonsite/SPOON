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
/* global Ext, CoreUtil, CoreService */

/* Author: cyearsley */

Ext.define('OSF.customSubmission.Section', {
	extend: 'Ext.form.Panel',
	alias: 'widget.osf-submissionform-section',
	requires: [
		'OSF.customSubmission.field.AttributesGrid',
		'OSF.customSubmission.field.ContactsGrid',
		'OSF.customSubmission.SubmissionFormWrapper',
		'OSF.customSubmission.field.DependenciesGrid',
		'OSF.customSubmission.field.Text'
	],
	
	width: '100%',
	showSectionName: true,
	scrollable: true,
	bodyStyle: 'padding: 20px;',
	layout: 'anchor',
	
	initComponent: function () {
		var section = this;
		section.callParent();
	},
	
	load: function(sectionData, template, userSubmission) {
		
		var section = this;
		
		var itemsToAdd = [];
		if (section.showSectionName) {
			itemsToAdd.push({
				xtype: 'panel',
				width: '100%',
				data: sectionData,
				tpl: new Ext.XTemplate(
					'<div class="submission-section-title">{name}</div>'
				)
			});		
		}
		itemsToAdd.push({
				xtype: 'panel',
				width: '100%',
				margin: '0 0 20 0',
				data: sectionData,
				tpl: new Ext.XTemplate(
					'<div class="submission-instructions">{instructions}</div>'
				)
		});
		
		
		Ext.Array.each(sectionData.fields, function(field){

			var createQuestionLabel = function() {
				var fullLabel = '';
				if (field.questionNumber) {
					fullLabel += '<span class="submission-question-number">' +field.questionNumber + '</span>  ';
				}

				if (field.label) {
					var tooltip = '';
					if (field.labelTooltip) {
						tooltip = ' <i class="fa fa-lg fa-question-circle"  data-qtip="' + field.labelTooltip+'"></i>';
					}
					var required = field.required ? '<span class="field-required" />' : '';			

					fullLabel += '<span class="submission-label">' + field.label + '</span>'
							+ required + tooltip;
				}	
				return fullLabel;
			};			
			
			var defaults = {
				fieldTemplate: field,
				createQuestionLabel: createQuestionLabel,
				margin: '0 0 20 0'
			};
			switch(field.fieldType) {
				case 'ATTRIBUTE_MULTI':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-attributegrid'
					}));
				break;
				case 'CONTACT':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-formwrapper',
						actualForm: {
							xtype: 'osf-submissionform-contact'
						}						
					}));
				break;				
				case 'CONTACT_MULTI':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-contactgrid'						
					}));
				break;	
				case 'EXT_DEPEND':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-formwrapper',
						actualForm: {
							xtype: 'osf-submissionform-dependency'
						}						
					}));
				break;					
				case 'EXT_DEPEND_MULTI':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-dependencygrid'						
					}));
				break;
				case 'MEDIA':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-formwrapper',
						actualForm: {
							xtype: 'osf-submissionform-dependency'
						}						
					}));
				break;					
				case 'MEDIA_MULTI':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-dependencygrid'						
					}));
				break;			
				
				case 'TEXT':
					itemsToAdd.push(Ext.apply(defaults, {
						xtype: 'osf-submissionform-text',
						labelAlign: field.labelAlign ? field.labelAlign : 'top'
					}));
				break;	
			}			
		});
				
		section.add(itemsToAdd);
	},		
	
	getUserValues: function () {
		var data = [];
		var section = this;
				
		Ext.Array.forEach(section.items.items, function (field) {
			data.push(field.getUserData());
		});
		return data;
	}
	
});
