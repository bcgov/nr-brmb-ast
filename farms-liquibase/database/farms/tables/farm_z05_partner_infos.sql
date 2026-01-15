CREATE TABLE farms.farm_z05_partner_infos (
	partner_info_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	partnership_pin integer NOT NULL,
	partner_first_name varchar(30),
	partner_last_name varchar(30),
	partner_corp_name varchar(50),
	partner_sin_ctn_bn varchar(15),
	partner_percent decimal(6,4),
	partner_pin integer,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z05_partner_infos IS E'Z05 PARTNER INFO identifies all the partners the producer has entered in section 6 of the Harmonized form. This should not include the participant. Even if the participant is in a partnership, thre is no requirement to submit a list of participants, so this file may not have any data for that statement. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_corp_name IS E'PARTNER CORP NAME is the Partners Corporate name.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_first_name IS E'PARTNER FIRST NAME is the partners given name.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_info_key IS E'PARTNER INFO KEY is a unique identifier for Z05 PARTNER INFO.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_last_name IS E'PARTNER LAST NAME is the partners surname.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_percent IS E'PARTNER PERCENT is the partners percentage share.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_pin IS E'PARTNER PIN is CAIS pin of the partner, if one is available.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_sin_ctn_bn IS E'PARTNER SIN CTN BN is the Partners SIN/CTN/BN - see participant SIN/CTN/BN for valid formats.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.partnership_pin IS E'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same partnership pin will show up under both pins. PARTNERSHIP PIN represents the same operation if/when they are used in different program years.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z05_partner_infos.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z05_farm_z03_fk_i ON farms.farm_z05_partner_infos (participant_pin, program_year, operation_number);
ALTER TABLE farms.farm_z05_partner_infos ADD CONSTRAINT farm_z05_pk PRIMARY KEY (partner_info_key);
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN partner_info_key SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN partnership_pin SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z05_partner_infos ALTER COLUMN when_created SET NOT NULL;
