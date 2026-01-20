CREATE TABLE farms.farm_import_versions (
	import_version_id bigint NOT NULL,
	imported_by_user varchar(30),
	description varchar(2000),
	import_control_file_date timestamp(0),
	import_control_file_info varchar(4000),
	import_date timestamp(0),
	import_file_name varchar(255) NOT NULL,
	import_file_password varchar(100),
	import_file bytea,
	staging_audit_info text,
	import_audit_info text,
	last_status_message varchar(255),
	last_status_date timestamp(0),
	import_state_code varchar(10) NOT NULL,
	import_class_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_import_versions IS E'IMPORT VERSION defines the number used to identify the batch in which one or more PROGRAM YEAR VERSIONs were processed.';
COMMENT ON COLUMN farms.farm_import_versions.description IS E'DESCRIPTION is a note provided by the user about the import process.';
COMMENT ON COLUMN farms.farm_import_versions.import_audit_info IS E'IMPORT AUDIT INFO contains XML which describes in detail how the operational data was updated based on the data in the staging tables.';
COMMENT ON COLUMN farms.farm_import_versions.import_class_code IS E'IMPORT CLASS CODE identifies the type of an IMPORT VERSION .  Possible values: CRA - Canada Revenue Agency, BPU - Benchmark Per Unit, FMV - Fair Market Value .';
COMMENT ON COLUMN farms.farm_import_versions.import_control_file_date IS E'IMPORT CONTROL FILE DATE identifies the federal extract date for the import file.';
COMMENT ON COLUMN farms.farm_import_versions.import_control_file_info IS E'IMPORT CONTROL FILE INFO describes information about the control file information  such as rowcount per extract file number.';
COMMENT ON COLUMN farms.farm_import_versions.import_date IS E'IMPORT DATE is the date the Agristability data was imported into the system.';
COMMENT ON COLUMN farms.farm_import_versions.import_file IS E'IMPORT FILE contains the zip file which contains the CSV data from the federal government.';
COMMENT ON COLUMN farms.farm_import_versions.import_file_name IS E'IMPORT FILE NAME identifies the name of the uploaded import file.';
COMMENT ON COLUMN farms.farm_import_versions.import_file_password IS E'IMPORT FILE PASSWORD is an encrypted password for the IMPORT FILE.';
COMMENT ON COLUMN farms.farm_import_versions.import_state_code IS E'IMPORT STATE CODE identifies the state of an IMPORT VERSION .  Possible values: STG - Staging, SF - Stagin Failed, SC - Staging Complete, IMP - Importing, CAN - Cancelled .';
COMMENT ON COLUMN farms.farm_import_versions.import_version_id IS E'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.';
COMMENT ON COLUMN farms.farm_import_versions.imported_by_user IS E'IMPORTED BY USER identifies the name of the user who initiated the import process for the IMPORT VERSION.';
COMMENT ON COLUMN farms.farm_import_versions.last_status_date IS E'LAST STATUS DATE is the date and time when the LAST STATUS MESSAGE was populated.';
COMMENT ON COLUMN farms.farm_import_versions.last_status_message IS E'LAST STATUS MESSAGE is used to give the user feedback during the import.';
COMMENT ON COLUMN farms.farm_import_versions.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_import_versions.staging_audit_info IS E'STAGING AUDIT INFO contains XML which captures information about the population of the staging tables such as errors, warnings, or other relevent information.';
COMMENT ON COLUMN farms.farm_import_versions.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_import_versions.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_import_versions.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_import_versions.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_iv_farm_iscc_fk_i ON farms.farm_import_versions (import_class_code);
CREATE INDEX farm_iv_farm_isc_fk_i ON farms.farm_import_versions (import_state_code);
ALTER TABLE farms.farm_import_versions ADD CONSTRAINT farm_iv_pk PRIMARY KEY (import_version_id);
ALTER TABLE farms.farm_import_versions ALTER COLUMN import_version_id SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN import_file_name SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN import_state_code SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN import_class_code SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_import_versions ALTER COLUMN when_created SET NOT NULL;
