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

Ext.define('OSF.landing.DefaultInfo', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultinfo',
	
	layout: 'center',
	width: '100%',
	
	items: [
		{
			xtype: 'panel',
			width: '50%',
			height: 250,
			cls: 'home-info-carousel',
			itemId: 'carousel',
			tpl: new Ext.XTemplate(
				'<div class="new-home-highlight-item">',
				'	<div class="new-home-highlight-item-back">',
					'<div class="home-highlight-header"><tpl if="link"><a href="{link}" class="homelink" target="_blank">{title} <i class="fa fa-link"></i></a></tpl><tpl if="!link">{title}</tpl></div>',
					'<div class="new-home-highlight-item-desc"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{displayDesc}</div>',					
					'<div class="home-highlight-footer"><span style="font-size: 10px; float: left;">Updated: {[Ext.util.Format.date(values.updateDts, "m/d/y")]}</span><span style="margin-left: 20px;float: right;"><a href="#" class="homelink" onclick="homepage.readToggleHighlight(\'{highlightId}\');">{moreText}</a></span></div>',
				'	</div>',
				'</div>'
			)			
		}
	],	
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var carousel = panel.queryById('carousel');
			if (width < 1024) {
				carousel.setWidth('100%');										
			} else {
				carousel.setWidth('50%');										
			}			
		}
	},	
	initComponent: function () {
		this.callParent();			
		var infoPanel = this;
		
		
		var carousel = infoPanel.queryById('carousel');
		infoPanel.infoItems = [];	
		carousel.currentHighlighIndex = 0;
		var updateHighlight = function(slideDirOut, slideDirIn) {			
			if (carousel.currentHighlighIndex >= infoPanel.infoItems.length) {
				carousel.currentHighlighIndex = 0;
			} 

			if (carousel.currentHighlighIndex < 0) {
				carousel.currentHighlighIndex = infoPanel.infoItems-1;
			}

			carousel.update(infoPanel.infoItems[carousel.currentHighlighIndex]);
			var textel = Ext.query('.new-home-highlight-item-desc');
			var el = Ext.get(textel[0]);
			el.slideIn();	
										
		};		
		
		Ext.Ajax.request({
			url: 'api/v1/resource/highlights',
			success: function(response, opts) {
				var highlights = Ext.decode(response.responseText);
				
				Ext.Array.each(highlights, function(highlight){
					if (!highlight.link) {
						highlight.link = false;
					}
					highlight.moreText = 'Read More >>';
					highlight.displayDesc = Ext.util.Format.ellipsis(Ext.util.Format.stripTags(highlight.description), 700);

				});				
				
				Ext.Array.each(highlights, function(item){
					infoPanel.infoItems.push(item);
				});
				
				Ext.Ajax.request({
					url: 'api/v1/service/search/recent',
					success: function(response, opts) {
						var recent = Ext.decode(response.responseText);
						
//						Ext.Array.each(recent, function(item){
//							infoPanel.infoItems.push(item);
//						});
						
						infoPanel.highlightTask = Ext.TaskManager.newTask({
							run: function() {								
								updateHighlight();								
								carousel.currentHighlighIndex++;
							},
							interval: 10000
						});
						Ext.TaskManager.start(infoPanel.highlightTask);						
						updateHighlight();
						carousel.currentHighlighIndex++;
					}
				});
			}
		});
		
		
		
	}
	
});
