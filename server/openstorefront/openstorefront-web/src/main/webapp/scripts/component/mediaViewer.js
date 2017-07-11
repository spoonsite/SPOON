/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

Ext.define('OSF.component.MediaViewerWindow', {
	extend: 'Ext.window.Window',
	alias: 'osf.widget.MediaViewerWindow',
	
	frame: false,
	header: false,
	//maximized: true,
	width: 500,
	height: 300,
	scrollable: true,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					text: 'Close',
					scale: 'large',
					iconCls: 'fa fa-2x fa-close icon-button-color-warning',
					handler: function(){
						this.up('window').close();
					}
				},
				{
					xtype: 'tbfill'
				}				
			]
		},		
		{
			xtype: 'toolbar',
			itemId: 'previousBar',
			dock: 'left',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					itemId: 'previous',
					scale: 'large',					
					iconCls: 'fa fa-2x fa-arrow-left',
					handler: function() {
						var messageWindow = this.up('window');
						messageWindow.currentMediaIndex--;
						messageWindow.media = MediaViewer.mediaList[messageWindow.currentMediaIndex]; 
						messageWindow.showCurrentMedia();
					}
				},
				{
					xtype: 'tbfill'
				}				
			]
		},
		{
			xtype: 'toolbar',
			itemId: 'nextBar',
			dock: 'right',
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'button',
					itemId: 'next',
					scale: 'large',	
					margin: '0 5 0 0',
					iconCls: 'fa fa-2x fa-arrow-right icon-button-color-default',
					handler: function() {
						var messageWindow = this.up('window');
						messageWindow.currentMediaIndex++;
						messageWindow.media = MediaViewer.mediaList[messageWindow.currentMediaIndex]; 
						messageWindow.showCurrentMedia();
					}					
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
					iconCls: 'fa fa-large fa-download icon-button-color-default',
					handler: function(){
						window.location.href = this.up('window').media.link;
					}
				},
				{
					xtype: 'tbfill'
				}				
			]
		},
		{
			xtype: 'toolbar',
			itemId: 'captionToolBar',
			dock: 'bottom',
			hidden: true,
			style: 'background: rgba(10, 10, 10, .7) !important;',
			items: [
				{
					xtype: 'tbfill'
				},
				{
					xtype: 'tbtext',
					itemId: 'caption',
					style: 'color: white;',
					text: ''					
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
		
		messageWindow.showCurrentMedia();
		
		if (!MediaViewer.mediaList || MediaViewer.mediaList.length === 0) {
			messageWindow.getComponent('previousBar').getComponent('previous').setHidden(true);
			messageWindow.getComponent('nextBar').getComponent('next').setHidden(true);
		}
		
		messageWindow.on('close', function(){
			Ext.getDoc().un('keydown', messageWindow.keyHandler);
		});

	},
	
	afterRender: function() {
		this.callParent();
		var messageWindow = this;
		messageWindow.maximize();
	
		messageWindow.keyHandler = function(e, t, opts){
			if (e.keyCode === 39) {
				if (!messageWindow.getComponent('nextBar').getComponent('next').isDisabled()){
					messageWindow.currentMediaIndex++;
					messageWindow.media = MediaViewer.mediaList[messageWindow.currentMediaIndex]; 
					messageWindow.showCurrentMedia();				
				}
			} else if (e.keyCode === 37) {
				if (!messageWindow.getComponent('previousBar').getComponent('previous').isDisabled()){
					messageWindow.currentMediaIndex--;
					messageWindow.media = MediaViewer.mediaList[messageWindow.currentMediaIndex]; 
					messageWindow.showCurrentMedia();				
				}
			}
		};
	
		Ext.getDoc().on('keydown', messageWindow.keyHandler);
	},
	
	checkNextPrev: function(){
		var messageWindow = this;
		
		if (MediaViewer.mediaList && MediaViewer.mediaList.length > 0) {
			
			var index = 0;
			Ext.Array.each(MediaViewer.mediaList, function(item){
				if (item.componentMediaId === messageWindow.media.componentMediaId) {
					return false;
				}
				index++;
			});
			messageWindow.currentMediaIndex = index;			
			
			messageWindow.getComponent('previousBar').getComponent('previous').setDisabled(false);
			messageWindow.getComponent('nextBar').getComponent('next').setDisabled(false);
			
			if (index === 0) {
				messageWindow.getComponent('previousBar').getComponent('previous').setDisabled(true);
			} 
			if (index === MediaViewer.mediaList.length-1) {
				messageWindow.getComponent('nextBar').getComponent('next').setDisabled(true);
			}
		}
	},	
	
	showCurrentMedia: function(){
		var messageWindow = this;
		
		messageWindow.removeAll();
	
		messageWindow.mediaPanel = Ext.create('Ext.panel.Panel', {
			maxWidth: '100%',
			maxHeight: '100%'
		});
		
		if (messageWindow.media.caption){
			messageWindow.getComponent('captionToolBar').setHidden(false);
			messageWindow.getComponent('captionToolBar').getComponent('caption').setText(messageWindow.media.caption);
		} else {
			messageWindow.getComponent('captionToolBar').setHidden(true);
		}
		
		switch (messageWindow.media.mediaTypeCode) {
			case 'IMG':
				messageWindow.mediaPanel.setHtml('<img src="' + messageWindow.media.link + '" style="max-height:100%; max-width: 100%; width:auto;" />');
			break;
			case 'AUD':
				messageWindow.mediaPanel.setHtml('<div style="width: 400px; background: rgba(255,255,255,.7); padding: 20px; text-align: center;"><i class="fa fa-file-sound-o" style="font-size: 11em;"></i><br><audio style="width: 100%" controls><source src="' + messageWindow.media.link + '" type="' + messageWindow.media.mimeType + '" ><i class="fa fa-5x fa-file-sound-o"></i></audio></div>');
			break;
			case 'VID':
				messageWindow.mediaPanel.setHtml('<video width="720" height="480" controls><source src="' + messageWindow.media.link + '" type="' + messageWindow.media.mimeType + '" ><i class="fa fa-5x fa-file-video-o"></i></video>');
			break;
			case 'ARC':
				messageWindow.mediaPanel.setHtml('<div style="background: rgba(255,255,255,.7); padding: 20px; text-align: center;"><i class="fa fa-file-archive-o" style="font-size: 20em;"></i></div>');
			break;	
			case 'TEX':
				messageWindow.mediaPanel.setHtml('<div style="background: rgba(255,255,255,.7); padding: 20px; text-align: center;"><i class="fa fa-file-text-o" style="font-size: 20em;"></i></div>');
			break;			
			default:
				messageWindow.mediaPanel.setHtml('<div style="background: rgba(255,255,255,.7); padding: 20px; text-align: center;"><i class="fa fa-file-o" style="font-size: 20em;"></i></div>');
			break;	
		}
		
		messageWindow.add(messageWindow.mediaPanel);
		messageWindow.checkNextPrev();
	}
	
	
});

var MediaViewer ={
	showMedia: function(mediaTypeCode, link, caption, filename, mimeType, mediaId){
		var media = {
			mediaTypeCode: mediaTypeCode,
			link: link,
			mimeType: mimeType,
			caption: caption,
			filename: filename,
			componentMediaId: mediaId
		};

		var mediaWindow = Ext.create('OSF.component.MediaViewerWindow', {
			media: media,
			closeAction: 'destroy',
			style: 'background: transparent;',
			bodyCls: 'mediaviewer-body'
		});

		mediaWindow.show();

	},
	mediaList: []
};