/**
 * GetSubscriptionCodeList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class GetSubscriptionCodeList  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest;

    public GetSubscriptionCodeList() {
    }

    public GetSubscriptionCodeList(
           ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) {
           this.subscriptionCodeListRequest = subscriptionCodeListRequest;
    }


    /**
     * Gets the subscriptionCodeListRequest value for this GetSubscriptionCodeList.
     * 
     * @return subscriptionCodeListRequest
     */
    public ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest getSubscriptionCodeListRequest() {
        return subscriptionCodeListRequest;
    }


    /**
     * Sets the subscriptionCodeListRequest value for this GetSubscriptionCodeList.
     * 
     * @param subscriptionCodeListRequest
     */
    public void setSubscriptionCodeListRequest(ca.bc.gov.srm.farm.subscription.client.SubscriptionCodeListRequest subscriptionCodeListRequest) {
        this.subscriptionCodeListRequest = subscriptionCodeListRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubscriptionCodeList)) return false;
        GetSubscriptionCodeList other = (GetSubscriptionCodeList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subscriptionCodeListRequest==null && other.getSubscriptionCodeListRequest()==null) || 
             (this.subscriptionCodeListRequest!=null &&
              this.subscriptionCodeListRequest.equals(other.getSubscriptionCodeListRequest())));
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
        if (getSubscriptionCodeListRequest() != null) {
            _hashCode += getSubscriptionCodeListRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscriptionCodeList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getSubscriptionCodeList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscriptionCodeListRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionCodeListRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListRequest"));
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
