/**
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import java.util.Calendar;
import java.util.Map;


/**
 * DAOStoredProcedure.
 */
public class DAOStoredProcedure implements AutoCloseable {

  /** NO. */
  public static final String NO = "N";

  /** YES. */
  public static final String YES = "Y";

  /** NO_RETURN_TYPE. */
  protected static final int NO_RETURN_TYPE = -1;

  /** statement. */
  private CallableStatement stmt;

  /** paramValues. */
  private String[] paramValues;

  /** procName. */
  private String prcName;

  /** returnType. */
  private int rtrnType;

  /** Creates a new DAOStoredProcedure object. */
  protected DAOStoredProcedure() {
  }

  /**
   * Creates a new DAOStoredProcedure object.
   *
   * @param   connection    Input parameter to initialize class.
   * @param   procName      Input parameter to initialize class.
   * @param   paramCount    Input parameter to initialize class.
   * @param   returnsValue  Input parameter to initialize class.
   *
   * @throws  SQLException  On exception.
   */
  public DAOStoredProcedure(final Connection connection, final String procName,
    final int paramCount, final boolean returnsValue) throws SQLException {

    if (returnsValue) {
      init(connection, procName, paramCount, oracle.jdbc.OracleTypes.CURSOR);
    } else {
      init(connection, procName, paramCount, NO_RETURN_TYPE);
    }
  }

  /**
   * Creates a new DAOStoredProcedure object.
   *
   * @param   connection  Input parameter to initialize class.
   * @param   procName    Input parameter to initialize class.
   * @param   paramCount  Input parameter to initialize class.
   * @param   returnType  Input parameter to initialize class.
   *
   * @throws  SQLException  On exception.
   */
  public DAOStoredProcedure(final Connection connection, final String procName,
    final int paramCount, final int returnType) throws SQLException {
    init(connection, procName, paramCount, returnType);
  }


  /**
   * @return  x
   *
   * @see     java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    StringBuffer result = new StringBuffer();

    result.append("\r\nSQL Call: ");

    result.append(getPrepareCallSql());

    if (paramValues.length > 0) {
      result.append("\r\nParameter Values:");

      if (returnsValue()) {
        result.append("\r\n\tReturn Value " + getReturnTypeString());
      }

      for (int i = 0; i < paramValues.length; i++) {
        result.append("\r\n\t");
        result.append(paramValues[i]);
      }
    }

    return result.toString();
  }


  /**
   * @return  x
   */
  protected final String getReturnTypeString() {
    String str = "";

    if (getReturnType() == oracle.jdbc.OracleTypes.CURSOR) {
      str = "CURSOR";
    } else if (getReturnType() == oracle.jdbc.OracleTypes.VARCHAR) {
      str = "VARCHAR";
    }

    return str;
  }

  /**
   * getProcName.
   *
   * @return  The return value.
   */
  public final String getProcName() {
    return prcName;
  }

  /**
   * getNumParameters.
   *
   * @return  The return value.
   */
  public final int getNumParameters() {
    return paramValues.length;
  }

  /**
   * registerOutParameter.
   *
   * @param   index    The parameter value.
   * @param   sqlType  The parameter value.
   *
   * @throws  SQLException  On exception.
   */
  public final void registerOutParameter(final int index, final int sqlType)
    throws SQLException {
    stmt.registerOutParameter(getBindIndex(index), sqlType);
  }

  /**
   * registerOutParameter.
   *
   * @param   index    The parameter value.
   * @param   sqlType  The parameter value.
   * @param   scale    The parameter value.
   *
   * @throws  SQLException  On exception.
   */
  public final void registerOutParameter(final int index, final int sqlType,
    final int scale) throws SQLException {
    stmt.registerOutParameter(getBindIndex(index), sqlType, scale);
  }

  /**
   * registerOutParameter.
   *
   * @param   index     The parameter value.
   * @param   sqlType   The parameter value.
   * @param   typeName  The parameter value.
   *
   * @throws  SQLException  On exception.
   */
  public final void registerOutParameter(final int index, final int sqlType,
    final String typeName) throws SQLException {
    stmt.registerOutParameter(getBindIndex(index), sqlType, typeName);
  }

