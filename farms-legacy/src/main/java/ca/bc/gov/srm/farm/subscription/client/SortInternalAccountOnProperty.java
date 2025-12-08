/**
 * SortInternalAccountOnProperty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class SortInternalAccountOnProperty implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SortInternalAccountOnProperty(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Void = "Void";
    public static final java.lang.String _BCgovAccountType = "BCgovAccountType";
    public static final java.lang.String _UserId = "UserId";
    public static final java.lang.String _FirstName = "FirstName";
    public static final java.lang.String _LastName = "LastName";
    public static final java.lang.String _Initials = "Initials";
    public static final java.lang.String _Email = "Email";
    public static final java.lang.String _Telephone = "Telephone";
    public static final java.lang.String _City = "City";
    public static final SortInternalAccountOnProperty Void = new SortInternalAccountOnProperty(_Void);
    public static final SortInternalAccountOnProperty BCgovAccountType = new SortInternalAccountOnProperty(_BCgovAccountType);
    public static final SortInternalAccountOnProperty UserId = new SortInternalAccountOnProperty(_UserId);
    public static final SortInternalAccountOnProperty FirstName = new SortInternalAccountOnProperty(_FirstName);
    public static final SortInternalAccountOnProperty LastName = new SortInternalAccountOnProperty(_LastName);
    public static final SortInternalAccountOnProperty Initials = new SortInternalAccountOnProperty(_Initials);
    public static final SortInternalAccountOnProperty Email = new SortInternalAccountOnProperty(_Email);
    public static final SortInternalAccountOnProperty Telephone = new SortInternalAccountOnProperty(_Telephone);
    public static final SortInternalAccountOnProperty City = new SortInternalAccountOnProperty(_City);
    public java.lang.String getValue() { return _value_;}
    public static SortInternalAccountOnProperty fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        SortInternalAccountOnProperty enumeration = (SortInternalAccountOnProperty)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static SortInternalAccountOnProperty fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(SortInternalAccountOnProperty.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "SortInternalAccountOnProperty"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
