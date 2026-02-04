/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.sort;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.collections.comparators.TransformingComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;


/**
 * SortUtils.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class SortUtils {

  /** Creates a new SortUtils object. */
  protected SortUtils() {
  }


  /**
   * sort.
   *
   * @param   array      The parameter value.
   * @param   sortOrder  The parameter value.
   *
   * @return  The return value.
   */
  public static Object[] sort(final Object[] array, final String[] sortOrder) {
    return sort(array, sortOrder, null);
  }


  /**
   * sort.
   *
   * @param   array       The parameter value.
   * @param   comparator  The parameter value.
   *
   * @return  The return value.
   */
  public static Object[] sort(final Object[] array,
    final Comparator comparator) {

    // create collection
    List list = new ArrayList();

    for (int i = 0; i < array.length; i++) {
      list.add(array[i]);
    }

    Collections.sort(list, comparator);

    // convert list back to array
    ListIterator i = list.listIterator();

    while (i.hasNext()) {
      array[i.nextIndex()] = i.next();
    }

    return array;
  }


  /**
   * sort.
   *
   * @param   array        The parameter value.
   * @param   sortOrder    The parameter value.
   * @param   reverseSort  The parameter value.
   *
   * @return  The return value.
   */
  public static Object[] sort(final Object[] array, final String[] sortOrder,
    final boolean[] reverseSort) {

    // Create comparator chain
    ComparatorChain chain = new ComparatorChain();

    for (int i = 0; i < sortOrder.length; i++) {

      // Case Insensitive Comparator
      Transformer transformer = new Transformer() {
          @Override
          public Object transform(final Object input) {

            if (input instanceof String) {
              return ((String) input).toLowerCase();
            }

            return input;
          }
        };

      Comparator c = new TransformingComparator(transformer,
          new NullComparator());

      // Null Safe Bean Comparator
      c = new NullSafeBeanComparator(sortOrder[i], c);

      if ((reverseSort != null) && (i < reverseSort.length)) {

        if (reverseSort[i]) {
          c = new ReverseComparator(c);
        }
      }

      chain.addComparator(c);
    }

    // create collection
    List list = new ArrayList();

    for (int i = 0; i < array.length; i++) {
      list.add(array[i]);
    }

    Collections.sort(list, chain);

    // convert list back to array
    ListIterator i = list.listIterator();

    while (i.hasNext()) {
      array[i.nextIndex()] = i.next();
    }

    return array;
  }
}
