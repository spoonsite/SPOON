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
								name: 'Section 1',
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
										fieldId: 3,
										questionNumber: 'Q-3.',
										label: 'Add Tech Point of Contact',										
										mappingType: 'COMPLEX',
										required: true,
										fieldType: 'CONTACT'											
									},
									{
										fieldId: 3,
										questionNumber: 'Q-4.',
										label: 'Entry Name',										
										mappingType: 'COMPONENT',
										required: true,
										fieldType: 'TEXT'										
									}
								]										
							},
							{
								stepId: 2,
								name: 'Section 2',
								instructions: 'This is section 2',
								stepOrder: 0,
								fields: [
								]										
							},
							{
								stepId: 3,
								name: 'Section 3',
								instructions: 'This is section 3',
								stepOrder: 0,
								fields: [
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
			