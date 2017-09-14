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

/* global Ext, MediaViewer, CoreService, CoreUtil, relatedStore */

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
	alias: ['widget.templatedescription'],	
	
	showDescriptionHeader: false,
	bodyCls: 'text-readable',
	tpl: new Ext.XTemplate(
		'<div><tpl if="showDescriptionHeader"><h2><tpl if="componentSecurityMarkingType">({componentSecurityMarkingType}) </tpl>Description</h2></tpl>',	
		'	{description}',
		'</div>'		
	),
		
	initComponent: function () {
		this.callParent();				
	},
	
	updateHandler: function(entry){
		// Ext escape dose not properly escape Apostrophes for display on an html page 
		// so we need to replace \' with the proper html escape of &apos;
		entry.description = Ext.util.Format.escape(entry.description).replace(/\n/g, '').replace(/\r/g, '').replace(/\\'/g,'&apos;');		
				
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
				if (item.getAttribute && item.getAttribute('href')) {
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
			if (mediaToShow.length === 0) {
				this.setHidden(true);
			}
			
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

Ext.define('OSF.component.template.Reviews', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Reviews',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Reviews',
	bodyStyle: 'padding: 10px;',
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
	dockedItems: [
		{
			xtype: 'button',
			text: 'Write a Review',
			maxWidth: 200,
			scale: 'medium',
			margin: 10,
			iconCls: 'fa fa-lg fa-star-half-o icon-small-vertical-correction',
			handler: function(){	
				var reviewPanel = this.up('panel');
				reviewPanel.reviewActions.reviewWindow.refresh();
				reviewPanel.reviewActions.reviewWindow.show();
			}
		}
	],		
	items: [
		{
			xtype: 'panel',
			itemId: 'summary',
			title: 'Review Summary',
			titleCollapse: true,
			collapsible: true,
			hidden: true,
			margin: '0 0 1 0',
			bodyStyle: 'padding: 10px;',
			tpl: new Ext.XTemplate(
				'<div class="review-summary">',
				'	<div class="details">',
				'		<tpl if="totalReviews && totalReviews &gt; 0">',
				'		    <div class="review-summary-rating">Average Rating: <tpl for="averageRatingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></div>',							
				'			<div><span class="label">{recommended} out of {totalReviews} ({[Math.round((values.recommended/values.totalReviews)*100)]}%)</span> reviewers recommended</div>',
				'		</tpl>',
				'	</div>',
				'	<div class="pros">',
				'		<tpl if="pros.length &gt; 0">',
				'			<div class="review-pro-con-header">Pros</div>',
				'			<tpl for="pros">',
				'				- {text} <span class="review-summary-count">({count})</span><br>',	
				'			</tpl>',
				'		</tpl>',
				'	</div>',
				'	<div class="cons">',
				'		<tpl if="cons.length &gt; 0">',
				'			<div class="review-pro-con-header">Cons</div>',							
				'			<tpl for="cons">',
				'				- {text} <span class="review-summary-count">({count})</span><br>',	
				'			</tpl>',
				'		</tpl>',
				'	</div>',
				'</div>'
			)						
		},
		{
			xtype: 'panel',
			itemId: 'reviews',
			title: 'User Reviews',
			hidden: true,						
			titleCollapse: true,
			collapsible: true,
			bodyStyle: 'padding: 10px;',
			tpl: new Ext.XTemplate(
				'<tpl for=".">',	
				'<div class="review-section">',
				'	<tpl if="activeStatus == \'P\'"><div class="alert-warning" style="text-align: center;"><i class="fa fa-warning"></i> Review pending admin approval before being made public.</div></tpl>',
				'	<div class="details">',
				'		<div class="title"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>{title}</div>',
				'		<div class="rating"><tpl for="ratingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></div>',
				'		<div class="review-who-section">',
				'			{username} ({userTypeCode}) - {[Ext.util.Format.date(values.updateDate, "m/d/y")]}<tpl if="recommend"> - <strong>Recommend</strong></tpl>',
				'			<tpl if="owner"><i class="fa fa-edit small-button-normal" title="Edit" onclick="CoreUtil.pageActions.reviewActions.editReview(\'{reviewId}\')"> Edit</i> <i class="fa fa-trash small-button-danger" title="Delete" onclick="CoreUtil.pageActions.reviewActions.deleteReview(\'{reviewId}\', \'{componentId}\')"> Delete</i></tpl>',			
				'		</div>',
				'		<div><span class="label">Organization:</span> {organization}</div>',
				'		<div><span class="label">Experience:</span> {userTimeDescription}</div>',							
				'		<div><span class="label">Last Used:</span> {[Ext.util.Format.date(values.lastUsed, "m/Y")]}</div>',
				'	</div>',			
				'	<div class="pros">',
				'		<tpl if="pros.length &gt; 0">',									
				'		<div class="review-pro-con-header">Pros</div>',
				'		<tpl for="pros">',
				'			- {text}<br>',	
				'		</tpl></tpl>',
				'	</div>',			
				'	<div class="cons">',
				'		<tpl if="cons.length &gt; 0">',
				'		<div class="review-pro-con-header">Cons</div>',
				'		<tpl for="cons">',
				'			- {text}<br>',	
				'		</tpl></tpl>',
				'	</div>',				
				'	<div class="comments">',
				'		<span class="label">Comments:</span>',				
				'		<div>{comment}</div>',							
				'	</div>',				
				'</div>',
				'</tpl>'
			)						
		}
	],	
	
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var reviewPanel = this;
		
		var reviewActions = {
			editReview: function(reviewId) {
				var reviewData;
				Ext.Array.each(reviewActions.reviews, function(review){
					if (review.reviewId === reviewId) {
						reviewData = review;
					}
				});
				
				var record = Ext.create('Ext.data.Model', {					
				});
				record.set(reviewData);
				
				reviewActions.reviewWindow.show();
				reviewActions.reviewWindow.editReview(record);
			},
			
			deleteReview: function(reviewId, componentId) {
				Ext.Msg.show({
					title:'Delete Review?',
					message: 'Are you sure you want to delete this review?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							reviewPanel.setLoading("Deleting...");
							Ext.Ajax.request({
								url: 'api/v1/resource/components/'+ componentId+'/reviews/'+reviewId,
								method: 'DELETE',
								callback: function(){
									reviewPanel.setLoading(false);
								},
								success: function(){
									reviewActions.refreshReviews();
								}
							});
						} 
					}
				});				
			},
			refreshReviews: function() {
				reviewPanel.setLoading('Refreshing...');
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + entry.componentId + '/reviews/view',
					callback: function(){
						reviewPanel.setLoading(false);
					}, 						
					success: function(response, opts){
						var reviews = Ext.decode(response.responseText);
						var entryLocal = {};
						entryLocal.reviews = reviews;
						processReviews(entryLocal, reviewPanel.user);							
					}
				});				
			},
			reviewWindow: Ext.create('OSF.component.ReviewWindow', {	
				componentId: entry.componentId,
				postHandler: function(reviewWin, response) {
					reviewActions.refreshReviews();
				}
			})			
		};
		reviewPanel.reviewActions = reviewActions;
		CoreUtil.pageActions.reviewActions = reviewActions;
		
		var processReviews = function(entryLocal, user) {
				
				//gather summary
				var summaryData = {					
					totalRatings: 0,
					averageRatingStars: [],
					pros: [],
					cons: [],
					totalReviews: entryLocal.reviews.length,
					recommended: 0
				};
				
				Ext.Array.each(entryLocal.reviews, function(review){
					summaryData.totalRatings += review.rating;
					if (review.recommend) {					
						summaryData.recommended++;
					}
					
					Ext.Array.each(review.pros, function(pro) {
						var found = false;
					
						Ext.Array.each(summaryData.pros, function(sumpro){
							if (sumpro.text === pro.text) {
								sumpro.count++;
								found = true;
							}
						});
						
						if (!found) {
							summaryData.pros.push({
								text: pro.text,
								count: 1
							});
						}
					});
					
					Ext.Array.each(review.cons, function(con) {
						var found = false;
					
						Ext.Array.each(summaryData.cons, function(sumpro){
							if (sumpro.text === con.text) {
								sumpro.count++;
								found = true;
							}
						});
						
						if (!found) {
							summaryData.cons.push({
								text: con.text,
								count: 1
							});
						}
					});	
					
					Ext.Array.sort(review.pros, function(a, b){
						return a.text.localeCompare(b.text);	
					});
					Ext.Array.sort(review.cons, function(a, b){
						return a.text.localeCompare(b.text);	
					});	
					
					review.ratingStars = [];
					for (var i=0; i<5; i++){					
						review.ratingStars.push({						
							star: i < review.rating ? (review.rating - i) > 0 && (review.rating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
						});
					}
					
					if (review.username === user.username || 
							CoreService.userservice.userHasPermisson(user, ['ADMIN-REVIEW'])
						) {
						review.owner = true;
					}
					
				});
				
				reviewActions.reviews = entryLocal.reviews;
				
				var reviewPanelReviews = reviewPanel.getComponent('reviews');
				var reviewPanelSummary = reviewPanel.getComponent('summary');
				
				Ext.Array.sort(summaryData.pros, function(a, b){
					return a.text.localeCompare(b.text);	
				});
				Ext.Array.sort(summaryData.cons, function(a, b){
					return a.text.localeCompare(b.text);	
				});				
				var averageRating = summaryData.totalRatings / summaryData.totalReviews;
				summaryData.averageRating = averageRating;

				var fullStars = Math.floor(averageRating);
				for (var i=1; i<=fullStars; i++) {
					summaryData.averageRatingStars.push({star: 'star'});
				}

				// If the amount over the integer is at least 0.5 they get a half star, otherwise no half star.
				var halfStar = Math.abs(fullStars - averageRating) >= 0.5;
				if (halfStar) {
					summaryData.averageRatingStars.push({star: 'star-half-o'});
				}

				// Add empty stars until there are 5 stars total.
				while (summaryData.averageRatingStars.length < 5) {
					summaryData.averageRatingStars.push({star: 'star-o'});
				}

								
				if (entryLocal.reviews.length > 0) {
					reviewPanelSummary.setHidden(false);
					reviewPanelSummary.update(summaryData);
					reviewPanelReviews.setHidden(false);
					reviewPanelReviews.update(entryLocal.reviews);					
				} else {					
					reviewPanelSummary.update(summaryData);					
					reviewPanelReviews.update(entryLocal.reviews);	
					reviewPanelSummary.setHidden(true);
					reviewPanelReviews.setHidden(true);
				}
				
			};		
		
		CoreService.userservice.getCurrentUser().then(function(user){			
			reviewPanel.user = user;
			processReviews(entry, user);
		});		
		
		return null;
	}

});

