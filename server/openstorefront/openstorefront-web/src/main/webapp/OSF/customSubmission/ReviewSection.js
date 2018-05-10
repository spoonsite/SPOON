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

/* global Ext, reviewSections, section */

Ext.define('OSF.customSubmission.ReviewSection', {
	extend: 'Ext.panel.Panel',
	xtype: 'osf-customsubmission-reviewsection',
	
	scrollable: true,	
	bodyStyle: 'padding: 20px',
	title: 'Review Submission',
	titleAlign: 'center',
	
	displayReviewSections: function(reviewSections, submissionForm) {
		var reviewPanel = this;
		
		reviewPanel.removeAll();
		reviewPanel.reviewSections = reviewSections;
		
		window.submissionFormJump = function(sectionIndex) {
			submissionForm.jumpToSection(sectionIndex);
		};
		
		var displayItems = [{
			html: 'Review ALL sections and verify input before submitting.'	
		}];
		Ext.Array.each(reviewSections, function(section){
			var validMessage = '<i class="fa fa-lg fa-close text-danger" title="Valid"></i>';
			if (section.component.valid()) {
				validMessage = '<i class="fa fa-lg fa-check text-success" title="Invalid"></i>';
			}
			var questions = section.component.getReviewableQuestions();
			var questionsConfig = [];
			if (questions.length > 0) {
				questionsConfig.push({
					data: questions,					
					tpl: new Ext.XTemplate(
						'<tpl for=".">' +	
							'<div><span>{question}</span> '+
								'<tpl if="valid"><i class="fa fa-lg fa-check text-success" title="Valid"></i></tpl>' +
								'<tpl if="!valid"><i class="fa fa-lg fa-close text-danger" title="Invalid"></i></tpl>' +												
								'<div style="margin-top: 10px; margin-bottom: 10px;">' +
								'{value}' +
								'</div>' +
							'</div>' +
						'</tpl>'	
					)	
				});
			} else {
				questionsConfig.push({
					html: 'No Form Fields'	
				});
			}
			
			displayItems.push({
				xtype: 'fieldset',
				title: '<a href="#" onclick="submissionFormJump(' + section.index + ')">' + section.name + '</a> ' + validMessage,
				items: questionsConfig,
				margin: '0 0 20 0'
			});
			
		});
		
		reviewPanel.add(displayItems);
	},
	
	allSectionsValid: function() {
		var reviewPanel = this;
		
		var valid = false;
		
		if (reviewPanel.reviewSections) {
			valid = true;
			Ext.Array.each(reviewSections, function(section){
				if (!section.valid()) {
					valid = false;
				} 
			});
		}
		
		return valid;
	}	
	
	
});
