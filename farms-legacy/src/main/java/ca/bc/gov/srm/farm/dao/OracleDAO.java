/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.exception.CheckConstraintException;
import ca.bc.gov.srm.farm.exception.ChildRecordsExistException;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.DataNotCurrentException;
import ca.bc.gov.srm.farm.exception.DuplicateDataException;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.LineItemNotFoundException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.webade.dbpool.WrapperConnection;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;


/**
 * OracleDAO.
 *
 * @author   awilkinson
 */
abstract class OracleDAO {

  //
  // This value should be declared as a constant in the ORACLE application
  // environment ORACLE user defined error codes need to be negative values
  // after -20000, For example, if we declare the error code as -20001 in
  // ORACLE, we need to declare it as 20001 (positive) in Java.
  /** DATA_NOT_CURRENT. */
  protected static final int DATA_NOT_CURRENT = 20001;

  /** DUPLICATE_DATA. */
  protected static final int DUPLICATE_DATA = 1; // 00001

  /** CHILD_RECORD_EXISTS. */
  protected static final int CHILD_RECORD_EXISTS = 2292; // 02292

  /** CHECK_CONSTRAINT. */
  protected static final int CHECK_CONSTRAINT = 2290; // 02290

  protected static final int INVALID_REVISION_COUNT = 20010;

  protected static final int LINE_ITEM_NOT_FOUND = 20011;

  protected static final String NUM_COLLECTION_TYPE_NAME = "FARM_ID_TBL";
  protected static final String CODE_COLLECTION_TYPE_NAME = "FARM_CD_TBL";
  protected static final String GUID_COLLECTION_TYPE_NAME = "FARM_GUID_TBL";

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * @return  Logger
   */
  protected Logger getLog() {
    return log;
  }

  /**
   * close.
   *
   * @param  cstmt  The parameter value.
   */
  @Deprecated
  protected void close(final CallableStatement cstmt) {

    if (cstmt != null) {

      try {
        cstmt.close();
      } catch (SQLException e) {
        log.error(e.getMessage(), e);
      }
    }
  }


  /**
   * close.
   *
   * @param  resultSet  The parameter value.
   * @param  cstmt      The parameter value.
   */
  @Deprecated
  protected void close(final ResultSet resultSet,
    final CallableStatement cstmt) {

    if (resultSet != null) {

      try {
        resultSet.close();
      } catch (SQLException e) {
        log.error(e.getMessage(), e);
      }
    }

    close(cstmt);
  }


  /**
   * close.
   *
   * @param  resultSet  The parameter value.
   * @param  proc       The parameter value.
   */
  @Deprecated
  protected void close(final ResultSet resultSet,
    final DAOStoredProcedure proc) {

    if (resultSet != null) {

      try {
        resultSet.close();
      } catch (SQLException e) {
        log.error(e.getMessage(), e);
      }
    }

    if (proc != null) {

      try {
        proc.close();
      } catch (SQLException e) {
        log.error(e.getMessage(), e);
      }
    }
  }


  /**
   * close.
   *
   * @param  proc  The parameter value.
   */
  @Deprecated
  protected void close(final DAOStoredProcedure proc) {

    if (proc != null) {

      try {
        proc.close();
      } catch (SQLException e) {
        log.error(e.getMessage(), e);
      }
    }
  }


