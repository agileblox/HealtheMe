<%--

    Copyright (C) 2012 KRM Associates, Inc. healtheme@krminc.com

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
<%--
    -- SiteMesh Decorator --
    Document   : nonav.jsp
    Created on : May 25, 2009, 3:49:29 PM
    Author     : Daniel Shaw (dshaw.com)
    Description:
        This is the main decorator for pages in this app.
--%>

<%@ include file="/includes/taglibs.jsp" %>
<decorator:usePage id="thePage" />
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/includes/inc_content-type.jsp" %>
        <title><decorator:title default="" />${ projectname }</title>
        <%@ include file="/includes/inc_css.jsp" %>
        <style type="text/css">
            #data { margin: 6px 0 0; }
            #main { width: 938px; }
        </style>
        <%@ include file="/includes/inc_js.jsp" %>
        <%@ include file="/includes/inc_validation.jspf" %>
        <decorator:head />
    </head>
    <body  id="<decorator:getProperty property="body.id"/>" class="<decorator:getProperty property="body.class"/>" bgcolor="#ffffff" text="#000000" link="#0000cc" vlink="#551a8b" alink="#ff0000">
    <div id="outer-wrapper">
        <div id="container">
            <%@ include file="/includes/header.jsp" %>
            <div id="content">
                <div class="clear"></div>
                    <div id="infolayer">
                        <div id="breadcrumb"></div>
                        <div id="infoactions">
                            <%@ include file="/includes/components/inc_welcome.jspf" %>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div id="main">
                        <decorator:body />
                    </div>
                    <div class="clear"> </div>
                    <div id="disclaimer">
                    	<div id="disclaimer-wrapper">
                        	<div class="disclaimer-img">
                                <img alt="warning" src="${ctx_static}/css/img/caution.gif" />
                            </div>
                            <div id="disclaimer-content">
                                <p>Information contained within this site is intended solely for general educational purposes and is not intended nor implied to be a substitute for professional medical advice relative to your specific medical condition or question. Always seek the advice of your physician or other health provider for any questions you may have regarding your medical condition.</p>
                                <p class="b">Only your physician can provide specific diagnoses and therapies.</p>
                            </div>
                            <div class="clear"> </div>
                       	</div>
                    </div><!-- /#disclaimer -->
                </div><!-- /#content -->
                <div id="footer-bottom" />
            </div><!-- /#container -->
            <%@ include file="/WEB-INF/jspf/layout/footer/footer.jspf" %>
        </div><!-- /#outer-wrapper -->

    </body>
</html>