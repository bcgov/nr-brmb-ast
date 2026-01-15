CREATE TABLE farms.farm_z50_participnt_bnft_calcs (
	benefit_calc_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	agristability_status smallint NOT NULL,
	unadjusted_reference_margin decimal(13,2),
	adjusted_reference_margin decimal(13,2),
	program_margin decimal(13,2),
	whole_farm_ind varchar(1),
	structure_change_ind varchar(1),
	structure_change_adj_amount decimal(13,2),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z50_participnt_bnft_calcs IS E'Z50 PARTICIPNT BNFT CALC identifies benefit calculations used to determine the participants CAIS Benefits, including adjusted and unadjusted Margins, and calculation type indicators. This data can only be provided for years FIPD has processed. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.adjusted_reference_margin IS E'ADJUSTED REFERENCE MARGIN is the final reference margin used, after adjustments for Structure change, Combining, cash vs accrual changes, and other adjustments.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.agristability_status IS E'AGRISTABILITY STATUS is the status of this participant''s data for the current year.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.benefit_calc_key IS E'BENEFIT CALC KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.program_margin IS E'PROGRAM MARGIN is the PROGRAM YEAR margin.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.structure_change_adj_amount IS E'STRUCTURE CHANGE ADJ AMOUNT is the amount that affects the production margin of a farm (or combined whole farm) due to a structure change.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.structure_change_ind IS E'STRUCTURE CHANGE IND indicates if this partcipants data was modified because of a structure change situation. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.unadjusted_reference_margin IS E'UNADJUSTED REFERENCE MARGIN is initial calculated margin.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.whole_farm_ind IS E'WHOLE FARM IND indicates this participants data was combined with another participants data. Allowable values are Y - Yes, N - No.';
CREATE INDEX farm_z50_farm_z02_fk_i ON farms.farm_z50_participnt_bnft_calcs (participant_pin, program_year);
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ADD CONSTRAINT farm_z50_pk PRIMARY KEY (benefit_calc_key);
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN benefit_calc_key SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN agristability_status SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z50_participnt_bnft_calcs ALTER COLUMN when_created SET NOT NULL;