  /**
   * getArray.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Array getArray(final CallableStatement statement, final int index)
    throws SQLException {
    return statement.getArray(index);
  }


  /**
   * getBigDecimal.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected BigDecimal getBigDecimal(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getBigDecimal(index);
  }

  /**
   * getBlob.
   *
   * @param   resultSet  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Blob getBlob(final ResultSet resultSet) throws SQLException {
    Blob blob = resultSet.getBlob(1);

    return blob;
  }


  /**
   * getBooleanValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected boolean getBooleanValue(final CallableStatement statement,
    final int index) throws SQLException {
    String s = getString(statement, index);

    return booleanValue(s);
  }


  /**
   * getByte.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Byte getByte(final CallableStatement statement, final int index)
    throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Byte(bd.byteValue());
    }

    return null;
  }


  /**
   * getBytes.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected byte[] getBytes(final CallableStatement statement, final int index)
    throws SQLException {
    return statement.getBytes(index);
  }


  /**
   * getByteValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected byte getByteValue(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getByte(index);
  }


  /**
   * getConnection.
   *
   * @param   transaction  Input parameter.
   *
   * @return  The return value.
   */
  protected Connection getConnection(final Transaction transaction) {

    if (transaction == null) {
      throw new IllegalArgumentException("transaction cannot be null.");
    }

    if (!(transaction.getDatastore() instanceof Connection)) {
      throw new IllegalArgumentException(
        "Transaction datastore is not the expected to be type "
        + Connection.class.getName() + " not "
        + transaction.getDatastore().getClass().getName());
    }

    return (Connection) transaction.getDatastore();
  }

  /**
   * getDate.
   *
   * @param   timestamp  Input parameter.
   *
   * @return  The return value.
   */
  protected Date getDate(final Timestamp timestamp) {
    java.util.Date result = null;

    if (timestamp != null) {
      result = new java.util.Date(timestamp.getTime());
    }

    return result;
  }

  /**
   * getDate.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Date getDate(final ResultSet resultSet, final String columnName)
    throws SQLException {
    java.sql.Timestamp timestamp = resultSet.getTimestamp(columnName);

    return getDate(timestamp);
  }
  
  /**
   * getDate.
   *
   * @param   resultSet   Input parameter.
   * @param   index       Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Date getDate(final ResultSet resultSet, final int index)
      throws SQLException {
    java.sql.Timestamp timestamp = resultSet.getTimestamp(index);
    
    return getDate(timestamp);
  }


  /**
   * getDate.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.util.Date getDate(final CallableStatement statement,
    final int index) throws SQLException {
    java.sql.Date sd = getSqlDate(statement, index);

    if (sd != null) {
      return getTimestamp(new java.util.Date(sd.getTime()), 0, 0, 0, 0);
    }

    return null;
  }

  /**
   * getDouble.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Double getDouble(final ResultSet resultSet, final String columnName)
    throws SQLException {
    String value = resultSet.getString(columnName);

    if (!StringUtils.isBlank(value)) {
      return new Double(value);
    }

    return null;
  }
  
  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return Double
   * 
   * @throws SQLException
   *           SQLException
   */
  protected Double getDouble(final ResultSet rs, final int i) throws SQLException {
    double v = rs.getDouble(i);

    if (rs.wasNull()) {
      return null;
    }

    return new Double(v);
  }


  /**
   * getDouble.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Double getDouble(final CallableStatement statement, final int index)
    throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Double(bd.doubleValue());
    }

    return null;
  }


  /**
   * getDoubleValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected double getDoubleValue(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getDouble(index);
  }

  /**
   * getFloat.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Float getFloat(final CallableStatement statement, final int index)
    throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Float(bd.floatValue());
    }

    return null;
  }


  /**
   * getFloatValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected float getFloatValue(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getFloat(index);
  }


  /**
   * getIndicator.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected boolean getIndicator(final ResultSet resultSet,
    final String columnName) throws SQLException {
    String value = resultSet.getString(columnName);
    boolean result = false;

    try {
      result = DataParseUtils.parseBoolean(value);
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
    }

    return result;
  }
  
  
  /**
   * @param rs
   *          ResultSet
   * @param i
   *          int
   * 
   * @return Boolean
   * 
   * @throws SQLException
   *           SQLException
   */
  protected Boolean getIndicator(final ResultSet rs, final int i)
      throws SQLException {
    String value = rs.getString(i);
    Boolean result = Boolean.FALSE;

    try {
      result = Boolean.valueOf(DataParseUtils.parseBoolean(value));
    } catch (ParseException e) {
      throw new SQLException(e.toString());
    }

    return result;
  }


