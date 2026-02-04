/**
 * InternalAccountMatch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class InternalAccountMatch  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString bCgovAccountType;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString initials;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString city;

    public InternalAccountMatch() {
    }

    public InternalAccountMatch(
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString bCgovAccountType,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString initials,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString city) {
           this.bCgovAccountType = bCgovAccountType;
           this.userId = userId;
           this.firstName = firstName;
           this.lastName = lastName;
           this.initials = initials;
           this.email = email;
           this.telephone = telephone;
           this.city = city;
    }


    /**
     * Gets the bCgovAccountType value for this InternalAccountMatch.
     * 
     * @return bCgovAccountType
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getBCgovAccountType() {
        return bCgovAccountType;
    }


    /**
     * Sets the bCgovAccountType value for this InternalAccountMatch.
     * 
     * @param bCgovAccountType
     */
    public void setBCgovAccountType(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString bCgovAccountType) {
        this.bCgovAccountType = bCgovAccountType;
    }


    /**
     * Gets the userId value for this InternalAccountMatch.
     * 
     * @return userId
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this InternalAccountMatch.
     * 
     * @param userId
     */
    public void setUserId(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId) {
        this.userId = userId;
    }


    /**
     * Gets the firstName value for this InternalAccountMatch.
     * 
     * @return firstName
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getFirstName() {
        return firstName;
    }


    /**
     * Sets the firstName value for this InternalAccountMatch.
     * 
     * @param firstName
     */
    public void setFirstName(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName) {
        this.firstName = firstName;
    }


    /**
     * Gets the lastName value for this InternalAccountMatch.
     * 
     * @return lastName
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getLastName() {
        return lastName;
    }


    /**
     * Sets the lastName value for this InternalAccountMatch.
     * 
     * @param lastName
     */
    public void setLastName(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName) {
        this.lastName = lastName;
    }


    /**
     * Gets the initials value for this InternalAccountMatch.
     * 
     * @return initials
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getInitials() {
        return initials;
    }


    /**
     * Sets the initials value for this InternalAccountMatch.
     * 
     * @param initials
     */
    public void setInitials(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString initials) {
        this.initials = initials;
    }


    /**
     * Gets the email value for this InternalAccountMatch.
     * 
     * @return email
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getEmail() {
        return email;
    }


    /**
     * Sets the email value for this InternalAccountMatch.
     * 
     * @param email
     */
    public void setEmail(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email) {
        this.email = email;
    }


    /**
     * Gets the telephone value for this InternalAccountMatch.
     * 
     * @return telephone
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getTelephone() {
        return telephone;
    }


    /**
     * Sets the telephone value for this InternalAccountMatch.
     * 
     * @param telephone
     */
    public void setTelephone(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone) {
        this.telephone = telephone;
    }


    /**
     * Gets the city value for this InternalAccountMatch.
     * 
     * @return city
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getCity() {
        return city;
    }


    /**
     * Sets the city value for this InternalAccountMatch.
     * 
     * @param city
     */
    public void setCity(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString city) {
        this.city = city;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InternalAccountMatch)) return false;
        InternalAccountMatch other = (InternalAccountMatch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bCgovAccountType==null && other.getBCgovAccountType()==null) || 
             (this.bCgovAccountType!=null &&
              this.bCgovAccountType.equals(other.getBCgovAccountType()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.firstName==null && other.getFirstName()==null) || 
             (this.firstName!=null &&
              this.firstName.equals(other.getFirstName()))) &&
            ((this.lastName==null && other.getLastName()==null) || 
             (this.lastName!=null &&
              this.lastName.equals(other.getLastName()))) &&
            ((this.initials==null && other.getInitials()==null) || 
             (this.initials!=null &&
              this.initials.equals(other.getInitials()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.telephone==null && other.getTelephone()==null) || 
             (this.telephone!=null &&
              this.telephone.equals(other.getTelephone()))) &&
            ((this.city==null && other.getCity()==null) || 
             (this.city!=null &&
              this.city.equals(other.getCity())));
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
        if (getBCgovAccountType() != null) {
            _hashCode += getBCgovAccountType().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getFirstName() != null) {
            _hashCode += getFirstName().hashCode();
        }
        if (getLastName() != null) {
            _hashCode += getLastName().hashCode();
        }
        if (getInitials() != null) {
            _hashCode += getInitials().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getTelephone() != null) {
            _hashCode += getTelephone().hashCode();
        }
        if (getCity() != null) {
            _hashCode += getCity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InternalAccountMatch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "InternalAccountMatch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BCgovAccountType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "bCgovAccountType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "firstName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "lastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initials");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "initials"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telephone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "telephone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("city");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "city"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
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
