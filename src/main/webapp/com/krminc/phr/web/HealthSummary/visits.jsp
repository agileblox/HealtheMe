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
    Document   : visits
    Created on : Jul 10, 2009, 3:10:04 PM
    Author     : cmccall
--%>

<%@ include file="/includes/taglibs.jspf" %>
<%@ include file="/includes/inc_displaytag_decorators.jspf" %>
<c:choose>
    <c:when test="${empty param.results}">
        <c:set scope="page" var="numResults" value="10"></c:set>
    </c:when>
    <c:otherwise>
        <c:catch var="e">
            <fmt:formatNumber minIntegerDigits="1" maxIntegerDigits="2" type="number" value="${param.results}" var="numResults" scope="page"></fmt:formatNumber>
        </c:catch>
        <c:if test="${e != null}">
            <c:set scope="page" var="numResults" value="10"></c:set>
        </c:if>
        <c:if test="${numResults < 1}">
            <c:set scope="page" var="numResults" value="10"></c:set>
        </c:if>
    </c:otherwise>
</c:choose>

<head>
    <meta name="requiresJQUI" content="true">
    <title>HealtheMe - Health Record - Appointments &amp; Visits</title>
    <script type="text/javascript" src="${ctx_static}/js/jquery.DOMWindow.js"></script>
    <script language="javascript" src="${ctx_static}/js/jquery.pager.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx_static}/css/widget.css" />
    <link rel="stylesheet" type="text/css" href="${ctx_static}/css/paging.css" />
    <script language="javascript">
    //set globals
    var ctx_static = "${ctx_static}";
    var patientId = ${it.healthRecordId};
    var max = ${numResults}; //num per page
    var orderBy = "visitdate"; //must match first item in Sort DDL
    var midPath = "/patients/${it.healthRecordId}/visits/";

    //handle JSON data and segment for parsing
    function handleData(data,updatedPosition){
        if (updatedPosition != undefined) {
            //handle update
            if (data.visitId != null) {
                doParse(data, updatedPosition);
                notify("Record updated");
            } else {
                pagingError();
            }
        } else {
            //handle full results
            if (data.visit != null) {
                $("#tabs-content #emptyresults").hide();
                if (data.visit.length) {
                    //many results
                    $.each(data.visit, function (i){
                       doParse(data.visit[i],i+1);
                    });
                } else {
                    //1 result
                    doParse(data.visit,1);
                }
            } else {
                //no results
            }
        }
    }

    //place an individual json element at a specific target in the page
    function doParse(v, position){
        //parse and prepare data
        var itemid = v.visitId;
        var itemtitle = v.title;
        var visitDate = v.visitDate;
        var visitTime = v.visitTime;
        var status = v.status;
        var purpose = v.purpose;
        var location = v.location;
        var provider = v.provider;
        var comments = v.comments;
        var sourceId = v.dataSourceId;
        var dateAdded = v.dateAdded;
        var sourceText = "";
        var sourceClass = "";

        //strip Not Available classes
        $("div#target"+position+" .ntav").removeClass("ntav");

        //populate parsed data to appropriate target - hide,manipulate,show
        $("#buttons-" + position).find("div").hide();
        $("#target-title-" + position).text(itemtitle).css({fontWeight:"bold"});
        $("#target-id-" + position).text(itemid);

        //individual data items
        setDisplay(visitDate,"visitdate",position);
        setDisplay(visitTime,"visittime",position);
        setDisplay(status,"status",position);
        setDisplay(purpose,"purpose",position);
        setDisplay(location,"location",position);
        setDisplay(provider,"provider",position);
        setDisplay(comments,"comments",position);
        setDisplay(dateAdded,"dateadded",position);

        $("#buttons-" + position).find("a").attr("href", "${ctx_patient}/${it.healthRecordId}/visits/" + itemid +"/edit");

        if(sourceId==1) {
            sourceClass="self";
            sourceText="Self Entered";
            $("#buttons-" + position).show();
        } else {
            sourceClass="clinical";
            sourceText="External System";
            $("#buttons-" + position).hide();
        }
        $("#target-source-" + position).text(sourceText);
        $("#target" + position).removeClass().addClass(sourceClass).addClass("record-container");

        //record detail state
		toggleDetails();

        $("#buttons-" + position).show();
        $("#" + sourceClass + "-buttons-" + position).show();
        $(("#target" + position)).show();
    }
    </script>
    <script language="javascript" src="${ctx_static}/js/widget.js"></script>
