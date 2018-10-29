/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/* global Ext, CoreUtil */
Ext.define('OSF.component.SubmissionFamilyTagWindow', {
    extend: 'Ext.window.Window',
    xtype: 'submissionFamilyTagWindow',

    title: 'Are you sure you want to add a new tag? ',
    width: '70%',
    height: 450,
    y: 200,
    modal: true,
    layout: 'fit',
    closeAction: 'destroy',
    returnTagInfo: null,
    componentEntryType: null,
    alwaysOnTop: 9999,
    possibleNewTag: null,

    initComponent: function(){
        this.callParent();
        var familyTagWindow = this;
        familyTagWindow.winText = '<h3> Are you sure that you would like to add a new tag? </br> ' + 
        ' Please see other possible matches below.</h3>';

        var viewPanel = Ext.create('Ext.panel.Panel',{
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
                            url: (familyTagWindow.componentEntryType) ? 
                            ('api/v1/resource/components/' + familyTagWindow.componentEntryType + '/relatedtypetags') : 
                            ('api/v1/resource/components/tags')
                            
                        }
                    }),
                    columns: [
                        { text: 'Related Tags', dataIndex: 'text', flex: 1, minWidth: 100 }
                    ],
                    listeners: {
                        selectionchange: function(grid, record, index, opts){
                            var fullgrid = this;
                            if (fullgrid.getSelectionModel().getCount() === 1) {
                                familyTagWindow.queryById('dropdowntoolbar').getComponent('useOldTag').setDisabled(false);
                            } else {
                                familyTagWindow.queryById('dropdowntoolbar').getComponent('useOldTag').setDisabled(false);
                            }
                        }						
                    },

                }
            ]
        });
        
        familyTagWindow.add(viewPanel);

        dockedChildren =  [
            {
                xtype: 'panel',
                itemId: 'headerPanel',
                dock: 'top',
                html: '<h3> Are you sure that you would like to add a new tag? </br> ' + 
                ' Please see other possible matches below.</h3>'
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
                            if(familyTagWindow.returnTagInfo && familyTagWindow.possibleNewTag){
                                familyTagWindow.returnTagInfo(familyTagWindow.possibleNewTag);
                            }
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
                            if(familyTagWindow.returnTagInfo){
                                familyTagWindow.returnTagInfo(familyTagWindow.getComponent('topPanel').getComponent('gridPanelItemId').getSelection()[0].data);
                            }
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
        ],

        familyTagWindow.addDocked(dockedChildren);
 
    },
	loadData: function(data) {
	}

});