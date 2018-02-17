/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
Ext.define('OSF.customSubmissionTool.SectionNavPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-csf-sectionnavpanel',
	
	title: 'Sections',
	scrollable: true,
	
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			
			items: [
				{
					text: 'Add',
					iconCls: 'fa fa-lg fa-plus icon-button-color-save',
					handler: function() {
						
					}
				},
				{
					xtype: 'tbfill'
				},
				{
					text: 'Delete',
					iconCls: 'fa fa-lg fa-trash icon-button-color-warning',
					handler: function() {
						
					}					
				}
			]
		}
	],	
	initComponent: function () {		
		this.callParent();
		var sectionPanel = this;

		var items = [
			{
				xtype: 'treelist',				
				store: {
					root: {
						expanded: true,
						children: [
							{
								text: 'Instuctions',
								expanded: false,
								iconCls: 'fa fa-file-text',
								children: [
									{
										text: 'G-1. Company Name',													
										leaf: true
									},
									{
										text: 'G-2. Point of Contact',													
										leaf: true
									}
								]
							},
							{
								text: 'Software Description',
								expanded: false,
								iconCls: 'fa fa-file-text',
								children: [
									{
										text: 'P-1.  List the machines and/or services you require and machine resources.',													
										leaf: true
									},
									{
										text: 'P-2. List hardware attributes that would have a negative impact on the operation of your software, or identify hardware attributes that would have a optimize the operation of your software.',													
										leaf: true
									},
									{
										text: 'P-3. What operating system(s) does the software require?',												
										leaf: true
									}
								]
							}
						]
					}
				}
			}
		];
		
		sectionPanel.add(items);
		
	}
});

