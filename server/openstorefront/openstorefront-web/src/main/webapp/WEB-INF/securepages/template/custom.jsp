<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/templateLayout.jsp">
    <stripes:layout-component name="contents">
		
        <script type="text/javascript">
			
			var template = {
				blocks: [],
				refresh: function(container, entry) {
					container.removeAll(false);
					
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
				${actionBean.templateContents}				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>