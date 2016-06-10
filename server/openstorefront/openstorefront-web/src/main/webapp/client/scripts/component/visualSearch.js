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
/* global Ext, CoreUtil */

Ext.define('OSF.component.VisualSearchPanel', {
	extend: 'Ext.draw.Container',
	alias: 'osf.widget.VisualSearchPanel',
	
	
	//height: 5000,
	//border: true,
	plugins: ['spriteevents'],
	//style: 'cursor: pointer',
	bodyStyle: 'background: #2d2c2c;',
	viewData: [],	

		
	initComponent: function () {
		this.callParent();

		var visPanel = this;
				
		visPanel.camera = {
			pan: false,
			panX: 0,
			panY: 0,
			startX: 0,
			startY: 0,
			panOriginX: 0,
			panOriginY: 0,
			zoom: 1.0,
			zoomCenterX: 0,
			zoomCenterY: 1
		};
	
		
		visPanel.on('spritemouseout', function(item, event, eOpts){
			var sprite = item && item.sprite;	
			if (sprite.node) {
				sprite.setAttributes({ 
					fillStyle: sprite.originalFill
				});
				sprite.getSurface().renderFrame();		
			}
		});		
		
		visPanel.on('spritemouseover', function(item, event, opts){
			var sprite = item && item.sprite;
			if (sprite.node) {
				sprite.originalFill = sprite.fillStyle;
				var fill = 'rgb(255,255, 0)';

				sprite.setAttributes({ 					
					fillStyle: fill
				});
				sprite.getSurface().renderFrame();						
			}
		});	

		visPanel.on('spritemousedown', function(sprite, event, opts){
			visPanel.camera.pan = true;		
			visPanel.camera.panOriginX = event.pageX;						
			visPanel.camera.panOriginY = event.pageY;
		});

		visPanel.on('spritemouseup', function(sprite, event, opts){
			visPanel.camera.pan = false;	
			visPanel.camera.startX = visPanel.camera.panX;
			visPanel.camera.startY = visPanel.camera.panY;
		});

		visPanel.on('spritemousemove', function(item, event, opts){
			var sprite = item && item.sprite;
			if (visPanel.camera.pan) {
				visPanel.camera.panX = visPanel.camera.startX + (event.pageX - visPanel.camera.panOriginX);						
				visPanel.camera.panY = visPanel.camera.startY + (event.pageY - visPanel.camera.panOriginY);	
				
				Ext.Array.each(visPanel.sprites, function(item){
					if (!item.backgroundSprite) {
						item.setAttributes({
							translationX: visPanel.camera.panX,
							translationY: visPanel.camera.panY					
						});
					}
				});
				
				visPanel.getSurface().renderFrame();
			} 
		});
		
		var zoom = function (e) {
			// cross-browser wheel delta
			e = window.event || e;
			
			if (e.target.id.indexOf('ext') !== -1) {
				var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
				
				visPanel.camera.zoom += (delta / 2);
				if (visPanel.camera.zoom < .1) {
					visPanel.camera.zoom = .1;
				}
				if (visPanel.camera.zoom > 4) {
					visPanel.camera.zoom = 4;
				}

				if (visPanel.camera.zoom >= .1 &&
						visPanel.camera.zoom <= 4)
				{
					visPanel.camera.zoomCenterX = e.pageX;
					visPanel.camera.zoomCenterY = e.pageY;
					
					Ext.Array.each(visPanel.sprites, function(item){
						if (!item.backgroundSprite) {
							item.setAttributes({
								scaleX:  visPanel.camera.zoom,
								scaleY:  visPanel.camera.zoom,
								scaleCenterX: visPanel.camera.zoomCenterX,
								scaleCenterY: visPanel.camera.zoomCenterY					
							});
						}
					});					

					visPanel.getSurface().renderFrame();	
				}
				//console.log(e);
				
				e.preventDefault();
			}			
		};

		if (window.addEventListener) {
			window.addEventListener("mousewheel", zoom, false);
			window.addEventListener("DOMMouseScroll", zoom, false);
		} else {
			window.attachEvent("onmousewheel", zoom);
		}	
		
	},
	
	reset: function() {
		var visPanel = this;
				
		visPanel.camera = {
			pan: false,
			panX: 0,
			panY: 0,
			startX: 0,
			startY: 0,
			panOriginX: 0,
			panOriginY: 0,
			zoom: 1.0,
			zoomCenterX: 0,
			zoomCenterY: 1
		};
		visPanel.loadRelationships();
	},
	
	afterRender: function() {
		this.callParent();
		
		var visPanel = this;
		visPanel.loadRelationships();
	},
	
	loadRelationships: function() {
		var visPanel = this;
		
		visPanel.setLoading("Loading Initial Relationships...");
		Ext.Ajax.request({
			url: '../api/v1/resource/componentrelationship',
			callback: function(){
				visPanel.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				
				var viewData = [];
				Ext.Array.each(data, function(relationship){
					viewData.push({
						type: 'component',
						nodeId: relationship.relationshipId,
						key: relationship.ownerComponentId,
						label: relationship.ownerComponentName,
						relationshipLabel: relationship.relationshipTypeDescription,
						targetKey: relationship.targetComponentId,
						targetName: relationship.targetComponentName
					});
				});
				
				visPanel.viewData = visPanel.viewData.concat(viewData);
				visPanel.initVisual(visPanel.viewData);
			}
		});			
	},
	
	initVisual: function(viewData) {
		var visPanel = this;
		
		//group and sort data
		
		var nodes = [];
		var nodeKeys = {};
		Ext.Array.each(viewData, function(node){
			if (!nodeKeys[node.key]) {
				nodes.push({
					key: node.key,
					name: node.label,
					type: node.type,
					edges: []
				});				
				nodeKeys[node.key] = true;
			}
			if (!nodeKeys[node.targetKey]) {
				nodes.push({
					key: node.targetKey,
					name: node.targetName,
					type: node.type,
					edges: []	
				});				
				nodeKeys[node.targetKey] = true;
			}
		});
		
		Ext.Array.each(viewData, function(relationship){
			var targetNode = Ext.Array.findBy(nodes, function(node) {
				if (node.key === relationship.targetKey) {
					return true;
				} else {
					return false;
				}
			});
			var ownerNode = Ext.Array.findBy(nodes, function(node) {
				if (node.key === relationship.key) {
					return true;
				} else {
					return false;
				}
			});
			ownerNode.edges.push({
				targetKey: relationship.targetKey,
				ownerKey: relationship.key,
				relationshipLabel: relationship.relationshipLabel
			});
		});
		
		
		
		var mainSprites = [];
		var sprites = [];
		
				
		var containerHeight = visPanel.getHeight();
		var containerWidth = visPanel.getWidth();		
		
		
		var cameraSprite = {
			backgroundSprite: true,
			type: 'rect',
			x: 0,
			y: 0,
			width: containerWidth,
			height: containerHeight,
			fillStyle: 'rgba(0, 0, 0, 0)'
		};
		
//		
		for(var i=30; i<=containerWidth; i=i+30) {
			mainSprites.push({
				backgroundSprite: true,
				type: 'line',
				fromX: i,
				fromY: 0,
				toX: i,
				toY: containerHeight,
				lineWidth: 1,
				strokeStyle: 'rgba(255, 255, 255, .2)'
			});
		}
		
		for(var i=30; i<=containerHeight; i=i+30) {
			mainSprites.push({
				backgroundSprite: true,
				type: 'line',
				fromX: 0,
				fromY: i,
				toX: containerWidth,
				toY: i,
				lineWidth: 1,
				strokeStyle: 'rgba(255, 255, 255, .2)'
			});
		}		
		mainSprites.push(cameraSprite);		
		
		var startX = 150;
		var startY = 250;
		var rowCount  = 0;
		var nodeRadius = 20;
	
		
		var renderNodes = {};
		
		var textNode = {
			type: 'text',
			textAlign: 'center',
			fontSize: 12,
			shadowColor: 'rgba(0, 0, 0, 1)',
			shadowOffsetX: 2, 
			shadowOffsetY: 2,
			//strokeStyle: 'black',
			fillStyle: 'rgba(255, 255, 255, 1)'
		};
		
		var componentNode = {
			type: 'circle',
			r: nodeRadius, 
			fillStyle: 'orange',
			lineWidth: 3,
			strokeStyle: 'rgba(255, 255, 255, 1)'			
		};
		var hubFillStyle = 'rgba(87, 160, 204, 1)';
		
		var relationshipEdge = {
			type: 'line',
			lineWidth: 3,			
			strokeStyle: 'rgba(255, 255, 255, .6)'			
		};	
		
		var relationshipText = {
			type: 'text',
			textAlign: 'center',
			fillStyle: 'rgba(255, 255, 255, 1)'
		};
		
		var arrowLength = 10;
		var arrowLine =  {
			type: 'line',
			lineWidth: 3,
			strokeStyle: 'rgba(200, 200, 200, 1)'		
		};
		
		
		Ext.Array.each(nodes, function(node) {
			
			if (!renderNodes[node.key]) {

				var setNodePosition = true;
				if (node.edges.length > 0) {
					var edgeNode =  Ext.Array.findBy(nodes, function(item) {
						if (item.key === node.edges[0].targetKey) {
							return true;
						} else {
							return false;
						}
					});
					
					if (edgeNode.positionX && edgeNode.positionY) {
						node.positionX = edgeNode.positionX - (componentNode.r  * 20 - nodeRadius);
						node.positionY = edgeNode.positionY;
						setNodePosition = false;
					}
					
				}

				if (setNodePosition) {
					node.positionX = startX + (componentNode.r *6) + 40;
					node.positionY = startY;
				}
				
				var hubNodeRadius = nodeRadius + (node.edges.length*3);
				
				sprites.push(Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY,
					node: node,
					r: hubNodeRadius,
					fillStyle: hubFillStyle
				}, componentNode));
				
				sprites.push(Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY + hubNodeRadius + 15,
					text: Ext.util.Format.ellipsis(node.name, 20),
					node: node,
					nodeText: true
				}, textNode));

			
				var rotation = 0;
				Ext.Array.each(node.edges, function(edgeNode) {
					
					if (!renderNodes[edgeNode.targetKey]) {	
						
						var targetNode = Ext.Array.findBy(nodes, function(item) {
							if (item.key === edgeNode.targetKey) {
								return true;
							} else {
								return false;
							}
						});
						
						
						targetNode.positionX = node.positionX;
						targetNode.positionY = node.positionY - componentNode.r * 10;
						targetNode.rotationDegrees = rotation;
						
						var point = new Ext.draw.Point(targetNode.positionX, targetNode.positionY);
						point = point.rotate(rotation, new Ext.draw.Point(node.positionX, node.positionY) );
						targetNode.positionX = point.x;
						targetNode.positionY = point.y;
						
						sprites.push(Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY,
							node: edgeNode
						}, componentNode));

						sprites.push(Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY + nodeRadius + 15,
							text: Ext.util.Format.ellipsis(targetNode.name, 20),
							node: edgeNode,
							nodeText: true
						}, textNode));					
						
						rotation += 45; 
						renderNodes[edgeNode.targetKey] = true;
					}
				});
			
				startX += (componentNode.r  * 20) + nodeRadius;
				
				rowCount++;
				if (rowCount >= 5) {
					startX = 150;
					startY += 450;
					rowCount = 0;
				}				
				
				renderNodes[node.key] = true;
			}
		});
		
		//add edges
	
		Ext.Array.each(viewData, function(relationship){
			
			var targetNode = Ext.Array.findBy(nodes, function(node) {
				if (node.key === relationship.targetKey) {
					return true;
				} else {
					return false;
				}
			});
			var ownerNode = Ext.Array.findBy(nodes, function(node) {
				if (node.key === relationship.key) {
					return true;
				} else {
					return false;
				}
			});	
			
			var dx = targetNode.positionX - ownerNode.positionX;
			var dy = targetNode.positionY - ownerNode.positionY;
			var length = Math.sqrt(dx * dx + dy * dy);
			if (length > 0)
			{
				dx /= length;				
				dy /= length;
			}
			dx *= length - nodeRadius;
			dy *= length - nodeRadius;
			var endX = (ownerNode.positionX + dx);
			var endY = (ownerNode.positionY + dy);				
						
			sprites.push(Ext.apply({}, {
				fromX: ownerNode.positionX,
				fromY: ownerNode.positionY,
				toX: endX,					
				toY: endY				
			}, relationshipEdge));	
			
			
			
			dx = endX - ownerNode.positionX;
			dy = endY - ownerNode.positionY;

			var theta = Math.atan2(dy, dx);
			var rad = 35 * (Math.PI/180); //35 angle
			var x = endX - arrowLength * Math.cos(theta + rad);
			var y = endY - arrowLength * Math.sin(theta + rad);

			var phi2 = -35 * (Math.PI/180);//-35 angle
			var x2 = endX - arrowLength * Math.cos(theta + phi2);
			var y2 = endY - arrowLength * Math.sin(theta + phi2);					
						
			sprites.push(Ext.apply({}, {
				fromX: endX,
				fromY: endY,
				toX: x,					
				toY: y
			}, arrowLine));
			
			sprites.push(Ext.apply({}, {
				fromX: endX,
				fromY: endY,
				toX: x2 ,					
				toY: y2
			}, arrowLine));	
			
			var xAdjust = 0;
			if (theta === (90 * (Math.PI/180))) {
				xAdjust = 10;
			} 
			if (theta === (-90 * (Math.PI/180))) {
				xAdjust = -10;
			}
			
			sprites.push(Ext.apply({}, {
				x:  (endX + ownerNode.positionX) /2 + xAdjust,
				y:  ownerNode.positionY + (endY - ownerNode.positionY)/ 2 - 10,
				text: Ext.util.Format.ellipsis(relationship.relationshipLabel, 20),
				rotationRads: theta
			}, relationshipText));				
			
			
		});
	
		sprites.reverse();
		mainSprites = Ext.Array.merge(mainSprites, sprites);
		
		visPanel.getSurface().removeAll(true);
		
		visPanel.setSprites(mainSprites);		
		visPanel.renderFrame();		
		
	}
		
});

