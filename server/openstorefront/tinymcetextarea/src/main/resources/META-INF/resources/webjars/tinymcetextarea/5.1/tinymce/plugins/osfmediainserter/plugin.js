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

tinymce.PluginManager.add('osfmediainserter', function(editor) {
	editor.addCommand('InsertMedia', function() {
		var insertWin = Ext.create('OSF.component.MediaInsertWindow', {				
			id: "osfmediainsertwindow"
		});
		Ext.osfTinyMceEditor = editor;
		insertWin.show();
		insertWin.toFront();
	});

	editor.addButton('osfmediainserter', {
		icon: 'image',
		tooltip: 'Insert an Image',
		cmd: 'InsertMedia'
	});

	editor.addMenuItem('osfmediainserter', {
		icon: 'image',
		text: 'Insert an Image',
		cmd: 'InsertMedia',
		context: 'insert'
	});
});
