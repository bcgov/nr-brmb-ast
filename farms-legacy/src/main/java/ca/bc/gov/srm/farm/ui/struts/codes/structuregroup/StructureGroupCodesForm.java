/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.structuregroup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.StructureGroupCode;

/**
 * @author hwang
 */
public class StructureGroupCodesForm extends ValidatorForm {

    private List<StructureGroupCode> codes;
    private int numCodes;

    private String code;
    private String description;
    private String rollupStructureGroupCode;
    private String rollupStructureGroupCodeDescription;
    private String structureGroupSearchInput;
    private Integer revisionCount;

    private boolean isNew = false;

    private boolean isSetFilterContext;
    private String codeFilter;
    private String descriptionFilter;

    /**
     * @param mapping mapping
     * @param request request
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        setNew(false);
    }

    /**
     * Gets codes
     *
     * @return the codes
     */
    public List<StructureGroupCode> getCodes() {
        return codes;
    }

    /**
     * Sets codes
     *
     * @param pCodes the codes to set
     */
    public void setCodes(final List<StructureGroupCode> pCodes) {
        this.codes = pCodes;
    }

    /**
     * Gets numCodes
     *
     * @return the numCodes
     */
    public int getNumCodes() {
        return numCodes;
    }

    /**
     * Sets numCodes
     *
     * @param pNumCodes the numCodes to set
     */
    public void setNumCodes(final int pNumCodes) {
        this.numCodes = pNumCodes;
    }

    /**
     * Gets code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code
     *
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Gets description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description
     *
     * @param pDescription the description to set
     */
    public void setDescription(final String pDescription) {
        this.description = pDescription;
    }

    /**
     * Gets rollupStructureGroupCode
     *
     * @return the rollupStructureGroupCode
     */
    public String getRollupStructureGroupCode() {
        return rollupStructureGroupCode;
    }

    /**
     * Sets rollupStructureGroupCode
     *
     * @param pRollupStructureGroupCode the rollupStructureGroupCode to set
     */
    public void setRollupStructureGroupCode(final String pRollupStructureGroupCode) {
        this.rollupStructureGroupCode = pRollupStructureGroupCode;
    }

    /**
     * Gets rollupStructureGroupCodeDescription
     *
     * @return the rollupStructureGroupCodeDescription
     */
    public String getRollupStructureGroupCodeDescription() {
        return rollupStructureGroupCodeDescription;
    }

    /**
     * Sets rollupStructureGroupCodeDescription
     *
     * @param pRollupStructureGroupCodeDescription the rollupStructureGroupCodeDescription to set
     */
    public void setRollupStructureGroupCodeDescription(final String pRollupStructureGroupCodeDescription) {
        this.rollupStructureGroupCodeDescription = pRollupStructureGroupCodeDescription;
    }

    /**
     * Gets structureGroupSearchInput
     *
     * @return the structureGroupSearchInput
     */
    public String getStructureGroupSearchInput() {
        return structureGroupSearchInput;
    }

    /**
     * Sets structureGroupSearchInput
     *
     * @param pStructureGroupSearchInput the structureGroupSearchInput to set
     */
    public void setStructureGroupSearchInput(final String pStructureGroupSearchInput) {
        this.structureGroupSearchInput = pStructureGroupSearchInput;
    }

    /**
     * Gets revisionCount
     *
     * @return the revisionCount
     */
    public Integer getRevisionCount() {
        return revisionCount;
    }

    /**
     * Sets revisionCount
     *
     * @param pRevisionCount the revisionCount to set
     */
    public void setRevisionCount(final Integer pRevisionCount) {
        revisionCount = pRevisionCount;
    }

    /**
     * Gets isNew
     *
     * @return the isNew
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Sets isNew
     *
     * @param pIsNew the isNew to set
     */
    public void setNew(final boolean pIsNew) {
        isNew = pIsNew;
    }

    /**
     * @return the isSetFilterContext
     */
    public boolean isSetFilterContext() {
        return isSetFilterContext;
    }

    /**
     * @param pIsNew the isSetFilterContext to set
     */
    public void setSetFilterContext(final boolean pIsNew) {
        this.isSetFilterContext = pIsNew;
    }

    /**
     * @return the codeFilter
     */
    public String getCodeFilter() {
        return codeFilter;
    }

    /**
     * @param codeFilter the codeFilter to set
     */
    public void setCodeFilter(final String codeFilter) {
        this.codeFilter = codeFilter;
    }

    /**
     * @return the descriptionFilter
     */
    public String getDescriptionFilter() {
        return descriptionFilter;
    }

    /**
     * @param descriptionFilter the descriptionFilter to set
     */
    public void setDescriptionFilter(final String descriptionFilter) {
        this.descriptionFilter = descriptionFilter;
    }

}
