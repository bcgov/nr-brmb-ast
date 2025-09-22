/**
 * BCeIDBusinessMatch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDBusinessMatch  implements java.io.Serializable {
    private java.lang.String businessGuid;

    private java.lang.String luid;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString legalName;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString doingBusinessAs;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString businessNumber;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessNumberVerified searchableBusinessNumberVerified;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString statementOfReg;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString incorporationNumber;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString jurisdictionOfIncorporation;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessType searchableBusinessType;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessState searchableBusinessState;

    public BCeIDBusinessMatch() {
    }

    public BCeIDBusinessMatch(
           java.lang.String businessGuid,
           java.lang.String luid,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString legalName,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString doingBusinessAs,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString businessNumber,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessNumberVerified searchableBusinessNumberVerified,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString statementOfReg,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString incorporationNumber,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString jurisdictionOfIncorporation,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessType searchableBusinessType,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessState searchableBusinessState) {
           this.businessGuid = businessGuid;
           this.luid = luid;
           this.legalName = legalName;
           this.doingBusinessAs = doingBusinessAs;
           this.businessNumber = businessNumber;
           this.searchableBusinessNumberVerified = searchableBusinessNumberVerified;
           this.statementOfReg = statementOfReg;
           this.incorporationNumber = incorporationNumber;
           this.jurisdictionOfIncorporation = jurisdictionOfIncorporation;
           this.searchableBusinessType = searchableBusinessType;
           this.searchableBusinessState = searchableBusinessState;
    }


    /**
     * Gets the businessGuid value for this BCeIDBusinessMatch.
     * 
     * @return businessGuid
     */
    public java.lang.String getBusinessGuid() {
        return businessGuid;
    }


    /**
     * Sets the businessGuid value for this BCeIDBusinessMatch.
     * 
     * @param businessGuid
     */
    public void setBusinessGuid(java.lang.String businessGuid) {
        this.businessGuid = businessGuid;
    }


    /**
     * Gets the luid value for this BCeIDBusinessMatch.
     * 
     * @return luid
     */
    public java.lang.String getLuid() {
        return luid;
    }


    /**
     * Sets the luid value for this BCeIDBusinessMatch.
     * 
     * @param luid
     */
    public void setLuid(java.lang.String luid) {
        this.luid = luid;
    }


    /**
     * Gets the legalName value for this BCeIDBusinessMatch.
     * 
     * @return legalName
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getLegalName() {
        return legalName;
    }


    /**
     * Sets the legalName value for this BCeIDBusinessMatch.
     * 
     * @param legalName
     */
    public void setLegalName(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString legalName) {
        this.legalName = legalName;
    }


    /**
     * Gets the doingBusinessAs value for this BCeIDBusinessMatch.
     * 
     * @return doingBusinessAs
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getDoingBusinessAs() {
        return doingBusinessAs;
    }


    /**
     * Sets the doingBusinessAs value for this BCeIDBusinessMatch.
     * 
     * @param doingBusinessAs
     */
    public void setDoingBusinessAs(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString doingBusinessAs) {
        this.doingBusinessAs = doingBusinessAs;
    }


    /**
     * Gets the businessNumber value for this BCeIDBusinessMatch.
     * 
     * @return businessNumber
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getBusinessNumber() {
        return businessNumber;
    }


    /**
     * Sets the businessNumber value for this BCeIDBusinessMatch.
     * 
     * @param businessNumber
     */
    public void setBusinessNumber(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString businessNumber) {
        this.businessNumber = businessNumber;
    }


    /**
     * Gets the searchableBusinessNumberVerified value for this BCeIDBusinessMatch.
     * 
     * @return searchableBusinessNumberVerified
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessNumberVerified getSearchableBusinessNumberVerified() {
        return searchableBusinessNumberVerified;
    }


    /**
     * Sets the searchableBusinessNumberVerified value for this BCeIDBusinessMatch.
     * 
     * @param searchableBusinessNumberVerified
     */
    public void setSearchableBusinessNumberVerified(ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessNumberVerified searchableBusinessNumberVerified) {
        this.searchableBusinessNumberVerified = searchableBusinessNumberVerified;
    }


    /**
     * Gets the statementOfReg value for this BCeIDBusinessMatch.
     * 
     * @return statementOfReg
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getStatementOfReg() {
        return statementOfReg;
    }


    /**
     * Sets the statementOfReg value for this BCeIDBusinessMatch.
     * 
     * @param statementOfReg
     */
    public void setStatementOfReg(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString statementOfReg) {
        this.statementOfReg = statementOfReg;
    }


    /**
     * Gets the incorporationNumber value for this BCeIDBusinessMatch.
     * 
     * @return incorporationNumber
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getIncorporationNumber() {
        return incorporationNumber;
    }


    /**
     * Sets the incorporationNumber value for this BCeIDBusinessMatch.
     * 
     * @param incorporationNumber
     */
    public void setIncorporationNumber(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString incorporationNumber) {
        this.incorporationNumber = incorporationNumber;
    }


    /**
     * Gets the jurisdictionOfIncorporation value for this BCeIDBusinessMatch.
     * 
     * @return jurisdictionOfIncorporation
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getJurisdictionOfIncorporation() {
        return jurisdictionOfIncorporation;
    }


    /**
     * Sets the jurisdictionOfIncorporation value for this BCeIDBusinessMatch.
     * 
     * @param jurisdictionOfIncorporation
     */
    public void setJurisdictionOfIncorporation(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString jurisdictionOfIncorporation) {
        this.jurisdictionOfIncorporation = jurisdictionOfIncorporation;
    }


    /**
     * Gets the searchableBusinessType value for this BCeIDBusinessMatch.
     * 
     * @return searchableBusinessType
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessType getSearchableBusinessType() {
        return searchableBusinessType;
    }


    /**
     * Sets the searchableBusinessType value for this BCeIDBusinessMatch.
     * 
     * @param searchableBusinessType
     */
    public void setSearchableBusinessType(ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessType searchableBusinessType) {
        this.searchableBusinessType = searchableBusinessType;
    }


    /**
     * Gets the searchableBusinessState value for this BCeIDBusinessMatch.
     * 
     * @return searchableBusinessState
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessState getSearchableBusinessState() {
        return searchableBusinessState;
    }


    /**
     * Sets the searchableBusinessState value for this BCeIDBusinessMatch.
     * 
     * @param searchableBusinessState
     */
    public void setSearchableBusinessState(ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableBusinessState searchableBusinessState) {
        this.searchableBusinessState = searchableBusinessState;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDBusinessMatch)) return false;
        BCeIDBusinessMatch other = (BCeIDBusinessMatch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.businessGuid==null && other.getBusinessGuid()==null) || 
             (this.businessGuid!=null &&
              this.businessGuid.equals(other.getBusinessGuid()))) &&
            ((this.luid==null && other.getLuid()==null) || 
             (this.luid!=null &&
              this.luid.equals(other.getLuid()))) &&
            ((this.legalName==null && other.getLegalName()==null) || 
             (this.legalName!=null &&
              this.legalName.equals(other.getLegalName()))) &&
            ((this.doingBusinessAs==null && other.getDoingBusinessAs()==null) || 
             (this.doingBusinessAs!=null &&
              this.doingBusinessAs.equals(other.getDoingBusinessAs()))) &&
            ((this.businessNumber==null && other.getBusinessNumber()==null) || 
             (this.businessNumber!=null &&
              this.businessNumber.equals(other.getBusinessNumber()))) &&
            ((this.searchableBusinessNumberVerified==null && other.getSearchableBusinessNumberVerified()==null) || 
             (this.searchableBusinessNumberVerified!=null &&
              this.searchableBusinessNumberVerified.equals(other.getSearchableBusinessNumberVerified()))) &&
            ((this.statementOfReg==null && other.getStatementOfReg()==null) || 
             (this.statementOfReg!=null &&
              this.statementOfReg.equals(other.getStatementOfReg()))) &&
            ((this.incorporationNumber==null && other.getIncorporationNumber()==null) || 
             (this.incorporationNumber!=null &&
              this.incorporationNumber.equals(other.getIncorporationNumber()))) &&
            ((this.jurisdictionOfIncorporation==null && other.getJurisdictionOfIncorporation()==null) || 
             (this.jurisdictionOfIncorporation!=null &&
              this.jurisdictionOfIncorporation.equals(other.getJurisdictionOfIncorporation()))) &&
            ((this.searchableBusinessType==null && other.getSearchableBusinessType()==null) || 
             (this.searchableBusinessType!=null &&
              this.searchableBusinessType.equals(other.getSearchableBusinessType()))) &&
            ((this.searchableBusinessState==null && other.getSearchableBusinessState()==null) || 
             (this.searchableBusinessState!=null &&
              this.searchableBusinessState.equals(other.getSearchableBusinessState())));
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
        if (getBusinessGuid() != null) {
            _hashCode += getBusinessGuid().hashCode();
        }
        if (getLuid() != null) {
            _hashCode += getLuid().hashCode();
        }
        if (getLegalName() != null) {
            _hashCode += getLegalName().hashCode();
        }
        if (getDoingBusinessAs() != null) {
            _hashCode += getDoingBusinessAs().hashCode();
        }
        if (getBusinessNumber() != null) {
            _hashCode += getBusinessNumber().hashCode();
        }
        if (getSearchableBusinessNumberVerified() != null) {
            _hashCode += getSearchableBusinessNumberVerified().hashCode();
        }
        if (getStatementOfReg() != null) {
            _hashCode += getStatementOfReg().hashCode();
        }
        if (getIncorporationNumber() != null) {
            _hashCode += getIncorporationNumber().hashCode();
        }
        if (getJurisdictionOfIncorporation() != null) {
            _hashCode += getJurisdictionOfIncorporation().hashCode();
        }
        if (getSearchableBusinessType() != null) {
            _hashCode += getSearchableBusinessType().hashCode();
        }
        if (getSearchableBusinessState() != null) {
            _hashCode += getSearchableBusinessState().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDBusinessMatch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessMatch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessGuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "businessGuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("luid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "luid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("legalName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "legalName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doingBusinessAs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "doingBusinessAs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "businessNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchableBusinessNumberVerified");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchableBusinessNumberVerified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessNumberVerified"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementOfReg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "statementOfReg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incorporationNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "incorporationNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jurisdictionOfIncorporation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "jurisdictionOfIncorporation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchableBusinessType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchableBusinessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchableBusinessState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchableBusinessState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableBusinessState"));
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
