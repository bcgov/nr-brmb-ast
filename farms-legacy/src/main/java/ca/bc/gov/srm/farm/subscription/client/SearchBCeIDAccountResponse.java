/**
 * SearchBCeIDAccountResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SearchBCeIDAccountResponse  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccountResult;

    public SearchBCeIDAccountResponse() {
    }

    public SearchBCeIDAccountResponse(
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccountResult) {
           this.searchBCeIDAccountResult = searchBCeIDAccountResult;
    }


    /**
     * Gets the searchBCeIDAccountResult value for this SearchBCeIDAccountResponse.
     * 
     * @return searchBCeIDAccountResult
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse getSearchBCeIDAccountResult() {
        return searchBCeIDAccountResult;
    }


    /**
     * Sets the searchBCeIDAccountResult value for this SearchBCeIDAccountResponse.
     * 
     * @param searchBCeIDAccountResult
     */
    public void setSearchBCeIDAccountResult(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountSearchResponse searchBCeIDAccountResult) {
        this.searchBCeIDAccountResult = searchBCeIDAccountResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchBCeIDAccountResponse)) return false;
        SearchBCeIDAccountResponse other = (SearchBCeIDAccountResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.searchBCeIDAccountResult==null && other.getSearchBCeIDAccountResult()==null) || 
             (this.searchBCeIDAccountResult!=null &&
              this.searchBCeIDAccountResult.equals(other.getSearchBCeIDAccountResult())));
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
        if (getSearchBCeIDAccountResult() != null) {
            _hashCode += getSearchBCeIDAccountResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchBCeIDAccountResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">searchBCeIDAccountResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchBCeIDAccountResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchBCeIDAccountResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountSearchResponse"));
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
