/**
 * AccountDetailRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class AccountDetailRequest  extends ca.bc.gov.srm.farm.subscription.client.RequestBase  implements java.io.Serializable {
    private java.lang.String userId;

    private java.lang.String userGuid;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode accountTypeCode;

    public AccountDetailRequest() {
    }

    public AccountDetailRequest(
           java.lang.String onlineServiceId,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode requesterAccountTypeCode,
           java.lang.String requesterUserId,
           java.lang.String requesterUserGuid,
           java.lang.String userId,
           java.lang.String userGuid,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode accountTypeCode) {
        super(
            onlineServiceId,
            requesterAccountTypeCode,
            requesterUserId,
            requesterUserGuid);
        this.userId = userId;
        this.userGuid = userGuid;
        this.accountTypeCode = accountTypeCode;
    }


    /**
     * Gets the userId value for this AccountDetailRequest.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this AccountDetailRequest.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the userGuid value for this AccountDetailRequest.
     * 
     * @return userGuid
     */
    public java.lang.String getUserGuid() {
        return userGuid;
    }


    /**
     * Sets the userGuid value for this AccountDetailRequest.
     * 
     * @param userGuid
     */
    public void setUserGuid(java.lang.String userGuid) {
        this.userGuid = userGuid;
    }


    /**
     * Gets the accountTypeCode value for this AccountDetailRequest.
     * 
     * @return accountTypeCode
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode getAccountTypeCode() {
        return accountTypeCode;
    }


    /**
     * Sets the accountTypeCode value for this AccountDetailRequest.
     * 
     * @param accountTypeCode
     */
    public void setAccountTypeCode(ca.bc.gov.srm.farm.subscription.client.BCeIDAccountTypeCode accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountDetailRequest)) return false;
        AccountDetailRequest other = (AccountDetailRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.userGuid==null && other.getUserGuid()==null) || 
             (this.userGuid!=null &&
              this.userGuid.equals(other.getUserGuid()))) &&
            ((this.accountTypeCode==null && other.getAccountTypeCode()==null) || 
             (this.accountTypeCode!=null &&
              this.accountTypeCode.equals(other.getAccountTypeCode())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getUserGuid() != null) {
            _hashCode += getUserGuid().hashCode();
        }
        if (getAccountTypeCode() != null) {
            _hashCode += getAccountTypeCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccountDetailRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "AccountDetailRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userGuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "userGuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "accountTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccountTypeCode"));
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
