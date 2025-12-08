package ca.bc.gov.srm.farm.reasonability;

import java.util.Arrays;
import java.util.List;

import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.util.MathUtils;

public final class ReasonabilityTestUtils {
  
  // Forage codes for crops that are fed to cattle. This array specifies the
  // order in which the forage quantities are reduced.
  public static final String[] FED_OUT_INVENTORY_CODE_ORDER = {
      "5584", // Silage
      "5583", // Silage; Corn
      "5562", // Greenfeed
      "5580", // Haylage
      
      // Hay codes
      "5564", // Hay; Alfalfa
      "5566", // Hay; Alfalfa; Organic
      "5568", // Hay; Alfalfa/Brome
      "5570", // Hay; Alfalfa/Grass
      "5572", // Hay; Clover
      "5574", // Hay; Grass
      "5576", // Hay; Other
      "5578", // Hay; Slough
      "5579", // Hay; Timothy
      };
  
  private static final List<String> FED_OUT_INVENTORY_CODE_ORDER_LIST;
  static {
    FED_OUT_INVENTORY_CODE_ORDER_LIST = Arrays.asList(FED_OUT_INVENTORY_CODE_ORDER);
  }
  
  
  private ReasonabilityTestUtils() {
    // private constructor
  }
  
  public static void addErrorMessage(List<ReasonabilityTestResultMessage> messages, String message, Object... parameters) {
    addMessage(messages, MessageTypeCodes.ERROR, message, parameters);
  }
  
  public static void addInfoMessage(List<ReasonabilityTestResultMessage> messages, String message, Object... parameters) {
    addMessage(messages, MessageTypeCodes.INFO, message, parameters);
  }
  
  public static void addWarningMessage(List<ReasonabilityTestResultMessage> messages, String message, Object... parameters) {
    addMessage(messages, MessageTypeCodes.WARNING, message, parameters);
  }
  
  public static void addMessage(List<ReasonabilityTestResultMessage> messages,
      String messageTypeCode, String message, Object... parameters) {
    
    boolean found = false;
    for(ReasonabilityTestResultMessage msg : messages) {
      if(msg.getMessage().equals(message)) {
        found = true;
        break;
      }
    }
    
    if(!found) {
      ReasonabilityTestResultMessage msg = new ReasonabilityTestResultMessage(message, messageTypeCode, parameters);
      messages.add(msg);
    }
  }
  
  public static double roundPercent(double percent) {
    return MathUtils.round(percent, ReasonabilityTest.PERCENT_DECIMAL_PLACES);
  }
  
  public boolean isForageConsumedByCattle(String inventoryItemCode) {
    return FED_OUT_INVENTORY_CODE_ORDER_LIST.contains(inventoryItemCode);
  }
}