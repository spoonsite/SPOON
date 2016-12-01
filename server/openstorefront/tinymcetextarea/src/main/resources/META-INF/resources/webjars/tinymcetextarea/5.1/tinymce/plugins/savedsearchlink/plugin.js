/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/*global tinymce:true */

tinymce.PluginManager.add('savedsearchlink', function(editor) {
	editor.addCommand('InsertSavedSearchLink', function() {
		var insertWin = Ext.create('OSF.component.SavedSearchLinkInsertWindow', {				
		});
		insertWin.editor = editor;
		insertWin.show();
		insertWin.toFront();
	});

	editor.addButton('savedsearchlink', {
		image: tinymce.baseURL + '/plugins/savedsearchlink/icon/icon.gif',
		tooltip: 'Insert a link to a saved search',
		cmd: 'InsertSavedSearchLink'
	});

	editor.addMenuItem('savedsearchlink', {
		image: tinymce.baseURL + '/plugins/savedsearchlink/icon/icon.gif',
		text: 'Insert saved search link',
		cmd: 'InsertSavedSearchLink',
		context: 'insert'
	});
});
