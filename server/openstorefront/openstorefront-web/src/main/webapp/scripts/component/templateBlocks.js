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

/* global Ext, MediaViewer */

Ext.define('OSF.component.template.BaseBlock', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.BaseBlock',
	
	updateHandler: function(entry){
		return entry;
	},
		
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var block = this;
		var data = block.updateHandler(entry);
		if (data) {
			block.update(data);			
		}
	}
	
});

Ext.define('OSF.component.template.Description', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Description',	
	
	showDescriptionHeader: true,
	tpl: new Ext.XTemplate(
		'<div><tpl if="showDescriptionHeader"><h2><tpl if="componentSecurityMarkingType">({componentSecurityMarkingType}) </tpl>Description</h2></tpl>',	
		'	{description}',
		'</div>'		
	),
		
	initComponent: function () {
		this.callParent();				
	},
	
	updateHandler: function(entry){
		entry.description = Ext.util.Format.escape(entry.description).replace(/\n/g, '').replace(/\r/g, '');		
				
		// Perform client-side processing of the description, consisting of two things:
		// Add the onclick handler for saved search links.
		// Set target=_blank for all links.
		var dom = Ext.dom.Helper.createDom("<div><div>");	
		dom.innerHTML = entry.description;		
		var element = Ext.dom.Element.get(dom);
		var links = element.query('a',false);
		if (links && links.length > 0) {

			// Set targets
			Ext.Array.each(links, function(item){ 
				if (item.getAttribute('href')) {
					item.set({target: '_blank'});
				}
			});

			// Set up onclick handlers for saved search links
			Ext.Array.each(links, function(item){
				if (item.getAttribute('href') && item.getAttribute('href').indexOf('searchResults.jsp') !== -1) {
					var searchId = item.getAttribute('href').substr(item.getAttribute('href').indexOf('savedSearchId='), item.getAttribute('href').length);
					searchId = searchId.replace('savedSearchId=', '');
					item.set({'onclick': "CoreUtil.showSavedSearchWindow('" + searchId + "'); return false;"});
				}	
			});
		}
		entry.description  = dom.innerHTML;
				
		entry.showDescriptionHeader = this.showDescriptionHeader;
		return entry;
	}
	
});

Ext.define('OSF.component.template.Resources', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Resources',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Location of Entry Artifacts',
	
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',	
		'	<tr><th class="details-table">Name</th><th class="details-table">Link</th></tr>',
		'	<tpl for="resources">',	
		'		<tr class="details-table">',
		'			<td class="details-table"><b>{resourceTypeDesc}</b>',
		'                       <tpl if="description"><br>{description}</tpl>',
		'                       </td>',
		'			<td class="details-table"><tpl if="securityMarkingType">({securityMarkingType}) </tpl><a href="{actualLink}" class="details-table" target="_blank">{link}</a></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'		
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.resources || entry.resources.length === 0) {
			this.setHidden(true);
		} else {
			Ext.Array.sort(entry.resources, function(a, b){
				return a.resourceTypeDesc.localeCompare(b.resourceTypeDesc);				
			});	
		
			var updated = false;
			Ext.Array.each(entry.resources, function(resource){				
				if (resource.originalFileName) {
					resource.link = resource.originalFileName;
				}
				if (resource.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}				
		}
		
		return entry;
	}	
	
});

Ext.define('OSF.component.template.Contacts', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Contacts',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Points of Contact',
	
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',	
		'	<tr><th class="details-table">Name</th><th class="details-table">Position</th><th class="details-table">Phone</th><th class="details-table">Email</th></tr>',
		'	<tpl for="contacts">',	
		'		<tr class="details-table">',
		'			<td class="details-table"><tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{name}</b> <br> ({organization})</td>',
		'			<td class="details-table">{positionDescription}</td>',
		'			<td class="details-table"><tpl if="phone">{phone}</tpl><tpl if="!phone">—</tpl></td>',
		'			<td class="details-table"><a href="mailto:{email}" class="details-table">{email}</a></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.contacts || entry.contacts.length === 0) {
			this.setHidden(true);
		} else {
			Ext.Array.each(entry.contacts, function(contact){
				if (!contact.phone){
					contact.phone = null;
				}
			});
		
			Ext.Array.sort(entry.contacts, function(a, b){
				return a.name.localeCompare(b.name);	
			});		

			var updated = false;
			Ext.Array.each(entry.contacts, function(item){
				if (item.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}				
			
		}		
		return entry;
	}	
	
});

