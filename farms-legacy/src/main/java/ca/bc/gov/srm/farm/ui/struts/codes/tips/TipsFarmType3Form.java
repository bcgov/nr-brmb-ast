package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;

public class TipsFarmType3Form extends ValidatorForm {
	
  private static final long serialVersionUID = -5209522543699372420L;
  
  private boolean isNew = false;
  
  private List<FarmType3> farmTypeItems;
  private List<TipFarmTypeIncomeRange> incomeRange;
  private String incomeRangeJson;
  private Boolean usingDefaultRange;
  
  private int numFarmTypes;
  private String farmTypeName;
  private Integer id;
  private Integer revisionCount;
	
  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }
  
  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean pIsNew) {
    isNew = pIsNew;
  }

  public List<FarmType3> getFarmTypeItems() {
	return farmTypeItems;
  }

  public void setFarmTypeItems(List<FarmType3> farmTypeItems) {
	this.farmTypeItems = farmTypeItems;
  }
  
  public int getNumFarmTypes() { 
	 return this.numFarmTypes;
  }
  
  public void setNumFarmTypes(int numFarmTypes) {
    this.numFarmTypes = numFarmTypes;
  }

  public String getFarmTypeName() {
    return farmTypeName;
  }
	
  public void setFarmTypeName(String farmTypeName) {
    this.farmTypeName = farmTypeName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getRevisionCount() {
	return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
	this.revisionCount = revisionCount;
  }

  public List<TipFarmTypeIncomeRange> getIncomeRange() {
    return incomeRange;
  }

  public void setIncomeRange(List<TipFarmTypeIncomeRange> incomeRange) {
    this.incomeRange = incomeRange;
  }

  public Boolean getUsingDefaultRange() {
    return usingDefaultRange;
  }

  public void setUsingDefaultRange(Boolean usingDefaultRange) {
    this.usingDefaultRange = usingDefaultRange;
  }

  public String getIncomeRangeJson() {
    return incomeRangeJson;
  }

  public void setIncomeRangeJson(String incomeRangeJson) {
    this.incomeRangeJson = incomeRangeJson;
  }  
}