/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 /* Author: Ryan Frazier */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanProgress.ProgressView', {
    extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.osf.wp.progressView',
    dock: 'bottom',
    id: 'workplan-progress-view',
    hidden: true,
    border: false,
    bodyBorder: false,
    layout: {
        pack: 'center'
    },
    style: 'border-top: 1px solid #D0D0D0 !important',
    title: 'Selected Workplan Progress',
    addSteps: function(record) {
        var steps = record.get('steps');
        var currentStep = record.get('currentStep');
        var statusCmp = Ext.getCmp('workplan-progress-management-worklink-status');
        var statusToolbarCmp = Ext.getCmp('workplan-progress-view');

        statusToolbarCmp.setVisible(true);
        statusCmp.removeAll();
        Ext.Array.forEach(steps, function (el, index) {
            statusCmp.add({
                xtype: 'container',
                html:	'<div class="step-view-container ' + (index === steps.length - 1 ? 'last-step' : ' ') + '">' + 
                            '<span class="wp-step-label ' + (index === steps.length - 1 ? 'last-step' : ' ') + '">' + el.name + '</span>' +
                            '<div data-qtip="' + el.description + '"' + 
                            'class="step-view ' + 
                            (el.workPlanStepId === currentStep.workPlanStepId ? ' current-step ' : ' wp-step ') +
                            (index === steps.length - 1 ? ' last-step ' : ' ') +
                            '"></div>' +
                        '</div>'
            });
        });
    },
    items: [
        {
            xtype: 'container',
            itemId: 'stepsLegendContainer',
            id: 'workplan-progress-management-worklink-status-legend',
            width: 120,
            padding: '0 0 0 15',
            style: 'border-right: 1px solid #ccc',
            layout: {
                type: 'vbox',
                pack: 'center'
            },
            defaults: {
                margin: '0 0 10 0'
            },
            items: [
                {
                    xtype: 'container',
                    html: '<div style="width: 100%;" data-qtip="The current step of the workplan"><div class="wp-step-lengend current-step"></div>&nbsp;<strong>Current Step</strong></div>'
                }
            ]
        },
        {
            // the container will be populated when an item is selected
            xtype: 'container',
            itemId: 'workplanStatusContainer',
            id: 'workplan-progress-management-worklink-status',
            layout: {
                type: 'hbox',
                pack: 'center',
                scrollable: 'x',
                height: '100%',
                padding: '0 30 15 20',
                cls: 'step-container',
            },
            listeners: {
                render: function () {

                    var stepsContainer = this;

                    // set up horizontal scrolling
                    stepsContainer.el.on('mousewheel', function (e) {
                        e.preventDefault();

                        if (e.getWheelDeltas().y < 0) {
                            stepsContainer.el.scrollTo('left', stepsContainer.el.getScroll().left + 20)
                        }
                        else {
                            stepsContainer.el.scrollTo('left', stepsContainer.el.getScroll().left - 20)
                        }
                    });
                }
            }
        }
    ]
});