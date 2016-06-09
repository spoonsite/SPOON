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

Ext.define('OSF.component.StandardComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.StandardComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	width: 175,
	margin: '0 20 0 0',
	valueField: 'code',
	displayField: 'description',
	typeAhead: true,
	forceSelection: true,	
	queryMode: 'local',
	labelAlign: 'top',	
	
	initComponent: function() {
		var me = this;	
		
		if (me.storeConfig)
		{
			var cbStore = CoreUtil.lookupStore(me.storeConfig);
			me.store = cbStore;
		}
		
		this.callParent();
	}	
	
});

Ext.define('OSF.component.SecurityComboBox', {
    extend: 'Ext.form.field.ComboBox',
	alias: 'osf.widget.SecurityComboBox',
	
	emptyText: 'Select',
	labelSeparator: '',
	fieldLabel: 'Security Marking',
	name: 'securityMarkingType',
	width: '100%',	
	valueField: 'code',
	displayField: 'description',
	typeAhead: false,
	editable: false,
	forceSelection: true,	
	queryMode: 'local',
	labelAlign: 'top',
	store: {
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: '../api/v1/resource/lookuptypes/SecurityMarkingType'			
		}
	},	
	initComponent: function() {
		var me = this;	
		me.callParent();
	}	
	
});
