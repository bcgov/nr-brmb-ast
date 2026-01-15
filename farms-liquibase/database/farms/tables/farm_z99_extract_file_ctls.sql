CREATE TABLE farms.farm_z99_extract_file_ctls (
	extract_file_number smallint NOT NULL,
	extract_date varchar(8) NOT NULL,
	row_count integer NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z99_extract_file_ctls IS E'Z99 EXTRACT FILE CTL identifies row counts for every other file in the extract. There will be one row per file. This file is created by FIPD. If both the AgriStability t1273 extract files and the AgriStability supplemental data files are extracted, there will be a single file 99 with counts for all files. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.extract_date IS E'EXTRACT DATE is the PROGRAM YEAR for this record.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.extract_file_number IS E'EXTRACT FILE NUMBER the 2 digit file number of each file (01, 02, etc.).';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.row_count IS E'ROW COUNT is the number of rows each extract file should have.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z99_extract_file_ctls.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_z99_extract_file_ctls ADD CONSTRAINT farm_z99_pk PRIMARY KEY (extract_file_number);
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN extract_file_number SET NOT NULL;
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN extract_date SET NOT NULL;
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN row_count SET NOT NULL;
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z99_extract_file_ctls ALTER COLUMN when_created SET NOT NULL;