  /**
   * getIndicator.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Boolean getIndicator(final CallableStatement statement,
    final int index) throws SQLException {
    String s = getString(statement, index);

    return new Boolean(booleanValue(s));
  }

  /**
   * @param booleanIndicator booleanIndicator
   * @return If true, return "Y". If false return "N".
   */
  protected String getIndicatorYN(Boolean booleanIndicator) {
    String result;
    if(booleanIndicator.booleanValue()) {
      result = "Y";
    } else {
      result = "N";
    }
    
    return result;
  }


  /**
   * getInteger.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Integer getInteger(final CallableStatement statement,
    final int index) throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Integer(bd.intValue());
    }

    return null;
  }

  /**
   * @param   rs  ResultSet
   * @param   columnIndex   int
   *
   * @return  Integer
   *
   * @throws  SQLException  SQLException
   */
  protected Integer getInteger(final ResultSet rs, final int columnIndex)
    throws SQLException {
    int v = rs.getInt(columnIndex);

    if (rs.wasNull()) {
      return null;
    }

    return new Integer(v);
  }
  
  /**
   * @param   rs  ResultSet
   * @param   columnName   int
   *
   * @return  Integer
   *
   * @throws  SQLException  SQLException
   */
  protected Integer getInteger(final ResultSet rs, final String columnName)
  throws SQLException {
    int v = rs.getInt(columnName);
    
    if (rs.wasNull()) {
      return null;
    }
    
    return new Integer(v);
  }


  /**
   * getIntValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected int getIntValue(final CallableStatement statement, final int index)
    throws SQLException {
    return statement.getInt(index);
  }

  /**
   * getLong.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Long getLong(final ResultSet resultSet, final String columnName)
    throws SQLException {
    String value = resultSet.getString(columnName);

    if (!StringUtils.isBlank(value)) {
      return new Long(value);
    }

    return null;
  }

  /**
   * getBigDecimal.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected BigDecimal getBigDecimal(final ResultSet resultSet, final String columnName)
    throws SQLException {
    return resultSet.getBigDecimal(columnName);
  }

  /**
   * getLong.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Long getLong(final CallableStatement statement, final int index)
    throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Long(bd.longValue());
    }

    return null;
  }


  /**
   * getLongValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected long getLongValue(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getLong(index);
  }


  /**
   * getObject.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Object getObject(final CallableStatement statement, final int index)
    throws SQLException {
    return statement.getObject(index);
  }

  /**
   * getOracleConnection.
   *
   * @param   transaction  Input parameter.
   *
   * @return  The return value.
   */
  protected OracleConnection getOracleConnection(
    final Transaction transaction) {
    @SuppressWarnings("resource")
    Connection c = wrappedConnection(getConnection(transaction));

    if (c instanceof OracleConnection) {
      return (OracleConnection) c;
    }

    return null;
  }


  protected OracleConnection getOracleConnection(final Connection connection) {

    Connection c = wrappedConnection(connection);

    if (c instanceof OracleConnection) {
      return (OracleConnection) c;
    }

    return null;
  }


