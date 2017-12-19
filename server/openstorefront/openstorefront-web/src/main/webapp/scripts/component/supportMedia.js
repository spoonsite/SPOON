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

Ext.define('OSF.component.SupportMediaPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.SupportMediaPanel',
	layout: 'border',
	selecterRegion: 'west',	
	selectedMediaId: null,
	collapseSelect: false,
	initComponent: function () {
		this.callParent();
		
		var supportPanel = this;
		
		supportPanel.selector = Ext.create('Ext.panel.Panel', {
			region: supportPanel.selecterRegion,
			title: 'Selection',			
			width: 310,
			split: true,
			collapsible: true,
			titleCollapse: true,
			collapsed: supportPanel.collapseSelect,
			layout: 'fit',
			bodyStyle: 'padding: 5px;',
			border: true,
			items: [
				{
					xtype: 'dataview',
					itemId: 'dataView',					
					scrollable: true,
					itemSelector: 'div.media-item',
					store: {						
					},
					tpl: new Ext.XTemplate(
						'	<tpl for=".">',
						'		<div class="detail-media-block media-item">',
						'			<tpl if="mediaType == \'IMG\'">',
						'				<img class="x-item" src="{link}" height="150" alt="{[values.title ? values.title : values.filename]}">',
						'				<p class="detail-media-caption">{title}</p>',
						'			</tpl>',
						'			<tpl if="mediaType == \'VID\'">',
						'				<video height="150" controls><source src="{link}#t=10" onloadedmetadata="this.currentTime=10;" type="{mimeType}" ><i class="fa fa-5x fa-file-video-o"></i></video>',
						'				<p class="detail-media-caption" style="opacity: 1;">{title}</p>',
						'			</tpl>',
						'		</div>',				
						'	</tpl>'
					),
					listeners: {
						itemclick: function(dataView, offRecord, item, index, e, eOpts) {	
							var record = dataView.getStore().getAt(index);
							if (!record) {
								record = offRecord;
							}
							supportPanel.loadSelection(record);
						}
					}
				}
			]
		});
		
				
		supportPanel.contents = Ext.create('Ext.panel.Panel', {
			region: 'center',
			layout: 'fit',
			bodyStyle: 'background: white;',
			items: [				
			],
			dockedItems: [
				{
					xtype: 'panel',
					itemId: 'title',
					dock: 'top',
					bodyStyle: 'padding-top: 10px; padding-bottom: 10px; background: white;',
					data: { title: null},
					tpl: new Ext.XTemplate(
						'<h1 style="text-align: center"><tpl if="!title">Select a tutorial to View</tpl><tpl if="title">{title}</tpl></h1>'	
					)
				},
				{
					xtype: 'panel',
					itemId: 'description',
					dock: 'bottom',
					bodyStyle: 'padding: 20px;background: white;',
					tpl: new Ext.XTemplate(
						'<div class="support-media-description">{description}</div>'	
					)
				}				
			]
		});
		
		supportPanel.add(supportPanel.contents);
		supportPanel.add(supportPanel.selector);
		
		
		//load supportMedia
		supportPanel.selector.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/supportmedia',
			callback: function() {
				supportPanel.selector.setLoading(false);
			},
			success: function(response, opt) {
				var data = Ext.decode(response.responseText);
				
				//create media items
				var mediaItems = [];
				Ext.Array.each(data, function(item){
					mediaItems.push({
						supportMediaId: item.supportMediaId,
						title: item.title,
						description: item.description,
						mediaType: item.mediaType,
						link: 'Media.action?SupportMedia&mediaId=' + item.supportMediaId,
						mimeType: item.file.mimeType,
						filename: item.file.originalName
					});
				});
				
				var dataView = supportPanel.selector.queryById('dataView');
				dataView.getStore().loadData(mediaItems);				
				
				if (supportPanel.selectedMediaId) {
					var index = dataView.getStore().find('supportMediaId', supportPanel.selectedMediaId);					
					var selectedRecord = dataView.getStore().getAt(index);
					supportPanel.loadSelection(selectedRecord);
				}
				
			}
		});
		
		supportPanel.loadSelection = function(record) {
			
			var titlePanel = supportPanel.contents.queryById('title');
			titlePanel.update({
				title: record.get('title')
			});
			
			var descriptionPanel = supportPanel.contents.queryById('description');
			descriptionPanel.update({
				description: record.get('description')
			});				
			
			supportPanel.contents.removeAll();
			supportPanel.contents.add({
				xtype: 'video',
				src: record.get('link') 
			});
					
		};
		
	}
	
});


Ext.define('OSF.component.SupportMediaWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.SupportMediaWindow',
	layout: 'fit',
	title: 'Tutorials',
	iconCls: 'fa fa-lg fa-question-circle',
	y: 150,
	width: 800,
	height: 600,
	maximizable: true,
	collapsible: true,
	alwaysOnTop: true,
	closeAction: 'destroy',
	selecterRegion: 'west',
	selectedMediaId: null,
	collapseSelect: false,
	tools: [
		{
			type: 'toggle',
			tooltip: 'Open in new window',
			callback: function(panel, tool, event){
				var supportWin = window.open('supportMedia.jsp', 'supportmediawin');
				if (!supportWin) {
					Ext.toast('Unable to open training/tutorials. Check popup blocker.');
				} else {
					this.up('window').close();
				}
			}
		}
	],
	listeners: {		
		show: function() {
			this.removeCls("x-unselectable");    
		}		
	},
	
	initComponent: function () {
		this.callParent();
		
		var supportWin = this;
		
		supportWin.supportPanel = Ext.create('OSF.component.SupportMediaPanel', {	
			itemId: 'supportMediaPanel',
			selecterRegion: supportWin.selecterRegion,
			selectedMediaId: supportWin.selectedMediaId,
			collapseSelect: supportWin.collapseSelect
		});
		
		supportWin.add(supportWin.supportPanel);
		
	}
		
	
});
