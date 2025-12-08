package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.list.FarmSubtypeListView;
import ca.bc.gov.srm.farm.list.FarmType3ListView;

public class TipsFarmSubtypeForm extends ValidatorForm {
  private static final long serialVersionUID = 5533699556381795444L;
  
  private int numFarmSubtypes;
  private boolean isNew = false;
  
  private Integer id;
  private Integer parentId;
  private Integer secondaryParentId;
  
  private String name;
  private String parentName;
  private String secondaryParentName;
  private Integer revisionCount;
  
  private List<FarmSubtype> farmSubtypeAItems;
  private List<FarmSubtype> farmSubtypeBItems;
  private List<FarmType3> farmTypeItems;
  
  private List<FarmType3ListView> farmTypeListViewItems;
  private List<FarmSubtypeListView> farmSubtypeAListViewItems;
  
  private List<TipFarmTypeIncomeRange> incomeRange;
  private String incomeRangeJson;
  private Boolean usingDefaultRange;
  private Boolean isInherited;
  private String inheritedFrom;

  public int getNumFarmSubtypes() {
    return numFarmSubtypes;
  }

  public void setNumFarmSubtypes(int numFarmSubtypes) {
    this.numFarmSubtypes = numFarmSubtypes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public List<FarmSubtype> getFarmSubtypeAItems() {
    return farmSubtypeAItems;
  }

  public void setFarmSubtypeAItems(List<FarmSubtype> farmSubtypeItems) {
    this.farmSubtypeAItems = farmSubtypeItems;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  public List<FarmType3> getFarmTypeItems() {
    return farmTypeItems;
  }

  public void setFarmTypeItems(List<FarmType3> farmTypeItems) {
    this.farmTypeItems = farmTypeItems;
  }

  public List<FarmType3ListView> getFarmTypeListViewItems() {
    return farmTypeListViewItems;
  }

  public void setFarmTypeListViewItems(List<FarmType3ListView> farmTypeListViewItems) {
    this.farmTypeListViewItems = farmTypeListViewItems;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<FarmSubtype> getFarmSubtypeBItems() {
    return farmSubtypeBItems;
  }

  public void setFarmSubtypeBItems(List<FarmSubtype> farmSubtypeBItems) {
    this.farmSubtypeBItems = farmSubtypeBItems;
  }

  public List<FarmSubtypeListView> getFarmSubtypeAListViewItems() {
    return farmSubtypeAListViewItems;
  }

  public void setFarmSubtypeAListViewItems(List<FarmSubtypeListView> farmSubtypeAListViewItems) {
    this.farmSubtypeAListViewItems = farmSubtypeAListViewItems;
  }

  public String getSecondaryParentName() {
    return secondaryParentName;
  }

  public void setSecondaryParentName(String secondaryParentName) {
    this.secondaryParentName = secondaryParentName;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public Integer getSecondaryParentId() {
    return secondaryParentId;
  }

  public void setSecondaryParentId(Integer secondaryParentId) {
    this.secondaryParentId = secondaryParentId;
  }

  public List<TipFarmTypeIncomeRange> getIncomeRange() {
    return incomeRange;
  }

  public void setIncomeRange(List<TipFarmTypeIncomeRange> incomeRange) {
    this.incomeRange = incomeRange;
  }

  public String getIncomeRangeJson() {
    return incomeRangeJson;
  }

  public void setIncomeRangeJson(String incomeRangeJson) {
    this.incomeRangeJson = incomeRangeJson;
  }

  public Boolean getUsingDefaultRange() {
    return usingDefaultRange;
  }

  public void setUsingDefaultRange(Boolean usingDefaultRange) {
    this.usingDefaultRange = usingDefaultRange;
  }

  public Boolean getIsInherited() {
    return isInherited;
  }

  public void setIsInherited(Boolean isInherited) {
    this.isInherited = isInherited;
  }

  public String getInheritedFrom() {
    return inheritedFrom;
  }

  public void setInheritedFrom(String inheritedFrom) {
    this.inheritedFrom = inheritedFrom;
  }
}