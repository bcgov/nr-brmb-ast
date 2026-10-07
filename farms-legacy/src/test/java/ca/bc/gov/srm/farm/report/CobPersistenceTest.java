package ca.bc.gov.srm.farm.report;

import static ca.bc.gov.srm.farm.report.CobReportRendererTest.defaultValue;
import static ca.bc.gov.srm.farm.report.CobReportRendererTest.proxy;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.dao.CobDAO;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/** Verifies the Jasper-only persistence path; never connects to a real database. */
class CobPersistenceTest {
  private static final byte[] PDF = "%PDF-test".getBytes(StandardCharsets.US_ASCII);

  @Test
  void insertAndPdfUpdateParticipateInOneCallerOwnedTransaction() throws Exception {
    Fixture fixture = new Fixture(0);
    new CobDAO().saveJasperCob(fixture.transaction(), 1081568, PDF, true, "test-user");
    assertTrue(fixture.calls.get(0).contains("INSERT_COB"));
    assertTrue(fixture.calls.get(1).contains("UPDATE_COB_DOCUMENT"));
    assertEquals(1081568L, fixture.bindings.get(0).get(2));
    assertEquals("test-user", fixture.bindings.get(0).get(3));
    assertArrayEquals(PDF, (byte[]) fixture.bindings.get(1).get(2));
    assertEquals(2, fixture.executions);
    assertEquals(2, fixture.closedStatements);
  }

  @Test
  void reprintUsesUpdateNotInsert() throws Exception {
    Fixture fixture = new Fixture(1);
    new CobDAO().saveJasperCob(fixture.transaction(), 1081568, PDF, false, "test-user");
    assertTrue(fixture.calls.get(0).contains("UPDATE_COB("));
    assertEquals(1081568L, fixture.bindings.get(0).get(1));
    assertEquals(2, fixture.executions);
  }

  @Test
  void failedPdfWritePropagatesWithoutCommittingTheClearedDocument() {
    Fixture fixture = new Fixture(1);
    fixture.failPdfWrite = true;
    assertThrows(DataAccessException.class,
        () -> new CobDAO().saveJasperCob(fixture.transaction(), 1081568, PDF, false, "test-user"));
    assertEquals(2, fixture.closedStatements);
  }

  @Test
  void staleInsertAndReprintRequestsDoNotMutateDocuments() {
    for (boolean insert : new boolean[] {true, false}) {
      Fixture fixture = new Fixture(insert ? 1 : 0);
      assertThrows(DataAccessException.class,
          () -> new CobDAO().saveJasperCob(fixture.transaction(), 1081568, PDF, insert, "test-user"));
      assertTrue(fixture.calls.isEmpty());
    }
  }

  @Test
  void refusesAutoCommitAndEmptyDocuments() {
    Fixture fixture = new Fixture(0);
    fixture.autoCommit = true;
    assertThrows(DataAccessException.class,
        () -> new CobDAO().saveJasperCob(fixture.transaction(), 1081568, PDF, true, "test-user"));
    assertThrows(IllegalArgumentException.class,
        () -> new CobDAO().saveJasperCob(fixture.transaction(), 1081568, new byte[0], true, "test-user"));
    assertTrue(fixture.calls.isEmpty());
  }

  private static class Fixture {
    final int existingDocuments;
    final List<String> calls = new ArrayList<>();
    final List<Map<Integer, Object>> bindings = new ArrayList<>();
    boolean autoCommit;
    boolean failPdfWrite;
    int executions;
    int closedStatements;

    Fixture(int existingDocuments) {
      this.existingDocuments = existingDocuments;
    }

    Transaction transaction() {
      Connection connection = proxy(Connection.class, (o, m, a) -> {
        switch (m.getName()) {
          case "getAutoCommit": return autoCommit;
          case "prepareStatement":
            return proxy(PreparedStatement.class, (s, method, args) -> {
              if (method.getName().equals("executeQuery")) {
                int[] row = {0};
                return proxy(ResultSet.class, (r, operation, values) -> {
                  if (operation.getName().equals("next")) return row[0]++ == 0;
                  if (operation.getName().equals("getInt")) return existingDocuments;
                  return defaultValue(operation.getReturnType());
                });
              }
              return defaultValue(method.getReturnType());
            });
          case "prepareCall":
            String sql = (String) a[0];
            calls.add(sql);
            Map<Integer, Object> params = new HashMap<>();
            bindings.add(params);
            return proxy(CallableStatement.class, (s, method, args) -> {
              if (method.getName().equals("setLong") || method.getName().equals("setString")
                  || method.getName().equals("setBytes")) {
                params.put((Integer) args[0], args[1]);
              }
              if (method.getName().equals("execute")) {
                executions++;
                if (failPdfWrite && sql.contains("UPDATE_COB_DOCUMENT")) throw new SQLException("test failure");
              }
              if (method.getName().equals("close")) closedStatements++;
              return defaultValue(method.getReturnType());
            });
          case "commit": case "rollback": case "close": case "setAutoCommit":
            fail("The Jasper DAO must leave transaction ownership with the service: " + m.getName());
          default: return defaultValue(m.getReturnType());
        }
      });
      return proxy(Transaction.class, (o, m, a) -> m.getName().equals("getDatastore")
          ? connection : defaultValue(m.getReturnType()));
    }
  }
}
