/**
 * CreateAccountRequestResponseType1.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class CreateAccountRequestResponseType1  implements java.io.Serializable {
    private ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequestResult;

    public CreateAccountRequestResponseType1() {
    }

    public CreateAccountRequestResponseType1(
           ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequestResult) {
           this.createAccountRequestResult = createAccountRequestResult;
    }


    /**
     * Gets the createAccountRequestResult value for this CreateAccountRequestResponseType1.
     * 
     * @return createAccountRequestResult
     */
    public ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse getCreateAccountRequestResult() {
        return createAccountRequestResult;
    }


    /**
     * Sets the createAccountRequestResult value for this CreateAccountRequestResponseType1.
     * 
     * @param createAccountRequestResult
     */
    public void setCreateAccountRequestResult(ca.bc.gov.srm.farm.subscription.client.CreateAccountRequestResponse createAccountRequestResult) {
        this.createAccountRequestResult = createAccountRequestResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateAccountRequestResponseType1)) return false;
        CreateAccountRequestResponseType1 other = (CreateAccountRequestResponseType1) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createAccountRequestResult==null && other.getCreateAccountRequestResult()==null) || 
             (this.createAccountRequestResult!=null &&
              this.createAccountRequestResult.equals(other.getCreateAccountRequestResult())));
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
        if (getCreateAccountRequestResult() != null) {
            _hashCode += getCreateAccountRequestResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateAccountRequestResponseType1.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", ">createAccountRequestResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createAccountRequestResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createAccountRequestResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "CreateAccountRequestResponse"));
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
