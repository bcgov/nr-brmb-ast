CREATE TABLE farms.farm_chef_submssn_crm_entities(
    chef_submssn_crm_entity_id    numeric(10, 0)    NOT NULL,
    crm_entity_guid                  varchar(100)      NOT NULL,
    crm_entity_type_code             varchar(10)       NOT NULL,
    chef_submission_id               numeric(10, 0)    NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                      varchar(30)       NOT NULL,
    when_created                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                      varchar(30),
    when_updated                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.chef_submssn_crm_entity_id IS 'CHEF SUBMISSION CRM ENTITY ID is a surrogate unique identifier for CHEF SUBMISSION CRM ENTITY.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.crm_entity_type_code IS 'CRM ENTITY TYPE CODE is a unique code for the object CRM ENTITY TYPE CODE. Examples of codes and descriptions are BENEFIT_UP - Benefit Update, ACCOUNT - Account, TASK - Task, VLDTN_TASK - Validation Error Task, NOTE - Note.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.chef_submission_id IS 'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_chef_submssn_crm_entities IS 'CHEF SUBMISSION CRM ENTITIES is a cross reference to link CHEF Form Submissions to CRM entities.'
;


CREATE INDEX ix_csce_cetc ON farms.farm_chef_submssn_crm_entities(crm_entity_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_csce_csi ON farms.farm_chef_submssn_crm_entities(chef_submission_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_csce_csi_ceg ON farms.farm_chef_submssn_crm_entities(chef_submission_id, crm_entity_guid)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_chef_submssn_crm_entities ADD 
    CONSTRAINT pk_csce PRIMARY KEY (crm_entity_guid) USING INDEX TABLESPACE pg_default 
;
