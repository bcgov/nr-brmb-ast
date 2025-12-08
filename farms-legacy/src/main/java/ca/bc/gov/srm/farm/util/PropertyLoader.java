/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.util;

import java.io.InputStream;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * PropertyLoader.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public final class PropertyLoader {

  /** THROW_LOAD_ON_FAILURE. */
  private static final boolean THROW_LOAD_ON_FAILURE = true;

  /** LOAD_AS_RESOURCE_BUNDLE. */
  private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;

  /** DEFAULT_SUFFIX. */
  private static final String DEFAULT_SUFFIX = ".properties";

  /** Creates a new PropertyLoader object. */
  private PropertyLoader() {
  }


  /**
   * loadProperties.
   *
   * @param   name  The parameter value.
   *
   * @return  The return value.
   */
  public static Properties loadProperties(final String name) {
    return loadProperties(name, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Looks up a resource named 'name' in the classpath. The resource must map to
   * a file with .properties extention. The name is assumed to be absolute and
   * can use either "/" or "." for package segment separation with an optional
   * leading "/" and optional ".properties" suffix. Thus, the following names
   * refer to the same resource:
   *
   * <pre>
     some.pkg.Resource
     some.pkg.Resource.properties
     some/pkg/Resource
     some/pkg/Resource.properties
     /some/pkg/Resource
     /some/pkg/Resource.properties
   * </pre>
   *
   * @param   name    classpath resource name [may not be null]
   * @param   loader  classloader through which to load the resource [null is
   *                  equivalent to the application loader]
   *
   * @return  resource converted to java.util.Properties [may be null if the
   *          resource was not found and THROW_LOAD_ON_FAILURE is false]
   */
  public static Properties loadProperties(final String name,
    final ClassLoader loader) {
    String newName = name;

    if (newName == null) {
      throw new IllegalArgumentException("Argument name cannot be null");
    }

    if (newName.startsWith("/")) {
      newName = newName.substring(1);
    }

    if (newName.endsWith(DEFAULT_SUFFIX)) {
      newName = newName.substring(0,
          newName.length() - DEFAULT_SUFFIX.length());
    }

    Properties result = null;

    try {
      ClassLoader newLoader = loader;

      if (newLoader == null) {
        newLoader = ClassLoader.getSystemClassLoader();
      }

      if (LOAD_AS_RESOURCE_BUNDLE) {
        newName = newName.replace('/', '.');

        // Throws MissingResourceException on lookup failures:
        final ResourceBundle rb = ResourceBundle.getBundle(newName,
            Locale.getDefault(), newLoader);
        result = new Properties();

        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
          final String key = keys.nextElement();
          final String value = rb.getString(key);
          result.put(key, value);
        }
      } else {
        newName = newName.replace('.', '/');

        if (!newName.endsWith(DEFAULT_SUFFIX)) {
          newName = newName.concat(DEFAULT_SUFFIX);
        }

        try (InputStream in = newLoader.getResourceAsStream(newName);) {
          // Returns null on lookup failures:
  
          if (in != null) {
            result = new Properties();
            result.load(in); // Can throw IOException
          }
        }
      }
    } catch (Exception e) {
      result = null;
    }

    if (THROW_LOAD_ON_FAILURE && (result == null)) {
      String resType = "a classloader resource";

      if (LOAD_AS_RESOURCE_BUNDLE) {
        resType = "a resource bundle";
      }

      throw new IllegalArgumentException("could not load [" + name + "]"
        + " as " + resType);
    }

    return result;
  }
}
