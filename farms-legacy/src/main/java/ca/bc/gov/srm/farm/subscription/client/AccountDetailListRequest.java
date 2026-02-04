/**
 * AccountDetailListRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class AccountDetailListRequest  extends ca.bc.gov.srm.farm.subscription.client.RequestBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem[] requestItemList;

    public AccountDetailListRequest() {
    }

    public AccountDetailListRequest(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem[] requestItemList) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid);
        this.requestItemList = requestItemList;
    }


    /**
     * Gets the requestItemList value for this AccountDetailListRequest.
     * 
     * @return requestItemList
     */
    public ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem[] getRequestItemList() {
        return requestItemList;
    }


    /**
     * Sets the requestItemList value for this AccountDetailListRequest.
     * 
     * @param requestItemList
     */
    public void setRequestItemList(ca.bc.gov.srm.farm.subscription.client.AccountDetailListRequestItem[] requestItemList) {
        this.requestItemList = requestItemList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountDetailListRequest)) return false;
        AccountDetailListRequest other = (AccountDetailListRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.requestItemList==null && other.getRequestItemList()==null) || 
             (this.requestItemList!=null &&
              java.util.Arrays.equals(this.requestItemList, other.getRequestItemList())));
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
        if (getRequestItemList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRequestItemList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRequestItemList(), i);
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
        new org.apache.axis.description.TypeDesc(AccountDetailListRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestItemList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requestItemList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequestItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailListRequestItem"));
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