  /**
   * wasNull.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final boolean wasNull() throws SQLException {
    return stmt.wasNull();
  }

  /**
   * getMetaData.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final ResultSetMetaData getMetaData() throws SQLException {
    return stmt.getMetaData();
  }

  /**
   * addBatch.
   *
   * @throws  SQLException  On exception.
   */
  public final void addBatch() throws SQLException {
    stmt.addBatch();
  }

  /**
   * clearParameters.
   *
   * @throws  SQLException  On exception.
   */
  public final void clearParameters() throws SQLException {
    stmt.clearParameters();
  }

  /**
   * close.
   *
   * @throws  SQLException  On exception.
   */
  @Override
  public final void close() throws SQLException {
    stmt.close();
  }

  /**
   * execute.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final boolean execute() throws SQLException {
    return stmt.execute();
  }
  
  /**
   * @return int[] of rows updated or success/fail status
   * @see java.sql.Statement#executeBatch()
   * 
   * @throws SQLException On exception.
   */
  public final int[] executeBatch() throws SQLException {
    return stmt.executeBatch();
  }

  /**
   * getResultSet.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final ResultSet getResultSet() throws SQLException {
    return (ResultSet) getResult();
  }

  /**
   * getResult.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Object getResult() throws SQLException {
    Object result = null;

    if (returnsValue()) {
      result = stmt.getObject(1);
    }

    return result;
  }

  /**
   * getString.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final String getString(final int index) throws SQLException {
    return stmt.getString(index);
  }

  /**
   * getBoolean.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final boolean getBoolean(final int index) throws SQLException {
    return stmt.getBoolean(index);
  }

  /**
   * getByte.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final byte getByte(final int index) throws SQLException {
    return stmt.getByte(index);
  }

  /**
   * getShort.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final short getShort(final int index) throws SQLException {
    return stmt.getShort(index);
  }

  /**
   * getInt.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final int getInt(final int index) throws SQLException {
    return stmt.getInt(index);
  }

  /**
   * getIntObj.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Integer getIntObj(final int index) throws SQLException {
    BigDecimal bd = stmt.getBigDecimal(index);

    if (bd == null) {
      return null;
    }

    return new Integer(bd.intValue());
  }

  /**
   * getLong.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final long getLong(final int index) throws SQLException {
    return stmt.getLong(index);
  }

  /**
   * getFloat.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final float getFloat(final int index) throws SQLException {
    return stmt.getFloat(index);
  }

  /**
   * getDouble.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final double getDouble(final int index) throws SQLException {
    return stmt.getDouble(index);
  }

  /**
   * getBigDecimal.
   *
   * @param   index  Input parameter.
   * @param   scale  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final BigDecimal getBigDecimal(final int index, final int scale)
    throws SQLException {
    return stmt.getBigDecimal(index);
  }

  /**
   * getBytes.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final byte[] getBytes(final int index) throws SQLException {
    return stmt.getBytes(index);
  }

  /**
   * getDate.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Date getDate(final int index) throws SQLException {
    return stmt.getDate(index);
  }

  /**
   * getTime.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Time getTime(final int index) throws SQLException {
    return stmt.getTime(index);
  }

  /**
   * getTimestamp.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Timestamp getTimestamp(final int index)
    throws SQLException {
    return stmt.getTimestamp(index);
  }

  /**
   * getObject.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Object getObject(final int index) throws SQLException {
    return stmt.getObject(index);
  }

  /**
   * getBigDecimal.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final BigDecimal getBigDecimal(final int index) throws SQLException {
    return stmt.getBigDecimal(index);
  }

  /**
   * getObject.
   *
   * @param   index  Input parameter.
   * @param   map    Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Object getObject(final int index, final Map<?, ?> map)
    throws SQLException {
    return stmt.getObject(index);
  }

  /**
   * getRef.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Ref getRef(final int index) throws SQLException {
    return stmt.getRef(index);
  }

  /**
   * getBlob.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Blob getBlob(final int index) throws SQLException {
    return stmt.getBlob(index);
  }

  /**
   * getClob.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Clob getClob(final int index) throws SQLException {
    return stmt.getClob(index);
  }

  /**
   * getArray.
   *
   * @param   index  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Array getArray(final int index) throws SQLException {
    return stmt.getArray(index);
  }

  /**
   * getDate.
   *
   * @param   index  Input parameter.
   * @param   cal    Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Date getDate(final int index, final Calendar cal)
    throws SQLException {
    return stmt.getDate(index);
  }

  /**
   * getTime.
   *
   * @param   index  Input parameter.
   * @param   cal    Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Time getTime(final int index, final Calendar cal)
    throws SQLException {
    return stmt.getTime(index);
  }

  /**
   * getTimestamp.
   *
   * @param   index  Input parameter.
   * @param   cal    Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final java.sql.Timestamp getTimestamp(final int index,
    final Calendar cal) throws SQLException {
    return stmt.getTimestamp(index);
  }

  /**
   * getIndicator.
   *
   * @param   i  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  public final Boolean getIndicator(final int i) throws SQLException {
    String s = getString(i);

    if (s == null) {
      return null;
    }

    if ("Y".equals(s) || "T".equals(s)) {
      return Boolean.TRUE;
    }

    if ("N".equals(s) || "F".equals(s)) {
      return Boolean.FALSE;
    }

    return null;
  }

  /**
   * Sets the value for null.
   *
   * @param   index    Input parameter.
   * @param   sqlType  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setNull(final int index, final int sqlType)
    throws SQLException {
    addParamValue(index, "null");
    stmt.setNull(getBindIndex(index), sqlType);
  }

  /**
   * Sets the value for boolean.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBoolean(final int index, final boolean x)
    throws SQLException {
    addParamValue(index, new Boolean(x));
    stmt.setBoolean(getBindIndex(index), x);
  }

  /**
   * Sets the value for boolean.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBoolean(final int index, final String x)
    throws SQLException {
    boolean isOn = false;

    if ((x != null) && x.equals(YES)) {
      isOn = true;
    }

    setBoolean(index, isOn);
  }

  /**
   * Sets the value for byte.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setByte(final int index, final byte x) throws SQLException {
    addParamValue(index, new Byte(x));
    stmt.setByte(getBindIndex(index), x);
  }

  /**
   * Sets the value for byte.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setByte(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setByte(index, Byte.parseByte(x));
    }
  }

  /**
   * Sets the value for short.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setShort(final int index, final short x)
    throws SQLException {
    addParamValue(index, new Short(x));
    stmt.setShort(getBindIndex(index), x);
  }

  /**
   * Sets the value for short.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setShort(final int index, final Short x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, x);
      stmt.setShort(getBindIndex(index), x.shortValue());
    }
  }

  /**
   * Sets the value for short.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setShort(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setShort(index, Short.parseShort(x));
    }
  }

  /**
   * Sets the value for int.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setInt(final int index, final int x) throws SQLException {

    if (x == NullConstants.INT) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, new Integer(x));
      stmt.setInt(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for int.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setInt(final int index, final Integer x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, x);
      stmt.setInt(getBindIndex(index), x.intValue());
    }
  }

  /**
   * Sets the value for optional int.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setOptionalInt(final int index, final int x)
    throws SQLException {

    if (x == NullConstants.INT) {
      setNull(index, Types.NUMERIC);
    } else {
      setInt(index, x);
    }
  }

  /**
   * Sets the value for int.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setInt(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setInt(index, Integer.parseInt(x));
    }
  }


  /**
   * Sets the value for indicator.
   *
   * @param   i     Input parameter.
   * @param   bool  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setIndicator(final int i, final Boolean bool)
    throws SQLException {

    if (bool == null) {
      setString(i, null);
    } else {
      setIndicator(i, bool.booleanValue());
    }
  }

  /**
   * Sets the value for indicator.
   *
   * @param   i     Input parameter.
   * @param   bool  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setIndicator(final int i, final boolean bool)
    throws SQLException {

    if (bool) {
      setString(i, "Y");
    } else {
      setString(i, "N");
    }
  }

  /**
   * Sets the value for long.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setLong(final int index, final long x) throws SQLException {

    if (x == NullConstants.LONG) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, new Long(x));
      stmt.setLong(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for long.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setLong(final int index, final Long x) throws SQLException {

    if (x == null) {
      setNull(index, Types.NUMERIC);
    } else {
      setLong(index, x.longValue());
    }
  }

  /**
   * Sets the value for long.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setLong(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setLong(index, Long.parseLong(x));
    }
  }

  /**
   * Sets the value for float.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setFloat(final int index, final float x)
    throws SQLException {
    addParamValue(index, new Float(x));
    stmt.setFloat(getBindIndex(index), x);
  }

  /**
   * Sets the value for float.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setFloat(final int index, final Float x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.NUMERIC);
    } else {
      setFloat(index, x.floatValue());
    }
  }

  /**
   * Sets the value for optional float.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setOptionalFloat(final int index, final float x)
    throws SQLException {

    if (x == NullConstants.FLOAT) {
      setNull(index, Types.NUMERIC);
    } else {
      setFloat(index, x);
    }
  }

  /**
   * Sets the value for float.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setFloat(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setFloat(index, Float.parseFloat(x));
    }
  }

  /**
   * Sets the value for double.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDouble(final int index, final double x)
    throws SQLException {

    if (x == NullConstants.DOUBLE) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, new Double(x));
      stmt.setDouble(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for double.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDouble(final int index, final Double x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.NUMERIC);
    } else {
      addParamValue(index, x);
      stmt.setDouble(getBindIndex(index), x.doubleValue());
    }
  }

  /**
   * Sets the value for double.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDouble(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.NUMERIC);
    } else {
      setDouble(index, Double.parseDouble(x));
    }
  }

  /**
   * Sets the value for big integer.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBigInteger(final int index, final BigInteger x)
    throws SQLException {

    if (x == null) {

      // cast null so that the correct method is called
      setBigDecimal(index, (BigDecimal) null);
    } else {
      setBigDecimal(index, new BigDecimal(x));
    }
  }

  /**
   * Sets the value for big decimal.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBigDecimal(final int index, final BigDecimal x)
    throws SQLException {
    addParamValue(index, x);
    stmt.setBigDecimal(getBindIndex(index), x);
  }

  /**
   * Sets the value for big decimal.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBigDecimal(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.VARCHAR);
    } else {
      setBigDecimal(index, BigDecimal.valueOf(Long.parseLong(x)));
    }
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final String x)
    throws SQLException {

    if ((x == null) || (x.trim().length() == 0)) {
      setNull(index, Types.VARCHAR);
    } else {
      addParamValue(index, x);
      stmt.setString(getBindIndex(index), x);
    }
  }

  /**
   * allow an empty or null string to be passed as VARCHAR this usually for
   * output parms when input is null.
   *
   * @param      index  The new stringEmpty value
   * @param      x      The new stringEmpty value
   *
   * @exception  SQLException  Description of the Exception
   */
  public final void setStringEmpty(final int index, final String x)
    throws SQLException {
    addParamValue(index, x);
    stmt.setString(getBindIndex(index), x);
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final boolean x)
    throws SQLException {

    if (x) {
      setString(index, YES);
    } else {
      setString(index, NO);
    }
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final byte x)
    throws SQLException {
    setString(index, Byte.toString(x));
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final short x)
    throws SQLException {
    setString(index, Short.toString(x));
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final int x)
    throws SQLException {
    setString(index, Integer.toString(x));
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final long x)
    throws SQLException {
    setString(index, Long.toString(x));
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final float x)
    throws SQLException {
    setString(index, Float.toString(x));
  }

