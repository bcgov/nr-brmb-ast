/**
 *
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.util.Arrays;
import java.util.List;

public final class ObjectUtils {
  
  /** private constructor */
  private ObjectUtils() {
  }

  public static boolean isOneOf(Object o, Object... list) {
    return Arrays.asList(list).contains(o);
  }
  
  public static boolean listContainsReference(List<?> list, Object o1) {
    return list.stream().filter(o2 -> o2 == o1).findFirst().isPresent();
  }
  
  public static <T> T ifNull(T t1, T t2) {
    if(t1 != null) {
      return t1;
    }
    return t2;
  }
}
