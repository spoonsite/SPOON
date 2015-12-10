/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global Ext */

var CoreUtil = {
	showContextMenu: function (menu, event) {

		event.stopEvent();
		menu.showAt(0, 0);

		var avaliableHeight = Ext.getBody().getHeight();
		var avaliableWidth = Ext.getBody().getWidth();

		var maxHeight = avaliableHeight - menu.getHeight();
		var maxWidth = avaliableWidth - menu.getWidth();

		var showHeight = event.getY() + menu.getHeight();
		var showWidth = event.getX() + menu.getWidth();

		var x = event.getX();
		var y = event.getY();
		if (showHeight > maxHeight)
		{
			y = event.getY() - menu.getHeight();
		}

		if (showWidth > maxWidth)
		{
			x = event.getX() - menu.getWidth();
		}
		menu.showAt(x, y);
	},
	lookupStore: function (options) {

		var model = 'LookupDataModel';
		if (options.model)
		{
			model = options.model;
		}

		var autoLoad = true;
		if (options.autoLoad)
		{
			autoLoad = options.autoLoad;
		}

		var store;
		if (options.customStore) {
			store = Ext.create('Ext.data.Store', options.customStore);
		} else {
			store = Ext.create('Ext.data.Store', {
				model: model,
				autoLoad: autoLoad,
				proxy: {
					type: 'ajax',
					url: options.url
				}
			});
			store = Ext.applyIf(store, options);
			if (options.addRecords) {
				store.on('load', function (myStore, records, sucessful, opts) {
					myStore.add(options.addRecords);
				});
			}
		}

		return store;
	},
	popupMessage: function (title, message, delay) {
		if (delay)
		{
			delay = 1000;
		}

		//'t' top and 'b' , 'r' 'l' (See the Fx class overview for valid anchor point options)
		if (!CoreUtil.popupMessageMsgCt) {
			CoreUtil.popupMessageMsgCt = Ext.core.DomHelper.insertFirst(document.body, {id: 'msg-div'}, true);
		}
		var m = Ext.core.DomHelper.append(CoreUtil.popupMessageMsgCt, '<div class="popup-message"><h3>' + title + '</h3><p>' + message + '</p></div>', true);
		m.hide();
		m.slideIn('t').ghost('t', {delay: delay, remove: true});
	},
	/**
	 * Chain load the store and then the form...so the form loads correctly
	 * 
	 * options: 
	 * 
	 * Stores (Array of stores)
	 * Form (form to load)
	 * formLoadOpts
	 * Components to mask 
	 *  @param options
	 */
	chainedStoreLoad: function (options) {

		//mask componets
		if (options.maskComponents)
		{
			if (Ext.isArray(options.maskComponents))
			{
				for (var i = 0; i < options.maskComponents.length; i++) {
					options.maskComponents[i].setLoading(true);
				}
			}
			else
			{
				options.maskComponents.setLoading(true);
			}
		}

		var loadFormFunc = function () {
			var options = this;
			if (options.maskComponents)
			{
				if (Ext.isArray(options.maskComponents))
				{
					for (var i = 0; i < options.maskComponents.length; i++) {
						options.maskComponents[i].setLoading(false);
					}
				}
				else
				{
					options.maskComponents.setLoading(false);
				}
			}

			options.form.load(options.formLoadOpts);
		};

		//chain the stores
		if (Ext.isArray(options.stores))
		{
			for (var i = 0; i < options.stores.length; i++) {

				if (i === (options.stores.length - 1)) {
					options.stores[i].on('load', loadFormFunc, options);
				} else {
					options.stores[i].on('load', function () {
						this.load();
					}, options.stores[i + 1]);
				}

			}
		}
		else
		{
			options.stores.on('load', loadFormFunc, options);
		}
	},
	cloneStoreRecords: function (originalStore)
	{
		var newStore = Ext.create('Ext.data.Store', {
			model: originalStore.model,
			fields: originalStore.fields
		});

		originalStore.each(function (record) {
			newStore.add(record);
		});

		return newStore;
	},
	sessionStorage: function () {
		if (sessionStorage) {
			return sessionStorage;
		} else {
			CoreUtil.popupMessage('Session Storage', 'Not Supported');
			return null;
		}
	},
	localStorage: function () {
		if (localStorage) {
			return localStorage;
		} else {
			CoreUtil.popupMessage('Local Storage', 'Not Supported');
			return null;
		}	},
	removeBlankDataItem: function (originalData) {
		Ext.Object.each(originalData, function (key, value, data) {
			if (value === "") {
				delete data[key];
			}
		});
	},
	submitForm: function (options) {

		if (options.removeBlankDataItems) {
			Ext.Object.each(options.data, function (key, value, data) {
				if (value === "") {
					delete data[key];
				}
			});
		}

		var failurehandler = function (response, opts) {
			options.form.setLoading(false);

			var errorResponse = Ext.decode(response.responseText);
			var errorObj = {};
			Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
				errorObj[item.key] = item.value;
			});
			options.form.getForm().markInvalid(errorObj);
			options.failure(response, opts);
		};

		var loadingText = options.loadingText ? options.loadingText : 'Saving...';

		options.form.setLoading(loadingText);
		Ext.Ajax.request({
			url: options.url,
			method: options.method,
			jsonData: options.data,
			success: function (response, opts) {
				options.form.setLoading(false);
				if (response) {
					var data = Ext.decode(response.responseText);
					if ((data.success !== undefined && data.success !== null && data.success) ||
							data.success === undefined)
					{
						options.success(response, opts);
					} else {
						failurehandler(response, opts);
					}
				}
			},
			failure: function (response, opts) {
				failurehandler(response, opts);
			}
		});

	},
	pagingProxy: function (options) {
		var proxy = Ext.create('Ext.data.proxy.Ajax', {
			url: options.url,
			actionMethods: options.actionMethods ? options.actionMethods : {create: 'POST', read: 'GET', update: 'POST', destroy: 'POST'},
			params: options.params ? options.params : {},
			extraParams: options.extraParams ? options.extraParams : {},
			directionParam: 'sortOrder',
			sortParam: 'sortField',
			startParam: 'offset',
			limitParam: 'max',
			simpleSortMode: true,
			reader: options.reader ? options.reader : {type: 'json'}
		});

		return proxy;
	}


};