Ext.define('OSF.component.template.Questions', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Questions',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Questions & Answers',
	bodyStyle: 'padding: 10px;',
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
	dockedItems: [
		{
			xtype: 'button',
			text: 'Ask a Question',
			maxWidth: 200,
			scale: 'medium',
			margin: 10,
			iconCls: 'fa  fa-lg fa-comment icon-small-vertical-correction',
			handler: function(){	
				var questionPanel = this.up('panel');
				questionPanel.questionActions.questionWindow.show();
				questionPanel.questionActions.questionWindow.refresh();
			}
		}
	],	
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var questionPanel = this;
		
		var questionActions = {
			
			refreshQuestions: function(){
				questionPanel.setLoading('Refreshing...');
				Ext.Ajax.request({
					url: 'api/v1/resource/components/' + entry.componentId + '/questions/view',
					callback: function(){
						questionPanel.setLoading(false);
					}, 						
					success: function(response, opts){
						var questions = Ext.decode(response.responseText);
						var entryLocal = {};
						entryLocal.questions = questions;
						processQuestions(entryLocal, questionPanel.user);							
					}
				});
			},
			editResponse: function(responseId) {
				var responseData;
				Ext.Array.each(questionActions.questions, function(question){
					Ext.Array.each(question.responses, function(response){
						if (response.responseId === responseId) {
							responseData = response;							
						}						
					});
				});
				
				var record = Ext.create('Ext.data.Model', {					
				});
				record.set(responseData);
				
				questionActions.responseWindow.show();
				questionActions.responseWindow.edit(record);				
				
			},
			deleteResponse: function(responseId, questionId, componentId){
				
				Ext.Msg.show({
					title:'Delete Answer?',
					message: 'Are you sure you want to delete this answer?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn) {
						if (btn === 'yes') {
							questionPanel.setLoading("Deleting...");
							Ext.Ajax.request({
								url: 'api/v1/resource/components/'+componentId+'/questions/' + questionId + '/responses/' + responseId,
								method: 'DELETE',
								callback: function(){
									questionPanel.setLoading(false);
								},
								success: function(){
									questionActions.refreshQuestions();
								}
							});
						} 
					}
				});				
			},			
			questionWindow: Ext.create('OSF.component.QuestionWindow', {
				componentId: entry.componentId,
				postHandler: function(questionWin, response) {
					questionActions.refreshQuestions();
				}				
			}),
			responseWindow: Ext.create('OSF.component.ResponseWindow', {
				componentId: entry.componentId,
				postHandler: function(responseWin, response) {
					questionActions.refreshQuestions();
				}
			})
		};
		
		questionPanel.questionActions = questionActions;
		CoreUtil.pageActions.questionActions = questionActions;		
		
		var processQuestions = function(entryLocal, user) {

			var questionPanels = [];
			questionActions.questions = entryLocal.questions;
			Ext.Array.each(entryLocal.questions, function(question){

				var questionSecurity = '';
				if (question.securityMarkingType) {
					questionSecurity = '(' + question.securityMarkingType + ') '; 
				}
				var pendingNotice = "";
				if(question.activeStatus === "P")
				{
					pendingNotice = '<div class="alert-warning" style="text-align: center;"><i class="fa fa-warning"></i> Question pending admin approval before being made public.</div>';
				}
				var text = '<div class="question-question">' + pendingNotice + '<span class="question-response-letter-q">Q.</span> '+ questionSecurity + question.question + '</div>';
				text += '<div class="question-info">' +
						question.username + ' (' + question.userType + ') - ' + Ext.util.Format.date(question.questionUpdateDts, "m/d/Y") +
						'</div>';

				Ext.Array.each(question.responses, function(response){
					response.questionId = question.questionId;
					response.componentId = question.componentId;
					response.owner = (response.username === user.username || CoreService.userservice.userHasPermisson(user, ['ADMIN-QUESTIONS']));					
				});


				var panel = Ext.create('Ext.panel.Panel', {
					titleCollapse: true,
					collapsible: true,
					title: text,
					bodyStyle: 'padding: 10px;',
					data: question.responses,
					tpl: new Ext.XTemplate(							
						'<tpl for=".">',
						'	<tpl if="activeStatus === \'A\' || (activeStatus === \'P\' &amp;&amp; owner === true)">',
						'		<tpl if="activeStatus === \'P\'"><div class="alert-warning" style="text-align: center;font-size:1.25em"><i class="fa fa-warning"></i> Answer pending admin approval before being made public.</div></tpl>',
						'		<div class="question-response"><span class="question-response-letter">A.</span><tpl if="securityMarkingType">({securityMarkingType}) </tpl> {response}</div>',
						'		<tpl if="owner"><i class="fa fa-edit small-button-normal" title="Edit" onclick="CoreUtil.pageActions.questionActions.editResponse(\'{responseId}\')"> Edit</i> <i class="fa fa-trash small-button-danger" title="Delete" onclick="CoreUtil.pageActions.questionActions.deleteResponse(\'{responseId}\', \'{questionId}\', \'{componentId}\')"> Delete</i></tpl>',
						'		<div class="question-info">{username} ({userType}) - {[Ext.util.Format.date(values.answeredDate, "m/d/Y")]}</div><br>',	
						'		<hr>',
						'	</tpl>',
						'</tpl>'
					),
					dockedItems: [
						{
							xtype: 'button',
							dock: 'bottom',
							text: 'Answer',
							maxWidth: 150,
							scale: 'medium',								
							margin: 10,
							iconCls: 'fa  fa-lg fa-comments-o icon-top-padding-5',
							handler: function(){
								questionActions.responseWindow.refresh();
								questionActions.responseWindow.questionId = question.questionId;
								questionActions.responseWindow.show();
							}
						}
					]				
				});
				if (question.username === user.username || 
						CoreService.userservice.userHasPermisson(user, ['ADMIN-QUESTIONS'])
					) 
				{
					panel.addDocked(
						{
							xtype: 'toolbar',
							dock: 'top',								
							items: [
								{
									text: 'Edit',
									tooltip: 'Edit Question',
									iconCls: 'fa fa-lg fa-edit icon-button-color-edit',
									handler: function(){
										questionActions.questionWindow.show();

										var record = Ext.create('Ext.data.Model');
										record.set(question);											
										questionActions.questionWindow.edit(record);
									}
								},
								{	
									text: 'Delete',
									tooltip: 'Delete Question',
									iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
									handler: function(){
										Ext.Msg.show({
											title:'Delete Question?',
											message: 'Are you sure you want to delete this Question?',
											buttons: Ext.Msg.YESNO,
											icon: Ext.Msg.QUESTION,
											fn: function(btn) {
												if (btn === 'yes') {
													questionPanel.setLoading("Deleting...");
													Ext.Ajax.request({
														url: 'api/v1/resource/components/' + questionPanel.componentId + '/questions/' + question.questionId,
														method: 'DELETE',
														callback: function(){
															questionPanel.setLoading(false);
														},
														success: function(){
															questionActions.refreshQuestions();
														}
													});
												} 
											}
										});
									}										
								}
							]
						}
					);
				}											

				questionPanels.push(panel);				

			});
			questionPanel.removeAll();
			questionPanel.add(questionPanels);

		};		
		questionPanel.componentId = entry.componentId;
		CoreService.userservice.getCurrentUser().then(function(user){			
			questionPanel.user = user;
			processQuestions(entry, user);
		});	
		
		return null;
	}

});

