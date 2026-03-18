package ca.bc.gov.aad;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.bc.gov.aad.helpers.AuthException;
import ca.bc.gov.aad.helpers.AuthHelper;
import ca.bc.gov.aad.helpers.Config;
import ca.bc.gov.aad.helpers.IdentityContextAdapterServlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AADRedirectServlet", urlPatterns = "/auth/redirect")
public class AADRedirectServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(AADRedirectServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        logger.log(Level.FINE, "Request has come with params {0}", req.getQueryString());
        try {
            AuthHelper.processAADCallback(new IdentityContextAdapterServlet(req, resp));
            logger.log(Level.INFO, "redirecting to home page.");
            resp.sendRedirect(Config.HOME_PAGE);
        } catch (AuthException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            logger.log(Level.INFO, "redirecting to error page to display auth error to user.");
            try {
                RequestDispatcher rd = req.getRequestDispatcher(String.format("/auth_error_details?details=%s", URLEncoder.encode(ex.getMessage(), "UTF-8")));
                rd.forward(req, resp);
            } catch (Exception except) {
                except.printStackTrace();
            }
        }
    }

}
