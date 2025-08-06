CREATE TABLE farms.farm_z05_partner_infos(
    partner_info_key            numeric(10, 0)    NOT NULL,
    participant_pin                    numeric(9, 0)     NOT NULL,
    program_year                       numeric(4, 0)     NOT NULL,
    operation_number                   numeric(4, 0)     NOT NULL,
    partnership_pin                    numeric(9, 0)     NOT NULL,
    partner_first_name                 varchar(30),
    partner_last_name                  varchar(30),
    partner_corp_name                  varchar(50),
    partner_sin_ctn_bn    varchar(15),
    partner_percent                    numeric(6, 4),
    partner_pin                        numeric(9, 0),
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                        varchar(30)       NOT NULL,
    when_created                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                        varchar(30),
    when_updated                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_info_key IS 'PARTNER INFORMATION KEY is a unique identifier for Z05 PARTNER INFO.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partnership_pin IS 'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same partnership pin will show up under both pins. PARTNERSHIP PIN represents the same operation if/when they are used in different program years.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_first_name IS 'PARTNER FIRST NAME is the partners given name.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_last_name IS 'PARTNER LAST NAME is the partners surname.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_corp_name IS 'PARTNER CORP NAME is the Partners Corporate name.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_sin_ctn_bn IS 'PARTNER SIN CTN BUSINESS NUMBER is the Partners SIN/CTN/BN - see participant SIN/CTN/BN for valid formats.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_percent IS 'PARTNER PERCENT is the partners percentage share.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.partner_pin IS 'PARTNER PIN is CAIS pin of the partner, if one is available.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z05_partner_infos.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z05_partner_infos IS 'Z05 PARTNER INFORMATION identifies all the partners the producer has entered in section 6 of the Harmonized form. This should not include the participant. Even if the participant is in a partnership, there is no requirement to submit a list of participants, so this file may not have any data for that statement. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpi1_pp_py_on ON farms.farm_z05_partner_infos(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z05_partner_infos ADD 
    CONSTRAINT pk_zpi1 PRIMARY KEY (partner_info_key) USING INDEX TABLESPACE pg_default 
;
