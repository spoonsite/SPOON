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

/* global Ext */

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
		block.update(data);
	}
	
});

Ext.define('OSF.component.template.Description', {
	extend: 'OSF.component.template.BaseBlock',
	alias: 'osf.widget.template.Description',
	
	tpl: new Ext.XTemplate(
		'<h2>Description</h2>',	
		'{description}'	
	),
		
	initComponent: function () {
		this.callParent();
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
		'			<td class="details-table"><b>{resourceTypeDesc}</b></td>',
		'			<td class="details-table"><a href="actualLink" class="details-table" target="_blank">{link}</a></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'		
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.resources) {
			this.setHidden(true);
		}		
		Ext.Array.sort(entry.resources, function(a, b){
			return a.resourceTypeDesc.localeCompare(b.resourceTypeDesc);	
		});		
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
		'			<td class="details-table"><b>{name}</b> <br> ({organization})</td>',
		'			<td class="details-table">{positionDescription}</td>',
		'			<td class="details-table"><tpl if="phone">{phone)</tpl><tpl if="!phone">—</tpl></td>',
		'			<td class="details-table"><a href="mailto:{email}" class="details-table">{email}</a></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.contacts) {
			this.setHidden(true);
		}
		Ext.Array.sort(entry.contacts, function(a, b){
			return a.name.localeCompare(b.name);	
		});				
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
		'			<td class="details-table highlight-{highlightStyle}" data-qtip="{tip}" data-qtitle="{value}"><a href="#" class="details-table" onclick="">{value}</a></td>',
		'		</tr>',
		'	</tpl>',
		'</table>'		
	),
		
	initComponent: function () {
		this.callParent();
	},
	
	updateHandler: function(entry){
		if (!entry.attributes && !entry.metadata) {
			this.setHidden(true);
		}
		
		//normalize and sort
		var vitals = [];
		Ext.Array.each(entry.attributes, function(item){
			vitals.push({
				label: item.typeDescription,
				value: item.codeDescription,
				highlightStyle: item.highlightStyle,
				type: item.type,
				code: item.code,
				tip: item.codeLongDescription
			});
		});
		
		Ext.Array.each(entry.metadata, function(item){
			vitals.push({
				label: item.label,
				value: item.value
			});			
		});
		
		Ext.Array.sort(vitals, function(a, b){
			return a.label.localeCompare(b.label);	
		});
		entry.vitals = vitals;
		
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
		'			<td class="details-table"><b>{dependencyName} {version}</b> <br>',
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
		if (!entry.dependencies) {
			this.setHidden(true);
		}
		Ext.Array.sort(entry.dependencies, function(a, b){
			return a.dependencyName.localeCompare(b.dependencyName);	
		});				
		return entry;
	}	
	
});