/**
 * SortOfSortInternalAccountOnProperty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SortOfSortInternalAccountOnProperty  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.SortDirection direction;

    private ca.bc.gov.srm.farm.subscription.client.SortInternalAccountOnProperty onProperty;

    public SortOfSortInternalAccountOnProperty() {
    }

    public SortOfSortInternalAccountOnProperty(
           ca.bc.gov.srm.farm.subscription.client.SortDirection direction,
           ca.bc.gov.srm.farm.subscription.client.SortInternalAccountOnProperty onProperty) {
           this.direction = direction;
           this.onProperty = onProperty;
    }


    /**
     * Gets the direction value for this SortOfSortInternalAccountOnProperty.
     * 
     * @return direction
     */
    public ca.bc.gov.srm.farm.subscription.client.SortDirection getDirection() {
        return direction;
    }


    /**
     * Sets the direction value for this SortOfSortInternalAccountOnProperty.
     * 
     * @param direction
     */
    public void setDirection(ca.bc.gov.srm.farm.subscription.client.SortDirection direction) {
        this.direction = direction;
    }


    /**
     * Gets the onProperty value for this SortOfSortInternalAccountOnProperty.
     * 
     * @return onProperty
     */
    public ca.bc.gov.srm.farm.subscription.client.SortInternalAccountOnProperty getOnProperty() {
        return onProperty;
    }


    /**
     * Sets the onProperty value for this SortOfSortInternalAccountOnProperty.
     * 
     * @param onProperty
     */
    public void setOnProperty(ca.bc.gov.srm.farm.subscription.client.SortInternalAccountOnProperty onProperty) {
        this.onProperty = onProperty;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SortOfSortInternalAccountOnProperty)) return false;
        SortOfSortInternalAccountOnProperty other = (SortOfSortInternalAccountOnProperty) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.direction==null && other.getDirection()==null) || 
             (this.direction!=null &&
              this.direction.equals(other.getDirection()))) &&
            ((this.onProperty==null && other.getOnProperty()==null) || 
             (this.onProperty!=null &&
              this.onProperty.equals(other.getOnProperty())));
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
        if (getDirection() != null) {
            _hashCode += getDirection().hashCode();
        }
        if (getOnProperty() != null) {
            _hashCode += getOnProperty().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SortOfSortInternalAccountOnProperty.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortOfSortInternalAccountOnProperty"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "direction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortDirection"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onProperty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "onProperty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortInternalAccountOnProperty"));
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