  /**
   * getShort.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected Short getShort(final CallableStatement statement, final int index)
    throws SQLException {
    BigDecimal bd = getBigDecimal(statement, index);

    if (bd != null) {
      return new Short(bd.shortValue());
    }

    return null;
  }


  /**
   * getShortValue.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected short getShortValue(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getShort(index);
  }


  /**
   * getSqlDate.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.sql.Date getSqlDate(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getDate(index);
  }


  /**
   * getSqlTime.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.sql.Time getSqlTime(final CallableStatement statement,
    final int index) throws SQLException {
    return statement.getTime(index);
  }


  /**
   * getSqlTimestamp.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.sql.Timestamp getSqlTimestamp(
    final CallableStatement statement, final int index) throws SQLException {
    return statement.getTimestamp(index);
  }

  /**
   * getString.
   *
   * @param   resultSet   Input parameter.
   * @param   columnName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected String getString(final ResultSet resultSet, final String columnName)
    throws SQLException {
    String value = resultSet.getString(columnName);

    return value;
  }
  
  /**
   * getString.
   *
   * @param   resultSet   Input parameter.
   * @param   columnIndex Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected String getString(final ResultSet resultSet, final int columnIndex)
  throws SQLException {
    String value = resultSet.getString(columnIndex);
    
    return value;
  }


  /**
   * getString.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected String getString(final CallableStatement statement, final int index)
    throws SQLException {
    return statement.getString(index);
  }


  /**
   * getTime.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.util.Date getTime(final CallableStatement statement,
    final int index) throws SQLException {
    java.sql.Date sd = getSqlDate(statement, index);

    if (sd != null) {
      return new java.util.Date(sd.getTime());
    }

    return null;
  }


  /**
   * getTimestamp.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected java.util.Date getTimestamp(final CallableStatement statement,
    final int index) throws SQLException {
    java.sql.Timestamp st = getSqlTimestamp(statement, index);

    if (st != null) {
      return new java.util.Date(st.getTime());
    }

    return null;
  }


  /**
   * handleException.
   *
   * @param   e  The parameter value.
   *
   * @throws  DataAccessException  On exception.
   */
  protected void handleException(final Exception e) throws DataAccessException {

    if ((e instanceof SQLException)) {
      SQLException sqlEx = (SQLException) e;
      String msg = MessageFormat.format("{2}. [{0} - {1}]",
          new Object[] {
            new Integer(sqlEx.getErrorCode()), sqlEx.getSQLState(),
            sqlEx.getMessage()
          });

      // examine sql code, make a specific DataAccessException...
      if (sqlEx.getErrorCode() == DATA_NOT_CURRENT) {
        DataNotCurrentException ex = new DataNotCurrentException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else if (sqlEx.getErrorCode() == DUPLICATE_DATA) {
        DuplicateDataException ex = new DuplicateDataException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else if (sqlEx.getErrorCode() == CHILD_RECORD_EXISTS) {
        ChildRecordsExistException ex = new ChildRecordsExistException(msg,
            sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else if (sqlEx.getErrorCode() == CHECK_CONSTRAINT) {
        CheckConstraintException ex = new CheckConstraintException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else if (sqlEx.getErrorCode() == INVALID_REVISION_COUNT) {
        InvalidRevisionCountException ex = new InvalidRevisionCountException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else if (sqlEx.getErrorCode() == LINE_ITEM_NOT_FOUND) {
        LineItemNotFoundException ex = new LineItemNotFoundException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      } else {
        DataAccessException ex = new DataAccessException(msg, sqlEx);
        ex.setErrorCode(new Long(sqlEx.getErrorCode()));
        throw ex;
      }
    } else {
      throw new DataAccessException(e);
    }
  }

  /**
   * isTrue.
   *
   * @param   value  Input parameter.
   *
   * @return  The return value.
   */
  protected boolean isTrue(final String value) {
    boolean result = false;

    try {
      result = DataParseUtils.parseBoolean(value);
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
    }

    return result;
  }


  /**
   * prepareFunction.
   *
   * @param   connection      The parameter value.
   * @param   procedureName   The parameter value.
   * @param   parameterCount  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected CallableStatement prepareFunction(final Connection connection,
    final String procedureName, final int parameterCount) throws SQLException {
    CallableStatement result = null;
    String sql = createPrepareCallSql(procedureName, parameterCount, true);
    result = connection.prepareCall(sql);
    result.registerOutParameter(1, OracleTypes.CURSOR);

    return result;
  }


  /**
   * prepareFunction.
   *
   * @param   connection      The parameter value.
   * @param   procedureName   The parameter value.
   * @param   parameterCount  The parameter value.
   * @param   returnType      The parameter value.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected CallableStatement prepareFunction(final Connection connection,
    final String procedureName, final int parameterCount, final int returnType)
    throws SQLException {
    CallableStatement result = null;
    String sql = createPrepareCallSql(procedureName, parameterCount, true);
    result = connection.prepareCall(sql);
    result.registerOutParameter(1, returnType);

    return result;
  }


  /**
   * prepareProcedure.
   *
   * @param   connection      The parameter value.
   * @param   procedureName   The parameter value.
   * @param   parameterCount  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   */
  protected CallableStatement prepareProcedure(final Connection connection,
    final String procedureName, final int parameterCount) throws SQLException {
    CallableStatement result = null;
    String sql = createPrepareCallSql(procedureName, parameterCount, false);
    result = connection.prepareCall(sql);

    return result;
  }

  /**
   * readBlob.
   *
   * @param   blob          The parameter value.
   * @param   outputStream  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  SQLException  On exception.
   * @throws  IOException   On exception.
   */
  protected Long readBlob(final Blob blob, final OutputStream outputStream)
    throws SQLException, IOException {
    Long result = new Long(0);
    try(InputStream inputStream = blob.getBinaryStream();) {
      result = new Long(blob.length());
      copyStream(inputStream, outputStream);
    }

    return result;
  }


  /**
   * Sets the value for array.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setArray(final CallableStatement statement, final int index,
    final Array value) throws SQLException {
    statement.setArray(index, value);
  }


  /**
   * Sets the value for big decimal.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setBigDecimal(final CallableStatement statement,
    final int index, final BigDecimal value) throws SQLException {
    statement.setBigDecimal(index, value);
  }


  /**
   * Sets the value for big decimal.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setBigDecimal(final CallableStatement statement,
    final int index, final String value) throws SQLException {
    setBigDecimal(statement, index, BigDecimal.valueOf(Long.parseLong(value)));
  }


  /**
   * Sets the value for boolean value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setBooleanValue(final CallableStatement statement,
    final int index, final boolean value) throws SQLException {
    statement.setBoolean(index, value);
  }


  /**
   * Sets the value for byte.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setByte(final CallableStatement statement, final int index,
    final Byte value) throws SQLException {

    if (value != null) {
      statement.setByte(index, value.byteValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for byte.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setByte(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setByte(statement, index, Byte.valueOf(value));
  }


  /**
   * Sets the value for bytes.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setBytes(final CallableStatement statement, final int index,
    final byte[] value) throws SQLException {
    statement.setBytes(index, value);
  }


  /**
   * Sets the value for byte value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setByteValue(final CallableStatement statement,
    final int index, final byte value) throws SQLException {
    statement.setByte(index, value);
  }


  /**
   * Sets the value for date.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setDate(final CallableStatement statement, final int index,
    final java.util.Date value) throws SQLException {
    java.sql.Date sqlDate = null;

    if (value != null) {
      sqlDate = new java.sql.Date(value.getTime());
    }

    setSqlDate(statement, index, sqlDate);
  }


  /**
   * Sets the value for double.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setDouble(final CallableStatement statement, final int index,
    final Double value) throws SQLException {

    if (value != null) {
      statement.setDouble(index, value.doubleValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for double.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setDouble(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setDouble(statement, index, Double.valueOf(value));
  }


  /**
   * Sets the value for double value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setDoubleValue(final CallableStatement statement,
    final int index, final double value) throws SQLException {
    statement.setDouble(index, value);
  }


  /**
   * Sets the value for float.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setFloat(final CallableStatement statement, final int index,
    final Float value) throws SQLException {

    if (value != null) {
      statement.setFloat(index, value.floatValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for float.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setFloat(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setFloat(statement, index, Float.valueOf(value));
  }


  /**
   * Sets the value for float value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setFloatValue(final CallableStatement statement,
    final int index, final float value) throws SQLException {
    statement.setFloat(index, value);
  }


  /**
   * Sets the value for indicator.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setIndicator(final CallableStatement statement,
    final int index, final Boolean value) throws SQLException {

    if (value != null) {
      statement.setString(index, getIndicatorValue(value));
    } else {
      statement.setString(index, getIndicatorValue(new Boolean(false)));
    }
  }


  /**
   * Sets the value for indicator.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setIndicator(final CallableStatement statement,
    final int index, final boolean value) throws SQLException {
    setIndicator(statement, index, new Boolean(value));
  }


  /**
   * Sets the value for indicator.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setIndicator(final CallableStatement statement,
    final int index, final String value) throws SQLException {
    Boolean b = new Boolean(booleanValue(value));
    setIndicator(statement, index, b);
  }


  /**
   * Sets the value for integer.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setInteger(final CallableStatement statement, final int index,
    final Integer value) throws SQLException {

    if (value != null) {
      statement.setInt(index, value.intValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for integer.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setInteger(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setInteger(statement, index, Integer.valueOf(value));
  }


  /**
   * Sets the value for int value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setIntValue(final CallableStatement statement, final int index,
    final int value) throws SQLException {
    statement.setInt(index, value);
  }


  /**
   * Sets the value for long.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setLong(final CallableStatement statement, final int index,
    final Long value) throws SQLException {

    if (value != null) {
      statement.setLong(index, value.longValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for long.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setLong(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setLong(statement, index, Long.valueOf(value));
  }


  /**
   * Sets the value for long value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setLongValue(final CallableStatement statement,
    final int index, final long value) throws SQLException {
    statement.setLong(index, value);
  }


  /**
   * Sets the value for null.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   sqlType    Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setNull(final CallableStatement statement, final int index,
    final int sqlType) throws SQLException {
    statement.setNull(index, sqlType);
  }


  /**
   * Sets the value for null.
   *
   * @param   statement    Input parameter.
   * @param   index        Input parameter.
   * @param   sqlType      Input parameter.
   * @param   sqlTypeName  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setNull(final CallableStatement statement, final int index,
    final int sqlType, final String sqlTypeName) throws SQLException {
    statement.setNull(index, sqlType, sqlTypeName);
  }


  /**
   * Sets the value for object.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setObject(final CallableStatement statement, final int index,
    final Object value) throws SQLException {
    statement.setObject(index, value);
  }


  /**
   * Sets the value for object.
   *
   * @param   statement      Input parameter.
   * @param   index          Input parameter.
   * @param   value          Input parameter.
   * @param   targetSqlType  Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setObject(final CallableStatement statement, final int index,
    final Object value, final int targetSqlType) throws SQLException {
    statement.setObject(index, value, targetSqlType);
  }


  /**
   * Sets the value for short.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setShort(final CallableStatement statement, final int index,
    final Short value) throws SQLException {

    if (value != null) {
      statement.setShort(index, value.shortValue());
    } else {
      setNull(statement, index, Types.NUMERIC);
    }
  }


  /**
   * Sets the value for short.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setShort(final CallableStatement statement, final int index,
    final String value) throws SQLException {
    setShort(statement, index, Short.valueOf(value));
  }


  /**
   * Sets the value for short value.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setShortValue(final CallableStatement statement,
    final int index, final short value) throws SQLException {
    statement.setShort(index, value);
  }


  /**
   * Sets the value for sql date.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setSqlDate(final CallableStatement statement, final int index,
    final java.sql.Date value) throws SQLException {

    if (value != null) {
      statement.setDate(index, value);
    } else {
      setNull(statement, index, Types.DATE);
    }
  }


  /**
   * Sets the value for sql time.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setSqlTime(final CallableStatement statement, final int index,
    final java.sql.Time value) throws SQLException {

    if (value != null) {
      statement.setTime(index, value);
    } else {
      setNull(statement, index, Types.TIME);
    }
  }

  /**
   * Sets the value for sql timestamp.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setSqlTimestamp(final CallableStatement statement,
    final int index, final java.sql.Timestamp value) throws SQLException {

    if (value != null) {
      statement.setTimestamp(index, value);
    } else {
      setNull(statement, index, Types.TIMESTAMP);
    }
  }


  /**
   * Sets the value for string.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setString(final CallableStatement statement, final int index,
    final String value) throws SQLException {

    if (!StringUtils.isBlank(value)) {
      statement.setString(index, value);
    } else {
      setNull(statement, index, Types.VARCHAR);
    }
  }


  /**
   * Sets the value for time.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setTime(final CallableStatement statement, final int index,
    final java.util.Date value) throws SQLException {
    java.sql.Time sqlTime = null;

    if (value != null) {
      sqlTime = new java.sql.Time(value.getTime());
    }

    setSqlTime(statement, index, sqlTime);
  }


  /**
   * Sets the value for timestamp.
   *
   * @param   statement  Input parameter.
   * @param   index      Input parameter.
   * @param   value      Input parameter.
   *
   * @throws  SQLException  On exception.
   */
  protected void setTimestamp(final CallableStatement statement,
    final int index, final java.util.Date value) throws SQLException {
    java.sql.Timestamp sqlTS = null;

    if (value != null) {
      sqlTS = new java.sql.Timestamp(value.getTime());
    }

    setSqlTimestamp(statement, index, sqlTS);
  }


  /**
   * wrappedConnection.
   *
   * @param   connection  The parameter value.
   *
   * @return  The return value.
   */
  protected Connection wrappedConnection(final Connection connection) {

    Connection inConnection = connection;

    if (inConnection instanceof WrapperConnection) {
      WrapperConnection wconn = (WrapperConnection) inConnection;
      inConnection = wconn.getWrappedConnection();
    }

    return inConnection;
  }

  /**
   * writeBlob.
   *
   * @param   blob         The parameter value.
   * @param   inputStream  The parameter value.
   *
   * @throws  SQLException  On exception.
   * @throws  IOException   On exception.
   */
  protected void writeBlob(final Blob blob, final InputStream inputStream)
    throws SQLException, IOException {
    try(OutputStream outputStream = blob.setBinaryStream(0L);) {
      copyStream(inputStream, outputStream);
    }
  }

  /**
   * booleanValue.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   */
  private boolean booleanValue(final String value) {
    boolean result = false;

    try {
      result = DataParseUtils.parseBoolean(value);
    } catch (ParseException e) {
      result = false;
    }

    return result;
  }

  /**
   * copyStream.
   *
   * @param   in   The parameter value.
   * @param   out  The parameter value.
   *
   * @throws  IOException  On exception.
   */
  private void copyStream(final InputStream in, final OutputStream out)
    throws IOException {
    final int buffersize = 256;
    byte[] buffer = new byte[buffersize];
    int bytesRead = 0;

    while ((bytesRead = in.read(buffer)) != -1) {
      out.write(buffer, 0, bytesRead);
    }
  }


  /**
   * createPrepareCallSql.
   *
   * @param   procedureName   The parameter value.
   * @param   parameterCount  The parameter value.
   * @param   returnsValue    The parameter value.
   *
   * @return  The return value.
   */
  private String createPrepareCallSql(final String procedureName,
    final int parameterCount, final boolean returnsValue) {
    StringBuffer result = new StringBuffer();
    result.append("{");

    if (returnsValue) {
      result.append("? = ");
    }

    result.append("call ");
    result.append(procedureName);

    if (parameterCount > 0) {
      String args = StringUtils.repeat("?,", parameterCount);
      result.append("(");
      result.append(StringUtils.left(args, args.lastIndexOf(",")));
      result.append(") ");
    }

    result.append("}");
    log.debug(result.toString());

    return result.toString();
  }


  /**
   * getIndicatorValue.
   *
   * @param   value  Input parameter.
   *
   * @return  The return value.
   */
  private String getIndicatorValue(final Boolean value) {
    String result = "N";

    if ((value != null) && value.booleanValue()) {
      result = "Y";
    }

    return result;
  }

  /**
   * getTimestamp.
   *
   * @param   d     Input parameter.
   * @param   hour  Input parameter.
   * @param   min   Input parameter.
   * @param   sec   Input parameter.
   * @param   ms    Input parameter.
   *
   * @return  The return value.
   */
  private Date getTimestamp(final Date d, final int hour, final int min,
    final int sec, final int ms) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);

    return getTimestamp(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH), hour, min, sec, ms);
  }

  /**
   * getTimestamp.
   *
   * @param   year        Input parameter.
   * @param   month       Input parameter.
   * @param   dayOfMonth  Input parameter.
   * @param   hour        Input parameter.
   * @param   min         Input parameter.
   * @param   sec         Input parameter.
   * @param   ms          Input parameter.
   *
   * @return  The return value.
   */
  private Date getTimestamp(final int year, final int month,
    final int dayOfMonth, final int hour, final int min, final int sec,
    final int ms) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.MILLISECOND, ms);
    cal.set(Calendar.SECOND, sec);
    cal.set(Calendar.MINUTE, min);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.YEAR, year);

    return cal.getTime();
  }
  
