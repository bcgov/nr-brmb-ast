package ca.bc.gov.srm.farm.ui.j2ee;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;

/**
 * 
 */
public class RestrictHostFilter implements Filter {

  private static Logger logger = LoggerFactory.getLogger(RestrictHostFilter.class);

  @SuppressWarnings("unused")
  private FilterConfig filterConfig;
  
  private String ipRegex;

	@Override
  public void destroy() {
	  // do nothing
	}
	
  /**
   * doFilter.
   *
   * @param   request   The parameter value.
   * @param   response  The parameter value.
   * @param   chain     The parameter value.
   *
   * @throws  IOException       On exception.
   * @throws  ServletException  On exception.
   */
	@Override
  public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(ipRegex == null){
			try {
				if ((request instanceof HttpServletRequest)
			            && (response instanceof HttpServletResponse)) {
					HttpServletRequest httpRequest = (HttpServletRequest) request;
					WebADEPreferenceSet prefSet = HttpRequestUtils.getApplication(httpRequest.getSession().getServletContext()).getWebADEApplicationPreferences().getPreferenceSet("app-config","webservice");
					if(prefSet != null) {
						ipRegex = prefSet.getPreference("allowable.ip.regex").getPreferenceValue();
					}
				}
				if(ipRegex == null) {
					logger.error("Unable to configure the RestrictHostFilter.  Missing preference value for 'allowable.ip.regex'");
					throw new RuntimeException("Unable to configure the RestrictHostFilter.  Missing preference value for 'allowable.ip.regex'");
				}
			} catch (WebADEException e) {
				logger.error("Unexpected error: ", e);
			}
		}
		if ((request instanceof HttpServletRequest)
	            && (response instanceof HttpServletResponse)) {
	          HttpServletRequest httpRequest = (HttpServletRequest) request;
	          String inIP = httpRequest.getRemoteAddr();
	          Pattern p = Pattern.compile(ipRegex);
	          // Create a matcher with an input string
	          Matcher m = p.matcher(inIP);
	          if( m.find() ){
	        	  // Let the request through
	        	  logger.debug("A request was made to the server and the caller ip address " + inIP + " matched the allowable.ip.regex (" + ipRegex + ")");
	        	  chain.doFilter(request, response);
	          } else {
	        	  // Stop the request, the ip doesn't match
	        	  logger.warn("A request was made to the server and the caller ip address " + inIP + " did not match the allowable.ip.regex (" + ipRegex + ")");
	          }
		}

	}

   /**
   * init.
   *
   * @param   config  The parameter value.
   *
   * @throws  ServletException  On exception.
   */
	@Override
  public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}

}
