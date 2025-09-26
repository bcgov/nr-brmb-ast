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

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  dzwiers
 */
public class RunStagingClear {

  private Connection conn = null;

  private StagingDAO dao = null;

  @BeforeEach
  protected final void setUp() throws Exception {
    conn = TestUtils.openConnection();
    dao = new StagingDAO(conn);
  }

  @AfterEach
  protected final void tearDown() throws Exception {
    if (conn != null) {
      conn.commit();
      conn.close();
    }
  }

  @Test
  public final void testClear() throws SQLException {
    dao.clear();
  }
}
