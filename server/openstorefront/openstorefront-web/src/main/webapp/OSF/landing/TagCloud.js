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

 /* global Ext, CoreUtil */

Ext.define('OSF.landing.TagCloud', {
	
	handler: function(record, item) {
		var tagCloud = this;		
		if (!tagCloud.view) {
			tagCloud.view = Ext.create('OSF.landing.TagCloudView', {				
			});
		}	
		tagCloud.view.show();
	}
	
});

Ext.define('OSF.landing.TagCloudView', {
	extend: 'Ext.window.Window',
	alias: 'widget.osf-tagcloudview',
	
	width: '70%',
	height: '70%',
	scrollable: true,
	maximizable: true,	
	modal: true,
	title: 'Tags - Click on Tag to view Entries. The more Entries tagged the larger the text.',
	iconCls: 'fa fa-lg fa-tags',
	listeners: {
		resize: function(view, width, height, oldWidth, oldHeight, eOpts) {
			if (width < 568) {
				this.maximize(false);
			} else {
				this.restore();
			}		
		}
	},	
	
	initComponent: function () {
		this.callParent();			
		var	tagView = this;
		
		CoreUtil.pageActions.viewTag = function(tag) {
				var searchObj = {
					"sortField": null,
					"sortDirection": "ASC",
					"startOffset": 0,
					"max": 2147483647,
					"searchElements": [{
							"searchType": "TAG",
							"field": null,
							"value": tag,
							"keyField": null,
							"keyValue": null,
							"startDate": null,
							"endDate": null,
							"caseInsensitive": false,
							"numberOperation": "EQUALS",
							"stringOperation": "EQUALS",
							"mergeCondition": "OR"
						}]
				};
				
				var searchRequest = {
					type: 'Advance',
					query: searchObj
				};
				
				CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
				window.location.href = 'searchResults.jsp';				
		};
		
		
		var tagPanel = Ext.create('Ext.panel.Panel', {			
			bodyStyle: 'padding: 20px;',			
			tpl: new Ext.XTemplate(
				'<tpl for=".">',
				' <span style="font-size: {tagSize}em; padding: 0px 10px 0px; 10px; line-height: 100%;"><a href="#" class="link" title="{count}" onclick="CoreUtil.pageActions.viewTag(\'{text}\');">{text}</a></span>',
				'</tpl>'
			)
		});			

		Ext.Ajax.request({
			url: 'api/v1/resource/components/tagviews',				
			success: function(response, opts) {
				var tags = Ext.decode(response.responseText);
				var groupedTags = [];
				Ext.Array.each(tags, function(tag) {
					var existingTag = Ext.Array.findBy(groupedTags, function(item){
						return item.text === tag.text;
					});
					if (existingTag) {
						existingTag.count++;
					} else {
						groupedTags.push({
							text: tag.text,
							count: 1
						});
					}
				});	
				groupedTags.sort(function(a, b){
					return a.text.localeCompare(b.text);
				});

				var maxFontSize = 5;
				var minFontSize = .625;
				var maxCount = 0;
				var minCount = 0;
				Ext.Array.each(groupedTags, function(tag) {
					if (maxCount < tag.count) {
						maxCount = tag.count;
					}
					if (minCount > tag.count) {
						minCount = tag.count;
					}
				});
				Ext.Array.each(groupedTags, function(tag) {
					var tagSize = Math.abs((maxFontSize * (tag.count - minCount)) / (maxCount - minCount));
					if (tagSize < minFontSize) {
						tagSize = minFontSize;
					}
					tag.tagSize = tagSize;
				});


				tagPanel.update(groupedTags);
			}
		});	
		
		tagView.add(tagPanel);
		
	}
	
});
