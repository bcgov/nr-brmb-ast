CREATE TABLE crm_entity_type_code(
    crm_entity_type_code    varchar(10)      NOT NULL,
    description             varchar(256)     NOT NULL,
    established_date        date             NOT NULL,
    expiry_date             date             NOT NULL,
    revision_count          numeric(5, 0)    NOT NULL,
    create_user             varchar(30)      NOT NULL,
    create_date             timestamp(6)     NOT NULL,
    update_user             varchar(30),
    update_date             timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN crm_entity_type_code.crm_entity_type_code IS 'CRM ENTITY TYPE CODE is a unique code for the object CRM ENTITY TYPE CODE. Examples of codes and descriptions are BENEFIT_UP - Benefit Update, ACCOUNT - Account, TASK - Task, VLDTN_TASK - Validation Error Task, NOTE - Note.'
;
COMMENT ON COLUMN crm_entity_type_code.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN crm_entity_type_code.established_date IS 'ESTABLISHED DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN crm_entity_type_code.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN crm_entity_type_code.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN crm_entity_type_code.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN crm_entity_type_code.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN crm_entity_type_code.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN crm_entity_type_code.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE crm_entity_type_code IS 'CRM ENTITY TYPE CODE indicates a type of entity that exists in the CRM system.'
;

ALTER TABLE crm_entity_type_code ADD 
    CONSTRAINT pk_cety PRIMARY KEY (crm_entity_type_code) USING INDEX TABLESPACE pg_default 
;
