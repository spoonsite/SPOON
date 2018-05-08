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

/* global Ext, reviewSections */

Ext.define('OSF.customSubmission.ReviewSection', {
	extend: 'Ext.panel.Panel',
	xtype: 'osf-customsubmission-reviewsection',
	
	scrollable: true,	
	
	displayReviewSections: function(reviewSections) {
		var reviewPanel = this;
		
		reviewPanel.removeAll();
		reviewPanel.reviewSections = reviewSections;
		
		var displayItems = [];
		Ext.Array.each(reviewSections, function(section){
			var validMessage = '<i class="fa fa-lg fa-close alert-danger"';
			if (section.valid()) {
				validMessage = '<i class="fa fa-lg fa-check alert-success"';
			}
			
			displayItems.push({
				xtype: 'fieldSet',
				title: section.name + ' ' + validMessage,
				items: [
					//section.allQuestions
				],
				margin: '0 0 20 0'
			});
			
		});
		
		reviewPanel.add(displayItems);
	},
	
	allSectionValid: function() {
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