  /**
   * @param e e
   */
  protected void logSqlException(SQLException e) {
    if (e.getErrorCode() == DATA_NOT_CURRENT
        || e.getErrorCode() == INVALID_REVISION_COUNT
        || e.getErrorCode() == LINE_ITEM_NOT_FOUND) {
      getLog().warn("Unexpected error: ", e);
    } else {
      getLog().error("Unexpected error: ", e);
    }
  }

  private Object[] toArray(List<?> values) {
    Object[] array = {};
    if(values != null) {
      array = values.toArray();
    }
    return array;
  }

  @SuppressWarnings("resource")
  protected Array createStringOracleArray(Transaction transaction, List<String> values) throws SQLException {
    return getOracleConnection(transaction).createOracleArray(CODE_COLLECTION_TYPE_NAME, toArray(values));
  }
  
  @SuppressWarnings("resource")
  protected Array createNumbersOracleArray(Transaction transaction, List<Integer> values) throws SQLException {
    return getOracleConnection(transaction).createOracleArray(NUM_COLLECTION_TYPE_NAME, toArray(values));
  }

  protected Array createStringOracleArray(Connection connection, List<String> values) throws SQLException {
    return ((OracleConnection)connection).createOracleArray(CODE_COLLECTION_TYPE_NAME, toArray(values));
  }
  
