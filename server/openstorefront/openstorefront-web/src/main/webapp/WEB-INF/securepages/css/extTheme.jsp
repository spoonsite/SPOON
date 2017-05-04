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

<%@page  contentType="text/css" %>


body.x-border-layout-ct,
div.x-border-layout-ct {
    background-color: #f5f5f5;
    background: url('${pageContext.request.contextPath}/images/grid.png');	
}

.x-body {
    color: #000;
    margin-right: 1px;
    font-size: 14px;
    line-height: 20px;
    font-family: helvetica, arial, verdana, sans-serif;
    background-color: #f5f5f5;
    background: url('${pageContext.request.contextPath}/images/grid.png');
}

.x-tip-default {
    background-color: #E6E6E6;
    border-color: #A2A2A2;
}

.x-toolbar-default {
    padding: 6px 0 6px 8px;
    border-style: solid;
    border-color: #c2c2c2;
    border-width: 1px;
    background-image: none;
    background-color: #f5f5f5;
    background: url('${pageContext.request.contextPath}/images/grid.png');
}

.x-window-header-default-top {
    -moz-border-radius-topleft: 4px;
    -webkit-border-top-left-radius: 4px;
    border-top-left-radius: 4px;
    -moz-border-radius-topright: 4px;
    -webkit-border-top-right-radius: 4px;
    border-top-right-radius: 4px;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 0;
    -webkit-border-bottom-left-radius: 0;
    border-bottom-left-radius: 0;
    padding: 5px 5px 5px 5px;
    border-width: 5px 5px 5px 5px;
    border-style: solid;
    background-color: ${actionBean.branding.primaryColor};
}

.x-window-header-default {
    font-size: 13px;
    border-color: ${actionBean.branding.primaryColor};
    /* border-bottom: 1px solid ${actionBean.branding.accentColor}; */
    background-color: ${actionBean.branding.primaryColor};
}

.x-window-body-default {
    border-color: ${actionBean.branding.primaryColor};
    border-width: 1px;
    border-style: solid;
    background: #fff;
    color: #000;
    font-size: 13px;
    font-weight: normal;
    font-family: helvetica, arial, verdana, sans-serif;
}

.x-panel-header-default {
    background-image: none;
    background-color: ${actionBean.branding.panelHeaderColor};
}

.x-panel-header-default {
    font-size: 13px;
    border: 1px solid ${actionBean.branding.panelHeaderColor};
}

.x-panel-body-default {
    background: transparent;
    border-color: #c2c2c2;
    color: #000;
    font-size: 13px;
    font-weight: normal;
    font-family: helvetica, arial, verdana, sans-serif;
    border-width: 1px;
    border-style: solid;
}

.x-panel-default-outer-border-trbl {
    border-color: #D6D6D6 !important;
    border-width: 1px !important;
}

.x-panel-default-outer-border-rbl {
    border-right-color: lightgray !important;
    border-right-width: 1px !important;
    border-bottom-color: lightgray !important;
    border-bottom-width: 1px !important;
    border-left-color: lightgray !important;
    border-left-width: 1px !important;
}

.x-panel-default-outer-border-trl {
    border-top-color: lightgray !important;
    border-top-width: 1px !important;
    border-right-color: lightgray !important;
    border-right-width: 1px !important;
    border-left-color: lightgray !important;
    border-left-width: 1px !important;
}

.x-panel-default {
    border-color: ${actionBean.branding.primaryColor};
    padding: 0;
}

.x-accordion-item .x-accordion-hd {
    background: #D8DDE0;
    border-width: 0;
    border-color: #BDBDBD;
    padding: 8px 10px;
}

.x-panel-default-framed {	
    border-color: ${actionBean.branding.primaryColor};
    padding: 0;
    background-color: ${actionBean.branding.primaryColor};
}

.x-panel-header-default .x-tool-img {
    background-color: transparent;
}

.x-panel-default-framed-outer-border-trbl {
    border-color: ${actionBean.branding.primaryColor} !important;
    border-width: 1px !important;
}

