/**
 * SubscriptionCodeListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SubscriptionCodeListResponse  extends ca.bc.gov.srm.farm.subscription.client.ResponseBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription[] subscriptionList;

    public SubscriptionCodeListResponse() {
    }

    public SubscriptionCodeListResponse(
           ca.bc.gov.srm.farm.subscription.client.ResponseCode code,
           ca.bc.gov.srm.farm.subscription.client.FailureCode failureCode,
           java.lang.String message,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription[] subscriptionList) {
        super(
            code,
            failureCode,
            message);
        this.subscriptionList = subscriptionList;
    }


    /**
     * Gets the subscriptionList value for this SubscriptionCodeListResponse.
     * 
     * @return subscriptionList
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription[] getSubscriptionList() {
        return subscriptionList;
    }


    /**
     * Sets the subscriptionList value for this SubscriptionCodeListResponse.
     * 
     * @param subscriptionList
     */
    public void setSubscriptionList(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscription[] subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubscriptionCodeListResponse)) return false;
        SubscriptionCodeListResponse other = (SubscriptionCodeListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.subscriptionList==null && other.getSubscriptionList()==null) || 
             (this.subscriptionList!=null &&
              java.util.Arrays.equals(this.subscriptionList, other.getSubscriptionList())));
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
        if (getSubscriptionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubscriptionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubscriptionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubscriptionCodeListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SubscriptionCodeListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscriptionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription"));
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
