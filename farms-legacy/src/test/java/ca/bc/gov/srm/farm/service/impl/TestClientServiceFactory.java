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
package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author  dzwiers
 */
public class TestClientServiceFactory {

  private Connection conn = null;

  @BeforeEach
  protected final void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterEach
  protected final void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. */
  @Test
  public final void testClientInfoWithHistory() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(1531227, 2009, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(ac);
    assertNotNull(ac.getFarmingYear());

    assertNotNull(ac.getClient().getOwner());
    assertNotNull(ac.getClient().getContact());

    FarmingYear pyv = ac.getFarmingYear();
    assertEquals(2009,ac.getYear().intValue());

    List<FarmingOperation> farms = pyv.getFarmingOperations();
    assertEquals(1, farms.size());
    assertEquals(29, farms.get(0).getIncomeExpenses().size());
    assertEquals(4, farms.get(0).getProductiveUnitCapacities().size());
    assertEquals(10, farms.get(0).getInventoryItems().size());

    assertEquals(ScenarioTypeCodes.USER, ac.getScenarioTypeCode());

  }

  /** This test assumes data was loaded from the fipd_V12_sample.zip file. */
  @Test
  public final void testClientInfoWithHistory3() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(7654321, 2009, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(ac);
  }

  /** This test assumes data was loaded from the fipd_V12_sample.zip file. */
  @Test
  public final void testClientInfoWithHistory4() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(23466410, 2009, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(ac);
  }

  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  @Test
  public final void testClientInfoWithHistory5() throws Exception {

    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(22277743, 2009, new Integer(2), ClientService.DEF_FIRST_MODE);
  
    assertNotNull(ac);
    assertEquals(new Integer(2), ac.getScenarioNumber());
  }
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  @Test
  public final void testClientInfoWithHistory6() throws Exception {
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(22818199, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(ac);
  }
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  @Test
  public final void testClientInfoWithHistory7() throws Exception {
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(4328423, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(ac);
  }
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  // disabled this test because the failure is related to a known data issue
  /*public final void testClientInfoWithHistory8(){
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(22467377, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(ac);
  }*/
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  @Test
  public final void testClientInfoWithHistory9() throws Exception {
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(1579432, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(ac);
  }
  
  /** This test assumes data was loaded from the fipd_V12_sample.zip file. 
   * @throws SQLException */
  @Test
  public final void testClientInfoWithHistory10() throws Exception {
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario ac = cs.getClientInfoWithHistory(3712601, 2009, null, ClientService.COMP_FIRST_MODE);
    
    assertNotNull(ac);
    assertTrue(ac.getIsDefaultInd().booleanValue());
    assertEquals(ScenarioStateCodes.VERIFIED,ac.getScenarioStateCode());

    ac = cs.getClientInfoWithHistory(3712601, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(ac);
    assertTrue(ac.getIsDefaultInd().booleanValue());
  }
  
  
  /**
   * make sure the farm_reference_scenarios fields are set correctly
   */
  @Test
  public final void testClientInfoWithHistory11() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(22232284, 2009, null, ClientService.DEF_FIRST_MODE);
    
    assertNotNull(sc);
    assertTrue(sc.getIsDefaultInd().booleanValue());
    
    //select * from farm_reference_scenarios t where t.for_agristability_scenario_id = 36341
		for(ReferenceScenario rs : sc.getReferenceScenarios()) {
			int id = rs.getScenarioId().intValue();
			
			switch(id) {
			  case 36342: {assertFalse(rs.getUsedInCalc().booleanValue()); break;}
			  case 36343: {assertTrue(rs.getUsedInCalc().booleanValue()); break;}
			  case 36344: {assertTrue(rs.getUsedInCalc().booleanValue()); break;}
			  case 36345: {assertTrue(rs.getUsedInCalc().booleanValue()); break;}
			  case 36346: {assertFalse(rs.getUsedInCalc().booleanValue()); break;}
			}
		}
  }
  
  
  /**
   * make sure operations have BUPs
   */
  @Test
  public final void testClientInfoWithHistory12() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(3231917, 2009, null, ClientService.COMP_FIRST_MODE);
    
    assertNotNull(sc);
    
    for(ReferenceScenario rs : sc.getReferenceScenarios()) {
			int year = rs.getYear().intValue();
			
      for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
			
			  for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
				  
				  System.out.println("year: " + year + 
						", inv code: " + puc.getInventoryItemCode() + 
				    ", group code: " + puc.getStructureGroupCode() );
				  assertNotNull(puc.getBasePricePerUnit());
			  }
      }
		}
  }
  
  
  /**
   * make sure structural change fields are set
   */
  @Test
  public final void testClientInfoWithHistory13() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(23015787, 2009, null, ClientService.COMP_FIRST_MODE);
    
    assertNotNull(sc);
    assertNotNull(sc.getFarmingYear().getBenefit().getStructuralChangeMethodCode());
    assertNotNull(sc.getFarmingYear().getBenefit().getStructuralChangeMethodCodeDescription());
  }
  
  
  /**
   * make sure we get the expected PUCs
   */
  @Test
  public final void testClientInfoWithHistory14() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(23015787, 2009, null, ClientService.COMP_FIRST_MODE);
    
    assertNotNull(sc);
    
    FarmingYear fy = sc.getFarmingYear();
    assertEquals(1, fy.getFarmingOperations().size());
    
    FarmingOperation fo = fy.getFarmingOperationByNumber(new Integer(1));
    assertEquals(1, fo.getProductiveUnitCapacities().size());
    
  }
  
  
  /**
   * make sure reference years are loaded for a CRA scenario
   */
  @Test
  public final void testClientInfoWithHistory15() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(22488597, 2009, new Integer(1), ClientService.DEF_FIRST_MODE);
    
    assertNotNull(sc);
    
    List<ReferenceScenario> refScenarios = sc.getReferenceScenarios();
    assertNotNull(refScenarios);
    assertEquals(5, refScenarios.size());
    
    ReferenceScenario rs2004 = refScenarios.get(0);
    ReferenceScenario rs2005 = refScenarios.get(1);
    ReferenceScenario rs2006 = refScenarios.get(2);
    ReferenceScenario rs2007 = refScenarios.get(3);
    ReferenceScenario rs2008 = refScenarios.get(4);
    
    assertNotNull(rs2004);
    assertNotNull(rs2005);
    assertNotNull(rs2006);
    assertNotNull(rs2007);
    assertNotNull(rs2008);
    
    assertNotNull(rs2004.getScenarioId());
    assertNotNull(rs2005.getScenarioId());
    assertNotNull(rs2006.getScenarioId());
    assertNotNull(rs2007.getScenarioId());
    assertNotNull(rs2008.getScenarioId());
  }
  
  
  /**
   * make sure program year IS loaded and reference years are NOT loaded
   * when loading without history 
   */
  @Test
  public final void testClientInfoWithoutHistory() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithoutHistory(22274997, 2009, new Integer(1), ClientService.DEF_FIRST_MODE);
    
    assertNotNull(sc);
    assertNotNull(sc.getScenarioId());
    assertEquals(new Integer(2009), sc.getYear());
    
    List<ReferenceScenario> refScenarios = sc.getReferenceScenarios();
    assertNotNull(refScenarios);
    assertEquals(0, refScenarios.size());
  }

}
