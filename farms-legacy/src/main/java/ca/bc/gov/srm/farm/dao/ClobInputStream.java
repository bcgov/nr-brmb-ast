package ca.bc.gov.srm.farm.dao;

import oracle.sql.CLOB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * ClobInputStream.
 *
 * @author   $author$
 * @version  $Revision: 5660 $, $Date: 2024-11-22 16:15:03 -0800 (Fri, 22 Nov 2024) $
 */
class ClobInputStream extends InputStream {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClobInputStream.class);

  private InputStream in = null;

  private CallableStatement statement = null;

  private ResultSet resultSet = null;

  private CLOB clob = null;

  /**
   * constructor.
   *
   * @param  c    document me
   * @param  rs   document me
   * @param  stm  document me
   * @param  is   document me
   */
  ClobInputStream(final CLOB c, final ResultSet rs, final CallableStatement stm,
    final InputStream is) {
    this.in = is;
    this.clob = c;
    this.resultSet = rs;
    this.statement = stm;
  }

  /**
   * @return  int
   *
   * @throws  IOException  On IOException
   */
  @Override
  public int read() throws IOException {
    return in.read();
  }

  /**
   * @throws  IOException  On IOException
   */
  @Override
  public void close() throws IOException {

    try {

      if (clob != null) {
        clob.close();
      }

      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }
    } catch (SQLException e) {
      LOGGER.warn(e.getMessage(), e);
    }

    in.close();
  }

}
