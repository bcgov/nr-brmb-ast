/**
 * PaginationResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class PaginationResponse  implements java.io.Serializable {
    private int totalItems;

    private int totalVirtualItems;

    private int requestedPageSize;

    private int requestedPageIndex;

    public PaginationResponse() {
    }

    public PaginationResponse(
           int totalItems,
           int totalVirtualItems,
           int requestedPageSize,
           int requestedPageIndex) {
           this.totalItems = totalItems;
           this.totalVirtualItems = totalVirtualItems;
           this.requestedPageSize = requestedPageSize;
           this.requestedPageIndex = requestedPageIndex;
    }


    /**
     * Gets the totalItems value for this PaginationResponse.
     * 
     * @return totalItems
     */
    public int getTotalItems() {
        return totalItems;
    }


    /**
     * Sets the totalItems value for this PaginationResponse.
     * 
     * @param totalItems
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }


    /**
     * Gets the totalVirtualItems value for this PaginationResponse.
     * 
     * @return totalVirtualItems
     */
    public int getTotalVirtualItems() {
        return totalVirtualItems;
    }


    /**
     * Sets the totalVirtualItems value for this PaginationResponse.
     * 
     * @param totalVirtualItems
     */
    public void setTotalVirtualItems(int totalVirtualItems) {
        this.totalVirtualItems = totalVirtualItems;
    }


    /**
     * Gets the requestedPageSize value for this PaginationResponse.
     * 
     * @return requestedPageSize
     */
    public int getRequestedPageSize() {
        return requestedPageSize;
    }


    /**
     * Sets the requestedPageSize value for this PaginationResponse.
     * 
     * @param requestedPageSize
     */
    public void setRequestedPageSize(int requestedPageSize) {
        this.requestedPageSize = requestedPageSize;
    }


    /**
     * Gets the requestedPageIndex value for this PaginationResponse.
     * 
     * @return requestedPageIndex
     */
    public int getRequestedPageIndex() {
        return requestedPageIndex;
    }


    /**
     * Sets the requestedPageIndex value for this PaginationResponse.
     * 
     * @param requestedPageIndex
     */
    public void setRequestedPageIndex(int requestedPageIndex) {
        this.requestedPageIndex = requestedPageIndex;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PaginationResponse)) return false;
        PaginationResponse other = (PaginationResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.totalItems == other.getTotalItems() &&
            this.totalVirtualItems == other.getTotalVirtualItems() &&
            this.requestedPageSize == other.getRequestedPageSize() &&
            this.requestedPageIndex == other.getRequestedPageIndex();
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
        _hashCode += getTotalItems();
        _hashCode += getTotalVirtualItems();
        _hashCode += getRequestedPageSize();
        _hashCode += getRequestedPageIndex();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PaginationResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "PaginationResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "totalItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalVirtualItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "totalVirtualItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestedPageSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requestedPageSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestedPageIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "requestedPageIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
