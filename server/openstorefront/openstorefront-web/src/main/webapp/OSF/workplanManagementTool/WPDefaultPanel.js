/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanManagementTool.WPDefaultPanel', {
	extend: 'Ext.panel.Panel',

	getWpWindow: function () {
		return this.up('window');
	},

	alert: function (recipient) {
		if (recipient) {
			if (typeof this.getWpWindow()[recipient] !== 'undefined') {
				this.getWpWindow()[recipient].alertChange();
			}
			else if (recipient === 'window') {
				this.getWpWindow().alertChange();
			}
			else {
				console.warn('There is no component "' + recipient + '" that can be alerted.');
			}
		}
		else {
			this.getWpWindow().alertChildrenComponents();
		}
	}

});
