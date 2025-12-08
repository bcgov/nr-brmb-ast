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
    <link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/assets/skins/sam/skin.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/calendar/assets/skins/sam/calendar.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="yui/2.8.2r1/build/autocomplete/assets/skins/sam/autocomplete.css" media="all"/>
    
    <!-- Custom CSS files -->
    <link rel="stylesheet" type="text/css" href="css/farm-base.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/farm.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/farm-custom.css" media="all"/>
    <!--[if (IE 7)|(IE 8)]>
    <link href="css/farm-ie-7-and-8.css" type="text/css" rel="stylesheet" media="all"/>
    <![endif]-->
    <link href="css/farm-print.css" type="text/css" rel="stylesheet" media="print"/>
    
    <!-- YUI JS files -->
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
          <td id="bcgov-header-logo"><a href="<fmt:message key="bc.gov.url"/>" title="British Columbia"><img src="images/bcgov_logo.gif" title="British Columbia" alt="British Columbia Logo" width="163" height="58"/></a></td>
          <td id="bcgov-header-title" style="padding-left:10px">
            <form name="seek1" method="get" action="http://datafind.gov.bc.ca/query.html">
              <input type="hidden" name="mi" value=""/>
              <table>
                <tr>
                  <td>
                  </td>
                  <td style="padding-bottom:2px; vertical-align:middle">
                    <input type="radio" name="qp" value="" alt="<fmt:message key="All.BC.Government"/>" />
                    <span class="header-text"><fmt:message key="All.BC.Government"/></span>
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
                  <div class="header-text" style="padding-bottom:10px;">
                    <a href="<fmt:message key="bc.gov.mainindex.url"/>" title="Main Index">Main Index</a>
                    <a href="<u:screenHelpUrl screenNumber="${screenNumber}" />" title="Help" target="_new" >Help</a>
                    <a href="<fmt:message key="bc.gov.contact.url"/>" title="Contact Us">Contact Us</a>
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
          <td id="bcgov-menu">
            <!-- START MENU -->
            <!-- START SERVICE LINKS -->
            <div id="bcgov-service">
              <div id="bcgov-service-top"><img src="images/bcgov_navBlueTop.gif" title="" alt="" /></div>
              <div id="bcgov-service-links">
                <div id="bcgov-service-bchome"><a href="<fmt:message key="bc.gov.url"/>" title="B.C. Home">B.C. Home</a></div>
                <div id="bcgov-service-ministry"><a href="<fmt:message key="bc.gov.al.url"/>" title="<fmt:message key="Ministry.Name"/>"><fmt:message key="Ministry.Name"/></a></div>
                <div id="bcgov-service-application"><a href="<html:rewrite action="welcome"/>" title="<fmt:message key="application.title"/>"><w:applicationCode/> - <fmt:message key="application.title"/></a></div>
              </div>
            </div>
            <!-- END SERVICE LINKS -->
            <%@ include file="/WEB-INF/jsp/common/navigation.jsp" %>
            <div class="related-links">
              <table cellpadding="0" cellspacing="0" class="related-links">
                <tr>
                  <th>Other Links</th>
                </tr>
                <tr>
                  <td> 
                    <ul class="related-links">
                      <li><a href="http://www.agf.gov.bc.ca/sitemap_text.htm">Other Ministry Programs &amp; Services</a></li>
                      <li><a href="http://infobasket.gov.bc.ca/portal/server.pt?">Infobasket</a></li>
                      <li><a href="http://www4.agr.gc.ca/AAFC-AAC/display-afficher.do?id=1200408916804&amp;lang=eng">Federal Business Risk Management</a></li>
                      <li><a href="http://www.theweathernetwork.com/weather/cities/indexBC.htm">Weather</a></li>
                      <li><a href="http://www.gov.bc.ca/agf/">BCMAL Home</a></li>
                    </ul>
                  </td>
                </tr>
              </table>
            </div>
            <div class="bcgov-menu-section-grey-end">&nbsp;</div>
            <div id="bcgov-menu-spacer"></div>
            <!-- END MENU -->
          </td> 
          <td id="bcgov-content">
            <!-- START BREADCRUMB -->
            <div id="bcgov-breadcrumb">
              <p id="bcgov-screen-number"><w:currentUser/></p>
              <ul>
                <li><a href="<fmt:message key="bc.gov.url"/>" title="B.C. Home" >B.C. Home</a></li>
                <li> &gt; <a href="<fmt:message key="bc.gov.al.url"/>" title="<fmt:message key="Ministry.Name"/>" ><fmt:message key="Ministry.Name"/></a></li>
                <li> &gt; <a href="<fmt:message key="bc.gov.agristability.url"/>">AgriStability</a></li>
                <li> &gt; <a href="<html:rewrite action="welcome"/>"><w:applicationCode/></a></li>
              </ul>
            </div>
            <!-- END BREADCRUMB -->
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
          <td><a href="<fmt:message key="bc.gov.copyright.url"/>" id="bcgov-footer-copyright">COPYRIGHT</a></td>
          <td><a href="<fmt:message key="bc.gov.disclaimer.url"/>" id="bcgov-footer-disclaimer">DISCLAIMER</a></td>
          <td><a href="<fmt:message key="bc.gov.privacy.url"/>" id="bcgov-footer-privacy">PRIVACY</a></td>
          <td><a href="<fmt:message key="bc.gov.accessibility.url"/>" id="bcgov-footer-accessibility">ACCESSIBILITY</a></td>
        </tr>
      </table>
      <!-- END FOOTER -->
    </div>
  </body>
</html:html>