</head>
<body id="visits">

    <h1>Appointments &amp; Visits</h1>
    
    <div class="def-container">
    	<div class="def-title">Definition</div>
        A listing of any health-related interactions with a medical professional either scheduled or past.  Track upcoming doctor's visits, manage follow-up appointments, or review your past interactions.
    </div>

    <%@ include file="/includes/components/phr/healthrecord/inc_tip_hrec_self.jspf" %>

    <%@ include file="/includes/components/phr/notifiers/inc_notifier.jspf" %>

	<div id="ajax-container">
        <div id="widget-container">
            <div id="visit-widget" class="widget">
                <div class="expander">
                    <form id="expand" action="#" method="post">
                       <input type="checkbox" id="chkexpand" /><label for="chkexpand">Show All Details</label>
                    </form>
                </div>
                <ul id="tabs">
                    <li><a id="self" href="#" class="fakelink">Self Entered Records</a></li>
                </ul>
		<div id="controls">
                    <div class="add">
                    <a class="opensFrame" href="${ctx_patient}/${it.healthRecordId}/visits-add" title="Add Record">Add Record</a>
                </div>
                <div class="sorter">
                    <form id="sorting" action="#" method="post">
                        <label for="sort">Sort by:</label>
                        <select name="sort" id="sort">
                            <option value="name" class="asc">Appointment: ascending</option>
                            <option value="name" class="desc">Appointment: descending</option>
                            <option value="dateadded" class="desc">Date Added: recent first</option>
                            <option value="dateadded" class="asc">Date Added: oldest first</option>
                            <option value="visitdate" selected="selected" class="desc">Date of Visit: recent first</option>
                            <option value="visitdate" class="asc">Date of Visit: oldest first</option>
                            <option value="provider" class="asc">Provider: ascending</option>
                            <option value="provider" class="desc">Provider: descending</option>
                            <option value="type">Type of Visit</option>
                        </select>
                    </form>
                </div>
                <div class="clear"> </div>
                </div>
        
                <div id="tabs-content">
                    <div id="emptyresults" class="hiddenwidget">No records found.</div>

                    <!-- data elements begin -->
                    <c:forEach var="i" begin="1" end="${numResults}">
                    <div id="target${i}" class="record-container hiddenwidget">
                        <div class="rec-header-box" style="padding: 2px;">
                           <span id="target-id-${i}" style="display:none;"></span>
                           <div id="buttons-${i}" class="record-action-box">
                               <ul class="actionButtons" id="self-buttons-${i}">
                                    <li class="edit">
                                        <a id ="target-link-${i}" href="${ctx_patient}/${it.healthRecordId}/medicalevent/0/edit" class="opensFrame" title="Edit Medical Event" id=""></a>
                                    </li>
                                    <li class="delete">
                                        <a href="#" title="Delete Medical Event" onClick="doDelete(${i});return false;"></a>
                                    </li>
                                </ul>
                                <div class="actionButtons" id="clinical-buttons-${i}" style="display:none;">
                                    <span class="btn no-edit">View Only</span>
                                </div>
                           </div>
                           <div class="rec-header-title">
                               <span id="target-fulltitle-${i}"></span><span id="target-title-${i}"></span>
                           </div>
                        </div><!--/header-box -->
                        <div class="clear"> </div>

                        <div class="widget-content-detail-box">
                            <div id="content-display-${i}">
                                <div class="content">
                                    <ul class="content2">
                                        <li>
                                            <div class="w-label">Date of Visit</div>
                                            <div><span id="target-visitdate-${i}"></span></div>
                                        </li>
                                        <li>
                                            <div class="w-label">Time of Visit</div>
                                            <div><span id="target-visittime-${i}"></span></div>
                                        </li>
                                    </ul>
                                    <ul class="content2">
                                        <li>
                                            <div class="w-label">Purpose</div>
                                            <div><span id="target-purpose-${i}"></span></div>
                                        </li>
                                        <li>
                                            <div class="w-label">Location</div>
                                            <div><span id="target-location-${i}"></span></div>
                                        </li>
                                        <li>
                                            <div class="w-label">Provider</div>
                                            <div><span id="target-provider-${i}"></span></div>
                                        </li>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                            <div id="content-toggle-${i}" style="display:none;">
                                <div class="content">
                                    <ul class="content2">
                                        <li>
                                            <div class="w-label">Date Added</div>
                                            <span id="target-dateadded-${i}"></span>
                                        </li>
                                        <li class="notes">
                                            <div class="w-label">Comments</div>
                                            <div><span id="target-comments-${i}"></span></div>
                                        </li>
                                    </ul>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </div><!--/widget-content-detail-box-->
                            <div id="test${i}" class="toggle-action-box">
                                <span class="toggles">
                                    <a id="toggle-${i}" class="toggle closed" href="#" title="Show Details"></a>
                                </span>
                            </div>
                            <div class="clear"></div>
                    </div><!--/test-target-->
                    </c:forEach>
               
                <div id="paging">
                            <div id="pager"></div>
                            <div id="full-results">
                                Records <span id="results-min">0</span> to <span id="results-max">0</span> of <span id="results-total">0</span>
                            </div>
                        </div>
                    </div><!-- /tab content-->
                </div><!--/widget-->
            </div><!--/widget container-->
        
        <div class="clear"></div>
    </div> <!--ajax container-->
    <div id="progress" style="display:none"><span class="ajaxload"><img src="${ctx_static}/css/img/ajax-loader.gif" class="ajaxload"/></span></div>
    <%--DEBUG: <display:table name="${it.bps}" />--%>

</body>