/**
 * BCeIDAddress.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDAddress  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine1;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine2;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString city;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString postal;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString province;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString country;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDString unstructuredAddress;

    public BCeIDAddress() {
    }

    public BCeIDAddress(
           ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine1,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine2,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString city,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString postal,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString province,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString country,
           ca.bc.gov.srm.farm.subscription.client.BCeIDString unstructuredAddress) {
           this.addressLine1 = addressLine1;
           this.addressLine2 = addressLine2;
           this.city = city;
           this.postal = postal;
           this.province = province;
           this.country = country;
           this.unstructuredAddress = unstructuredAddress;
    }


    /**
     * Gets the addressLine1 value for this BCeIDAddress.
     * 
     * @return addressLine1
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getAddressLine1() {
        return addressLine1;
    }


    /**
     * Sets the addressLine1 value for this BCeIDAddress.
     * 
     * @param addressLine1
     */
    public void setAddressLine1(ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine1) {
        this.addressLine1 = addressLine1;
    }


    /**
     * Gets the addressLine2 value for this BCeIDAddress.
     * 
     * @return addressLine2
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getAddressLine2() {
        return addressLine2;
    }


    /**
     * Sets the addressLine2 value for this BCeIDAddress.
     * 
     * @param addressLine2
     */
    public void setAddressLine2(ca.bc.gov.srm.farm.subscription.client.BCeIDString addressLine2) {
        this.addressLine2 = addressLine2;
    }


    /**
     * Gets the city value for this BCeIDAddress.
     * 
     * @return city
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getCity() {
        return city;
    }


    /**
     * Sets the city value for this BCeIDAddress.
     * 
     * @param city
     */
    public void setCity(ca.bc.gov.srm.farm.subscription.client.BCeIDString city) {
        this.city = city;
    }


    /**
     * Gets the postal value for this BCeIDAddress.
     * 
     * @return postal
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getPostal() {
        return postal;
    }


    /**
     * Sets the postal value for this BCeIDAddress.
     * 
     * @param postal
     */
    public void setPostal(ca.bc.gov.srm.farm.subscription.client.BCeIDString postal) {
        this.postal = postal;
    }


    /**
     * Gets the province value for this BCeIDAddress.
     * 
     * @return province
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getProvince() {
        return province;
    }


    /**
     * Sets the province value for this BCeIDAddress.
     * 
     * @param province
     */
    public void setProvince(ca.bc.gov.srm.farm.subscription.client.BCeIDString province) {
        this.province = province;
    }


    /**
     * Gets the country value for this BCeIDAddress.
     * 
     * @return country
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getCountry() {
        return country;
    }


    /**
     * Sets the country value for this BCeIDAddress.
     * 
     * @param country
     */
    public void setCountry(ca.bc.gov.srm.farm.subscription.client.BCeIDString country) {
        this.country = country;
    }


    /**
     * Gets the unstructuredAddress value for this BCeIDAddress.
     * 
     * @return unstructuredAddress
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDString getUnstructuredAddress() {
        return unstructuredAddress;
    }


    /**
     * Sets the unstructuredAddress value for this BCeIDAddress.
     * 
     * @param unstructuredAddress
     */
    public void setUnstructuredAddress(ca.bc.gov.srm.farm.subscription.client.BCeIDString unstructuredAddress) {
        this.unstructuredAddress = unstructuredAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDAddress)) return false;
        BCeIDAddress other = (BCeIDAddress) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.addressLine1==null && other.getAddressLine1()==null) || 
             (this.addressLine1!=null &&
              this.addressLine1.equals(other.getAddressLine1()))) &&
            ((this.addressLine2==null && other.getAddressLine2()==null) || 
             (this.addressLine2!=null &&
              this.addressLine2.equals(other.getAddressLine2()))) &&
            ((this.city==null && other.getCity()==null) || 
             (this.city!=null &&
              this.city.equals(other.getCity()))) &&
            ((this.postal==null && other.getPostal()==null) || 
             (this.postal!=null &&
              this.postal.equals(other.getPostal()))) &&
            ((this.province==null && other.getProvince()==null) || 
             (this.province!=null &&
              this.province.equals(other.getProvince()))) &&
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry()))) &&
            ((this.unstructuredAddress==null && other.getUnstructuredAddress()==null) || 
             (this.unstructuredAddress!=null &&
              this.unstructuredAddress.equals(other.getUnstructuredAddress())));
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
        if (getAddressLine1() != null) {
            _hashCode += getAddressLine1().hashCode();
        }
        if (getAddressLine2() != null) {
            _hashCode += getAddressLine2().hashCode();
        }
        if (getCity() != null) {
            _hashCode += getCity().hashCode();
        }
        if (getPostal() != null) {
            _hashCode += getPostal().hashCode();
        }
        if (getProvince() != null) {
            _hashCode += getProvince().hashCode();
        }
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getUnstructuredAddress() != null) {
            _hashCode += getUnstructuredAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDAddress.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAddress"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addressLine1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "addressLine1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addressLine2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "addressLine2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("city");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "city"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "postal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("province");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "province"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unstructuredAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "unstructuredAddress"));
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
