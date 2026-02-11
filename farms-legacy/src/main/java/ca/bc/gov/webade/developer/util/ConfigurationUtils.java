package ca.bc.gov.webade.developer.util;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * Helper class for dealing with the Configuration.
 * 
 * @author Vivid Solutions Inc
 */
public final class ConfigurationUtils {

  /**
   * Check first in system properties them in servlet context for property
   * value.
   * 
   * @param context The servlet context.
   * @param property The property we are looking for.
   * @return The value of the property or null if not found.
   */
  public static String findProperty(ServletContext context, String property) {
    String result = null;
    if (context != null) {
      result = System.getProperty(property);
    }
    if (result == null) {
      result = context.getInitParameter(property);
    }
    return result;
  }

  /**
   * Check first in system properties them in filterConfig for property value.
   * 
   * @param filterConfig The servlet filter config.
   * @param property The property we are looking for.
   * @return The value of the property or null if not found.
   */
  public static String findProperty(FilterConfig filterConfig, String property) {
    String result = System.getProperty(property);
    if (result == null && filterConfig != null) {
      result = filterConfig.getInitParameter(property);
    }
    return result;
  }

}
