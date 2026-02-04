/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.submission;

import java.util.Objects;

public class LabelValue {

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public LabelValue() {}

	public LabelValue(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return "LabelValueBase [label=" + label + ", value=" + value + "]";
	}

  @Override
  public int hashCode() {
    return Objects.hash(label, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LabelValue other = (LabelValue) obj;
    return Objects.equals(label, other.label) && Objects.equals(value, other.value);
  }

}
