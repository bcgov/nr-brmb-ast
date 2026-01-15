CREATE TABLE farms.farm_agristability_represntves (
	agristability_represntve_id bigint NOT NULL,
	user_guid varchar(32) NOT NULL,
	userid varchar(64) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_agristability_represntves IS E'AGRISTABILITY REPRESENTATIVE is a type of user who can access AgriStability information data on behalf of specified clients . An AGRISTABILITY REPRESENTATIVE can be associated with one or more clients. An AGRISTABILITY REPRESENTATIVE may have an associated PERSON. An AGRISTABILITY REPRESENTATIVE will be identified from federal tax return data or could be supplied by BC staff.';
COMMENT ON COLUMN farms.farm_agristability_represntves.agristability_represntve_id IS E'AGRISTABILITY REPRESNTVE ID is a surrogate unique identifier for AGRISTABILITY REPRESENTATIVEs.';
COMMENT ON COLUMN farms.farm_agristability_represntves.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_agristability_represntves.user_guid IS E'USER GUID is the Globally Unique Identifier of the person. The user ID is not guaranteed to be unique.';
COMMENT ON COLUMN farms.farm_agristability_represntves.userid IS E'USERID is the user ID associated with the GUID that a user would use to log on with.';
COMMENT ON COLUMN farms.farm_agristability_represntves.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_agristability_represntves.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_agristability_represntves.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristability_represntves.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_agristability_represntves ADD CONSTRAINT farm_asr_userid_uk UNIQUE (userid);
ALTER TABLE farms.farm_agristability_represntves ADD CONSTRAINT farm_asr_guid_uk UNIQUE (user_guid);
ALTER TABLE farms.farm_agristability_represntves ADD CONSTRAINT farm_asr_pk PRIMARY KEY (agristability_represntve_id);
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN agristability_represntve_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN user_guid SET NOT NULL;
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN userid SET NOT NULL;
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_agristability_represntves ALTER COLUMN when_created SET NOT NULL;
