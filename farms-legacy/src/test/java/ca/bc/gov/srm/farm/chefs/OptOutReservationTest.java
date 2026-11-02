package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.dao.OptOutSubmissionDAO;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.exception.DataAccessException;

/** Exercises the real reservation DAO with local JDBC responses, without Oracle. */
public class OptOutReservationTest {
  @Test void claimsAndCommitsBeforeTheCallerCanUpdateCore() throws Exception {
    Database database = new Database();
    assertEquals("request", database.dao.claimEnrolment(database.connection, "enrolment", 1, "request", "system"));
    assertEquals("request", database.owner);
    assertEquals("enrolment", database.target);
    assertEquals(1, database.inserts);
    assertEquals(1, database.commits);
    assertEquals(0, database.rollbacks);
  }

  @Test void retryReusesItsReservationWithoutAnotherInsert() throws Exception {
    Database database = new Database(); database.owner = "request"; database.target = "enrolment";
    assertEquals("request", database.dao.claimEnrolment(database.connection, "enrolment", 1, "request", "system"));
    assertEquals(0, database.inserts);
    assertEquals(1, database.commits);
  }

  @Test void anotherSubmissionReceivesTheExistingOwner() throws Exception {
    Database database = new Database(); database.owner = "previous-request";
    assertEquals("previous-request", database.dao.claimEnrolment(database.connection, "enrolment", 2, "request", "system"));
    assertEquals(0, database.inserts);
    assertEquals(1, database.commits);
  }

  @Test void losingAUniqueKeyRaceReturnsTheWinningRequestAfterRollback() throws Exception {
    Database database = new Database(); database.race = true;
    assertEquals("winning-request", database.dao.claimEnrolment(database.connection, "enrolment", 2, "request", "system"));
    assertEquals(1, database.rollbacks);
    assertEquals(0, database.commits);
  }

  @Test void unrelatedDatabaseErrorsRemainErrors() {
    Database database = new Database(); database.failInsert = true;
    assertThrows(DataAccessException.class,
        () -> database.dao.claimEnrolment(database.connection, "enrolment", 1, "request", "system"));
    assertEquals(1, database.rollbacks);
    assertEquals(0, database.commits);
  }

  @Test void editedRequestsCannotReserveAnotherEnrolment() {
    Database database = new Database(); database.target = "original-enrolment";
    assertThrows(DataAccessException.class,
        () -> database.dao.claimEnrolment(database.connection, "changed-enrolment", 1, "request", "system"));
    assertEquals(0, database.inserts);
    assertEquals(1, database.rollbacks);
  }

  private static class Database {
    String owner; String target;
    int commits; int rollbacks; int inserts;
    boolean race; boolean failInsert;
    final OptOutSubmissionDAO dao = new OptOutSubmissionDAO() {
      @Override public void createCrmEntity(Connection connection, ChefsSubmssnCrmEntity entity, String user) throws DataAccessException {
        inserts++;
        assertEquals("ENROLMENT", entity.getCrmEntityTypeCode());
        if (race) { owner = "winning-request"; throw new DataAccessException("Simulated unique key conflict"); }
        if (failInsert) throw new DataAccessException("Simulated database failure");
        owner = "request"; target = entity.getCrmEntityGuid();
      }
    };
    final Connection connection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[] { Connection.class },
        (proxy, method, arguments) -> {
          if ("prepareStatement".equals(method.getName())) return statement((String) arguments[0]);
          if ("commit".equals(method.getName())) { commits++; return null; }
          if ("rollback".equals(method.getName())) { rollbacks++; return null; }
          throw new UnsupportedOperationException(method.getName());
        });

    private PreparedStatement statement(String sql) {
      Map<Integer, Object> parameters = new HashMap<>();
      return (PreparedStatement) Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(), new Class<?>[] { PreparedStatement.class },
          (proxy, method, arguments) -> {
            if ("setInt".equals(method.getName()) || "setString".equals(method.getName())) {
              parameters.put((Integer) arguments[0], arguments[1]); return null;
            }
            if ("close".equals(method.getName())) return null;
            if ("executeQuery".equals(method.getName())) {
              assertNotNull(parameters.get(1));
              if (sql.endsWith("FOR UPDATE")) return result("locked-submission");
              assertEquals("ENROLMENT", parameters.get(2));
              if (sql.startsWith("SELECT crm_entity_guid")) return result(target);
              assertTrue(sql.contains("JOIN farm_chef_submissions"));
              return result(owner);
            }
            throw new UnsupportedOperationException(method.getName());
          });
    }

    private ResultSet result(String value) {
      boolean[] visited = { false };
      return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class },
          (proxy, method, arguments) -> {
            if ("next".equals(method.getName())) { boolean available = !visited[0] && value != null; visited[0] = true; return available; }
            if ("getString".equals(method.getName())) return value;
            if ("close".equals(method.getName())) return null;
            throw new UnsupportedOperationException(method.getName());
          });
    }
  }
}
