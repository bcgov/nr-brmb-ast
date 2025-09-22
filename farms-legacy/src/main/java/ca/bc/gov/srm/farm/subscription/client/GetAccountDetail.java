/**
 * GetAccountDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class GetAccountDetail  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest;

    public GetAccountDetail() {
    }

    public GetAccountDetail(
           ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) {
           this.accountDetailRequest = accountDetailRequest;
    }


    /**
     * Gets the accountDetailRequest value for this GetAccountDetail.
     * 
     * @return accountDetailRequest
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest getAccountDetailRequest() {
        return accountDetailRequest;
    }


    /**
     * Sets the accountDetailRequest value for this GetAccountDetail.
     * 
     * @param accountDetailRequest
     */
    public void setAccountDetailRequest(ca.bc.gov.srm.farm.subscription.client.AccountDetailRequest accountDetailRequest) {
        this.accountDetailRequest = accountDetailRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAccountDetail)) return false;
        GetAccountDetail other = (GetAccountDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accountDetailRequest==null && other.getAccountDetailRequest()==null) || 
             (this.accountDetailRequest!=null &&
              this.accountDetailRequest.equals(other.getAccountDetailRequest())));
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
        if (getAccountDetailRequest() != null) {
            _hashCode += getAccountDetailRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAccountDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountDetailRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailRequest"));
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