.x-panel-header-default-framed-top {
    -moz-border-radius-topleft: 4px;
    -webkit-border-top-left-radius: 4px;
    border-top-left-radius: 4px;
    -moz-border-radius-topright: 4px;
    -webkit-border-top-right-radius: 4px;
    border-top-right-radius: 4px;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 0;
    -webkit-border-bottom-left-radius: 0;
    border-bottom-left-radius: 0;
    padding: 5px 5px 5px 5px;
    border-width: 5px 5px 0 5px;
    border-style: solid;
    background-color: ${actionBean.branding.primaryColor};
}

.x-panel-header-default-framed {
    font-size: 13px;
    border: 5px solid ${actionBean.branding.primaryColor};
}

.x-panel-header-default-framed .x-tool-img {
    background-color: transparent;
}

.x-panel-header-default-framed-left {
    -moz-border-radius-topleft: 4px;
    -webkit-border-top-left-radius: 4px;
    border-top-left-radius: 4px;
    -moz-border-radius-topright: 0;
    -webkit-border-top-right-radius: 0;
    border-top-right-radius: 0;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 4px;
    -webkit-border-bottom-left-radius: 4px;
    border-bottom-left-radius: 4px;
    padding: 5px 5px 5px 5px;
    border-width: 5px 0 5px 5px;
    border-style: solid;
    background-color: ${actionBean.branding.primaryColor};
}

.x-panel-default-framed-outer-border-rbl {
    border-right-color: ${actionBean.branding.primaryColor} !important;
    border-right-width: 1px !important;
    border-bottom-color: ${actionBean.branding.primaryColor} !important;
    border-bottom-width: 1px !important;
    border-left-color: ${actionBean.branding.primaryColor} !important;
    border-left-width: 1px !important;
}

.x-panel-default-framed-outer-border-trl {
    border-top-color: ${actionBean.branding.primaryColor} !important;
    border-top-width: 1px !important;
    border-right-color: ${actionBean.branding.primaryColor} !important;
    border-right-width: 1px !important;
    border-left-color: ${actionBean.branding.primaryColor} !important;
    border-left-width: 1px !important;
}

.x-panel-default-framed-outer-border-rl {
    border-right-color: ${actionBean.branding.primaryColor} !important;
    border-right-width: 1px !important;
    border-left-color: ${actionBean.branding.primaryColor} !important;
    border-left-width: 1px !important;
}


.x-window-header-default .x-tool-img {
    background-color: transparent;
}
.x-window-default {
    border-color: ${actionBean.branding.primaryColor};
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -ms-border-radius: 4px;
    -o-border-radius: 4px;
    border-radius: 4px;
}

.x-mask {
    background-image: none;
    background-color: rgba(60, 60, 60, 0.7);
    cursor: default;
    border-style: solid;
    border-width: 1px;
    border-color: transparent;
}

.x-btn-default-large {
    border-color: #505050;
}

.x-btn-default-large {
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -ms-border-radius: 3px;
    -o-border-radius: 3px;
    border-radius: 3px;
    padding: 3px 3px 3px 3px;
    border-width: 1px;
    border-style: solid;
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);	
}

