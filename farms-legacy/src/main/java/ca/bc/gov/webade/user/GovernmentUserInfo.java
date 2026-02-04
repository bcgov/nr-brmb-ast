/**
 * @(#)GovernmentUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

/**
 * @author jross
 */
public interface GovernmentUserInfo extends WebADEUserInfo {

    /**
     * The reserved attribute name for the employee id attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String EMPLOYEE_ID = "government.user.employee.id";

    /**
     * The reserved attribute name for the city attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CITY = "government.user.city";
    
    /**
     * The reserved attribute name for the title attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String TITLE = "government.user.title";
    
    /**
     * The reserved attribute name for the company attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String COMPANY = "government.user.company";
    
    /**
     * The reserved attribute name for the organization code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String ORGANIZATION_CODE = "government.user.organization.code";
    
    /**
     * The reserved attribute name for the government department attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String GOVERNMENT_DEPARTMENT = "government.user.department";
    
    /**
     * The reserved attribute name for the office attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String OFFICE = "government.user.office";
    
    /**
     * The reserved attribute name for the description attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DESCRIPTION = "government.user.description";

    /**
     * @return The user's employee Id.
     */
    public String getEmployeeId();

    /**
     * @return True if the user is a government employee.
     */
    public boolean isEmployee();

    /**
     * @return The user's city.
     */
    public String getCity();
    
    /**
     * @return The user's title.
     */
    public String getTitle();
    
    /**
     * @return The user's company.
     */
    public String getCompany();
    
    /**
     * @return The user's organization code.
     */
    public String getOrganizationCode();
    
    /**
     * @return The user's government department.
     */
    public String getGovernmentDepartment();
    
    /**
     * @return The user's office.
     */
    public String getOffice();
    
    /**
     * @return The user's description.
     */
    public String getDescription();
}