Ext.define('OSF.component.template.RelatedAttributes', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.RelatedAttributes',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Related Entries',
	layout: 'fit',
	dockedItems: [
		{
			xtype: 'combobox',
			itemId: 'attributeSelect',
			valueField: 'code',
			displayField: 'description',
			fieldLabel: 'Related to',
			editable: false,
			forceSelection: true,			
			store: {},
			listeners: {
				change: function(field, newValue, oldValue, opts) {
					var relatedPanel = field.up('panel');
					
					var record = field.getSelection();
					
					var searchObj = {
						"sortField": "name",
						"sortDirection": "ASC",
						"searchElements": [{
								"searchType": "ATTRIBUTE",
								"keyField": record.get('type'),
								"keyValue": record.get('code'),
								"caseInsensitive": true,
								"numberOperation": "EQUALS",
								"stringOperation": "EQUALS",
								"mergeCondition": "OR"
							}]
					};
					 
					var store = relatedPanel.getComponent('grid').getStore();
					store.getProxy().buildRequest = function (operation) {
						var initialParams = Ext.apply({
							paging: true,
							sortField: operation.getSorters()[0].getProperty(),
							sortOrder: operation.getSorters()[0].getDirection(),
							offset: operation.getStart(),
							max: operation.getLimit()
						}, operation.getParams());
						params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

						var request = new Ext.data.Request({
							url: 'api/v1/service/search/advance',
							params: params,
							operation: operation,
							action: operation.getAction(),
							jsonData: Ext.util.JSON.encode(searchObj)
						});
						operation.setRequest(request);

						return request;
					};
					store.loadPage(1);						 
					
				}
			}
		}
	],		
	initComponent: function () {
		this.callParent();
		var relatedPanel = this;
		
		relatedPanel.relatedStore = Ext.create('Ext.data.Store', {
			pageSize: 50,
			autoLoad: false,
			remoteSort: true,
			sorters: [
				new Ext.util.Sorter({
					property: 'name',
					direction: 'ASC'
				})
			],				
			proxy: CoreUtil.pagingProxy({
				actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			}),
			listeners: {
				load: function(store, records) {
					store.filterBy(function(record){
						return record.get('componentId') !== relatedPanel.componentId;
					});
				}
			}			
		});
		
		relatedPanel.add({
			xtype: 'grid',
			itemId: 'grid',
			height: 300,
			columnLines: true,
			store: relatedPanel.relatedStore,
			columns: [
				{ text: 'Name', dataIndex: 'name', flex:2, minWidth: 250, cellWrap: true, 
					renderer: function (value, meta, record) {
						return '<a class="details-table" href="view.jsp?id=' + record.get('componentId') + '&fullPage=true" target="_blank">' + value + '</a>';
					}
				},
				{ text: 'Description', dataIndex: 'description', flex: 2,
					cellWrap: true,
					renderer: function (value) {
						value = Ext.util.Format.stripTags(value);
						return Ext.String.ellipsis(value, 300);
					}
				},							
				{ text: 'Type', align: 'center', dataIndex: 'componentTypeDescription', width: 150 }				
			],
			dockedItems: [
				{
					xtype: 'pagingtoolbar',							
					dock: 'bottom',
					store: relatedPanel.relatedStore,
					displayInfo: true
				}				
			]
		});		
		
	},
	
	updateHandler: function(entry){
		var relatedPanel = this;
				
		if ((!entry.attributes || entry.attributes.length === 0)) {
			this.setHidden(true);
		}
		
		relatedPanel.componentId = entry.componentId;

		if (entry.attributes) {
			var attributes = [];	
			Ext.Array.each(entry.attributes, function(item){
				attributes.push({
					code: item.code,
					type: item.type,
					description: item.typeDescription + ": " + item.codeDescription
				});
			});
			
			//load
			relatedPanel.queryById('attributeSelect').getStore().loadData(attributes);
			if (attributes.length > 0) {
				relatedPanel.queryById('attributeSelect').setValue(attributes[0].code);
			}
		}		
		
		
		return null;
	}

});

