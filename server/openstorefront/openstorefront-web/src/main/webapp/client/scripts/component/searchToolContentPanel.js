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

Ext.define('OSF.component.SearchToolContentPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'osf.widget.SearchToolContentPanel',
    layout: 'border',
    defaults: {
        bodyStyle: 'padding:0px'
    },
    initComponent: function () {
        this.callParent();

        var searchContentPanel = this;

        //
        //  This is the navPanel on the left 
        //
        searchContentPanel.navPanel = Ext.create('Ext.panel.Panel', {
            region: 'west',
            width: '30%',
            autoScroll: true,
            layout: 'vbox',
            split: true
        });

        //
        //  This is the infoPanel on the top right
        //
        searchContentPanel.infoPanel = Ext.create('Ext.panel.Panel', {
            region: 'north',
            height: '30%',
            title: 'Description',
            autoScroll: true,
            bodyPadding: 10,
            split: true
        });

        //
        //  This allows the search results to be loaded and paged in the results grid panel
        //
        searchContentPanel.loadGrid = function (searchObj) {

            var theStore = gStore;
            theStore.getProxy().buildRequest = function (operation) {
                var initialParams = Ext.apply({
                    paging: true,
                    sortField: operation.getSorters()[0].getProperty(),
                    sortOrder: operation.getSorters()[0].getDirection(),
                    offset: operation.getStart(),
                    max: operation.getLimit()
                }, operation.getParams());
                params = Ext.applyIf(initialParams, theStore.getProxy().getExtraParams() || {});

                var request = new Ext.data.Request({
                    url: 'api/v1/service/search/advance',
                    params: params,
                    operation: operation,
                    action: operation.getAction(),
                    jsonData: Ext.util.JSON.encode(searchObj)
                });
                operation.setRequest(request);

                return request;
            };

            theStore.loadPage(1);

        };
        //
        //  This is the results grid panel data store
        //
        var gStore = Ext.create('Ext.data.Store', {
            fields: ['name', 'description'],
            pageSize: 50,
            autoLoad: false,
            remoteSort: true,
            sorters: [
                new Ext.util.Sorter({
                    property: 'name',
                    direction: 'DESC'
                })
            ],
            proxy: CoreUtil.pagingProxy({
                actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
                reader: {
                    type: 'json',
                    rootProperty: 'data',
                    totalProperty: 'totalNumber'
                }
            })
        });
        //
        // This is the results Grid panel in the bottom right of the layout.
        //
        searchContentPanel.resultsGridPanel = Ext.create('Ext.grid.Panel', {
            store: gStore,
            columns: [
                {text: 'Name',
                    cellWrap: true,
                    dataIndex: 'name',
                    width: '20%',
                    autoSizeColumn: false,
                    renderer: function (value) {
                        return '<span class="search-tools-column-orange-text">' + value + '</span>';
                    }
                },
                {text: 'Description',
                    dataIndex: 'description',
                    width: '77%',
                    autoSizeColumn: true,
                    cellWrap: true,
                    renderer: function (value) {
                        var str = value.substring(0, 500);
                        if (str === value) {
                            return str;
                        }
                        else {
                            str = str.substr(0, Math.min(str.length, str.lastIndexOf(' ')));
                            return str += ' ... <br/>';
                        }


                    }
                },
                {xtype: 'actioncolumn',
                    width: '25',
                    resizable: false,
                    sortable: false,
                    menuDisabled: true,
                    items: [{
                            iconCls: 'fa fa-link',
                            tooltip: 'Link to page',
                            handler: function (grid, rowIndex, colIndex) {
                                var theStore = grid.getStore();
                                var newUrl = '/openstorefront/single?id=' + theStore.getAt(rowIndex).data.componentId;
                                window.location.assign(window.location.origin + newUrl);

                                var win = Ext.getCmp('searchToolWindow');
                                win.close();

                            }
                        }]
                }
            ],
            width: '100%',
            dockedItems: [{
                    xtype: 'pagingtoolbar',
                    store: gStore,
                    dock: 'bottom',
                    displayInfo: true
                }]

        });
        //
        //  This is the results panel in the bottom right holds the grid panel
        //
        searchContentPanel.resultsPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            title: 'Search Results',
            split: true,
            layout: 'fit',
            items: [
                searchContentPanel.resultsGridPanel
            ],
            header: {
                items: [{
                        xtype: 'button',
                        text: '<i style="color:white;" class="fa fa-external-link"></i>',
                        tooltip: 'Show results',
                        handler: function () {
                            ////console.log("load all clicked");
                            if (!searchContentPanel.resultsGridPanel.hidden)
                            {
                                //perform search
                                var win = Ext.getCmp('searchToolWindow');
                                win.angularScope.closeSearchTools(win.searchObj);

                                //close window
                                win.close();

                            }
                        }
                    }]
            }
        });

        //
        //  This is the center panel which splits the right top and bottom panels.
        //
        var centerPanel = Ext.create('Ext.panel.Panel', {
            layout: 'border',
            region: 'center',
            items: [
                searchContentPanel.infoPanel,
                searchContentPanel.resultsPanel
            ]
        });



        searchContentPanel.add(centerPanel);
        searchContentPanel.add(searchContentPanel.navPanel);

    }




});
