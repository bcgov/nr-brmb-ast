/**
 * BCeIDAccountMatch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDAccountMatch  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName1;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName2;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone;

    private java.util.Calendar birthDate;

    private java.lang.String acctLuid;

    private java.lang.String acctGuid;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString knownAs;

    private ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString department;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableAccountType searchableAccountType;

    public BCeIDAccountMatch() {
    }

    public BCeIDAccountMatch(
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName1,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName2,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone,
           java.util.Calendar birthDate,
           java.lang.String acctLuid,
           java.lang.String acctGuid,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString knownAs,
           ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString department,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableAccountType searchableAccountType) {
           this.userId = userId;
           this.firstName = firstName;
           this.lastName = lastName;
           this.middleName1 = middleName1;
           this.middleName2 = middleName2;
           this.email = email;
           this.telephone = telephone;
           this.birthDate = birthDate;
           this.acctLuid = acctLuid;
           this.acctGuid = acctGuid;
           this.knownAs = knownAs;
           this.department = department;
           this.searchableAccountType = searchableAccountType;
    }


    /**
     * Gets the userId value for this BCeIDAccountMatch.
     * 
     * @return userId
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this BCeIDAccountMatch.
     * 
     * @param userId
     */
    public void setUserId(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString userId) {
        this.userId = userId;
    }


    /**
     * Gets the firstName value for this BCeIDAccountMatch.
     * 
     * @return firstName
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getFirstName() {
        return firstName;
    }


    /**
     * Sets the firstName value for this BCeIDAccountMatch.
     * 
     * @param firstName
     */
    public void setFirstName(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString firstName) {
        this.firstName = firstName;
    }


    /**
     * Gets the lastName value for this BCeIDAccountMatch.
     * 
     * @return lastName
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getLastName() {
        return lastName;
    }


    /**
     * Sets the lastName value for this BCeIDAccountMatch.
     * 
     * @param lastName
     */
    public void setLastName(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString lastName) {
        this.lastName = lastName;
    }


    /**
     * Gets the middleName1 value for this BCeIDAccountMatch.
     * 
     * @return middleName1
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getMiddleName1() {
        return middleName1;
    }


    /**
     * Sets the middleName1 value for this BCeIDAccountMatch.
     * 
     * @param middleName1
     */
    public void setMiddleName1(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName1) {
        this.middleName1 = middleName1;
    }


    /**
     * Gets the middleName2 value for this BCeIDAccountMatch.
     * 
     * @return middleName2
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getMiddleName2() {
        return middleName2;
    }


    /**
     * Sets the middleName2 value for this BCeIDAccountMatch.
     * 
     * @param middleName2
     */
    public void setMiddleName2(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString middleName2) {
        this.middleName2 = middleName2;
    }


    /**
     * Gets the email value for this BCeIDAccountMatch.
     * 
     * @return email
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getEmail() {
        return email;
    }


    /**
     * Sets the email value for this BCeIDAccountMatch.
     * 
     * @param email
     */
    public void setEmail(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString email) {
        this.email = email;
    }


    /**
     * Gets the telephone value for this BCeIDAccountMatch.
     * 
     * @return telephone
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getTelephone() {
        return telephone;
    }


    /**
     * Sets the telephone value for this BCeIDAccountMatch.
     * 
     * @param telephone
     */
    public void setTelephone(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString telephone) {
        this.telephone = telephone;
    }


    /**
     * Gets the birthDate value for this BCeIDAccountMatch.
     * 
     * @return birthDate
     */
    public java.util.Calendar getBirthDate() {
        return birthDate;
    }


    /**
     * Sets the birthDate value for this BCeIDAccountMatch.
     * 
     * @param birthDate
     */
    public void setBirthDate(java.util.Calendar birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Gets the acctLuid value for this BCeIDAccountMatch.
     * 
     * @return acctLuid
     */
    public java.lang.String getAcctLuid() {
        return acctLuid;
    }


    /**
     * Sets the acctLuid value for this BCeIDAccountMatch.
     * 
     * @param acctLuid
     */
    public void setAcctLuid(java.lang.String acctLuid) {
        this.acctLuid = acctLuid;
    }


    /**
     * Gets the acctGuid value for this BCeIDAccountMatch.
     * 
     * @return acctGuid
     */
    public java.lang.String getAcctGuid() {
        return acctGuid;
    }


    /**
     * Sets the acctGuid value for this BCeIDAccountMatch.
     * 
     * @param acctGuid
     */
    public void setAcctGuid(java.lang.String acctGuid) {
        this.acctGuid = acctGuid;
    }


    /**
     * Gets the knownAs value for this BCeIDAccountMatch.
     * 
     * @return knownAs
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getKnownAs() {
        return knownAs;
    }


    /**
     * Sets the knownAs value for this BCeIDAccountMatch.
     * 
     * @param knownAs
     */
    public void setKnownAs(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString knownAs) {
        this.knownAs = knownAs;
    }


    /**
     * Gets the department value for this BCeIDAccountMatch.
     * 
     * @return department
     */
    public ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString getDepartment() {
        return department;
    }


    /**
     * Sets the department value for this BCeIDAccountMatch.
     * 
     * @param department
     */
    public void setDepartment(ca.bc.gov.srm.farm.subscription.client.MatchPropertyOfString department) {
        this.department = department;
    }


    /**
     * Gets the searchableAccountType value for this BCeIDAccountMatch.
     * 
     * @return searchableAccountType
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableAccountType getSearchableAccountType() {
        return searchableAccountType;
    }


    /**
     * Sets the searchableAccountType value for this BCeIDAccountMatch.
     * 
     * @param searchableAccountType
     */
    public void setSearchableAccountType(ca.bc.gov.srm.farm.subscription.client.BCeIDSearchableAccountType searchableAccountType) {
        this.searchableAccountType = searchableAccountType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDAccountMatch)) return false;
        BCeIDAccountMatch other = (BCeIDAccountMatch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.firstName==null && other.getFirstName()==null) || 
             (this.firstName!=null &&
              this.firstName.equals(other.getFirstName()))) &&
            ((this.lastName==null && other.getLastName()==null) || 
             (this.lastName!=null &&
              this.lastName.equals(other.getLastName()))) &&
            ((this.middleName1==null && other.getMiddleName1()==null) || 
             (this.middleName1!=null &&
              this.middleName1.equals(other.getMiddleName1()))) &&
            ((this.middleName2==null && other.getMiddleName2()==null) || 
             (this.middleName2!=null &&
              this.middleName2.equals(other.getMiddleName2()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.telephone==null && other.getTelephone()==null) || 
             (this.telephone!=null &&
              this.telephone.equals(other.getTelephone()))) &&
            ((this.birthDate==null && other.getBirthDate()==null) || 
             (this.birthDate!=null &&
              this.birthDate.equals(other.getBirthDate()))) &&
            ((this.acctLuid==null && other.getAcctLuid()==null) || 
             (this.acctLuid!=null &&
              this.acctLuid.equals(other.getAcctLuid()))) &&
            ((this.acctGuid==null && other.getAcctGuid()==null) || 
             (this.acctGuid!=null &&
              this.acctGuid.equals(other.getAcctGuid()))) &&
            ((this.knownAs==null && other.getKnownAs()==null) || 
             (this.knownAs!=null &&
              this.knownAs.equals(other.getKnownAs()))) &&
            ((this.department==null && other.getDepartment()==null) || 
             (this.department!=null &&
              this.department.equals(other.getDepartment()))) &&
            ((this.searchableAccountType==null && other.getSearchableAccountType()==null) || 
             (this.searchableAccountType!=null &&
              this.searchableAccountType.equals(other.getSearchableAccountType())));
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
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getFirstName() != null) {
            _hashCode += getFirstName().hashCode();
        }
        if (getLastName() != null) {
            _hashCode += getLastName().hashCode();
        }
        if (getMiddleName1() != null) {
            _hashCode += getMiddleName1().hashCode();
        }
        if (getMiddleName2() != null) {
            _hashCode += getMiddleName2().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getTelephone() != null) {
            _hashCode += getTelephone().hashCode();
        }
        if (getBirthDate() != null) {
            _hashCode += getBirthDate().hashCode();
        }
        if (getAcctLuid() != null) {
            _hashCode += getAcctLuid().hashCode();
        }
        if (getAcctGuid() != null) {
            _hashCode += getAcctGuid().hashCode();
        }
        if (getKnownAs() != null) {
            _hashCode += getKnownAs().hashCode();
        }
        if (getDepartment() != null) {
            _hashCode += getDepartment().hashCode();
        }
        if (getSearchableAccountType() != null) {
            _hashCode += getSearchableAccountType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDAccountMatch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountMatch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("middleName1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "middleName1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("middleName2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "middleName2"));
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
        elemField.setFieldName("birthDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "birthDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acctLuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "acctLuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acctGuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "acctGuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("knownAs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "knownAs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("department");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "department"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "MatchPropertyOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchableAccountType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "searchableAccountType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSearchableAccountType"));
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
