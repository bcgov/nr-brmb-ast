<%@ page language="java" %>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!DOCTYPE html>

<tiles:useAttribute name="screenNumber" />
<u:getForm var="form" scope="request"/>

<html:html xhtml="true">
  <head>
    <title><fmt:message key="application.title"/></title>
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
    <link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/base/base-min.css"/>
    <link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/assets/skins/sam/skin.css"/>
    
    <!-- Custom CSS files -->
    <link href="css/farm.css" type="text/css" rel="stylesheet" media="all"/>
    <!--[if (IE 7)|(IE 8)]>
    <link href="css/farm-ie-7-and-8.css" type="text/css" rel="stylesheet" media="all"/>
    <![endif]-->
    <link href="css/farm-print.css" type="text/css" rel="stylesheet" media="print"/>
    
    <!-- YUI JS files -->
    <script type="text/javascript" src="yui/2.8.2r1/build/utilities/utilities.js"></script>
    <script type="text/javascript" src="yui/2.8.2r1/build/container/container-min.js"></script>
    <script type="text/javascript" src="yui/2.8.2r1/build/menu/menu-min.js"></script>
    <script type="text/javascript" src="yui/2.8.2r1/build/button/button-min.js"></script>
    
    <!-- Custom JS files -->
    <script type="text/javascript" src="javascript/farm.js"></script>
    <script type="text/javascript" src="javascript/jquery.min.js"></script>

  </head>
  <body class="yui-skin-sam" onload="if (typeof onPageLoad == 'function') onPageLoad();">
    <div id="bcgov-container">
      <table id="bcgov-main-section">
        <tr>
          <td id="bcgov-content">
            <!-- START BODY CONTENT -->
            <div id="bcgov-content-body" class="yui-skin-sam">
              <div id="bcgov-content-liner">
                <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
                <tiles:insert attribute="body" />
              </div>
              <table id="bcgov-content-processing" style="margin:0 auto; height:300px; display:none">
                <tr>
                  <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
                  <td id="processingMessage"><fmt:message key="Processing"/>...</td>
                </tr>
              </table>    
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