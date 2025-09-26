package ca.bc.gov.srm.farm.domain.codes;

import java.util.Arrays;
import java.util.List;

public class ParticipantDataSrcCodes {

  public static final String CRA = "CRA";
  public static final String LOCAL = "LOCAL";
  
  // NONE does not exist in the code table.
  // and is used only for the UI.
  public static final String NONE = "NONE";

  private ParticipantDataSrcCodes() {
    // private constructor
  }

  public static List<String> getCodeList() {
    return Arrays.asList(new String[] {CRA, LOCAL});
  }
}
