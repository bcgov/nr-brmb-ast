CREATE TABLE farms.farm_import_versions(
    import_version_id                  numeric(10, 0)    NOT NULL,
    imported_by_user                   varchar(30),
    description                        varchar(2000),
    import_control_file_date           date,
    import_control_file_info    varchar(4000),
    import_date                        date,
    import_file_name                   varchar(255)      NOT NULL,
    import_file_password               varchar(100),
    import_file                        bytea,
    staging_audit_info          text,
    import_audit_info           text,
    last_status_message                varchar(255),
    last_status_date                   date,
    import_state_code                  varchar(10)       NOT NULL,
    import_class_code                  varchar(10)       NOT NULL,
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                        varchar(30)       NOT NULL,
    when_created                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                        varchar(30),
    when_updated                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_import_versions.import_version_id IS 'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.'
;
COMMENT ON COLUMN farms.farm_import_versions.imported_by_user IS 'IMPORTED BY USER identifies the name of the user who initiated the import process for the IMPORT VERSION.'
;
COMMENT ON COLUMN farms.farm_import_versions.description IS 'DESCRIPTION is a note provided by the user about the import process.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_control_file_date IS 'IMPORT CONTROL FILE DATE identifies the federal extract date for the import file.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_control_file_info IS 'IMPORT CONTROL FILE INFORMATION describes information about the control file information  such as rowcount per extract file number.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_date IS 'IMPORT DATE is the date the Agristability data was imported into the system.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_file_name IS 'IMPORT FILE NAME identifies the name of the uploaded import file.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_file_password IS 'IMPORT FILE PASSWORD is an encrypted password for the IMPORT FILE.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_file IS 'IMPORT FILE contains the zip file which contains the CSV data from the federal government.'
;
COMMENT ON COLUMN farms.farm_import_versions.staging_audit_info IS 'STAGING AUDIT INFORMATION contains XML which captures information about the population of the staging tables such as errors, warnings, or other relevant information.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_audit_info IS 'IMPORT AUDIT INFORMATION contains XML which describes in detail how the operational data was updated based on the data in the staging tables.'
;
COMMENT ON COLUMN farms.farm_import_versions.last_status_message IS 'LAST STATUS MESSAGE is used to give the user feedback during the import.'
;
COMMENT ON COLUMN farms.farm_import_versions.last_status_date IS 'LAST STATUS DATE is the date and time when the LAST STATUS MESSAGE was populated.'
;
COMMENT ON COLUMN farms.farm_import_versions.import_state_code IS 'IMPORT STATE CODE identifies the status of an IMPORT VERSION .  Possible values: STG - Staging, SF - Stagin Failed, SC - Staging Complete, IMP - Importing, CAN - Cancelled .'
;
COMMENT ON COLUMN farms.farm_import_versions.import_class_code IS 'IMPORT CLASS CODE identifies the type of an IMPORT VERSION .  Possible values: CRA - Canada Revenue Agency, BPU - Benchmark Per Unit, FMV - Fair Market Value .'
;
COMMENT ON COLUMN farms.farm_import_versions.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_import_versions.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_import_versions.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_import_versions.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_import_versions.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_import_versions IS 'IMPORT VERSION defines the number used to identify the batch in which one or more PROGRAM YEAR VERSIONs were processed.'
;


CREATE INDEX ix_iv_icc ON farms.farm_import_versions(import_class_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_iv_isc ON farms.farm_import_versions(import_state_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_import_versions ADD 
    CONSTRAINT pk_iv PRIMARY KEY (import_version_id) USING INDEX TABLESPACE pg_default 
;
