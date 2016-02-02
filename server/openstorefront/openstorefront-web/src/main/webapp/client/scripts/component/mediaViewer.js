/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

Ext.define('OSF.component.MediaViewerWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.MediaViewerWindow',
	
	title: 'Media Viewer',
	iconCls: 'fa fa-lg fa-file-image-o',
	maximized: true,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'left',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					scale: 'large',
					
					iconCls: 'fa fa-2x fa-arrow-left'
				},
				{
					xtype: 'tbfill'
				}				
			]
		},
		{
			xtype: 'toolbar',
			dock: 'right',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					scale: 'large',	
					margin: '0 5 0 0',
					iconCls: 'fa fa-2x fa-arrow-right'
				},
				{
					xtype: 'tbfill'
				}				
			]
		},
		{
			xtype: 'toolbar',
			dock: 'bottom',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					text: 'Download',
					iconCls: 'fa fa-large fa-download'
				},
				{
					xtype: 'tbfill'
				}				
			]
		}	
	],	
	layout: 'center',
	
	initComponent: function () {
		this.callParent();

		var messageWindow = this;
		
		var mediaPanel = Ext.create('Ext.panel.Panel', {
			maxWidth: '100%',
			maxHeight: '100%'
		});
		
		switch (messageWindow.media.mediaTypeCode) {
			case 'IMG':
				mediaPanel.setHtml('<img src="' + messageWindow.media.link + '" height="100%" />');
			break;
			case 'AUD':
			break;
			case 'VID':
			break;
			default: 
			break;	
		}
		
		messageWindow.add(mediaPanel);
		
	}	
	
	
});

var MediaViewer = function(mediaTypeCode, link, caption, filename){
	var media = {
		mediaTypeCode: mediaTypeCode,
		link: link,
		caption: caption,
		filename: filename
	};

	var mediaWindow = Ext.create('OSF.component.MediaViewerWindow', {
		media: media,
		closeAction: 'destroy',
		style: 'background: transparent;',
		bodyCls: 'mediaviewer-body'
	});
	
	mediaWindow.show();
	
};