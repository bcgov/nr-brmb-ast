/**
 * CreateSubscriptionBatchRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class CreateSubscriptionBatchRequest  extends ca.bc.gov.srm.farm.subscription.client.RequestBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch batch;

    public CreateSubscriptionBatchRequest() {
    }

    public CreateSubscriptionBatchRequest(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch batch) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid);
        this.batch = batch;
    }


    /**
     * Gets the batch value for this CreateSubscriptionBatchRequest.
     * 
     * @return batch
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch getBatch() {
        return batch;
    }


    /**
     * Sets the batch value for this CreateSubscriptionBatchRequest.
     * 
     * @param batch
     */
    public void setBatch(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionBatch batch) {
        this.batch = batch;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSubscriptionBatchRequest)) return false;
        CreateSubscriptionBatchRequest other = (CreateSubscriptionBatchRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.batch==null && other.getBatch()==null) || 
             (this.batch!=null &&
              this.batch.equals(other.getBatch())));
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
        if (getBatch() != null) {
            _hashCode += getBatch().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSubscriptionBatchRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateSubscriptionBatchRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionBatch"));
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
