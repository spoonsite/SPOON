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
Ext.define('OSF.component.SearchToolWindow', {
    extend: 'Ext.window.Window',
    alias: 'osf.widget.SearchToolWindow',
    title: 'Search Tools',
    iconCls: 'fa fa-lg fa-th',
    width: '70%',
    height: '70%',
    y: 40,
    modal: true,
    maximizable: true,
    layout: 'fit',
    id: 'searchToolWindow',
    initComponent: function () {
        this.callParent();

        var searchToolWin = this;

        //
        //  topicSearchPanel Tab
        //  This is the panel tab for the topic search tool
        //
        var topicSearchPanel = Ext.create('Ext.panel.Panel', {
            title: 'Topic',
            iconCls: 'fa fa-book',
            layout: 'fit',
            items: [
                Ext.create('OSF.component.SearchToolContentPanel', {
                    searchToolType: "topic",
                    itemId: 'topicPanel'
                })
            ]
        });

        //
        //  categorySearchPanel Tab
        //  This is the panel tab for the category search tool
        //
        var categorySearchPanel = Ext.create('Ext.panel.Panel', {
            title: 'Category',
            iconCls: 'fa fa-list-ul',
            layout: 'fit',
            items: [
                Ext.create('OSF.component.SearchToolContentPanel', {
                    searchToolType: "category",
                    itemId: 'contentPanel'
                })
            ]
        });

        //
        //  archSearchPanel Tab
        //  This is the panel tab for the architecture search tool
        //
        var archSearchPanel = Ext.create('Ext.panel.Panel', {
            title: 'Architecture',
            iconCls: 'fa fa-sitemap',
            layout: 'fit',
            items: [
                Ext.create('OSF.component.SearchToolContentPanel', {
                    searchToolType: "architecture",
                    itemId: 'archPanel'
                })
            ]
        });

        //
        //  tabPanel
        //  This is the panel to hold all the other tab panels
        //
        var tabPanel = Ext.create('Ext.tab.Panel', {
            items: [
                topicSearchPanel,
                categorySearchPanel,
                archSearchPanel
            ]

        });

        //***************************
        //  Utility Methods 
        //***************************
        var sortList = function (theArray, fieldname, direction) {

            if (direction === 'ASC') {
                var tData = theArray.sort(function (a, b) {
                    if (a[fieldname] > b[fieldname])
                        return -1;
                    if (a[fieldname] < b[fieldname])
                        return 1;
                    return 0;

                });
            }
            else if (direction === 'DESC') {
                var tData = theArray.sort(function (a, b) {
                    if (a[fieldname] < b[fieldname])
                        return -1;
                    if (a[fieldname] > b[fieldname])
                        return 1;
                    return 0;

                });
            }


            return tData;
        };




        //***************************
        //  Topic Methods 
        //***************************
        //

        //
        //  Topic Load Search
        //
        var topicButtonHandler = function (newTab, item) {

            var desc = item.description;
            if (desc === '')
            {
                desc = 'None';
            }
            newTab.getComponent('topicPanel').infoPanel.update(desc);


            //Do the search on the category attribute
            searchToolWin.searchObj = {
                "sortField": null,
                "sortDirection": "DESC",
                "startOffset": 0,
                "max": 2147483647,
                "searchElements": [{
                        "searchType": "COMPONENT",
                        "field": "componentType",
                        "value": item.type,
                        "keyField": null,
                        "keyValue": null,
                        "startDate": null,
                        "endDate": null,
                        "caseInsensitive": false,
                        "numberOperation": "EQUALS",
                        "stringOperation": "EQUALS",
                        "mergeCondition": "OR"  //OR.. NOT.. AND..
                    }]
            };
            newTab.getComponent('topicPanel').loadGrid(searchToolWin.searchObj);
        };



        //
        //  This method calls an API call to get all the topics and creates a list of buttons inside the navPanel.
        //
        //
        var loadTopicNav = function (newTab) {
            //console.log("Loading Topic Nav");
            newTab.setLoading(true);
            Ext.Ajax.request({
                url: 'api/v1/resource/componenttypes',
                success: function (response, opts) {
                    newTab.setLoading(false);
                    var data = Ext.decode(response.responseText);
                    var tData = sortList(data, 'label', 'DESC');
                    ////console.log("tData",tData);
                    Ext.Array.each(tData, function (item) {
                        ////console.log("newTab",newTab);

                        newTab.getComponent('topicPanel').navPanel.add({
                            xtype: 'button',
                            cls: 'list-button',
                            height: 50,
                            text: item.label,
                            desc: item.description,
                            width: '100%',
                            handler: function () {
                                //console.log("Comp clicked", this.text);
                                topicButtonHandler(newTab, item);
                            }
                        });
                    });
                },
                failure: function (response, opts) {
                    newTab.setLoading(false);
                }
            });
            newTab.doneLoad = true;
        };


        //
        //  Topic Tab Processing
        //
        var topicTabProcessing = function (tabpanel, newTab, oldtab, opts) {
            if (!newTab.doneLoad) {
                loadTopicNav(newTab);
            }
        };



        //***************************
        //  Category Methods 
        //***************************

        //
        // This is the button handler for the category list button
        // This does an advanced search on the category list button clicked. 
        //
        var categoryButtonHandler = function (newTab, item, item2) {

            var desc = item2.description;
            if (desc === '')
            {
                desc = 'None';
            }
            newTab.getComponent('contentPanel').infoPanel.update(item2.label + '<br/> Item Code: ' + item2.code + '<br/> Description: ' + desc);

            //Do the search on the category attribute
            searchToolWin.searchObj = {
                "sortField": null,
                "sortDirection": "DESC",
                "startOffset": 0,
                "max": 2147483647,
                "searchElements": [{
                        "searchType": "ATTRIBUTE",
                        "field": null,
                        "value": null,
                        "keyField": item.attributeType,
                        "keyValue": item2.code,
                        "startDate": null,
                        "endDate": null,
                        "caseInsensitive": false,
                        "numberOperation": "EQUALS",
                        "stringOperation": "EQUALS",
                        "mergeCondition": "OR"  //OR.. NOT.. AND..
                    }]
            };
            // newTab.setLoading(true);
            newTab.getComponent('contentPanel').loadGrid(searchToolWin.searchObj);
        };

        //
        // This is the loader handler when a category is selected. The category list buttons load below and the panel expands.
        // The list buttons are loaded from the api call if they have not been loaded before.
        //

        var categoryBeforePanelExpandHandler = function (p, animate, eOpts, newTab, item) {
            //Add description when selected
            newTab.getComponent('contentPanel').infoPanel.update(item.attributeTypeDescription + '<br/> Item Code: ' + item.attributeType);
            if (p.loadedUp === undefined) {
                newTab.setLoading(true);
                Ext.Ajax.request({
                    url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent(item.attributeType) + '/attributecodeviews',
                    success: function (response, opts) {
                        newTab.setLoading(false);
                        var aData = Ext.decode(response.responseText);

                        //Sort the aData
                        var dataArray = aData.data;
                        var tData = sortList(dataArray, 'label', 'DESC');

                        Ext.Array.each(tData, function (item2) {
                            p.add({
                                xtype: 'button',
                                text: item2.label,
                                width: '100%',
                                textAlign: 'left',
                                cls: 'list-button',
                                handler: function () {
                                    categoryButtonHandler(newTab, item, item2);
                                }
                            });
                        });

                        p.setHeight(null); //This is required to make the panel collapse/expand work.
                        p.loadedUp = true;

                    },
                    failure: function (response, opts) {
                        newTab.setLoading(false);
                        p.update("Failed to load data.");
                        //console.log("Failed to load data:" + response);
                        p.setHeight(null);

                    }

                });
            }
        };

        //
        //  This method calls an API call to get all the categories and creates a list of collapsible panels inside the navPanel.
        //
        //
        var loadCategoryNav = function (newTab) {

            newTab.setLoading(true);
            Ext.Ajax.request({
                url: 'api/v1/resource/branding/current',
                success: function (response, opts) {
                    newTab.setLoading(false);
                    var data = Ext.decode(response.responseText);
                    var dArray = data.topicSearchViews;

                    tData = sortList(dArray, 'attributeTypeDescription', 'DESC');

                    Ext.Array.each(tData, function (item) {

                        newTab.getComponent('contentPanel').navPanel.add({
                            xtype: 'panel',
                            collapsible: true,
                            collapsed: true,
                            titleCollapse: true,
                            height: 100,
                            header: {
                                cls: 'panel-header',
                                title: item.attributeTypeDescription
                            },
                            bodyCls: 'search-tools-nav-body-panel-item',
                            width: '100%',
                            listeners: {
                                beforeexpand: function (p, animate, eOpts) {
                                    categoryBeforePanelExpandHandler(p, animate, eOpts, newTab, item);
                                }
                            }
                        });
                    });
                },
                failure: function (response, opts) {
                    newTab.setLoading(false);
                }
            });
            newTab.doneLoad = true;
        };

        //
        //  Category Tab Processing
        //
        var categoryTabProcessing = function (tabpanel, newTab, oldtab, opts) {
            if (!newTab.doneLoad) {
                loadCategoryNav(newTab);
            }
        };

        //***************************
        //  Architecture Methods 
        //***************************

        //
        // This is the button handler for the category list button
        // This does an advanced search on the category list button clicked. 
        //
        var archSelectHandler = function (newTab, item) {

            var desc = item.get('description');
            if (desc === '')
            {
                desc = 'None';
            }
            newTab.getComponent('archPanel').infoPanel.update(desc);

            //Do the search on the category attribute
            searchToolWin.searchObj = {
                "sortField": null,
                "sortDirection": "DESC",
                "startOffset": 0,
                "max": 2147483647,
                "searchElements": [{
                        "searchType": "ATTRIBUTE",
                        "field": 'architectureCode',
                        "value": item.get('attributeCode'),
                        "keyField": item.get('attributeType'),
                        "keyValue": null,
                        "startDate": null,
                        "endDate": null,
                        "caseInsensitive": false,
                        "numberOperation": "EQUALS",
                        "stringOperation": "STARTS_LIKE",
                        "mergeCondition": "OR"  //OR.. NOT.. AND..
                    }]
            };
            newTab.getComponent('archPanel').loadGrid(searchToolWin.searchObj);
        };




        //
        //  This method calls an API call to get all the SVC-V4-A tree arcg and creates a tree inside the navPanel.
        //
        //
        var loadArchNav = function (newTab) {

            newTab.setLoading(true);
            Ext.Ajax.request({
                url: 'api/v1/resource/attributes/attributetypes/' + encodeURIComponent('DI2E-SVCV4-A') + '/architecture',
                success: function (response, opts) {
                    newTab.setLoading(false);
                    var data = Ext.decode(response.responseText);
                    var tStore = Ext.create('Ext.data.TreeStore', {
                        storeId: 'archStore',
                        rootVisible: true,
                        fields: [{
                                name: 'text',
                                mapping: 'name'
                            },
                            "description",
                            "attributeType",
                            "attributeCode",
                            "originalAttributeCode",
                            "architectureCode",
                            "sortOrder"

                        ],
                        data: data
                    });


                    newTab.getComponent('archPanel').navPanel.add({
                        xtype: 'treepanel',
                        store: tStore,
                        width: '100%',
                        rootVisible: false,
                        listeners: {
                            beforeselect: function (thetree, therecord, theindex, theOpts) {
                                archSelectHandler(newTab, therecord);
                            }
                        }
                    });

                },
                failure: function (response, opts) {
                    newTab.setLoading(false);
                }
            });
            newTab.doneLoad = true;
        };



        //
        //  Architecture Tab Processing
        //
        var archTabProcessing = function (tabpanel, newTab, oldtab, opts) {
            if (!newTab.doneLoad) {
                loadArchNav(newTab);
            }
        };


        //***************************
        //  Tab Panel  Methods 
        //***************************
        //
        //  This is the tab panel tab change handler
        //


        tabPanel.on('tabchange', function (tabpanel, newTab, oldtab, opts) {

            if (newTab.getTitle() === 'Topic') {
                topicTabProcessing(tabpanel, newTab, oldtab, opts);
            }
            else if (newTab.getTitle() === 'Category') {

                categoryTabProcessing(tabpanel, newTab, oldtab, opts);
            }
            else if (newTab.getTitle() === 'Architecture') {
                archTabProcessing(tabpanel, newTab, oldtab, opts);
            }
        });
        searchToolWin.add(tabPanel);


        var setActiveTabByTitle = function (tabTitle) {

            //console.log('Setting Active Tab to:' + tabTitle);
            var tabs = tabPanel.items.findIndex('title', tabTitle);
            tabPanel.setActiveTab(tabs);
        };
        setActiveTabByTitle("Category");
        setActiveTabByTitle("Topic");

    } //End Init Component

});
