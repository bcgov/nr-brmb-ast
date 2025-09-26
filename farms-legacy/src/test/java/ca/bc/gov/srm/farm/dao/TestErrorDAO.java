/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  dzwiers
 */
public class TestErrorDAO {

  private Connection conn = null;

  private ErrorDAO dao = null;

  @BeforeEach
  protected final void setUp() throws Exception {
    conn = TestUtils.openConnection();
    dao = new ErrorDAO(conn);
  }

  @AfterEach
  protected final void tearDown() throws Exception {

    if (conn != null) {
      conn.rollback();
      conn.close();
    }
  }

  @Test
  public final void testReadClient() throws DataAccessException {
    String msg = dao.codify("FARM_AS_FARM_STC_FK");
    assertNotNull(msg);
  }
}
