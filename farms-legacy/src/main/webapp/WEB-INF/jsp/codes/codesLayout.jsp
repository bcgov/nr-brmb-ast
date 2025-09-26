<%@ page language="java" %>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!DOCTYPE html>

<tiles:useAttribute name="subTabs" scope="request" ignore="true"/>
<tiles:useAttribute name="tabName" scope="request" ignore="true"/>
<tiles:useAttribute name="screenNumber" scope="request" ignore="true"/>

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
    <link rel="stylesheet" href="yui/2.8.2r1/build/assets/skins/sam/skin.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="yui/2.8.2r1/build/calendar/assets/skins/sam/calendar.css" type="text/css" media="all"/> 
    <link rel="stylesheet" href="yui/2.8.2r1/build/autocomplete/assets/skins/sam/autocomplete.css" type="text/css" media="all"/>                     

    <!-- Custom CSS files -->
    <link rel="stylesheet" href="css/farm-base.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="css/farm.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="css/farm-custom.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="css/farm-wide.css" type="text/css" media="all"/>
    <!--[if (IE 7)|(IE 8)]>
    <link href="css/farm-ie-7-and-8.css" type="text/css" rel="stylesheet" media="all"/>
    <![endif]-->
    <link href="css/farm-print.css" type="text/css" rel="stylesheet" media="print"/>

    <!-- Trumbowyg CSS files --> 
    <link rel="stylesheet" href="trumbowyg/dist/ui/trumbowyg.min.css">
    <link rel="stylesheet" href="trumbowyg/dist/plugins/table/ui/trumbowyg.table.min.css">
    <link rel="stylesheet" href="css/editor.css"/>

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
<body class="yui-skin-sam" onload="if (typeof onPageLoad == 'function') onPageLoad();">
<div id="bcgov-container">
  <!-- START HEADER -->
  <table id="bcgov-header">
    <tr>
      <td id="bcgov-header-logo"><a target="_blank" href="<fmt:message key="bc.gov.url"/>" title="British Columbia"><img src="images/bcgov_logo.gif" title="British Columbia" alt="British Columbia Logo" width="163" height="58"/></a></td>
      <td align="center" id="bcgov-header-title" style="padding-left:10px">
        <form name="seek1" method="get" action="http://datafind.gov.bc.ca/query.html">
          <input type="hidden" name="mi" value=""/>
          <table>
            <tr>
              <td>
              </td>
              <td style="padding-bottom:2px; vertical-align:middle">
                <input type="radio" name="qp" value="" alt="<fmt:message key="All.BC.Government"/>" />
                <span class="header-text"><fmt:message key="All.BC.Government"/> </span>
                <input type="radio" name="qp" value="url:www.al.gov.bc.ca" alt="<fmt:message key="Ministry.Name"/>" checked="checked"/>
                <span class="header-text"><fmt:message key="Ministry.Name"/></span>
              </td>
              <td>
              </td>
            </tr>
            <tr>
              <td style="padding-right:5px;">
                <img height="12" alt="Search" src="images/search.gif" width="45" />
              </td>
              <td>
                <div>
                  <input style="width:97%" type="text" name="qt" size="35" value="" maxlength="1991" class="searchBox" alt="" />
                </div>
              </td>
              <td>
                <input type="image" name="Submit" src="images/b_go.gif" alt="Go" />
              </td>
            </tr>
          </table>
        </form>
      </td>
      <td id="bcgov-header-right">
        <table style="padding-left:10px; width:100%;">
          <tr>
            <td style="text-align:right">
              <div class="header-text" style="padding-bottom:10px;text-align:center;float:right;">
                <a href="<u:screenHelpUrl screenNumber="${screenNumber}" />" title="Help" target="_new" >Help</a>
              </div>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">
              <div class="header-text" style="padding-bottom:10px;text-align:center;float:right;">
                <a href="<html:rewrite action="welcome"/>" title="<fmt:message key="Back.To.Main.Menu"/>">&laquo; <fmt:message key="Back.To.Main.Menu"/></a>
              </div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <!-- END HEADER -->
  <table id="bcgov-main-section">
    <tr>
      <td id="bcgov-content"><!-- START BREADCRUMB -->
        <div id="bcgov-breadcrumb">
          <p id="bcgov-screen-number"><w:currentAccountName/></p>
          <h1><fmt:message key="Code.Management"/></h1>
        </div>
        <!-- END BREADCRUMB -->
        <!-- START BODY CONTENT -->
        <div id="bcgov-content-body" class="yui-skin-sam">
          <!-- If this page has tabs, they will display the messages -->
          <c:if test="${empty tabName}">
            <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
          </c:if>
          <div id="bcgov-content-liner">

            <!-- START body -->
            <tiles:insert attribute="body">
              <tiles:put name="subTabs" beanName="subTabs"/>
              <tiles:put name="subTabBody" beanName="subTabBody"/>
            </tiles:insert>
            <!-- END body -->

          </div>
            <table id="bcgov-content-processing" style="margin:0 auto; height:300px; display:none">
              <tr>
                <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
                <td id="processingMessage"><fmt:message key="Processing"/>...</td>
              </tr>
            </table>    
          <div id="panel"></div>
        </div>
        <!-- END BODY CONTENT -->
      </td>
    </tr>
  </table>
  <!-- START FOOTER -->
  <table id="bcgov-footer">
    <tr>
      <td id="bcgov-footer-info"><w:applicationCode/><c:out value="${screenNumber}"/> v<fmt:message key="application.version"/></td>
      <td><a target="_blank" href="http://www.gov.bc.ca/com/copy/" id="bcgov-footer-copyright">COPYRIGHT</a></td>
      <td><a target="_blank" href="http://www.gov.bc.ca/com/disc/" id="bcgov-footer-disclaimer">DISCLAIMER</a></td>
      <td><a target="_blank" href="http://www.gov.bc.ca/com/priv/" id="bcgov-footer-privacy">PRIVACY</a></td>
      <td><a target="_blank" href="http://www.gov.bc.ca/com/accessibility/" id="bcgov-footer-accessibility">ACCESSIBILITY</a></td>
    </tr>
  </table>
  <!-- END FOOTER -->
</div>
</body>
</html:html>
