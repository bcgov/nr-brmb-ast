/**
 * CreateAccountRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class CreateAccountRequest  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(
           ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) {
           this.createAccountRequestRequest = createAccountRequestRequest;
    }


    /**
     * Gets the createAccountRequestRequest value for this CreateAccountRequest.
     * 
     * @return createAccountRequestRequest
     */
    public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest getCreateAccountRequestRequest() {
        return createAccountRequestRequest;
    }


    /**
     * Sets the createAccountRequestRequest value for this CreateAccountRequest.
     * 
     * @param createAccountRequestRequest
     */
    public void setCreateAccountRequestRequest(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestRequest createAccountRequestRequest) {
        this.createAccountRequestRequest = createAccountRequestRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateAccountRequest)) return false;
        CreateAccountRequest other = (CreateAccountRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createAccountRequestRequest==null && other.getCreateAccountRequestRequest()==null) || 
             (this.createAccountRequestRequest!=null &&
              this.createAccountRequestRequest.equals(other.getCreateAccountRequestRequest())));
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
        if (getCreateAccountRequestRequest() != null) {
            _hashCode += getCreateAccountRequestRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateAccountRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createAccountRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createAccountRequestRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestRequest"));
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
