/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.lang.reflect.Array;

import java.util.Collection;


/**
 * BaseObject.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class BaseObject {

  /** HASH_SEED. */
  protected static final int HASH_SEED = 23;

  /** ODD_PRIME_NUMBER. */
  protected static final int ODD_PRIME_NUMBER = 27;

  /**
   * equals.
   *
   * @param   that  The parameter value.
   *
   * @return  The return value.
   */
  @Override
  public boolean equals(final Object that) {

    if (this == that) {
      return true;
    }

    if (that == null) {

      if (this == null) {
        return true;
      }
    } else {

      if (!that.getClass().isInstance(this)) {
        return false;
      }
    }

    return this.hashCode() == that.hashCode();
  }

  /**
   * hashCode.
   *
   * @return  The return value.
   */
  @Override
  public int hashCode() {
    return generateHashCode();
  }

  /**
   * generateHashCode.
   *
   * @return  The return value.
   */
  protected abstract int generateHashCode();

  /**
   * arrayToString.
   *
   * @param   name   The parameter value.
   * @param   array  The parameter value.
   *
   * @return  The return value.
   */
  protected String arrayToString(final String name, final Object[] array) {

    //helper method for toString...
    if (array == null) {
      return name + "=[]";
    }

    StringBuffer buf = new StringBuffer();
    buf.append(name).append("=[");

    if (array != null) {

      for (int i = 0; i < array.length; i++) {
        buf.append(array[i]);

        if (i < (array.length - 1)) {
          buf.append(",");
        }
      }

    }

    buf.append("]");

    return buf.toString();
  }

  /**
   * classnameToString.
   *
   * @return  The return value.
   */
  protected String classnameToString() {
    String result = this.getClass().getName();
    result = result.substring(result.lastIndexOf(".") + 1);

    return result;
  }

  /**
   * collectionToString.
   *
   * @param   name  The parameter value.
   * @param   c     The parameter value.
   *
   * @return  The return value.
   */
  protected String collectionToString(final String name, final Collection c) {

    //helper method for toString...
    if (c == null) {
      return name + "=[]";
    }

    Object[] array = c.toArray();

    return arrayToString(name, array);
  }

  /**
   * hash.
   *
   * @param   aSeed     The parameter value.
   * @param   aBoolean  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final boolean aBoolean) {

    if (aBoolean) {
      return firstTerm(aSeed) + 1;
    } else {

      return firstTerm(aSeed);
    }
  }

  /**
   * hash.
   *
   * @param   aSeed  The parameter value.
   * @param   aChar  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final char aChar) {
    return firstTerm(aSeed) + aChar;
  }

  /**
   * hash.
   *
   * @param   aSeed  The parameter value.
   * @param   aInt   The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final int aInt) {
    return firstTerm(aSeed) + aInt;
  }

  /**
   * hash.
   *
   * @param   aSeed  The parameter value.
   * @param   aLong  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final long aLong) {
    final int shiftInt = 32;

    return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> shiftInt));
  }

  /**
   * hash.
   *
   * @param   aSeed   The parameter value.
   * @param   aFloat  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final float aFloat) {
    return hash(aSeed, Float.floatToIntBits(aFloat));
  }

  /**
   * hash.
   *
   * @param   aSeed    The parameter value.
   * @param   aDouble  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final double aDouble) {
    return hash(aSeed, Double.doubleToLongBits(aDouble));
  }

  /**
   * hash.
   *
   * @param   aSeed    The parameter value.
   * @param   aObject  The parameter value.
   *
   * @return  The return value.
   */
  protected final int hash(final int aSeed, final Object aObject) {
    int result = aSeed;

    if (aObject == null) {
      result = hash(result, 0);
    } else if (!isObjectAnArray(aObject)) {
      result = hash(result, aObject.hashCode());
    } else {
      int length = Array.getLength(aObject);

      for (int idx = 0; idx < length; ++idx) {
        Object item = Array.get(aObject, idx);

        // recursive call!
        result = hash(result, item);
      }
    }

    return result;
  }

  /**
   * firstTerm.
   *
   * @param   aSeed  The parameter value.
   *
   * @return  The return value.
   */
  private int firstTerm(final int aSeed) {
    return ODD_PRIME_NUMBER * aSeed;
  }

  /**
   * isObjectAnArray.
   *
   * @param   aObject  Input parameter.
   *
   * @return  The return value.
   */
  private boolean isObjectAnArray(final Object aObject) {
    return aObject.getClass().isArray();
  }


}
