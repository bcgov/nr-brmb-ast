package ca.bc.gov.srm.farm.chefs.resource.statementA;

import com.fasterxml.jackson.annotation.JsonAlias;

/** The Individuals form uses different field names from the Corporate form. */
public class StatementAIndividualsSubmissionDataResource extends StatementASubmissionDataResource {

  private String participantName;

  public String getParticipantName() {
    // Retain the participant name when reading earlier Individuals test submissions.
    return participantName == null ? getCorporationName() : participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
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
