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

/* global Ext, CoreService */

Ext.define('OSF.landing.DefaultFooter', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf-defaultfooter',
	
	bodyCls: 'home-footer',	
	layout: 'center',
	width: '100%',
	items: [
		{
			xtype: 'panel',
			width: '100%',
			data: {},
			tpl: new Ext.XTemplate(
				'<table style="width: 100%;">',
				'	<tr>',
				'		<td style="flex: 1;padding-left: 30px;" valign="top"><img src="images/di2elogo-sm.png" /></td>',
				'		<td style="width: 300px;" class="home-footer-item">',
				'			<a href="https://devtools.di2e.net" rel="nofollow">DI2E Dev tools</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/Support">Contact Support</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/Our+Service+Commitment">Our Service Commitment</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/Onboarding">Our Onboarding Process</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/Privacy+Policy" class="current ancestor-link">Our Privacy Policy</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/Terms+of+Service">Our Terms of Service</a><br>',
				'			<a href="https://www.di2e.net/display/DI2E/International+Traffic+in+Arms+Regulations+%28ITAR%29+and+Controlled+Unclassified+Information+%28CUI%29+Guidance">Our ITAR and CUI Guidance</a><br>',				
				'		</td>',
				'	</tr>',				
				'</table>',				
				'<div style="text-align: center;" class="home-footer-item">',
				' <blockquote>One Registry for the IC, One Registry for the DOD, One Registry to bind them all</blockquote><br>',
				' © DI2E&nbsp;|&nbsp;<a href="https://www.di2e.net/display/DI2E/Consent+to+Monitoring">Consent to Monitoring</a> | <a href="https://www.di2e.net/display/DI2E/Rules+of+Behavior">Rules of Behavior</a> | <a href="https://www.di2e.net/display/DI2E/Approved+Use">Approved Use</a>&nbsp;|&nbsp;All rights reserved</div>'		
			)
		}
	],

	initComponent: function () {
		this.callParent();	
		var footerPanel = this;
		
		CoreService.brandingservice.getCurrentBranding().then(function(branding){
			//footerPanel.update(branding);
			//TODO: pull footer from branding;
		});
		
	}
	
});

