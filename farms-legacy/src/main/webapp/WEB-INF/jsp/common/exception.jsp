<%@page isErrorPage="true" language="java" contentType="text/plain"
   import="org.apache.struts.Globals,
           org.apache.commons.lang.StringUtils,
           java.io.*" %><%@
 taglib prefix="w" uri="http://gov.bc.ca/struts/tags/webade" %><%

  Exception theException = (Exception) request.getAttribute(Globals.EXCEPTION_KEY);
 
  String exceptionTraceback = null;
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    theException.printStackTrace(pw);
    exceptionTraceback = sw.toString();
  }
  try {
      response.reset();
  } catch (Exception e) {
    // do nothing
  }
  
  Throwable theCause = theException.getCause();
  String causeTraceback = null;
  if(theCause != null) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    theCause.printStackTrace(pw);
    causeTraceback = sw.toString();
  }

%>Farmer Access to Risk Management System

An unexpected error has happened. Please contact Business Support to make them aware of the issue.
The following data may assist them in diagnosing the problem. 

DETAILS

Application: <w:applicationCode/>
Current User: <w:currentUser/>


Exception:

<%=exceptionTraceback%>



<%
  if(causeTraceback != null) {
%>
Cause:

<%=causeTraceback%>

<%
  }
%>

