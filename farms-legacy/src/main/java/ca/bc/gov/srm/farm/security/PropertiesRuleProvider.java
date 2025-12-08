/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.security;

import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.util.PropertyLoader;

import org.apache.commons.lang.StringUtils;

import java.util.Properties;


/**
 * PropertiesRuleProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
final class PropertiesRuleProvider extends SecurityRuleProvider {

  /** properties. */
  private Properties properties = null;

  /**
   * Creates a new PropertiesRuleProvider object.
   *
   * @param  value  Input parameter to initialize class.
   */
  PropertiesRuleProvider(final Properties value) {
    initialize(value);
  }

  /**
   * Creates a new PropertiesRuleProvider object.
   *
   * @param  path  Input parameter to initialize class.
   */
  PropertiesRuleProvider(final String path) {
    initialize(path);
  }

  /**
   * getSecurityRule.
   *
   * @param   key  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public SecurityRule getSecurityRule(final String key)
    throws ProviderException {
    checkInitialized();

    SecurityRule result = new SecurityRule();
    String ruleName = properties.getProperty(key);

    if (StringUtils.isBlank(ruleName)) {
      ruleName = key;
    }

    result.setRuleName(ruleName);

    return result;
  }

  /**
   * getSecurityRule.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public SecurityRule getSecurityRule(final BusinessAction businessAction)
    throws ProviderException {
    checkInitialized();

    if (businessAction == null) {
      throw new IllegalArgumentException("businessAction cannot be null.");
    }

    return getSecurityRule(businessAction.getActionName());
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    if (resource == null) {
      throw new IllegalArgumentException("resource cannot be null.");
    }

    if (!((resource instanceof Properties) || (resource instanceof String))) {
      throw new IllegalArgumentException(
        "resource is expected to be Properties or String, not "
        + resource.getClass().getName());
    }

    if (resource instanceof Properties) {
      properties = (Properties) resource;
    } else if (resource instanceof String) {
      properties = PropertyLoader.loadProperties((String) resource);
    }

    setInitialized(properties != null);
  }

}
