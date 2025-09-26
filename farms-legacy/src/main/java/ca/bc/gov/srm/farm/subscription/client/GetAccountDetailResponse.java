/**
 * GetAccountDetailResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class GetAccountDetailResponse  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetailResult;

    public GetAccountDetailResponse() {
    }

    public GetAccountDetailResponse(
           ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetailResult) {
           this.getAccountDetailResult = getAccountDetailResult;
    }


    /**
     * Gets the getAccountDetailResult value for this GetAccountDetailResponse.
     * 
     * @return getAccountDetailResult
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getGetAccountDetailResult() {
        return getAccountDetailResult;
    }


    /**
     * Sets the getAccountDetailResult value for this GetAccountDetailResponse.
     * 
     * @param getAccountDetailResult
     */
    public void setGetAccountDetailResult(ca.bc.gov.srm.farm.subscription.client.AccountDetailResponse getAccountDetailResult) {
        this.getAccountDetailResult = getAccountDetailResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAccountDetailResponse)) return false;
        GetAccountDetailResponse other = (GetAccountDetailResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAccountDetailResult==null && other.getGetAccountDetailResult()==null) || 
             (this.getAccountDetailResult!=null &&
              this.getAccountDetailResult.equals(other.getGetAccountDetailResult())));
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
        if (getGetAccountDetailResult() != null) {
            _hashCode += getGetAccountDetailResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAccountDetailResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">getAccountDetailResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAccountDetailResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "getAccountDetailResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailResponse"));
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
