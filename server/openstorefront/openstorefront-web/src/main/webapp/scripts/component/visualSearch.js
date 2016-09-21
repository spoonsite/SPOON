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
	customActions: [],
	
	/**
	 * RELATION, TAGS, ORG, ATT
	 */
	viewType: 'RELATION', 
	listeners: {
		element: 'element',
		scope: 'this',
		mousedown: 'spritemousedown',
		mousemove: 'spritemousemove',
		mouseup: 'spritemouseup',
		mouseleave: 'spritemouseup',
		mousewheel: 'zoom',
		DOMMouseScroll: 'zoom'
	},
	
	onMouseDown: function (e) {
		console.log(e);
	},	
		
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
			},
			log: function() {
				console.log('--------->');
				console.log(this);
				console.log('<---------');
			}
		};
		
		var menuItems = [
			{
				text: 'Expand',
				iconCls: 'fa fa-expand',
				handler: function(){
					visPanel.loadNextLevel(visPanel.actionMenu.eventContext.key, 
									visPanel.actionMenu.eventContext.type, 
									visPanel.actionMenu.eventContext.name
					);
				}
			},
			{
				text: 'Collapse',
				iconCls: 'fa fa-compress',
				handler: function(){
					var sprite = visPanel.actionMenu.eventContext.sprite;
					var nodesKeysToRemove = [];
					Ext.Array.each(sprite.node.edges, function(edge){
						if (edge.targetKey !== visPanel.actionMenu.eventContext.key) {
							nodesKeysToRemove.push(edge.targetKey);
						}
					});
					visPanel.removeNodes(nodesKeysToRemove);
				}
			},
			{
				xtype: 'menuseparator'
			}
		];
		
				
		visPanel.actionMenu = Ext.create('Ext.menu.Menu', {
			items: Ext.Array.merge(menuItems,  visPanel.customActions)
		});
		
		var tip = Ext.create('Ext.tip.ToolTip', {
			dismissDelay: 2000,
			html: ''
		});
		
		visPanel.on('spritemouseout', function(item, event, eOpts){
			var sprite = item && item.sprite;	
			if (sprite.node) {
				visPanel.setStyle({
					cursor: 'default'
				});
				
				try{
					sprite.setAttributes({ 
						fillStyle: sprite.originalFill
					});
					sprite.getSurface().renderFrame();	
				} catch (e) {					
				}	
			}
		});		
		
		visPanel.on('spritemouseover', function(item, event, opts){
			var sprite = item && item.sprite;
			if (sprite.node) {
				//console.log("Name: " + (sprite.node.name ? sprite.node.name : sprite.node.targetName) + " xy: " + sprite.x + ', ' + sprite.y);
				
				var type = sprite.node.type ? sprite.node.type : sprite.node.targetType;
				if (type === 'component') {
					type = 'Entry';
				}
				if (type === 'attribute') {
					type = 'Attribute/Vital';
				}
				var tipXY = event.xy;
				tipXY[0] += 20;
				tipXY[1] += 20;
				
				tip.update(type);
				tip.showAt(tipXY);
				
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
			
			if (sprite.node && !sprite.nodeText) {
				
				
				
				var key  = sprite.node.key ? sprite.node.key : sprite.node.targetKey;
				var type = sprite.node.type ? sprite.node.type : sprite.node.targetType;
				var name = sprite.node.name ? sprite.node.name : sprite.node.targetName;
				visPanel.actionMenu.eventContext= {
					sprite: sprite,
					key: key,
					type: type,
					name: name
				};
				visPanel.actionMenu.showAt(event.xy);
				
				
			} else if (sprite.node && sprite.nodeText) {
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
						'<h1>{name}</h1><i>{componentTypeLabel}</i>',
						'<tpl if="badgeUrl"><img src="{badgeUrl}" title="{codeLabel}" width="40" /></tpl>',
						'<hr>',
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
						url: 'api/v1/resource/components/' + (sprite.node.key ? sprite.node.key : sprite.node.targetKey) + '/detail',
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
						url: 'api/v1/resource/organizations/' + (sprite.node.key ? sprite.node.key : sprite.node.targetKey),
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
					
					var key = (sprite.node.key ? sprite.node.key : sprite.node.targetKey);
					
					var url;
					descriptionWindow.setLoading(true);
					var attributeType = false;
					if (key.indexOf('#') !== -1) {
						var keySplit = key.split('#');
						url = 'api/v1/resource/attributes/attributetypes/' + keySplit[0] + '/' + keySplit[1] +'/detail';						
					} else {
						url = 'api/v1/resource/attributes/attributetypes/' + key;
						attributeType = true;
					}
					
					
					Ext.Ajax.request({
						url: url,
						callback: function(){
							descriptionWindow.setLoading(false);
						},
						success: function(response, opts) {
							var data = Ext.decode(response.responseText);
							
							if (attributeType) {
								data.componentTypeLabel = 'Attribute Type';
								data.name = data.description;
								data.description = data.detailedDescription;
							} else {
								data.componentTypeLabel = 'Attribute Code';
								data.name = data.typeLabel + " - " + data.codeLabel;
								data.description = data.codeDetailDescription;
							}
							descriptionWindow.update(data);
						}
					});					
					
				} else {
					descriptionWindow.close();
				}
			}
		});

		visPanel.spritemousedown = function(event, sprite, opts){
			visPanel.camera.pan = true;		
			visPanel.camera.panOriginX = event.pageX;						
			visPanel.camera.panOriginY = event.pageY;
		};	
		
		visPanel.on('spritemousedown', visPanel.spritemousedown);

		visPanel.spritemouseup = function(event, sprite, opts){
			visPanel.camera.pan = false;	
			visPanel.camera.startX = visPanel.camera.panX;
			visPanel.camera.startY = visPanel.camera.panY;						
		};		
		visPanel.on('spritemouseup', visPanel.spritemouseup);


		visPanel.spritemousemove = function(event, item, opts){
			var sprite = item && item.sprite;			
			if (visPanel.camera.pan) {
				visPanel.camera.panX = visPanel.camera.startX + (event.pageX - visPanel.camera.panOriginX);						
				visPanel.camera.panY = visPanel.camera.startY + (event.pageY - visPanel.camera.panOriginY);	
				
				visPanel.camera.update();
			} 
		};
		
		visPanel.on('spritemousemove', visPanel.spritemousemove);
		
		
		visPanel.zoom = function (orgEvent) {
			// cross-browser wheel delta
			
			//e = window.event || e;
			var e = orgEvent.event;
			
			if (e.target.nodeName.toLowerCase() === 'canvas' || e.target.nodeName.toLowerCase() === 'svg') {
				var oldZoom = visPanel.camera.zoom;
				
				var delta = Math.max(-.25, Math.min(.25, (e.wheelDelta || -e.detail)));
				
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
					var offsets = visPanel.getOffsetsTo(Ext.getBody());
					
					var lastX = e.offsetX || (e.pageX - offsets[0]);
					var lastY = e.offsetY || (e.pageY - offsets[1]);
							
					//http://stackoverflow.com/questions/2916081/zoom-in-on-a-point-using-scale-and-translate
					var scaleChange = visPanel.camera.zoom - oldZoom;
					lastX = -(lastX * scaleChange);
					lastY = -(lastY * scaleChange);

					visPanel.camera.panX = visPanel.camera.panX + lastX;
					visPanel.camera.panY = visPanel.camera.panY + lastY;
					
					//Reset start so it doesn't jump
					visPanel.camera.startX = visPanel.camera.panX;
					visPanel.camera.startY = visPanel.camera.panY;					
					
					visPanel.camera.update();	
				}			
				
				e.preventDefault();
			}			
		};

