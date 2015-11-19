/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*global Ext*/
Ext.define('OSF.component.FeedbackWindow', {
    extend: 'Ext.window.Window',
    alias: 'osf.widget.FeedbackWindow',
    title: 'Feedback / Issues',
    iconCls: 'fa fa-exclamation-triangle',
    width: '50%',
    y: 40,
    modal: true,
    maximizable: false,
    layout: 'fit',
    id: 'feedbackWindow',
    initComponent: function () {
        this.callParent();

        var feedbackWin = this;
        //
        // Selection Combobox feedbackTypes items
        //
        var feedbackTypes = Ext.create('Ext.data.Store',{
           fields: ['name'],
           data:[
               {"name":"Send Comment"},
               {"name":"Report Issue"}
               
           ]
        });
        
        //
        //  Feedback Panel
        //  This is the panel tab for the topic search tool
        //
      
        var formPanel = Ext.create('Ext.form.Panel', {
            id: 'feedbackForm',
            layout: 'vbox',
            scrollable: true,
            bodyStyle: 'padding: 10px;',
            defaults: {
                labelAlign: 'top'
            },
            dockedItems: [
                {
                    dock: 'bottom',
                    xtype: 'toolbar',
                    items: [
                        {
                            text: 'Send',
                            formBind: true,
                            iconCls: 'fa fa-save',
                            handler: function () {
                                var method = 'POST';
                                var url = '';
                                var data = Ext.getCmp('feedbackForm').getValues();

                                CoreUtil.submitForm({
                                    url: url,
                                    method: method,
                                    data: data,
                                    removeBlankDataItems: true,
                                    form: Ext.getCmp('feedbackForm'),
                                    success: function (response, opts) {
                                        Ext.toast('Sent Successfully', '', 'tr');
                                        Ext.getCmp('feedbackForm').setLoading(false);
                                        Ext.getCmp('feedbackWindow').close();
                                    }
                                });
                            }
                        },
                        {
                            xtype: 'tbfill'
                        },
                        {
                            text: 'Cancel',
                            iconCls: 'fa fa-close',
                            handler: function () {
                                Ext.getCmp('feedbackWindow').close();
                            }
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'combobox',
                    id: 'feedbackType',
                    name: 'feedbackType',
                    fieldLabel: 'Choose Type<span class="field-required" />',
                    width: '100%',
                    maxLength: 50,
                    store: feedbackTypes,
                    displayField:'name',
                    valueField:'name',
                    editable:false
                },
                {
                    xtype: 'textfield',
                    id: 'summary',
                    name: 'summary',
                    fieldLabel: 'Summary<span class="field-required" />',
                    width: '100%',
                    maxLength: 50,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    id: 'description',
                    name: 'description',
                    fieldLabel: 'Description<span class="field-required" />',
                    width: '100%',
                    height: 200,
                    maxLength: 255,
                    allowBlank: false
                }
            ]


        });
        
        feedbackWin.add(formPanel);
    }
});