.x-btn-default-small {
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -ms-border-radius: 3px;
    -o-border-radius: 3px;
    border-radius: 3px;
    padding: 3px 3px 3px 3px;
    border-width: 1px;
    border-style: solid;
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-default-small-mc {
    background-image: url(images/btn/btn-default-small-fbg.gif);
    background-position: 0 top;
    background-color: #555555;
}

.x-btn-focus.x-btn-default-small {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    -webkit-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    -moz-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
}

.x-btn-over.x-btn-default-small {
    border-color: #787878;
    background-image: none;
    background-color: #666666;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #747474), color-stop(50%, #666666), color-stop(51%, #666666), color-stop(0%, #707070));
    background-image: -webkit-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -moz-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -o-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -ms-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
}

.x-btn.x-btn-menu-active.x-btn-default-small,
.x-btn.x-btn-pressed.x-btn-default-small {
    border-color: #484848;
    background-image: none;
    background-color: #444444;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #444444), color-stop(50%, #444444), color-stop(51%, #444444), color-stop(0%, #4B4B4B));
    background-image: -webkit-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -moz-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -o-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -ms-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
}

.x-btn.x-btn-disabled.x-btn-default-small {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-over .x-btn-default-small-mc {
    background-color: #666666;
    background-image: url(images/btn/btn-default-small-over-fbg.gif);
}

.x-btn-focus.x-btn-over .x-btn-default-small-mc {
    background-color: #666666;
    background-image: url(images/btn/btn-default-small-focus-over-fbg.gif);
}

.x-btn.x-btn-menu-active .x-btn-default-small-mc,
.x-btn.x-btn-pressed .x-btn-default-small-mc {
    background-color: #444444;
    background-image: url(images/btn/btn-default-small-pressed-fbg.gif);
}

.x-btn.x-btn-disabled .x-btn-default-small-mc {
    background-color: #555555;
    background-image: url(images/btn/btn-default-small-disabled-fbg.gif);
}

.x-btn-default-medium {
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -ms-border-radius: 3px;
    -o-border-radius: 3px;
    border-radius: 3px;
    padding: 3px 3px 3px 3px;
    border-width: 1px;
    border-style: solid;
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-default-medium-mc {
    background-image: url(images/btn/btn-default-medium-fbg.gif);
    background-position: 0 top;
    background-color: #555555;
}

.x-btn-focus.x-btn-default-medium {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    -webkit-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    -moz-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
}

.x-btn-over.x-btn-default-medium {
    border-color: #787878;
    background-image: none;
    background-color: #666666;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #747474), color-stop(50%, #666666), color-stop(51%, #666666), color-stop(0%, #707070));
    background-image: -webkit-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -moz-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -o-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -ms-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
}

.x-btn.x-btn-menu-active.x-btn-default-medium,
.x-btn.x-btn-pressed.x-btn-default-medium {
    border-color: #484848;
    background-image: none;
    background-color: #444444;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #4B4B4B), color-stop(50%, #444444), color-stop(51%, #444444), color-stop(0%, #4B4B4B));
    background-image: -webkit-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -moz-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -o-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -ms-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
}

.x-btn.x-btn-disabled.x-btn-default-medium {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-focus .x-btn-default-medium-mc {
    background-color: #555555;
    background-image: url(images/btn/btn-default-medium-focus-fbg.gif);
}

.x-btn-over .x-btn-default-medium-mc {
    background-color: #666666;
    background-image: url(images/btn/btn-default-medium-over-fbg.gif);
}

.x-btn.x-btn-menu-active .x-btn-default-medium-mc,
.x-btn.x-btn-pressed .x-btn-default-medium-mc {
    background-color: #444444;
    background-image: url(images/btn/btn-default-medium-pressed-fbg.gif);
}

.x-btn-focus.x-btn-menu-active .x-btn-default-medium-mc,
.x-btn-focus.x-btn-pressed .x-btn-default-medium-mc {
    background-color: #444444;
    background-image: url(images/btn/btn-default-medium-focus-pressed-fbg.gif);
}

.x-btn.x-btn-disabled .x-btn-default-medium-mc {
    background-color: #555555;
    background-image: url(images/btn/btn-default-medium-disabled-fbg.gif);
}

.x-btn-default-large {
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -ms-border-radius: 3px;
    -o-border-radius: 3px;
    border-radius: 3px;
    padding: 3px 3px 3px 3px;
    border-width: 1px;
    border-style: solid;
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-default-large-mc {
    background-image: url(images/btn/btn-default-large-fbg.gif);
    background-position: 0 top;
    background-color: #555555;
}

.x-btn-focus.x-btn-default-large {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    -webkit-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    -moz-box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
    box-shadow: #d7e9f6 0 1px 0px 0 inset, #d7e9f6 0 -1px 0px 0 inset, #d7e9f6 -1px 0 0px 0 inset, #d7e9f6 1px 0 0px 0 inset;
}


.x-btn-over.x-btn-default-large {
    border-color: #787878;
    background-image: none;
    background-color: #666666;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #747474), color-stop(50%, #666666), color-stop(51%, #666666), color-stop(0%, #707070));
    background-image: -webkit-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -moz-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -o-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: -ms-linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
    background-image: linear-gradient(top, #747474, #666666 50%, #666666 51%, #707070);
}

.x-btn.x-btn-menu-active.x-btn-default-large,
.x-btn.x-btn-pressed.x-btn-default-large {
    border-color: #484848;
    background-image: none;
    background-color: #444444;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #4B4B4B), color-stop(50%, #444444), color-stop(51%, #444444), color-stop(0%, #4B4B4B));
    background-image: -webkit-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -moz-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -o-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: -ms-linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
    background-image: linear-gradient(top, #4B4B4B, #444444 50%, #444444 51%, #4B4B4B);
}

.x-btn.x-btn-disabled.x-btn-default-large {
    background-image: none;
    background-color: #555555;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #656464), color-stop(50%, #555555), color-stop(51%, #555555), color-stop(0%, #676666));
    background-image: -webkit-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -moz-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -o-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: -ms-linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
    background-image: linear-gradient(top, #656464, #555555 50%, #555555 51%, #676666);
}

.x-btn-over .x-btn-default-large-mc {
    background-color: #666666;
    background-image: url(images/btn/btn-default-large-over-fbg.gif);
}

.x-btn-focus.x-btn-over .x-btn-default-large-mc {
    background-color: #666666;
    background-image: url(images/btn/btn-default-large-focus-over-fbg.gif);
}

.x-btn.x-btn-menu-active .x-btn-default-large-mc,
.x-btn.x-btn-pressed .x-btn-default-large-mc {
    background-color: #444444;
    background-image: url(images/btn/btn-default-large-pressed-fbg.gif);
}

.x-btn-focus.x-btn-menu-active .x-btn-default-large-mc,
.x-btn-focus.x-btn-pressed .x-btn-default-large-mc {
    background-color: #444444;
    background-image: url(images/btn/btn-default-large-focus-pressed-fbg.gif);
}

.x-btn.x-btn-disabled .x-btn-default-large-mc {
    background-color: #555555;
    background-image: url(images/btn/btn-default-large-disabled-fbg.gif);
}

.x-btn-default-small {
    border-color: #6F6F6F;
}

.x-progress-default .x-progress-bar-default {
    background-image: none;
    background-color: ${actionBean.branding.accentColor};
}

.x-splitter-horizontal {
    background-color: #dedede;
}

.x-splitter-vertical {
    background-color: #dedede;
}

.x-view-item-focused {
    outline: 1px dashed ${actionBean.branding.accentColor} !important;
    outline-offset: -1px;
}

.x-mask.x-focus {
    border-style: solid;
    border-width: 1px;
    border-color: ${actionBean.branding.accentColor};
}

.x-panel-default {
    border-color: #A7C4E4;
    padding: 0;
}

.x-tab-bar-default {
    background-color:#FFFFFF;
    padding-top:5px;
}

.x-tab-bar-strip-default {
    border-style: solid;
    border-color:  ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor};
}

.x-tab.x-tab-active.x-tab-default {
    border-color: ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor} !important; 	
    	
}
.x-box-scroller-tab-bar-default {
    background-color: ${actionBean.branding.accentColor};
}

.x-tab.x-tab-active.x-tab-default .x-tab-inner-default {
    color: ${actionBean.branding.primaryTextColor};
}

.x-tab.x-tab-active.x-tab-default .x-tab-icon-el {
    color: ${actionBean.branding.primaryTextColor};
}

.x-tab-default-top, .x-tab-default-left {
    -moz-border-radius-topleft: 3px;
    -webkit-border-top-left-radius: 3px;
    border-top-left-radius: 3px;
    -moz-border-radius-topright: 3px;
    -webkit-border-top-right-radius: 3px;
    border-top-right-radius: 3px;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 0;
    -webkit-border-bottom-left-radius: 0;
    border-bottom-left-radius: 0;
    padding: 7px 12px 6px 12px;
    border-width: 1px;
    border-style: solid;
    background-color: #989898;
    outline:none;
}

.x-tab-over.x-tab-default{
    border-color: #989898;
    background-color: ${actionBean.branding.panelHeaderColor};
    outline:none;
}

.x-tab-default{
    border-color:${actionBean.branding.accentColor};
    cursor: pointer;
    outline:none;
}

.x-tab-focus.x-tab-active.x-tab-default {
    border-color: ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor};
    outline:none;
}

.x-tab-focus.x-tab-default {
    border-color: ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor};
    outline:none;
}

.x-tab-focus.x-tab-over.x-tab-default {
    border-color: ${actionBean.branding.primaryColor};
    background-color: ${actionBean.branding.primaryColor};
    outline:none;
}

.x-tab-default-top.x-tab-focus {
    -webkit-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    -moz-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
}

.x-tab-default-top.x-tab-focus.x-tab-over {

    -webkit-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    -moz-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
}

.x-tab-default-top.x-tab-focus.x-tab-active {
    -webkit-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    -moz-box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
    box-shadow: ${actionBean.branding.primaryColor} 0 1px 0px 0 inset, ${actionBean.branding.primaryColor} 0 -1px 0px 0 inset, ${actionBean.branding.primaryColor} -1px 0 0px 0 inset, ${actionBean.branding.primaryColor} 1px 0 0px 0 inset;
}

.x-tab.x-tab-disabled.x-tab-default {
    border-color: #CACACA;
    background-color: #AFAFAF;
    cursor: default;
}

.x-tab-default-left {
    -moz-border-radius-topleft: 3px;
    -webkit-border-top-left-radius: 3px;
    border-top-left-radius: 3px;
    -moz-border-radius-topright: 0;
    -webkit-border-top-right-radius: 0;
    border-top-right-radius: 0;
    -moz-border-radius-bottomright: 0;
    -webkit-border-bottom-right-radius: 0;
    border-bottom-right-radius: 0;
    -moz-border-radius-bottomleft: 3px;
    -webkit-border-bottom-left-radius: 3px;
    border-bottom-left-radius: 3px;
    padding: 7px 12px 6px 12px;
    border-width: 1px 1px 1px 1px;
    border-style: solid;
    background-color: #989898;
}

.x-panel-header-title-default {
    color: ${actionBean.branding.panelHeaderTextColor};
    font-size: 13px;
    font-weight: bold;
    font-family: helvetica, arial, verdana, sans-serif;
    line-height: 16px;
}

.x-panel-header-title-default-framed {
    color: ${actionBean.branding.primaryTextColor};
    font-size: 13px;
    font-weight: bold;
    font-family: helvetica, arial, verdana, sans-serif;
    line-height: 16px;
}

.x-window-header-title-default {
    color: ${actionBean.branding.primaryTextColor};
    font-size: 13px;
    font-weight: bold;
    font-family: helvetica, arial, verdana, sans-serif;
    line-height: 16px;
}

.x-grid-item-selected {
    color: #000;
    background-color:#e2e2e2;
    outline:none;
}

.x-grid-item-over {
    color: #000;
    background-color:#e2e2e2;
    outline:none;
}

.x-grid-item-focused {
    color: #000;
    background-color:#e2e2e2;
    outline:none;
}

.x-grid-item {
    outline:none;
}


.activecell .x-grid-cell {
    background-color: #ccffcc !important;
}

.x-toolbar-footer {
    padding: 6px 0 6px 6px;
    border-style: solid;
    border-color: #c2c2c2;
    border-width: 0;
    background-image: none;
    background-color: #E8E8E8;
}

/* Fix for issue described in https://www.sencha.com/forum/showthread.php?307051-Positioning-of-datepicker-in-extjs  */
/* Date-picker empty screen issue */
.x-datepicker.x-layer {
	position: fixed !important;
}

/* Fix for issue described in https://www.sencha.com/forum/showthread.php?307051-Positioning-of-datepicker-in-extjs  */
/*  Fix for multiselector widget  */
.x-panel.x-layer {
	position: fixed !important;
}

/* Add some space around icon and make darker for actioncolumn items */
.x-action-col-icon {
    height: 16px;
    width: 16px;
    margin-right: 6px;
	color: #666;
}

/* IE 9 */
.x-window-header-default-top-mc{
   background-color: ${actionBean.branding.primaryColor};
}

.x-tab-default-top-mc{
   background-color: ${actionBean.branding.primaryColor};
}

.x-btn-focus .x-btn-default-small-mc{
	background-image: url("images/btn/btn-default-small-focus-fbg.gif");
	background-color: ${actionBean.branding.primaryColor};	
}

.x-btn-focus .x-btn-default-medium-mc{
	background-image: url("images/btn/btn-default-medium-focus-fbg.gif");
	background-color: ${actionBean.branding.primaryColor};	
}

.x-panel-header-default-framed-top-mc{
   background-color: ${actionBean.branding.primaryColor};	
}

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

/* end of IE 9 */