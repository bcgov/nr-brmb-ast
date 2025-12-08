/**
 * SubscriptionCodeListRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SubscriptionCodeListRequest  extends ca.bc.gov.srm.farm.subscription.client.RequestBase  implements java.io.Serializable {
    private java.lang.String userGuid;

    public SubscriptionCodeListRequest() {
    }

    public SubscriptionCodeListRequest(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           java.lang.String userGuid) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid);
        this.userGuid = userGuid;
    }


    /**
     * Gets the userGuid value for this SubscriptionCodeListRequest.
     * 
     * @return userGuid
     */
    public java.lang.String getUserGuid() {
        return userGuid;
    }


    /**
     * Sets the userGuid value for this SubscriptionCodeListRequest.
     * 
     * @param userGuid
     */
    public void setUserGuid(java.lang.String userGuid) {
        this.userGuid = userGuid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubscriptionCodeListRequest)) return false;
        SubscriptionCodeListRequest other = (SubscriptionCodeListRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.userGuid==null && other.getUserGuid()==null) || 
             (this.userGuid!=null &&
              this.userGuid.equals(other.getUserGuid())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getUserGuid() != null) {
            _hashCode += getUserGuid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubscriptionCodeListRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userGuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "userGuid"));
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
