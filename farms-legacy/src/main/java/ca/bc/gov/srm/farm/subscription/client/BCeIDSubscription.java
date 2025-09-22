/**
 * BCeIDSubscription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDSubscription  implements java.io.Serializable {
    private java.lang.String osKey;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType codeType;

    private java.lang.String subscriptionCode;

    private int batchNumber;

    private int batchItemNumber;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope scope;

    private java.lang.String question;

    private java.lang.String answer;

    private java.util.Calendar activationExpiryDate;

    private java.util.Calendar createdOnDateTime;

    private java.util.Calendar downloadDateTime;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccount account;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus status;

    private java.lang.String batchNumberString;

    private java.lang.String batchItemNumberString;

    private java.lang.String initialActivationPeriod;

    private java.lang.String downloadDate;

    public BCeIDSubscription() {
    }

    public BCeIDSubscription(
           java.lang.String osKey,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType codeType,
           java.lang.String subscriptionCode,
           int batchNumber,
           int batchItemNumber,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope scope,
           java.lang.String question,
           java.lang.String answer,
           java.util.Calendar activationExpiryDate,
           java.util.Calendar createdOnDateTime,
           java.util.Calendar downloadDateTime,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccount account,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus status,
           java.lang.String batchNumberString,
           java.lang.String batchItemNumberString,
           java.lang.String initialActivationPeriod,
           java.lang.String downloadDate) {
           this.osKey = osKey;
           this.codeType = codeType;
           this.subscriptionCode = subscriptionCode;
           this.batchNumber = batchNumber;
           this.batchItemNumber = batchItemNumber;
           this.scope = scope;
           this.question = question;
           this.answer = answer;
           this.activationExpiryDate = activationExpiryDate;
           this.createdOnDateTime = createdOnDateTime;
           this.downloadDateTime = downloadDateTime;
           this.account = account;
           this.status = status;
           this.batchNumberString = batchNumberString;
           this.batchItemNumberString = batchItemNumberString;
           this.initialActivationPeriod = initialActivationPeriod;
           this.downloadDate = downloadDate;
    }


    /**
     * Gets the osKey value for this BCeIDSubscription.
     * 
     * @return osKey
     */
    public java.lang.String getOsKey() {
        return osKey;
    }


    /**
     * Sets the osKey value for this BCeIDSubscription.
     * 
     * @param osKey
     */
    public void setOsKey(java.lang.String osKey) {
        this.osKey = osKey;
    }


    /**
     * Gets the codeType value for this BCeIDSubscription.
     * 
     * @return codeType
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType getCodeType() {
        return codeType;
    }


    /**
     * Sets the codeType value for this BCeIDSubscription.
     * 
     * @param codeType
     */
    public void setCodeType(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType codeType) {
        this.codeType = codeType;
    }


    /**
     * Gets the subscriptionCode value for this BCeIDSubscription.
     * 
     * @return subscriptionCode
     */
    public java.lang.String getSubscriptionCode() {
        return subscriptionCode;
    }


    /**
     * Sets the subscriptionCode value for this BCeIDSubscription.
     * 
     * @param subscriptionCode
     */
    public void setSubscriptionCode(java.lang.String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }


    /**
     * Gets the batchNumber value for this BCeIDSubscription.
     * 
     * @return batchNumber
     */
    public int getBatchNumber() {
        return batchNumber;
    }


    /**
     * Sets the batchNumber value for this BCeIDSubscription.
     * 
     * @param batchNumber
     */
    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }


    /**
     * Gets the batchItemNumber value for this BCeIDSubscription.
     * 
     * @return batchItemNumber
     */
    public int getBatchItemNumber() {
        return batchItemNumber;
    }


    /**
     * Sets the batchItemNumber value for this BCeIDSubscription.
     * 
     * @param batchItemNumber
     */
    public void setBatchItemNumber(int batchItemNumber) {
        this.batchItemNumber = batchItemNumber;
    }


    /**
     * Gets the scope value for this BCeIDSubscription.
     * 
     * @return scope
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope getScope() {
        return scope;
    }


    /**
     * Sets the scope value for this BCeIDSubscription.
     * 
     * @param scope
     */
    public void setScope(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionScope scope) {
        this.scope = scope;
    }


    /**
     * Gets the question value for this BCeIDSubscription.
     * 
     * @return question
     */
    public java.lang.String getQuestion() {
        return question;
    }


    /**
     * Sets the question value for this BCeIDSubscription.
     * 
     * @param question
     */
    public void setQuestion(java.lang.String question) {
        this.question = question;
    }


    /**
     * Gets the answer value for this BCeIDSubscription.
     * 
     * @return answer
     */
    public java.lang.String getAnswer() {
        return answer;
    }


    /**
     * Sets the answer value for this BCeIDSubscription.
     * 
     * @param answer
     */
    public void setAnswer(java.lang.String answer) {
        this.answer = answer;
    }


    /**
     * Gets the activationExpiryDate value for this BCeIDSubscription.
     * 
     * @return activationExpiryDate
     */
    public java.util.Calendar getActivationExpiryDate() {
        return activationExpiryDate;
    }


    /**
     * Sets the activationExpiryDate value for this BCeIDSubscription.
     * 
     * @param activationExpiryDate
     */
    public void setActivationExpiryDate(java.util.Calendar activationExpiryDate) {
        this.activationExpiryDate = activationExpiryDate;
    }


    /**
     * Gets the createdOnDateTime value for this BCeIDSubscription.
     * 
     * @return createdOnDateTime
     */
    public java.util.Calendar getCreatedOnDateTime() {
        return createdOnDateTime;
    }


    /**
     * Sets the createdOnDateTime value for this BCeIDSubscription.
     * 
     * @param createdOnDateTime
     */
    public void setCreatedOnDateTime(java.util.Calendar createdOnDateTime) {
        this.createdOnDateTime = createdOnDateTime;
    }


    /**
     * Gets the downloadDateTime value for this BCeIDSubscription.
     * 
     * @return downloadDateTime
     */
    public java.util.Calendar getDownloadDateTime() {
        return downloadDateTime;
    }


    /**
     * Sets the downloadDateTime value for this BCeIDSubscription.
     * 
     * @param downloadDateTime
     */
    public void setDownloadDateTime(java.util.Calendar downloadDateTime) {
        this.downloadDateTime = downloadDateTime;
    }


    /**
     * Gets the account value for this BCeIDSubscription.
     * 
     * @return account
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccount getAccount() {
        return account;
    }


    /**
     * Sets the account value for this BCeIDSubscription.
     * 
     * @param account
     */
    public void setAccount(ca.bc.gov.srm.farm.subscription.client.BCeIDAccount account) {
        this.account = account;
    }


    /**
     * Gets the status value for this BCeIDSubscription.
     * 
     * @return status
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this BCeIDSubscription.
     * 
     * @param status
     */
    public void setStatus(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionStatus status) {
        this.status = status;
    }


    /**
     * Gets the batchNumberString value for this BCeIDSubscription.
     * 
     * @return batchNumberString
     */
    public java.lang.String getBatchNumberString() {
        return batchNumberString;
    }


    /**
     * Sets the batchNumberString value for this BCeIDSubscription.
     * 
     * @param batchNumberString
     */
    public void setBatchNumberString(java.lang.String batchNumberString) {
        this.batchNumberString = batchNumberString;
    }


    /**
     * Gets the batchItemNumberString value for this BCeIDSubscription.
     * 
     * @return batchItemNumberString
     */
    public java.lang.String getBatchItemNumberString() {
        return batchItemNumberString;
    }


    /**
     * Sets the batchItemNumberString value for this BCeIDSubscription.
     * 
     * @param batchItemNumberString
     */
    public void setBatchItemNumberString(java.lang.String batchItemNumberString) {
        this.batchItemNumberString = batchItemNumberString;
    }


    /**
     * Gets the initialActivationPeriod value for this BCeIDSubscription.
     * 
     * @return initialActivationPeriod
     */
    public java.lang.String getInitialActivationPeriod() {
        return initialActivationPeriod;
    }


    /**
     * Sets the initialActivationPeriod value for this BCeIDSubscription.
     * 
     * @param initialActivationPeriod
     */
    public void setInitialActivationPeriod(java.lang.String initialActivationPeriod) {
        this.initialActivationPeriod = initialActivationPeriod;
    }


    /**
     * Gets the downloadDate value for this BCeIDSubscription.
     * 
     * @return downloadDate
     */
    public java.lang.String getDownloadDate() {
        return downloadDate;
    }


    /**
     * Sets the downloadDate value for this BCeIDSubscription.
     * 
     * @param downloadDate
     */
    public void setDownloadDate(java.lang.String downloadDate) {
        this.downloadDate = downloadDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDSubscription)) return false;
        BCeIDSubscription other = (BCeIDSubscription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.osKey==null && other.getOsKey()==null) || 
             (this.osKey!=null &&
              this.osKey.equals(other.getOsKey()))) &&
            ((this.codeType==null && other.getCodeType()==null) || 
             (this.codeType!=null &&
              this.codeType.equals(other.getCodeType()))) &&
            ((this.subscriptionCode==null && other.getSubscriptionCode()==null) || 
             (this.subscriptionCode!=null &&
              this.subscriptionCode.equals(other.getSubscriptionCode()))) &&
            this.batchNumber == other.getBatchNumber() &&
            this.batchItemNumber == other.getBatchItemNumber() &&
            ((this.scope==null && other.getScope()==null) || 
             (this.scope!=null &&
              this.scope.equals(other.getScope()))) &&
            ((this.question==null && other.getQuestion()==null) || 
             (this.question!=null &&
              this.question.equals(other.getQuestion()))) &&
            ((this.answer==null && other.getAnswer()==null) || 
             (this.answer!=null &&
              this.answer.equals(other.getAnswer()))) &&
            ((this.activationExpiryDate==null && other.getActivationExpiryDate()==null) || 
             (this.activationExpiryDate!=null &&
              this.activationExpiryDate.equals(other.getActivationExpiryDate()))) &&
            ((this.createdOnDateTime==null && other.getCreatedOnDateTime()==null) || 
             (this.createdOnDateTime!=null &&
              this.createdOnDateTime.equals(other.getCreatedOnDateTime()))) &&
            ((this.downloadDateTime==null && other.getDownloadDateTime()==null) || 
             (this.downloadDateTime!=null &&
              this.downloadDateTime.equals(other.getDownloadDateTime()))) &&
            ((this.account==null && other.getAccount()==null) || 
             (this.account!=null &&
              this.account.equals(other.getAccount()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.batchNumberString==null && other.getBatchNumberString()==null) || 
             (this.batchNumberString!=null &&
              this.batchNumberString.equals(other.getBatchNumberString()))) &&
            ((this.batchItemNumberString==null && other.getBatchItemNumberString()==null) || 
             (this.batchItemNumberString!=null &&
              this.batchItemNumberString.equals(other.getBatchItemNumberString()))) &&
            ((this.initialActivationPeriod==null && other.getInitialActivationPeriod()==null) || 
             (this.initialActivationPeriod!=null &&
              this.initialActivationPeriod.equals(other.getInitialActivationPeriod()))) &&
            ((this.downloadDate==null && other.getDownloadDate()==null) || 
             (this.downloadDate!=null &&
              this.downloadDate.equals(other.getDownloadDate())));
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
        if (getOsKey() != null) {
            _hashCode += getOsKey().hashCode();
        }
        if (getCodeType() != null) {
            _hashCode += getCodeType().hashCode();
        }
        if (getSubscriptionCode() != null) {
            _hashCode += getSubscriptionCode().hashCode();
        }
        _hashCode += getBatchNumber();
        _hashCode += getBatchItemNumber();
        if (getScope() != null) {
            _hashCode += getScope().hashCode();
        }
        if (getQuestion() != null) {
            _hashCode += getQuestion().hashCode();
        }
        if (getAnswer() != null) {
            _hashCode += getAnswer().hashCode();
        }
        if (getActivationExpiryDate() != null) {
            _hashCode += getActivationExpiryDate().hashCode();
        }
        if (getCreatedOnDateTime() != null) {
            _hashCode += getCreatedOnDateTime().hashCode();
        }
        if (getDownloadDateTime() != null) {
            _hashCode += getDownloadDateTime().hashCode();
        }
        if (getAccount() != null) {
            _hashCode += getAccount().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getBatchNumberString() != null) {
            _hashCode += getBatchNumberString().hashCode();
        }
        if (getBatchItemNumberString() != null) {
            _hashCode += getBatchItemNumberString().hashCode();
        }
        if (getInitialActivationPeriod() != null) {
            _hashCode += getInitialActivationPeriod().hashCode();
        }
        if (getDownloadDate() != null) {
            _hashCode += getDownloadDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDSubscription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("osKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "osKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "codeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionCodeType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscriptionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batchNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchItemNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batchItemNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scope");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "scope"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionScope"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("question");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "question"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "answer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationExpiryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "activationExpiryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdOnDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createdOnDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "downloadDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNumberString");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batchNumberString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchItemNumberString");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batchItemNumberString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initialActivationPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "initialActivationPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "downloadDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
