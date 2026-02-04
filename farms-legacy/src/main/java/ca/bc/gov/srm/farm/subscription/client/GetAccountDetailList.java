/**
 * GetAccountDetailList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class GetAccountDetailList  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest;

    public GetAccountDetailList() {
    }

    public GetAccountDetailList(
           ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) {
           this.accountDetailListRequest = accountDetailListRequest;
    }


    /**
     * Gets the accountDetailListRequest value for this GetAccountDetailList.
     * 
     * @return accountDetailListRequest
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest getAccountDetailListRequest() {
        return accountDetailListRequest;
    }


    /**
     * Sets the accountDetailListRequest value for this GetAccountDetailList.
     * 
     * @param accountDetailListRequest
     */
    public void setAccountDetailListRequest(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequest accountDetailListRequest) {
        this.accountDetailListRequest = accountDetailListRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAccountDetailList)) return false;
        GetAccountDetailList other = (GetAccountDetailList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accountDetailListRequest==null && other.getAccountDetailListRequest()==null) || 
             (this.accountDetailListRequest!=null &&
              this.accountDetailListRequest.equals(other.getAccountDetailListRequest())));
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
        if (getAccountDetailListRequest() != null) {
            _hashCode += getAccountDetailListRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAccountDetailList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetailList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountDetailListRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountDetailListRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequest"));
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
