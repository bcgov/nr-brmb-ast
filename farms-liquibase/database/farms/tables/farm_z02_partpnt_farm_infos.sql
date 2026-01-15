CREATE TABLE farms.farm_z02_partpnt_farm_infos (
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	form_version_number smallint NOT NULL,
	province_of_residence varchar(2) NOT NULL,
	province_of_main_farmstead varchar(2),
	postmark_date varchar(8) NOT NULL,
	received_date varchar(8) NOT NULL,
	sole_proprietor_ind varchar(1),
	partnership_member_ind varchar(1),
	corporate_shareholder_ind varchar(1),
	coop_member_ind varchar(1),
	common_share_total integer,
	farm_years smallint,
	last_year_farming_ind varchar(1),
	form_origin_code smallint,
	industry_code integer,
	participant_profile_code smallint NOT NULL,
	accrual_cash_conversion_ind varchar(1),
	perishable_commodities_ind varchar(1),
	receipts_ind varchar(1),
	other_text_ind varchar(1),
	other_text varchar(100),
	accrual_worksheet_ind varchar(1),
	cwb_worksheet_ind varchar(1),
	combined_this_year_ind varchar(1),
	completed_prod_cycle_ind varchar(1),
	disaster_ind varchar(1),
	copy_cob_to_contact_ind varchar(1),
	municipality_code smallint,
	form_version_effective_date varchar(8) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z02_partpnt_farm_infos IS E'Z02 PARTPNT FARM INFO lists AgriStablity Participant Farming information, from page 1/Section 1 of the Harmonized t1273 form. This file is sent to the provinces by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.accrual_cash_conversion_ind IS E'ACCRUAL CASH CONVERSION IND indicates if the accrual to cash/cash to accrual conversions box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.accrual_worksheet_ind IS E'ACCRUAL WORKSHEET IND indicates if the Accrual Reference Margin Worksheet box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.combined_this_year_ind IS E'COMBINED THIS YEAR IND indicates if the "Should this operation be combined for" box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.common_share_total IS E'COMMON SHARE TOTAL denoted the the outstanding common shares.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.completed_prod_cycle_ind IS E'COMPLETED PROD CYCLE IND indicates if the "Have you completed a production cycle on at least one of the commodities you produced?" box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.coop_member_ind IS E'COOP MEMBER IND is Y if carried on farming business as a member of a co-operative, otherwise N.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.copy_cob_to_contact_ind IS E'COPY COB TO CONTACT IND can be "Y" or "N" - "Y" indicates that a copy of the Calculation of Benefits (COB) statement should be sent to the Contact person.  Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.corporate_shareholder_ind IS E'CORPORATE SHAREHOLDER IND is Y  if carried on farming business as a shareholder of a corporation; otherwise N. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.cwb_worksheet_ind IS E'CWB WORKSHEET IND indicates if the CWB Adjustment Worksheet box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.disaster_ind IS E'DISASTER IND indicates if the "were you unable to complete a production cycle due to disaster circumstances" box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.farm_years IS E'FARM YEARS is the number of farming years.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.form_origin_code IS E'FORM ORIGIN CODE indicates how the form information was received at CCRA.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.form_version_effective_date IS E'FORM VERSION EFFECTIVE DATE is the date the form version information was last updated.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.form_version_number IS E'FORM VERSION NUMBER Distinguishes between different versions of the AgriStability application from the producer. Both the producer and the administration can initiate adjustments that create a new form version in a specific PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.industry_code IS E'INDUSTRY CODE is the code for the industry.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.last_year_farming_ind IS E'LAST YEAR FARMING IND was the last year of farming. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.municipality_code IS E'MUNICIPALITY CODE is the three digit code for the Municipality.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.other_text IS E'OTHER TEXT contains the additional text if the other box is checked.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.other_text_ind IS E'OTHER TEXT IND identifies if Other Text ind is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.participant_profile_code IS E'PARTICIPANT PROFILE CODE is a code to indicate which programs the participant is applying for.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.partnership_member_ind IS E'PARTNERSHIP MEMBER IND is Y if carried on farming business partner of a partnership; otherwise N. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.perishable_commodities_ind IS E'PERISHABLE COMMODITIES IND indicates if the Perishable Commodities Worksheet box checked.  Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.postmark_date IS E'POSTMARK DATE is the Date Form was Postmarked. Will be received date if received before filing deadline.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.province_of_main_farmstead IS E'PROVINCE OF MAIN FARMSTEAD is province of Main Farmstead from page 1 of T1163 E.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.province_of_residence IS E'PROVINCE OF RESIDENCE is the province of Residence.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.receipts_ind IS E'RECEIPTS IND indicates if the receipts box is checked. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.received_date IS E'RECEIVED DATE is the date form was received by RCT.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.sole_proprietor_ind IS E'SOLE PROPRIETOR IND is Y if carried on farming business as a sole proprietor, otherwise N. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z02_partpnt_farm_infos.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_z02_partpnt_farm_infos ADD CONSTRAINT farm_z02_pk PRIMARY KEY (participant_pin,program_year);
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN form_version_number SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN province_of_residence SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN postmark_date SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN received_date SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN participant_profile_code SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN form_version_effective_date SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z02_partpnt_farm_infos ALTER COLUMN when_created SET NOT NULL;
