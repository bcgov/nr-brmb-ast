/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.factory;

import java.util.HashMap;


/**
 * ObjectFactory.
 *
 * @author   jjobson
 */
public class ObjectFactory extends BaseFactory {

  private static final ObjectFactory INSTANCE = new ObjectFactory();

  private static HashMap<Class<?>, String> implementingMap = new HashMap<>();


  /**
   * setImplementingClass. Used by JUnit tests to specify different
   * implementations.
   *
   * @param  interfaceClass         The parameter value.
   * @param  implementingClassName  The parameter value.
   */
  public static void setImplementingClass(final Class<?> interfaceClass,
    final String implementingClassName) {
    implementingMap.put(interfaceClass, implementingClassName);
  }

  /**
   * createObject.
   *
   * @param   interfaceClass  The parameter value.
   *
   * @return  The return value.
   */
  public static Object createObject(final Class<?> interfaceClass) {
    return createObject(interfaceClass, null);
  }

  /**
   * createObject.
   *
   * @param   interfaceClass     The parameter value.
   * @param   implementationKey  The parameter value.
   *
   * @return  The return value.
   */
  public static Object createObject(final Class<?> interfaceClass,
    final Object implementationKey) {
    String implementingClassName = null;

    if (implementingMap.containsKey(interfaceClass)) {
      implementingClassName = implementingMap.get(interfaceClass);
    } else {
      implementingClassName = INSTANCE.findImplementingClassName(
          interfaceClass, implementationKey);
    }

    return INSTANCE.createInstance(interfaceClass, implementingClassName);
  }
  
  /**
   * JUnit work-around.
   * @return ObjectFactory
   */
  public static ObjectFactory getInstance() {
  	return INSTANCE;
  }
}
