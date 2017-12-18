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
	layout: 'border',
	autoLoad: false,
	initComponent: function () {
		
		this.callParent();
		
		var faqPanel = this;
		faqPanel.faqFlat = [];
		
		faqPanel.navPanel = Ext.create('Ext.panel.Panel', {
			
			items: [
				{}]
		});
	},
	loadQuestions: function () {}
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
	items: [
		Ext.create('OSF.component.FaqPanel', {	
			itemId: 'faqPanel'
		})
	],
	tools: [
		{
			type: 'toggle',
			tooltip: 'Open in new window',
			callback: function(panel, tool, event){
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
		beforeShow: function(faqWin){
			if (!faqWin.loaded) {
				faqWin.getComponent('faqPanel').loadQuestions();
				faqWin.loaded = true;
			}
		},
		show: function()   
		{        
			this.removeCls("x-unselectable");    
		}		
	},
	
	initComponent: function () {
		this.callParent();
	}
		
	
});