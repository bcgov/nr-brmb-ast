CREATE TABLE farms.farm_users (
	user_id bigint NOT NULL,
	user_guid varchar(32) NOT NULL,
	source_directory varchar(64) NOT NULL,
	account_name varchar(64),
	email_address varchar(128),
	verifier_ind varchar(1) NOT NULL DEFAULT 'N',
	deleted_ind varchar(1) NOT NULL DEFAULT 'N',
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_users IS E'USER is a user who has access to the FARM application. Users data is pulled from the BCeID WebService using the WebADE Java API.';
COMMENT ON COLUMN farms.farm_users.account_name IS E'ACCOUNT NAME is the username of the user, for example, JOHNTDOE.';
COMMENT ON COLUMN farms.farm_users.deleted_ind IS E'DELETED IND identifies if the user is no longer active and should not be listed as a choice in the application.';
COMMENT ON COLUMN farms.farm_users.email_address IS E'EMAIL ADDRESS is the email address of the user. In the microsoft clound, including CRM, this is used as the username.';
COMMENT ON COLUMN farms.farm_users.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_users.source_directory IS E'SOURCE DIRECTORY is the user directory of this user.';
COMMENT ON COLUMN farms.farm_users.user_guid IS E'USER GUID is a surrogate unique identifier for the USER in the BCeID Web Service.';
COMMENT ON COLUMN farms.farm_users.user_id IS E'USER ID is a surrogate unique identifier for a USER.';
COMMENT ON COLUMN farms.farm_users.verifier_ind IS E'VERIFIER IND identifies if the user has verifier permissions in FARM. This is automatically determined based on the roles of the user.';
COMMENT ON COLUMN farms.farm_users.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_users.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_users.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_users.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_users ADD CONSTRAINT farm_u_pk PRIMARY KEY (user_id);
ALTER TABLE farms.farm_users ADD CONSTRAINT farm_u_ug_uk UNIQUE (user_guid);
ALTER TABLE farms.farm_users ADD CONSTRAINT farm_u_d_chk CHECK (verifier_ind in ('N', 'Y'));
ALTER TABLE farms.farm_users ADD CONSTRAINT farm_u_v_chk CHECK (verifier_ind in ('N', 'Y'));
ALTER TABLE farms.farm_users ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN user_guid SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN verifier_ind SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN deleted_ind SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN when_created SET NOT NULL;
ALTER TABLE farms.farm_users ALTER COLUMN source_directory SET NOT NULL;
