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

        // -----------------------
        // compute days ago edited
        // -----------------------
        var today = new Date();
        var oneDay = 1000*60*60*24;
        var updateDts = record.get('updateDts'); 
        if (typeof updateDts !== "object") {
            updateDts = Ext.Date.parse(updateDts, 'c');
        } 
        var daysAgo = Math.ceil((updateDts - today.getTime())/oneDay) * -1;
        // -----------------------
        var statusCmp = Ext.getCmp('workplan-progress-management-worklink-status');
        var statusToolbarCmp = Ext.getCmp('workplan-progress-view');

        var subStatusDescription = record.get('subStatusDescription');

        var pendingColor           = record.get('workPlanPendingColor');
        var progressColor          = record.get('workPlanInProgressColor');
        var completeColor          = record.get('workPlanCompleteColor');
        var statusColor            = record.get('workPlanSubStatusColor');
        var legend                 = Ext.getCmp('stepsLegendContainer');

        var html = '<div style="width: 100%;"><div style="border: 1px solid #222; background: #' + pendingColor + ';" class="wp-step-legend br-100"></div>&nbsp;<strong>Pending</strong></div>' +
                   '<div style="width: 100%;"><div style="border: 1px solid #222; background: #' + progressColor + ';" class="wp-step-legend br-100"></div>&nbsp;<strong>In Progress</strong></div>' +
                   '<div style="width: 100%;"><div style="border: 1px solid #222; background: #' + completeColor + ';" class="wp-step-legend br-100"></div>&nbsp;<strong>Complete</strong></div>' +
                   '<div style="width: 100%;"><div style="border: 1px solid #222; background: #' + statusColor + ';" class="wp-step-legend br-100"></div>&nbsp;<strong>Attention Required</strong></div>'
        legend.setHtml(html)

        statusToolbarCmp.setVisible(true);
        statusCmp.removeAll();

        var stepColors = Array(steps.length); // each color corresponds to a step
        var beforeCurrentStep = true;
        Ext.Array.forEach(steps, function (el, index) {
            stepColors[index] = pendingColor;
            if (beforeCurrentStep) {
                stepColors[index] = completeColor;
            }
            if (el.workPlanStepId === currentStep.workPlanStepId) {
                beforeCurrentStep = false;
                stepColors[index] = progressColor;
            }
            if (el.workPlanStepId === currentStep.workPlanStepId && (index + 1) === steps.length) {
                stepColors[index] = completeColor;
            }
        })

        Ext.Array.forEach(steps, function (el, index) {
            statusCmp.add({
                xtype: 'container',
                html:	'<div class="step-view-container ' + (index === steps.length - 1 ? 'last-step' : ' ') + ' " ' +
                        '>' + 
                            '<span class="wp-step-label ' + (index === steps.length - 1 ? 'last-step' : ' ') + '">' + el.name + '</span>' +
                            // TODO: replace qtip with a popup with more information about the workplan step
                            '<div data-qtip=" ' +
                                'Workplan last update: ' + daysAgo + ' days ago' +
                                ((subStatusDescription && stepColors[index] === progressColor) ?
                                '<br>Attention Required: ' + subStatusDescription + ' " '  
                                : ' " ') +
                            'class="step-view static-step-view ' + 
                            ' br-100 ' +
                            (index === steps.length - 1 ? ' last-step ' : ' ') +
                            '" ' + 
                            ' style="background-color: #' + stepColors[index] + '; ' +
                            ((subStatusDescription && stepColors[index] === progressColor) ?
                            ' box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.4), 0 6px 20px 0 #' + statusColor + ';" ' 
                            : ';" '
                            ) +
                            '></div>' +
                        '</div>'
            });
        });
    },
    items: [
        {
            xtype: 'container',
            id: 'workplan-progress-management-worklink-status-legend',
            width: 175,
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
                    id: 'stepsLegendContainer'
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