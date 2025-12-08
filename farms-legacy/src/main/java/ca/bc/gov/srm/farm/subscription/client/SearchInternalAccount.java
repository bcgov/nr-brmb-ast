/**
 * SearchInternalAccount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SearchInternalAccount  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest;

    public SearchInternalAccount() {
    }

    public SearchInternalAccount(
           ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) {
           this.internalAccountSearchRequest = internalAccountSearchRequest;
    }


    /**
     * Gets the internalAccountSearchRequest value for this SearchInternalAccount.
     * 
     * @return internalAccountSearchRequest
     */
    public ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest getInternalAccountSearchRequest() {
        return internalAccountSearchRequest;
    }


    /**
     * Sets the internalAccountSearchRequest value for this SearchInternalAccount.
     * 
     * @param internalAccountSearchRequest
     */
    public void setInternalAccountSearchRequest(ca.bc.gov.srm.farm.subscription.client.InternalAccountSearchRequest internalAccountSearchRequest) {
        this.internalAccountSearchRequest = internalAccountSearchRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchInternalAccount)) return false;
        SearchInternalAccount other = (SearchInternalAccount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.internalAccountSearchRequest==null && other.getInternalAccountSearchRequest()==null) || 
             (this.internalAccountSearchRequest!=null &&
              this.internalAccountSearchRequest.equals(other.getInternalAccountSearchRequest())));
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
        if (getInternalAccountSearchRequest() != null) {
            _hashCode += getInternalAccountSearchRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchInternalAccount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchInternalAccount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalAccountSearchRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "internalAccountSearchRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchRequest"));
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
