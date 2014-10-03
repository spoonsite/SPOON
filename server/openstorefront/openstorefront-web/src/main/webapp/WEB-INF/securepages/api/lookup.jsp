<%--
Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/apidocLookup.css" rel="stylesheet" type="text/css"/>
        <script src="script/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="script/reference.js" type="text/javascript"></script>
    </head>
    <body>
        <h2>Lookup Table Reference</h2>	
        <div class='wrapper'>
            <table id="tableOfContents"></table>
            <div id="content"></div>
        </div>
        <script type="text/javascript">
            doLookups();
        </script>
                
    </body>
</html>
