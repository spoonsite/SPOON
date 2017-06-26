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
/* global Ext, CoreUtil */

Ext.define('OSF.component.RelationshipVisPanel', {
	extend: 'Ext.draw.Container',
	alias: 'osf.widget.RelationshipVisPanel',
	
	width: "100%",
	height: 500,
	border: true,
	plugins: ['spriteevents'],
	style: 'cursor: pointer',
		
	initComponent: function () {
		this.callParent();

		var visPanel = this;
		
		var compareViewTemplate = new Ext.XTemplate(						
		);

		Ext.Ajax.request({
				url: 'Router.action?page=shared/entryCompareTemplate.jsp',
				success: function(response, opts){
					compareViewTemplate.set(response.responseText, true);
				}
		});	
		visPanel.viewWin = Ext.create('Ext.window.Window', {			
			title: 'Quick Details View',			
			modal: true,
			width: '80%',
			height: '80%',
			maximizable: true,
			scrollable: true,		
			bodyStyle: 'padding: 20px;',
			tpl: compareViewTemplate,
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'bottom',
					items: [
						{
							xtype: 'tbfill'
						},
						{
							text: 'Open Details',
							iconCls: 'fa fa-arrows-alt',
							handler: function() {
								var relatedwin = window.open('view.jsp?fullPage=true&id=' + visPanel.viewWin.componentId, "RelatedWindow");
							}
						},
						{
							xtype: 'tbfill'
						}
					]
				}
			]
		});		
		
		visPanel.on('resize', function(panel, width, height, oldWidth, oldHeight, eOpts){
			panel.drawVisual();
		});
		
		visPanel.on('spriteclick', function(item, event, eOpts){
			var sprite = item && item.sprite;
			if (sprite.componentId && !sprite.openComponent) {
				
				visPanel.setLoading(true);
				//load the 
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + sprite.componentId + '/relationships/all',
					callback: function(){
						visPanel.setLoading(false);
					},
					success: function(response, opts) {
						var relationships = Ext.decode(response.responseText);
						var entry = {
							componentId: sprite.componentId,
							name: sprite.componentName,
							relationships: relationships
						};
						visPanel.updateDiagramData(entry);
						visPanel.setHeight(entry.relationships.length*80);
						visPanel.drawVisual();
					}
				});				
			} else if (sprite.openComponent) {
				visPanel.viewWin.show();
				visPanel.viewWin.componentId = sprite.componentId;
				visPanel.viewWin.setLoading(true);
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + sprite.componentId + '/detail',
					callback: function(){
						visPanel.viewWin.setLoading(false);
					}, 
					success: function(response, opts) {
						var data = Ext.decode(response.responseText);
						data = CoreUtil.processEntry(data);
						
						CoreUtil.calculateEvalutationScore({
							fullEvaluations: data.fullEvaluations,
							evaluation: data.fullEvaluations,
							success: function (newData) {
								data.fullEvaluations = newData.fullEvaluations;
								visPanel.viewWin.update(data);

								// Add event listeners for toggle-able containers
								var toggleElements = document.querySelectorAll('.toggle-collapse');
								for (ii = 0; ii < toggleElements.length; ii += 1) {
									toggleElements[ii].removeEventListener('click', CoreUtil.toggleEventListener);
									toggleElements[ii].addEventListener('click', CoreUtil.toggleEventListener);
								}
							}
						});
					}
				});
				
			}
		});
		
		visPanel.on('spritemouseout', function(item, event, eOpts){
			var sprite = item && item.sprite;
			if (sprite.componentBlock) {
				sprite.setAttributes({ 
					fillStyle: sprite.originalFill
				});
				sprite.getSurface().renderFrame();			
			}
		});
		
		visPanel.on('spritemouseover', function(item, event, eOpts){		
			var sprite = item && item.sprite;
			if (sprite.componentBlock) {
				sprite.originalFill = sprite.fillStyle;
				var fill = 'rgb(225,225, 225)';
				if (sprite.type === 'text') {
					fill = 'rgb(80,80, 80)';
				}
				sprite.setAttributes({ 					
					fillStyle: fill
				});
				sprite.getSurface().renderFrame();			
			}			
		});
	},
	
	afterRender: function() {
		this.callParent();
		
		var visPanel = this;
		Ext.defer(function(){
			visPanel.drawVisual();		
		}, 50, visPanel);
	},
	
	drawVisual: function(){
		var visPanel = this;
		
		
		if (visPanel.entry) {

			var containerHeight = visPanel.getHeight();
			var containerWidth = visPanel.getWidth();

			var sprites = [{
				type: 'rect',	
				x: 0,
				y: 0,
				width: containerWidth,
				height: containerHeight,
				fillStyle: 'rgb(255,255,255)'				
			}];

			var mainBlockWidth = Math.round(containerWidth * .15);
			var mainBlockHeight = 40;
			var mainBlockX = Math.round(containerWidth / 2) - Math.round(mainBlockWidth/2);
			var mainBlockY = Math.round(containerHeight / 2) - Math.round(mainBlockHeight/2);
			
			var textChop = function(text, maxWidth) {
				maxWidth = maxWidth - 20;
				var maxLength = text.length;
				var newText = text;
				for (var i=maxLength; i > 0; i--) {								
					var size = Ext.draw.TextMeasurer.measureTextSingleLine(newText, "helvetica");
					if (size.width <= maxWidth) {
						break;
					}
					newText = Ext.String.ellipsis(newText, i);
				}
				return newText;
			};


			sprites.push({
				type: 'rect',
				x: mainBlockX,
				y: mainBlockY,
				width: mainBlockWidth,
				height: mainBlockHeight,
				fillStyle: 'rgb(191, 229, 247)',
				strokeStyle: 'rgb(100, 100, 100)',
				shadowOffsetX: 8,
				shadowOffsetY: 8,
				shadowColor: 'rgb(25,25,25)',
				shadowBlur: 10,
				lineWidth: 3
				
			});
			
			sprites.push({
				type: 'text',
				text: textChop(visPanel.entry.name, mainBlockWidth),
				textAlign: 'center',
				x: mainBlockX + (mainBlockWidth/2),
				y: mainBlockY + (mainBlockHeight/2),
				fillStyle: 'rgb(0, 0, 0)'			
			});			
			
			if (visPanel.originalComponentId && 
						visPanel.originalComponentId !== visPanel.entry.componentId) {					
					
				var plus = {
					type: 'plus',
					componentId: visPanel.entry.componentId,
					openComponent: true,
					x: mainBlockX + (mainBlockWidth- 10),
					y: mainBlockY + 10,
					fillStyle: '#1F6D91',					
					size: 5
				};
				
				sprites.push(plus);
			}

			var pointedToMe = [];
			var pointedTo = [];
			Ext.Array.each(visPanel.entry.relationships, function(relation){
				if (relation.targetComponentId !== visPanel.entry.componentId) {
					pointedTo.push(relation);
				} else {
					pointedToMe.push(relation);
				}
			});
						
						
			var offset = 0;			
			Ext.Array.each(pointedTo, function(relation){
				var block = {
					type: 'rect',
					x: mainBlockX + mainBlockWidth * 2,
					y: Math.round(containerHeight / (pointedTo.length+1)) - Math.round(mainBlockHeight/2) + offset,
					width: mainBlockWidth,
					height: mainBlockHeight,
					componentId: relation.targetComponentId,
					componentName: relation.targetComponentName,
					componentBlock: true,					
					fillStyle: 'rgb(215, 189, 146)',
					strokeStyle: 'rgb(100, 100, 100)',
					shadowOffsetX: 8,
					shadowOffsetY: 8,
					shadowColor: 'rgb(25,25,25)',
					shadowBlur: 10,
					lineWidth: 3
				};				
				offset += mainBlockHeight + 30;
			
				var blockLine = {
					type: 'line',
					fromX: mainBlockX + mainBlockWidth,
					fromY: mainBlockY + (mainBlockHeight/2),	
					toX: block.x,
					toY: block.y + (mainBlockHeight/2),		
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'			
				};
				
				//arrows
				var arrowLength = 10;
				var dx = blockLine.toX - blockLine.fromX;
				var dy = blockLine.toY - blockLine.fromY;
				
				var theta = Math.atan2(dy, dx);
				var rad = 35 * (Math.PI/180); //35 angle
				var x = blockLine.toX - arrowLength * Math.cos(theta + rad);
				var y = blockLine.toY - arrowLength * Math.sin(theta + rad);
				
				var phi2 = -35 * (Math.PI/180);//-35 angle
				var x2 = blockLine.toX - arrowLength * Math.cos(theta + phi2);
				var y2 = blockLine.toY - arrowLength * Math.sin(theta + phi2);				
				
				var arrowTop = {
					type: 'line',
					fromX: blockLine.toX,
					fromY: blockLine.toY,	
					toX: x,
					toY: y,
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'			
				};	
				
				var arrowBottom = {
					type: 'line',
					fromX: blockLine.toX,
					fromY: blockLine.toY,	
					toX: x2,
					toY: y2,
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'				
				};				
				
				var blockText = {
					type: 'text',
					text: textChop(relation.targetComponentName, block.width),
					textAlign: 'center',
					componentBlock: true,
					componentId: relation.targetComponentId,
					componentName: relation.targetComponentName,
					x: block.x + (block.width/2),
					y: block.y + (block.height/2),									
					fillStyle: 'rgb(0, 0, 0)'			
				};				
				
				//arrow text
				var arrowText = {
					type: 'text',
					text: relation.relationshipTypeDescription,
					textAlign: 'center',
					x: (blockLine.toX + blockLine.fromX)/2,
					y: blockLine.fromY + (blockLine.toY - blockLine.fromY)/ 2 - 10,									
					fillStyle: 'rgb(0, 0, 0)',
					rotationRads: theta
				};					
				
				var plus = {
					type: 'plus',
					componentId: relation.targetComponentId,
					openComponent: true,
					x: block.x + (block.width - 10),
					y: block.y + 10,
					fillStyle: '#1F6D91',					
					size: 5
				};
				
				sprites.push(block);
				sprites.push(blockText);
				sprites.push(blockLine);
				sprites.push(arrowTop);
				sprites.push(arrowBottom);
				sprites.push(arrowText);
				if (visPanel.originalComponentId && 
						visPanel.originalComponentId !== relation.targetComponentId) {					
					sprites.push(plus);
				}
			});

			var offset = 0;	
			Ext.Array.each(pointedToMe, function(relation){
				var block = {
					type: 'rect',
					x: mainBlockX - mainBlockWidth*2,
					y: Math.round(containerHeight / (pointedToMe.length+1)) - Math.round(mainBlockHeight/2) + offset,
					width: mainBlockWidth,
					height: mainBlockHeight,
					componentId: relation.ownerComponentId,
					componentName: relation.ownerComponentName,
					componentBlock: true,										
					fillStyle: 'rgb(234, 232, 230)',
					strokeStyle: 'rgb(100, 100, 100)',
					shadowOffsetX: 8,
					shadowOffsetY: 8,
					shadowColor: 'rgb(25,25,25)',
					shadowBlur: 10,
					lineWidth: 3
				};				
				offset += mainBlockHeight + 30;				
				
				var blockLine = {
					type: 'line',
					fromX: mainBlockX,
					fromY: mainBlockY + (mainBlockHeight/2),	
					toX: block.x + mainBlockWidth,
					toY: block.y + (mainBlockHeight/2),		
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'			
				};
								
				var blockText = {
					type: 'text',
					text: textChop(relation.ownerComponentName, block.width),
					textAlign: 'center',
					componentBlock: true,
					componentId: relation.ownerComponentId,
					componentName: relation.ownerComponentName,
					x: block.x + (block.width/2),
					y: block.y + (block.height/2),									
					fillStyle: 'rgb(0, 0, 0)'					
				};				
				
				//arrows
				var arrowLength = 10;
				var dx = blockLine.fromX - blockLine.toX;
				var dy = blockLine.fromY - blockLine.toY;
				
				var theta = Math.atan2(dy, dx);
				var rad = 35 * (Math.PI/180); //35 angle
				var x = blockLine.fromX - arrowLength * Math.cos(theta + rad);
				var y = blockLine.fromY - arrowLength * Math.sin(theta + rad);
				
				var phi2 = -35 * (Math.PI/180);//-35 angle
				var x2 = blockLine.fromX - arrowLength * Math.cos(theta + phi2);
				var y2 = blockLine.fromY - arrowLength * Math.sin(theta + phi2);				
				
				var arrowTop = {
					type: 'line',
					fromX: blockLine.fromX,
					fromY: blockLine.fromY,	
					toX: x,
					toY: y,
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'			
				};	
				
				var arrowBottom = {
					type: 'line',
					fromX: blockLine.fromX,
					fromY: blockLine.fromY,	
					toX: x2,
					toY: y2,
					lineWidth: 3,
					strokeStyle: 'rgb(50, 50, 50)'				
				};					
				
				//arrow text
				var arrowText = {
					type: 'text',
					text: relation.relationshipTypeDescription,
					textAlign: 'center',
					x: (blockLine.toX + blockLine.fromX)/2,
					y: blockLine.fromY + (blockLine.toY - blockLine.fromY)/ 2 - 10,									
					fillStyle: 'rgb(0, 0, 0)',
					rotationRads: theta
				};	
				
				var plus = {
					type: 'plus',
					componentId: relation.ownerComponentId,
					openComponent: true,
					x: block.x + (block.width - 10),
					y: block.y + 10,
					fillStyle: '#1F6D91',
					size: 5
				};				
				
				sprites.push(block);
				sprites.push(blockText);
				sprites.push(blockLine);
				sprites.push(arrowTop);
				sprites.push(arrowBottom);				
				sprites.push(arrowText);
				if (visPanel.originalComponentId && 
						visPanel.originalComponentId !== relation.ownerComponentId) {					
					sprites.push(plus);
				}
			});
			

			visPanel.setSprites(sprites);
			visPanel.renderFrame();
		}		
	},
	
	updateDiagramData: function(entry) {
		var visPanel = this;
		visPanel.entry = entry;
	}
	
});