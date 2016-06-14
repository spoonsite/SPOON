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
	
	/**
	 * RELATION, TAGS, ORG, ATT
	 */
	viewType: 'RELATION', 
		
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
			zoomCenterY: 0,
			update: function(sprites){
				Ext.Array.each(visPanel.sprites, function(item){
					if (!item.backgroundSprite) {
						item.setAttributes({
							translationX: visPanel.camera.panX,
							translationY: visPanel.camera.panY,
							scaleX:  visPanel.camera.zoom,
							scaleY:  visPanel.camera.zoom,
							scaleCenterX: visPanel.camera.zoomCenterX,
							scaleCenterY: visPanel.camera.zoomCenterY									
						});
					}
				});
				visPanel.getSurface().renderFrame();
			}
		};
		
		visPanel.on('spritemouseout', function(item, event, eOpts){
			var sprite = item && item.sprite;	
			if (sprite.node) {
				visPanel.setStyle({
					cursor: 'default'
				});
				
				sprite.setAttributes({ 
					fillStyle: sprite.originalFill
				});
				sprite.getSurface().renderFrame();		
			}
		});		
		
		visPanel.on('spritemouseover', function(item, event, opts){
			var sprite = item && item.sprite;
			if (sprite.node) {
				visPanel.setStyle({
					cursor: 'pointer'
				});
				
				sprite.originalFill = sprite.fillStyle;
				var fill = 'rgb(255,255, 0)';

				sprite.setAttributes({ 					
					fillStyle: fill
				});
				sprite.getSurface().renderFrame();						
			}
		});
		
		visPanel.on('spriteclick', function(item, event, opts){
			var sprite = item && item.sprite;
			if (sprite.node && sprite.nodeText) {
				var winWidth = 500;
				var winHeight = 400;
				var descriptionWindow = Ext.create('Ext.window.Window', {
					title: 'Details',
					closeAction: 'destroy',
					scrollable: true,
					width: winWidth,
					height: winHeight,
					modal: true,
					closeToolText: '',
					bodyStyle: 'padding: 5px;',
					maximizable: true,
					tpl: new Ext.XTemplate(
						'<h1>{name}</h1><i>{componentTypeLabel}</i><hr>',
						'{description}'
					)
				});
				
				var winX = event.pageX;
				var winY = event.pageY;
				if ((event.pageX + winWidth) > visPanel.getWidth()) {
					winX = visPanel.getWidth() - (winWidth+ 10);
				}
				
				if ((event.pageY + winHeight) > visPanel.getHeight()) {
					winY = visPanel.getHeight() - (winHeight - 10);
				}				
				descriptionWindow.showAt(winX, winY, true);
				
				if (sprite.node.type === 'component') {					
					descriptionWindow.setLoading(true);
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + (sprite.node.key ? sprite.node.key : sprite.node.targetKey) + '/detail',
						callback: function(){
							descriptionWindow.setLoading(false);
						},
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
							descriptionWindow.update(data);
						}
					});
				} else if (sprite.node.type === 'tag') {
					descriptionWindow.update({
						name: sprite.node.name,
						description: '',
						componentTypeLabel: 'Tag'
					});					
				} else if (sprite.node.type === 'organization') {
					descriptionWindow.setLoading(true);
					Ext.Ajax.request({
						url: '../api/v1/resource/organizations/' + (sprite.node.key ? sprite.node.key : sprite.node.targetKey),
						callback: function(){
							descriptionWindow.setLoading(false);
						},
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
							data.componentTypeLabel = 'Organization';
							descriptionWindow.update(data);
						}
					});
				} else  if (sprite.node.type === 'attribute') {
					
				} else {
					descriptionWindow.close();
				}
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
				
				visPanel.camera.update();
			} 
		});
		
		var zoom = function (e) {
			// cross-browser wheel delta
			e = window.event || e;
			
			if (e.target.nodeName.toLowerCase() === 'canvas' || e.target.nodeName.toLowerCase() === 'svg') {
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
					
					visPanel.camera.update();	
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
				
		visPanel.camera = Ext.apply(visPanel.camera, {
			pan: false,
			panX: 0,
			panY: 0,
			startX: 0,
			startY: 0,
			panOriginX: 0,
			panOriginY: 0,
			zoom: 1.0,
			zoomCenterX: 0,
			zoomCenterY: 0
		}, visPanel.camera);
		visPanel.update();
		
		visPanel.viewData = [];
		if (visPanel.viewType === "RELATION") {
			visPanel.loadRelationships();
		} else if (visPanel.viewType === "TAGS") {
			visPanel.loadTags();
		} else if (visPanel.viewType === "ORG") {
			visPanel.loadOrganizations();
		} else if (visPanel.viewType === "ATT") {
			visPanel.loadAttributes();
		}		
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
						targetName: relationship.targetComponentName,
						targetType: 'component'
					});
				});
				
				visPanel.viewData = visPanel.viewData.concat(viewData);
				visPanel.initVisual(visPanel.viewData);				
			}
		});			
	},
		
	loadTags: function() {
		var visPanel = this;
		
		visPanel.setLoading("Loading Initial Tags...");
		Ext.Ajax.request({
			url: '../api/v1/resource/components/tagviews?approvedOnly=true',
			callback: function(){
				visPanel.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				
				var viewData = [];
				Ext.Array.each(data, function(tagview){
					viewData.push({
						type: 'component',
						nodeId: tagview.tagId,
						key: tagview.componentId,
						label: tagview.componentName,
						relationshipLabel: '',
						targetKey: tagview.text,
						targetName: tagview.text,
						targetType: 'tag'
					});
				});
				
				visPanel.viewData = visPanel.viewData.concat(viewData);
				visPanel.initVisual(visPanel.viewData);				
			}
		});		
		
	},
	
	loadOrganizations: function() {
		var visPanel = this;
		
		visPanel.setLoading("Loading Organizations...");
		Ext.Ajax.request({
			url: '../api/v1/resource/organizations/componentrelationships',
			callback: function(){
				visPanel.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				
				var viewData = [];
				Ext.Array.each(data, function(tagview){
					viewData.push({
						type: 'organization',
						nodeId: tagview.tagId,
						key: tagview.organizationId,
						label: tagview.organizationName,
						relationshipLabel: '',
						targetKey: tagview.componentId,
						targetName: tagview.componentName,
						targetType: 'component'
					});
				});
				
				visPanel.viewData = visPanel.viewData.concat(viewData);
				visPanel.initVisual(visPanel.viewData);				
			}
		});		
		
	},
	
	loadAttributes: function() {
		var visPanel = this;
		
		//prompt for type to display
		
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
				relationshipLabel: relationship.relationshipLabel,
				name: targetNode.name,
				type: relationship.targetType
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
				strokeStyle: 'rgba(255, 255, 255, .1)'
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
				strokeStyle: 'rgba(255, 255, 255, .1)'
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
		var tagNode = {
			type: 'diamond',
			size: nodeRadius, 
			fillStyle: 'rgba(87, 160, 204, 1)',
			lineWidth: 3,
			strokeStyle: 'rgba(255, 255, 255, 1)'			
		};		
		
		var organizationNode={
			type: 'square',
			size: nodeRadius, 
			fillStyle: 'rgba(160, 160, 160, 1)',
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
		
		//hubs and edges
		var hubs = [];		
		Ext.Array.each(nodes, function(node) {
			
			if (!renderNodes[node.key]) {

				var hub = {
					spriteConfigs: [],
					addNode: function(spriteConfig) {
						var me = this;
						me.spriteConfigs.push(spriteConfig);						
						
						var buffer = nodeRadius*3;
						Ext.Array.each(me.spriteConfigs, function(n) {
							if (!me.bbox) {
								me.bbox = {
									minX: n.positionX,
									minY: n.positionY,
									maxX: n.positionX,
									maxY: n.positionY,
									contains: function(x, y) {
										var thisBBox = this;
										
										if (x >= thisBBox.minX && 
											x <= thisBBox.maxX &&
										  y >= thisBBox.minY &&
										  y <= thisBBox.maxY) {
											return true;
										} else {
											return false;
										}
									},
									overlaps: function(bbox) {
										var thisBBox = this;
										if (thisBBox.contains(bbox.minX, bbox.minY) ||
											thisBBox.contains(bbox.maxX, bbox.maxY))
										{
											return true;
										} else {
											return false;
										}
									},
									compare: function(bbox) {
										var thisBBox = this;
										if (thisBBox.minX === bbox.minX &&
											thisBBox.minY === bbox.minY &&
											thisBBox.maxX === bbox.maxX &&
											thisBBox.maxY === bbox.maxY) {										
											return 0;
										} else if (thisBBox.minX < bbox.minX ||
											thisBBox.minY < bbox.minY && 
											(thisBBox.maxX < bbox.maxX ||
											thisBBox.maxY < bbox.maxY)) {	
											return 1;											
										} else if (thisBBox.minX > bbox.minX ||
											thisBBox.minY > bbox.minY &&
											(thisBBox.maxX > bbox.maxX &&
											thisBBox.maxY > bbox.maxY)) {	
											return -1;
										} else {
											return 2;
										}										
									}
								};
							} else {						
								if (n.positionX < me.bbox.minX) {
									me.bbox.minX = n.positionX;
								}
								if (n.positionX > me.bbox.maxX) {
									me.bbox.maxX = n.positionX;
								}
								if (n.positionY < me.bbox.minY) {
									me.bbox.minT = n.positionY;
								}
								if (n.positionY > me.bbox.maxY) {
									me.bbox.maxY = n.positionY;
								}							
							}
						});
						me.bbox.minX -= buffer;
						me.bbox.minY -= buffer;
						me.bbox.maxX += buffer;
						me.bbox.maxY += buffer;						
					}
					
				};
				hubs.push(hub);

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
						node.positionX = edgeNode.positionX - (componentNode.r  * 25 - nodeRadius);
						node.positionY = edgeNode.positionY;
						setNodePosition = false;
					}
					
				}

				if (setNodePosition) {
					node.positionX = startX + (componentNode.r *6) + 40;
					node.positionY = startY;
				}
				
				var hubNodeRadius = nodeRadius + (node.edges.length*3);
				
				var baseNode =componentNode; 
				if (node.type === 'tag') {
					baseNode = tagNode; 
				}
				if (node.type === 'organization') {
					baseNode = organizationNode; 
				}				
				node.nodeSize = hubNodeRadius;
				
				sprites.push(Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY,
					node: node,
					r: node.nodeSize
					//fillStyle: hubFillStyle
				}, baseNode));
				
				sprites.push(Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY + hubNodeRadius + 15,
					text: Ext.util.Format.ellipsis(node.name, 20),
					node: node,
					nodeText: true
				}, textNode));
				
				hub.addNode(node);				
			
				var rotation = 0;
				var distanceFromHub = componentNode.r * 10;
				var generation = 1;				
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
						targetNode.positionY = node.positionY - (distanceFromHub * generation);
						targetNode.rotationDegrees = rotation;
						
						
						var point = new Ext.draw.Point(targetNode.positionX, targetNode.positionY);
						point = point.rotate(rotation, new Ext.draw.Point(node.positionX, node.positionY) );
						targetNode.positionX = point.x;
						targetNode.positionY = point.y;
						
						var baseNode = componentNode; 
						if (edgeNode.type === 'tag') {
							baseNode = tagNode; 
							targetNode.nodeSize = baseNode.size;
						} else { 
							targetNode.nodeSize = baseNode.r;
						}
						
						
						sprites.push(Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY,
							node: edgeNode
						}, baseNode));

						sprites.push(Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY + nodeRadius + 15,
							text: Ext.util.Format.ellipsis(targetNode.name, 20),
							node: edgeNode,
							nodeText: true
						}, textNode));					
						
						if ((rotation + 45) >= 360) {
							generation++;
							rotation = 0;
						} 
						rotation +=  (45 / generation); 
						hub.addNode(targetNode);
						
						renderNodes[edgeNode.targetKey] = true;
					}
				});
			
			
				//calc bbox for hub 
				var bbox = hub.bbox();
			
			
				//
				startX = bbox.maxX;
				
				
