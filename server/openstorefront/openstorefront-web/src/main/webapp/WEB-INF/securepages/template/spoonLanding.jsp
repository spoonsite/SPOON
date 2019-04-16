<%--
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 --%>

<script type="text/javascript">
	Ext.require('OSF.landing.DefaultHeader');
	Ext.require('OSF.landing.DefaultFooter');
	Ext.require('OSF.landing.DefaultSearch');
	Ext.require('OSF.landing.DefaultVersion');
	Ext.require('OSF.landing.DefaultSearchTools');
	Ext.require('OSF.landing.DefaultActions');
	Ext.require('OSF.landing.DefaultInfo');	
	Ext.require('OSF.landing.EntryTypeTopics');

	Ext.onReady(function(){

		

		Ext.create('Ext.container.Viewport', {			
			cls: 'home-viewport',
			layout: 'border',
			listeners: {
				resize: function(view, width, height, oldWidth, oldHeight, eOpts) {								
					view.updateLayout(true, true);
				}
			},			
			items: [								
				{
					xtype: 'osf-defaultheader'
				},
				{
					xtype: 'panel',
					region: 'center',
					scrollable: true,
					items: [	
						{
							xtype: 'panel',
							cls: 'home-backsplash',
							items: [
								{
									xtype: 'osf-defaultsearch',
									bodyStyle: 'padding-top: 80px; padding-bottom: 40px;'
								},
								{
									xtype: 'osf-defaultsearchtools',
									bodyStyle: 'padding-bottom: 40px;'
								}								
							]
						},		
						{
							xtype: 'osf-defaultactions'
						},
						{
							xtype: 'osf-defaultinfo'
							//margin: '0 0 40 0'
						},						
						{
							xtype: 'osf-entryTypeTopics'
						},
						{
							xtype: 'osf-defaultfooter',
							bodyStyle: 'padding-top: 20px;'							
						},
						{
							xtype: 'osf-defaultversion',
							bodyStyle: 'padding-bottom: 20px;'
						},
						{
							xtype: 'osf-defaultdisclaimer',
							bodyStyle: 'padding-bottom: 20px;',
							hidden:true,
							itemId: 'disclaimerPanel',
							beforeRender:function(){
								//if there is a disclaimer message, show the element
								var that = this;
								CoreService.brandingservice.getCurrentBranding().then(function(branding){
									if(branding.disclaimerMessage){
										that.up().queryById('disclaimerPanel').setVisible(true);
									}
								});
							}
						}
					]
				}

			]
		});

	});	

</script>