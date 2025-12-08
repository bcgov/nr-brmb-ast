/**
 * FailureCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class FailureCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FailureCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Void = "Void";
    public static final java.lang.String _NoResults = "NoResults";
    public static final java.lang.String _ExpectedResults = "ExpectedResults";
    public static final java.lang.String _ArgumentException = "ArgumentException";
    public static final java.lang.String _AuthenticationException = "AuthenticationException";
    public static final java.lang.String _ValidationException = "ValidationException";
    public static final java.lang.String _AuthorizationException = "AuthorizationException";
    public static final java.lang.String _Timeout = "Timeout";
    public static final FailureCode Void = new FailureCode(_Void);
    public static final FailureCode NoResults = new FailureCode(_NoResults);
    public static final FailureCode ExpectedResults = new FailureCode(_ExpectedResults);
    public static final FailureCode ArgumentException = new FailureCode(_ArgumentException);
    public static final FailureCode AuthenticationException = new FailureCode(_AuthenticationException);
    public static final FailureCode ValidationException = new FailureCode(_ValidationException);
    public static final FailureCode AuthorizationException = new FailureCode(_AuthorizationException);
    public static final FailureCode Timeout = new FailureCode(_Timeout);
    public java.lang.String getValue() { return _value_;}
    public static FailureCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FailureCode enumeration = (FailureCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FailureCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(FailureCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "FailureCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
