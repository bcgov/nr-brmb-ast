package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmQueueItemTest {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(CrmQueueItemTest.class);

  private static CrmRestApiDao crmDao;
  
  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    
    conn = TestUtils.openConnection();
    crmDao = new CrmRestApiDao();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }
  

  @Test
  public void createTaskAndAddToQueue() {
    
    int participantPin = 4248530;
    String queueName = "<Andrew Wilkinson>";
    
    CrmQueueResource queue = null;
    try {
      queue = crmDao.getQueueByName(queueName);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(queue);
    
    CrmAccountResource accountResource = null;
    try {
      accountResource = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(accountResource);
    
    String accountId = accountResource.getAccountid();
    
    CrmTaskResource task = null;
    try {
      task = crmDao.createAndGetTask("Queue Test Subject", "Queue Test Description", accountResource, queue.getQueueId());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    assertNotNull(task);
    assertEquals("Queue Test Subject", task.getSubject());
    assertEquals(accountId, task.getAccountId());
    assertEquals("Queue Test Description", task.getDescription());
  }

}