Ext.define('OSF.component.template.RelatedOrganization', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.RelatedOrganization',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Related Organization Entries',
	layout: 'fit',

	initComponent: function () {
		this.callParent();
		
		var relatedPanel = this;
		
		relatedPanel.relatedStore = Ext.create('Ext.data.Store', {
			pageSize: 50,
			autoLoad: false,
			remoteSort: true,
			sorters: [
				new Ext.util.Sorter({
					property: 'name',
					direction: 'ASC'
				})
			],				
			proxy: CoreUtil.pagingProxy({
				actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			}),
			listeners: {
				load: function(store, records) {
					store.filterBy(function(record){
						return record.get('componentId') !== relatedPanel.componentId;
					});
				}
			}			
		});
		
		relatedPanel.add({
			xtype: 'grid',
			itemId: 'grid',
			height: 300,
			columnLines: true,
			store: relatedPanel.relatedStore,
			columns: [
				{ text: 'Name', dataIndex: 'name', flex:2, minWidth: 250, cellWrap: true, 
					renderer: function (value, meta, record) {
						return '<a class="details-table" href="view.jsp?id=' + record.get('componentId') + '&fullPage=true" target="_blank">' + value + '</a>';
					}
				},
				{ text: 'Description', dataIndex: 'description', flex: 2,
					cellWrap: true,
					renderer: function (value) {
						value = Ext.util.Format.stripTags(value);
						return Ext.String.ellipsis(value, 300);
					}
				},							
				{ text: 'Type', align: 'center', dataIndex: 'componentTypeDescription', width: 150 }				
			],
			dockedItems: [
				{
					xtype: 'pagingtoolbar',							
					dock: 'bottom',
					store: relatedPanel.relatedStore,
					displayInfo: true
				}				
			]
		});				
	},
	
	updateHandler: function(entry){
		var relatedPanel = this;
	
		relatedPanel.componentId = entry.componentId;
	
		var searchObj = {
			"sortField": "name",
			"sortDirection": "ASC",				
			"searchElements": [{
					"searchType": 'COMPONENT',
					"field": 'organization',
					"value": entry.organization,
					"caseInsensitive": true,
					"numberOperation": "EQUALS",
					"stringOperation": "EQUALS",
					"mergeCondition": "OR" 
			}]
		 };

		var store = relatedPanel.getComponent('grid').getStore();
		store.getProxy().buildRequest = function (operation) {
			var initialParams = Ext.apply({
				paging: true,
				sortField: operation.getSorters()[0].getProperty(),
				sortOrder: operation.getSorters()[0].getDirection(),
				offset: operation.getStart(),
				max: operation.getLimit()
			}, operation.getParams());
			params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

			var request = new Ext.data.Request({
				url: 'api/v1/service/search/advance',
				params: params,
				operation: operation,
				action: operation.getAction(),
				jsonData: Ext.util.JSON.encode(searchObj)
			});
			operation.setRequest(request);

			return request;
		};
		store.loadPage(1);	
		
		return null;
	}

});

