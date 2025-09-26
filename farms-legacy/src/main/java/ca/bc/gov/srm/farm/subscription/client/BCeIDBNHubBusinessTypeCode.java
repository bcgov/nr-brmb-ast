/**
 * BCeIDBNHubBusinessTypeCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDBNHubBusinessTypeCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BCeIDBNHubBusinessTypeCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Void = "Void";
    public static final java.lang.String _Proprietorship = "Proprietorship";
    public static final java.lang.String _GeneralPartnership = "GeneralPartnership";
    public static final java.lang.String _Corporation = "Corporation";
    public static final java.lang.String _Other = "Other";
    public static final BCeIDBNHubBusinessTypeCode Void = new BCeIDBNHubBusinessTypeCode(_Void);
    public static final BCeIDBNHubBusinessTypeCode Proprietorship = new BCeIDBNHubBusinessTypeCode(_Proprietorship);
    public static final BCeIDBNHubBusinessTypeCode GeneralPartnership = new BCeIDBNHubBusinessTypeCode(_GeneralPartnership);
    public static final BCeIDBNHubBusinessTypeCode Corporation = new BCeIDBNHubBusinessTypeCode(_Corporation);
    public static final BCeIDBNHubBusinessTypeCode Other = new BCeIDBNHubBusinessTypeCode(_Other);
    public java.lang.String getValue() { return _value_;}
    public static BCeIDBNHubBusinessTypeCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        BCeIDBNHubBusinessTypeCode enumeration = (BCeIDBNHubBusinessTypeCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static BCeIDBNHubBusinessTypeCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(BCeIDBNHubBusinessTypeCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDBNHubBusinessTypeCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
