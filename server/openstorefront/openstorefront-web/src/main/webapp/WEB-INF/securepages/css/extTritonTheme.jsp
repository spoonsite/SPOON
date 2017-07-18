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

<%--
    Document   : extTritonTheme
    Created on : Dec 2, 2016, 12:19:06 PM
    Author     : dshurtleff
--%>

<%@page  contentType="text/css" %>

body > .x-mask {
    background-image: none;
    background-color: rgba(0, 0, 0, 0.5);
}

/* Panels/windows */

div[id*="-east-handle"] {
	width: 3px;
}

div[id*="-south-handle"] {
	height: 3px;
}

.x-panel-body-default {
    background: transparent;
    border-color: #d0d0d0;
    color: #404040;
    font-size: 13px;
    font-weight: 300;
    font-family: 'Open Sans', 'Helvetica Neue', helvetica, arial, verdana, sans-serif;
    border-width: 1px;
    border-style: solid;
}

.x-panel-header-default .x-tool-tool-el {
    background-color: transparent;
}

.x-panel-header-default-framed .x-tool-tool-el {
    background-color: transparent;
}

.x-panel-header-default-framed-top {
    -moz-border-radius-topleft: 0;
    -webkit-border-top-left-radius: 0;
    border-top-left-radius: 0;
    -moz-border-radius-topright: 0;
    -webkit-border-top-right-radius: 0;
    border-top-right-radius: 0;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 0;
    -webkit-border-bottom-left-radius: 0;
    border-bottom-left-radius: 0;
    padding: 10px 14px 10px 14px;
    border-width: 2px 2px 0 2px;
    border-style: solid;
    background-color: ${actionBean.branding.primaryColor};
}
.x-panel-header-default-framed-horizontal {
    padding: 10px 14px 10px 14px;
}
.x-panel-header-default-framed {
    font-size: 16px;
    border: 2px solid ${actionBean.branding.primaryColor};
}

.x-panel-default-framed {
    border-color: ${actionBean.branding.primaryColor};
    padding: 0;
}

.x-panel-default-outer-border-rbl {
    border-right-color: #c3c3c3 !important;
    border-right-width: 1px !important;
    border-bottom-color: #c3c3c3 !important;
    border-bottom-width: 1px !important;
    border-left-color: #c3c3c3 !important;
    border-left-width: 1px !important;
}

.x-panel-default-outer-border-trl {
    border-top-color: #c3c3c3 !important;
    border-top-width: 1px !important;
    border-right-color: #c3c3c3 !important;
    border-right-width: 1px !important;
    border-left-color: #c3c3c3 !important;
    border-left-width: 1px !important;
}

.x-panel-default {
    border-color: ${actionBean.branding.primaryColor};
    padding: 0;
}

.x-panel-default-framed-outer-border-trbl {
    border-color: ${actionBean.branding.primaryColor} !important;
    border-width: 1px !important;
}

x-window-header-default-top {
    -moz-border-radius-topleft: 0;
    -webkit-border-top-left-radius: 0;
    border-top-left-radius: 0;
    -moz-border-radius-topright: 0;
    -webkit-border-top-right-radius: 0;
    border-top-right-radius: 0;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 0;
    -webkit-border-bottom-left-radius: 0;
    border-bottom-left-radius: 0;
    padding: 10px 14px 10px 14px;
    border-width: 2px 2px 2px 2px;
    border-style: solid;
    background-color: ${actionBean.branding.primaryColor};
}
.x-window-header-default {
    font-size: 16px;
    border-color: ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor};
}

.x-window-default {
    border-color: ${actionBean.branding.primaryColor};
    -webkit-border-radius: 0 0 0 0;
    -moz-border-radius: 0 0 0 0;
    -ms-border-radius: 0 0 0 0;
    -o-border-radius: 0 0 0 0;
    border-radius: 0 0 0 0;
}

.x-window-header-default .x-tool-img {
    background-color: transparent;
}

.x-tab-bar-default {
    background-color: #3d464e;
}

.x-panel-header-default {
    background-image: none;
    background-color: ${actionBean.branding.panelHeaderColor};
}

.x-panel-header-default {
    font-size: 16px;
    border: 1px solid ${actionBean.branding.panelHeaderColor};
}

/* Buttons */

.x-btn-default-small {
    border-color: #5e6163;
}

.x-btn-default-small {
    -webkit-border-radius: 0;
    -moz-border-radius: 0;
    -ms-border-radius: 0;
    -o-border-radius: 0;
    border-radius: 0;
    padding: 7px 7px 7px 7px;
    border-width: 1px;
    border-style: solid;
    background-color: #888a8c;
}

.x-btn-default-large {
    border-color: #58595a;
}

.x-btn-default-large {
    -webkit-border-radius: 0;
    -moz-border-radius: 0;
    -ms-border-radius: 0;
    -o-border-radius: 0;
    border-radius: 0;
    padding: 9px 9px 9px 9px;
    border-width: 1px;
    border-style: solid;
    background-color: #7c7d7d;
}

.x-btn-default-medium {
    border-color: #58595a;
}

.x-btn-default-medium {
    -webkit-border-radius: 0;
    -moz-border-radius: 0;
    -ms-border-radius: 0;
    -o-border-radius: 0;
    border-radius: 0;
    padding: 8px 8px 8px 8px;
    border-width: 1px;
    border-style: solid;
    background-color: #7c7d7d;
}

/* MISC */
.x-colorpicker-field-swatch-inner {
    position: absolute;
    height: 100%;
    z-index: 999;
    width: 100%;
}

.x-progress-default {
    background-color: #efe3d8;
    border-width: 1px;
    height: 24px;
    border-color: #5fa2dd;
    border-style: solid;
}

/* IE 9 */

.x-nbr .x-panel-header-default-framed-top {
    padding: 0 !important;
    border-width: 0 !important;
    -webkit-border-radius: 0px;
    -moz-border-radius: 0px;
    -ms-border-radius: 0px;
    -o-border-radius: 0px;
    border-radius: 0px;
   /* background-color: transparent !important; */
    box-shadow: none !important;
}

.x-nbr .x-panel-default-framed {
   /* background-color: transparent !important; */
}

.x-nbr .x-window-default {
	background-color: ${actionBean.branding.primaryColor} !important;
}

.x-nbr .x-tab-default-top {
	background-color: rgba(151,151,151,1) !important;
}

.x-nbr .x-btn-default-large {
	background-color: rgb(85,85,85)  !important;
}

.x-nbr .x-btn-default-small {
	background-color: rgb(85,85,85)  !important;
}

.x-nbr .x-btn-default-toolbar-small {
	background-color: rgb(245,245,245)  !important;
}

.x-nbr .x-btn-default-toolbar-medium {
	background-color: rgb(245,245,245)  !important;
}

.x-nbr .x-btn-default-toolbar-large {
	background-color: rgb(245,245,245)  !important;
}

.x-nbr .x-panel-default-framed {
	background-color: ${actionBean.branding.primaryColor} !important;
}

.x-btn-default-small-mc {
	background-color: #7c7d7d;
}

.x-btn-default-medium-mc {
	background-color: #7c7d7d;
}

.x-btn-default-large-mc {
	background-color: #7c7d7d;
}
