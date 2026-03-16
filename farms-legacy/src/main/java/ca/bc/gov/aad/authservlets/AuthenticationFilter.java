package ca.bc.gov.aad.authservlets;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.aad.helpers.AuthException;
import ca.bc.gov.aad.helpers.AuthHelper;
import ca.bc.gov.aad.helpers.IdentityContextAdapter;
import ca.bc.gov.aad.helpers.IdentityContextAdapterServlet;
import ca.bc.gov.aad.helpers.IdentityContextData;

public class AuthenticationFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void init(FilterConfig config) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        IdentityContextAdapter contextAdapter = new IdentityContextAdapterServlet(request, response);
        IdentityContextData context = contextAdapter.getContext();
        if (!context.getAuthenticated()) {
            try {
                AuthHelper.signIn(contextAdapter);
            } catch (AuthException ex) {
                logger.error(ex.getMessage());
                logger.error(Arrays.toString(ex.getStackTrace()));
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
