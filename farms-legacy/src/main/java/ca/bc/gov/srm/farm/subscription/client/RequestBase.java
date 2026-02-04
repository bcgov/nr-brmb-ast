/**
 * RequestBase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public abstract class RequestBase  implements java.io.Serializable {
    private java.lang.String onlineServiceId;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode;

    private java.lang.String requesterUserId;

    private java.lang.String requesterUserGuid;

    public RequestBase() {
    }

    public RequestBase(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid) {
           this.onlineServiceId = onlineServiceId;
           this.requesterAccountTypeCode = requesterAccountTypeCode;
           this.requesterUserId = requesterUserId;
           this.requesterUserGuid = requesterUserGuid;
    }


    /**
     * Gets the onlineServiceId value for this RequestBase.
     * 
     * @return onlineServiceId
     */
    public java.lang.String getOnlineServiceId() {
        return onlineServiceId;
    }


    /**
     * Sets the onlineServiceId value for this RequestBase.
     * 
     * @param onlineServiceId
     */
    public void setOnlineServiceId(java.lang.String onlineServiceId) {
        this.onlineServiceId = onlineServiceId;
    }


    /**
     * Gets the requesterAccountTypeCode value for this RequestBase.
     * 
     * @return requesterAccountTypeCode
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode getRequesterAccountTypeCode() {
        return requesterAccountTypeCode;
    }


    /**
     * Sets the requesterAccountTypeCode value for this RequestBase.
     * 
     * @param requesterAccountTypeCode
     */
    public void setRequesterAccountTypeCode(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode) {
        this.requesterAccountTypeCode = requesterAccountTypeCode;
    }


    /**
     * Gets the requesterUserId value for this RequestBase.
     * 
     * @return requesterUserId
     */
    public java.lang.String getRequesterUserId() {
        return requesterUserId;
    }


    /**
     * Sets the requesterUserId value for this RequestBase.
     * 
     * @param requesterUserId
     */
    public void setRequesterUserId(java.lang.String requesterUserId) {
        this.requesterUserId = requesterUserId;
    }


    /**
     * Gets the requesterUserGuid value for this RequestBase.
     * 
     * @return requesterUserGuid
     */
    public java.lang.String getRequesterUserGuid() {
        return requesterUserGuid;
    }


    /**
     * Sets the requesterUserGuid value for this RequestBase.
     * 
     * @param requesterUserGuid
     */
    public void setRequesterUserGuid(java.lang.String requesterUserGuid) {
        this.requesterUserGuid = requesterUserGuid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestBase)) return false;
        RequestBase other = (RequestBase) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.onlineServiceId==null && other.getOnlineServiceId()==null) || 
             (this.onlineServiceId!=null &&
              this.onlineServiceId.equals(other.getOnlineServiceId()))) &&
            ((this.requesterAccountTypeCode==null && other.getRequesterAccountTypeCode()==null) || 
             (this.requesterAccountTypeCode!=null &&
              this.requesterAccountTypeCode.equals(other.getRequesterAccountTypeCode()))) &&
            ((this.requesterUserId==null && other.getRequesterUserId()==null) || 
             (this.requesterUserId!=null &&
              this.requesterUserId.equals(other.getRequesterUserId()))) &&
            ((this.requesterUserGuid==null && other.getRequesterUserGuid()==null) || 
             (this.requesterUserGuid!=null &&
              this.requesterUserGuid.equals(other.getRequesterUserGuid())));
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
        if (getOnlineServiceId() != null) {
            _hashCode += getOnlineServiceId().hashCode();
        }
        if (getRequesterAccountTypeCode() != null) {
            _hashCode += getRequesterAccountTypeCode().hashCode();
        }
        if (getRequesterUserId() != null) {
            _hashCode += getRequesterUserId().hashCode();
        }
        if (getRequesterUserGuid() != null) {
            _hashCode += getRequesterUserGuid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestBase.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "RequestBase"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onlineServiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "onlineServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requesterAccountTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requesterAccountTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountTypeCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requesterUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requesterUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requesterUserGuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requesterUserGuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
