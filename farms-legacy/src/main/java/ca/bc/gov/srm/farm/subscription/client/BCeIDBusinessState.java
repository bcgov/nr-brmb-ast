/**
 * BCeIDBusinessState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDBusinessState  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended;

    public BCeIDBusinessState() {
    }

    public BCeIDBusinessState(
           ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended) {
           this.isSuspended = isSuspended;
    }


    /**
     * Gets the isSuspended value for this BCeIDBusinessState.
     * 
     * @return isSuspended
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean getIsSuspended() {
        return isSuspended;
    }


    /**
     * Sets the isSuspended value for this BCeIDBusinessState.
     * 
     * @param isSuspended
     */
    public void setIsSuspended(ca.bc.gov.srm.farm.subscription.client.BCeIDBoolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDBusinessState)) return false;
        BCeIDBusinessState other = (BCeIDBusinessState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.isSuspended==null && other.getIsSuspended()==null) || 
             (this.isSuspended!=null &&
              this.isSuspended.equals(other.getIsSuspended())));
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
        if (getIsSuspended() != null) {
            _hashCode += getIsSuspended().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDBusinessState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBusinessState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isSuspended");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "isSuspended"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBoolean"));
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
