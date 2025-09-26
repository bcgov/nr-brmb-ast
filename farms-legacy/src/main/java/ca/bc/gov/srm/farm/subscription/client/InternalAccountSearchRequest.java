/**
 * InternalAccountSearchRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class InternalAccountSearchRequest  extends ca.bc.gov.srm.farm.subscription.client.SearchRequestBase  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.SortOfSortInternalAccountOnProperty sort;

    private ca.bc.gov.srm.farm.subscription.client.InternalAccountMatch accountMatch;

    public InternalAccountSearchRequest() {
    }

    public InternalAccountSearchRequest(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           ca.bc.gov.srm.farm.subscription.client.PaginationRequest pagination,
           ca.bc.gov.srm.farm.subscription.client.SortOfSortInternalAccountOnProperty sort,
           ca.bc.gov.srm.farm.subscription.client.InternalAccountMatch accountMatch) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid,
            pagination);
        this.sort = sort;
        this.accountMatch = accountMatch;
    }


    /**
     * Gets the sort value for this InternalAccountSearchRequest.
     * 
     * @return sort
     */
    public ca.bc.gov.srm.farm.subscription.client.SortOfSortInternalAccountOnProperty getSort() {
        return sort;
    }


    /**
     * Sets the sort value for this InternalAccountSearchRequest.
     * 
     * @param sort
     */
    public void setSort(ca.bc.gov.srm.farm.subscription.client.SortOfSortInternalAccountOnProperty sort) {
        this.sort = sort;
    }


    /**
     * Gets the accountMatch value for this InternalAccountSearchRequest.
     * 
     * @return accountMatch
     */
    public ca.bc.gov.srm.farm.subscription.client.InternalAccountMatch getAccountMatch() {
        return accountMatch;
    }


    /**
     * Sets the accountMatch value for this InternalAccountSearchRequest.
     * 
     * @param accountMatch
     */
    public void setAccountMatch(ca.bc.gov.srm.farm.subscription.client.InternalAccountMatch accountMatch) {
        this.accountMatch = accountMatch;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InternalAccountSearchRequest)) return false;
        InternalAccountSearchRequest other = (InternalAccountSearchRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.sort==null && other.getSort()==null) || 
             (this.sort!=null &&
              this.sort.equals(other.getSort()))) &&
            ((this.accountMatch==null && other.getAccountMatch()==null) || 
             (this.accountMatch!=null &&
              this.accountMatch.equals(other.getAccountMatch())));
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
        if (getSort() != null) {
            _hashCode += getSort().hashCode();
        }
        if (getAccountMatch() != null) {
            _hashCode += getAccountMatch().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InternalAccountSearchRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountSearchRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sort");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "sort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortOfSortInternalAccountOnProperty"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountMatch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountMatch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountMatch"));
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
