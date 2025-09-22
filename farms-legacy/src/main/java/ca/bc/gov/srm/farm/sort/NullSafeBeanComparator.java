/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.sort;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;


/**
 * NullSafeBeanComparator.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public class NullSafeBeanComparator extends BeanComparator {

  /** serialVersionUID. */
  private static final long serialVersionUID = 6345325068182901954L;

  /** nullsAreHigh. */
  private boolean nullsAreHigh = true;

  /** log. */
  private Logger log = LoggerFactory.getLogger(NullSafeBeanComparator.class);

  /**
   * Creates a new NullSafeBeanComparator object.
   *
   * @param  prop  Input parameter to initialize class.
   * @param  c     Input parameter to initialize class.
   */
  public NullSafeBeanComparator(final String prop, final Comparator c) {
    super(prop, c);
  }

  /**
   * Creates a new NullSafeBeanComparator object.
   *
   * @param  prop         Input parameter to initialize class.
   * @param  c            Input parameter to initialize class.
   * @param  nullAreHigh  Input parameter to initialize class.
   */
  public NullSafeBeanComparator(final String prop, final Comparator c,
    final boolean nullAreHigh) {
    super(prop, c);
    this.nullsAreHigh = nullAreHigh;
  }

  /**
   * compare.
   *
   * @param   o1  The parameter value.
   * @param   o2  The parameter value.
   *
   * @return  The return value.
   */
  @Override
  public int compare(final Object o1, final Object o2) {

    if (this.getProperty() == null) {

      // use the object's compare since no property is specified
      return this.getComparator().compare(o1, o2);
    }

    Object val1 = null, val2 = null;

    try {

      try {
        val1 = PropertyUtils.getProperty(o1, getProperty());
        // } catch (NestedNullException ignored) {
      } catch (Exception ignored) {
        val1 = null;
      }

      try {
        val2 = PropertyUtils.getProperty(o2, getProperty());
        // } catch (NestedNullException ignored) {
      } catch (Exception ignored) {
        val2 = null;
      }

      if ((val1 == val2) || ((val1 == null) && (val2 == null))) {
        return -1;
      }

      if (val1 == null) {

        if (this.nullsAreHigh) {
          return 1;
        } else {
          return -1;
        }
      }

      if (val2 == null) {

        if (this.nullsAreHigh) {
          return -1;
        } else {
          return 1;
        }
      }

      return this.getComparator().compare(val1, val2);
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("Unexpected error: ", e);

      return 0;
    }
  }

  /**
   * isNullsAreHigh.
   *
   * @return  The return value.
   */
  public boolean isNullsAreHigh() {
    return nullsAreHigh;
  }

  /**
   * Sets the value for nulls are high.
   *
   * @param  areNullsHigh  Input parameter.
   */
  public void setNullsAreHigh(final boolean areNullsHigh) {
    this.nullsAreHigh = areNullsHigh;
  }

}
