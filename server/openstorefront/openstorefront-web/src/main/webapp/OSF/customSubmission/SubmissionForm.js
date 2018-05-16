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
		

		
		//save and then update the ids
		//server to create a new one
		
	},
	
	loadTemplate: function(submissionFormTemplate, entryType, userSubmission) {
		var submissionForm = this;
		submissionForm.template = submissionFormTemplate;
		submissionForm.entryType = entryType;
		submissionForm.currentSection = 0;
		if (!userSubmission)	{
			userSubmission = submissionForm.initializeUserSubmission(submissionForm.template);
		}
		submissionForm.userSubmission = userSubmission;
		
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
				});				
			} else {
				var sectionComponent = Ext.create('OSF.customSubmission.Section', {				
				});
				sectionComponent.load(section, submissionFormTemplate, userSubmission);			
				
				submissionForm.formSections.push(section);
			}
			section.component = sectionComponent;
			sectionComponents.push(sectionComponents);
			
		});
		submissionForm.add(sectionComponents);		
		submissionForm.displayCurrentSection();
						
		submissionForm.fireEvent('ready', submissionForm);
	},
	
	displayCurrentSection: function() {
		var submissionForm = this;		
		var section = submissionForm.template.sections[submissionForm.currentSection];
		submissionForm.setActiveItem(section.component);
		//section.component.setScrollY(0, true);
		
		if (section.review) {
			section.component.displayReviewSections(submissionForm.formSections, submissionForm);
		}
		
		if (submissionForm.progressCallback) {
			submissionForm.progressCallback(submissionForm.currentSection, submissionForm.template.sections.length);
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
	
	getUserData: function() {
		var submissionForm = this;
		
		
		


		
	}
	
});
	
