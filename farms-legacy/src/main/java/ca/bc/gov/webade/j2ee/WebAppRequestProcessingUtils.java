package ca.bc.gov.webade.j2ee;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebAppRequestProcessingUtils {
    /**
     * The attribute name for the <code>WebADEUserInfo</code> object in the
     * <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_INFO = "webade.current.user.info";

    public static boolean preprocessRequest(ServletContext context,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException {
        return true;
    }

    public static void postprocessRequest(ServletContext context,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException {
    }
}