Ext.define('OSF.component.template.Vitals', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Vitals',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Entry Vitals',
	
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',			
		'	<tpl for="vitals">',	
		'		<tr class="details-table">',
		'			<td class="details-table"><b>{label}</b></td>',
		'			<td class="details-table highlight-{highlightStyle}"><tpl if="securityMarkingType">({securityMarkingType}) </tpl><a href="#" class="details-table" title="Show related entries" onclick="CoreUtil.showRelatedVitalWindow(\'{type}\',\'{code}\',\'{label} - {value}\', \'{vitalType}\', \'{tip}\', \'{componentId}\', \'{codeHasAttachment}\');">{value}</a><tpl if="codeHasAttachment"> <a href="api/v1/resource/attributes/attributetypes/{type}/attributecodes/{code}/attachment"><i class="fa fa-paperclip"></i> </a></tpl></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'		
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if ((!entry.attributes || entry.attributes.length === 0) && 
				(!entry.metadata || entry.metadata.length === 0)) {
			this.setHidden(true);
		}
		
		//normalize and sort
		var vitals = [];
		if (entry.attributes) {
			Ext.Array.each(entry.attributes, function(item){
				vitals.push({
					componentId: entry.componentId,
					label: item.typeDescription,
					value: item.codeDescription,
					highlightStyle: item.highlightStyle,
					type: item.type,
					code: item.code,
					updateDts: item.updateDts,
					securityMarkingType: item.securityMarkingType,
					codeHasAttachment: item.codeHasAttachment,
					vitalType: 'ATTRIBUTE',
					tip: item.codeLongDescription ? Ext.util.Format.escape(item.codeLongDescription).replace(/"/g, '').replace(/'/g, '').replace(/\n/g, '').replace(/\r/g, '') : item.codeLongDescription
				});				
			});
		}
		
		if (entry.metadata) {
			Ext.Array.each(entry.metadata, function(item){
				vitals.push({
					componentId: entry.componentId,
					label: item.label,
					value: item.value,
					type: item.label,
					code: item.value,
					vitalType: 'METADATA',
					securityMarkingType: item.securityMarkingType,
					updateDts: item.updateDts
				});			
			});
		}
		
		Ext.Array.sort(vitals, function(a, b){
			return a.label.localeCompare(b.label);	
		});
		entry.vitals = vitals;
		
		var updated = false;
		Ext.Array.each(entry.vitals, function(item){
			if (item.updateDts && item.updateDts > entry.lastViewedDts) {
				updated = true;
			}	
		}, this);	
		if (updated) {
			this.addBodyCls('watch-detail-update');
		}		
		return entry;
	}	
	
});

Ext.define('OSF.component.template.Dependencies', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Dependencies',
	
	titleCollapse: true,
	collapsible: true,
	title: 'External Dependencies',
	
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',			
		'	<tpl for="dependencies">',	
		'		<tr class="details-table">',
		'			<td class="details-table"><tpl if="securityMarkingType">({securityMarkingType}) </tpl><b>{dependencyName} {version}</b> <br>',
		'			<tpl if="dependancyReferenceLink"><a href="{dependancyReferenceLink}" class="details-table" target="_blank">{dependancyReferenceLink}</a><br></tpl> ',
		'			<tpl if="comment">{comment}</tpl> ',
		'			</td>',
		'		</tr>',
		'	</tpl>',
		'</table>'
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.dependencies || entry.dependencies.length === 0) {
			this.setHidden(true);
		} else {
			Ext.Array.sort(entry.dependencies, function(a, b){
				return a.dependencyName.localeCompare(b.dependencyName);	
			});	
			
			var updated = false;
			Ext.Array.each(entry.dependencies, function(item){
				if (item.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}			
		}		
		return entry;
	}	
	
});

