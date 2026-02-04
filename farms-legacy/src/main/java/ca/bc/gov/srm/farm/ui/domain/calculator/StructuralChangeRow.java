package ca.bc.gov.srm.farm.ui.domain.calculator;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean used by screen 930.
 */
public class StructuralChangeRow {

  private String code;

  private String description;
  
  private Map<Integer, Double> capacityYearMap = new HashMap<>();
  private Map<Integer, Double> varianceYearMap = new HashMap<>();

  // margin values
  private Map<Integer, Double> bpuYearMap = new HashMap<>();
  private Map<Integer, Double> adjustmentsYearMap = new HashMap<>();
  private Map<Integer, Double> refYearProdValueYearMap = new HashMap<>(); // reference year productive value
  private Map<Integer, Double> progYearProdValueYearMap = new HashMap<>(); // program year productive value

  // expense values
  private Map<Integer, Double> expenseBpuYearMap = new HashMap<>();
  private Map<Integer, Double> expenseAdjustmentsYearMap = new HashMap<>();
  private Map<Integer, Double> expenseRefYearProdValueYearMap = new HashMap<>(); // reference year productive value
  private Map<Integer, Double> expenseProgYearProdValueYearMap = new HashMap<>(); // program year productive value
  
	/**
	 * @return the adjustmentsYearMap
	 */
	public Map<Integer, Double> getAdjustmentsYearMap() {
		return adjustmentsYearMap;
	}
	/**
	 * @return the bpuYearMap
	 */
	public Map<Integer, Double> getBpuYearMap() {
		return bpuYearMap;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the decription
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param decription the decription to set
	 */
	public void setDescription(String decription) {
		this.description = decription;
	}
	/**
	 * @return the unitYearMap
	 */
	public Map<Integer, Double> getCapacityYearMap() {
		return capacityYearMap;
	}
	/**
	 * @return the varianceYearMap
	 */
	public Map<Integer, Double> getVarianceYearMap() {
		return varianceYearMap;
	}
	/**
	 * @return the progYearProdValueYearMap
	 */
	public Map<Integer, Double> getProgYearProdValueYearMap() {
		return progYearProdValueYearMap;
	}
	/**
	 * @return the refYearProdValueYearMap
	 */
	public Map<Integer, Double> getRefYearProdValueYearMap() {
		return refYearProdValueYearMap;
	}
  /**
   * @return the expenseBpuYearMap
   */
  public Map<Integer, Double> getExpenseBpuYearMap() {
    return expenseBpuYearMap;
  }
  /**
   * @return the expenseAdjustmentsYearMap
   */
  public Map<Integer, Double> getExpenseAdjustmentsYearMap() {
    return expenseAdjustmentsYearMap;
  }
  /**
   * @return the expenseRefYearProdValueYearMap
   */
  public Map<Integer, Double> getExpenseRefYearProdValueYearMap() {
    return expenseRefYearProdValueYearMap;
  }
  /**
   * @return the expenseProgYearProdValueYearMap
   */
  public Map<Integer, Double> getExpenseProgYearProdValueYearMap() {
    return expenseProgYearProdValueYearMap;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "StructuralChangeRow [code=" + code + ", description=" + description + "]";
  }
	
}
