/**
 * SortBCeIDAccountOnProperty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SortBCeIDAccountOnProperty implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SortBCeIDAccountOnProperty(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Void = "Void";
    public static final java.lang.String _UserId = "UserId";
    public static final java.lang.String _Firstname = "Firstname";
    public static final java.lang.String _LastName = "LastName";
    public static final java.lang.String _MiddleName1 = "MiddleName1";
    public static final java.lang.String _MiddleName2 = "MiddleName2";
    public static final java.lang.String _Email = "Email";
    public static final java.lang.String _Telephone = "Telephone";
    public static final java.lang.String _Department = "Department";
    public static final java.lang.String _KnownAs = "KnownAs";
    public static final java.lang.String _BirthDate = "BirthDate";
    public static final SortBCeIDAccountOnProperty Void = new SortBCeIDAccountOnProperty(_Void);
    public static final SortBCeIDAccountOnProperty UserId = new SortBCeIDAccountOnProperty(_UserId);
    public static final SortBCeIDAccountOnProperty Firstname = new SortBCeIDAccountOnProperty(_Firstname);
    public static final SortBCeIDAccountOnProperty LastName = new SortBCeIDAccountOnProperty(_LastName);
    public static final SortBCeIDAccountOnProperty MiddleName1 = new SortBCeIDAccountOnProperty(_MiddleName1);
    public static final SortBCeIDAccountOnProperty MiddleName2 = new SortBCeIDAccountOnProperty(_MiddleName2);
    public static final SortBCeIDAccountOnProperty Email = new SortBCeIDAccountOnProperty(_Email);
    public static final SortBCeIDAccountOnProperty Telephone = new SortBCeIDAccountOnProperty(_Telephone);
    public static final SortBCeIDAccountOnProperty Department = new SortBCeIDAccountOnProperty(_Department);
    public static final SortBCeIDAccountOnProperty KnownAs = new SortBCeIDAccountOnProperty(_KnownAs);
    public static final SortBCeIDAccountOnProperty BirthDate = new SortBCeIDAccountOnProperty(_BirthDate);
    public java.lang.String getValue() { return _value_;}
    public static SortBCeIDAccountOnProperty fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        SortBCeIDAccountOnProperty enumeration = (SortBCeIDAccountOnProperty)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static SortBCeIDAccountOnProperty fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SortBCeIDAccountOnProperty.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortBCeIDAccountOnProperty"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
