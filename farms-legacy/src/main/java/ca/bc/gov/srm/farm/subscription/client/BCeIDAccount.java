/**
 * BCeIDAccount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDAccount  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDString guid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString luid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString bceidLuid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString userId;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString displayName;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString employeeId;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType type;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountState state;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContact contact;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDIndividualIdentity individualIdentity;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBusiness business;

    public BCeIDAccount() {
    }

    public BCeIDAccount(
           ca.bc.gov.srm.farm.subscription.client.BCeIDString guid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString luid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString bceidLuid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString userId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString displayName,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString employeeId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType type,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountState state,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContact contact,
           ca.bc.gov.srm.farm.subscription.client.BCeIDIndividualIdentity individualIdentity,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBusiness business) {
           this.guid = guid;
           this.luid = luid;
           this.bceidLuid = bceidLuid;
           this.userId = userId;
           this.displayName = displayName;
           this.employeeId = employeeId;
           this.type = type;
           this.state = state;
           this.contact = contact;
           this.individualIdentity = individualIdentity;
           this.business = business;
    }


    /**
     * Gets the guid value for this BCeIDAccount.
     * 
     * @return guid
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getGuid() {
        return guid;
    }


    /**
     * Sets the guid value for this BCeIDAccount.
     * 
     * @param guid
     */
    public void setGuid(ca.bc.gov.srm.farm.subscription.client.BCeIDString guid) {
        this.guid = guid;
    }


    /**
     * Gets the luid value for this BCeIDAccount.
     * 
     * @return luid
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getLuid() {
        return luid;
    }


    /**
     * Sets the luid value for this BCeIDAccount.
     * 
     * @param luid
     */
    public void setLuid(ca.bc.gov.srm.farm.subscription.client.BCeIDString luid) {
        this.luid = luid;
    }


    /**
     * Gets the bceidLuid value for this BCeIDAccount.
     * 
     * @return bceidLuid
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getBceidLuid() {
        return bceidLuid;
    }


    /**
     * Sets the bceidLuid value for this BCeIDAccount.
     * 
     * @param bceidLuid
     */
    public void setBceidLuid(ca.bc.gov.srm.farm.subscription.client.BCeIDString bceidLuid) {
        this.bceidLuid = bceidLuid;
    }


    /**
     * Gets the userId value for this BCeIDAccount.
     * 
     * @return userId
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this BCeIDAccount.
     * 
     * @param userId
     */
    public void setUserId(ca.bc.gov.srm.farm.subscription.client.BCeIDString userId) {
        this.userId = userId;
    }


    /**
     * Gets the displayName value for this BCeIDAccount.
     * 
     * @return displayName
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this BCeIDAccount.
     * 
     * @param displayName
     */
    public void setDisplayName(ca.bc.gov.srm.farm.subscription.client.BCeIDString displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the employeeId value for this BCeIDAccount.
     * 
     * @return employeeId
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getEmployeeId() {
        return employeeId;
    }


    /**
     * Sets the employeeId value for this BCeIDAccount.
     * 
     * @param employeeId
     */
    public void setEmployeeId(ca.bc.gov.srm.farm.subscription.client.BCeIDString employeeId) {
        this.employeeId = employeeId;
    }


    /**
     * Gets the type value for this BCeIDAccount.
     * 
     * @return type
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType getType() {
        return type;
    }


    /**
     * Sets the type value for this BCeIDAccount.
     * 
     * @param type
     */
    public void setType(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountType type) {
        this.type = type;
    }


    /**
     * Gets the state value for this BCeIDAccount.
     * 
     * @return state
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountState getState() {
        return state;
    }


    /**
     * Sets the state value for this BCeIDAccount.
     * 
     * @param state
     */
    public void setState(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountState state) {
        this.state = state;
    }


    /**
     * Gets the contact value for this BCeIDAccount.
     * 
     * @return contact
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContact getContact() {
        return contact;
    }


    /**
     * Sets the contact value for this BCeIDAccount.
     * 
     * @param contact
     */
    public void setContact(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContact contact) {
        this.contact = contact;
    }


    /**
     * Gets the individualIdentity value for this BCeIDAccount.
     * 
     * @return individualIdentity
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDIndividualIdentity getIndividualIdentity() {
        return individualIdentity;
    }


    /**
     * Sets the individualIdentity value for this BCeIDAccount.
     * 
     * @param individualIdentity
     */
    public void setIndividualIdentity(ca.bc.gov.srm.farm.subscription.client.BCeIDIndividualIdentity individualIdentity) {
        this.individualIdentity = individualIdentity;
    }


    /**
     * Gets the business value for this BCeIDAccount.
     * 
     * @return business
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBusiness getBusiness() {
        return business;
    }


    /**
     * Sets the business value for this BCeIDAccount.
     * 
     * @param business
     */
    public void setBusiness(ca.bc.gov.srm.farm.subscription.client.BCeIDBusiness business) {
        this.business = business;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDAccount)) return false;
        BCeIDAccount other = (BCeIDAccount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.guid==null && other.getGuid()==null) || 
             (this.guid!=null &&
              this.guid.equals(other.getGuid()))) &&
            ((this.luid==null && other.getLuid()==null) || 
             (this.luid!=null &&
              this.luid.equals(other.getLuid()))) &&
            ((this.bceidLuid==null && other.getBceidLuid()==null) || 
             (this.bceidLuid!=null &&
              this.bceidLuid.equals(other.getBceidLuid()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.employeeId==null && other.getEmployeeId()==null) || 
             (this.employeeId!=null &&
              this.employeeId.equals(other.getEmployeeId()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.contact==null && other.getContact()==null) || 
             (this.contact!=null &&
              this.contact.equals(other.getContact()))) &&
            ((this.individualIdentity==null && other.getIndividualIdentity()==null) || 
             (this.individualIdentity!=null &&
              this.individualIdentity.equals(other.getIndividualIdentity()))) &&
            ((this.business==null && other.getBusiness()==null) || 
             (this.business!=null &&
              this.business.equals(other.getBusiness())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGuid() != null) {
            _hashCode += getGuid().hashCode();
        }
        if (getLuid() != null) {
            _hashCode += getLuid().hashCode();
        }
        if (getBceidLuid() != null) {
            _hashCode += getBceidLuid().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getEmployeeId() != null) {
            _hashCode += getEmployeeId().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getContact() != null) {
            _hashCode += getContact().hashCode();
        }
        if (getIndividualIdentity() != null) {
            _hashCode += getIndividualIdentity().hashCode();
        }
        if (getBusiness() != null) {
            _hashCode += getBusiness().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDAccount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "guid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("luid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "luid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bceidLuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "bceidLuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "displayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "employeeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountState"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "contact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContact"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("individualIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "individualIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDIndividualIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("business");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "business"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusiness"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
