package ca.bc.gov.srm.farm.chefs.resource.statementA;

import com.fasterxml.jackson.annotation.JsonAlias;

/** The Individuals form uses different field names from the Corporate form. */
public class StatementAIndividualsSubmissionDataResource extends StatementASubmissionDataResource {

  private String participantName;
  private String shouldBeCombinedCurrentYear;
  private String wasCombinedLast3Years;
  private Double total;

  public String getParticipantName() {
    // Retain the participant name when reading earlier Individuals test submissions.
    return participantName == null ? getCorporationName() : participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
  }

  @Override
  @JsonAlias("adjustmentToBusinessUseOfHomeExpenses")
  public void setBusinessUseOfHomeAdjustment(Double amount) {
    super.setBusinessUseOfHomeAdjustment(amount);
  }

  @Override
  @JsonAlias("gsthstRebateForPartnersReceivedInTheYear")
  public void setGstHstRebateForPartners(Double amount) {
    super.setGstHstRebateForPartners(amount);
  }

  // Preserve the form's combining-history answers and partnership display total
  // for management/PDF output, without changing the shared import rules.
  public String getShouldBeCombinedCurrentYear() {
    return shouldBeCombinedCurrentYear;
  }

  public void setShouldBeCombinedCurrentYear(String shouldBeCombinedCurrentYear) {
    this.shouldBeCombinedCurrentYear = shouldBeCombinedCurrentYear;
  }

  public String getWasCombinedLast3Years() {
    return wasCombinedLast3Years;
  }

  public void setWasCombinedLast3Years(String wasCombinedLast3Years) {
    this.wasCombinedLast3Years = wasCombinedLast3Years;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  @Override
  @JsonAlias("SummaryOfIncome")
  public void setSummaryOfIncome(Double amount) {
    super.setSummaryOfIncome(amount);
  }

  @Override
  @JsonAlias("SummaryOfExpenses")
  public void setSummaryOfExpenses(Double amount) {
    super.setSummaryOfExpenses(amount);
  }

}