Ext.define('OSF.component.template.DI2EEvalLevel', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.DI2EEvalLevel',
	
	titleCollapse: true,
	collapsible: true,
	title: 'DI2E Evaluation Level',
	
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',					
		'		<tpl if="evalLevels.level">',
		'			<tr class="details-table">',
		'				<th class="details-table"><b>{evalLevels.level.typeDesciption}</b></th>',
		'				<td class="details-table highlight-{evalLevels.level.highlightStyle}" ><h3>{evalLevels.level.label}</h3>{evalLevels.level.description}</td>',
		'			</tr>',	
		'		</tpl>',
		'		<tpl if="evalLevels.state">',		
		'			<tr class="details-table">',
		'				<th class="details-table"><b>{evalLevels.state.typeDesciption}</b></th>',
		'				<td class="details-table highlight-{evalLevels.state.highlightStyle}" ><h3>{evalLevels.state.label}</h3>{evalLevels.state.description}</td>',
		'			</tr>',	
		'		</tpl>',
		'		<tpl if="evalLevels.intent">',				
		'			<tr class="details-table">',
		'				<th class="details-table"><b>{evalLevels.intent.typeDesciption}</b></th>',
		'				<td class="details-table highlight-{evalLevels.intent.highlightStyle}" ><h3>{evalLevels.intent.label}</h3>{evalLevels.intent.description}</td>',
		'			</tr>',			
		'		</tpl>',
		'</table>'		
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		
		var evalLevels = {};		
		if (!entry.attributes && entry.attributes.length <= 0) {
			this.setHidden(true);
		} else {
			var updated = false;			
			Ext.Array.each(entry.attributes, function(item){
				if (item.type === 'DI2ELEVEL') {
					evalLevels.level = {};
					evalLevels.level.typeDesciption = item.typeDescription; 
					evalLevels.level.code = item.code; 
					evalLevels.level.label = item.codeDescription; 
					evalLevels.level.description = item.codeLongDescription;
					evalLevels.level.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}
				} else if (item.type === 'DI2ESTATE') {
					evalLevels.state = {};
					evalLevels.state.typeDesciption = item.typeDescription; 
					evalLevels.state.code = item.code; 
					evalLevels.state.label = item.codeDescription; 
					evalLevels.state.description = item.codeLongDescription;
					evalLevels.state.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}					
				} else if (item.type === 'DI2EINTENT') {
					evalLevels.intent = {};
					evalLevels.intent.typeDesciption = item.typeDescription; 
					evalLevels.intent.code = item.code; 
					evalLevels.intent.label = item.codeDescription; 
					evalLevels.intent.description = item.codeLongDescription; 
					evalLevels.intent.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}					
				}
			});			
			
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}	
			
			if (!evalLevels.level && !evalLevels.state && !evalLevels.intent) {
				this.setHidden(true);
			}
		}
		entry.evalLevels = evalLevels;					
		return entry;
	}	
	
});

Ext.define('OSF.component.template.EvaluationSummary', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationSummary',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Reusability Factors (5=best)',
	
	tpl: new Ext.XTemplate(
		'<div class="rolling-container">',			
		'	<div class="rolling-container-row">',
		'		<tpl for="evaluation.evaluationSections">',	
		'			<div class="rolling-container-block">',
		'				<div class="detail-eval-item ">',
		'					<span class="detail-eval-label">{name} <tpl if="sectionDescription"><i class="fa fa-question-circle" data-qtip="{sectionDescription}" data-qtitle="{name}" data-qalignTarget="bl-tl" data-qclosable="true" ></i></tpl></span>',
		'					<span class="detail-eval-score" data-qtip="{actualScore}">{display}</span>',	
		'				</div>',	
		'			</div>',
		'		</tpl>',
		'	</div>',
		'</div>'
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.evaluation || entry.evaluation.evaluationSections.length === 0) {
			this.setHidden(true);		
			return null;
		} else {
			Ext.Array.each(entry.evaluation.evaluationSections, function(section){
				if (section.notAvailable || section.actualScore <= 0) {
					section.display = "N/A";
				} else {
					var score = Math.round(section.actualScore);
					section.display = "";
					for (var i= 0; i<score; i++){
						section.display += '<i class="fa fa-circle detail-evalscore"></i>';
					}
				}				
			});
			
			
			Ext.Array.sort(entry.evaluation.evaluationSections, function(a, b){
				return a.name.localeCompare(b.name);	
			});
			
			var updated = false;
			Ext.Array.each(entry.evaluation.evaluationSections, function(item){
				if (item.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}			
			return entry;
		}
	}	
	
});

