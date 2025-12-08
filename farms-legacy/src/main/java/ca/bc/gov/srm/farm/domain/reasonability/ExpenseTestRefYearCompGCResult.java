package ca.bc.gov.srm.farm.domain.reasonability;

public class ExpenseTestRefYearCompGCResult extends ReasonabilityTestResult {

  private static final long serialVersionUID = -3653694503517486705L;
  
  public static final String NAME = "EXPENSE GENERAL CROPS";

  private Double programYearAcrAdjExpense;
  private Double expenseStructuralChangeYearMinus1;
  private Double expenseStructuralChangeYearMinus2;
  private Double expenseStructuralChangeYearMinus3;
  private Double expenseStructuralChangeYearMinus4;
  private Double expenseStructuralChangeYearMinus5;
  
  private Double variance;
  private Double varianceLimit;
  private Double expenseStructrualChangeAverage;
  
  @Override
  public String getName() {
    return ExpenseTestRefYearCompGCResult.NAME;
  }

  public Double getExpenseStructuralChangeYearMinus1() {
    return expenseStructuralChangeYearMinus1;
  }

  public void setExpenseStructuralChangeYearMinus1(Double expenseStructuralChangeYearMinus1) {
    this.expenseStructuralChangeYearMinus1 = expenseStructuralChangeYearMinus1;
  }

  public Double getExpenseStructuralChangeYearMinus2() {
    return expenseStructuralChangeYearMinus2;
  }

  public void setExpenseStructuralChangeYearMinus2(Double expenseStructuralChangeYearMinus2) {
    this.expenseStructuralChangeYearMinus2 = expenseStructuralChangeYearMinus2;
  }

  public Double getExpenseStructuralChangeYearMinus3() {
    return expenseStructuralChangeYearMinus3;
  }

  public void setExpenseStructuralChangeYearMinus3(Double expenseStructuralChangeYearMinus3) {
    this.expenseStructuralChangeYearMinus3 = expenseStructuralChangeYearMinus3;
  }

  public Double getExpenseStructuralChangeYearMinus4() {
    return expenseStructuralChangeYearMinus4;
  }

  public void setExpenseStructuralChangeYearMinus4(Double expenseStructuralChangeYearMinus4) {
    this.expenseStructuralChangeYearMinus4 = expenseStructuralChangeYearMinus4;
  }

  public Double getExpenseStructuralChangeYearMinus5() {
    return expenseStructuralChangeYearMinus5;
  }

  public void setExpenseStructuralChangeYearMinus5(Double expenseStructuralChangeYearMinus5) {
    this.expenseStructuralChangeYearMinus5 = expenseStructuralChangeYearMinus5;
  }

  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }

  public Double getExpenseStructrualChangeAverage() {
    return expenseStructrualChangeAverage;
  }

  public void setExpenseStructrualChangeAverage(Double expenseStructrualChangeAverage) {
    this.expenseStructrualChangeAverage = expenseStructrualChangeAverage;
  }

  public Double getProgramYearAcrAdjExpense() {
    return programYearAcrAdjExpense;
  }

  public void setProgramYearAcrAdjExpense(Double programYearAcrAdjExpense) {
    this.programYearAcrAdjExpense = programYearAcrAdjExpense;
  }
  
  public void copy(ExpenseTestRefYearCompGCResult o) {
    super.copy(o);
    programYearAcrAdjExpense = o.programYearAcrAdjExpense;
    expenseStructuralChangeYearMinus1 = o.expenseStructuralChangeYearMinus1;
    expenseStructuralChangeYearMinus2 = o.expenseStructuralChangeYearMinus2;
    expenseStructuralChangeYearMinus3 = o.expenseStructuralChangeYearMinus3;
    expenseStructuralChangeYearMinus4 = o.expenseStructuralChangeYearMinus4;
    expenseStructuralChangeYearMinus5 = o.expenseStructuralChangeYearMinus5;
    variance = o.variance;
    varianceLimit = o.varianceLimit;
    expenseStructrualChangeAverage = o.expenseStructrualChangeAverage;
  }
}