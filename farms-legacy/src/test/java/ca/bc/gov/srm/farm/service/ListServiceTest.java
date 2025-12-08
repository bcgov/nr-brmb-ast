/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.InventoryItemListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class ListServiceTest {

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
  }


  @Test
  public void listServiceInventory() {
    
    ListService listService = ServiceFactory.getListService();
    
    // The ListService has a TestListServiceImpl implementation that is used by
    // unit tests, if the TestUtils.overrideServices() is called.
    try {
      // Since the TestListServiceImpl does not load all of its lists from
      // the database then to make sure the inventory item lists (there are several)
      // are loaded we call this refresh method (normally used by the admin screens
      // when the data is modified).
      listService.refreshCodeTableList(CodeTables.INVENTORY_ITEM);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    List<ListView> inventoryItemList = listService.getList(ListService.INVENTORY_VALID_ITEM);
    assertNotNull(inventoryItemList);
    assertFalse(inventoryItemList.isEmpty());
    
    String inventoryItemCode = "5030"; // Apples
    
    InventoryItemListView foundItem = findCropOrLivestockItem(inventoryItemList, inventoryItemCode);

    InventoryItem inventoryItem = createInventoryItemObject(foundItem);
    
    assertNotNull(inventoryItem);
    assertEquals(inventoryItem.getInventoryClassCode(), InventoryClassCodes.CROP);
    assertTrue(inventoryItem instanceof CropItem);
  }


  private InventoryItem createInventoryItemObject(InventoryItemListView item) {
    InventoryItem inventoryItem = null;
    if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP) ) {
      CropItem cropItem = new CropItem();
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      inventoryItem = cropItem;
    } else if(item.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK) ) {
      LivestockItem livestockItem = new LivestockItem();
      livestockItem.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      inventoryItem = livestockItem;
    }
    return inventoryItem;
  }

  private InventoryItemListView findCropOrLivestockItem(List<ListView> inventoryItemList, String inventoryItemCode) {
    InventoryItemListView foundItem =
        inventoryItemList.stream()
        .map(i -> (InventoryItemListView) i)
        .filter(i -> i.getValue().equals(inventoryItemCode)
            && (i.getInventoryClassCode().equals(InventoryClassCodes.CROP)
                || i.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)))
    .findFirst()
    .orElse(null);
    return foundItem;
  }

}
