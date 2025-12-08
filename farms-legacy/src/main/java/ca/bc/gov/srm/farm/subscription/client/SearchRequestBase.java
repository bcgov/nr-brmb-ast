/**
 * SearchRequestBase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SearchRequestBase  extends ca.bc.gov.srm.farm.subscription.client.RequestBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.PaginationRequest pagination;

    public SearchRequestBase() {
    }

    public SearchRequestBase(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           ca.bc.gov.srm.farm.subscription.client.PaginationRequest pagination) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid);
        this.pagination = pagination;
    }


    /**
     * Gets the pagination value for this SearchRequestBase.
     * 
     * @return pagination
     */
    public ca.bc.gov.srm.farm.subscription.client.PaginationRequest getPagination() {
        return pagination;
    }


    /**
     * Sets the pagination value for this SearchRequestBase.
     * 
     * @param pagination
     */
    public void setPagination(ca.bc.gov.srm.farm.subscription.client.PaginationRequest pagination) {
        this.pagination = pagination;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchRequestBase)) return false;
        SearchRequestBase other = (SearchRequestBase) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.pagination==null && other.getPagination()==null) || 
             (this.pagination!=null &&
              this.pagination.equals(other.getPagination())));
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
        if (getPagination() != null) {
            _hashCode += getPagination().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchRequestBase.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SearchRequestBase"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "pagination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "PaginationRequest"));
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
