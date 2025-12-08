/**
 * AccountDetailListResponseItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class AccountDetailListResponseItem  extends ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse  implements java.io.Serializable {
    private java.lang.String clientKey;

    public AccountDetailListResponseItem() {
    }

    public AccountDetailListResponseItem(
           ca.bc.gov.srm.farm.subscription.client.ResponseCode code,
           ca.bc.gov.srm.farm.subscription.client.FailureCode failureCode,
           java.lang.String message,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccount account,
           java.lang.String clientKey) {
        super(
            code,
            failureCode,
            message,
            account);
        this.clientKey = clientKey;
    }


    /**
     * Gets the clientKey value for this AccountDetailListResponseItem.
     * 
     * @return clientKey
     */
    public java.lang.String getClientKey() {
        return clientKey;
    }


    /**
     * Sets the clientKey value for this AccountDetailListResponseItem.
     * 
     * @param clientKey
     */
    public void setClientKey(java.lang.String clientKey) {
        this.clientKey = clientKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountDetailListResponseItem)) return false;
        AccountDetailListResponseItem other = (AccountDetailListResponseItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.clientKey==null && other.getClientKey()==null) || 
             (this.clientKey!=null &&
              this.clientKey.equals(other.getClientKey())));
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
        if (getClientKey() != null) {
            _hashCode += getClientKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccountDetailListResponseItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "clientKey"));
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
