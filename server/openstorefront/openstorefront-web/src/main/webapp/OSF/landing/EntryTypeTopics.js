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
/* global Ext, CoreUtil */

/***
 * @author cyearsley
 */

Ext.define('OSF.landing.EntryTypeTopics', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.osf-entryTypeTopics',

    width: '100%',
    titleCollapse: true,
    collapsible: true,
    hideCollapseTool: true,
    layout: 'center',
    collapsed: false,
    cls: 'home-category-block',
    rowLength: 4,
    layout: {
        type: 'vbox',
        align: 'center',
        pack: 'center'
    },

    header: {
        title: '<span class="home-category-title">Browse Topics &nbsp;<i class="fa fa-lg fa-caret-down"></i></span>',
        cls: 'home-category-title-section',
        padding: 20,
        titleAlign: 'center'
    },

    initComponent: function () {

        this.callParent(arguments);
        var topicComponent = this;

        // query component types
        Ext.Ajax.request({
            url: 'api/v1/resource/componenttypes/nested',
            type: 'ajax',
            success: function (response) {

                var decodedResponse = Ext.decode(response.responseText);
                var topicCount = decodedResponse.children.length;

                var topicTemplate = new Ext.XTemplate();

                // query template for entry type topics buttons
                Ext.Ajax.request({
                    url: 'Router.action?page=shared/entryTypeTopics.jsp',
                    success: function (response, opts) {

                        // set the template and add the data view for entry type buttons
                        var topicHtml = topicTemplate.set(response.responseText, true);
                        topicComponent.add({
                            xtype: 'dataview',
                            itemSelector: 'topic-item',
                            tpl: topicTemplate,
                            itemSelector: 'div.entry-topics-button',
                            width: '100%',
                            store: {},
                            listeners: {
                                itemclick: function (dataView, record, item, index, e, eOpts) {

                                    // redirect user to search page with selected entry type
                                    var searchObj = {
                                        "sortField": null,
                                        "sortDirection": "ASC",
                                        "startOffset": 0,
                                        "max": 2147483647,
                                        "searchElements": [
                                            {
                                                "searchType": "ENTRYTYPE",
                                                "field": 'componentType',
                                                "searchChildren": true,
                                                "value": record.getData().componentType.componentType,
                                                "caseInsensitive": false,
                                                "numberOperation": "EQUALS",
                                                "stringOperation": "EQUALS",
                                                "mergeCondition": "OR"
                                            }
                                        ]
                                    };

                                    searchRequest = {
                                        type: 'Advance',
                                        query: searchObj
                                    };

                                    CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));

                                    window.location.href = 'searchResults.jsp';
                                }
                            }
                        }).getStore().loadData(decodedResponse.children);
                    }
                });
            }
        });
    },

    items: []
});
