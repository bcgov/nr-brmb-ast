/**
 * BCeIDIndividualIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDIndividualIdentity  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDName name;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime dateOfBirth;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAddress residentialAddress;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAddress mailingAddress;

    public BCeIDIndividualIdentity() {
    }

    public BCeIDIndividualIdentity(
           ca.bc.gov.srm.farm.subscription.client.BCeIDName name,
           ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime dateOfBirth,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAddress residentialAddress,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAddress mailingAddress) {
           this.name = name;
           this.dateOfBirth = dateOfBirth;
           this.residentialAddress = residentialAddress;
           this.mailingAddress = mailingAddress;
    }


    /**
     * Gets the name value for this BCeIDIndividualIdentity.
     * 
     * @return name
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDName getName() {
        return name;
    }


    /**
     * Sets the name value for this BCeIDIndividualIdentity.
     * 
     * @param name
     */
    public void setName(ca.bc.gov.srm.farm.subscription.client.BCeIDName name) {
        this.name = name;
    }


    /**
     * Gets the dateOfBirth value for this BCeIDIndividualIdentity.
     * 
     * @return dateOfBirth
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime getDateOfBirth() {
        return dateOfBirth;
    }


    /**
     * Sets the dateOfBirth value for this BCeIDIndividualIdentity.
     * 
     * @param dateOfBirth
     */
    public void setDateOfBirth(ca.bc.gov.srm.farm.subscription.client.BCeIDDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    /**
     * Gets the residentialAddress value for this BCeIDIndividualIdentity.
     * 
     * @return residentialAddress
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAddress getResidentialAddress() {
        return residentialAddress;
    }


    /**
     * Sets the residentialAddress value for this BCeIDIndividualIdentity.
     * 
     * @param residentialAddress
     */
    public void setResidentialAddress(ca.bc.gov.srm.farm.subscription.client.BCeIDAddress residentialAddress) {
        this.residentialAddress = residentialAddress;
    }


    /**
     * Gets the mailingAddress value for this BCeIDIndividualIdentity.
     * 
     * @return mailingAddress
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAddress getMailingAddress() {
        return mailingAddress;
    }


    /**
     * Sets the mailingAddress value for this BCeIDIndividualIdentity.
     * 
     * @param mailingAddress
     */
    public void setMailingAddress(ca.bc.gov.srm.farm.subscription.client.BCeIDAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDIndividualIdentity)) return false;
        BCeIDIndividualIdentity other = (BCeIDIndividualIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.dateOfBirth==null && other.getDateOfBirth()==null) || 
             (this.dateOfBirth!=null &&
              this.dateOfBirth.equals(other.getDateOfBirth()))) &&
            ((this.residentialAddress==null && other.getResidentialAddress()==null) || 
             (this.residentialAddress!=null &&
              this.residentialAddress.equals(other.getResidentialAddress()))) &&
            ((this.mailingAddress==null && other.getMailingAddress()==null) || 
             (this.mailingAddress!=null &&
              this.mailingAddress.equals(other.getMailingAddress())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDateOfBirth() != null) {
            _hashCode += getDateOfBirth().hashCode();
        }
        if (getResidentialAddress() != null) {
            _hashCode += getResidentialAddress().hashCode();
        }
        if (getMailingAddress() != null) {
            _hashCode += getMailingAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDIndividualIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDIndividualIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDName"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOfBirth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "dateOfBirth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDDateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("residentialAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "residentialAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mailingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "mailingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress"));
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
