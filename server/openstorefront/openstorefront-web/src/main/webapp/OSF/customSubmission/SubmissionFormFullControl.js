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

Ext.define('OSF.customSubmission.SubmissionFormFullControl', {
	extend: 'Ext.panel.Panel',
	xtype: 'osf-customSubmission-SubmissionformFullControl',
	requires: [
		'OSF.customSubmission.SubmissionForm'
	],

	layout: 'border',
	showJumpSection: false,
	showNavigationPanel: true,
	collaspeNavAtFirst: false,
	hideSave: false,
	showCustomButton: false,
	previewMode: false,
	customButtonText: 'Close',
	customButtonIconCls: 'fa fa-2x fa-close icon-vertical-correction icon-button-color-warning',
	
	items: [
		{
			xtype: 'osf-submissionform',
			region: 'center',
			itemId: 'submissionForm',
			border: true,
			progressCallback: function(currentSectionIndex, totalSections) {
				var submissionFormFullControl = this.up('panel');
				var total = totalSections - 1;
				submissionFormFullControl.queryById('progress').updateProgress(currentSectionIndex / total, (currentSectionIndex + 1) + ' / ' + totalSections, true);
				
				submissionFormFullControl.updateNavPanel(currentSectionIndex);
			},
			sectionChangeHandler: function(initialDisplay) {
				var submissionFormFullControl = this.up('panel');
				var form = submissionFormFullControl.queryById('submissionForm');
				submissionFormFullControl.checkNextPrevious();
				
				if (form.userSubmission && !initialDisplay) {
					submissionFormFullControl.saveSubmission();
				}
			}
		},
		{
			xtype: 'panel',
			itemId: 'navPanel',
			title: 'Sections',
			region: 'west',
			scrollable: true,
			split: true,
			layout: 'anchor',
			titleCollapse: true,
			collapsible: true,
			border: true,
			bodyStyle: 'padding: 10px; background: white;',
			width: 250			
		}
	],	
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					xtype: 'tbtext',
					text: 'Progress:'
				},
				{
					xtype: 'progressbar',
					itemId: 'progress',
					width: '30%',
					maxWidth: 300,
					animate: true,
					textTpl: '0 / 0'
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Jump To Section',
					itemId: 'jumpSectionNav',
					scale: 'medium',
					hidden: true,
					menu: {						
					}
				}
			]
		},
		{
			xtype: 'toolbar',
			dock: 'bottom',
			items: [
				{
					text: 'Previous',
					itemId: 'previousSection',
					iconCls: 'fa fa-2x fa-arrow-left icon-vertical-correction icon-button-color-default',
					scale: 'medium',
					disabled: true,
					handler: function() {
						var submissionFormFullControl = this.up('panel');
						var form = submissionFormFullControl.queryById('submissionForm');
						form.previousSection();
						submissionFormFullControl.checkNextPrevious();
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Close',					
					itemId: 'custom',
					iconCls: 'fa fa-2x fa-close icon-vertical-correction icon-button-color-warning',
					scale: 'medium',
					hidden: true,
					handler: function() {
						var submissionFormFullControl = this.up('panel');

						if (submissionFormFullControl.customButtonHandler) {
							submissionFormFullControl.customButtonHandler();						
						}
						
					}
				},
				{
					text: 'Save',
					itemId: 'save',
					iconCls: 'fa fa-2x fa-save icon-vertical-correction icon-button-color-save',
					scale: 'medium',			
					handler: function() {
						var submissionFormFullControl = this.up('panel');
						submissionFormFullControl.saveSubmission();
						Ext.Msg.alert('Warning', 'Saving does not submit a submission for review.<br/>' +
							'To submit a submission proceed to the end of the form and click "Submit For Approval"');
							Ext.Msg.alert({
								title: 'Warning',
								message: 'Saving does not submit a submission for review.<br/>' +
								'To submit a submission proceed to the end of the form and click "Submit For Approval"',
								icon: Ext.Msg.WARNING,
								buttons: Ext.Msg.OK
							});
					}
				},
				{
					text: 'Submit For Approval',
					itemId: 'submitApproval',
					iconCls: 'fa fa-2x fa-check-square-o icon-vertical-correction icon-button-color-save',
					scale: 'medium',
					hidden: true,
					handler: function() {
						var submissionFormFullControl = this.up('panel');
						submissionFormFullControl.submitSumissionForApproval();						
					}
				},				
				{
					xtype: 'tbfill'
				},
				{
					text: 'Next',
					itemId: 'nextSection',
					iconCls: 'fa fa-2x fa-arrow-right  icon-vertical-correction icon-button-color-default',
					scale: 'medium',
					handler: function() {
						var submissionFormFullControl = this.up('panel');
						var form = submissionFormFullControl.queryById('submissionForm');
						form.nextSection();	
						submissionFormFullControl.checkNextPrevious();
					}					
				}
			]
		}
	],
	initComponent: function () {
		var submissionFormFullControl = this;
		submissionFormFullControl.callParent();		
		
		if (submissionFormFullControl.showJumpSection) {
			submissionFormFullControl.queryById('jumpSectionNav').setHidden(false);
		}	
		
		if (!submissionFormFullControl.showNavigationPanel) {
			submissionFormFullControl.queryById('navPanel').setHidden(true);
		}
		
		if (submissionFormFullControl.hideSave) {
			submissionFormFullControl.queryById('save').setHidden(true);
		}
		
		if (submissionFormFullControl.showCustomButton) {
			submissionFormFullControl.queryById('custom').setText(submissionFormFullControl.customButtonText);
			submissionFormFullControl.queryById('custom').setIconCls(submissionFormFullControl.customButtonIconCls);
			submissionFormFullControl.queryById('custom').setHidden(false);
		}		
		
		submissionFormFullControl.queryById('submitApproval').setText(submissionFormFullControl.submitText || 'Submit For Approval');
		
		
		submissionFormFullControl.queryById('submissionForm').previewMode = submissionFormFullControl.previewMode;
		submissionFormFullControl.queryById('submissionForm').finishInitialSave = submissionFormFullControl.finishInitialSave;
		
	},
	updateNavPanel: function(currentSectionIndex) {
		var submissionFormFullControl = this;
		var navPanel = submissionFormFullControl.queryById('navPanel');
		var selectedComponents = navPanel.query();
		if (selectedComponents) {
			Ext.Array.each(selectedComponents, function(navButton){
				navButton.removeCls('submission-section-nav-select');
			});
		}
		
		Ext.Array.each(navPanel.items.items, function(navButton) {
			if (navButton.sectionIndex === currentSectionIndex) {
				navButton.addCls('submission-section-nav-select');	
				
				//auto scroll is problemmatic
				//54 - relative offset
				//var newY = navButton.getY() - (navPanel.getY() + 54);
				//navPanel.scrollTo(0, newY, true);								
			}	
		});		
	},
	
	remoteLoad: function(submissionTemplateId, entryType, userSubmissionId) {
		var submissionFormFullControl = this;
		
		submissionFormFullControl.setLoading(true);
		var form = submissionFormFullControl.queryById('submissionForm');
		submissionFormFullControl.handleInitEvent();
		form.remoteLoadTemplate(submissionTemplateId, entryType, userSubmissionId);		

	},
	
	checkNextPrevious : function() {
		var submissionFormFullControl = this;
		var form = submissionFormFullControl.queryById('submissionForm');		
		submissionFormFullControl.queryById('previousSection').setDisabled(true);					
		submissionFormFullControl.queryById('nextSection').setDisabled(true);

		if (form.hasPreviousSection()) {
			submissionFormFullControl.queryById('previousSection').setDisabled(false);					
		}
		if (form.hasNextSection()) {
			submissionFormFullControl.queryById('nextSection').setDisabled(false);					
		}
		var section = form.getCurrentSection();
		if (section && section.review) {
			if (!submissionFormFullControl.hideSave) {
				submissionFormFullControl.queryById('save').setHidden(true);
				submissionFormFullControl.queryById('submitApproval').setHidden(false);
			}
			
			if (!section.component.allSectionsValid()) {
				submissionFormFullControl.queryById('submitApproval').setDisabled(true);
			} else {
				submissionFormFullControl.queryById('submitApproval').setDisabled(false);
			}
			
		} else {
			if (!submissionFormFullControl.hideSave) {
				submissionFormFullControl.queryById('save').setHidden(false);
				submissionFormFullControl.queryById('submitApproval').setHidden(true);
			}
		}		
	},
	
	handleInitEvent: function() {
		var submissionFormFullControl = this;
		var form = submissionFormFullControl.queryById('submissionForm');
		if (submissionFormFullControl.initHandler) {
			form.un('ready', submissionFormFullControl.initHandler);
		} else {
			submissionFormFullControl.initHandler = function(subForm) {
				submissionFormFullControl.setLoading(false);
			
				var sectionMenu = [];
				var index = 0;
				Ext.Array.each(form.getSections(), function(section) {
					sectionMenu.push({
						text: section.name,
						sectionIndex: index,
						section: section,
						handler: function() {							
							form.jumpToSection(this.sectionIndex);
							submissionFormFullControl.checkNextPrevious();
						}
					});
					index++;
				});				
				submissionFormFullControl.queryById('jumpSectionNav').getMenu().add(sectionMenu);
				submissionFormFullControl.checkNextPrevious();
				
				var sectionButtons = [];
				var reviewSection = [];
				index = 0;
				Ext.Array.each(form.getSections(), function(section) {
					
					var navButton = {
						xtype: 'button',
						text: section.name,
						textAlign: 'left',
						width: '100%',
						margin: '0 0 20 0',
						sectionIndex: index,
						section: section,
						handler: function() {							
							form.jumpToSection(this.sectionIndex);
							submissionFormFullControl.checkNextPrevious();
						}
					};
					
					if (section.review) {
						navButton.dock = 'bottom';
						navButton.textAlign = 'center';
						navButton.iconCls = 'fa fa-lg fa-check-square-o';
						navButton.cls = 'submission-form-reviewbutton';
						reviewSection.push(navButton);
					} else {
						sectionButtons.push(navButton);
					}
					index++;
				});	
				
				submissionFormFullControl.queryById('navPanel').add(sectionButtons);
				submissionFormFullControl.queryById('navPanel').addDocked(reviewSection);
				submissionFormFullControl.updateNavPanel(0);				
								
			};
		}		
		form.on('ready', submissionFormFullControl.initHandler);
	},
	
	load: function(submissionTemplate, entryType, userSubmission, createNewSubmission) {
		var submissionFormFullControl = this;
		
		submissionFormFullControl.setLoading(true);
		var form = submissionFormFullControl.queryById('submissionForm');
		
		submissionFormFullControl.handleInitEvent();
		form.loadTemplate(submissionTemplate, entryType, userSubmission, createNewSubmission);		
		
	},
	
	saveSubmission: function() {
		var submissionFormFullControl = this;
		
		if (submissionFormFullControl.previewMode) {
			//don't save			
			return;
		}
		
		var form = submissionFormFullControl.queryById('submissionForm');						
		var userSubmission = form.getUserData();		
		
		var method = 'POST';
		var endingURL = '';
		if (userSubmission.userSubmissionId) {
			method = 'PUT';
			endingURL = '/' + userSubmission.userSubmissionId;
		}
		
		submissionFormFullControl.setLoading("Saving...");
		Ext.Ajax.request({
			url: 'api/v1/resource/usersubmissions' + endingURL,
			method: method,
			jsonData: userSubmission,
			callback: function() {		
				submissionFormFullControl.setLoading(false);
			},
			success: function(response, opts) {
				Ext.toast('Saved Successfully');
			}
		});
	},
	
	submitSumissionForApproval: function() {
		var submissionFormFullControl = this;
		var form = submissionFormFullControl.queryById('submissionForm');
		
		
		var userSubmission = form.getUserData();	
		
		if (submissionFormFullControl.verifyMode) {
			
			submissionFormFullControl.setLoading("Verify Submission...");
			Ext.Ajax.request({
				url: 'api/v1/resource/submissiontemplates/' + userSubmission.templateId + '/verify/' + userSubmission.userSubmissionId,
				method: 'PUT',
				jsonData: userSubmission,
				callback: function() {		
					submissionFormFullControl.setLoading(false);
				},
				success: function(response, opts) {
					var data;
					try {
						data = Ext.decode(response.responseText);
					} catch(e){						
					}
					if (data && !data.success) {
						var errorMessage = '';
						Ext.Array.each(data.errors.entry, function(errorItem){
							errorMessage += '<b>' + errorItem.key + ':</b> ' + errorItem.value + '<br>';
						});
						
						Ext.Msg.show({
							title: 'Unable to Verify',
							message: 'Check Input and make sure template has all required Fields/Attributes. <br><br>' + errorMessage,
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.ERROR,
							fn: function(btn) {
							}
						});
						
					} else {
						Ext.toast('Verified Successfully');					

						if (submissionFormFullControl.submissionSuccess) {
							submissionFormFullControl.submissionSuccess();
						}
					}
				}
			});
			
		} else {				
			if (userSubmission.originalComponentId) {
				submissionFormFullControl.setLoading("Submitting Change Request...");
				Ext.Ajax.request({
					url: 'api/v1/resource/usersubmissions/' + userSubmission.userSubmissionId + '/submitchangeforapproval',
					method: 'PUT',
					jsonData: userSubmission,
					callback: function() {		
						submissionFormFullControl.setLoading(false);
					},
					success: function(response, opts) {
						var data;
						try {
							data = Ext.decode(response.responseText);
						} catch(e){						
						}
						if (data && !data.success) {
							var errorMessage = '';
							Ext.Array.each(data.errors.entry, function(errorItem){
								errorMessage += '<b>' + errorItem.key + ':</b> ' + errorItem.value + '<br>';
							});

							Ext.Msg.show({
								title: 'Unable to Submit Change Request',
								message: 'Check input and adjust the following.<br><br>' + errorMessage,
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) {
								}
							});

						} else {	
							Ext.toast('Change Request Submitted Successfully');

							if (submissionFormFullControl.submissionSuccess) {
								submissionFormFullControl.submissionSuccess();
							}
						}	
					}
				});
			} else {		
				submissionFormFullControl.setLoading("Submitting Entry...");
				Ext.Ajax.request({
					url: 'api/v1/resource/usersubmissions/' + userSubmission.userSubmissionId + '/submitforapproval',
					method: 'PUT',
					jsonData: userSubmission,
					callback: function() {		
						submissionFormFullControl.setLoading(false);
					},
					success: function(response, opts) {
						var data;
						try {
							data = Ext.decode(response.responseText);
						} catch(e){						
						}
						if (data && !data.success) {
							var errorMessage = '';
							Ext.Array.each(data.errors.entry, function(errorItem){
								errorMessage += '<b>' + errorItem.key + ':</b> ' + errorItem.value + '<br>';
							});

							Ext.Msg.show({
								title: 'Unable to Submit Entry',
								message: 'Check input and adjust the following.<br><br>' + errorMessage,
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR,
								fn: function(btn) {
								}
							});

						} else {						

							Ext.toast('Entry Submitted Successfully');

							if (submissionFormFullControl.submissionSuccess) {
								submissionFormFullControl.submissionSuccess();
							}
						}
					}
				});
			}
		}
		
	}	
	
});
