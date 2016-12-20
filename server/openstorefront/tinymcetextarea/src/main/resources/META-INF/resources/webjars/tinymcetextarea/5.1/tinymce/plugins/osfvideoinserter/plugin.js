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
/*global tinymce, Ext:true */

tinymce.PluginManager.add('osfvideoinserter', function(editor) {
	editor.addCommand('InsertVideo', function() {
		var insertWin = Ext.create('OSF.component.MediaInsertWindow', {				
			editor: editor,
			mediaToShow: 'VID',
			mediaName: 'Video'
		});		
		insertWin.show();
		insertWin.toFront();
	});

	editor.addButton('osfvideoinserter', {
		icon: 'media',
		tooltip: 'Insert a video',
		cmd: 'InsertVideo'
	});

	editor.addMenuItem('osfvideoinserter', {
		icon: 'media',
		text: 'Insert a video',
		cmd: 'InsertVideo',
		context: 'insert'
	});
});