//		if (window.addEventListener) {
//			window.addEventListener("mousewheel", zoom, false);
//			window.addEventListener("DOMMouseScroll", zoom, false);
//			//window.addEventListener("mousemove", mouseMove, false);
//		} else {
//			window.attachEvent("onmousewheel", zoom);
//			//window.attachEvent("onmousemove", mouseMove);
//			//window.attachEvent("onmouseleave", mouseMove);
//		}
		
		
		
		
//		window.oncontextmenu = function (e){
//			return false;
//		};
		
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
		if (!Ext.isIE) {
			visPanel.update();
		}
		
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
		if (visPanel.viewType) {
			visPanel.loadRelationships();
		}
	},
	
	loadNextLevel: function(key, entityType, nodeName) {
		var visPanel = this;
		
		visPanel.setLoading("Loading Relationships for " + nodeName + "...");
		Ext.Ajax.request({
			url: 'api/v1/service/relationship?key=' + key.replace('#', '~') + '&entityType=' + entityType,
			callback: function(){
				visPanel.setLoading(false);
			},
			success: function(response, opts) {
				var data = Ext.decode(response.responseText);
				
				var viewData = [];
				Ext.Array.each(data, function(relationship){
					viewData.push({
						type: relationship.entityType,					
						key: relationship.key,
						label: relationship.name,
						relationshipLabel: relationship.relationshipLabel,
						targetKey: relationship.targetKey,
						targetName: relationship.targetName,
						targetType: relationship.targetEntityType
					});
				});
				
				visPanel.addViewData(viewData);
			}
		});		
	},
	
	addViewData: function(newViewData) {
		var visPanel = this;
		
		//de-dup relationships
		Ext.Array.each(newViewData, function(newRelationship){
			var containRelation = false;
			Ext.Array.each(visPanel.viewData, function(existing){
				if (newRelationship.key === existing.key && 
				    newRelationship.targetKey === existing.targetKey &&
				    newRelationship.relationshipLabel === existing.relationshipLabel) {
					containRelation = true;
				}
			});
			if (containRelation === false) {
				visPanel.viewData.push(newRelationship);
			}	
		});
		
		visPanel.initVisual(visPanel.viewData);
	},
	
	removeNodes: function(nodeKeysToRemove) {
		var visPanel = this;
	
		var keepArray = [];
		var addParent = true;
		Ext.Array.each(visPanel.viewData, function(existingNode){
			var found = Ext.Array.findBy(nodeKeysToRemove, function(nodeToRemove) {
				if (existingNode.targetKey === nodeToRemove) {
					return true;
				} else {
					return false;
				}
			});
			if (!found) {
				keepArray.push(existingNode);
			} else {
				//keep parent 
				if (addParent) {
					addParent = false;
					keepArray.push({
						key: existingNode.key,
						label: existingNode.label,
						type: existingNode.type
					});
				}

			}
		});
		visPanel.viewData = keepArray;
		visPanel.initVisual(visPanel.viewData);
	},
	
	loadRelationships: function() {
		var visPanel = this;
		
		visPanel.setLoading("Loading Initial Relationships...");
		Ext.Ajax.request({
			url: 'api/v1/resource/componentrelationship',
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
			url: 'api/v1/resource/components/tagviews?approvedOnly=true',
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
	
	loadOrganizations: function(organizationId) {
		var visPanel = this;
		
		var organizationLoad = function(organizationId) {
			visPanel.setLoading("Loading Organizations...");
			Ext.Ajax.request({
				url: 'api/v1/resource/organizations/componentrelationships?organizationId=' + organizationId,
				callback: function(){
					visPanel.setLoading(false);
				},
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);
					
					if (data.length === 0) {
						Ext.toast('No entries found for organization.');
					} else {
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
				}
			});	
		};
		
		if (organizationId) {
			organizationLoad(organizationId);
		} else {
			//prompt for type to display
			var prompt = Ext.create('Ext.window.Window', {
				title: 'Select Organization to View',
				modal: true,
				closeMode: 'destory',
				width: 500,			
				height: 150,
				bodyStyle: 'padding: 10px;',
				layout: 'anchor',
				items: [
					{
						xtype: 'combo',
						fieldLabel: 'Organization',
						itemId: 'organization',
						labelAlign: 'top',
						valueField: 'code',
						width: '100%', 
						displayField: 'description',
						typeAhead: true,
						editable: true,
						allowBlank: false,
						queryMode: 'remote',
						store: {
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/organizations/lookup?approvedComponentsOnly=true'								
							}
						}
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								xtype: 'tbfill'
							},
							{
								text: 'Show',
								iconCls: 'fa fa-check',
								handler: function(){
									var promptWindow = this.up('window');
									var orgCb = promptWindow.getComponent('organization');
									if (orgCb.getValue()) {
										organizationLoad(orgCb.getValue());
										visPanel.updateAttribute(orgCb.getValue());
										promptWindow.close();										
									} else {
										Ext.Msg.show({
											title:'Validation Failed',
											message: 'Select an Organization to show.',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR,
											fn: function(btn) {
											}
										});
									}
								}
							},
							{
								xtype: 'tbfill'
							}						
						]
					}
				]
			});
			prompt.show();					
		}
	},
	
	loadAttributes: function(attributeType) {
		var visPanel = this;
		
		var attributeLoad = function(attributeType) {
			visPanel.setLoading("Loading Attributes...");
			Ext.Ajax.request({
				url: 'api/v1/resource/attributes/relationships?attributeType=' + attributeType,
				callback: function(){
					visPanel.setLoading(false);
				},
				success: function(response, opts) {
					var data = Ext.decode(response.responseText);

					var viewData = [];
					Ext.Array.each(data, function(attributeRelationship){
						viewData.push({
							type: 'attribute',
							nodeId: attributeRelationship.key,
							key: attributeRelationship.targetKey,
							label: attributeRelationship.targetName,
							relationshipLabel: attributeRelationship.relationshipLabel,
							targetKey: attributeRelationship.key,
							targetName: attributeRelationship.name,
							targetType: 'attribute'
						});
					});

					visPanel.viewData = visPanel.viewData.concat(viewData);
					visPanel.initVisual(visPanel.viewData);
				}
			});			
		};
		
		
		if (attributeType) {
			attributeLoad(attributeType);
		} else {
			//prompt for type to display
			var prompt = Ext.create('Ext.window.Window', {
				title: 'Select Attribute/Vital to View',
				modal: true,
				closeMode: 'destory',
				width: 400,			
				height: 150,
				bodyStyle: 'padding: 10px;',
				layout: 'anchor',
				items: [
					{
						xtype: 'combo',
						fieldLabel: 'Attribute',
						itemId: 'attributeType',
						labelAlign: 'top',
						valueField: 'attributeType',
						width: '100%', 
						displayField: 'description',
						typeAhead: true,
						editable: true,
						allowBlank: false,
						queryMode: 'remote',
						store: {
							proxy: {
								type: 'ajax',
								url: 'api/v1/resource/attributes/attributetypes',
								reader: {
									type: 'json',
									rootProperty: 'data'
								}
							}
						}
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						items: [
							{
								xtype: 'tbfill'
							},
							{
								text: 'Show',
								iconCls: 'fa fa-check',
								handler: function(){
									var promptWindow = this.up('window');
									var attributeTypeCb = promptWindow.getComponent('attributeType');
									if (attributeTypeCb.getValue()) {
										attributeLoad(attributeTypeCb.getValue());
										visPanel.updateAttribute(attributeTypeCb.getValue());
										promptWindow.close();										
									} else {
										Ext.Msg.show({
											title:'Validation Failed',
											message: 'Select an attribute to show.',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR,
											fn: function(btn) {
											}
										});
									}
								}
							},
							{
								xtype: 'tbfill'
							}						
						]
					}
				]
			});
			prompt.show();				
		}		
	},
	
	updateVisual: function(relationShipData) {
		var visPanel = this;
		visPanel.viewType = null;
		visPanel.reset();
		visPanel.viewData = relationShipData;
		visPanel.initVisual(visPanel.viewData);		
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
			if (node.targetKey &&  !nodeKeys[node.targetKey]) {
				nodes.push({
					key: node.targetKey,
					name: node.targetName,
					type: node.targetType,
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
			
			//check for edge
			Ext.Array.each(nodes, function(node) {
				var newEdges = [];
				Ext.Array.each(node.edges, function(edge) {
					if (edge.targetKey !== relationship.key) {
						newEdges.push(edge);
					}	
				});
				node.edges = newEdges; 
			});
			
			
			var ownerNode = Ext.Array.findBy(nodes, function(node) {
				if (node.key === relationship.key) {
					return true;
				} else {
					return false;
				}
			});
			if (targetNode) {
				ownerNode.edges.push({
					targetKey: relationship.targetKey,
					ownerKey: relationship.key,
					relationshipLabel: relationship.relationshipLabel,
					name: targetNode.name,
					type: relationship.targetType
				});
			}
		});
		
		
		
		var mainSprites = [];
		var sprites = [];
		
				
		var containerHeight = visPanel.getHeight();
		var containerWidth = visPanel.getWidth();		
		
		//grid and camera
		var cameraSprite = {
			backgroundSprite: true,
			type: 'rect',
			x: 0,
			y: 0,
			width: containerWidth,
			height: containerHeight,
			fillStyle: 'rgba(0, 0, 0, 0)'
		};
		
		for(var i=30; i<=containerWidth; i=i+30) {
			
			var opacity = .1;
			if ((i/30) % 5 === 0) {
				opacity = .3;
			}
			
			mainSprites.push({
				backgroundSprite: true,
				type: 'line',
				fromX: i,
				fromY: 0,
				toX: i,
				toY: containerHeight,
				lineWidth: 1,
				strokeStyle: 'rgba(255, 255, 255, ' +opacity + ')'
			});
		}
		
		for(var i=30; i<=containerHeight; i=i+30) {
			
			var opacity = .1;
			if ((i/30) % 5 === 0) {
				opacity = .3;
			}
			
			mainSprites.push({
				backgroundSprite: true,
				type: 'line',
				fromX: 0,
				fromY: i,
				toX: containerWidth,
				toY: i,
				lineWidth: 1,
				strokeStyle: 'rgba(255, 255, 255, ' +opacity + ')'
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
		
		var attributeNode={
			type: 'square',
			size: nodeRadius, 
			fillStyle: 'brown',
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
		
		//hubs, nodes 
		var hubs = [];		
		Ext.Array.each(nodes, function(node) {
			
			if (!renderNodes[node.key]) {

				var hub = {
					id: Ext.id({}, 'hub-gen'),
					spriteConfigs: [],
					edgeHubs: [],
					containsNode: function(key) {
						var me = this;
						var contains = false;
						Ext.Array.each(me.spriteConfigs, function(n) {
							if (n.node && !n.nodeText) {
								var nodeKey = n.node.key ? n.node.key : n.node.targetKey;
								if (nodeKey === key) {
									contains = true;
								}
							}
						});
						return contains;
					},
					addNode: function(spriteConfig) {
						var me = this;
						me.spriteConfigs.push(spriteConfig);						
						me.updateBBox();
					},
					updateBBox:  function (){
						var me = this;
						me.bbox = null;
						
						Ext.Array.each(me.spriteConfigs, function(n) {								
							var buffer = n.r ? n.r : n.size ? n.size : 0;
							var nxLeft = n.x - buffer;
							var nxRight = n.x + buffer;
							var nyTop = n.y - buffer;
							var nyBottom = n.y + buffer;

							if (!me.bbox) {
								me.bbox = {
									minX: nxLeft,
									minY: nyTop,
									maxX: nxRight,
									maxY: nyBottom,
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
										return Math.abs(thisBBox.minX -  bbox.minX) * 2 < ((thisBBox.maxX - thisBBox.minX) + (bbox.maxX - bbox.minX)) &&
											  Math.abs(thisBBox.minY -  bbox.minY) * 2 < ((thisBBox.maxY - thisBBox.minY) + (bbox.maxY - bbox.minY));										
									},
									compare: function(bbox) {
										var thisBBox = this;
										if (thisBBox.minX === bbox.minX &&
											thisBBox.minY === bbox.minY) {										
											return 0;
										} else if (thisBBox.minX < bbox.minX ||
											thisBBox.minY < bbox.minY) {	
											return 1;											
										} else if (thisBBox.minX > bbox.minX ||
											thisBBox.minY > bbox.minY ) {	
											return -1;
										} 										
									}
								};
							} else {						
								if (nxLeft< me.bbox.minX) {
									me.bbox.minX = nxLeft;
								} else  if (nxRight > me.bbox.maxX) {
									me.bbox.maxX = nxRight;
								}

								if (nyTop < me.bbox.minY) {
									me.bbox.minY = nyTop;
								} else if (nyBottom > me.bbox.maxY) {
									me.bbox.maxY = nyBottom;
								}							
							}
						});
						me.bbox.minX -= 10;
						me.bbox.minY -= 2;
						me.bbox.maxX += 10;
						me.bbox.maxY += 10;						
					},
					translateHub: function(newHubTopX, newHubTopY) {
						var me = this;
						
						var translateX = newHubTopX - me.bbox.minX;
						var translateY = newHubTopY - me.bbox.minY;
						
						Ext.Array.each(me.spriteConfigs, function(n) {
							n.x += translateX;
							n.y += translateY;
							if (n.node && !n.nodeText) {
								n.node.positionX = n.x ;
								n.node.positionY = n.y ;
							}
							if (n.targetNode && !n.nodeText) {
								n.targetNode.positionX = n.x ;
								n.targetNode.positionY = n.y ;
							}							
						});
						
						me.updateBBox();
					}
				};
				

				var setNodePosition = true;
//				if (node.edges.length > 0) {
//					var edgeNode =  Ext.Array.findBy(nodes, function(item) {
//						if (item.key === node.edges[0].targetKey) {
//							return true;
//						} else {
//							return false;
//						}
//					});
//					
//					if (edgeNode.positionX && edgeNode.positionY) {
//						node.positionX = edgeNode.positionX - (componentNode.r  * 25 - nodeRadius);
//						node.positionY = edgeNode.positionY;
//						setNodePosition = false;
//						
//						//find hub that contains edgeNode
//						var hubWithNode;
//						 Ext.Array.each(hubs, function(h) {
//							if (h.containsNode(edgeNode.key)) {
//								hubWithNode = h;
//							} else {
//								var hubFound = Ext.Array.findBy(h.edgeHubs, function(edgeHub) {
//									return edgeHub.containsNode(edgeNode.key);
//								});
//								if (hubFound) {
//									hubWithNode = hubFound;
//								}
//							}
//						});
//						if (hubWithNode) {
//							hubWithNode.edgeHubs.push(hub);
//						}
//					}
//				} 
				
				if (setNodePosition) {
					node.positionX = startX + (componentNode.r *6) + 40;
					node.positionY = startY;
					hubs.push(hub);
				}
				
				var hubNodeRadius = nodeRadius + (node.edges.length);
				
				var baseNode =componentNode; 
				if (node.type === 'tag') {
					baseNode = tagNode; 
				}
				if (node.type === 'organization') {
					baseNode = organizationNode; 
				}	
				if (node.type === 'attribute') {
					baseNode = attributeNode; 
				}
				node.nodeSize = hubNodeRadius;
				
				
				var nodeSprite = Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY,
					node: node,
					r: node.nodeSize,
					size: node.nodeSize
					//fillStyle: hubFillStyle
				}, baseNode);
				sprites.push(nodeSprite);
				hub.addNode(nodeSprite);
				
				var nodeTextSprite = Ext.apply({}, {
					x:  node.positionX,
					y:  node.positionY + hubNodeRadius + 15,
					text: Ext.util.Format.ellipsis(node.name, 20),
					node: node,
					nodeText: true
				}, textNode);				
				sprites.push(nodeTextSprite);				
				hub.addNode(nodeTextSprite);				
			
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
						} else if (edgeNode.type === 'attribute') {
							baseNode = attributeNode; 
							targetNode.nodeSize = baseNode.size;
						} else if (edgeNode.type === 'organization') {
							baseNode = organizationNode; 
							targetNode.nodeSize = baseNode.size;
						} else { 
							targetNode.nodeSize = baseNode.r;
						}
						
						var targetNodeSprite = Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY,
							node: edgeNode,
							targetNode: targetNode
						}, baseNode);						
						sprites.push(targetNodeSprite);
						hub.addNode(targetNodeSprite);

						var targetNodeTextSprite = Ext.apply({}, {
							x:  targetNode.positionX,
							y:  targetNode.positionY + nodeRadius + 15,
							text: Ext.util.Format.ellipsis(targetNode.name, 20),
							node: edgeNode,
							targetNode: targetNode,
							nodeText: true
						}, textNode);						
						sprites.push(targetNodeTextSprite);	
						hub.addNode(targetNodeTextSprite);
						
						if ((rotation + 45) >= 360) {
							generation++;
							rotation = 0;
						} 
						rotation +=  (45 / generation); 
						
						
						renderNodes[edgeNode.targetKey] = true;
					}
				});
			
				var maxX;
				Ext.Array.each(hubs, function(h) {
					if (!maxX) {
						maxX = h.bbox.maxX;
					} else if (h.bbox.maxX > maxX) {
						maxX = h.bbox.maxX;
					}					
					Ext.Array.each(h.edgehubs, function(edgeHub) {
						if (!maxX) {
							maxX = edgeHub.bbox.maxX;
						} else if (edgeHub.bbox.maxX > maxX) {
							maxX = edgeHub.bbox.maxX;
						}
					});
				});
				startX = maxX;

				renderNodes[node.key] = true;
			}
		});
		
		//Layout hubs so they don't over lap 
		var layoutGeneration = 1;
		var firstHub = true;
		var hubPositionRotation = 0;		
		var generationMinX;
		var generationMinY;
		var minXOfGeneration;
		var perviousHub;
		var containerCenterX = containerWidth / 2;
		var containerCenterY = containerHeight / 2;		
		var spreadX = 100;
		var spreadY = 50;
		
		var allHubsWithEgdes= [];
		Ext.Array.each(hubs, function(h) {
			
			allHubsWithEgdes.push(h);
			Ext.Array.each(h.edgeHubs, function(edgeHub) {
				allHubsWithEgdes.push(edgeHub);
			});
			
		});
		
		//move into position
		Ext.Array.each(hubs, function(h) {
			
			if (firstHub) {
				//center hub on container				
				var transX = containerCenterX - (h.bbox.maxX - h.bbox.minX) / 2;
				var transY = containerCenterY - (h.bbox.maxY - h.bbox.minY) / 2;
				

				h.translateHub(transX, transY);

//				sprites.push({
//					type: 'rect',
//					x: h.bbox.minX,
//					y: h.bbox.minY,
//					width: h.bbox.maxX - h.bbox.minX,
//					height: h.bbox.maxY - h.bbox.minY,
//					strokeStyle: 'yellow',
//					lineWidth: 1, 
//					fillOpacity: 0
//				});

//				sprites.push({
//					type: 'text',
//					x: h.bbox.minX,
//					y: h.bbox.minY,					
//					strokeStyle: 'yellow',
//					text: h.id,
//					lineWidth: 1, 
//					fillOpacity: 1
//				});
//				
				generationMinX = h.bbox.minX;
				generationMinY = h.bbox.minY;
				minXOfGeneration = h.bbox.minX;
				
				firstHub = false;
			} else {
				var transX = generationMinX - ((h.bbox.maxX - h.bbox.minX) + spreadX);
				var transY = generationMinY;
				
				var point = new Ext.draw.Point(transX, transY);
				point = point.rotate(hubPositionRotation, new Ext.draw.Point(containerCenterX, containerCenterY));
				

				h.translateHub(point.x, point.y);
				
				
//				sprites.push({
//					type: 'text',
//					x: h.bbox.minX,
//					y: h.bbox.minY,					
//					strokeStyle: 'yellow',
//					text: h.id,
//					lineWidth: 1, 
//					fillOpacity: 1
//				});				
				
//				sprites.push({
//					type: 'rect',
//					x: h.bbox.minX,
//					y: h.bbox.minY,
//					width: h.bbox.maxX - h.bbox.minX,
//					height: h.bbox.maxY - h.bbox.minY,
//					strokeStyle: 'yellow',
//					lineWidth: 1, 
//					fillOpacity: 0
//				});					
				
				
			}
			perviousHub = h;
						
			Ext.Array.each(h.edgeHubs, function(edgeHub) {
				
				var transX = generationMinX - ((edgeHub.bbox.maxX - edgeHub.bbox.minX) + spreadX);
				var transY = generationMinY;
				
				var point = new Ext.draw.Point(transX, transY);
				point = point.rotate(hubPositionRotation, new Ext.draw.Point(containerCenterX, containerCenterY));
				
				edgeHub.translateHub(point.x, point.y);
						
//				sprites.push({
//					type: 'text',
//					x: edgeHub.bbox.minX,
//					y: edgeHub.bbox.minY,					
//					strokeStyle: 'yellow',
//					text: edgeHub.id,
//					lineWidth: 1, 
//					fillOpacity: 1
//				});						
						
//				sprites.push({
//					type: 'rect',
//					x: edgeHub.bbox.minX,
//					y: edgeHub.bbox.minY,
//					width: edgeHub.bbox.maxX - edgeHub.bbox.minX,
//					height: edgeHub.bbox.maxY - edgeHub.bbox.minY,
//					strokeStyle: 'red',
//					lineWidth: 1, 
//					fillOpacity: 0
//				});							
//								
				perviousHub = h;	
				if (edgeHub.bbox.minX  < minXOfGeneration) {
					minXOfGeneration = edgeHub.bbox.minX;
				}
				
				hubPositionRotation += 45; 			
				if (hubPositionRotation % 360 === 0) {
					layoutGeneration++;				
					generationMinX = minXOfGeneration;
				}	
			});		
					
			
			if (h.bbox.minX  < minXOfGeneration) {
				minXOfGeneration = h.bbox.minX;
			}			
			
			hubPositionRotation += 45; 			
			if (hubPositionRotation % 360 === 0) {
				layoutGeneration++;	
				generationMinX = minXOfGeneration;
			}
		});		
		
		//fix overlaps
		var movedhub = true;
		var max = 2000;
		var moveCount = 0;
		while (movedhub && moveCount <= max) {
			movedhub = false;
			Ext.Array.each(allHubsWithEgdes, function (h) {

				Ext.Array.each(allHubsWithEgdes, function (otherHub) {
					if (otherHub.id !== h.id) {
						if (h.bbox.overlaps(otherHub.bbox)) {

							var newX = otherHub.bbox.minX;
							var newY = otherHub.bbox.minY;
							
							if (h.bbox.maxX > otherHub.bbox.minX) {
								newX = h.bbox.maxX + 1;
							} else if (h.bbox.minX > otherHub.bbox.minX) {
								newX = otherHub.bbox.maxX + 1;
							} 

							if (h.bbox.maxY > otherHub.bbox.minY) {
								newY = h.bbox.maxY + 1;
							} else if (h.bbox.minY > otherHub.bbox.minY) {
								newY = otherHub.bbox.maxY +1;
							}							
							
							if ( newX !== otherHub.bbox.minX && 
								newY !== otherHub.bbox.minY)
							{
								otherHub.translateHub(newX, newY);
							}
							movedhub = true;
							
							//console.log("It: " + moveCount + " Moved - " + (otherHub.id ) + ' to X: ' + newX + ' to Y: ' +newY + " In relation to: " + h.id);
						}
					}
				});

			});
			moveCount++;
		}
		
		
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
			
			if (targetNode) {
			
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

				//console.log(relationship.relationshipLabel + ' - ' + theta);
				if (theta > (Math.PI/2) && theta <= Math.PI ) {
					theta +=  Math.PI;
					xAdjust  = 20;
				}

				if (theta < (Math.PI/2 * -1) && theta >= Math.PI * -1 ) {
					theta +=  Math.PI;				
					xAdjust = -15;
				}			

				sprites.push(Ext.apply({}, {
					x:  (endX + ownerNode.positionX) /2 + xAdjust,
					y:  ownerNode.positionY + (endY - ownerNode.positionY)/ 2 - 10,
					text: Ext.util.Format.ellipsis(relationship.relationshipLabel, 20),
					rotationRads: theta
				}, relationshipText));				
			}
			
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
				fillStyle: {
				   type: 'radial',
				   start: {
					   x: 0,
					   y: 0,
					   r: 0
				   },
				   end: {
					   x: 0,
					   y: 0,
					   r: 1
				   },
				   stops: [{
					   offset: 0,
					   color: '#5586dc'
				   }, {
					   offset: 1,
					   color: '#1f3163'
				   }]
			   }
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
		
		//put sprite  back to 0,0 then move to center (Keep in mind the sprites position doesn't move it just has matrixes applied to it)
		visPanel.camera.panX = x * -1 + (containerWidth/2);
		visPanel.camera.panY = y * -1 + (containerHeight/2);
		visPanel.camera.zoom = zoom;
		visPanel.camera.zoomCenterX = x;
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
					labelAlign: 'right',
					valueField: 'code',
					displayField: 'description',
					typeAhead: false,
					editable: false,
					value: 'RELATION',
					store: {
						data: [
							{ code: 'RELATION', description: 'Entries' },
							{ code: 'ORG', description: 'Organization' },
							{ code: 'ATT', description: 'Attributes' },
							{ code: 'TAGS', description: 'Tags' }
						]
					},
					listeners: {
						change: function (cb, newValue, oldValue, opts) {
							var containerPanel = this.up('panel');							
							
							containerPanel.getComponent('tools').getComponent('attributeType').reset();
							if (newValue === 'ATT') {
								containerPanel.getComponent('tools').getComponent('attributeType').setHidden(false);
							} else {
								containerPanel.getComponent('tools').getComponent('attributeType').setHidden(true);
							}
							
							containerPanel.getComponent('tools').getComponent('organization').reset();
							if (newValue === 'ORG') {
								containerPanel.getComponent('tools').getComponent('organization').setHidden(false);
							} else {
								containerPanel.getComponent('tools').getComponent('organization').setHidden(true);
							}		
							
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
					labelAlign: 'right',
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
							
							if (newValue && cb.getSelection()) {
								var node = cb.getSelection().data;							
								containerPanel.visualPanel.zoomTo(node.positionX, node.positionY, 2);
							}
						}
					}
				},
				{
					xtype: 'combo',
					fieldLabel: 'Add Attribute',
					labelAlign: 'right',
					itemId: 'attributeType',
					hidden: true,				
					valueField: 'attributeType',
					width: 400, 
					displayField: 'description',
					typeAhead: true,
					editable: true,									
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/attributes/attributetypes',
							reader: {
								type: 'json',
								rootProperty: 'data'
							}
						}
					},
					listeners: {
						change: function (cb, newValue, oldValue, opts) {
							var containerPanel = this.up('panel');
							
							if (newValue) {
								containerPanel.visualPanel.loadAttributes(newValue);
							}
						}
					}					
				},
				{
					xtype: 'combo',
					fieldLabel: 'Add Organization',
					labelAlign: 'right',
					itemId: 'organization',
					hidden: true,				
					valueField: 'code',
					width: 425, 
					labelWidth: 150,
					displayField: 'description',
					typeAhead: true,
					editable: true,									
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/organizations/lookup?approvedComponentsOnly=true'							
						}
					},
					listeners: {
						change: function (cb, newValue, oldValue, opts) {
							var containerPanel = this.up('panel');
							
							if (newValue) {
								containerPanel.visualPanel.loadOrganizations(newValue);
							}
						}
					}					
				},				
				{
					xtype: 'tbfill'					
				},
				{
					text: 'Download Image',
					iconCls: 'fa fa-download',					
					handler: function(){
						var containerPanel = this.up('panel');
						var data = containerPanel.visualPanel.getImage('png');
						Ext.DomHelper.append(Ext.getBody(),
										"<form id='visual-download' method='POST' action='Media.action?DataImage'>" + 
										"<input type='hidden' name='imageData' value='" + data.data + "' /> " + 
										"<input type='hidden' name='imageType' value='" + data.type + "' /> ");
						var form = Ext.get("visual-download");	
						form.dom.submit();
						form.destroy();
						
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
						
						var findCB = containerPanel.getComponent('tools').getComponent('find');
						findCB.reset();
						
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
			},
			updateAttribute: function(attributeType) {
				containerPanel.getComponent('tools').getComponent('attributeType').setValue(attributeType);
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