/**
 * InternalAccountSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class InternalAccountSearchResponse  extends ca.bc.gov.srm.farm.subscription.client.SearchResponseBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccount[] accountList;

    public InternalAccountSearchResponse() {
    }

    public InternalAccountSearchResponse(
           ca.bc.gov.srm.farm.subscription.client.ResponseCode code,
           ca.bc.gov.srm.farm.subscription.client.FailureCode failureCode,
           java.lang.String message,
           ca.bc.gov.srm.farm.subscription.client.PaginationResponse pagination,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccount[] accountList) {
        super(
            code,
            failureCode,
            message,
            pagination);
        this.accountList = accountList;
    }


    /**
     * Gets the accountList value for this InternalAccountSearchResponse.
     * 
     * @return accountList
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccount[] getAccountList() {
        return accountList;
    }


    /**
     * Sets the accountList value for this InternalAccountSearchResponse.
     * 
     * @param accountList
     */
    public void setAccountList(ca.bc.gov.srm.farm.subscription.client.BCeIDAccount[] accountList) {
        this.accountList = accountList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InternalAccountSearchResponse)) return false;
        InternalAccountSearchResponse other = (InternalAccountSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.accountList==null && other.getAccountList()==null) || 
             (this.accountList!=null &&
              java.util.Arrays.equals(this.accountList, other.getAccountList())));
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
        if (getAccountList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAccountList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAccountList(), i);
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
        new org.apache.axis.description.TypeDesc(InternalAccountSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount"));
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
