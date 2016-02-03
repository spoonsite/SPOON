<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/templateLayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
				
			var template = {
				blocks: [],
				refresh: function(container, entry) {
					container.removeAll();
					
					Ext.Array.each(template.blocks, function(block){
						block.updateTemplate(entry);	
					});
					
					container.add(template.blocks);	
					Ext.defer(function(){
						container.updateLayout(true, true);
					}, 500);					
				}
			};
			
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
								
				var description = Ext.create('OSF.component.template.Description', {
					margin: '0 0 20 0'
				});								
				template.blocks.push(description);
				
				var media = Ext.create('OSF.component.template.Media', {
					margin: '0 0 20 0'
				});								
				template.blocks.push(media);				
								
				var dependencies = Ext.create('OSF.component.template.Dependencies', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(dependencies);		
				
				var di2elevel = Ext.create('OSF.component.template.DI2EEvalLevel', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(di2elevel);	

				var evaluationSummary = Ext.create('OSF.component.template.EvaluationSummary', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(evaluationSummary);	
				
				var resources = Ext.create('OSF.component.template.Resources', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(resources);
				
				var contacts = Ext.create('OSF.component.template.Contacts', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(contacts);				
				
				var vitals = Ext.create('OSF.component.template.Vitals', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(vitals);					
				
				var vitals = Ext.create('OSF.component.template.Relationships', {					
					margin: '0 0 20 0'
				});								
				template.blocks.push(vitals);					
								
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>			
