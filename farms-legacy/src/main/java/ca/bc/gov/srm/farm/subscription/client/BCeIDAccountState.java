/**
 * BCeIDAccountState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDAccountState  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime expiryDateTime;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isManagerDisabled;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isLocked;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordChangeRequired;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordHintsRequired;

    public BCeIDAccountState() {
    }

    public BCeIDAccountState(
           ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime expiryDateTime,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isManagerDisabled,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isLocked,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordChangeRequired,
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordHintsRequired) {
           this.expiryDateTime = expiryDateTime;
           this.isSuspended = isSuspended;
           this.isManagerDisabled = isManagerDisabled;
           this.isLocked = isLocked;
           this.passwordChangeRequired = passwordChangeRequired;
           this.passwordHintsRequired = passwordHintsRequired;
    }


    /**
     * Gets the expiryDateTime value for this BCeIDAccountState.
     * 
     * @return expiryDateTime
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime getExpiryDateTime() {
        return expiryDateTime;
    }


    /**
     * Sets the expiryDateTime value for this BCeIDAccountState.
     * 
     * @param expiryDateTime
     */
    public void setExpiryDateTime(ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }


    /**
     * Gets the isSuspended value for this BCeIDAccountState.
     * 
     * @return isSuspended
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getIsSuspended() {
        return isSuspended;
    }


    /**
     * Sets the isSuspended value for this BCeIDAccountState.
     * 
     * @param isSuspended
     */
    public void setIsSuspended(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended) {
        this.isSuspended = isSuspended;
    }


    /**
     * Gets the isManagerDisabled value for this BCeIDAccountState.
     * 
     * @return isManagerDisabled
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getIsManagerDisabled() {
        return isManagerDisabled;
    }


    /**
     * Sets the isManagerDisabled value for this BCeIDAccountState.
     * 
     * @param isManagerDisabled
     */
    public void setIsManagerDisabled(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isManagerDisabled) {
        this.isManagerDisabled = isManagerDisabled;
    }


    /**
     * Gets the isLocked value for this BCeIDAccountState.
     * 
     * @return isLocked
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getIsLocked() {
        return isLocked;
    }


    /**
     * Sets the isLocked value for this BCeIDAccountState.
     * 
     * @param isLocked
     */
    public void setIsLocked(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isLocked) {
        this.isLocked = isLocked;
    }


    /**
     * Gets the passwordChangeRequired value for this BCeIDAccountState.
     * 
     * @return passwordChangeRequired
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }


    /**
     * Sets the passwordChangeRequired value for this BCeIDAccountState.
     * 
     * @param passwordChangeRequired
     */
    public void setPasswordChangeRequired(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }


    /**
     * Gets the passwordHintsRequired value for this BCeIDAccountState.
     * 
     * @return passwordHintsRequired
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getPasswordHintsRequired() {
        return passwordHintsRequired;
    }


    /**
     * Sets the passwordHintsRequired value for this BCeIDAccountState.
     * 
     * @param passwordHintsRequired
     */
    public void setPasswordHintsRequired(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean passwordHintsRequired) {
        this.passwordHintsRequired = passwordHintsRequired;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDAccountState)) return false;
        BCeIDAccountState other = (BCeIDAccountState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.expiryDateTime==null && other.getExpiryDateTime()==null) || 
             (this.expiryDateTime!=null &&
              this.expiryDateTime.equals(other.getExpiryDateTime()))) &&
            ((this.isSuspended==null && other.getIsSuspended()==null) || 
             (this.isSuspended!=null &&
              this.isSuspended.equals(other.getIsSuspended()))) &&
            ((this.isManagerDisabled==null && other.getIsManagerDisabled()==null) || 
             (this.isManagerDisabled!=null &&
              this.isManagerDisabled.equals(other.getIsManagerDisabled()))) &&
            ((this.isLocked==null && other.getIsLocked()==null) || 
             (this.isLocked!=null &&
              this.isLocked.equals(other.getIsLocked()))) &&
            ((this.passwordChangeRequired==null && other.getPasswordChangeRequired()==null) || 
             (this.passwordChangeRequired!=null &&
              this.passwordChangeRequired.equals(other.getPasswordChangeRequired()))) &&
            ((this.passwordHintsRequired==null && other.getPasswordHintsRequired()==null) || 
             (this.passwordHintsRequired!=null &&
              this.passwordHintsRequired.equals(other.getPasswordHintsRequired())));
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
        if (getExpiryDateTime() != null) {
            _hashCode += getExpiryDateTime().hashCode();
        }
        if (getIsSuspended() != null) {
            _hashCode += getIsSuspended().hashCode();
        }
        if (getIsManagerDisabled() != null) {
            _hashCode += getIsManagerDisabled().hashCode();
        }
        if (getIsLocked() != null) {
            _hashCode += getIsLocked().hashCode();
        }
        if (getPasswordChangeRequired() != null) {
            _hashCode += getPasswordChangeRequired().hashCode();
        }
        if (getPasswordHintsRequired() != null) {
            _hashCode += getPasswordHintsRequired().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDAccountState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expiryDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "expiryDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDDateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isSuspended");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "isSuspended"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isManagerDisabled");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "isManagerDisabled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isLocked");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "isLocked"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("passwordChangeRequired");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "passwordChangeRequired"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("passwordHintsRequired");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "passwordHintsRequired"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
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