Ext.define('OSF.component.template.Media', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Media',
	
	scrollable: 'x',
	style: 'height: auto !important;',
	
	tpl: new Ext.XTemplate(
		' <h2>Screenshots / Media</h2>',	
		'	<tpl for="componentMedia">',	
		'       <tpl if="typeof(hideInDisplay) == \'undefined\' || hideInDisplay !== true">',
		'		<div class="detail-media-block">',
		'		<tpl switch="mediaTypeCode">',
		'				<tpl case="IMG">',
		'					<img src="{link}" height="150" alt="{[values.caption ? values.caption : values.filename]}" onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'<tpl if="caption">{caption}</tpl>\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');" />',		
		'				<tpl case="AUD">',
		'					<i class="fa fa-file-sound-o" style="font-size: 11em;" onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'{caption}\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');"></i><br><br>',
		'				<tpl case="VID">',
		'					<video onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'{caption}\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');" height="130" src="{link}#t=10" onloadedmetadata="this.currentTime=10;" ></video><br><br>',		
		'				<tpl case="ARC">',
		'					<i class="fa fa-file-archive-o" style="font-size: 11em;" onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'{caption}\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');"></i><br><br>',
		'				<tpl case="TEX">',
		'					<i class="fa fa-file-text-o" style="font-size: 11em;" onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'{caption}\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');"></i><br><br>',
		'				<tpl default>',
		'					<i class="fa fa-file-o" style="font-size: 11em;" onclick="MediaViewer.showMedia(\'{mediaTypeCode}\', \'{link}\', \'{caption}\', \'{filename}\', \'{mimeType}\', \'{componentMediaId}\');"></i><br><br>',
		'			</tpl>',
		'			<tpl if="caption || securityMarkingType"><p class="detail-media-caption"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{caption}</p></tpl>',
		'		</div>',
		'       </tpl>',
		'	</tpl>'
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.componentMedia || entry.componentMedia.length === 0) {
			this.setHidden(true);
		} else {

			// Compile a list of media that are not hidden.
			var mediaToShow = [];
			Ext.Array.each(entry.componentMedia, function(media) {
				if (!media.hideInDisplay) mediaToShow.push(media);
			});
			MediaViewer.mediaList = mediaToShow;
			
			var updated = false;
			Ext.Array.each(entry.componentMedia, function(item){
				if (item.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}			
		}
		return entry;
	}	
	
});

Ext.define('OSF.component.template.Relationships', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Relationships',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Relationships',
	
		
	initComponent: function () {
		this.callParent();
		
		var relationshipPanel = this;
				
		
		relationshipPanel.tabPanel = Ext.create('Ext.tab.Panel', {
			items: [
				Ext.create('OSF.component.RelationshipVisPanel', {
					title: 'Visualization',
					itemId: 'visual'
				}),
				{
					xtype: 'panel',
					itemId: 'relationTable',
					title: 'Table',
					tpl: new Ext.XTemplate(
						' <table class="details-table" width="100%">',	
						'	<tr><th class="details-table">Entry</th><th class="details-table">Relationship Type</th><th class="details-table">Related Entry</th></tr>',
						'	<tpl for="relationships">',	
						'		<tr class="details-table">',
						'			<td class="details-table">{ownerComponentName}</td>',
						'			<td class="details-table" align="center"><b>{relationshipTypeDescription}</b></td>',
						'			<td class="details-table"><a href="view.jsp?id={targetComponentId}" class="details-table" target="_blank">{targetComponentName}</a></td>',
						'		</tr>',
						'	</tpl>',
						'</table>'		
					)					
				}
			]
		});
		relationshipPanel.add(relationshipPanel.tabPanel);
		
	},
	
	updateHandler: function(entry){
		var relationshipPanel = this;
		
		if (!entry.relationships || entry.relationships.length === 0) {
			this.setHidden(true);
			return null;
		} else {
			Ext.Array.sort(entry.relationships, function(a, b){
				return a.relationshipTypeDescription.localeCompare(b.relationshipTypeDescription);	
			});
									
			relationshipPanel.tabPanel.getComponent('relationTable').update(entry);
			relationshipPanel.tabPanel.getComponent('visual').setHeight(entry.relationships.length*80);
			relationshipPanel.tabPanel.getComponent('visual').originalComponentId = entry.componentId;
			relationshipPanel.tabPanel.getComponent('visual').updateDiagramData(entry);	
			
			var updated = false;
			Ext.Array.each(entry.relationships, function(item){
				if (item.updateDts > entry.lastViewedDts) {
					updated = true;
				}	
			}, this);
			if (updated) {
				this.addBodyCls('watch-detail-update');
			}			
		}		
				
		return null;
	}	
	
});

