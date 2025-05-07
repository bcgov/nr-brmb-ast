CREATE TABLE chef_submission_crm_entity(
    chef_submission_crm_entity_id    numeric(10, 0)    NOT NULL,
    crm_entity_guid                  varchar(100)      NOT NULL,
    crm_entity_type_code             varchar(10)       NOT NULL,
    chef_submission_id               numeric(10, 0)    NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN chef_submission_crm_entity.chef_submission_crm_entity_id IS 'CHEF SUBMISSION CRM ENTITY ID is a surrogate unique identifier for CHEF SUBMISSION CRM ENTITY.'
;
COMMENT ON COLUMN chef_submission_crm_entity.crm_entity_type_code IS 'CRM ENTITY TYPE CODE is a unique code for the object CRM ENTITY TYPE CODE. Examples of codes and descriptions are BENEFIT_UP - Benefit Update, ACCOUNT - Account, TASK - Task, VLDTN_TASK - Validation Error Task, NOTE - Note.'
;
COMMENT ON COLUMN chef_submission_crm_entity.chef_submission_id IS 'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.'
;
COMMENT ON COLUMN chef_submission_crm_entity.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN chef_submission_crm_entity.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN chef_submission_crm_entity.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN chef_submission_crm_entity.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN chef_submission_crm_entity.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE chef_submission_crm_entity IS 'CHEF SUBMISSION CRM ENTITIES is a cross reference to link CHEF Form Submissions to CRM entities.'
;


CREATE INDEX ix_csce_cetc ON chef_submission_crm_entity(crm_entity_type_code)
;
CREATE INDEX ix_csce_csi ON chef_submission_crm_entity(chef_submission_id)
;
CREATE UNIQUE INDEX uk_csce_csi_ceg ON chef_submission_crm_entity(chef_submission_id, crm_entity_guid)
;
