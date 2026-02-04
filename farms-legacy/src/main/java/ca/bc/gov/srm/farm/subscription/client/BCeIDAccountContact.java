/**
 * BCeIDAccountContact.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDAccountContact  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceType preference;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString email;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString telephone;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString preferredName;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString department;

    public BCeIDAccountContact() {
    }

    public BCeIDAccountContact(
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceType preference,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString email,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString telephone,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString preferredName,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString department) {
           this.preference = preference;
           this.email = email;
           this.telephone = telephone;
           this.address = address;
           this.preferredName = preferredName;
           this.department = department;
    }


    /**
     * Gets the preference value for this BCeIDAccountContact.
     * 
     * @return preference
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceType getPreference() {
        return preference;
    }


    /**
     * Sets the preference value for this BCeIDAccountContact.
     * 
     * @param preference
     */
    public void setPreference(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountContactPreferenceType preference) {
        this.preference = preference;
    }


    /**
     * Gets the email value for this BCeIDAccountContact.
     * 
     * @return email
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getEmail() {
        return email;
    }


    /**
     * Sets the email value for this BCeIDAccountContact.
     * 
     * @param email
     */
    public void setEmail(ca.bc.gov.srm.farm.subscription.client.BCeIDString email) {
        this.email = email;
    }


    /**
     * Gets the telephone value for this BCeIDAccountContact.
     * 
     * @return telephone
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getTelephone() {
        return telephone;
    }


    /**
     * Sets the telephone value for this BCeIDAccountContact.
     * 
     * @param telephone
     */
    public void setTelephone(ca.bc.gov.srm.farm.subscription.client.BCeIDString telephone) {
        this.telephone = telephone;
    }


    /**
     * Gets the address value for this BCeIDAccountContact.
     * 
     * @return address
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAddress getAddress() {
        return address;
    }


    /**
     * Sets the address value for this BCeIDAccountContact.
     * 
     * @param address
     */
    public void setAddress(ca.bc.gov.srm.farm.subscription.client.BCeIDAddress address) {
        this.address = address;
    }


    /**
     * Gets the preferredName value for this BCeIDAccountContact.
     * 
     * @return preferredName
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getPreferredName() {
        return preferredName;
    }


    /**
     * Sets the preferredName value for this BCeIDAccountContact.
     * 
     * @param preferredName
     */
    public void setPreferredName(ca.bc.gov.srm.farm.subscription.client.BCeIDString preferredName) {
        this.preferredName = preferredName;
    }


    /**
     * Gets the department value for this BCeIDAccountContact.
     * 
     * @return department
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getDepartment() {
        return department;
    }


    /**
     * Sets the department value for this BCeIDAccountContact.
     * 
     * @param department
     */
    public void setDepartment(ca.bc.gov.srm.farm.subscription.client.BCeIDString department) {
        this.department = department;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDAccountContact)) return false;
        BCeIDAccountContact other = (BCeIDAccountContact) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.preference==null && other.getPreference()==null) || 
             (this.preference!=null &&
              this.preference.equals(other.getPreference()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.telephone==null && other.getTelephone()==null) || 
             (this.telephone!=null &&
              this.telephone.equals(other.getTelephone()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.preferredName==null && other.getPreferredName()==null) || 
             (this.preferredName!=null &&
              this.preferredName.equals(other.getPreferredName()))) &&
            ((this.department==null && other.getDepartment()==null) || 
             (this.department!=null &&
              this.department.equals(other.getDepartment())));
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
        if (getPreference() != null) {
            _hashCode += getPreference().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getTelephone() != null) {
            _hashCode += getTelephone().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getPreferredName() != null) {
            _hashCode += getPreferredName().hashCode();
        }
        if (getDepartment() != null) {
            _hashCode += getDepartment().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDAccountContact.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContact"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "preference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountContactPreferenceType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telephone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "telephone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "preferredName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("department");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "department"));
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
