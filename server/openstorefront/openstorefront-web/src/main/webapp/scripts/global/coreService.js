/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/* global Ext */

var CoreService = {

	userservice: {

		getCurrentUser: function (forceReload) {
			var userservice = this;
			var deferred = new Ext.Deferred();

			var haveUser = false;

			//page level cache (safe but, minimual impact as it only helps complex nested components)
			if (userservice.user && !forceReload) {
				deferred.resolve(userservice.user);
				haveUser = true;
			}

			//caching this causes a lot of headaches with edge cases
//		if (sessionStorage && sessionStorage.getItem('user')) {
//			if (!forceReload) {
//				deferred.resolve(Ext.decode(sessionStorage.getItem('user')));
//				haveUser = true;
//			}
//		}

			if (haveUser === false) {
				Ext.Ajax.request({
					url: 'api/v1/resource/userprofiles/currentuser',
					success: function (response, opts) {
						var user = Ext.decode(response.responseText);
						//sessionStorage.setItem('user', Ext.encode(user));
						//deferred.resolve(Ext.decode(sessionStorage.getItem('user')));
						userservice.user = user;
						deferred.resolve(user);
					},
					failure: function (response, opts) {
						deferred.reject("Error loading user.");
					}
				});
			}

			return deferred.promise;
		},
		clearUser: function () {
			sessionStorage.removeItem('user');
		},
		userHasPermission: function (user, permissions, operator) {
			var userservice = this;
			var valid = false;
			if (Ext.isArray(permissions)) {
				var operatorValue = operator ? operator.toUpperCase() : 'AND';
				if (operatorValue === 'OR') {
					Ext.Array.each(permissions, function (permission) {
						Ext.Array.each(user.roles, function (role) {
							Ext.Array.each(role.permissions, function (userpermission) {
								if (userpermission.permission === permission) {
									valid = true;
									return false;
								}
							});
							if (valid) {
								return false;
							}
						});
						if (valid) {
							return false;
						}
					});
				} else {
					var foundCount = 0;
					Ext.Array.each(permissions, function (permission) {
						var foundPermission = false;
						Ext.Array.each(user.roles, function (role) {
							Ext.Array.each(role.permissions, function (userpermission) {
								if (userpermission.permission === permission) {
									foundPermission = true;
									return false;
								}
							});
							if (foundPermission) {
								return false;
							}
						});
						if (foundPermission) {
							foundCount++;
						}
					});
					if (foundCount === permissions.length) {
						valid = true;
					}
				}
			} else {
				Ext.Array.each(user.roles, function (role) {
					Ext.Array.each(role.permissions, function (userpermission) {
						if (userpermission.permission === permissions) {
							valid = true;
							return false;
						}
					});
					if (valid) {
						return false;
					}
				});
			}
			return valid;
		}

	},
	systemservice: {

		getConfigProperties: function (key) {
			var systemService = this;
			var deferred = new Ext.Deferred();
			var urlKey = "";
			if (key !== undefined)
			{
				urlKey = '/' + key;
			}
			var promise = Ext.Ajax.request({
				url: 'api/v1/service/application/configproperties' + urlKey,
				success: function (response, opts) {
					var properties = Ext.decode(response.responseText);
					deferred.resolve(properties);
				},
				failure: function (response, opts) {
					deferred.reject("Error loading Properties.");
				}
			});

			return deferred.promise;
		},

		getSecurityPolicy: function () {
			var systemService = this;
			var deferred = new Ext.Deferred();

			Ext.Ajax.request({
				url: 'api/v1/resource/securitypolicy',
				success: function (response, opts) {
					var securityPolicy = Ext.decode(response.responseText);
					deferred.resolve(securityPolicy);
				},
				failure: function (response, opts) {
					deferred.reject("Error loading user.");
				}
			});

			return deferred.promise;
		}

	},
	brandingservice: {

		getCurrentBranding: function () {
			var brandingservice = this;
			var deferred = new Ext.Deferred();

			var haveBranding = false;
			if (brandingservice.branding) {
				deferred.resolve(brandingservice.branding);
				haveBranding = true;
			}

			if (haveBranding === false) {
				Ext.Ajax.request({
					url: 'api/v1/resource/branding/current',
					success: function (response, opts) {
						var branding = Ext.decode(response.responseText);
						brandingservice.branding = branding;
						deferred.resolve(branding);
					},
					failure: function (response, opts) {
						deferred.reject("Error loading user.");
					}
				});
			}

			return deferred.promise;
		},

		getBranding: function (brandingId) {

			var promise = Ext.Ajax.request({
				url: 'api/v1/resource/branding/' + brandingId
			});
			return promise;
		}
	},
	attributeservice: {
		attributeCache: [],

		/**
		 * Always call this to warm the cache before use
		 * Due to async nature this may not be ready when it's need.  If timing is critical use the promise.
		 * @returns promise
		 */
		warmCache: function () {
			var promise = Ext.Ajax.request({
				url: 'api/v1/resource/attributes',
				success: function (response, opts) {
					var attributes = Ext.decode(response.responseText);
					CoreService.attributeservice.attributeCache = attributes;
				}
			});
			return promise;
		},

		/**
		 * Translate type to description
		 * @param {type} attributeType
		 * @returns description
		 */
		translateType: function (attributeType) {
			var attributeCache = CoreService.attributeservice.attributeCache;

			var attributeTypeTranslated = attributeType;
			Ext.Array.each(attributeCache, function (type) {
				if (type.attributeType === attributeType) {
					attributeTypeTranslated = type.description;
				}
			});
			return attributeTypeTranslated;
		},

		/**
		 * Translate code to label
		 * @param {type} attributeType
		 * @param {type} attributeCode
		 * @returns label
		 */
		translateCode: function (attributeType, attributeCode) {
			var attributeCache = CoreService.attributeservice.attributeCache;

			var attributeCodeTranslated = attributeCode;
			Ext.Array.each(attributeCache, function (type) {
				if (type.attributeType === attributeType) {
					Ext.Array.each(type.codes, function (code) {
						if (code.code === attributeCode) {
							attributeCodeTranslated = code.label;
						}
					});
				}
			});
			return attributeCodeTranslated;
		},

		/**
		 * Translates a label into a key in consistent way.
		 * @param {type} label
		 * @returns promise
		 */
		labelToCode: function (label) {

			var promise = Ext.Ajax.request({
				url: 'api/v1/service/application/key',
				method: 'GET',
				params: {
					label: label
				}
			});
			return promise;
		}
	},
	iconservice: {

		/**
		 * This will return array of icon classes
		 * @returns {Ext.Deferred.promise}
		 */
		getAllIcons: function () {

			var deferred = new Ext.Deferred();


			//this will pull font awesome icons
			var currentFontAwesome = 'webjars/font-awesome/4.7.0/css/font-awesome.css';
			Ext.Ajax.request({
				url: currentFontAwesome,
				success: function (response, opts) {
					var iconsClasses = [];
					var lines = response.responseText.split('\n');
					Ext.Array.each(lines, function (line) {
						if (line.indexOf(':before') !== -1) {
							var iconCls = line.replace(':before {', '').replace('.', '').replace(':before,', '');
							iconsClasses.push({
								cls: iconCls,
								view: '<i class="fa ' + iconCls + '" ></i> ' + iconCls
							});
						}
					});
					iconsClasses.sort(function (a, b) {
						return a.cls.localeCompare(b.cls);
					});
					deferred.resolve(iconsClasses);
				},
				failure: function (response, opts) {
					deferred.reject("Error loading user.");
				}
			});

			return deferred.promise;
		}

	}

};

