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

/* global Ext, URL */

var CoreUtil = {
	pageActions: {},
	toggleEventListener: function (event) {
		var el = event.target;

		// find the parent div
		var parentDiv = typeof el.parentElement.getElementsByTagName('section')[0] !== 'undefined' ? el.parentElement : el.parentElement.parentElement;
		var caret = parentDiv.getElementsByTagName('h3')[0].getElementsByTagName('div')[0];
		var section = parentDiv.getElementsByTagName('section')[0];

		// Toggle the class
		if (section.className === 'eval-visible-true') {
			section.className = 'eval-visible-false';
			caret.className = 'x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret';
			caret.setAttribute('data-qtip', 'Expand panel');
		} else {
			section.className = 'eval-visible-true';
			caret.className = 'x-tool-tool-el x-tool-img x-tool-expand-top eval-toggle-caret';
			caret.setAttribute('data-qtip', 'Collapse panel');
		}
	},
	calculateEvaluationScore: function (obj) {
		// obj.data.fullEvaluation requires the key: checkListAll
		var fullEvaluations = obj.fullEvaluations;
		var data = obj.data;
		var callBack = obj.success;

		Ext.Ajax.request({
			url: 'api/v1/resource/lookuptypes/EvaluationSection',
			success: function (response, opts) {
				var sectionLookup = Ext.decode(response.responseText);

				var findSectionDesc = function (sectionKey) {
					var desc = null;
					Ext.Array.each(sectionLookup, function (lookup) {
						if (lookup.code === sectionKey) {
							desc = lookup.detailedDescription;
						}
					});
					return desc;
				};

				for (var ii = 0; ii < fullEvaluations.length; ii += 1) {
					//group by section
					var groupStatus = {};
					Ext.Array.each(fullEvaluations[ii].checkListAll.responses, function (response) {
						if (groupStatus[response.question.evaluationSection]) {
							var stat = groupStatus[response.question.evaluationSection];
							if (!response.notApplicable) {
								stat.count++;
								stat.totalScore += response.score;
							}
						} else {
							groupStatus[response.question.evaluationSection] = {
								title: response.question.evaluationSectionDescription,
								sectionDescription: findSectionDesc(response.question.evaluationSection),
								count: 1,
								totalScore: response.score ? response.score : 0
							};
						}
					});


					//average and add dots
					var sections = [];
					Ext.Object.eachValue(groupStatus, function (section) {
						if (isNaN(section.count)) {
							section.count = 0;
						}
						if (section.count > 0) {
							section.average = Math.round((section.totalScore / section.count) * 10) / 10;

							var score = Math.round(section.average);
							section.display = "";
							for (var i = 0; i < score; i++) {
								section.display += '<i class="fa fa-circle detail-evalscore"></i>';
							}
						} else {
							section.average = 0;
						}
						if (isNaN(section.average) || section.average < 1) {
							section.average = 0;
							section.display = 'N/A';
						}

						sections.push(section);
					});
					Ext.Array.sort(sections, function (a, b) {
						return a.title.localeCompare(b.title);
					});

					fullEvaluations[ii].evaluationScores = sections;
					fullEvaluations[ii].evaluationCount = fullEvaluations.length;
				}

				// if obj.success was not defined, just return the data
				if (typeof obj.success !== 'undefined') {
					callBack({fullEvaluations: fullEvaluations}, data);
				} else {
					return {fullEvaluations: fullEvaluations};
				}
			}
		});
	},
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
		if (options.autoLoad !== undefined && options.autoLoad !== null)
		{
			autoLoad = options.autoLoad;
		}

		var store;
		if (options.customStore) {
			store = Ext.create('Ext.data.Store', options.customStore);
		} else {
			store = Ext.create('Ext.data.Store', Ext.applyIf({
				model: model,
				autoLoad: autoLoad,
				proxy: {
					type: 'ajax',
					url: options.url
				}
			}, options));
			if (options.addRecords) {
				store.on('load', function (myStore, records, sucessful, opts) {
					myStore.add(options.addRecords);
				});
			}
		}

		return store;
	},
	downloadCSVFile: function (filename, csvContent) {
		var charset = "utf-8";
		var blob = new Blob([csvContent], {
			type: "text/csv;charset=" + charset + ";"
		});
		if (window.navigator.msSaveOrOpenBlob) {
			window.navigator.msSaveBlob(blob, filename);
		} else {
			var link = document.createElement("a");
			if (link.download !== undefined) { // feature detection
				// Browsers that support HTML5 download attribute
				var url = URL.createObjectURL(blob);
				link.setAttribute("href", url);
				link.setAttribute("download", filename);
				link.style.visibility = 'hidden';
				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);
			}
		}
	},
	csvToHTML: function (csvData) {

		var lines = csvData.split("\n");
		var rows = [];
		var maxCols = 0;
		var htmlData = '';
		var foundHeaderFlag = 0;
		var lengthCheck = 3;
		var lengthCheckFlag = 0;

		var csv = Ext.util.CSV.decode(csvData);

		for (ctr = 0; ctr < csv.length; ctr++) {
			if (csv[ctr][0] === '' && csv[ctr].length === 1)
			{
				csv.splice(ctr, 1);
				continue;
			}
			if (csv[ctr].length > maxCols) {
				maxCols = csv[ctr].length;
			}
		}

		htmlData += '<html><head><style>' +
				' .reportview-table {border-collapse: collapse; border: 2px black solid; font: 12px sans-serif} ' +
				' tr.reportview-table:nth-child(odd) { background: white;} ' +
				' tr.reportview-table:nth-child(even) { background: whitesmoke;} ' +
				' .reportview-td{border: 1px black solid; padding:5px;}' +
				' .reportview-th{padding:5px;}' +
				'</style>' +
				'</head><body><table class="reportview-table">';

		for (ctr = 0; ctr < csv.length; ctr++) {
			for (ctr2 = 0; ctr2 < csv[ctr].length; ctr2++) {
				if (typeof csv[ctr][ctr2] === 'undefined') {
					csv[ctr][ctr2] = '&nbsp;';
				}

				var colDiff = maxCols - ctr2;
				if (ctr2 === 0) { //Start new row

					if ((ctr2 + 1) === csv[ctr].length) {
						if (colDiff !== 0) {
							htmlData += '<tr class="reportview-table"><td class="reportview-td" colspan="' + colDiff + '">' + csv[ctr][ctr2] + '</td></tr>';
						} else {
							htmlData += '<tr class="reportview-table"><td class="reportview-td" >' + csv[ctr][ctr2] + '</td></tr>';
						}
					} else {
						htmlData += '<tr class="reportview-table"><td class="reportview-td" >' + csv[ctr][ctr2] + '</td>';
					}


				}

				if ((ctr2 + 1) === csv[ctr].length) { //End row
					if (ctr2 !== 0) {
						if (colDiff !== 0) {
							htmlData += '<td class="reportview-td"  colspan="' + colDiff + '">' + csv[ctr][ctr2] + '</td></tr>';
						} else {
							htmlData += '<td class="reportview-td" >' + csv[ctr][ctr2] + '</td></tr>';
						}
					}
				} else if (ctr2 !== 0) { //Normal Data

					htmlData += '<td class="reportview-td" >' + csv[ctr][ctr2] + '</td>';
				}
			}
		}
		htmlData += '</table></body></html>';
		htmlData = htmlData.replace(/<td class="reportview-td" ><\/td>/g, '<td>&nbsp</td>');
		return htmlData;
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
			} else
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
				} else
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
		} else
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
		}
	},
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
			//If the request timesout then there won't be a response
			if (errorResponse) {
				var errorObj = {};
				if (errorResponse.errors !== undefined) {
					Ext.Array.each(errorResponse.errors.entry, function (item, index, entry) {
						errorObj[item.key] = item.value;
					});
				}
				options.form.getForm().markInvalid(errorObj);
			}
			if (options.failure) {
				options.failure(response, opts);
			}
		};

		var loadingText = options.loadingText ? options.loadingText : 'Saving...';

		if (!options.noLoadmask) {
			options.form.setLoading(loadingText);
		}
		Ext.Ajax.request({
			url: options.url,
			method: options.method,
			jsonData: options.data,
			callback: options.callback,
			success: function (response, opts) {
				options.form.setLoading(false);
				if (response) {
					if (response.status === 304) {
						options.success(response, opts);
					} else {
						if (response.responseText) {
							var data = Ext.decode(response.responseText);
							if ((data.success !== undefined && data.success !== null && data.success) ||
									data.success === undefined)
							{
								options.success(response, opts);
							} else {
								failurehandler(response, opts);
							}
						} else {
							//no response (assume success)
							options.success(response, opts);
						}
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
	},
	/**
	 *  Return predfined configs
	 * @param {additionalPlugins} additionalPlugins - a space-separated list (in a string) 
	 * of additional plugins to add to the config (optional)
	 * @returns {CoreUtil.tinymceConfig.defaultConfig}
	 */
	tinymceConfigNoMedia: function (additionalPlugins) {
		var defaultConfig = {
			plugins: [
				"advlist lists link charmap print preview hr anchor pagebreak",
				"searchreplace wordcount visualblocks visualchars code osffullscreen",
				"insertdatetime media nonbreaking save table contextmenu directionality",
				"emoticons template paste textcolor placeholder"
			],

			toolbar1: "formatselect | bold italic underline forecolor backcolor | bullist numlist | outdent indent | alignleft aligncenter alignright |  charmap | link table | osffullscreen | preview",

			content_css: "contents.css",
			browser_spellcheck: true,
			menubar: "edit format tools",
			statusbar: false,
			skin: 'openstorefront',
			toolbar_items_size: 'small',
			extended_valid_elements: 'img[data-storefront-ignore|src|border=0|alt|title|hspace|vspace|width|height|align|name]'
					+ ' table[class] td[class] th[class] tr[class]',
			table_default_styles: {border: 'solid 1px #ddd'},
			target_list: [ {title: 'New page', value: '_blank'} ],
			default_link_target: '_blank'
		};

		if (additionalPlugins) {
			defaultConfig.plugins.push(additionalPlugins);
		}

		return defaultConfig;
	},

	/**
	 *  Return predfined configs
	 * @param {additionalPlugins} additionalPlugins - a space-separated list (in a string) 
	 * of additional plugins to add to the config (optional)
	 * @returns {CoreUtil.tinymceConfig.defaultConfig}
	 */
	tinymceConfig: function (additionalPlugins) {
		var defaultConfig = {
			plugins: [
				"advlist lists link image charmap print preview hr anchor pagebreak",
				"searchreplace wordcount visualblocks visualchars code osffullscreen",
				"insertdatetime media nonbreaking save table contextmenu directionality",
				"emoticons template paste textcolor placeholder osfmediainserter osfvideoinserter"
			],

			toolbar1: "formatselect | bold italic underline forecolor backcolor | bullist numlist | outdent indent | alignleft aligncenter alignright | osfmediainserter osfvideoinserter charmap | link savedsearchlink table | osffullscreen | preview",

			content_css: "contents.css",
			browser_spellcheck: true,
			menubar: "edit format tools",
			statusbar: false,
			skin: 'openstorefront',
			toolbar_items_size: 'small',
			extended_valid_elements: 'img[data-storefront-ignore|src|border=0|alt|title|hspace|vspace|width|height|align|name]'
					+ ' table[class] td[class] th[class] tr[class]'
					+ ' a[href|target=_blank]',
			table_default_styles: {border: 'solid 1px #ddd'},
			target_list: [ {title: 'New page', value: '_blank'} ],
			default_link_target: '_blank',
			paste_data_images: true
		};

		if (additionalPlugins) {
			defaultConfig.plugins.push(additionalPlugins);
		}

		return defaultConfig;
	},
	/**
	 *  Return a config which includes the tinymce plugin/tools for the insertion of saved search links
	 * @param {additionalPlugins} additionalPlugins - a space-separated list (in a string) 
	 * @returns {CoreUtil.tinymceConfig.searchEntryConfig}
	 */
	tinymceSearchEntryConfig: function (additionalPlugins) {
		var searchEntryConfig = {
			plugins: [
				"advlist lists link image charmap print preview hr anchor pagebreak",
				"searchreplace wordcount visualblocks visualchars code osffullscreen",
				"insertdatetime media nonbreaking save table contextmenu directionality",
				"emoticons template paste textcolor placeholder savedsearchlink osfmediainserter osfvideoinserter"
			],

			toolbar1: "formatselect | bold italic underline forecolor backcolor | bullist numlist | outdent indent | alignleft aligncenter alignright | osfmediainserter osfvideoinserter charmap | link savedsearchlink table | osffullscreen | preview",

			content_css: "contents.css",
			browser_spellcheck: true,
			menubar: "edit format tools",
			statusbar: false,
			skin: 'openstorefront',
			toolbar_items_size: 'small',
			extended_valid_elements: 'img[data-storefront-ignore|src|border=0|alt|title|hspace|vspace|width|height|align|name]'
					+ ' table[class] td[class] th[class] tr[class]',
			table_default_styles: {border: 'solid 1px #ddd'},
			target_list: [ {title: 'New page', value: '_blank'} ],
			default_link_target: '_blank',
			paste_data_images: true

		};

		if (additionalPlugins) {
			searchEntryConfig.plugins.push(additionalPlugins);
		}

		return searchEntryConfig;

	},
	/**
	 * Defaults the search to wildcard
	 * @param {string} query 
	 * 
	 */
	searchQueryAdjustment: function (query) {
		if (query && !Ext.isEmpty(query)) {
			if (query.indexOf('"') === -1) {
				query = "*" + query + "*";
			}
		}
		return query;
	},
	/**
	 * Show related entries
	 * @param {type} attributeType
	 * @param {type} attributeCode
	 * @param {type} description
	 * @param {type} vitalType
	 * @param {type} tip
	 * @param {type} componentId
	 * @param {type} codeHasAttachment
	 * @returns {undefined}
	 */
	showRelatedVitalWindow: function (attributeType, attributeCode, description, vitalType, tip, componentId, codeHasAttachment) {

		var relatedStore = Ext.create('Ext.data.Store', {
			pageSize: 50,
			autoLoad: false,
			remoteSort: true,
			sorters: [
				new Ext.util.Sorter({
					property: 'name',
					direction: 'ASC'
				})
			],
			proxy: CoreUtil.pagingProxy({
				actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
				reader: {
					type: 'json',
					rootProperty: 'data',
					totalProperty: 'totalNumber'
				}
			}),
			listeners: {
				load: function (store, records) {
					store.filterBy(function (record) {
						return record.get('componentId') !== componentId;
					});
				}
			}
		});


		var attachmentLinkHTML = '';
		if (codeHasAttachment === 'true') {
			attachmentLinkHTML += '<p style="text-align: center;">' +
					'<i class="fa fa-paperclip"></i> ' +
					'<a href="api/v1/resource/attributes/attributetypes/{attributeType}' +
					'/attributecodes/{attributeCode}/attachment">' +
					'Download Attachment' +
					'</a></p>';
		}

		var relatedWindow = Ext.create('Ext.window.Window', {
			title: 'Related Entries',
			width: '95%',
			height: '60%',
			modal: true,
			draggable: false,
			maximizable: true,
			layout: 'fit',
			items: [
				{
					xtype: 'grid',
					itemId: 'grid',
					columnLines: true,
					store: relatedStore,
					columns: [
						{text: 'Name', dataIndex: 'name', flex: 2, minWidth: 250, cellWrap: true,
							renderer: function (value, meta, record) {
								return '<a class="details-table" href="view.jsp?id=' + record.get('componentId') + '&fullPage=true" target="_blank">' + value + '</a>';
							}
						},
						{text: 'Description', dataIndex: 'description', flex: 2,
							cellWrap: true,
							renderer: function (value) {
								value = Ext.util.Format.stripTags(value);
								return Ext.String.ellipsis(value, 300);
							}
						},
						{text: 'Type', align: 'center', dataIndex: 'componentTypeDescription', width: 150}
					],
					dockedItems: [
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							store: relatedStore,
							displayInfo: true
						},
						{
							xtype: 'panel',
							itemId: 'description',
							maxHeight: 200,
							bodyStyle: 'padding-left: 5px; padding-right: 5px;',
							scrollable: true,
							tpl: new Ext.XTemplate(
									'<h2 style="text-align: center;">{description}</h2>',
									attachmentLinkHTML,
									'<hr>',
									'{tip}'
									)
						}
					]
				}
			]
		});



		relatedWindow.getComponent('grid').getComponent('description').update({
			description: description,
			attributeCode: attributeCode,
			attributeType: attributeType,
			tip: tip
		});

		relatedWindow.show();

		var searchObj = {
			"sortField": "name",
			"sortDirection": "ASC",
			"searchElements": [{
					"searchType": vitalType,
					"keyField": attributeType,
					"keyValue": attributeCode,
					"caseInsensitive": true,
					"numberOperation": "EQUALS",
					"stringOperation": "EQUALS",
					"mergeCondition": "OR"
				}]
		};

		var store = relatedWindow.getComponent('grid').getStore();
		store.getProxy().buildRequest = function (operation) {
			var initialParams = Ext.apply({
				paging: true,
				sortField: operation.getSorters()[0].getProperty(),
				sortOrder: operation.getSorters()[0].getDirection(),
				offset: operation.getStart(),
				max: operation.getLimit()
			}, operation.getParams());
			params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

			var request = new Ext.data.Request({
				url: 'api/v1/service/search/advance',
				params: params,
				operation: operation,
				action: operation.getAction(),
				jsonData: Ext.util.JSON.encode(searchObj)
			});
			operation.setRequest(request);

			return request;
		};
		store.loadPage(1);

	},

	/**
	 * Sort and transfer entry for display
	 * @param {type} entry (componentAll)
	 */
	processEntry: function (entry) {

		//sort and process						
		Ext.Array.sort(entry.resources, function (a, b) {
			return a.resourceTypeDesc.localeCompare(b.resourceTypeDesc);
		});

		Ext.Array.sort(entry.contacts, function (a, b) {
			return a.name.localeCompare(b.name);
		});

		Ext.Array.sort(entry.dependencies, function (a, b) {
			return a.dependencyName.localeCompare(b.dependencyName);
		});

		var vitals = [];
		if (entry.attributes) {
			
			//group attributes by type; array of values
			var typeMap = {};
			Ext.Array.each(entry.attributes, function (item) {
				if (!typeMap[item.type]) {
					typeMap[item.type] = [];
				}
				typeMap[item.type].push(item);				
			});
			
			Ext.Object.each(typeMap, function(key, values) {				
				var maintype = values[0];
				
				
				var topHighLightStyle = null;
				if (values.length === 1) {
					topHighLightStyle = values[0].highlightStyle;
				} 
				
				//if 1 then set to fill background 
				//if more than one then style the codes 
				
				var processedValues = '';
				var baseUnitValues = '';
				var commonComment = null;
				var anyPrivate = false;
				var unit = '';
				var allTips = '';
				Ext.Array.each(values, function (item) {
					if (item.privateFlag) {
						anyPrivate = true;
					}
					if (item.comment) {
						// (ONLY allow one comment per type)
						//take the last non-null one
						commonComment = item.comment;
					}
					var tip = item.codeLongDescription ? Ext.util.Format.escape(item.codeLongDescription).replace(/"/g, '').replace(/'/g, '').replace(/\n/g, '').replace(/\r/g, '') : item.codeLongDescription;
				
					
					var codeValue =	item.codeDescription;					
					if (item.preferredUnit) {
						unit = item.preferredUnit.unit; 
						codeValue = item.preferredUnit.convertedValue;
					} else if (item.unit){
						unit = item.unit;
					}					
					
					if (tip && tip !== '') {
						allTips += '<b>' + codeValue + '</b>: ' + tip + '<br>';
					}
					
					var unitToShow = '';
					if (unit) {
						unitToShow =  ' ' + unit + '';
					}
					
					if(unitToShow != ''){
						if (Ext.isIE) {
							unitToShow = " " + unitToShow;
						} else {
							unitToShow = " " + CoreUtil.asciiToKatex(unitToShow, false);
						}
					}
					processedValues += '<b>' + codeValue + unitToShow + '</b>';					
					processedValues += ', ';		
					baseUnitValues += '<b>' + item.codeDescription + (item.unit ? ' ' + item.unit : '') + '</b>';					
					baseUnitValues += ', ';	
					
				});
				processedValues = processedValues.substring(0, processedValues.length-2);
	
				vitals.push({
					label: maintype.typeDescription,
					value: processedValues,
					baseUnitValue: baseUnitValues,
					highlightStyle: topHighLightStyle,
					type: maintype.type,
					code: maintype.code,
					unit: maintype.unit,
					privateFlag: anyPrivate,
					comment: commonComment,
					updateDts: maintype.updateDts,
					securityMarkingType: maintype.securityMarkingType,
					tip: allTips
				});				
				
			});			
			
		}

		Ext.Array.sort(vitals, function (a, b) {
			return a.label.localeCompare(b.label);
		});
		entry.vitals = vitals;

		Ext.Array.each(entry.evaluation.evaluationSections, function (section) {
			if (section.notAvailable || section.actualScore <= 0) {
				section.display = "N/A";
			} else {
				var score = Math.round(section.actualScore);
				section.display = "";
				for (var i = 0; i < score; i++) {
					section.display += '<i class="fa fa-circle detail-evalscore"></i>';
				}
			}
		});


		Ext.Array.sort(entry.evaluation.evaluationSections, function (a, b) {
			return a.name.localeCompare(b.name);
		});


		Ext.Array.each(entry.reviews, function (review) {
			Ext.Array.sort(review.pros, function (a, b) {
				return a.text.localeCompare(b.text);
			});
			Ext.Array.sort(review.cons, function (a, b) {
				return a.text.localeCompare(b.text);
			});

			review.ratingStars = [];
			for (var i = 0; i < 5; i++) {
				review.ratingStars.push({
					star: i < review.rating ? (review.rating - i) > 0 && (review.rating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
				});
			}
		});

		if (entry.attributes && entry.attributes.length > 0) {
			var evalLevels = {};
			Ext.Array.each(entry.attributes, function (item) {
				if (item.type === 'DI2ELEVEL') {
					evalLevels.level = {};
					evalLevels.level.typeDesciption = item.typeDescription;
					evalLevels.level.code = item.code;
					evalLevels.level.label = item.codeDescription;
					evalLevels.level.description = item.codeLongDescription;
					evalLevels.level.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}
				} else if (item.type === 'DI2ESTATE') {
					evalLevels.state = {};
					evalLevels.state.typeDesciption = item.typeDescription;
					evalLevels.state.code = item.code;
					evalLevels.state.label = item.codeDescription;
					evalLevels.state.description = item.codeLongDescription;
					evalLevels.state.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}
				} else if (item.type === 'DI2EINTENT') {
					evalLevels.intent = {};
					evalLevels.intent.typeDesciption = item.typeDescription;
					evalLevels.intent.code = item.code;
					evalLevels.intent.label = item.codeDescription;
					evalLevels.intent.description = item.codeLongDescription;
					evalLevels.intent.highlightStyle = item.highlightStyle;
					if (item.updateDts > entry.lastViewedDts) {
						updated = true;
					}
				}
			});
			entry.evalLevels = evalLevels;
		}


		return entry;
	},
	securityBannerPanel: function (branding) {

		if (branding && branding.securityBannerText) {
			var securityBanner = Ext.create('Ext.panel.Panel', {
				bodyCls: 'security-banner',
				dock: 'top',
				width: '100%',
				html: branding.securityBannerText
			});
			return securityBanner;
		}
		return null;
	},
	descriptionOfAdvancedSearch: function (searchElements) {
		if (searchElements) {
			var desc = '';
			var count = 0;

			Ext.Array.each(searchElements, function (element) {

				if (element.searchType) {
					desc += '<b>Search Type: </b>' + element.searchType + '<br>';
				}
				if (element.field) {
					desc += '<b>Field: </b>' + element.field + '<br>';
				}
				if (element.value) {
					desc += '<b>Value: </b>' + element.value + '<br>';
				}
				if (element.keyField) {
					desc += '<b>Key Field: </b>' + element.keyField + '<br>';
				}
				if (element.keyValue) {
					desc += '<b>Key Value: </b>' + element.keyValue + '<br>';
				}
				if (element.startDate) {
					desc += '<b>Start Date: </b>' + element.startDate + '<br>';
				}
				if (element.endDate) {
					desc += '<b>End Date: </b>' + element.endDate + '<br>';
				}
				if (element.caseInsensitive) {
					desc += '<b>Case Insensitive: </b>' + element.caseInsensitive + '<br>';
				}
				if (element.numberOperation) {
					desc += '<b>Number Operation: </b>' + element.numberOperation + '<br>';
				}
				if (element.stringOperation) {
					desc += '<b>String Operation: </b>' + element.stringOperation + '<br>';
				}

				if (count !== 0) {
					desc += '<br>' + element.mergeCondition + '<br><br>';
				}

				count++;
			});
			return desc;
		}
		return '';
	},
	actionSubComponentToggleStatus: function (grid, idField, entity, subEntityId, subEntity, forceDelete, successFunc) {
		var status = grid.getSelection()[0].get('activeStatus');
		var recordId = grid.getSelection()[0].get(idField);
		var componentId = grid.getSelection()[0].get('componentId');
		if (!componentId) {
			if (grid.componentRecord) {
				componentId = grid.componentRecord.get('componentId');
			} else {
				componentId = grid.componentId;
			}
		}
		subEntityId = subEntityId ? '/' + grid.getSelection()[0].get(subEntityId) : '';
		subEntity = subEntity ? '/' + subEntity : '';

		var urlEnding = '';
		var method = 'DELETE';

		if (status === 'I') {
			urlEnding = '/activate';
			method = 'PUT';
		}

		if (forceDelete)
		{
			urlEnding = '/force';
			method = 'DELETE';
		}

		grid.setLoading('Updating status...');
		Ext.Ajax.request({
			url: 'api/v1/resource/components/' + componentId + '/' + entity + '/' + recordId + subEntity + subEntityId + urlEnding,
			method: method,
			callback: function (opt, success, response) {
				grid.setLoading(false);
			},
			success: function (response, opts) {
				if (successFunc) {
					successFunc();
				} else {
					grid.getStore().reload();
				}
			}
		});
	},
	showSavedSearchWindow: function (searchId) {
		var searchWin = Ext.create('OSF.component.SearchPopupResultsWindow', {
			closeAction: 'destroy',
			alwaysOnTop: true
		});
		searchWin.showResults(searchId);

	},
	renderer: {

		booleanRenderer: function (value, meta, record) {
			if (value) {
				meta.tdCls = 'alert-success';
				return '<i class="fa fa-check"></i>';
			} else {
				meta.tdCls = 'alert-danger';
				return '<i class="fa fa-close"></i>';
			}
		}
	},
	split: function(text, split) {
		var tokens = [];
		if (text && text.length > 0){
			var token = '';
			for (var i=0; i<text.length; i++) {
				var char = text.charAt(i);
				if (Ext.Array.contains(split, char)) {
					if (token !== '') {
						tokens.push(token);						
					}
					token = '';					
				} else {
					token += char;
				}
			}
			if (token !== '') {
				tokens.push(token);						
			}
		}		
		return tokens;
	},
	emailValidateStrict: function(email) {
		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)) {  
			return true;
		}      
		return false;		
	},
	validateNumber: function(value) {
		var valid = true;
		var msg = '';

		if (Number(value)
			&& typeof value === 'string'
			&& Ext.String.endsWith(value.trim(), ".")
			) {
			valid = false;
			msg = 'Number must not have a decimal point or have at least one digit after the decimal point.';
		}
		try {
			var valueNumber = Number(value);
			if (isNaN(valueNumber)) {						
				valid = false;
				msg = "Value must be a valid number.";
			}
		} catch (e) {
			valid = false;
			msg = 'Number must not have a decimal point or have at least one digit after the decimal point.';
		}
		return { valid: valid, msg : msg };
	},
	traverseNestedModel: function(node, parents, target) {
		if (!node) return;
		if (target.componentType === node.componentType.componentType) {
			parents.push({
				'label': node.componentType.label,
				'componentType': node.componentType.componentType
			});
			target.parents = parents;
			return;
		}
		if (node.children.length > 0) {
			parents.push({
				'label': node.componentType.label,
				'componentType': node.componentType.componentType
			});
			Ext.Array.forEach(node.children, function(node) {
				//deep copy of parents for recursive call
				CoreUtil.traverseNestedModel(node, JSON.parse(JSON.stringify(parents)), target);
			});
		}
	},
	saveAdvancedComponentSearch: function(componentId) {
		var searchObj = {
			"sortField": null,
			"sortDirection": "ASC",
			"startOffset": 0,
			"max": 2147483647,
			"searchElements": [{
					"searchType": "ENTRYTYPE",
					"field": "componentType",
					"value": componentId,
					"searchChildren": true,
					"keyField": null,
					"keyValue": null,
					"startDate": null,
					"endDate": null,
					"caseInsensitive": false,
					"numberOperation": "EQUALS",
					"stringOperation": "EQUALS",
					"mergeCondition": "OR"  //OR.. NOT.. AND..
				}]
		};

		var searchRequest = {
			type: 'Advance',
			query: searchObj
		};

		CoreUtil.sessionStorage().setItem('searchRequest', Ext.encode(searchRequest));
	},
	uuidv4: function() {
	  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	  });
	},
	/**
	 * Returns HTML that beautifies units
	 * 
	 * @param {String} str input asciimath string to pass to katex
	 * @param {Boolean} block configure katex block element
	 * @returns render katex html
	 */
	asciiToKatex: function (str, block) {
		if (str !== undefined) {

			// if block == true it will return a block element, else it will return an inline element
			// regex for placing quotes in str before parsing to katex to ensure sub-units are grouped right 
			// ex: kg/m -> "kg"/"m"
			const regex = new RegExp("([a-zA-Z$]{1,})+", "gu");
			str = str.replace(regex, '"$&"');

			// This is for a specific JScience case where ^1/2 is resolved as 1:2, 
			// this is not a recognized ascii math method so it is changed 
			// back to 1/2 so katex can understand it.
			const JScienceRegex = new RegExp("((\\d+):(\\d+))+", "g");
			str = str.replace(JScienceRegex, '($2/$3)');

			// Katex does not allow $ when converting so this is the way 
			// of fixing that edge case. (cases like this should not 
			// be in the database though)
			if (str == '"$"') {
				return ("$");
			}

			// method for converting ascii to katex
			try {
				var katex = Window.renderAsciiMath(str, { displayMode: !!block, throwOnError: true })
				return katex;
			} catch (err) {
				return err;
			}
			
		} else {
			return "";
		}
	},

	/**
	 * This method will change a string that is a number from scientific notation to decimal notation.
	 * https://gist.github.com/jiggzson/b5f489af9ad931e3d186
	 * 
	 * @param {String} number 
	 * @returns {String} Number in decimal notation.
	 */
	scientificToDecimal: function(number) {
		let numberHasSign = number.startsWith("-") || number.startsWith("+");
		let sign = numberHasSign ? number[0] : "";
		number = numberHasSign ? number.replace(sign, "") : number;
	  
		//if the number is in scientific notation remove it
		if (/\d+\.?\d*e[\+\-]*\d+/i.test(number)) {
		  let zero = '0';
		  let parts = String(number).toLowerCase().split('e'); //split into coeff and exponent
		  let e = parts.pop();//store the exponential part
		  let l = Math.abs(e); //get the number of zeros
		  let sign = e / l;
		  let coeff_array = parts[0].split('.');
	  
		  if (sign === -1) {
			coeff_array[0] = Math.abs(coeff_array[0]);
			number = zero + '.' + new Array(l).join(zero) + coeff_array.join('');
		  } else {
			let dec = coeff_array[1];
			if (dec) l = l - dec.length;
			number = coeff_array.join('') + new Array(l + 1).join(zero);
		  }
		}
		return sign + number;
	},

	/**
	 * This method will check if the inputNumber is actually a number
	 * and if it is, it will reduce it's length in a meaningful way.
	 * ParseFloat info found at https://stackoverflow.com/questions/3612744/remove-insignificant-trailing-zeros-from-a-number
	 * 
	 * @param {String} inputNumber 
	 * @returns {String} The modified string if able to modify, otherwise return original inputNumber
	 */
	crushNumericString: function(inputNumber) {

		// If inputNumber is not a number return.
		if(isNaN(inputNumber)){
			return inputNumber;
		}
		// If it contains an E or e don't touch it and return.
		if(inputNumber.indexOf('E') != -1){
			inputNumber = CoreUtil.scientificToDecimal(inputNumber);
		}
		if(inputNumber.indexOf('e') != -1){
			inputNumber = CoreUtil.scientificToDecimal(inputNumber);
		}
		
		var magnitudeIsGreaterThanOne = false;
		var numberLength = inputNumber.length;

		// Is the absolute value of this number bigger than one?
		if(Math.abs(inputNumber) > 1){
			magnitudeIsGreaterThanOne = true;
		}

		// If it is take this route
		if(magnitudeIsGreaterThanOne){
			if(inputNumber.indexOf('.') != -1){
				if((numberLength - inputNumber.indexOf('.')) > 5){
					return parseFloat(parseFloat(inputNumber.slice(0, inputNumber.indexOf('.') + 4)).toFixed(3));
				}
				return parseFloat(inputNumber);
			}
		}

		// Otherwise take this route
		if(!magnitudeIsGreaterThanOne){
			// Find first non zero thing after the decimal and show 3 decimal places after it.
			var firstNonZeroIndex;
			for(var i = 0; i < numberLength; i++){
				if((inputNumber[i] == '-') || (inputNumber[i] == '.') || (inputNumber[i] == '0')){
					continue;
				}
				firstNonZeroIndex = i;
				break;
			}
			if(numberLength - firstNonZeroIndex > 5){
				return parseFloat(parseFloat(inputNumber.slice(0, firstNonZeroIndex + 4)).toFixed(firstNonZeroIndex + 1));
			}
			return parseFloat(inputNumber);
		}
		return parseFloat(inputNumber);
	},

	/**
	 * Creates a window to contact the vendor of an entry
	 * 
	 * @param {string} sendToEmail : The email to send the message to
	 * @returns void : It creates the contact vendor window
	 */
	showContactVendorWindow: function (sendToEmail) {
		CoreService.userservice.getCurrentUser().then(function (currUser) {

			// Users logging in through a service account should be able to change the email
			serviceAccounts = ["nasaames_serviceacct"];
			var isServiceAccount = false;
			if(serviceAccounts.indexOf(currUser.username) != -1){
				isServiceAccount = true;
			}

			var contactVendorWindow = Ext.create('Ext.window.Window', {
				title: 'Contact Vendor',
				width: 600,
				bodyPadding: 10,
				items: [
					{
						xtype: 'form',
						items: [
							{
								xtype: 'textfield',
								name: 'email',
								fieldLabel: 'From:',
								allowblank: false,
								width: '97%',
								vtype: 'email',
								disabled: (serviceAccounts.indexOf(currUser.username) != -1) ? false : true,
								value: isServiceAccount ? '' : currUser.email
							},
							{
								xtype: 'textareafield',
								grow: true,
								name: 'message',
								fieldLabel: 'Message',
								allowBlank: false,
								width: '97%',
								height: 200
							}
						]
					}
				],
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'bottom',
						border: false,
						items: [
							{
								text: 'Send',
								formBind: true,
								iconCls: 'fa fa-lg fa-envelope-o icon-button-color-save',
								handler: function () {
									var win = this.up().up();
									var form = win.down('form');
									var values = form.getForm().getValues();
									if (values.email === undefined) {
										values.email = currUser.email;
									}
									if (values.email.length < 1 || values.message.length < 1) {
										Ext.toast('Unable to send message, Please check for blank fields.');
									} else {
										var data = {
											userToEmail: sendToEmail,
											userFromEmail: values.email,
											message: values.message
										}
										Ext.toast('Message queued for sending.');
										contactVendorWindow.close();
										Ext.Ajax.request({
											url: 'api/v1/service/notification/contact-vendor',
											method: 'POST',
											jsonData: data,
											success: function (response, opts) {
												if (response.responseText == "") {
													Ext.toast('Sent message successfully<br> Individual email delivery success will depend on the email servers.');
												} else {
													Ext.toast('Message failed to send');
												}
												form.getForm().reset();
											},
											failure: function (response, opts) {
												Ext.toast('Message failed to send');
												form.getForm().reset();
											}
										});
									}
								}
							},
							{
								xtype: 'tbfill'
							},
							{
								text: 'Cancel',
								iconCls: 'fa fa-lg fa-close icon-button-color-warning',
								handler: function () {
									var win = this.up().up();
									var form = win.down('form');
									form.getForm().reset();
									contactVendorWindow.close();
								}
							}
						]
					}]
			});
			contactVendorWindow.show();
		})
	}
};
