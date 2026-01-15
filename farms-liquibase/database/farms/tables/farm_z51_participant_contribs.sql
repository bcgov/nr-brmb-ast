CREATE TABLE farms.farm_z51_participant_contribs (
	contribution_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	provincial_contributions decimal(13,2) NOT NULL,
	federal_contributions decimal(13,2) NOT NULL,
	interim_contributions decimal(13,2) NOT NULL,
	producer_share decimal(13,2) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z51_participant_contribs IS E'Z51 PARTICIPANT CONTRIB identifies contributions by the participant for the program year, broken out by type. This data can only be provided for years FIPD has processed. This file is created by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.contribution_key IS E'CONTRIBUTION KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.federal_contributions IS E'FEDERAL CONTRIBUTIONS is the amount of FEDERAL CONTRIBUTIONS this participant has received.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.interim_contributions IS E'INTERIM CONTRIBUTIONS is the amount of INTERIM CONTRIBUTIONS for this participant, if a final calculation is not yet been made. If a final calculation has been made, this amount will be 0.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.producer_share IS E'PRODUCER SHARE is the amount of AgriStability withdrawals that are producer''s share.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.provincial_contributions IS E'PROVINCIAL CONTRIBUTIONS is the amount of PROVINCIAL CONTRIBUTIONS this participant has received.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z51_participant_contribs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z51_farm_z02_fk_i ON farms.farm_z51_participant_contribs (participant_pin, program_year);
ALTER TABLE farms.farm_z51_participant_contribs ADD CONSTRAINT farm_z51_pk PRIMARY KEY (contribution_key);
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN contribution_key SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN provincial_contributions SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN federal_contributions SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN interim_contributions SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN producer_share SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z51_participant_contribs ALTER COLUMN when_created SET NOT NULL;