//				rowCount++;
//				if (rowCount >= 5) {
//					startX = 150;
//					startY += 500;
//					rowCount = 0;
//				}				
				
				renderNodes[node.key] = true;
			}
		});
		
		//spread out nodes so they don't over lap
//		Ext.Array.each(hubs, function(h) {
//			if (h.bbox().contains(startX)) {
//				var maxBBox;
//				Ext.Array.each(hubs, function(h) {
//					if (!maxBBox) {
//						menubar
//					}
//				}
//				start = maxBBox.getM
//			}
//		});
				
		
		
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
			dx *= length - targetNode.nodeSize;
			dy *= length - targetNode.nodeSize;
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
		
		if (!visPanel.initSurface) {
			visPanel.getSurface().setBackground({
				type: 'rect',
				x: 0,
				y: 0,
				width: containerWidth,
				height: containerHeight,
				fillStyle: 'rgba(29, 39, 38, 1)'
			});
			visPanel.initSurface = true;
		}

		//legend
		

		visPanel.setSprites(mainSprites);			
		visPanel.renderFrame();		
		
		if (visPanel.completedInit){
			visPanel.completedInit(nodes);
		}
		
	},
	
	zoomTo:function(x, y, zoom) {
		var visPanel = this;
		
		var containerHeight = visPanel.getHeight();
		var containerWidth = visPanel.getWidth();
		
		//put sprite  back to 0,0 then move to center (Keep in mind the sprites doesn't position doesn't move it just has matrixes applied to it)
		visPanel.camera.panX = x * -1 + (containerWidth/2);
		visPanel.camera.panY = y * -1 + (containerHeight/2);
		visPanel.camera.zoom = zoom;
		visPanel.camera.zoomCenterX = x ;
		visPanel.camera.zoomCenterY = y;

		//Reset start so it doesn't jump
		visPanel.camera.startX = visPanel.camera.panX;
		visPanel.camera.startY = visPanel.camera.panY;
		visPanel.camera.update();
				
	}
		
});