Ext.define('OSF.component.template.EvaluationVersionSelect', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationVersionSelect',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Evaluation Versions',	
	items: [
		{
			xtype: 'combo',
			itemId: 'versions',
			valueField: 'code',
			displayField: 'description',			
			editable: false,
			forceSelection: true,
			width: 300,
			store: {},
			listeners: {
				change: function(field, newValue, oldValue, opts) {
					var versionSelect = field.up('panel');
					
					versionSelect.entry.currentEval = field.getSelection().data.eval;
					
					//update all registered blocks on switch
					if (versionSelect.entry.evalListeners) {
						Ext.Array.each(versionSelect.entry.evalListeners, function(callback){
							callback.call(this, versionSelect.entry.currentEval);
						});
					}					
				}	
			}
		}
	],
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var versionSelect = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length <= 1) {
			versionSelect.setHidden(true);
		} else {

			if (entry.fullEvaluations.length > 0) {
				//populate combo
				var versions = [];
				Ext.Array.each(entry.fullEvaluations, function(eval){
					versions.push({
						code: eval.evaluation.evaluationId,
						eval: eval,
						description: "Version: " + eval.evaluation.version
					});
				});
				
				versionSelect.queryById('versions').getStore().loadData(versions);
				versionSelect.queryById('versions').suspendEvents(false);
				versionSelect.queryById('versions').setValue(entry.fullEvaluations[0].evaluation.evaluationId);
				versionSelect.queryById('versions').resumeEvents();
				entry.currentEval = entry.fullEvaluations[0];
				entry.evalListeners = [];
				versionSelect.entry = entry;
			}
		}
		
		return null;
	}

});

