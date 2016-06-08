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
	style: 'cursor: pointer',
	bodyStyle: 'background: #2d2c2c;',
		

		
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
		}
	
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

		visPanel.on('spritemousemove', function(sprite, event, opts){
			if (visPanel.camera.pan) {
				visPanel.camera.panX = visPanel.camera.startX + (event.pageX - visPanel.camera.panOriginX);						
				visPanel.camera.panY = visPanel.camera.startY + (event.pageY - visPanel.camera.panOriginY);	
				
				visPanel.graphComposite.setAttributes({
					translationX: visPanel.camera.panX,
					translationY: visPanel.camera.panY					
				});
				visPanel.getSurface().renderFrame();
			}
		});
		
		var doScroll = function (e) {
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
					
					visPanel.graphComposite.setAttributes({
						scaleX:  visPanel.camera.zoom,
						scaleY:  visPanel.camera.zoom,
						scaleCenterX: visPanel.camera.zoomCenterX,
						scaleCenterY: visPanel.camera.zoomCenterY						
					});
					visPanel.getSurface().renderFrame();	
				}
				//console.log(e);
				
				e.preventDefault();
			}			
		};

		if (window.addEventListener) {
			window.addEventListener("mousewheel", doScroll, false);
			window.addEventListener("DOMMouseScroll", doScroll, false);
		} else {
			window.attachEvent("onmousewheel", doScroll);
		}	
		
	},
	
	afterRender: function() {
		this.callParent();
		
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
				
				visPanel.drawVisual(viewData);
				visPanel.viewData = viewData;
			}
		});		
		
	},
	
	drawVisual: function(viewData) {
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
					egdes: []
				});				
				nodeKeys[node.key] = true;
			}
			if (!nodeKeys[node.targetKey]) {
				nodes.push({
					key: node.targetKey,
					name: node.targetName,
					type: node.type,
					egdes: []	
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
			targetNode.egdes.push({
				targetKey: relationship.targetKey,
				ownerKey: relationship.key,
				relationshipLabel: relationship.relationshipLabel,
				ownerNode: ownerNode
			});
		});
		
		
		
		var sprites = [];
				
		var containerHeight = visPanel.getHeight();
		var containerWidth = visPanel.getWidth();		
		
		
		var cameraSprite = {
			type: 'rect',
			x: 0,
			y: 0,
			width: containerWidth,
			height: containerHeight,
			fillStyle: '#2d2c2c'
		};
		
		sprites.push(cameraSprite);
		
		visPanel.graphComposite = Ext.create('Ext.draw.sprite.Composite', {	
			translationX: visPanel.camera.panX,
			translationY: visPanel.camera.panY,
			scaleX:  visPanel.camera.zoom,
			scaleY:  visPanel.camera.zoom,
			scaleCenterX: visPanel.camera.zoomCenterX,
			scaleCenterY: visPanel.camera.zoomCenterY
		});
		

		var startX = 20;
		var nodeRadius = 20;
		var renderNodes = {};
		Ext.Array.each(nodes, function(node) {
			
			if (!renderNodes[node.key]) {
			
				var componentNode = {
					type: 'circle',
					r: nodeRadius, 
					fillStyle: 'orange',
					lineWidth: 3,
					strokeStyle: '#FFFFFF',
					zIndex: 99
				};

				node.positionX = startX + componentNode.r *6 + 40;
				node.positionY = containerHeight /2;
				
				visPanel.graphComposite.add(Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY
				}, componentNode));

			
				var rotation = 0;
				Ext.Array.each(node.egdes, function(edgeNode) {
					
					edgeNode.ownerNode.positionX = node.positionX;
					edgeNode.ownerNode.positionY = node.positionY - componentNode.r * 5;
					edgeNode.ownerNode.rotationRads = rotation;
					
					visPanel.graphComposite.add(Ext.apply({}, {
						x:  edgeNode.ownerNode.positionX,
						y:  edgeNode.ownerNode.positionY,
						rotationCenterX: node.positionX,
						rotationCenterY: node.positionY,
						rotationRads: edgeNode.ownerNode.rotationRads
					}, componentNode));	
					
					rotation += 0.785398; 
					renderNodes[edgeNode.ownerKey] = true;
				});
			
				startX += componentNode.r  * 10 + nodeRadius;
				
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
			
			var relationshipEdge = {
				type: 'line',
				lineWidth: 3,
				strokeStyle: 'lightgrey',
				zIndex: 0
			};
		
			visPanel.graphComposite.add(Ext.apply({}, {
				fromX: ownerNode.positionX,
				fromY: ownerNode.positionY,
				toX: targetNode.positionX,					
				toY: targetNode.positionY
			}, relationshipEdge));				

			
		});
		
		
		
		sprites.push(visPanel.graphComposite);
		
		visPanel.setSprites(sprites);
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
					fieldLabel: 'Type',
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