Ext.define('OSF.component.VisualContainerPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.VisualContainerPanel',
	
	scrollable: false,
	dockedItems: [
		{
			xtype: 'toolbar',
			itemId: 'tools',
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
					},
					listeners: {
						change: function (cb, newValue, oldValue, opts) {
							var containerPanel = this.up('panel');							
							
							var findCB = containerPanel.getComponent('tools').getComponent('find');
							findCB.reset();
							
							containerPanel.visualPanel.viewType=newValue;
							containerPanel.visualPanel.reset();
							
						}
					}
				}, 
				{
					xtype: 'combo',
					itemId: 'find',
					fieldLabel: 'Find',
					valueField: 'key',
					displayField: 'name',
					typeAhead: true,
					editable: true,
					width: 400,
					store: {
						sorters: [
							new Ext.util.Sorter({
								property: 'name',
								direction: 'ASC'
							})
						]
					},
					listeners: {
						change: function (cb, newValue, oldValue, opts) {
							var containerPanel = this.up('panel');
							
							if (newValue) {
								var node = cb.getSelection().data;							
								containerPanel.visualPanel.zoomTo(node.positionX, node.positionY, 2);
							}
						}
					}
				},
				{
					xtype: 'tbfill'					
				},
				{
					text: 'Download',
					iconCls: 'fa fa-download',
					handler: function(){
						var containerPanel = this.up('panel');
						containerPanel.visualPanel.download();
					}
				},
				{
					xtype: 'tbseparator'
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
			completedInit: function(nodes) {
				var findCB = containerPanel.getComponent('tools').getComponent('find');
				findCB.getStore().setData(nodes);			
			}
		});
		
		containerPanel.add(containerPanel.visualPanel);
		
		Ext.defer(function(){
			containerPanel.updateLayout(true, true);
		}, 100);
		
		
		containerPanel.on('resize', function(container, width, height, oldWidth, oldHeight, opts){
			containerPanel.visualPanel.setWidth(containerPanel.getWidth());
			containerPanel.visualPanel.setHeight(containerPanel.getHeight()-40);
		});
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