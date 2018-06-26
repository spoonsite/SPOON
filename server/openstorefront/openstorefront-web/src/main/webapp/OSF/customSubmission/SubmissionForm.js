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

/* global Ext, sectionData */

Ext.define('OSF.customSubmission.SubmissionForm', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-submissionform',
	requires: [
		'OSF.customSubmission.Section',
		'OSF.customSubmission.ReviewSection'
	],

	scrollable: true,
	layout: 'card',

	initComponent: function () {
		var submissionForm = this;
		submissionForm.callParent();
	},
	
	remoteLoadTemplate: function(templateId, entryType, userSubmissionId) {
		var submissionForm = this;
				
		submissionForm.setLoading('Loading template...');
		Ext.Ajax.request({
			url: 'api/v1/resource/submissiontemplates/' + templateId,
			callback: function() {
				submissionForm.setLoading(false);
			},
			success: function(response, opts) {
				submissionForm.template = Ext.decode(response.reponseText);
				if (userSubmissionId) {
					submissionForm.setLoading('Loading submission...');
					Ext.Ajax.request({
						url: 'api/v1/resource/usersubmissions/' + userSubmissionId,
						callback: function() {
							submissionForm.setLoading(false);
						},
						success: function(response, opts) {
							submissionForm.userSubmission = Ext.decode(response.reponseText);
							submissionForm.loadTemplate(submissionForm.template, entryType, submissionForm.userSubmission);
						}
					});
					
				} else {				
					submissionForm.loadTemplate(submissionForm.template, entryType);
				}				
			}
		});
		
	},
	
	initializeUserSubmission: function() {
		//Create a new submission form and save
		var submissionForm = this;
		
		//prompt for submission name
		if (submissionForm.initialSubmissionName){
			completeInitialSave();			
		} else {
			var promptWin = Ext.create('Ext.window.Window', {
				title: 'Submission Name',
				modal: true,
				closable: false,
				alwaysOnTop: true,
				closeAction: 'destroy',
				width: 400,
				height: 200,
				layout: 'fit',
				onEsc: Ext.emptyFn,
				items: [
					{
						xtype: 'form',
						bodyStyle: 'padding: 10px;',
						layout: 'anchor',
						items: [
							{
								xtype: 'textfield',
								fieldLabel: 'Enter Inital Submission Name<span class="field-required" />',
								labelAlign: 'top',
								width: '100%',
								allowBlank: false,
								name: 'name'								
							}
						],
						dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										xtype: 'tbfill'
									},
									{
										text: 'Ok',
										formBind: true,
										width: 75,
										handler: function() {
											var data = this.up('form').getValues();
											submissionForm.initialSubmissionName = data.name;
											completeInitialSave();
											promptWin.close();
										}
									},
									{
										xtype: 'tbfill'
									}
								]
							}
						]
					}
				]								
			});
			promptWin.show();
		}
		
		
		
		var completeInitialSave = function() {		
			var userSubmission = {
				templateId : submissionForm.template.submissionTemplateId,
				componentType: submissionForm.entryType.componentType,
				submissionName: submissionForm.initialSubmissionName,
				fields: []		
			};	

			Ext.Ajax.request({
				url: 'api/v1/resource/usersubmissions',
				method: 'POST',
				jsonData: userSubmission,
				callback: function() {				
				},
				success: function(response, opts) {
					var savedSubmission = Ext.decode(response.responseText);
					submissionForm.userSubmission = savedSubmission;
					
					submissionForm.loadTemplate(
							submissionForm.template, 
							submissionForm.entryType, 
							submissionForm.userSubmission, 
							false
					);

					if (submissionForm.finishInitialSave) {
						submissionForm.finishInitialSave(savedSubmission);
					}					
				}
			});
		};
		
		return null;		
	},
	
	loadTemplate: function(submissionFormTemplate, entryType, userSubmission, createNewSubmission) {
		var submissionForm = this;
		
		var handleLoadTemplate = function(componentType) {
			//template get manipulated slightly
			submissionForm.template = Ext.clone(submissionFormTemplate);
			submissionForm.entryType = componentType;
			submissionForm.currentSection = 0;
			if (!userSubmission && createNewSubmission)	{
				userSubmission = submissionForm.initializeUserSubmission(submissionForm.template);
				if (!userSubmission) {
					return;
				}
			}
			submissionForm.userSubmission = userSubmission;

			if (!submissionForm.template.sections) {
				submissionForm.template.sections = [];
			}

			submissionForm.template.sections.push({
				name: 'Review Submission',
				instructions: 'Review submission and submit for approval',
				review: true
			});

			//create sections
			submissionForm.formSections = [];
			var sectionComponents = [];
			var index = 0;
			Ext.Array.each(submissionForm.template.sections, function(section){
				section.index = index++;			
				var sectionComponent;
				if (section.review) {
					sectionComponent = Ext.create('OSF.customSubmission.ReviewSection', {				
						componentType: componentType,
						submissionForm: submissionForm
					});				
				} else {
					sectionComponent = Ext.create('OSF.customSubmission.Section', {		
						componentType: componentType,
						submissionForm: submissionForm
					});
					sectionComponent.load(section, submissionFormTemplate, userSubmission);			

					submissionForm.formSections.push(section);
				}
				section.component = sectionComponent;					
				sectionComponents.push(sectionComponent);

			});
			submissionForm.add(sectionComponents);		
			submissionForm.displayCurrentSection(true);

			submissionForm.fireEvent('ready', submissionForm);			
		};	
			
		
		if (Ext.isString(entryType)) {
			
			Ext.Ajax.request({
				url: 'api/v1/resource/componenttypes/' + entryType,
				callback: function(){
				},
				success: function(response, opts) {
					var componentType = Ext.decode(response.responseText);
					handleLoadTemplate(componentType);
				}
			});			
		} else {
			handleLoadTemplate(entryType);
		}

	},
	
	displayCurrentSection: function(initialDisplay) {
		var submissionForm = this;		
		var section = submissionForm.template.sections[submissionForm.currentSection];
		submissionForm.setActiveItem(section.component);
		//section.component.setScrollY(0, true);
		Ext.defer(function(){
			submissionForm.updateLayout(true, true);
		}, 500);
		
		if (section.review) {
			section.component.displayReviewSections(submissionForm.formSections, submissionForm);
		}
		
		if (submissionForm.progressCallback) {
			submissionForm.progressCallback(submissionForm.currentSection, submissionForm.template.sections.length);
		}
		if (submissionForm.sectionChangeHandler) {
			submissionForm.sectionChangeHandler(initialDisplay);
		}
	},
	
	getSections: function() {
		var submissionForm = this;
		return submissionForm.template.sections;
	},
	
	getCurrentSection: function() {
		var submissionForm = this;
		return submissionForm.template.sections[submissionForm.currentSection];
	},
	
	hasNextSection: function() {
		var submissionForm = this;
		var hasSection = false;
		var nextIndex = submissionForm.currentSection + 1;
		if (submissionForm.template.sections) {
			if (nextIndex < submissionForm.template.sections.length) {
				hasSection = true;
			}
		}
		return hasSection;
	},
	
	nextSection: function() {
		var submissionForm = this;
		if (submissionForm.hasNextSection()) {
			submissionForm.currentSection += 1;
			submissionForm.displayCurrentSection();
		}
	},
	
	hasPreviousSection: function() {
		var submissionForm = this;
		var hasSection = false;
		var nextIndex = submissionForm.currentSection - 1;
		if (submissionForm.template.sections) {
			if (nextIndex >= 0) {
				hasSection = true;
			}
		}
		return hasSection;
	},	
	
	previousSection: function() {
		var submissionForm = this;
		if (submissionForm.hasPreviousSection()) {
			submissionForm.currentSection -= 1;
			submissionForm.displayCurrentSection();
		}		
	},
		
	jumpToSection: function(sectionIndex) {
		var submissionForm = this;
		
		if (sectionIndex >= 0 && sectionIndex < submissionForm.template.sections.length) {
			submissionForm.currentSection = sectionIndex;
			submissionForm.displayCurrentSection();
		}		
	},
	getFieldData: function(fieldId) {
		var submissionForm = this;
		
		var data = null;
		if (submissionForm.userSubmission) {
			if (submissionForm.userSubmission.fields) {
				Ext.Array.each(submissionForm.userSubmission.fields, function(field){
					if (field.templateFieldId ===  fieldId) {
						data = field.rawValue;
					}
				});
			}
		}
		return data;
	},	
	getUserData: function() {
		var submissionForm = this;
		
		if (!submissionForm.userSubmission) {
			submissionForm.userSubmission = {
				templateId : submissionForm.template.submissionTemplateId,
				componentType : submissionForm.entryType.componentType
			};
		}
		
		var userSubmissionFields = [];
		
		//go through section
		Ext.Array.each(submissionForm.template.sections, function(section){
			if (!section.review) {
				userSubmissionFields = userSubmissionFields.concat(section.component.getUserValues());			
			}
		});		
		
		//get field values
		submissionForm.userSubmission.fields = userSubmissionFields;
		
		return submissionForm.userSubmission;
	}
	
});
	
