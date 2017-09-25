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
	Ext.require('OSF.landing.DefaultCategory');	
	Ext.require('OSF.landing.StaticInfo');	
	Ext.require('OSF.landing.DefaultInfo');	
	
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
									bodyStyle: 'padding-top: 80px; padding-bottom: 80px;'
								},
								{
									layout: {
										type: 'hbox',
										align: 'stretch'
									},
									items: [
										{
											xtype: 'osf-defaultsearchtools',
											bodyStyle: 'padding-bottom: 40px;',
											toolSize: 'small',
											width: '50%',
											//cls: 'search-tool-background',
											searchTools: [
												{
													text: 'Tags',
													tip: 'Search Tag Cloud',
													icon: 'fa-cloud',
													toolType: 'OSF.landing.TagCloud'
												},
												{
													text: '<span style="font-size: 12px;">Organizations</span>',
													tip: 'Search by Entry Organization',
													icon: 'fa-sitemap',
													toolType: 'OSF.landing.OrganizationSearch'
												},
												{
													text: '<span style="font-size: 12px;">Relationships</span>',
													tip: 'View relationships between entries',
													icon: 'fa-share-alt',
													toolType: 'OSF.landing.RelationshipSearch'
												},				
												{
													text: 'Advanced',
													tip: 'Create Advanced Searches',
													icon: 'fa-search-plus',
													toolType: 'OSF.landing.AdvancedSearch'
												}			
											]									
										},
										{
											xtype: 'osf-defaultactions',
											width: '50%',
											toolSize: 'small',
											//cls: 'action-tool-background',
											textCSSOverride: 'search-tool-button-inner-small',
											actionTools: [
												{
													text: 'Dashboard',
													icon: 'fa-dashboard',
													tip: 'Access your dashboard',
													//imageSrc: 'images/dash.png',
													toolBackground: 'action-tool-button-background',													
													link: 'UserTool.action?load=Dashboard'
												},
												{
													text: '<span style="font-size: 12px;">Submissions</span>',
													icon: 'fa-edit',
													//imageSrc: 'images/submission.png',
													tip: 'Add or update entries to the registry',
													permission: 'USER-SUBMISSIONS',
													toolBackground: 'action-tool-button-background',
													link: 'UserTool.action?load=Submissions'
												},
												{
													text: '<span style="font-size: 12px;">My Searches</span>',
													icon: 'fa-floppy-o',
													tip: 'View and manage your saved searches',
													//imageSrc: 'images/savedsearch.png',
													toolBackground: 'action-tool-button-background',
													link: 'UserTool.action?load=Searches'
												},		
												{
													text: 'Feedback',
													icon: 'fa-comments',
													tip: 'Provide feedback about the site',
													//imageSrc: 'images/feedback.png',
													toolBackground: 'action-tool-button-background',
													link: 'feedback.jsp'
												}		
											]
										}								
									]
								}									
							]
						},																
						{
							xtype: 'osf-staticinfo'
						},						
						{
							xtype: 'osf-defaultcategory'
						},
						{
							xtype: 'osf-defaultfooter',
							bodyStyle: 'padding-top: 20px;'							
						},
						{
							xtype: 'osf-defaultversion',
							bodyStyle: 'padding-bottom: 20px;'
						}	
					]
				}

			]
		});

	});	

</script>