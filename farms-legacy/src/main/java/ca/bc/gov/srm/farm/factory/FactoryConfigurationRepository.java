/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * FactoryConfigurationRepository.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 4412 $
 */
public final class FactoryConfigurationRepository {

  /** INSTANCE. */
  private static final FactoryConfigurationRepository INSTANCE =
    new FactoryConfigurationRepository();

  /** paths. */
  private Map<String, List<String>> paths = new LinkedHashMap<>();

  /** Creates a new FactoryConfigurationRepository object. */
  private FactoryConfigurationRepository() {
  }

  /**
   * addResourcePath.
   *
   * @param  factoryClass  The parameter value.
   * @param  resourcePath  The parameter value.
   */
  public static void addResourcePath(Class factoryClass,
    String resourcePath) {
    List<String> list = INSTANCE.paths.get(factoryClass.getName());

    if (list == null) {
      list = new ArrayList<>();
    }

    if (!list.contains(resourcePath)) {
      list.add(resourcePath);
    }

    INSTANCE.paths.put(factoryClass.getName(), list);
  }

  /**
   * getResourcePaths.
   *
   * @param   factoryClass  Input parameter.
   *
   * @return  The return value.
   */
  public static List<String> getResourcePaths(Class factoryClass) {
    return INSTANCE.paths.get(factoryClass.getName());
  }

}
