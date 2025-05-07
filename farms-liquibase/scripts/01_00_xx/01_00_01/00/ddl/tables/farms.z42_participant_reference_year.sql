CREATE TABLE z42_participant_reference_year(
    productive_capacity_key      numeric(10, 0)    NOT NULL,
    participant_pin              numeric(9, 0)     NOT NULL,
    program_year                 numeric(4, 0)     NOT NULL,
    operation_number             numeric(4, 0)     NOT NULL,
    productive_type_code         numeric(2, 0)     NOT NULL,
    productive_code              numeric(5, 0)     NOT NULL,
    productive_capacity_units    numeric(13, 2),
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z42_participant_reference_year.productive_capacity_key IS 'PRODUCTIVE CAPACITY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN z42_participant_reference_year.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z42_participant_reference_year.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z42_participant_reference_year.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN z42_participant_reference_year.productive_type_code IS 'PRODUCTIVE TYPE CODE indicates if this is an Inventory Code 1, or a STRUCTURE GROUP CODE - 2.'
;
COMMENT ON COLUMN z42_participant_reference_year.productive_code IS 'PRODUCTIVE CODE is the structure group or inventory code.'
;
COMMENT ON COLUMN z42_participant_reference_year.productive_capacity_units IS 'PRODUCTIVE CAPACITY UNITS is the number of units of production FIPD has used when processing future year payments. # of head for livestock, the amount of acres/square meters for crop production.'
;
COMMENT ON COLUMN z42_participant_reference_year.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z42_participant_reference_year.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z42_participant_reference_year.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z42_participant_reference_year.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z42_participant_reference_year.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z42_participant_reference_year IS 'Z42 PARTICIPANT REFERENCE YEAR identifies reference year data by for productive units by both inventory codes and structure groups.  The productive capacity for each code (inventory or structure group) is displayed in column 7. This file will have multiple rows per participant and farming operation.This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;

