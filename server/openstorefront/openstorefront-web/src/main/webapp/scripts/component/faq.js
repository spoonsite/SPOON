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
/* global Ext */

Ext.define('OSF.component.FaqPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.FaqPanel',
	
	autoLoad: false,	
	items: [],
	scrollable: true,
	layout: 'anchor',
	
	initComponent: function () {
		this.callParent();
		if(this.autoLoad) {
			this.loadQuestions();
		}
	},
	loadQuestions: function () {
		var faqPanel = this;
		var getCategories = function (faqList) {
			var categories = Ext.Array.unique(Ext.Array.map(faqList, function (elem) {
				return elem.category;
			}));
			return categories;
		};
		var getFaqMap = function (faqList) {
			var categories = getCategories(faqList);
			var faqMap = [];
			Ext.Array.each(categories, function (category) {
				faqMap.push({
					category: category,
					description: Ext.Array.findBy(faqList, function (elem) {
						return elem.category === category;
					}).faqCategoryTypeDescription,
					questions: Ext.Array.filter(faqList, function (elem) {
						return elem.category === category;
					})
				});
			});
			return faqMap;
		};

		var createSection = function (sectionData) {
			var getFaqItems = function () {
				var items = [];
				Ext.Array.each(sectionData.questions, function (faqItem) {
					items.push({
						type: 'panel',
						collapsible: true,
						collapseFirst: true,
						collapsed: true,
						titleCollapse: true,
						width: '100%',
						title: faqItem.question,
						animCollapse: false,
						cls: 'faq-question',
						items: [{
								type: 'panel',
								style: 'padding-left: 40px;',
								html: faqItem.answer
							}]
					});
				});
				return items;
			};
			var sectionPanel = Ext.create('Ext.panel.Panel', {
				type: 'panel',
				itemId: 'faqSectionPanel-' + sectionData.category,
				width: '100%',
				layout: 'vbox',
				collapsible: true,
				titleCollapse: true,
				animCollapse: false,				
				title: sectionData.description
			});
			sectionPanel.add(getFaqItems());
			return sectionPanel;
		};
		Ext.Ajax.request({
			url: 'api/v1/resource/faq',
			method: 'GET',
			success: function (response, opts) {
				var faqList = Ext.decode(response.responseText);
				var faqMap = getFaqMap(faqList);
				var items = [];
				Ext.Array.each(faqMap, function (faqItem) {
					items.push(createSection(faqItem));
				});
				faqPanel.add(items);
			}
		});
	}

});

Ext.define('OSF.component.FaqWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.FaqWindow',
	layout: 'fit',
	title: 'Frequently Asked Questions',
	iconCls: 'fa fa-lg fa-info-circle',
	y: 150,
	width: 800,
	height: 600,
	maximizable: true,
	collapsible: true,
	alwaysOnTop: true,
	closeAction: 'destroy',
	items: [		
	],
	tools: [
		{
			type: 'toggle',
			tooltip: 'Open in new window',
			callback: function (panel, tool, event) {
				var faqWin = window.open('faq.jsp', 'faqwin');
				if (!faqWin) {
					Ext.toast('Unable to open Frequently Asked Questions. Check popup blocker.');
				} else {
					this.up('window').close();
				}
			}
		}
	],
	listeners: {
		show: function ()
		{
			this.removeCls("x-unselectable");
		}
	},

	initComponent: function () {
		this.callParent();
		
		var faqWin = this;
		
		faqWin.faqPanel = Ext.create('OSF.component.FaqPanel', {
			itemId: 'faqPanel'
		});
		faqWin.add(faqWin.faqPanel);
		faqWin.getComponent('faqPanel').loadQuestions();
				
				
	}


});