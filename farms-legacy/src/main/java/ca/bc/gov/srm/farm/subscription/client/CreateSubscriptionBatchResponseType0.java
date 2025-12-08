/**
 * CreateSubscriptionBatchResponseType0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class CreateSubscriptionBatchResponseType0  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatchResult;

    public CreateSubscriptionBatchResponseType0() {
    }

    public CreateSubscriptionBatchResponseType0(
           ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatchResult) {
           this.createSubscriptionBatchResult = createSubscriptionBatchResult;
    }


    /**
     * Gets the createSubscriptionBatchResult value for this CreateSubscriptionBatchResponseType0.
     * 
     * @return createSubscriptionBatchResult
     */
    public ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse getCreateSubscriptionBatchResult() {
        return createSubscriptionBatchResult;
    }


    /**
     * Sets the createSubscriptionBatchResult value for this CreateSubscriptionBatchResponseType0.
     * 
     * @param createSubscriptionBatchResult
     */
    public void setCreateSubscriptionBatchResult(ca.bc.gov.srm.farm.subscription.client.CreateSubscriptionBatchResponse createSubscriptionBatchResult) {
        this.createSubscriptionBatchResult = createSubscriptionBatchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSubscriptionBatchResponseType0)) return false;
        CreateSubscriptionBatchResponseType0 other = (CreateSubscriptionBatchResponseType0) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createSubscriptionBatchResult==null && other.getCreateSubscriptionBatchResult()==null) || 
             (this.createSubscriptionBatchResult!=null &&
              this.createSubscriptionBatchResult.equals(other.getCreateSubscriptionBatchResult())));
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
        if (getCreateSubscriptionBatchResult() != null) {
            _hashCode += getCreateSubscriptionBatchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSubscriptionBatchResponseType0.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createSubscriptionBatchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createSubscriptionBatchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createSubscriptionBatchResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchResponse"));
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
