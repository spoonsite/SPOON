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

Ext.define('OSF.common.SecurityComponentMixin', { extend: 'Ext.Mixin' }, function (mixin) {

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
		//	@param permissionLogicalOperator (string) - OR indicates the user needs one or more permissions, AND indicates user needs ALL listed permissions
		//	@param actionOnInvalidPermissions (string) - indicates what should happen to the UI component when the user has insufficient permissions
		//	@param requiredPermissions (string) - permissions that are tied to the UI component
		//	@param afterPermissionCheck (function) - to be called after permissions have been handled on UI component
		mixins: [mixin],
		securityMixin: mixin,
		permissionLogicalOperator: 'OR',
		actionOnInvalidPermission: 'hide',
		requiredPermissions: [],
		permissionCheckSuccess: function () {},
		permissionCheckFailure: function () {},
		permissionsActionMap: {
			// @param method - method that will be executed
			// @invalidValue - value to be passed to the <method> for invalid permissions
			// @validValue - value to be passed to the <method> for valid permissions
			// @canCallByDefault (bool) - this indicates that the <method> should NOT be called on componentInit or as a valid permission call to <method>
			//		This will either destroy the component, or alter it in such a way that may cause undesired results.
			'hide': {
				method: 'setHidden',
				invalidValue: true,
				validValue: false,
				canCallByDefault: true
			},
			'disable': {
				method: 'setDisabled',
				invalidValue: true,
				validValue: false,
				canCallByDefault: true
			},
			'destroy': {
				method: 'destroy',
				invalidValue: null,
				validValue: null,
				canCallByDefault: false
			}
		},
		initComponent: function () {

			this.callParent();
			var uiComponent = this;

			if (typeof uiComponent.requiredPermissions !== 'undefined' && uiComponent.requiredPermissions.length > 0) {

				var actionMapObj = uiComponent.permissionsActionMap[uiComponent.actionOnInvalidPermission];
				if (actionMapObj === undefined) {
					console.error("The action \"" + uiComponent.actionOnInvalidPermission + "\" is invalid for permission handling.");
				}
				else {
					// given the actionOnInvalidPermission, if it has a valid permissionActionMap method... perform it.
					if (actionMapObj.canCallByDefault) {
						uiComponent[actionMapObj.method](actionMapObj.invalidValue);
					}
	
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
									uiComponent.handleComponentRender(false);
									uiComponent.setHidden(true);
								}
								else {
									uiComponent.handleComponentRender(true);
								}
								break;
	
							case 'AND': // user requires ALL specified permission
								if (matchedPermissions !== uiComponent.requiredPermissions.length) {
									uiComponent.handleComponentRender(false);
								}
								else {
									uiComponent.handleComponentRender(true);
								}
								break;
	
							default:
								console.error("The logical operator \"" + uiComponent.permissionLogicalOperator + "\" was used and is invalid!");
						}
					});
				}
			}
		},
		handleComponentRender: function (hasPermission) {

			var uiComponent = this;
			var actionMapObj = uiComponent.permissionsActionMap[uiComponent.actionOnInvalidPermission];

			if (!hasPermission) {
				uiComponent[actionMapObj.method](actionMapObj.invalidValue !== null ? actionMapObj.invalidValue : undefined);
				uiComponent.permissionCheckFailure();
			}
			// Don't run unless user has valid permissions, and the actionMapObj has a method that doesn't destroy the component.
			else if (hasPermission && actionMapObj.canCallByDefault) {
				uiComponent[actionMapObj.method](actionMapObj.validValue !== null ? actionMapObj.validValue : undefined);
				uiComponent.permissionCheckSucces();
			}
		}
	});
});
