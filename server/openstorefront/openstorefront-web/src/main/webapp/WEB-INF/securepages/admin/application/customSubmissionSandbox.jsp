<%--
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
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var letterRange = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

				var getCode = function () {
					letterRange.charAt(Math.floor(Math.random() * letterRange.length)) + (Math.floor(Math.random() * 10))
				};

				var attrGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'AttributesGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Attributes Grid',
					labelTip: 'This is a totally useful tip for the field: AttributesGrid',
					labelCode: 'G6'
				});

				var contactsGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'ContactsGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Contacts Grid',
					labelTip: 'This is a totally useful tip for the field: ContactsGrid',
					labelCode: 'E3'
				});

				var dependGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'DependenciesGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Dependencies Grid',
					labelTip: 'This is a totally useful tip for the field: DependenciesGrid',
					labelCode: 'K8'
				});

				var mediaGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'MediaGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Media Grid',
					labelTip: 'This is a totally useful tip for the field: MediaGrid',
					labelCode: 'M1'
				});

				var relGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'RelationshipsGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Relationships Grid',
					labelTip: 'This is a totally useful tip for the field: RelationshipsGrid',
					labelCode: 'Y3'
				});

				var resourcesGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'ResourcesGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Resources  Grid',
					labelTip: 'This is a totally useful tip for the field: ResourcesGrid',
					labelCode: 'R2'
				});

				var tagsGrid = Ext.create('OSF.customSubmission.Field', {
				    fieldType: 'TagsGrid',
					isScoped: true,
					canComment: true,
					commentRich: true,
					label: 'Field for: Tags Grid',
					labelTip: 'This is a totally useful tip for the field: TagsGrid',
					labelCode: 'F6'
				});

				var formSection = Ext.create('OSF.customSubmission.Section', {
					scrollable: true,
					defaults: {
						margin: '0 0 50 0'
					},
					items: [
						attrGrid,
						contactsGrid,
						dependGrid,
						mediaGrid,
						relGrid,
						resourcesGrid,
						tagsGrid
					]
				});

				addComponentToMainViewPort(formSection);

			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>