  /**
   * Sets the value for string.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setString(final int index, final double x)
    throws SQLException {
    setString(index, Double.toString(x));
  }

  /**
   * Sets the value for bytes.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBytes(final int index, final byte[] x)
    throws SQLException {
    addParamValue(index, "-byte array-");
    stmt.setBytes(getBindIndex(index), x);
  }

  /**
   * Sets the value for date.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDate(final int index, final java.sql.Date x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.DATE);
    } else {
      addParamValue(index, x);
      stmt.setDate(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for date.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDate(final int index, final java.util.Date x)
    throws SQLException {
    java.sql.Date sqlDate = null;

    if (x != null) {
      sqlDate = new java.sql.Date(x.getTime());
    }

    setDate(index, sqlDate);
  }

  /**
   * Sets the value for time.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTime(final int index, final java.sql.Time x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.TIME);
    } else {
      addParamValue(index, x);
      stmt.setTime(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for time.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTime(final int index, final java.util.Date x)
    throws SQLException {
    java.sql.Time sqlTime = null;

    if (x != null) {
      sqlTime = new java.sql.Time(x.getTime());
    }

    setTime(index, sqlTime);
  }

  /**
   * Sets the value for timestamp.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTimestamp(final int index, final java.sql.Timestamp x)
    throws SQLException {

    if (x == null) {
      setNull(index, Types.TIMESTAMP);
    } else {
      addParamValue(index, x);
      stmt.setTimestamp(getBindIndex(index), x);
    }
  }

  /**
   * Sets the value for timestamp.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTimestamp(final int index, final java.util.Date x)
    throws SQLException {
    java.sql.Timestamp sqlTS = null;

    if (x != null) {
      sqlTS = new java.sql.Timestamp(x.getTime());
    }

    setTimestamp(index, sqlTS);
  }

  /**
   * Sets the value for ascii stream.
   *
   * @param   index   Input parameter.
   * @param   x       Input parameter.
   * @param   length  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setAsciiStream(final int index, final java.io.InputStream x,
    final int length) throws SQLException {
    addParamValue(index, "-input stream-");
    stmt.setAsciiStream(getBindIndex(index), x, length);
  }

  /**
   * Sets the value for binary stream.
   *
   * @param   index   Input parameter.
   * @param   x       Input parameter.
   * @param   length  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBinaryStream(final int index,
    final java.io.InputStream x, final int length) throws SQLException {
    addParamValue(index, "-input stream-");
    stmt.setBinaryStream(getBindIndex(index), x, length);
  }

  /**
   * Sets the value for object.
   *
   * @param   index          Input parameter.
   * @param   x              Input parameter.
   * @param   targetSqlType  Input parameter.
   * @param   scale          Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setObject(final int index, final Object x,
    final int targetSqlType, final int scale) throws SQLException {
    addParamValue(index, x);
    stmt.setObject(getBindIndex(index), x, targetSqlType, scale);
  }

  /**
   * Sets the value for object.
   *
   * @param   index          Input parameter.
   * @param   x              Input parameter.
   * @param   targetSqlType  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setObject(final int index, final Object x,
    final int targetSqlType) throws SQLException {
    addParamValue(index, x);
    stmt.setObject(index, x, targetSqlType);
  }

  /**
   * Sets the value for object.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setObject(final int index, final Object x)
    throws SQLException {
    addParamValue(index, x);
    stmt.setObject(getBindIndex(index), x);
  }

  /**
   * Sets the value for character stream.
   *
   * @param   index   Input parameter.
   * @param   reader  Input parameter.
   * @param   length  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setCharacterStream(final int index,
    final java.io.Reader reader, final int length) throws SQLException {
    addParamValue(index, "-reader-");
    stmt.setCharacterStream(getBindIndex(index), reader, length);
  }

  /**
   * Sets the value for ref.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setRef(final int index, final Ref x) throws SQLException {
    addParamValue(index, x);
    stmt.setRef(getBindIndex(index), x);
  }

  /**
   * Sets the value for blob.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setBlob(final int index, final Blob x) throws SQLException {
    addParamValue(index, x);
    stmt.setBlob(getBindIndex(index), x);
  }

  /**
   * Sets the value for clob.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setClob(final int index, final Clob x) throws SQLException {
    addParamValue(index, x);
    stmt.setClob(getBindIndex(index), x);
  }

  /**
   * Sets the value for array.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setArray(final int index, final Array x)
    throws SQLException {
    addParamValue(index, x);
    stmt.setArray(getBindIndex(index), x);
  }

  /**
   * Sets the value for date.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   * @param   cal    Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setDate(final int index, final java.sql.Date x,
    final Calendar cal) throws SQLException {
    addParamValue(index, x);
    stmt.setDate(getBindIndex(index), x, cal);
  }

  /**
   * Sets the value for time.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   * @param   cal    Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTime(final int index, final java.sql.Time x,
    final Calendar cal) throws SQLException {
    addParamValue(index, x);
    stmt.setTime(getBindIndex(index), x, cal);
  }

  /**
   * Sets the value for timestamp.
   *
   * @param   index  Input parameter.
   * @param   x      Input parameter.
   * @param   cal    Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setTimestamp(final int index, final java.sql.Timestamp x,
    final Calendar cal) throws SQLException {
    addParamValue(index, x);
    stmt.setTimestamp(getBindIndex(index), x, cal);
  }

  /**
   * Sets the value for null.
   *
   * @param   index     Input parameter.
   * @param   sqlType   Input parameter.
   * @param   typeName  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  public final void setNull(final int index, final int sqlType,
    final String typeName) throws SQLException {
    addParamValue(index, null);
    stmt.setNull(getBindIndex(index), sqlType, typeName);
  }

  /**
   * getPrepareCallSql.
   *
   * @return  The return value.
   */
  protected final String getPrepareCallSql() {
    StringBuffer result = new StringBuffer();

    result.append("{");

    if (returnsValue()) {
      result.append("? = ");
    }

    result.append("call " + getProcName() + "(");
    result.append(getParamListString());
    result.append(") }");

    return result.toString();
  }

