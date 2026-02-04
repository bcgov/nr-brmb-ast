/**
 * MatchPropertyOfString.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class MatchPropertyOfString  implements java.io.Serializable {
    private java.lang.String value;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyUsing matchPropertyUsing;

    public MatchPropertyOfString() {
    }

    public MatchPropertyOfString(
           java.lang.String value,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyUsing matchPropertyUsing) {
           this.value = value;
           this.matchPropertyUsing = matchPropertyUsing;
    }


    /**
     * Gets the value value for this MatchPropertyOfString.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this MatchPropertyOfString.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }


    /**
     * Gets the matchPropertyUsing value for this MatchPropertyOfString.
     * 
     * @return matchPropertyUsing
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyUsing getMatchPropertyUsing() {
        return matchPropertyUsing;
    }


    /**
     * Sets the matchPropertyUsing value for this MatchPropertyOfString.
     * 
     * @param matchPropertyUsing
     */
    public void setMatchPropertyUsing(ca.bc.gov.srm.farm.subscription.client.MatchPropertyUsing matchPropertyUsing) {
        this.matchPropertyUsing = matchPropertyUsing;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MatchPropertyOfString)) return false;
        MatchPropertyOfString other = (MatchPropertyOfString) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.matchPropertyUsing==null && other.getMatchPropertyUsing()==null) || 
             (this.matchPropertyUsing!=null &&
              this.matchPropertyUsing.equals(other.getMatchPropertyUsing())));
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
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getMatchPropertyUsing() != null) {
            _hashCode += getMatchPropertyUsing().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MatchPropertyOfString.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchPropertyUsing");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "matchPropertyUsing"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyUsing"));
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
