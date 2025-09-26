/**
 * BCeIDSubscriptionBatch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ca.bc.gov.srm.farm.subscription.client;

public class BCeIDSubscriptionBatch  implements java.io.Serializable {
    private java.lang.String batchNumber;

    private java.util.Calendar createdOn;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDAccount createdBy;

    private java.util.Calendar deletedOn;

    private java.lang.String description;

    private ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType type;

    private java.lang.Object[] subscriptionList;

    public BCeIDSubscriptionBatch() {
    }

    public BCeIDSubscriptionBatch(
           java.lang.String batchNumber,
           java.util.Calendar createdOn,
           ca.bc.gov.srm.farm.subscription.client.BCeIDAccount createdBy,
           java.util.Calendar deletedOn,
           java.lang.String description,
           ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType type,
           java.lang.Object[] subscriptionList) {
           this.batchNumber = batchNumber;
           this.createdOn = createdOn;
           this.createdBy = createdBy;
           this.deletedOn = deletedOn;
           this.description = description;
           this.type = type;
           this.subscriptionList = subscriptionList;
    }


    /**
     * Gets the batchNumber value for this BCeIDSubscriptionBatch.
     * 
     * @return batchNumber
     */
    public java.lang.String getBatchNumber() {
        return batchNumber;
    }


    /**
     * Sets the batchNumber value for this BCeIDSubscriptionBatch.
     * 
     * @param batchNumber
     */
    public void setBatchNumber(java.lang.String batchNumber) {
        this.batchNumber = batchNumber;
    }


    /**
     * Gets the createdOn value for this BCeIDSubscriptionBatch.
     * 
     * @return createdOn
     */
    public java.util.Calendar getCreatedOn() {
        return createdOn;
    }


    /**
     * Sets the createdOn value for this BCeIDSubscriptionBatch.
     * 
     * @param createdOn
     */
    public void setCreatedOn(java.util.Calendar createdOn) {
        this.createdOn = createdOn;
    }


    /**
     * Gets the createdBy value for this BCeIDSubscriptionBatch.
     * 
     * @return createdBy
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDAccount getCreatedBy() {
        return createdBy;
    }


    /**
     * Sets the createdBy value for this BCeIDSubscriptionBatch.
     * 
     * @param createdBy
     */
    public void setCreatedBy(ca.bc.gov.srm.farm.subscription.client.BCeIDAccount createdBy) {
        this.createdBy = createdBy;
    }


    /**
     * Gets the deletedOn value for this BCeIDSubscriptionBatch.
     * 
     * @return deletedOn
     */
    public java.util.Calendar getDeletedOn() {
        return deletedOn;
    }


    /**
     * Sets the deletedOn value for this BCeIDSubscriptionBatch.
     * 
     * @param deletedOn
     */
    public void setDeletedOn(java.util.Calendar deletedOn) {
        this.deletedOn = deletedOn;
    }


    /**
     * Gets the description value for this BCeIDSubscriptionBatch.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this BCeIDSubscriptionBatch.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the type value for this BCeIDSubscriptionBatch.
     * 
     * @return type
     */
    public ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType getType() {
        return type;
    }


    /**
     * Sets the type value for this BCeIDSubscriptionBatch.
     * 
     * @param type
     */
    public void setType(ca.bc.gov.srm.farm.subscription.client.BCeIDSubscriptionCodeType type) {
        this.type = type;
    }


    /**
     * Gets the subscriptionList value for this BCeIDSubscriptionBatch.
     * 
     * @return subscriptionList
     */
    public java.lang.Object[] getSubscriptionList() {
        return subscriptionList;
    }


    /**
     * Sets the subscriptionList value for this BCeIDSubscriptionBatch.
     * 
     * @param subscriptionList
     */
    public void setSubscriptionList(java.lang.Object[] subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BCeIDSubscriptionBatch)) return false;
        BCeIDSubscriptionBatch other = (BCeIDSubscriptionBatch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.batchNumber==null && other.getBatchNumber()==null) || 
             (this.batchNumber!=null &&
              this.batchNumber.equals(other.getBatchNumber()))) &&
            ((this.createdOn==null && other.getCreatedOn()==null) || 
             (this.createdOn!=null &&
              this.createdOn.equals(other.getCreatedOn()))) &&
            ((this.createdBy==null && other.getCreatedBy()==null) || 
             (this.createdBy!=null &&
              this.createdBy.equals(other.getCreatedBy()))) &&
            ((this.deletedOn==null && other.getDeletedOn()==null) || 
             (this.deletedOn!=null &&
              this.deletedOn.equals(other.getDeletedOn()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.subscriptionList==null && other.getSubscriptionList()==null) || 
             (this.subscriptionList!=null &&
              java.util.Arrays.equals(this.subscriptionList, other.getSubscriptionList())));
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
        if (getBatchNumber() != null) {
            _hashCode += getBatchNumber().hashCode();
        }
        if (getCreatedOn() != null) {
            _hashCode += getCreatedOn().hashCode();
        }
        if (getCreatedBy() != null) {
            _hashCode += getCreatedBy().hashCode();
        }
        if (getDeletedOn() != null) {
            _hashCode += getDeletedOn().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getSubscriptionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubscriptionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubscriptionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BCeIDSubscriptionBatch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionBatch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "batchNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createdOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "createdBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDAccount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deletedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "deletedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscriptionCodeType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscriptionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "subscriptionList"));
        //elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setArrayType(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "BCeIDSubscription"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.bceid.ca/webservices/Client/V7/", "anyType"));
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
