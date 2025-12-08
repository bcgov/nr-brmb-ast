/**
 * BCeIDName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDName  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDString firstname;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString middleName;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString otherMiddleName;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString surname;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString initials;

    public BCeIDName() {
    }

    public BCeIDName(
           ca.bc.gov.srm.farm.subscription.client.BCeIDString firstname,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString middleName,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString otherMiddleName,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString surname,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString initials) {
           this.firstname = firstname;
           this.middleName = middleName;
           this.otherMiddleName = otherMiddleName;
           this.surname = surname;
           this.initials = initials;
    }


    /**
     * Gets the firstname value for this BCeIDName.
     * 
     * @return firstname
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getFirstname() {
        return firstname;
    }


    /**
     * Sets the firstname value for this BCeIDName.
     * 
     * @param firstname
     */
    public void setFirstname(ca.bc.gov.srm.farm.subscription.client.BCeIDString firstname) {
        this.firstname = firstname;
    }


    /**
     * Gets the middleName value for this BCeIDName.
     * 
     * @return middleName
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getMiddleName() {
        return middleName;
    }


    /**
     * Sets the middleName value for this BCeIDName.
     * 
     * @param middleName
     */
    public void setMiddleName(ca.bc.gov.srm.farm.subscription.client.BCeIDString middleName) {
        this.middleName = middleName;
    }


    /**
     * Gets the otherMiddleName value for this BCeIDName.
     * 
     * @return otherMiddleName
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getOtherMiddleName() {
        return otherMiddleName;
    }


    /**
     * Sets the otherMiddleName value for this BCeIDName.
     * 
     * @param otherMiddleName
     */
    public void setOtherMiddleName(ca.bc.gov.srm.farm.subscription.client.BCeIDString otherMiddleName) {
        this.otherMiddleName = otherMiddleName;
    }


    /**
     * Gets the surname value for this BCeIDName.
     * 
     * @return surname
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getSurname() {
        return surname;
    }


    /**
     * Sets the surname value for this BCeIDName.
     * 
     * @param surname
     */
    public void setSurname(ca.bc.gov.srm.farm.subscription.client.BCeIDString surname) {
        this.surname = surname;
    }


    /**
     * Gets the initials value for this BCeIDName.
     * 
     * @return initials
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getInitials() {
        return initials;
    }


    /**
     * Sets the initials value for this BCeIDName.
     * 
     * @param initials
     */
    public void setInitials(ca.bc.gov.srm.farm.subscription.client.BCeIDString initials) {
        this.initials = initials;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDName)) return false;
        BCeIDName other = (BCeIDName) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.firstname==null && other.getFirstname()==null) || 
             (this.firstname!=null &&
              this.firstname.equals(other.getFirstname()))) &&
            ((this.middleName==null && other.getMiddleName()==null) || 
             (this.middleName!=null &&
              this.middleName.equals(other.getMiddleName()))) &&
            ((this.otherMiddleName==null && other.getOtherMiddleName()==null) || 
             (this.otherMiddleName!=null &&
              this.otherMiddleName.equals(other.getOtherMiddleName()))) &&
            ((this.surname==null && other.getSurname()==null) || 
             (this.surname!=null &&
              this.surname.equals(other.getSurname()))) &&
            ((this.initials==null && other.getInitials()==null) || 
             (this.initials!=null &&
              this.initials.equals(other.getInitials())));
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
        if (getFirstname() != null) {
            _hashCode += getFirstname().hashCode();
        }
        if (getMiddleName() != null) {
            _hashCode += getMiddleName().hashCode();
        }
        if (getOtherMiddleName() != null) {
            _hashCode += getOtherMiddleName().hashCode();
        }
        if (getSurname() != null) {
            _hashCode += getSurname().hashCode();
        }
        if (getInitials() != null) {
            _hashCode += getInitials().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDName"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "firstname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("middleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "middleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otherMiddleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "otherMiddleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("surname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "surname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initials");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "initials"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
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