  /**
   * getReturnType.
   *
   * @return  The return value.
   */
  protected final int getReturnType() {
    return rtrnType;
  }

  /**
   * returnsValue.
   *
   * @return  The return value.
   */
  public final boolean returnsValue() {
    return (rtrnType != NO_RETURN_TYPE);
  }

  /**
   * init.
   *
   * @param   connection   The parameter value.
   * @param   aProcName    The parameter value.
   * @param   paramCount   The parameter value.
   * @param   aReturnType  The parameter value.
   *
   * @throws  SQLException  On exception.
   */
  protected final void init(final Connection connection, final String aProcName,
    final int paramCount, final int aReturnType) throws SQLException {
    paramValues = new String[paramCount];
    prcName = aProcName;
    rtrnType = aReturnType;
    stmt = connection.prepareCall(getPrepareCallSql());

    if (returnsValue()) {
      stmt.registerOutParameter(1, rtrnType);
    }
  }

  /**
   * addParamValue.
   *
   * @param  index  The parameter value.
   * @param  value  The parameter value.
   */
  protected final void addParamValue(final int index, final Object value) {

    if (value == null) {
      paramValues[index - 1] = "null";
    } else {
      paramValues[index - 1] = value.toString();
    }
  }

  /**
   * getParamListString.
   *
   * @return  The return value.
   */
  protected final String getParamListString() {
    StringBuffer result = new StringBuffer();

    for (int i = 0; i < paramValues.length; i++) {
      result.append("?");

      if (i < (paramValues.length - 1)) {
        result.append(",");
      }
    }

    return result.toString();
  }

  /**
   * getBindIndex.
   *
   * @param   parameterIndex  Input parameter.
   *
   * @return  The return value.
   */
  protected final int getBindIndex(final int parameterIndex) {
    
    int result; 

    if (returnsValue()) {
      result = (parameterIndex + 1);
    } else {
      result = parameterIndex;
    }
    
    return result;
  }

  /**
   * Class which defines constants that are supposed to represent null Java
   * primitives. Note that you can't use NaN because you can't compare two
   * floats with that value.
   */
  public static final class NullConstants {

    /** Creates a new NullConstants object. */
    private NullConstants() {
    }

    /** FLOAT. */
    public static final float FLOAT = Float.MIN_VALUE;

    /** INT. */
    public static final int INT = Integer.MIN_VALUE;

    /** DOUBLE. */
    public static final double DOUBLE = Double.MIN_VALUE;

    /** LONG. */
    public static final long LONG = Long.MIN_VALUE;
  }
}