Ext.define('OSF.component.VisualContainerPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.VisualContainerPanel',
	
	scrollable: true,
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			style: 'background: darkgrey;',
			items: [
				{
					xtype: 'combo',
					fieldLabel: 'Initial View',
					valueField: 'code',
					displayField: 'description',
					typeAhead: false,
					editable: false,
					value: 'RELATION',
					store: {
						data: [
							{ code: 'RELATION', description: 'Relationships' },
							{ code: 'ORG', description: 'Organization' },
							{ code: 'ATT', description: 'Attributes' },
							{ code: 'TAGS', description: 'Tags' }
						]
					}
				}, 
				{
					xtype: 'tbfill'					
				},
				{
					text: 'Reset',
					iconCls: 'fa fa-reply',
					handler: function(){
						var containerPanel = this.up('panel');
						containerPanel.visualPanel.reset();
					}
				}
			]
		}
	],		
	
	initComponent: function () {
		this.callParent();

		var containerPanel = this;
		
		containerPanel.visualPanel = Ext.create('OSF.component.VisualSearchPanel', {
			
		});
		
		containerPanel.add(containerPanel.visualPanel);
		
		Ext.defer(function(){
			containerPanel.updateLayout(true, true);
		}, 100);
		
	}, 
	afterRender: function() {
		this.callParent();
		
		var containerPanel = this;
		Ext.defer(function(){
			containerPanel.visualPanel.setWidth(containerPanel.getWidth());
			containerPanel.visualPanel.setHeight(containerPanel.getHeight()-40);
		}, 100);
	}
	
});