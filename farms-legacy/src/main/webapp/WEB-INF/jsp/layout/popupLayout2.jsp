<%@ page language="java" %>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!DOCTYPE html>

<tiles:useAttribute name="screenNumber" scope="request" ignore="true"/>
<u:getForm var="form" scope="request"/>

<html:html xhtml="true">
<head>
<title>AgriStability</title>
<meta http-equiv="Content-Language" content="en-us"/>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<style type="text/css">
/*margin and padding on body element
      can introduce errors in determining
      element position and are not recommended;
      we turn them off as a foundation for YUI
      CSS treatments. */
    body {
  margin:0;
  padding:0;
}
</style>

<!-- YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/assets/skins/sam/skin.css" media="all"/>

<!-- Trumbowyg CSS files --> 
<link rel="stylesheet" href="trumbowyg/dist/ui/trumbowyg.min.css">
<link rel="stylesheet" href="trumbowyg/dist/plugins/table/ui/trumbowyg.table.min.css">
<link rel="stylesheet" href="css/editor.css"/>

<!-- Custom CSS files -->
<link rel="stylesheet" href="css/farm-base.css" media="all"/>
<link rel="stylesheet" href="css/farm.css" media="all"/>
<link rel="stylesheet" href="css/farm-custom.css" media="all"/>
<link rel="stylesheet" href="css/farm-wide.css" media="all"/>
<!--[if IE]>
<link type="text/css" rel="stylesheet" href="css/farm-ie-7-and-8.css" media="all"/>
<![endif]-->
<link type="text/css" rel="stylesheet" href="css/farm-print.css" media="print"/>

<!-- Combo-handled YUI JS files: -->
<script type="text/javascript" src="yui/2.8.2r1/build/utilities/utilities.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/container/container-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/menu/menu-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/button/button-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="yui/2.8.2r1/build/calendar/calendar-min.js"></script> 

<!-- Custom JS files -->
<script type="text/javascript" src="javascript/farm.js"></script>
<script type="text/javascript" src="javascript/jquery.min.js"></script>

</head>
<body class="yui-skin-sam" style="; background-color:#F2F2F2" onload="if (typeof onPageLoad == 'function') onPageLoad();">
<div id="bcgov-container" style="width:95%">
  <table id="bcgov-main-section">
    <tr>
      <td id="bcgov-content" style="width:95%">
        <!-- START BODY CONTENT -->
        <div>
          <div id="bcgov-content-liner">
            <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
            <tiles:insert attribute="body" />
          </div>
        </div>
        <table id="bcgov-content-processing" style="margin:0 auto; height:300px; display:none">
          <tr>
            <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
            <td id="processingMessage"><fmt:message key="Processing"/>...</td>
          </tr>
        </table>    
        <!-- END BODY CONTENT -->
      </td>
    </tr>
  </table>
  <!-- START FOOTER -->
  <table id="bcgov-footer">
    <tr>
      <td id="bcgov-footer-info"><w:applicationCode/><c:out value="${screenNumber}"/> v<fmt:message key="application.version"/>  </td>
    </tr>
  </table>
  <!-- END FOOTER -->
</div>
</body>
</html:html>
