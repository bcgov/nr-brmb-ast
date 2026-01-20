CREATE TABLE farms.farm_whole_farm_participants (
	whole_farm_participant_id bigint NOT NULL,
	whole_farm_combined_pin integer NOT NULL,
	whole_farm_comb_pin_add_ind varchar(1),
	whole_farm_comb_pin_remove_ind varchar(1),
	program_year_version_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_whole_farm_participants IS E'WHOLE FARM PARTICIPANT describes the combined PINS used by the FARMING OPERATION for a given PROGRAM YEAR VERSION.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.program_year_version_id IS E'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.whole_farm_comb_pin_add_ind IS E'WHOLE FARM COMB PIN ADD IND of Participant that has been combined with the participant on this form for whole farms calculation, see Section 2 - Participant  Profile.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.whole_farm_comb_pin_remove_ind IS E'WHOLE FARM COMB PIN REMOVE IND should be removed from the  PINs combined with this participant for whole farms.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.whole_farm_combined_pin IS E'WHOLE FARM COMBINED PIN of Participant that has been combined with the participant on this form for whole farms calculation, see Section 2 - Participant  Profile.';
COMMENT ON COLUMN farms.farm_whole_farm_participants.whole_farm_participant_id IS E'WHOLE FARM PARTICIPANT ID is a surrogate unique identifier for WHOLE FARM.';
CREATE INDEX farm_wfp_farm_pyv_fk_i ON farms.farm_whole_farm_participants (program_year_version_id);
ALTER TABLE farms.farm_whole_farm_participants ADD CONSTRAINT farm_wfp_pk PRIMARY KEY (whole_farm_participant_id);
ALTER TABLE farms.farm_whole_farm_participants ADD CONSTRAINT avcon_1260470137_whole_000 CHECK (whole_farm_comb_pin_add_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_whole_farm_participants ADD CONSTRAINT avcon_1260470137_whole_001 CHECK (whole_farm_comb_pin_remove_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN whole_farm_participant_id SET NOT NULL;
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN whole_farm_combined_pin SET NOT NULL;
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN program_year_version_id SET NOT NULL;
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_whole_farm_participants ALTER COLUMN when_created SET NOT NULL;
