package ca.bc.gov.webade.developer.xml;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ca.bc.gov.webade.WildcardOptions;
import ca.bc.gov.webade.developer.WebADEDeveloperException;
import ca.bc.gov.webade.developer.security.PasswordService;
import ca.bc.gov.webade.user.AbstractSiteMinderUserInfo;
import ca.bc.gov.webade.user.AbstractWebADEUserInfo;
import ca.bc.gov.webade.user.DefaultBcServicesCardUserInfo;
import ca.bc.gov.webade.user.DefaultBusinessPartnerUserInfo;
import ca.bc.gov.webade.user.DefaultGovernmentUserInfo;
import ca.bc.gov.webade.user.DefaultVerifiedIndividualUserInfo;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.BusinessPartnerUserInfo;
import ca.bc.gov.webade.user.DefaultIndividualUserInfo;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.GovernmentUserInfo;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.BusinessPartnerUserSearchQuery;
import ca.bc.gov.webade.user.search.GovernmentUserSearchQuery;
import ca.bc.gov.webade.user.search.UserSearchQuery;
import ca.bc.gov.webade.user.security.enterprise.SiteminderConstants;

/**
 * Acts as a communicator to the xml file for the XML FileProvider class.
 * 
 * @author Vivid Solutions Inc
 */
public final class XMLFileBinding implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(XMLFileBinding.class);
    
    private static final long serialVersionUID = 7421136052514512295L;
    
    private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String GOVERNMENT_USER_INFO_TAG = "government-user-info";
    public static final String BUSINESS_PARTNER_USER_INFO_TAG = "business-partner-user-info";
    public static final String VERIFIED_INDIVIDUAL_USER_INFO_TAG = "verified-individual-user-info";
    public static final String INDIVIDUAL_USER_INFO_TAG = "individual-user-info";
    public static final String BC_SERVICES_CARD_USER_INFO_TAG = "bc-services-card-user-info";

    public static final String PASSWORD_ATTR = "password";
    public static final String EMPLOYEE_ID_ATTR = "employeeId";
    public static final String ACCOUNT_TYPE_ATTR = "accountType";
    public static final String GUID_ATTR = "guid";

    public static final String BUSINESS_ACTIVATION_CODE_ATTR = "businessActivationCode";
    public static final String BUSINESS_GUID_ATTR = "businessGUID";
    public static final String BUSINESS_LEGAL_NAME_ATTR = "businessLegalName";
    public static final String BUSINESS_TRADING_NAME_ATTR = "businessTradingName";
    public static final String BUSINESS_NUMBER_ATTR = "businessNumber";
    public static final String INCORPORATION_NUMBER_ATTR = "incorporationNumber";
    public static final String EXTRA_PROV_REGISTRATION_NUMBER_ATTR = "extraProvRegistrationNumber";
    public static final String BUSINESS_LUID_ATTR = "businessLUID";
    public static final String BUSINESS_TYPE_CODE_ATTR = "businessType";
    public static final String BUSINESS_TYPE_OTHER_ATTR = "businessTypeOther";
    public static final String BN_HUB_BUSINESS_TYPE_CODE_ATTR = "bnHubBusinessType";
    public static final String BUSINESS_NUMBER_VERIFIED_FLAG_ATTR = "businessNumberVerified";
    public static final String STATEMENT_OF_REGISTRATION_NUMBER_ATTR = "statementOfRegistrationNumber";
    public static final String JURISDICTION_OF_INCORPORATION_ATTR = "jurisdictionOfIncorporation";
    public static final String DOING_BUSINESS_AS_ATTR = "doingBusinessAs";
    public static final String IS_SUSPENDED_ATTR = "suspended";
    public static final String BUSINESS_ADDRESS_LINE_1_ATTR = "businessAddressLine1";
    public static final String BUSINESS_ADDRESS_LINE_2_ATTR = "businessAddressLine2";
    public static final String BUSINESS_ADDRESS_CITY_ATTR = "businessAddressCity";
    public static final String BUSINESS_ADDRESS_PROVINCE_ATTR = "businessAddressProvince";
    public static final String BUSINESS_ADDRESS_COUNTRY_ATTR = "businessAddressCountry";
    public static final String BUSINESS_ADDRESS_POSTAL_CODE_ATTR = "businessAddressPostalCode";
    public static final String BUSINESS_ADDRESS_UNSTRUCTURED_ATTR = "businessAddressUnstructured";

    public static final String DATE_OF_BIRTH_ATTR = "dateOfBirth";
    public static final String RESIDENTIAL_ADDRESS_LINE_1_ATTR = "residentialAddressLine1";
    public static final String RESIDENTIAL_ADDRESS_LINE_2_ATTR = "residentialAddressLine2";
    public static final String RESIDENTIAL_ADDRESS_CITY_ATTR = "residentialAddressCity";
    public static final String RESIDENTIAL_ADDRESS_PROVINCE_ATTR = "residentialAddressProvince";
    public static final String RESIDENTIAL_ADDRESS_COUNTRY_ATTR = "residentialAddressCountry";
    public static final String RESIDENTIAL_ADDRESS_POSTAL_CODE_ATTR = "residentialAddressPostalCode";
    public static final String RESIDENTIAL_ADDRESS_UNSTRUCTURED_ATTR = "residentialAddressUnstructured";
    public static final String MAILING_ADDRESS_LINE_1_ATTR = "mailingAddressLine1";
    public static final String MAILING_ADDRESS_LINE_2_ATTR = "mailingAddressLine2";
    public static final String MAILING_ADDRESS_CITY_ATTR = "mailingAddressCity";
    public static final String MAILING_ADDRESS_PROVINCE_ATTR = "mailingAddressProvince";
    public static final String MAILING_ADDRESS_COUNTRY_ATTR = "mailingAddressCountry";
    public static final String MAILING_ADDRESS_POSTAL_CODE_ATTR = "mailingAddressPostalCode";
    public static final String MAILING_ADDRESS_UNSTRUCTURED_ATTR = "mailingAddressUnstructured";

    public static final String VISIBLE_ATTR = "visible";
    public static final String DISPLAY_NAME_ATTR = "displayName";
    public static final String LAST_NAME_ATTR = "lastName";
    public static final String FIRST_NAME_ATTR = "firstName";
    public static final String MIDDLE_INITIAL_ATTR = "middleInitial";
    public static final String MIDDLE_NAME_ATTR = "middleName";
    public static final String OTHER_MIDDLE_ATTR = "otherMiddleName";
    public static final String INITIALS = "initials";
    public static final String EMAIL_ADDRESS_ATTR = "emailAddress";
    public static final String PHONE_NUMBER_ATTR = "phoneNumber";
    public static final String EXPIRY_DATE_ATTR = "expiryDate";
    public static final String PREFERRED_NAME_ATTR = "preferredName";
    public static final String CONTACT_PREFERENCE_TYPE_ATTR = "contactPreferenceType";
    public static final String DEPARTMENT_ATTR = "department";
    public static final String CONTACT_ADDRESS_LINE_1_ATTR = "contactAddressLine1";
    public static final String CONTACT_ADDRESS_LINE_2_ATTR = "contactAddressLine2";
    public static final String CONTACT_ADDRESS_CITY_ATTR = "contactAddressCity";
    public static final String CONTACT_ADDRESS_PROVINCE_ATTR = "contactAddressProvince";
    public static final String CONTACT_ADDRESS_COUNTRY_ATTR = "contactAddressCountry";
    public static final String CONTACT_ADDRESS_POSTAL_CODE_ATTR = "contactAddressPostalCode";
    public static final String CONTACT_ADDRESS_UNSTRUCTURED_ATTR = "contactAddressUnstructured";

    public static final String CITY_ATTR = "city";
    public static final String TITLE_ATTR = "title";
    public static final String COMPANY_ATTR = "company";
    public static final String ORGANIZATION_CODE_ATTR = "organizationCode";
    public static final String GOVERNMENT_DEPARTMENT_ATTR = "governmentDepartment";
    public static final String OFFICE_ATTR = "office";
    public static final String DESCRIPTION_ATTR = "description";

	private static final String SITE_MINDER_USER_TYPE_ATTR = "siteMinderUserType";
	private static final String SITE_MINDER_USER_IDENTIFIER_TYPE_ATTR = "siteMinderUserIdentifierType";
	private static final String SITE_MINDER_USER_IDENTIFIER_ATTR = "siteMinderUserIdentifier";
	private static final String SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER_ATTR = "siteMinderAuthoritativePartyIdentifier";
	private static final String SITE_MINDER_AUTHORITATIVE_PARTY_NAME_ATTR = "siteMinderAuthoritativePartyName";

	private static final String SEX_ATTR = "sex";
	private static final String TRANSACTION_IDENTIFIER_ATTR = "transactionIdentifier";
	private static final String IDENTITY_ASSURANCE_LEVEL_ATTR = "identityAssuranceLevel";


    private File file;
    private Document usersDocument;
    private ArrayList<WebADEUserInfo> usersList = new ArrayList<WebADEUserInfo>();

    /**
     * Constructor.
     * 
     * @param file The file containing the user information.
     * @throws WebADEDeveloperException
     */
    public XMLFileBinding(File file) throws WebADEDeveloperException {
        this.file = file;
        loadXml(file);
    }

    /**
     * Loads the xml from the current file into a DOM in memory.
     * 
     * @throws WebADEDeveloperException
     */
    public void loadXml() throws WebADEDeveloperException {
        loadXml(file);
    }

    /**
     * Loads the xml from the given file into a DOM in memory.
     * 
     * @param fileLocation
     * @throws WebADEDeveloperException
     */
    public void loadXml(String fileLocation) throws WebADEDeveloperException {
        loadXml(new File(fileLocation));
    }

    /**
     * Loads the xml from the given file into a DOM in memory.
     * 
     * @param f The file
     * @throws WebADEDeveloperException
     */
    public void loadXml(File f) throws WebADEDeveloperException {

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Loading User Info File " + f.getAbsolutePath());
            }
            this.file = f;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            usersDocument = builder.parse(f);
            setUsers(usersDocument);
        }
        catch (ParserConfigurationException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        catch (SAXException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        catch (IOException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }

    }

    public ArrayList<WebADEUserInfo> getUsers() {
        return usersList;
    }

    public void setUsers(Document usersDocument) throws WebADEDeveloperException {
        ArrayList<WebADEUserInfo> results = new ArrayList<WebADEUserInfo>();
        Element usersElement = usersDocument.getDocumentElement();
        NodeList govUserList = usersElement.getElementsByTagName(GOVERNMENT_USER_INFO_TAG);
        NodeList bupUserList = usersElement.getElementsByTagName(BUSINESS_PARTNER_USER_INFO_TAG);
        NodeList indUserList = usersElement.getElementsByTagName(INDIVIDUAL_USER_INFO_TAG);
        NodeList vinUserList = usersElement.getElementsByTagName(VERIFIED_INDIVIDUAL_USER_INFO_TAG);
        NodeList bcsUserList = usersElement.getElementsByTagName(BC_SERVICES_CARD_USER_INFO_TAG);
        
        if (logger.isDebugEnabled()) {
            logger.debug("GOV Users = " + govUserList.getLength());
            logger.debug("BUP Users = " + bupUserList.getLength());
            logger.debug("UIN Users = " + indUserList.getLength());
            logger.debug("VIN Users = " + vinUserList.getLength());
            logger.debug("BCS Users = " + bcsUserList.getLength());
        }
        if (govUserList.getLength() > 0) {
            String userType = UserTypeCode.GOVERNMENT.getCodeValue();
            for (int i = 0; i < govUserList.getLength(); i++) {
                UserCredentials credentials = new UserCredentials();
                setCredentialsInformation(credentials, (Element) govUserList.item(i));
                WebADEUserInfo webADEUserInfo = getUser(credentials, userType);
                if (webADEUserInfo != null) {
                    results.add(webADEUserInfo);
                }
            }
        }
        if (bupUserList.getLength() > 0) {
            String userType = UserTypeCode.BUSINESS_PARTNER.getCodeValue();
            for (int i = 0; i < bupUserList.getLength(); i++) {
                UserCredentials credentials = new UserCredentials();
                setCredentialsInformation(credentials, (Element) bupUserList.item(i));
                WebADEUserInfo webADEUserInfo = getUser(credentials, userType);
                if (webADEUserInfo != null) {
                    results.add(webADEUserInfo);
                }
            }
        }
        if (indUserList.getLength() > 0) {
            String userType = UserTypeCode.INDIVIDUAL.getCodeValue();
            for (int i = 0; i < indUserList.getLength(); i++) {
                UserCredentials credentials = new UserCredentials();
                setCredentialsInformation(credentials, (Element) indUserList.item(i));
                WebADEUserInfo webADEUserInfo = getUser(credentials, userType);
                if (webADEUserInfo != null) {
                    results.add(webADEUserInfo);
                }
            }
        }
        if (vinUserList.getLength() > 0) {
            String userType = UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue();
            for (int i = 0; i < vinUserList.getLength(); i++) {
                UserCredentials credentials = new UserCredentials();
                setCredentialsInformation(credentials, (Element) vinUserList.item(i));
                WebADEUserInfo webADEUserInfo = getUser(credentials, userType);
                if (webADEUserInfo != null) {
                    results.add(webADEUserInfo);
                }
            }
        }
        if (bcsUserList.getLength() > 0) {
            String userType = UserTypeCode.BC_SERVICES_CARD.getCodeValue();
            for (int i = 0; i < bcsUserList.getLength(); i++) {
                UserCredentials credentials = new UserCredentials();
                setCredentialsInformation(credentials, (Element) bcsUserList.item(i));
                WebADEUserInfo webADEUserInfo = getUser(credentials, userType);
                if (webADEUserInfo != null) {
                    results.add(webADEUserInfo);
                }
            }
        }
        usersList = results;
    }

    public boolean isValidPassword(UserCredentials credentials, String password) throws WebADEDeveloperException {
        return isValidPassword(credentials, password, false);
    }

    public boolean isValidPassword(UserCredentials credentials, String password, boolean encrypted)
            throws WebADEDeveloperException {
        boolean validPassword = false;
        Element element = getElement(credentials, credentials.getUserTypeCode().getCodeValue());
        if (element != null) {
            String passwordAttr = element.getAttribute(PASSWORD_ATTR);
            if (!element.hasAttribute(PASSWORD_ATTR)) {
                validPassword = true;
            }
            else {
                if (encrypted) {
                	
                    validPassword = PasswordService.compare(password, passwordAttr);
                }
                else {
                    validPassword = passwordAttr.equals(password);
                }
            }
        }
        return validPassword;
    }

    private WebADEUserInfo getUser(UserCredentials credentials, String userType) throws WebADEDeveloperException {
        return getUser(credentials, userType, null);
    }

    private WebADEUserInfo getUser(UserCredentials credentials, String userType, String requestorUserType)
            throws WebADEDeveloperException {
    	WebADEUserInfo user = null;
        Element element = getElement(credentials, userType);

        if (element != null) {
        	AbstractSiteMinderUserInfo webADEUserInfo = null;
        	
            if (UserTypeCode.BC_SERVICES_CARD.getCodeValue().equals(userType)) {
                webADEUserInfo = new DefaultBcServicesCardUserInfo();
                webADEUserInfo.setUserCredentials(credentials);
                populateBcServicesCardUserInfo(element, (DefaultBcServicesCardUserInfo) webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
            }        	
            else if (UserTypeCode.GOVERNMENT.getCodeValue().equals(userType)) {
                webADEUserInfo = new DefaultGovernmentUserInfo();
                webADEUserInfo.setUserCredentials(credentials);
                populateGovernmentUserInfo(element, (DefaultGovernmentUserInfo) webADEUserInfo);
                populateIndividualUserInfo(element, (DefaultIndividualUserInfo) webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
                if (requestorUserType != null) {
                    if (webADEUserInfo.isVisible()) {
                        if (!UserTypeCode.GOVERNMENT.getCodeValue().equals(requestorUserType)) {
                            webADEUserInfo.setVisible(false);
                        }
                    }
                }
            }
            else if (UserTypeCode.BUSINESS_PARTNER.getCodeValue().equals(userType)) {
                webADEUserInfo = new DefaultBusinessPartnerUserInfo();
                webADEUserInfo.setUserCredentials(credentials);
                populateBusinessPartnerUserInfo(element, (DefaultBusinessPartnerUserInfo) webADEUserInfo);
                populateIndividualUserInfo(element, (DefaultIndividualUserInfo) webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
            }            
            else if (UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue().equals(userType)) {
                webADEUserInfo = new DefaultVerifiedIndividualUserInfo();
                webADEUserInfo.setUserCredentials(credentials);
                populateVerifiedIndividualUserInfo(element, (DefaultVerifiedIndividualUserInfo) webADEUserInfo);
                populateIndividualUserInfo(element, (DefaultIndividualUserInfo) webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
            }           
            else { // UserTypeCode.INDIVIDUAL.getCodeValue().equals(userType)
                webADEUserInfo = new DefaultIndividualUserInfo();
                webADEUserInfo.setUserCredentials(credentials);
                populateIndividualUserInfo(element, (DefaultIndividualUserInfo) webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
            }
            user = webADEUserInfo;
            if (logger.isInfoEnabled()) {
                logger.debug("Found user = " + user);
            }
        }
        return user;
    }

    private Element getElement(UserCredentials credentials, String userType) throws WebADEDeveloperException {
        Element user = null;
        try {
            Element users = usersDocument.getDocumentElement();
            NodeList userList;
            String xPathExpression = null;

            if (UserTypeCode.GOVERNMENT.getCodeValue().equals(userType)) {
                xPathExpression = GOVERNMENT_USER_INFO_TAG;
            }
            else if (UserTypeCode.BUSINESS_PARTNER.getCodeValue().equals(userType)) {
                xPathExpression = BUSINESS_PARTNER_USER_INFO_TAG;
            }
            else if (UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue().equals(userType)) {
                xPathExpression = VERIFIED_INDIVIDUAL_USER_INFO_TAG;
            }
            else if (UserTypeCode.INDIVIDUAL.getCodeValue().equals(userType)) {
                xPathExpression = INDIVIDUAL_USER_INFO_TAG;
            }
            else if (UserTypeCode.BC_SERVICES_CARD.getCodeValue().equals(userType)) {
                xPathExpression = BC_SERVICES_CARD_USER_INFO_TAG;
            }
            else { 
                throw new IllegalArgumentException("Unsupported user type: "+userType);
            }

            GUID guid = credentials.getUserGuid();
            if (logger.isDebugEnabled()) {
                logger.debug("GUID guid = " + guid);
            }
            if (guid != null) {
                String guidString = guid.toMicrosoftGUIDString();
                // xPathExpression = "...[@guid="AB12345"]";
                xPathExpression += "[@guid=\"" + guidString + "\"]";
                if (logger.isDebugEnabled()) {
                    logger.debug("xPathExpression = " + xPathExpression);
                }
                userList = XPathAPI.selectNodeList(users, xPathExpression);
                if (logger.isDebugEnabled()) {
                    logger.debug("userList = " + userList);
                }
            }
            else {
                String accountName = credentials.getAccountName();
                String sourceDirectory = credentials.getSourceDirectory();
                // We failed to find a GUID match, try accountName and
                // sourceDirectory
                // xPathExpression = "...[@accountName="CHBOREEN" and
                // @sourceDirectory="IDIR"]";

                String xPathCondition = null;

                if (accountName != null) {
                    xPathCondition = "@accountName=\"" + accountName + "\"";
                }

                if (xPathCondition != null && sourceDirectory != null) {
                    xPathCondition += " and @sourceDirectory=\"" + sourceDirectory + "\"";
                }
                else if (sourceDirectory != null) {
                    xPathCondition = "@sourceDirectory=\"" + sourceDirectory + "\"";
                }

                xPathExpression = xPathExpression + "[" + xPathCondition + "]";

                if (logger.isDebugEnabled()) {
                    logger.debug("xPathExpression = " + xPathExpression);
                }
                userList = XPathAPI.selectNodeList(users, xPathExpression);
                if (logger.isDebugEnabled()) {
                    logger.debug("userList size = " + userList.getLength());
                }
            }
            if (userList.getLength() == 0) { // We failed to find a match
                if (logger.isInfoEnabled()) {
                    logger.warn("No match found for " + xPathExpression + " in " + file.getAbsolutePath());
                }
                return null;
            }
            else if (userList.getLength() > 1) {
                logger.error("Multiple users found matching criteria " + credentials);
                throw new WebADEDeveloperException("Multiple users found matching criteria " + credentials);
            }
            else if (userList.getLength() == 1) {
                // we've found our user
                user = (Element) userList.item(0);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found user element: " + user.getNodeName());
                }
            }
        }
        catch (TransformerException e) {
            logger.error(e.getMessage(), e);
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        return user;
    }

    private static void populateGovernmentUserInfo(Element element, DefaultGovernmentUserInfo webADEUserInfo) {
        String employeeIdString = element.getAttribute(EMPLOYEE_ID_ATTR);
        if ("".equals(employeeIdString)) {
            webADEUserInfo.setEmployeeId(null);
        } else {
            webADEUserInfo.setEmployeeId(employeeIdString);
        }
        webADEUserInfo.setCity(element.getAttribute(CITY_ATTR));
        webADEUserInfo.setTitle(element.getAttribute(TITLE_ATTR));
        webADEUserInfo.setCompany(element.getAttribute(COMPANY_ATTR));
        webADEUserInfo.setOrganizationCode(element.getAttribute(ORGANIZATION_CODE_ATTR));
        webADEUserInfo.setGovernmentDepartment(element.getAttribute(GOVERNMENT_DEPARTMENT_ATTR));
        webADEUserInfo.setOffice(element.getAttribute(OFFICE_ATTR));
        webADEUserInfo.setDescription(element.getAttribute(DESCRIPTION_ATTR));
    }

    private static void populateBusinessPartnerUserInfo(Element element, DefaultBusinessPartnerUserInfo webADEUserInfo) {
        webADEUserInfo.setBusinessActivationCode(element.getAttribute(BUSINESS_ACTIVATION_CODE_ATTR));
        webADEUserInfo.setBusinessGUID(new GUID(element.getAttribute(BUSINESS_GUID_ATTR)));
        webADEUserInfo.setBusinessLegalName(element.getAttribute(BUSINESS_LEGAL_NAME_ATTR));
        webADEUserInfo.setBusinessTradingName(element.getAttribute(BUSINESS_TRADING_NAME_ATTR));
        webADEUserInfo.setBusinessNumber(element.getAttribute(BUSINESS_NUMBER_ATTR));
        webADEUserInfo.setIncorporationNumber(element.getAttribute(INCORPORATION_NUMBER_ATTR));
        webADEUserInfo.setExtraProvincialRegistrationNumber(element.getAttribute(EXTRA_PROV_REGISTRATION_NUMBER_ATTR));
        webADEUserInfo.setBusinessLUID(element.getAttribute(BUSINESS_LUID_ATTR));
        webADEUserInfo.setBusinessTypeCode(element.getAttribute(BUSINESS_TYPE_CODE_ATTR));
        webADEUserInfo.setBusinessTypeOther(element.getAttribute(BUSINESS_TYPE_OTHER_ATTR));
        webADEUserInfo.setBnHubBusinessTypeCode(element.getAttribute(BN_HUB_BUSINESS_TYPE_CODE_ATTR));
        webADEUserInfo.setStatementOfRegistrationNumber(element.getAttribute(STATEMENT_OF_REGISTRATION_NUMBER_ATTR));
        webADEUserInfo.setJurisdictionOfIncorporation(element.getAttribute(JURISDICTION_OF_INCORPORATION_ATTR));
        webADEUserInfo.setDoingBusinessAs(element.getAttribute(DOING_BUSINESS_AS_ATTR));
        webADEUserInfo.setBusinessAddressLine1(element.getAttribute(BUSINESS_ADDRESS_LINE_1_ATTR));
        webADEUserInfo.setBusinessAddressLine2(element.getAttribute(BUSINESS_ADDRESS_LINE_2_ATTR));
        webADEUserInfo.setBusinessAddressCity(element.getAttribute(BUSINESS_ADDRESS_CITY_ATTR));
        webADEUserInfo.setBusinessAddressProvince(element.getAttribute(BUSINESS_ADDRESS_PROVINCE_ATTR));
        webADEUserInfo.setBusinessAddressCountry(element.getAttribute(BUSINESS_ADDRESS_COUNTRY_ATTR));
        webADEUserInfo.setBusinessAddressPostalCode(element.getAttribute(BUSINESS_ADDRESS_POSTAL_CODE_ATTR));
        webADEUserInfo.setBusinessAddressUnstructured(element.getAttribute(BUSINESS_ADDRESS_UNSTRUCTURED_ATTR));

        String businessNumberVerifiedString = element.getAttribute(BUSINESS_NUMBER_VERIFIED_FLAG_ATTR);
        boolean businessNumberVerified = Boolean.valueOf(businessNumberVerifiedString).booleanValue();
        webADEUserInfo.setBusinessNumberVerified(businessNumberVerified);

        String suspendedString = element.getAttribute(IS_SUSPENDED_ATTR);
        boolean suspended = Boolean.valueOf(suspendedString).booleanValue();
        webADEUserInfo.setSuspended(suspended);
    }
    
    private void populateVerifiedIndividualUserInfo(Element element, DefaultVerifiedIndividualUserInfo webADEUserInfo) {
    	// do nothing
    }
    
    private static void populateBcServicesCardUserInfo(Element element, DefaultBcServicesCardUserInfo webADEUserInfo) throws WebADEDeveloperException {
    	 
        String dateOfBirthString = element.getAttribute(DATE_OF_BIRTH_ATTR);
        Date dateOfBirth = null;
        if (dateOfBirthString != null && dateOfBirthString.trim().length() > 0) {
            try {
                DateFormat formatter = null;
                if(dateOfBirthString.length()>10) {
                	formatter = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
                } else {
                	formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
                }
                dateOfBirth = formatter.parse(dateOfBirthString);
            }
            catch (ParseException e) {
                throw new WebADEDeveloperException(e.getMessage(), e);
            }
        }    	
    	
    	 webADEUserInfo.setDateOfBirth(dateOfBirth);
    	 webADEUserInfo.setSex(element.getAttribute(SEX_ATTR));
    	 webADEUserInfo.setTransactionIdentifier(element.getAttribute(TRANSACTION_IDENTIFIER_ATTR));
    	 webADEUserInfo.setIdentityAssuranceLevel(element.getAttribute(IDENTITY_ASSURANCE_LEVEL_ATTR));
    }

    /**
     * Sets the credentials information if it has not already been set using the
     * values from the user element.
     */
    private static void setCredentialsInformation(UserCredentials credentials, Element element) {
        if (credentials.getAccountName() == null) {
            String accountName = element.getAttribute("accountName");
            if (logger.isDebugEnabled()) {
                logger.debug("Setting account name = " + accountName);
            }
            credentials.setAccountName(accountName);
        }
        if (credentials.getUserGuid() == null) {
            String guidString = element.getAttribute(GUID_ATTR);
            if(guidString==null||guidString.trim().length()==0) {
            	guidString = element.getAttribute(SITE_MINDER_USER_IDENTIFIER_ATTR);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Setting guid = " + guidString);
            }
            credentials.setUserGuid((guidString == null) ? null : new GUID(guidString));
        }
        if (credentials.getSourceDirectory() == null) {
            String sourceDirectory = element.getAttribute("sourceDirectory");
            if (logger.isDebugEnabled()) {
                logger.debug("Setting source directory = " + sourceDirectory);
            }
            credentials.setSourceDirectory(sourceDirectory);
        }
        if (credentials.getUserTypeCode() == null) {
            String userType = element.getAttribute("userType");
            if (logger.isDebugEnabled()) {
                logger.debug("Setting userType = " + userType);
            }
            credentials.setUserTypeCode(UserTypeCode.getUserTypeCode(userType));
        }
    }

    private static void populateIndividualUserInfo(Element element, DefaultIndividualUserInfo webADEUserInfo) throws WebADEDeveloperException {

        String dateOfBirthString = element.getAttribute(DATE_OF_BIRTH_ATTR);
        Date dateOfBirth = null;
        if (dateOfBirthString != null && dateOfBirthString.trim().length() > 0) {
            try {
                DateFormat formatter = null;
                if(dateOfBirthString.length()>10) {
                	formatter = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
                } else {
                	formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
                }
                dateOfBirth = formatter.parse(dateOfBirthString);
            }
            catch (ParseException e) {
                throw new WebADEDeveloperException(e.getMessage(), e);
            }
        }
        webADEUserInfo.setDateOfBirth(dateOfBirth);
        webADEUserInfo.setResidentialAddressLine1(element.getAttribute(RESIDENTIAL_ADDRESS_LINE_1_ATTR));
        webADEUserInfo.setResidentialAddressLine2(element.getAttribute(RESIDENTIAL_ADDRESS_LINE_2_ATTR));
        webADEUserInfo.setResidentialAddressCity(element.getAttribute(RESIDENTIAL_ADDRESS_CITY_ATTR));
        webADEUserInfo.setResidentialAddressProvince(element.getAttribute(RESIDENTIAL_ADDRESS_PROVINCE_ATTR));
        webADEUserInfo.setResidentialAddressCountry(element.getAttribute(RESIDENTIAL_ADDRESS_COUNTRY_ATTR));
        webADEUserInfo.setResidentialAddressPostalCode(element.getAttribute(RESIDENTIAL_ADDRESS_POSTAL_CODE_ATTR));
        webADEUserInfo.setResidentialAddressUnstructured(element.getAttribute(RESIDENTIAL_ADDRESS_UNSTRUCTURED_ATTR));
        webADEUserInfo.setMailingAddressLine1(element.getAttribute(MAILING_ADDRESS_LINE_1_ATTR));
        webADEUserInfo.setMailingAddressLine2(element.getAttribute(MAILING_ADDRESS_LINE_2_ATTR));
        webADEUserInfo.setMailingAddressCity(element.getAttribute(MAILING_ADDRESS_CITY_ATTR));
        webADEUserInfo.setMailingAddressProvince(element.getAttribute(MAILING_ADDRESS_PROVINCE_ATTR));
        webADEUserInfo.setMailingAddressCountry(element.getAttribute(MAILING_ADDRESS_COUNTRY_ATTR));
        webADEUserInfo.setMailingAddressPostalCode(element.getAttribute(MAILING_ADDRESS_POSTAL_CODE_ATTR));
        webADEUserInfo.setMailingAddressUnstructured(element.getAttribute(MAILING_ADDRESS_UNSTRUCTURED_ATTR));
    }

    private static void populateSiteMinderUserInfo(Element element, AbstractSiteMinderUserInfo webADEUserInfo) {

        if (webADEUserInfo.getSiteMinderUserType() == null) {
        	String siteMinderUserType = element.getAttribute(SITE_MINDER_USER_TYPE_ATTR);
			if(siteMinderUserType==null||siteMinderUserType.trim().length()==0) {
				if(webADEUserInfo instanceof DefaultGovernmentUserInfo) {
					siteMinderUserType = SiteminderConstants.INTERNAL_USERTYPE;
				} else if(webADEUserInfo instanceof DefaultBusinessPartnerUserInfo) {
					siteMinderUserType = SiteminderConstants.BUSINESS_USERTYPE;
				} else if(webADEUserInfo instanceof DefaultVerifiedIndividualUserInfo) {
					siteMinderUserType = SiteminderConstants.VERIFIED_INDIVIDUAL_USERTYPE;
				} else if(webADEUserInfo instanceof DefaultIndividualUserInfo) {
					siteMinderUserType = SiteminderConstants.INDIVIDUAL_USERTYPE;
				} else if(webADEUserInfo instanceof DefaultBcServicesCardUserInfo) {
					siteMinderUserType = SiteminderConstants.VERIFIED_INDIVIDUAL_USERTYPE;
				} else {
					throw new IllegalArgumentException("Unsupported WebADEUserInfo type: "+webADEUserInfo.getClass());
				}
			}
            webADEUserInfo.setSiteMinderUserType(siteMinderUserType);
        }
        if (webADEUserInfo.getSiteMinderUserIdentiferType() == null) {
        	String siteMinderUserIdentifier = element.getAttribute(SITE_MINDER_USER_IDENTIFIER_TYPE_ATTR);
        	if(siteMinderUserIdentifier==null||siteMinderUserIdentifier.trim().length()==0) {
        		siteMinderUserIdentifier = "GUID";
        	}
            webADEUserInfo.setSiteMinderUserIdentiferType(siteMinderUserIdentifier);
        }
        if (webADEUserInfo.getSiteMinderUserIdentifer() == null) {
        	String siteMinderUserIdentifier = element.getAttribute(SITE_MINDER_USER_IDENTIFIER_ATTR);
        	if(siteMinderUserIdentifier==null||siteMinderUserIdentifier.trim().length()==0) {
        		siteMinderUserIdentifier = element.getAttribute(GUID_ATTR);
        	}
            webADEUserInfo.setSiteMinderUserIdentifer(siteMinderUserIdentifier);
        }
        if (webADEUserInfo.getSiteMinderAuthoritativePartyName() == null) {
            webADEUserInfo.setSiteMinderAuthoritativePartyName(element.getAttribute(SITE_MINDER_AUTHORITATIVE_PARTY_NAME_ATTR));
        }
        if (webADEUserInfo.getSiteMinderAuthoritativePartyIdentifier() == null) {
            webADEUserInfo.setSiteMinderAuthoritativePartyIdentifier(element.getAttribute(SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER_ATTR));
        }
    }

    private static void populateWebadeUserInfo(Element element, AbstractWebADEUserInfo webADEUserInfo) {
        // Set credentials
        UserCredentials credentials = webADEUserInfo.getUserCredentials();
        setCredentialsInformation(credentials, element);

        // Set info
        webADEUserInfo.setVisible(true);
        String visible = element.getAttribute(VISIBLE_ATTR);
        if (visible != null
                && (visible.equalsIgnoreCase("false") || visible.equalsIgnoreCase("F") || visible.equalsIgnoreCase("N"))) {
            webADEUserInfo.setVisible(false);
        }
        if (webADEUserInfo.getDisplayName() == null) {
            webADEUserInfo.setDisplayName(element.getAttribute(DISPLAY_NAME_ATTR));
        }
        if (webADEUserInfo.getLastName() == null) {
            webADEUserInfo.setLastName(element.getAttribute(LAST_NAME_ATTR));
        }
        if (webADEUserInfo.getFirstName() == null) {
            webADEUserInfo.setFirstName(element.getAttribute(FIRST_NAME_ATTR));
        }
        if (webADEUserInfo.getMiddleInitial() == null) {
            webADEUserInfo.setMiddleInitial(element.getAttribute(MIDDLE_INITIAL_ATTR));
        }
        if (webADEUserInfo.getMiddleName() == null) {
            webADEUserInfo.setMiddleName(element.getAttribute(MIDDLE_NAME_ATTR));
        }
        if (webADEUserInfo.getOtherMiddleName() == null) {
            webADEUserInfo.setOtherMiddleName(element.getAttribute(OTHER_MIDDLE_ATTR));
        }
        if (webADEUserInfo.getInitials() == null) {
            webADEUserInfo.setInitials(element.getAttribute(INITIALS));
        }
        if (webADEUserInfo.getEmailAddress() == null) {
            webADEUserInfo.setEmailAddress(element.getAttribute(EMAIL_ADDRESS_ATTR));
        }
        if (webADEUserInfo.getPhoneNumber() == null) {
            webADEUserInfo.setPhoneNumber(element.getAttribute(PHONE_NUMBER_ATTR));
        }
        if (webADEUserInfo.getPreferredName() == null) {
            webADEUserInfo.setPreferredName(element.getAttribute(PREFERRED_NAME_ATTR));
        }
        if (webADEUserInfo.getContactPreferenceType() == null) {
            webADEUserInfo.setContactPreferenceType(element.getAttribute(CONTACT_PREFERENCE_TYPE_ATTR));
        }
        if (webADEUserInfo.getDepartment() == null) {
            webADEUserInfo.setDepartment(element.getAttribute(DEPARTMENT_ATTR));
        }
        if (webADEUserInfo.getContactAddressLine1() == null) {
            webADEUserInfo.setContactAddressLine1(element.getAttribute(CONTACT_ADDRESS_LINE_1_ATTR));
        }
        if (webADEUserInfo.getContactAddressLine2() == null) {
            webADEUserInfo.setContactAddressLine2(element.getAttribute(CONTACT_ADDRESS_LINE_2_ATTR));
        }
        if (webADEUserInfo.getContactAddressCity() == null) {
            webADEUserInfo.setContactAddressCity(element.getAttribute(CONTACT_ADDRESS_CITY_ATTR));
        }
        if (webADEUserInfo.getContactAddressProvince() == null) {
            webADEUserInfo.setContactAddressProvince(element.getAttribute(CONTACT_ADDRESS_PROVINCE_ATTR));
        }
        if (webADEUserInfo.getContactAddressCountry() == null) {
            webADEUserInfo.setContactAddressCountry(element.getAttribute(CONTACT_ADDRESS_COUNTRY_ATTR));
        }
        if (webADEUserInfo.getContactAddressPostalCode() == null) {
            webADEUserInfo.setContactAddressPostalCode(element.getAttribute(CONTACT_ADDRESS_POSTAL_CODE_ATTR));
        }
        if (webADEUserInfo.getContactAddressUnstructured() == null) {
            webADEUserInfo.setContactAddressUnstructured(element.getAttribute(CONTACT_ADDRESS_UNSTRUCTURED_ATTR));
        }
    }

    /**
     * Retrieves the user information for the given bc services card user.
     * 
     * @param credentials The given bc services card user.
     * @return The user information.
     * @throws WebADEDeveloperException
     */
    public WebADEUserInfo getBcServicesCardUser(UserCredentials credentials) throws WebADEDeveloperException {
        String userType = UserTypeCode.BC_SERVICES_CARD.getCodeValue();
        return getUser(credentials, userType);
    }

    /**
     * Retrieves the user information for the given individual user.
     * 
     * @param credentials The given individual user.
     * @return The user information.
     * @throws WebADEDeveloperException
     */
    public WebADEUserInfo getVerifiedIndividualUser(UserCredentials credentials) throws WebADEDeveloperException {
        String userType = UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue();
        return getUser(credentials, userType);
    }

    /**
     * Retrives the user information for the given individual user.
     * 
     * @param credentials The given individual user.
     * @return The user information.
     * @throws WebADEDeveloperException
     */
    public WebADEUserInfo getIndividualUser(UserCredentials credentials) throws WebADEDeveloperException {
        String userType = UserTypeCode.INDIVIDUAL.getCodeValue();
        return getUser(credentials, userType);
    }

    /**
     * Retrives the user information for the given government user.
     * 
     * @param credentials The given government user.
     * @return The user information.
     * @throws WebADEDeveloperException
     */
    public WebADEUserInfo getGovernmentUser(UserCredentials credentials, String requestorUserType)
            throws WebADEDeveloperException {
        String userType = UserTypeCode.GOVERNMENT.getCodeValue();
        return getUser(credentials, userType, requestorUserType);
    }

    /**
     * Retrives the user information for the given business partner user.
     * 
     * @param credentials The given business partner user.
     * @return The user information.
     * @throws WebADEDeveloperException
     */
    public WebADEUserInfo getBusinessPartnerUser(UserCredentials credentials) throws WebADEDeveloperException {
        String userType = UserTypeCode.BUSINESS_PARTNER.getCodeValue();
        return getUser(credentials, userType);
    }

    /**
     * Checks if the searchFor is in the searchIn string at the location
     * designated by wildcardOption
     * 
     * @param searchFor String to search for
     * @param searchIn String to search in
     * @param wildcardOption location designation eg. EXACT_MATCH,
     *        WILDCARD_BOTH, WILDCARD_RIGHT
     * @return if the string was found at the location designated by
     *         wildcardOption
     */
    private static boolean isMatch(String searchFor, String searchIn, int wildcardOption) {
        boolean match = false;
        if (searchIn != null) {
            if (wildcardOption == WildcardOptions.EXACT_MATCH) {
                match = searchIn.equalsIgnoreCase(searchFor);
            }
            else if (wildcardOption == WildcardOptions.WILDCARD_BOTH) {
                match = searchIn.toUpperCase().indexOf(searchFor.toUpperCase()) > 0;
            }
            else if (wildcardOption == WildcardOptions.WILDCARD_RIGHT) {
                match = searchIn.toUpperCase().startsWith(searchFor.toUpperCase());
            }
            else if (wildcardOption == WildcardOptions.WILDCARD_LEFT) {
                match = searchIn.toUpperCase().endsWith(searchFor.toUpperCase());
            }
        }
        return match;
    }

    private static boolean isSearchUserMatch(WebADEUserInfo webADEUserInfo, UserSearchQuery userSearchQuery) {
        boolean result = true;

        String accountName = userSearchQuery.getUserId().getSearchValue();
        String userFirstName = userSearchQuery.getFirstName().getSearchValue();
        String userLastName = userSearchQuery.getLastName().getSearchValue();

        // match userId
        if (accountName != null && !accountName.equals("")) {
            if (!isMatch(accountName, webADEUserInfo.getUserCredentials().getAccountName(), userSearchQuery.getUserId()
                    .getWildcardOption())) {
                result = false;
            }
        }
        if (userFirstName != null) {
            if (!isMatch(userFirstName, webADEUserInfo.getFirstName(), userSearchQuery.getFirstName().getWildcardOption())) {
                result = false;
            }
        }
        if (userLastName != null) {
            if (!isMatch(userLastName, webADEUserInfo.getLastName(), userSearchQuery.getLastName().getWildcardOption())) {
                result = false;
            }
        }

        if (userSearchQuery instanceof GovernmentUserSearchQuery) {
            GovernmentUserSearchQuery governmentUserSearchQuery = (GovernmentUserSearchQuery) userSearchQuery;
            GovernmentUserInfo GovernmentUserInfo = (GovernmentUserInfo) webADEUserInfo;
            String middleInitial = governmentUserSearchQuery.getMiddleInitial().getSearchValue();
            String emailAddress = governmentUserSearchQuery.getEmailAddress().getSearchValue();
            String phoneNumber = governmentUserSearchQuery.getPhoneNumber().getSearchValue();
            String city = governmentUserSearchQuery.getCity().getSearchValue();
            if (middleInitial != null) {
                if (!isMatch(middleInitial, GovernmentUserInfo.getMiddleInitial(), governmentUserSearchQuery
                        .getMiddleInitial().getWildcardOption())) {
                    result = false;
                }
            }
            if (emailAddress != null) {
                if (!isMatch(emailAddress, GovernmentUserInfo.getEmailAddress(), governmentUserSearchQuery
                        .getEmailAddress().getWildcardOption())) {
                    result = false;
                }
            }
            if (phoneNumber != null) {
                if (!isMatch(phoneNumber, GovernmentUserInfo.getPhoneNumber(), governmentUserSearchQuery
                        .getPhoneNumber().getWildcardOption())) {
                    result = false;
                }
            }
            if (city != null) {
                if (!isMatch(city, GovernmentUserInfo.getCity(), governmentUserSearchQuery.getCity()
                        .getWildcardOption())) {
                    result = false;
                }
            }
        }
        else if (userSearchQuery instanceof BusinessPartnerUserSearchQuery) {
            BusinessPartnerUserSearchQuery businessPartnerUserSearchQuery = (BusinessPartnerUserSearchQuery) userSearchQuery;
            BusinessPartnerUserInfo businessPartnerUserInfo = (BusinessPartnerUserInfo) webADEUserInfo;
            String middleInitial = businessPartnerUserSearchQuery.getMiddleInitial().getSearchValue();
            String emailAddress = businessPartnerUserSearchQuery.getEmailAddress().getSearchValue();
            String phoneNumber = businessPartnerUserSearchQuery.getPhoneNumber().getSearchValue();
            String businessGUID = businessPartnerUserSearchQuery.getBusinessGUID().getSearchValue();
            if (middleInitial != null) {
                if (!isMatch(middleInitial, businessPartnerUserInfo.getMiddleInitial(), businessPartnerUserSearchQuery
                        .getMiddleInitial().getWildcardOption())) {
                    result = false;
                }
            }
            if (emailAddress != null) {
                if (!isMatch(emailAddress, businessPartnerUserInfo.getEmailAddress(), businessPartnerUserSearchQuery
                        .getEmailAddress().getWildcardOption())) {
                    result = false;
                }
            }
            if (phoneNumber != null) {
                if (!isMatch(phoneNumber, businessPartnerUserInfo.getPhoneNumber(), businessPartnerUserSearchQuery
                        .getPhoneNumber().getWildcardOption())) {
                    result = false;
                }
            }
            if (businessGUID != null) {
                if (!isMatch(businessGUID, businessPartnerUserInfo.getBusinessGUID().toMicrosoftGUIDString(),
                        businessPartnerUserSearchQuery.getBusinessGUID().getWildcardOption())) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Searches the xml for government users matching the given search criteria.
     * 
     * @param userSearchQuery The UserSearchObject holding the search criteria.
     * @return A list of government users matching the search criteria.
     * @throws WebADEDeveloperException
     */
    public List<WebADEUserInfo> findGovernmentUsers(UserSearchQuery userSearchQuery) throws WebADEDeveloperException {
        if (logger.isDebugEnabled()) {
            logger.debug("findGovernmentUsers: UserSearchQuery " + userSearchQuery);
        }
        List<WebADEUserInfo> results = new ArrayList<WebADEUserInfo>();
        try {
            Element users = usersDocument.getDocumentElement();
            String xPathExpression = GOVERNMENT_USER_INFO_TAG;
            NodeList userList = XPathAPI.selectNodeList(users, xPathExpression);
            // we've found our users
            for (int i = 0; i < userList.getLength(); i++) {
                Element element = (Element) userList.item(i);
                DefaultGovernmentUserInfo webADEUserInfo = new DefaultGovernmentUserInfo();
                populateGovernmentUserInfo(element, webADEUserInfo);
                populateIndividualUserInfo(element, webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
                if (isSearchUserMatch(webADEUserInfo, userSearchQuery)) {
                    results.add(webADEUserInfo);
                }
            }
        }
        catch (TransformerException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found Government Users:");
            Iterator<WebADEUserInfo> i = results.iterator();
            while (i.hasNext()) {
                sb.append(" ");
                sb.append(i.next().getUserCredentials());
            }
            logger.debug(sb.toString());
        }
        return results;
    }

    /**
     * Searches the xml for Business Partner users matching the given search
     * criteria.
     * 
     * @param userSearchQuery The UserSearchObject holding the search criteria.
     * @return A list of Business Partner users matching the search criteria.
     * @throws WebADEDeveloperException
     */
    public List<WebADEUserInfo> findBusinessPartnerUsers(UserSearchQuery userSearchQuery) throws WebADEDeveloperException {
        if (logger.isDebugEnabled()) {
            logger.debug("findBusinessPartnerUsers: UserSearchQuery " + userSearchQuery);
        }
        List<WebADEUserInfo> results = new ArrayList<WebADEUserInfo>();
        try {
            Element users = usersDocument.getDocumentElement();
            String xPathExpression = BUSINESS_PARTNER_USER_INFO_TAG;
            NodeList userList = XPathAPI.selectNodeList(users, xPathExpression);
            // we've found our users
            for (int i = 0; i < userList.getLength(); i++) {
                Element element = (Element) userList.item(i);
                DefaultBusinessPartnerUserInfo webADEUserInfo = new DefaultBusinessPartnerUserInfo();
                populateBusinessPartnerUserInfo(element, webADEUserInfo);
                populateIndividualUserInfo(element, webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
                if (isSearchUserMatch(webADEUserInfo, userSearchQuery)) {
                    results.add(webADEUserInfo);
                }
            }
        }
        catch (TransformerException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found Business Partner Users:");
            Iterator<WebADEUserInfo> i = results.iterator();
            while (i.hasNext()) {
                sb.append(" ");
                sb.append(i.next().getUserCredentials());
            }
            logger.debug(sb.toString());
        }
        return results;
    }

    /**
     * Searches the xml for Verified Individual users matching the given search
     * criteria.
     * 
     * @param userSearchQuery The UserSearchObject holding the search criteria.
     * @return A list of Verified Individual users matching the search criteria.
     * @throws WebADEDeveloperException
     */
    public List<WebADEUserInfo> findVerifiedIndividualUsers(UserSearchQuery userSearchQuery) throws WebADEDeveloperException {
        if (logger.isDebugEnabled()) {
            logger.debug("findVerifiedIndividualUsers: UserSearchQuery " + userSearchQuery);
        }
        List<WebADEUserInfo> results = new ArrayList<WebADEUserInfo>();
        try {
            Element users = usersDocument.getDocumentElement();
            String xPathExpression = VERIFIED_INDIVIDUAL_USER_INFO_TAG;
            NodeList userList = XPathAPI.selectNodeList(users, xPathExpression);
            // we've found our users
            for (int i = 0; i < userList.getLength(); i++) {
                Element element = (Element) userList.item(i);
                DefaultVerifiedIndividualUserInfo webADEUserInfo = new DefaultVerifiedIndividualUserInfo();
                populateIndividualUserInfo(element, webADEUserInfo);
                populateSiteMinderUserInfo(element, webADEUserInfo);
                populateWebadeUserInfo(element, webADEUserInfo);
                if (isSearchUserMatch(webADEUserInfo, userSearchQuery)) {
                    results.add(webADEUserInfo);
                }
            }
        }
        catch (TransformerException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found Verified Individual Users:");
            Iterator<WebADEUserInfo> i = results.iterator();
            while (i.hasNext()) {
                sb.append(" ");
                sb.append(i.next().getUserCredentials());
            }
            logger.debug(sb.toString());
        }
        return results;
    }

    /**
     * Retrieves an array of groups that the given user is a member of.
     * 
     * @param credentials The given user
     * @param groupGuids Checking if the given user is a member of any of these
     *        groups.
     * @return An array of GUID objects identifying groups the given user is a
     *         member of.
     * @throws WebADEDeveloperException
     */
    public GUID[] userInGroups(UserCredentials credentials, GUID[] groupGuids) throws WebADEDeveloperException {
        if (logger.isDebugEnabled()) {
            logger.debug("userInGroups: UserCredentials " + credentials + "GUID[] " + groupGuids);
        }
        ArrayList<GUID> allGroups = new ArrayList<GUID>();
        try {
            Element users = usersDocument.getDocumentElement();
            // String guidString = user.getUserGuid().toMicrosoftGUIDString();
            // xPathExpression = "*[@guid="A1234567890"]/group";
            String xPathExpression = "";
            GUID guid = credentials.getUserGuid();
            if (guid != null) {
                String guidString = guid.toMicrosoftGUIDString();
                // xPathExpression = "...[@guid="AB12345"]";
                xPathExpression = "*[@guid=\"" + guidString + "\"]/group";
            }
            else {
                String accountName = credentials.getAccountName();
                String sourceDirectory = credentials.getSourceDirectory();
                // We failed to find a GUID match, try accountName and
                // sourceDirectory
                // xPathExpression = "...[@accountName="CHBOREEN" and
                // @sourceDirectory="IDIR"]";
                xPathExpression = "*[@accountName=\"" + accountName + "\" and @sourceDirectory=\"" + sourceDirectory
                        + "\"]/group";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("xPathExpression = " + xPathExpression);
            }
            NodeList groupList = XPathAPI.selectNodeList(users, xPathExpression);
            // we've found our groups
            for (int i = 0; i < groupList.getLength(); i++) {
                Element element = (Element) groupList.item(i);
                GUID groupGUID = new GUID(element.getAttribute("guid"));
                allGroups.add(groupGUID);
            }
        }
        catch (TransformerException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        ArrayList<GUID> results = new ArrayList<GUID>();
        Iterator<GUID> i = allGroups.iterator();
        while (i.hasNext()) {
            GUID foundGuid = i.next();
            for (int j = 0; j < groupGuids.length; j++) {
                GUID searchGuid = groupGuids[j];
                if (foundGuid.equals(searchGuid)) {
                    results.add(foundGuid);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found User in Groups:");
            Iterator<GUID> j = results.iterator();
            while (j.hasNext()) {
                sb.append(" ");
                sb.append(j.next());
            }
            logger.debug(sb.toString());
        }
        return results.toArray(new GUID[] {});
    }

}
