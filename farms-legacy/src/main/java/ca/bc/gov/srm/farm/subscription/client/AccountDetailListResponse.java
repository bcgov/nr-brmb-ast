/**
 * AccountDetailListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class AccountDetailListResponse  extends ca.bc.gov.srm.farm.subscription.client.ResponseBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem[] responseItemList;

    public AccountDetailListResponse() {
    }

    public AccountDetailListResponse(
           ca.bc.gov.srm.farm.subscription.client.ResponseCode code,
           ca.bc.gov.srm.farm.subscription.client.FailureCode failureCode,
           java.lang.String message,
           ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem[] responseItemList) {
        super(
            code,
            failureCode,
            message);
        this.responseItemList = responseItemList;
    }


    /**
     * Gets the responseItemList value for this AccountDetailListResponse.
     * 
     * @return responseItemList
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem[] getResponseItemList() {
        return responseItemList;
    }


    /**
     * Sets the responseItemList value for this AccountDetailListResponse.
     * 
     * @param responseItemList
     */
    public void setResponseItemList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListResponseItem[] responseItemList) {
        this.responseItemList = responseItemList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountDetailListResponse)) return false;
        AccountDetailListResponse other = (AccountDetailListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.responseItemList==null && other.getResponseItemList()==null) || 
             (this.responseItemList!=null &&
              java.util.Arrays.equals(this.responseItemList, other.getResponseItemList())));
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
        if (getResponseItemList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResponseItemList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResponseItemList(), i);
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
        new org.apache.axis.description.TypeDesc(AccountDetailListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseItemList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "responseItemList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListResponseItem"));
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
