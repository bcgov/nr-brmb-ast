/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.notes;

import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 * @created Mar 2, 2011
 */
public class VerificationNotesForm extends CalculatorForm {

  private static final long serialVersionUID = -2645551921365403100L;
  
  public static final String NOTE_TYPE_INTERIM = "INTERIM";
  public static final String NOTE_TYPE_FINAL = "FINAL";
  public static final String NOTE_TYPE_ADJUSTMENT = "ADJUSTMENT";

  private String noteType;
  
  private String notes;

  public String getNoteType() {
    return noteType;
  }

  public void setNoteType(String noteType) {
    this.noteType = noteType;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
