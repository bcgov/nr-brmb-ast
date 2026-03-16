package ca.bc.gov.aad.authservlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.aad.msal4j.IAccount;

import ca.bc.gov.aad.helpers.AuthException;
import ca.bc.gov.aad.helpers.AuthHelper;
import ca.bc.gov.aad.helpers.IdentityContextAdapter;
import ca.bc.gov.aad.helpers.IdentityContextAdapterServlet;
import ca.bc.gov.aad.helpers.IdentityContextData;
import ca.bc.gov.webade.user.security.enterprise.SiteminderConstants;

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
            IAccount account = context.getAccount();
            String userType = SiteminderConstants.INTERNAL_USERTYPE;
            String userIdentifier = account.homeAccountId().replace("-", "").toUpperCase();
            String userGuid = userIdentifier;
            String email = account.username();
            HttpServletRequest wrappedRequest = new HeaderRequestWrapper(request, userType, userIdentifier, userGuid,
                    email);
            chain.doFilter(wrappedRequest, response);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private static class HeaderRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String> customHeaders = new HashMap<>();

        public HeaderRequestWrapper(HttpServletRequest request, String userType, String userIdentifier, String userGuid,
                String email) {
            super(request);
            customHeaders.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, userType);
            customHeaders.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, userIdentifier);
            customHeaders.put(SiteminderConstants.SMGOV_USERGUID_HEADER, userGuid);
            customHeaders.put(SiteminderConstants.SMGOV_EMAIL_HEADER, email);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return Collections.enumeration(Collections.singletonList(headerValue));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> names = new HashSet<>(customHeaders.keySet());
            Enumeration<String> originalNames = super.getHeaderNames();
            while (originalNames.hasMoreElements()) {
                names.add(originalNames.nextElement());
            }
            return Collections.enumeration(names);
        }
    }
}
