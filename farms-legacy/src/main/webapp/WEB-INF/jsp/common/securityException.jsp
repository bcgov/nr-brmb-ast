<%@page isErrorPage="true" language="java" contentType="text/plain"
    import="org.apache.struts.Globals" %><%

    Exception securityException = (Exception) request.getAttribute(Globals.EXCEPTION_KEY);
    
    try {
        response.reset();
    } catch (Exception e) {
        //System.err.println("security_failure.jsp - error resetting the response before rendering the jsp.");
    }
%>Farmer Access to Risk Management System

The following security issue has been raised. Please contact Business Support to make them aware of the issue.
The following data may assist them in diagnosing the problem.

<%= securityException.getMessage() %>