Ext.define('OSF.component.template.EvaluationSections', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationSections',
	
	//titleCollapse: true,
	//collapsible: true,	
	items: [		
	],		
	initComponent: function () {
		this.callParent();		
	},
	
	updateHandler: function(entry){
		var sectionPanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length <= 0) {
			sectionPanel.setHidden(true);
		} else {
			
			var updateSection = function(evaluation) {
				
				//var tabPanel = sectionPanel.queryById('tabs');
				sectionPanel.removeAll();
				
				var internalPanels = [];
				Ext.Array.each(evaluation.contentSections, function(section){
					internalPanels.push({
						xtype: 'panel',
						title: section.section.title,
						titleCollapse: true,
						collapsible: true,
						sectionData: section,
						margin: '0 0 20 0',
						bodyCls: 'text-readable',
						tpl: new Ext.XTemplate(
							'<div><h2><tpl if="section.securityMarkingType">({section.securityMarkingType})</tpl></h2>',	
							'	<tpl if="section.content">{section.content}</tpl>',
							'	<tpl for="subsections">',
							'		<tpl if="title && hideTitle == false"><h3>{title}</h3></tpl>',
							'		<tpl if="content">{content}</tpl>',
							'		<tpl for="customFields">',
							'			<b>{label}:</b> {value}',
							'		</tpl>',
							'	</tpl>',
							'</div>'		
						)
					});	
					
				});
				if (internalPanels.length > 0) {
					sectionPanel.add(internalPanels);					
					
					Ext.Array.each(sectionPanel.items.items, function(item){
						item.update(item.sectionData);							
					});
					
					Ext.defer(function(){	
						Ext.Array.each(sectionPanel.items.items, function(item){
						//	item.getEl().repaint();						
						});
						sectionPanel.updateLayout();
					}, 2000);
				}
			};
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);			
			
		}	
		return null;
	}

});

Ext.define('OSF.component.template.EvaluationSectionByTitle', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationSectionByTitle',
	
	titleCollapse: true,
	collapsible: true,
	title: '',
	sectionTitle: '',
	bodyCls: 'text-readable',
	tpl: new Ext.XTemplate(
		'<div><h2><tpl if="section.securityMarkingType">({section.securityMarkingType})</tpl></h2>',	
		'	<tpl if="section.content">{section.content}</tpl>',
		'	<tpl for="subsections">',
		'		<tpl if="title && hideTitle == false"><h3>{title}</h3></tpl>',
		'		<tpl if="content">{content}</tpl>',
		'		<tpl for="customFields">',
		'			<b>{label}:</b> {value}',
		'		</tpl>',
		'	</tpl>',
		'</div>'		
	),
		
	initComponent: function () {
		this.callParent();
		var sectionPanel = this;
	},
	
	updateHandler: function(entry){
		var sectionPanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
			sectionPanel.setHidden(true);
		} else {			
			
			var updateSection = function(evaluation) {			
				var sectionFound = null;
				Ext.Array.each(evaluation.contentSections, function(section){
					if (sectionPanel.sectionTitle === section.section.title) {
						sectionFound = section;
					}	
				});
				if (sectionFound) {
					sectionPanel.setTitle(sectionFound.section.title);
					sectionPanel.update(sectionFound);
					Ext.defer(function(){
						sectionPanel.updateLayout(true, true);						
					}, 200);					
				} else {
					sectionPanel.setHidden(true);
				}	
			};
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);
		}
		return null;
	}

});

Ext.define('OSF.component.template.EvaluationChecklistSummary', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationChecklistSummary',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Evaluation Checklist Summary',
	bodyCls: 'text-readable',
	tpl: new Ext.XTemplate(
		'<div><h2><tpl if="checkListAll.evaluationChecklist.securityMarkingType">({checkListAll.evaluationChecklist.securityMarkingType})</tpl></h2>',	
		'	<tpl if="checkListAll.evaluationChecklist.summary">{checkListAll.evaluationChecklist.summary}</tpl>',
		'</div>'		
	),	
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var checklistPanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
			checklistPanel.setHidden(true);
		} else {
			
			var updateSection = function(evaluation) {
				if (evaluation.checkListAll &&
					evaluation.checkListAll.evaluationChecklist &&
					evaluation.checkListAll.evaluationChecklist.summary) {
				
					checklistPanel.setHidden(false);
					checklistPanel.update(evaluation);
				} else {
					checklistPanel.setHidden(true);
				}
			};
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);
			
		}
		
		return null;		
	}

});

