CREATE TABLE farms.farm_chef_submssn_crm_entities (
	chef_submssn_crm_entity_id bigint NOT NULL,
	crm_entity_guid varchar(100) NOT NULL,
	crm_entity_type_code varchar(10) NOT NULL,
	chef_submission_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_chef_submssn_crm_entities IS E'CHEF SUBMSSN CRM ENTITIES is a cross reference to link CHEF Form Submissions to CRM entities.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.chef_submission_id IS E'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.chef_submssn_crm_entity_id IS E'CHEF SUBMSSN CRM ENTITY ID is a surrogate unique identifier for CHEF SUBMSSN CRM ENTITY.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.crm_entity_guid IS E'CRM ENTITY GUID is the global unique identifier for the entity in the CRM system (Customer Relationship Management).';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.crm_entity_type_code IS E'CRM ENTITY TYPE CODE is a unique code for the object CRM ENTITY TYPE CODE. Examples of codes and descriptions are BENEFIT_UP - Benefit Update, ACCOUNT - Account, TASK - Task, VLDTN_TASK - Validation Error Task, NOTE - Note.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_chef_submssn_crm_entities.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_csce_farm_cetc_fk_i ON farms.farm_chef_submssn_crm_entities (crm_entity_type_code);
CREATE INDEX farm_csce_farm_cs_fk_i ON farms.farm_chef_submssn_crm_entities (chef_submission_id);
ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT farm_csce_uk UNIQUE (chef_submission_id,crm_entity_guid);
ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT farm_csce_pk PRIMARY KEY (crm_entity_guid);
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN chef_submssn_crm_entity_id SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN crm_entity_guid SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN crm_entity_type_code SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN chef_submission_id SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_chef_submssn_crm_entities ALTER COLUMN when_created SET NOT NULL;
