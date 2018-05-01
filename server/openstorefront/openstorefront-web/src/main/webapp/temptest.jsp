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
			