Ext.define('OSF.component.template.EvaluationChecklistDetail', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationChecklistDetail',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Evaluation Checklist Details',	
	layout: 'anchor',
	tools: [
		{
			type: 'unpin',			
			tooltip: 'Toggle Restrict Height',
			callback:  function(panel, tool) {
				var grid = panel.queryById('grid');
				if (grid.fixedHeight) {
					grid.fixedHeight = false;	
					tool.setType('unpin');
					grid.setMaxHeight(500000);
				} else {
					grid.fixedHeight = true;			
					tool.setType('pin');
					var showHeight = panel.up('panel').getHeight() - 60;
					grid.setMaxHeight(showHeight);
				}
			}
		}
	],
	items: [
		{
			xtype: 'grid',
			itemId: 'grid',			
			columnLines: true,
			width: '100%',			
			viewConfig: {
				enableTextSelection: true				
			},			
			store: {				
			},
			plugins: 'gridfilters',
			columns: [
				{ text: 'QID', dataIndex: 'qid', width: 75, align: 'center',
					renderer: function(value, meta, record) {
						meta.tdCls = 'text-readable';
						var link = '<a href="#" style="text-decoration: none;" onclick="CoreUtil.pageActions.checklistDetail.showQuestionDetails(\'' + record.get('questionId') + '\')">';						
						link += '<b>' + record.get('qid') + '</b>';
						link += '</a>';
						return link;
					}
				},
				{ text: 'Section', dataIndex: 'evaluationSectionDescription', width: 175, align: 'center', cellWrap: true,
					filter: {
					  type: 'list'            
					},
					renderer: function(value, meta, record) {
						meta.tdCls = 'text-readable';
						return value;
					}
				},
				{ text: 'Question', dataIndex: 'question', flex: 2, cellWrap: true,
					renderer: function(value, meta, record) {
						meta.tdCls = 'text-readable';
						return value;
					}
				},
				{ text: 'Score', dataIndex: 'score', width: 75, align: 'center',
					filter: {
					  type: 'list'            
					},
					renderer: function(value, meta, record) {
						meta.tdCls = 'text-readable';
						var link = '<a href="#" style="text-decoration: none;" onclick="CoreUtil.pageActions.checklistDetail.showScoreDetails(\'' + record.get('questionId') + '\')">';						
						if (record.get('notApplicable')) {
							link += '<b>N/A</b>';
						} else if (record.get('score')) {
							link += '<b>' + record.get('score') + '</b>';
						}												
						link += '</a>';
						return link;
					}
				},
				{ text: 'Response', dataIndex: 'response', flex: 1,  cellWrap: true,
					renderer: function(value, meta, record) {
						meta.tdCls = 'text-readable';
						return value;
					}					
				}
			]
		}
	],
	
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var checklistPanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
			checklistPanel.setHidden(true);
		} else {
			
			var updateSection = function(evaluation) {

				if (evaluation.checkListAll && evaluation.checkListAll.responses) {
					checklistPanel.setHidden(false);
					
					var detailGrid = checklistPanel.queryById('grid');								
					
					var responseData = [];
					Ext.Array.each(evaluation.checkListAll.responses, function(response){
						responseData.push({
							qid: response.question.qid,
							questionId: response.question.questionId,
							question: response.question.question,
							evaluationSectionDescription: response.question.evaluationSectionDescription,
							score: response.score,
							notApplicable: response.notApplicable,
							response: response.response
						});
					});
					
					detailGrid.getStore().loadRawData(responseData);
					
					var findQuestion = function(questionId) {
						var question;
						Ext.Array.each(evaluation.checkListAll.responses, function(response){
							if (response.question.questionId === questionId) {
								question = response.question;
							}
						});
						return question;
					};
					
					CoreUtil.pageActions.checklistDetail = {
						showQuestionDetails: function(questionId) {
							var question = findQuestion(questionId);
							
							var detailWin = Ext.create('Ext.window.Window', {
								title: 'Question Details',
								closeAction: 'destroy',
								modal: true,
								width: '95%',
								height: '60%',							
								draggable: false,
								maximizable: true,
								scrollable: true,
								bodyCls: 'text-readable',
								bodyStyle: 'padding: 10px;',
								listeners: {
									show: function() {        
										this.removeCls("x-unselectable");    
									}
								},								
								data: question,
								tpl: new Ext.XTemplate(
									'<tpl if="objective"><b>Question Objective:</b> <br><br>',
									'{objective}<br><br></tpl>',
									'<tpl if="narrative"><b>Narrative:</b><br><br>',
									'{narrative}</tpl>',
									'<tpl if="!objective && !narrative">No addtional details</tpl>'
								),
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												iconCls: 'fa fa-lg fa-close',
												handler: function() {
													detailWin.close();
												}
											},
											{
												xtype: 'tbfill'
											}
										]
									}
								]								
							});
							detailWin.show();
						},
						showScoreDetails: function(questionId) {
							var question = findQuestion(questionId);
							
							var detailWin = Ext.create('Ext.window.Window', {
								title: 'Scoring Details',
								closeAction: 'destroy',
								modal: true,
								width: '95%',
								height: '60%',							
								draggable: false,
								maximizable: true,
								scrollable: true,
								bodyCls: 'text-readable',
								bodyStyle: 'padding: 10px;',
								listeners: {
									show: function() {        
										this.removeCls("x-unselectable");    
									}
								},																
								data: question,
								tpl: new Ext.XTemplate(
									'<tpl if="scoreCriteria"><b>Scoring Criteria:</b> <br><br>',
									'{scoreCriteria}<br><br></tpl>',
									'<tpl if="!scoreCriteria">No addtional details</tpl>'
								),
								dockedItems: [
									{
										xtype: 'toolbar',
										dock: 'bottom',
										items: [
											{
												xtype: 'tbfill'
											},
											{
												text: 'Close',
												iconCls: 'fa fa-lg fa-close',
												handler: function() {
													detailWin.close();
												}
											},
											{
												xtype: 'tbfill'
											}
										]
									}
								]								
							});
							detailWin.show();
						}
					};
				} else {
					checklistPanel.setHidden(true);
				}
			};
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);
			
			
			
		}
		
		return null;		
	}

});

Ext.define('OSF.component.template.EvaluationChecklistRecommendation', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationChecklistRecommendation',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Evaluation Recommendations',
	bodyCls: 'text-readable',
	tpl: new Ext.XTemplate(
		' <table class="details-table" width="100%">',			
		'	<tpl for="checkListAll.recommendations">',	
		'		<tr class="details-table">',
		'			<td class="details-table" width="150"><b>{recommendationTypeDescription}</b></td>',
		'			<td class="details-table"><tpl if="securityMarkingType">({securityMarkingType}) </tpl>',
		'				<tpl if="recommendation">{recommendation}</tpl>',				
		'			</td>',		
		'			<tpl if="reason"><td class="details-table">',
		'				{reason}',						
		'			</td></tpl>',
		'		</tr>',
		'	</tpl>',
		'</table>'				
	),	
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		var recomendationPanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
			recomendationPanel.setHidden(true);
		} else {
			
			var updateSection = function(evaluation) {
				if (!evaluation.checkListAll ||
					!evaluation.checkListAll.recommendations) {
					recomendationPanel.setHidden(true);
				} else {				
					if (!evaluation.checkListAll.recommendations || evaluation.checkListAll.recommendations.length === 0) {
						recomendationPanel.setHidden(true);
					} else {
						recomendationPanel.update(evaluation);
					}
				}
			};
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);
			
		}
		
		return null;
	}

});

