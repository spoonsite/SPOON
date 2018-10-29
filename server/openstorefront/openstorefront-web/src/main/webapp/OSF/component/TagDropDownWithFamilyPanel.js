/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
Ext.define('OSF.component.TagDropDownWithFamilyPanel', {
	extend: 'Ext.panel.Panel',
	xtype: 'familyTagDropPanel',

	layout: 'hbox',
	componentId: null,
	processTagsCallback: null,
	parentPanelString: null,
	refreshCallBack: null,

	initComponent: function(){
		this.callParent();

		var tagDropDownWithFamilyPanel = this;
		// console.log(componentId);

        var saveTagToComponent = function(tag){
            // If there are related tags then show the window.
            if (!tag || tag === '') {
                Ext.getCmp('tagField').markInvalid('Tag name required');
            } else {	
				// Otherwise just add the tag.
				if(tagDropDownWithFamilyPanel.parentPanelString){
					Ext.getCmp(tagDropDownWithFamilyPanel.parentPanelString).setLoading('Tagging Entry...');
				}
                Ext.Ajax.request({
                    url: 'api/v1/resource/components/' + tagDropDownWithFamilyPanel.componentId + '/tags',
                    method: 'POST',
                    jsonData: {
                        text: tag
                    },
                    callback: function(){
						if(tagDropDownWithFamilyPanel.parentPanelString){
							Ext.getCmp(tagDropDownWithFamilyPanel.parentPanelString).setLoading(false);
						}
                        
                    },
                    success: function(response, opt){
                        var tag = Ext.decode(response.responseText);
                        var tagField = Ext.getCmp('tagField');
    
                        if (typeof tag.errors === 'undefined') {
                            if(tagDropDownWithFamilyPanel.processTagsCallback){
                                tagDropDownWithFamilyPanel.processTagsCallback(tag);
                            }
                            tagField.reset();
							tagField.getStore().load();
                        }
                        else {
                            tagField.reset();
                            tagField.markInvalid(tag.errors.entry[0].value);
						}
						if(tagDropDownWithFamilyPanel.refreshCallBack){
							tagDropDownWithFamilyPanel.refreshCallBack();
						}
                    }
                });	
            }
        }

		var actionAddTag = function(tag, valueWasSelected) {

			if(valueWasSelected){
				saveTagToComponent(tag);
			} else {

				var familyTagWindow = Ext.create('Ext.window.Window', {
					itemId: 'familyTagWindowitemId',
					title: 'Are you sure you want to add a new tag? ',
					width: '70%',
					height: 450,
					y: 200,
					modal: true,
					layout: 'fit',
					closeAction: 'destroy',
					processTagsCallback: null,
					items: [
						{
							xtype: 'panel',
							itemId: 'topPanel',
							preventDefaultAction: true,
							layout: 'fit',
							items: [
								{
									xtype: 'gridpanel',
									itemId: 'gridPanelItemId',
									layout: 'fit',
									viewConfig: {
										enableTextSelection: true
									},
									store: Ext.create('Ext.data.Store', {
										fields: [			
											{
												name: 'createDts',
												type:	'date',
												dateFormat: 'c'
											}														
										],
										autoLoad: true,
										proxy: {
											type: 'ajax',
											url: 'api/v1/resource/components/' + tagDropDownWithFamilyPanel.componentId + '/relatedtags'
										}
									}),
									columns: [
										{ text: 'Related Tags', dataIndex: 'text', flex: 1, minWidth: 100 }
									],
									listeners: {
										selectionchange: function(grid, record, index, opts){
											var fullgrid = this;
											if (fullgrid.getSelectionModel().getCount() === 1) {
												familyTagWindow.getComponent('dropdowntoolbar').getComponent('useOldTag').setDisabled(false);
											} else {
												familyTagWindow.getComponent('dropdowntoolbar').getComponent('useOldTag').setDisabled(false);
											}
										}						
									},
		
								}
							]
						}
						
					],
					dockedItems: [
						{
							xtype: 'panel',
							itemId: 'headerPanel',
							dock: 'top',
							margin: '0 0 0 10',
							html: '<h3> Are you sure that you would like to add a new tag? </br>' +
							' Please see other possible matches below. </br>' +
							' New Tag Name: ' + '<font color="red">' + tag + '</font>' + '</h3>'
						},
						{
							xtype: 'toolbar',
							itemId: 'dropdowntoolbar',
							dock: 'bottom',
							items: [
								{
									text: 'Yes I am sure. Add the new tag.',
									iconCls: 'fa fa-lg fa-save icon-button-color-save',
									handler: function(){
										saveTagToComponent(tag);
										this.up('window').close();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'No, I want to use the selected prexisting tag.',
									itemId: 'useOldTag',
									iconCls: 'fa fa-lg fa-save icon-button-color-save',
									disabled: true,
									handler: function(){
										saveTagToComponent(familyTagWindow.getComponent('topPanel').getComponent('gridPanelItemId').getSelection()[0].data.text);
										this.up('window').close();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Cancel',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									handler: function(){
										this.up('window').close();
									}
								}
							]
						}
					]
				});

				var relatedParentTags;
				if(tagDropDownWithFamilyPanel.parentPanelString){
					Ext.getCmp(tagDropDownWithFamilyPanel.parentPanelString).setLoading('Checking Sources');
				}
				
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + tagDropDownWithFamilyPanel.componentId + '/relatedtags',
					method: 'GET',										
					success: function(response, opts){
						if(tagDropDownWithFamilyPanel.parentPanelString){
							Ext.getCmp(tagDropDownWithFamilyPanel.parentPanelString).setLoading(false);
						}
						relatedParentTags = Ext.decode(response.responseText);
						if (Array.isArray(relatedParentTags) && relatedParentTags.length) {
							familyTagWindow.show();
						} else {
							saveTagToComponent(tag);
						}
					},
					failure: function(){
						if(tagDropDownWithFamilyPanel.parentPanelString){
							Ext.getCmp(tagDropDownWithFamilyPanel.parentPanelString).setLoading(false);
						}
						Ext.toast({
							title: 'validation error. the server could not process the request. ',
							html: 'try changing the tag field.',
							width: 550,
							autoclosedelay: 10000
						});
					}
				});
			}
		};

		var children = [
			{
				xtype: 'combobox',
				id: 'tagField',
				emptyText: 'Select',
				labelSeparator: '',
				width: 175,
				typeAhead: true,
				anyMatch: true,
				queryMode: 'local',
				labelAlign: 'top',
				name: 'text',																				
				flex: 1,
				fieldLabel: 'Add Tag',
				forceSelection: false,
				valueField: 'text',
				displayField: 'text',										
				margin: '0 10 10 0',
				maxLength: 120,
				store: {
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: 'api/v1/resource/components/' + tagDropDownWithFamilyPanel.componentId + '/tagsfree'
					}
				},
				valueWasSelected: false,
				listeners:{
					specialkey: function(field, e) {
						var value = this.getValue();
						if (e.getKey() === e.ENTER && !Ext.isEmpty(value)) {
							actionAddTag(value, Ext.getCmp('tagField').getSelection() ? true : false);
						}	
					}
					
				}
			},
			{
				xtype: 'button',
				text: 'Add',
				iconCls: 'fa fa-plus',
				margin: '30 0 0 0',
				minWidth: 75,
				handler: function(){
					var tagField = Ext.getCmp('tagField');
					if (tagField.isValid()) {
						actionAddTag(tagField.getValue(), Ext.getCmp('tagField').getSelection() ? true : false);
					}
				}
			}
		];
		tagDropDownWithFamilyPanel.add(children);
	},
	loadData: function(data){
	}
});