/* 
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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

 /* Author: dshurt, rfrazier */

/* global Ext, CoreUtil, CoreService */
Ext.define('OSF.customSubmission.form.AddAttribute', {
    extend: 'Ext.button.Button',
    alias: 'widget.osf-submissionform-add-attribute',

    itemId: 'addAttributeType',
    text: 'Add',
    iconCls: 'fa fa-lg fa-plus icon-button-color-save',
    minWidth: 100,
    hidden: true,
    handler: function () {
        var addAttributeBtn = this;
        var attributeTypeCB = addAttributeBtn.attributeTypeCB;


        var addTypeWin = Ext.create('Ext.window.Window', {
            title: 'Add Type',
            iconCls: 'fa fa-plus',
            closeAction: 'destroy',
            alwaysOnTop: 9999,								
            modal: true,
            width: 400,
            height: 380,
            layout: 'fit',
            items: [
                {
                    xtype: 'form',
                    scrollable: true,
                    layout: 'anchor',
                    bodyStyle: 'padding: 10px',
                    defaults: {
                        labelAlign: 'top',
                        labelSeparator: '',
                        width: '100%'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            name: 'label',
                            fieldLabel: 'Label <span class="field-required" />',
                            allowBlank: false,
                            maxLength: 255
                        },
                        {
                            xtype: 'textarea',
                            name: 'detailedDescription',
                            fieldLabel: 'Description',
                            maxLength: 255
                        },
                        {
                            xtype: 'combobox',
                            fieldLabel: 'Code Label Value Type <span class="field-required" />',
                            displayField: 'description',
                            valueField: 'code',
                            typeAhead: false,
                            editable: false,
                            allowBlank: false,
                            name: 'attributeValueType',
                            store: {
                                autoLoad: true,
                                proxy: {
                                    type: 'ajax',
                                    url: 'api/v1/resource/lookuptypes/AttributeValueType'
                                }
                            }
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items: [
                                {
                                    text: 'Save',
                                    formBind: true,
                                    iconCls: 'fa fa-lg fa-save icon-button-color-save',
                                    handler: function () {
                                        // assumes this component is in a form
                                        var form = this.up('form');
                                        var data = form.getValues();
                                        var addTypeWin = this.up('window');

                                        var componentType = addAttributeBtn.componentType.componentType;
                                        if (componentType) {
                                            componentType = encodeURIComponent(componentType);
                                        } else {
                                            componentType = '';
                                        }

                                        CoreUtil.submitForm({
                                            url: 'api/v1/resource/attributes/attributetypes/metadata?componentType=' + componentType,
                                            method: 'POST',
                                            data: data,
                                            form: form,
                                            success: function (response, opts) {
                                                
                                                var newAttribute = Ext.decode(response.responseText);
                                                attributeTypeCB.getStore().add(newAttribute);
                                                addTypeWin.close();
                                            }
                                        });

                                    }
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    text: 'Cancel',
                                    iconCls: 'fa fa-lg fa-close icon-button-color-warning',
                                    handler: function () {
                                        this.up('window').close();
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        });
        addTypeWin.show();

    }
})
