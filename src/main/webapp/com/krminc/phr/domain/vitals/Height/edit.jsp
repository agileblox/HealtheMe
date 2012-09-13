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
    Document   : edit
    Created on : Oct 19, 2009, 3:16:55 PM
    Author     : cmccall
--%>

<%@ include file="/includes/taglibs.jspf" %>
<html>
<head>
    <title>Vitals - Height - Edit</title>
    <meta name="requiresJQUI" content="true">
    <meta name="requiresValidation" content="true">
    <script type="text/javascript" src="${ctx_static}/js/validate.vital.height.js"></script>
    <script type="text/javascript">
        var baseuri = '${ctx_patient}';
        var url = "${apipath}/patients/${it.healthRecordId}/vitals/height/${ it.heightId }/";

        $(document).ready(function() {
            $("input.datepicker").datepicker({showOn: 'button', buttonImage: '${ctx_static}/images/cal.gif', buttonImageOnly: true, onSelect:function(){$(this).valid();}});

            $("#submit").click(function() {
                var _form = $("#detailform");
                if (_form.valid()) {
                    $(this).attr("disabled","disabled");
                    _form.submit();
                }
                return false;
            });

            $("#cancel").click(function(cancelClick) {
                cancelClick.preventDefault();
                if (confirm("Are you sure you wish to cancel?")) parent.doClose();
            });
        });

    </script>
    <link rel="stylesheet" type="text/css" href="${ctx_static}/css/iframe.css" media="screen, print"/>

</head>
<body bgcolor="#ffffff" text="#000000" link="#0000cc" vlink="#551a8b" alink="#ff0000">
    <div id="lightBox">
        <div id="m_lightBox">
            <h1>Edit Body Height</h1>

            <span class="toolSummary">Note: This height measurement reading will appear under 'Self Entered' data.</span>

            <form id="detailform" name="detailform" action="./formhandler/" method="post">
            <input type="hidden" id="sourceurl" name="sourceuri" value="${ctx}/vitals/height/${it.heightId}/edit" />
            <input type="hidden" id="returnurl" name="returnurl" value="${ctx}/vitals/height" />
            <input type="hidden" id="patientid" name="patientid" value="${it.healthRecordId}" />
            <input type="hidden" id="datasourceid" name="datasourceid" value="${it.dataSourceId}" />
            <input type="hidden" id="heightid" name="heightid" value="${it.heightId}" />

            <div class="ie">
                <div>
                    <span class="fldgroup">
                        <div class="hv_element">
                            <label for="feet">Feet<span class="required">*</span></label>
                            <input class="hv_element_level_input" maxlength="30" id="feet" name="feet" value="${it.feet}"/>
                        </div>
                    </span>
                    <span class="fldgroup">
                        <div class="hv_element">
                            <label for="Inches">Inches<span class="required">*</span></label>
                            <input class="hv_element_level_input" maxlength="30" id="inches" name="inches" value="${it.inches}"/>
                        </div>
                    </span>
                </div>
                <div class="clear"> </div>
                <div class="hv_element hv_element_date">
                    <label for="observeddate">Date Observed<span class="required">*</span></label>
                    <input class="hv_element_date_input datepicker" maxlength="30" name="observeddate" id="observeddate" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${ it.observedDate }"/>"/>
                </div>
            </div>
            <div id="lightBoxActions">
                <div class="req-marker">
                    <span class="required">*</span> Required field
                </div>
                <div class="phr-input-button-align-right">
                    <input id="cancel" type="button" class="" value="Cancel" />
                    <input id="submit" type="submit" class="" value="Save" />
                </div>
            </div>

            </form>

        </div>
    </div>
    </body>
</html>
