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

/*
 * This is a general use mixin that will hide a component given a
 * required permission is specified and is not available for the
 * current user.
 * 
 * @author cyearsley
 */

Ext.define('OSF.common.SecurityComponentMixin', {
	extend: 'Ext.Mixin',

	mixinConfig: {
		after: {
			beforeRender: 'checkPermissions'
		},
	},
	checkPermissions: function () {

		var uiComponent = this;
		if (typeof uiComponent.requiredPermissions !== 'undefined' && uiComponent.requiredPermissions.length > 0) {

			CoreService.userservice.getCurrentUser().then(function (user) {
				
				var userPermissions = uiComponent.securityMixin.userPermissions;
				var matchedPermissions = 0;
				
				Ext.Array.forEach(uiComponent.requiredPermissions, function (permission) {
					if (userPermissions.indexOf(permission) !== -1) {
						matchedPermissions += 1;
					}
				});

				switch (uiComponent.permissionLogicalOperator) {
					case 'OR': // user only requires one of the specified permissions
						if (matchedPermissions === 0) {
							uiComponent.handleComponentRender();
						}
						break;

					case 'AND': // user requires ALL specified permission
						if (matchedPermissions !== uiComponent.requiredPermissions.length) {
							uiComponent.handleComponentRender();
						}
						break;

					default:
						console.error("The logical operator \"" + uiComponent.permissionLogicalOperator + "\" was used and is invalid!");
				}
			});

		}
	}
}, function (mixin) {

	var userPermissions = [];
	CoreService.userservice.getCurrentUser().then(function (user) {

		Ext.Array.forEach(user.roles, function (role) {

			userPermissions = userPermissions.concat(role.permissions);
		});
		Ext.Array.forEach(userPermissions, function (item, index) {

			userPermissions[index] = item.permission;
		});

		mixin.userPermissions = userPermissions;

	});

	// if this has an effect of performance, make the overrides more specific
	Ext.override(Ext.Component, {
		mixins: [mixin],
		securityMixin: mixin,
		permissionLogicalOperator: 'OR',
		actionOnInvalidPermission: 'hide',
		handleComponentRender: function () {

			var uiComponent = this;
			switch (uiComponent.actionOnInvalidPermission) {
				case 'hide':
					if (uiComponent.setHidden) {
						uiComponent.setHidden(true);
					}
					else {
						console.warn(uiComponent, "\nThe specified component does not support being hidden.");
					}
					break;

				case 'disable':
					if (uiComponent.setDisabled) {
						uiComponent.setDisabled(true);
					}
					else {
						console.warn(uiComponent, "\nThe specified component does not support being disabled.");
					}
					break;

				default:
					console.error("The action \"" + uiComponent.actionOnInvalidPermission + "\" is invalid for permission handling.");
			}
		}
	});
});
