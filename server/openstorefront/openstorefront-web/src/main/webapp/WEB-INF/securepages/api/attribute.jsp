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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="apidoc/css/apidocAttributes.css" rel="stylesheet" type="text/css"/>
        <script src="apidoc/script/jquery/jquery-3.2.1.min.js" type="text/javascript"></script>
        <script src="apidoc/script/reference.js" type="text/javascript"></script>
    </head>
    <body>
        <h2 style="padding-bottom: 10px;">Attribute Table Reference</h2>        		
        <div class='wrapper'>
            <table id="tableOfContents"></table>
            <div id="content"></div>
        </div>
        <script type="text/javascript">
            doAttributes();
        </script>
    </body>
</html>
