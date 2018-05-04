<%-- 
    Document   : temptest.jsp
    Created on : Apr 23, 2018, 1:44:52 PM
    Author     : dshurtleff
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">		
	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			
			Ext.require('OSF.customSubmission.SubmissionFormFullControl');
			
			Ext.onReady(function() {
				
				var subForm = Ext.create('OSF.customSubmission.SubmissionFormFullControl', {
				});
								
				var viewport = Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						subForm
					]
				});
				
				subForm.load(
					{
						name: 'Test Template',
						description: 'This is a test description',
						templateStatus: 'VALID',
						sections: [
							{
								stepId: 1,
								name: 'Section 1 - Grids',
								instructions: 'This is section 1',
								stepOrder: 0,
								fields: [
									{
										fieldId: 1,
										questionNumber: 'Q-1.',
										label: 'Select and add attributes to your entry.',
										labelTooltip: 'Attributes are metadata that describe your entry',
										mappingType: 'COMPLEX',
										required: false,
										fieldType: 'ATTRIBUTE_MULTI'
									},
									{
										fieldId: 2,
										questionNumber: 'Q-2.',
										label: 'Add Contacts to Entry.',
										labelTooltip: 'Contacts to entry',
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'CONTACT_MULTI'											
									},
									{
										fieldId: 5,
										questionNumber: 'Q-3.',
										label: 'Add Dependencies to Entry.',
										labelTooltip: 'Dependencies to entry',
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'EXT_DEPEND_MULTI'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-4.',
										label: 'Add Media (Images, Video...etc).',
										labelTooltip: 'This media will show in the media view of the entry.',
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'MEDIA_MULTI'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-5.',
										label: 'Add Relationships',
										labelTooltip: 'Describe relationships to existing items.',
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'RELATIONSHIPS_MULTI'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-6.',
										label: 'Add Resource',
										labelTooltip: 'Add external links and local asset',
										mappingType: 'COMPLEX',
										required: false,
										fieldType: 'RESOURCE_MULTI'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-7.',
										label: 'Add Tags',
										labelTooltip: 'Add tags',
										mappingType: 'COMPLEX',
										required: false,
										fieldType: 'TAG_MULTI'											
									}									
									
									
								]										
							},
							{
								stepId: 2,
								name: 'Section 2 - Forms',
								instructions: 'This is section 2',
								stepOrder: 0,
								fields: [
									{
										fieldId: 10,
										questionNumber: 'Q-10.',
										label: 'Add Attribute',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'ATTRIBUTE'											
									},
									{
										fieldId: 10,
										questionNumber: 'Q-11.',
										label: 'Add Tech Point of Contact',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'CONTACT'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-12.',
										label: 'Add Dependancy',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'EXT_DEPEND'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-13.',
										label: 'Add Media',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'MEDIA'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-14.',
										label: 'Add Relationship',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'RELATIONSHIPS'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-15.',
										label: 'Add Resources',										
										mappingType: 'COMPLEX',
										required: false,
										fieldType: 'RESOURCE'											
									},
									{
										fieldId: 6,
										questionNumber: 'Q-16.',
										label: 'Add Tag',										
										mappingType: 'COMPLEX',
										required: false,
										fieldType: 'TAG'											
									}									
								]										
							},
							{
								stepId: 3,
								name: 'Section 3 - Fields',
								instructions: 'This is section 3',
								stepOrder: 0,
								fields: [
									{
										fieldId: 4,
										questionNumber: 'Q-20.',
										label: 'Entry Name',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'TEXT'										
									},
									{
										fieldId: 4,
										questionNumber: 'Q-21.',
										label: 'Simple Description',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'TEXTAREA'										
									},
									{
										fieldId: 4,
										questionNumber: 'Q-22.',
										label: 'Version Number',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'NUMBER'										
									},
									{
										fieldId: 4,
										questionNumber: 'Q-23.',
										label: 'Rich text',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'RICHTEXT'										
									},									
									{
										fieldId: 4,
										questionNumber: 'Q-24.',
										label: 'Organization',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'ORGANIZATION'										
									}									
									
								]										
							},
							{
								stepId: 4,
								name: 'Section 4 - Static Content',
								instructions: 'Displays static content',
								stepOrder: 0,
								fields: [
									{	
										fieldId: 4,
										questionNumber: 'Q-30.',
										label: 'Line',										
										mappingType: 'NONE',
										required: false,
										fieldType: 'CONTENT',
										staticContent: '<hr>'
									},
									{	
										fieldId: 4,
										questionNumber: 'Q-31.',
										label: 'Text',										
										mappingType: 'NONE',
										required: false,
										fieldType: 'CONTENT',
										staticContent: '<b>Hello</b> this is some text'
									},									
									{	
										fieldId: 4,
										questionNumber: 'Q-32.',
										label: 'Image',										
										mappingType: 'NONE',
										required: false,
										fieldType: 'CONTENT',
										staticContent: '<img src="images/background_globe.jpg" height=200>'
									},
									{	
										fieldId: 4,
										questionNumber: 'Q-3.',
										label: 'Download',										
										mappingType: 'NONE',
										required: false,
										fieldType: 'CONTENT',
										staticContent: '<a href="download.csv" style="padding: 20px; text-align: center; background: purple; color: white;">Download</a>'
									}									
								]
							}
						]
					},
					'test'
				);
			
				
			});			
		</script>			
		
	</stripes:layout-component>
</stripes:layout-render>				
			