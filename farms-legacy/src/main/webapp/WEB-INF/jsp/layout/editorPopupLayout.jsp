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
<link rel="stylesheet" href="trumbowyg/dist/plugins/colors/ui/trumbowyg.colors.min.css">
<link rel="stylesheet" href="trumbowyg/dist/plugins/specialchars/ui/trumbowyg.specialchars.min.css">
<link rel="stylesheet" href="trumbowyg/dist/plugins/table/ui/trumbowyg.table.min.css">
<link rel="stylesheet" href="css/editor.css"/>

<!-- Custom CSS files -->
<link rel="stylesheet" href="css/farm-base.css" media="all"/>
<link rel="stylesheet" href="css/farm.css" media="all"/>
<link rel="stylesheet" href="css/farm-custom.css" media="all"/>
<link rel="stylesheet" href="css/farm-wide.css" media="all"/>
<link rel="stylesheet" type="text/css" href="css/farm-modal.css"/>
<!--[if IE]>
<link type="text/css" rel="stylesheet" href="css/farm-ie-7-and-8.css" media="all"/>
<![endif]-->
<link rel="stylesheet" type="text/css" href="css/farm-print.css" media="print"/>

<!-- Combo-handled YUI JS files: -->
<script src="yui/2.8.2r1/build/utilities/utilities.js"></script>
<script src="yui/2.8.2r1/build/container/container-min.js"></script>
<script src="yui/2.8.2r1/build/menu/menu-min.js"></script>
<script src="yui/2.8.2r1/build/button/button-min.js"></script>
<script src="yui/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script src="yui/2.8.2r1/build/calendar/calendar-min.js"></script> 

<script src="javascript/jquery.min.js"></script>

<script src="trumbowyg/dist/trumbowyg.min.js"></script>
<script src="trumbowyg/dist/plugins/base64/trumbowyg.base64.min.js"></script>
<script src="trumbowyg/dist/plugins/cleanpaste/trumbowyg.cleanpaste.min.js"></script>
<script src="trumbowyg/dist/plugins/colors/trumbowyg.colors.min.js"></script>
<script src="trumbowyg/dist/plugins/fontfamily/trumbowyg.fontfamily.min.js"></script>
<script src="trumbowyg/dist/plugins/fontsize/trumbowyg.fontsize.min.js"></script>
<script src="trumbowyg/dist/plugins/history/trumbowyg.history.min.js"></script>
<script src="trumbowyg/dist/plugins/indent/trumbowyg.indent.min.js"></script>
<script src="trumbowyg/dist/plugins/lineheight/trumbowyg.lineheight.min.js"></script>
<script src="trumbowyg/dist/plugins/preformatted/trumbowyg.preformatted.min.js"></script>
<script src="trumbowyg/dist/plugins/specialchars/trumbowyg.specialchars.min.js"></script>
<script src="trumbowyg/dist/plugins/table/trumbowyg.table.min.js"></script>
<script src="trumbowyg/dist/plugins/resizimg/trumbowyg.resizimg.min.js"></script>
<script src="javascript/jquery-resizable.min.js"></script> <!-- dependency for trumbowyg resizeable plugin -->

<!-- Custom JS files -->
<script src="javascript/farm.js"></script>

</head>
<body class="yui-skin-sam" style="text-align: initial; background-color:#F2F2F2" onload="if (typeof onPageLoad == 'function') onPageLoad();">
<div style="width:95%">
  <table id="bcgov-main-section">
    <tr>
      <td style="width:95%">
        <!-- START BODY CONTENT -->
        <div>
          <div id="editor-content-liner">
            <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
            <tiles:insert attribute="body" />
          </div>
        </div>
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