  protected Array createNumbersOracleArray(Connection connection, List<Integer> values) throws SQLException {
    return ((OracleConnection)connection).createOracleArray(NUM_COLLECTION_TYPE_NAME, toArray(values));
  }

  @SuppressWarnings("resource")
  protected Array createStringOracleArray(Transaction transaction, String[] values) throws SQLException {
    return getOracleConnection(transaction).createOracleArray(CODE_COLLECTION_TYPE_NAME, values);
  }
  
  @SuppressWarnings("resource")
  protected Array createNumbersOracleArray(Transaction transaction, Integer[] values) throws SQLException {
    return getOracleConnection(transaction).createOracleArray(NUM_COLLECTION_TYPE_NAME, values);
  }
  
  @SuppressWarnings("resource")
  protected Array createStringOracleArray(Connection connection, String[] values) throws SQLException {
    return getOracleConnection(connection).createOracleArray(CODE_COLLECTION_TYPE_NAME, values);
  }
  
  @SuppressWarnings("resource")
  protected Array createNumbersOracleArray(Connection connection, Integer[] values) throws SQLException {
    return getOracleConnection(connection).createOracleArray(NUM_COLLECTION_TYPE_NAME, values);
  }

  @SuppressWarnings("resource")
  protected Array createGuidOracleArray(Connection connection, List<String> values) throws SQLException {
    return getOracleConnection(connection).createOracleArray(GUID_COLLECTION_TYPE_NAME, toArray(values));
  }

}