Ext.define('OSF.component.template.EvaluationChecklistScores', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.EvaluationChecklistScores',
	
	titleCollapse: true,
	collapsible: true,
	title: 'Reusability Factors (5=best)',
	tpl: new Ext.XTemplate(
		'<div class="rolling-container">',			
		'	<div class="rolling-container-row">',
		'		<tpl for=".">',	
		'			<div class="rolling-container-block">',
		'				<div class="detail-eval-item ">',
		'					<span class="detail-eval-label">{title} <tpl if="sectionDescription"><i class="fa fa-question-circle" data-qtip="{sectionDescription}" data-qtitle="{name}" data-qalignTarget="bl-tl" data-qclosable="true" ></i></tpl></span>',
		'					<span class="detail-eval-score" data-qtip="{average}">{display}</span>',	
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
		var scorePanel = this;
		
		if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
			scorePanel.setHidden(true);
		} else {
			
			var updateSection = function(evaluation) {			
				
				if (evaluation.checkListAll &&
					evaluation.checkListAll.responses) {
				
						Ext.Ajax.request({
						url: 'api/v1/resource/lookuptypes/EvaluationSection',
						success: function(response, opts){
							var sectionLookup = Ext.decode(response.responseText);

							var findSectionDesc = function(sectionKey) {
								var desc = null;
								Ext.Array.each(sectionLookup, function(lookup) {
									if (lookup.code === sectionKey) {
										desc = lookup.detailedDescription;
									}
								});
								return desc;
							};

							//group by section
							var groupStatus = {};				
							Ext.Array.each(evaluation.checkListAll.responses, function(response){
								if (groupStatus[response.question.evaluationSection]) {
									var stat = groupStatus[response.question.evaluationSection];						
									if (!response.notApplicable) {
										stat.count++;
										stat.totalScore += response.score;
									}
								} else {
									groupStatus[response.question.evaluationSection] = {
										title: response.question.evaluationSectionDescription,
										sectionDescription: findSectionDesc(response.question.evaluationSection),
										count: 1,									
										totalScore: response.score ? response.score : 0
									};
								}
							});		

							//average and add dots
							var sections = [];
							Ext.Object.eachValue(groupStatus, function(section) {
								if (isNaN(section.count)) {
									section.count = 0;
								}
								if (section.count > 0) {
									section.average = Math.round((section.totalScore/section.count)*10) / 10;

									var score = Math.round(section.average);
									section.display = "";
									for (var i= 0; i<score; i++){
										section.display += '<i class="fa fa-circle detail-evalscore"></i>';
									}								
								} else {
									section.average = 0;								
								}
								if (isNaN(section.average) || section.average < 1) {
									section.average = 0;
									section.display = 'N/A';
								}

								sections.push(section);
							});
							Ext.Array.sort(sections, function(a, b){
								return a.title.localeCompare(b.title);
							});

							scorePanel.update(sections);
						}
					});
					scorePanel.setHidden(false);					
				} else {
					scorePanel.setHidden(true);
				}
			};
			
			updateSection(entry.fullEvaluations[0]);
			if (!entry.evalListeners) {
				entry.evalListeners = [];
			} 
			entry.evalListeners.push(updateSection);
			
		}
		
		return null;
	}

});

Ext.define('OSF.component.template.LayoutTab', {
	extend: 'Ext.tab.Panel',
	alias: 'osf.widget.template.LayoutTab',
	
	tabBar: {
		defaults: {
			flex: 1
		},
		dock: 'top',
		layout: {
			pack: 'left'
		}
	},
	listeners: {
		afterrender: function(tabPanel) {
			//force all tabs to render at the first
			for(i=0; i<20; i++){
				 tabPanel.setActiveTab(i);
			}
			tabPanel.setActiveTab(0);
		}
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry, container) {
		var layoutPanel = this;
		layoutPanel.holderContainer = container;
		
		var itemsToClose = [];
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item) {
				var process = true;
				if (item.evalTopPanel){
					if (!entry.fullEvaluations || entry.fullEvaluations.length === 0) {
						process = false;
						itemsToClose.push(item);
					}
				} 						

				if (process && item.updateTemplate) {
					item.updateTemplate(entry);
				}
			}
		});
		
		Ext.Array.each(itemsToClose, function(item) {
			item.close();
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutScroll', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutScroll',
	
	scrollable: true,
	layout: 'anchor',
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutHbox', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutHbox',
	
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutVbox', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutVbox',
	
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutAccordion', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutAccordion',
	
	layout: {
        type: 'accordion',
        titleCollapse: true,
        animate: true,
        activeOnTop: false
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutFit', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutFit',
	
	layout: {
        type: 'fit'
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});

Ext.define('OSF.component.template.LayoutCenter', {
	extend: 'Ext.panel.Panel',
	alias: 'osf.widget.template.LayoutCenter',
		
	layout: {
        type: 'center'
	},
	
	initComponent: function () {
		this.callParent();
	},
	
	updateTemplate: function (entry) {
		var layoutPanel = this;
		
		Ext.Array.each(layoutPanel.items.items, function(item){
			if (item.updateTemplate) {
				item.updateTemplate(entry);
			}
		});
		
		return null;
	}
	
});