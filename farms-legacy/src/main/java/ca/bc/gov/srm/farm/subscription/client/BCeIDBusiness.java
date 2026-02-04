/**
 * BCeIDBusiness.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDBusiness  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDString guid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString luid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessType type;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString legalName;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString businessNumber;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean businessNumberVerifiedFlag;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString statementOfRegistrationNumber;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString incorporationNumber;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString jurisdictionOfIncorporation;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString doingBusinessAs;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessState state;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessType bnHubBusinessType;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString businessTypeOther;

    public BCeIDBusiness() {
    }

    public BCeIDBusiness(
           ca.bc.gov.srm.farm.subscription.client.BCeIDString guid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString luid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessType type,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString legalName,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString businessNumber,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean businessNumberVerifiedFlag,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString statementOfRegistrationNumber,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString incorporationNumber,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString jurisdictionOfIncorporation,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString doingBusinessAs,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessState state,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessType bnHubBusinessType,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString businessTypeOther) {
           this.guid = guid;
           this.luid = luid;
           this.type = type;
           this.legalName = legalName;
           this.businessNumber = businessNumber;
           this.businessNumberVerifiedFlag = businessNumberVerifiedFlag;
           this.statementOfRegistrationNumber = statementOfRegistrationNumber;
           this.incorporationNumber = incorporationNumber;
           this.jurisdictionOfIncorporation = jurisdictionOfIncorporation;
           this.doingBusinessAs = doingBusinessAs;
           this.address = address;
           this.state = state;
           this.bnHubBusinessType = bnHubBusinessType;
           this.businessTypeOther = businessTypeOther;
    }


    /**
     * Gets the guid value for this BCeIDBusiness.
     * 
     * @return guid
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getGuid() {
        return guid;
    }


    /**
     * Sets the guid value for this BCeIDBusiness.
     * 
     * @param guid
     */
    public void setGuid(ca.bc.gov.srm.farm.subscription.client.BCeIDString guid) {
        this.guid = guid;
    }


    /**
     * Gets the luid value for this BCeIDBusiness.
     * 
     * @return luid
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getLuid() {
        return luid;
    }


    /**
     * Sets the luid value for this BCeIDBusiness.
     * 
     * @param luid
     */
    public void setLuid(ca.bc.gov.srm.farm.subscription.client.BCeIDString luid) {
        this.luid = luid;
    }


    /**
     * Gets the type value for this BCeIDBusiness.
     * 
     * @return type
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessType getType() {
        return type;
    }


    /**
     * Sets the type value for this BCeIDBusiness.
     * 
     * @param type
     */
    public void setType(ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessType type) {
        this.type = type;
    }


    /**
     * Gets the legalName value for this BCeIDBusiness.
     * 
     * @return legalName
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getLegalName() {
        return legalName;
    }


    /**
     * Sets the legalName value for this BCeIDBusiness.
     * 
     * @param legalName
     */
    public void setLegalName(ca.bc.gov.srm.farm.subscription.client.BCeIDString legalName) {
        this.legalName = legalName;
    }


    /**
     * Gets the businessNumber value for this BCeIDBusiness.
     * 
     * @return businessNumber
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getBusinessNumber() {
        return businessNumber;
    }


    /**
     * Sets the businessNumber value for this BCeIDBusiness.
     * 
     * @param businessNumber
     */
    public void setBusinessNumber(ca.bc.gov.srm.farm.subscription.client.BCeIDString businessNumber) {
        this.businessNumber = businessNumber;
    }


    /**
     * Gets the businessNumberVerifiedFlag value for this BCeIDBusiness.
     * 
     * @return businessNumberVerifiedFlag
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getBusinessNumberVerifiedFlag() {
        return businessNumberVerifiedFlag;
    }


    /**
     * Sets the businessNumberVerifiedFlag value for this BCeIDBusiness.
     * 
     * @param businessNumberVerifiedFlag
     */
    public void setBusinessNumberVerifiedFlag(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean businessNumberVerifiedFlag) {
        this.businessNumberVerifiedFlag = businessNumberVerifiedFlag;
    }


    /**
     * Gets the statementOfRegistrationNumber value for this BCeIDBusiness.
     * 
     * @return statementOfRegistrationNumber
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getStatementOfRegistrationNumber() {
        return statementOfRegistrationNumber;
    }


    /**
     * Sets the statementOfRegistrationNumber value for this BCeIDBusiness.
     * 
     * @param statementOfRegistrationNumber
     */
    public void setStatementOfRegistrationNumber(ca.bc.gov.srm.farm.subscription.client.BCeIDString statementOfRegistrationNumber) {
        this.statementOfRegistrationNumber = statementOfRegistrationNumber;
    }


    /**
     * Gets the incorporationNumber value for this BCeIDBusiness.
     * 
     * @return incorporationNumber
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getIncorporationNumber() {
        return incorporationNumber;
    }


    /**
     * Sets the incorporationNumber value for this BCeIDBusiness.
     * 
     * @param incorporationNumber
     */
    public void setIncorporationNumber(ca.bc.gov.srm.farm.subscription.client.BCeIDString incorporationNumber) {
        this.incorporationNumber = incorporationNumber;
    }


    /**
     * Gets the jurisdictionOfIncorporation value for this BCeIDBusiness.
     * 
     * @return jurisdictionOfIncorporation
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getJurisdictionOfIncorporation() {
        return jurisdictionOfIncorporation;
    }


    /**
     * Sets the jurisdictionOfIncorporation value for this BCeIDBusiness.
     * 
     * @param jurisdictionOfIncorporation
     */
    public void setJurisdictionOfIncorporation(ca.bc.gov.srm.farm.subscription.client.BCeIDString jurisdictionOfIncorporation) {
        this.jurisdictionOfIncorporation = jurisdictionOfIncorporation;
    }


    /**
     * Gets the doingBusinessAs value for this BCeIDBusiness.
     * 
     * @return doingBusinessAs
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getDoingBusinessAs() {
        return doingBusinessAs;
    }


    /**
     * Sets the doingBusinessAs value for this BCeIDBusiness.
     * 
     * @param doingBusinessAs
     */
    public void setDoingBusinessAs(ca.bc.gov.srm.farm.subscription.client.BCeIDString doingBusinessAs) {
        this.doingBusinessAs = doingBusinessAs;
    }


    /**
     * Gets the address value for this BCeIDBusiness.
     * 
     * @return address
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAddress getAddress() {
        return address;
    }


    /**
     * Sets the address value for this BCeIDBusiness.
     * 
     * @param address
     */
    public void setAddress(ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address) {
        this.address = address;
    }


    /**
     * Gets the state value for this BCeIDBusiness.
     * 
     * @return state
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessState getState() {
        return state;
    }


    /**
     * Sets the state value for this BCeIDBusiness.
     * 
     * @param state
     */
    public void setState(ca.bc.gov.srm.farm.subscription.client.BCeIDBusinessState state) {
        this.state = state;
    }


    /**
     * Gets the bnHubBusinessType value for this BCeIDBusiness.
     * 
     * @return bnHubBusinessType
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessType getBnHubBusinessType() {
        return bnHubBusinessType;
    }


    /**
     * Sets the bnHubBusinessType value for this BCeIDBusiness.
     * 
     * @param bnHubBusinessType
     */
    public void setBnHubBusinessType(ca.bc.gov.srm.farm.subscription.client.BCeIDBNHubBusinessType bnHubBusinessType) {
        this.bnHubBusinessType = bnHubBusinessType;
    }


    /**
     * Gets the businessTypeOther value for this BCeIDBusiness.
     * 
     * @return businessTypeOther
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getBusinessTypeOther() {
        return businessTypeOther;
    }


    /**
     * Sets the businessTypeOther value for this BCeIDBusiness.
     * 
     * @param businessTypeOther
     */
    public void setBusinessTypeOther(ca.bc.gov.srm.farm.subscription.client.BCeIDString businessTypeOther) {
        this.businessTypeOther = businessTypeOther;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDBusiness)) return false;
        BCeIDBusiness other = (BCeIDBusiness) obj;
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
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.legalName==null && other.getLegalName()==null) || 
             (this.legalName!=null &&
              this.legalName.equals(other.getLegalName()))) &&
            ((this.businessNumber==null && other.getBusinessNumber()==null) || 
             (this.businessNumber!=null &&
              this.businessNumber.equals(other.getBusinessNumber()))) &&
            ((this.businessNumberVerifiedFlag==null && other.getBusinessNumberVerifiedFlag()==null) || 
             (this.businessNumberVerifiedFlag!=null &&
              this.businessNumberVerifiedFlag.equals(other.getBusinessNumberVerifiedFlag()))) &&
            ((this.statementOfRegistrationNumber==null && other.getStatementOfRegistrationNumber()==null) || 
             (this.statementOfRegistrationNumber!=null &&
              this.statementOfRegistrationNumber.equals(other.getStatementOfRegistrationNumber()))) &&
            ((this.incorporationNumber==null && other.getIncorporationNumber()==null) || 
             (this.incorporationNumber!=null &&
              this.incorporationNumber.equals(other.getIncorporationNumber()))) &&
            ((this.jurisdictionOfIncorporation==null && other.getJurisdictionOfIncorporation()==null) || 
             (this.jurisdictionOfIncorporation!=null &&
              this.jurisdictionOfIncorporation.equals(other.getJurisdictionOfIncorporation()))) &&
            ((this.doingBusinessAs==null && other.getDoingBusinessAs()==null) || 
             (this.doingBusinessAs!=null &&
              this.doingBusinessAs.equals(other.getDoingBusinessAs()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.bnHubBusinessType==null && other.getBnHubBusinessType()==null) || 
             (this.bnHubBusinessType!=null &&
              this.bnHubBusinessType.equals(other.getBnHubBusinessType()))) &&
            ((this.businessTypeOther==null && other.getBusinessTypeOther()==null) || 
             (this.businessTypeOther!=null &&
              this.businessTypeOther.equals(other.getBusinessTypeOther())));
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
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getLegalName() != null) {
            _hashCode += getLegalName().hashCode();
        }
        if (getBusinessNumber() != null) {
            _hashCode += getBusinessNumber().hashCode();
        }
        if (getBusinessNumberVerifiedFlag() != null) {
            _hashCode += getBusinessNumberVerifiedFlag().hashCode();
        }
        if (getStatementOfRegistrationNumber() != null) {
            _hashCode += getStatementOfRegistrationNumber().hashCode();
        }
        if (getIncorporationNumber() != null) {
            _hashCode += getIncorporationNumber().hashCode();
        }
        if (getJurisdictionOfIncorporation() != null) {
            _hashCode += getJurisdictionOfIncorporation().hashCode();
        }
        if (getDoingBusinessAs() != null) {
            _hashCode += getDoingBusinessAs().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getBnHubBusinessType() != null) {
            _hashCode += getBnHubBusinessType().hashCode();
        }
        if (getBusinessTypeOther() != null) {
            _hashCode += getBusinessTypeOther().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDBusiness.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusiness"));
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
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("legalName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "legalName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "businessNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessNumberVerifiedFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "businessNumberVerifiedFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementOfRegistrationNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "statementOfRegistrationNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incorporationNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "incorporationNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jurisdictionOfIncorporation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "jurisdictionOfIncorporation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doingBusinessAs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "doingBusinessAs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessState"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bnHubBusinessType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "bnHubBusinessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBNHubBusinessType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessTypeOther");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "businessTypeOther"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
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
