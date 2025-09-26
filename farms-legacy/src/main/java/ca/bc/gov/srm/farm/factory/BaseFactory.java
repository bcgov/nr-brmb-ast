/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.PropertyLoader;


/**
 * BaseFactory.
 *
 * @author   awilkinson
 */
public abstract class BaseFactory {

  private static final String DEFAULT_RESOURCE_PATH =
    "config/implementation.properties";
  
  private String overrideResourcePath;

  private Logger log = LoggerFactory.getLogger(getClass());

  private Properties properties = null;

  /**
   * createInstance.
   *
   * @param   classToCreate          The parameter value.
   * @param   implementingClassName  The parameter value.
   *
   * @return  The return value.
   */
  protected Object createInstance(final Class<?> classToCreate,
    final String implementingClassName) {

    if (classToCreate == null) {
      throw new IllegalArgumentException("classToCreate cannot be null.");
    }

    if (StringUtils.isBlank(implementingClassName)) {
      throw new IllegalArgumentException(
        "implementingClassName cannot be null.");
    }

    Object result = null;
    Class<?> implementationClass = null;

    try {
      implementationClass = Class.forName(implementingClassName);
    } catch (ClassNotFoundException e) {
      String msg = implementingClassName + " cannot be found.";

      if (log.isErrorEnabled()) {
        log.error(msg, e);
      }

      throw new RuntimeException(e);
    }

    if (!classToCreate.isAssignableFrom(implementationClass)) {
      String msg = implementingClassName + " does not implement "
        + classToCreate.getName() + ".";

      if (log.isErrorEnabled()) {
        log.error(msg);
      }

      throw new IllegalArgumentException(msg);
    }

    // now try to create the class....
    try {

      try {
        result = implementationClass.newInstance();
      } catch (IllegalAccessException e) {

        // may have private or default constructors...
        result = createObjectByConstructor(implementationClass);
      } catch (InstantiationException e) {

        // may have private or default constructors...
        result = createObjectByConstructor(implementationClass);

      }

      if (log.isDebugEnabled()) {
        log.debug("created instance=[" + classToCreate.getName()
          + "], implementation=[" + implementationClass.getName() + "]");
      }
    } catch (Exception e) {
      String errorMsg = "Unable to construct instance of "
        + classToCreate.getName() + ".";
      log.error(errorMsg, e);
      throw new RuntimeException(errorMsg, e);
    }

    return result;
  }

  /**
   * createProxiedInstance.
   *
   * @param   classToCreate          The parameter value.
   * @param   implementingClassName  The parameter value.
   * @param   proxy                  The parameter value.
   *
   * @return  The return value.
   */
  protected Object createProxiedInstance(final Class<?> classToCreate,
    final String implementingClassName, final BaseProxy proxy) {
    Object result = null;
    Object delegate = createInstance(classToCreate, implementingClassName);

    if (proxy != null) {
      ClassLoader classLoader = classToCreate.getClassLoader();
      Class<?>[] classes = new Class[] {classToCreate};
      proxy.initialize(delegate);
      result = Proxy.newProxyInstance(classLoader, classes, proxy);
    } else {
      result = delegate;
    }

    return result;
  }

  /**
   * findImplementingClassName.
   *
   * @param   classToCreate      The parameter value.
   * @param   implementationKey  The parameter value.
   *
   * @return  The return value.
   */
  protected String findImplementingClassName(final Class<?> classToCreate,
    final Object implementationKey) {

    if (classToCreate == null) {
      throw new IllegalArgumentException("classToCreate cannot be null.");
    }
    // there are 2 reasons why the implementationKey may be null
    // 1. want the default implementation
    // 2. classToCreate is the implementing class and we are going to
    // create a proxy for it.

    String key = classToCreate.getName();

    if (implementationKey != null) {
      key = (classToCreate.getName() + "." + implementationKey.toString());
    }

    //now see if the key returns an implementing class name
    String result = getProperties().getProperty(key);

    if (StringUtils.isBlank(result)) {
      result = classToCreate.getName();
    }

    if (log.isDebugEnabled()) {
      log.debug("find implementation for instance=[" + classToCreate.getName()
        + "], key=[" + key + "], implementation=[" + result + "]");
    }

    return result;
  }

  protected String getResourcePath() {
    return DEFAULT_RESOURCE_PATH;
  }
  
  public String getOverrideResourcePath() {
    return overrideResourcePath;
  }
  
  public void setOverrideResourcePath(String overrideResourcePath) {
    this.overrideResourcePath = overrideResourcePath;
  }


  /**
   * createObjectByConstructor.
   *
   * @param   implementationClass  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  InstantiationException     On Exception
   * @throws  IllegalAccessException     On Exception
   * @throws  InvocationTargetException  On Exception
   */
  private Object createObjectByConstructor(final Class<?> implementationClass)
    throws InstantiationException, IllegalAccessException,
      InvocationTargetException {
    Object result = null;
    Constructor<?>[] constructors = implementationClass.getDeclaredConstructors();

    for (int i = 0; i < constructors.length; i++) {

      if (constructors[i].getParameterTypes().length == 0) {

        if (constructors[i] != null) {
          constructors[i].setAccessible(true);
          result = constructors[i].newInstance(new Object[0]);
        }
      }
    }

    return result;
  }

  /**
   * getProperties.
   *
   * @return  The return value.
   */
  protected Properties getProperties() {

    if (properties == null) {
      List<String> resourcePaths = FactoryConfigurationRepository.getResourcePaths(this.getClass());

      if ((resourcePaths == null) || (resourcePaths.size() == 0)) {

        //use the default...
        if (log.isDebugEnabled()) {
          log.debug("Initializing from default resource: " + getResourcePath());
        }

        properties = PropertyLoader.loadProperties(getResourcePath());
        
        String overridePath = getOverrideResourcePath();
        URL overrideResource = getClass().getResource("/"+getOverrideResourcePath());
        if(overrideResource != null) {
          Properties overrideProperties = PropertyLoader.loadProperties(overridePath);
          properties.putAll(overrideProperties);
        }
        
      } else {
        Iterator<String> iter = resourcePaths.iterator();
        properties = new Properties();

        if (log.isDebugEnabled()) {
          log.debug("Initializing from configured resources.");
        }

        while (iter.hasNext()) {
          String resourcePath = iter.next();

          if (log.isDebugEnabled()) {
            log.debug("loading from " + resourcePath);
          }

          Properties rp = PropertyLoader.loadProperties(resourcePath);
          properties.putAll(rp);
        }
      }
    }

    return properties;
  }

  
  /**
   * JUnit work-around.
   * @param key key
   * @param implementingClass implementingClass
   */
  public void setImplementingClass(String key, String implementingClass) {
  	getProperties().put(key, implementingClass);
  }
}
