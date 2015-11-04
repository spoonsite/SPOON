/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	displayField: 'desc',
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
