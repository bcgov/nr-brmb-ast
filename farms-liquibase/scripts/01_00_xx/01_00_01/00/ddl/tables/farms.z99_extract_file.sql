CREATE TABLE farms.farm_z99_extract_file_ctls(
    extract_file_number    numeric(2, 0)    NOT NULL,
    extract_date           varchar(8)       NOT NULL,
    row_count              numeric(9, 0)    NOT NULL,
    revision_count         numeric(5, 0)    DEFAULT 1 NOT NULL,
    who_created            varchar(30)      NOT NULL,
    when_created            timestamp(6)     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated            varchar(30),
    when_updated            timestamp(6)     DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.extract_file_number IS 'EXTRACT FILE NUMBER the 2 digit file number of each file (01, 02, etc.).'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.extract_date IS 'EXTRACT DATE is the PROGRAM YEAR for this record.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.row_count IS 'ROW COUNT is the number of rows each extract file should have.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z99_extract_file_ctls IS 'Z99 EXTRACT FILE identifies row counts for every other file in the extract. There will be one row per file. This file is created by FIPD. If both the AgriStability t1273 extract files and the AgriStability supplemental data files are extracted, there will be a single file 99 with counts for all files. This is a staging object used to load temporary data set before being merged into the operational data.'
;


ALTER TABLE farms.farm_z99_extract_file_ctls ADD 
    CONSTRAINT pk_zei PRIMARY KEY (extract_file_number) USING INDEX TABLESPACE pg_default 
;
