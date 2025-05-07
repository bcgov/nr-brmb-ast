CREATE TABLE z51_participant_contribution(
    contribution_key            numeric(10, 0)    NOT NULL,
    participant_pin             numeric(9, 0)     NOT NULL,
    program_year                numeric(4, 0)     NOT NULL,
    provincial_contributions    numeric(13, 2)    NOT NULL,
    federal_contributions       numeric(13, 2)    NOT NULL,
    interim_contributions       numeric(13, 2)    NOT NULL,
    producer_share              numeric(13, 2)    NOT NULL,
    revision_count              numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                 varchar(30)       NOT NULL,
    create_date                 timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                 varchar(30),
    update_date                 timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z51_participant_contribution.contribution_key IS 'CONTRIBUTION KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN z51_participant_contribution.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z51_participant_contribution.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z51_participant_contribution.provincial_contributions IS 'PROVINCIAL CONTRIBUTIONS is the amount of PROVINCIAL CONTRIBUTIONS this participant has received.'
;
COMMENT ON COLUMN z51_participant_contribution.federal_contributions IS 'FEDERAL CONTRIBUTIONS is the amount of FEDERAL CONTRIBUTIONS this participant has received.'
;
COMMENT ON COLUMN z51_participant_contribution.interim_contributions IS 'INTERIM CONTRIBUTIONS is the amount of INTERIM CONTRIBUTIONS for this participant, if a final calculation is not yet been made. If a final calculation has been made, this amount will be 0.'
;
COMMENT ON COLUMN z51_participant_contribution.producer_share IS 'PRODUCER SHARE is the amount of AgriStability withdrawals that are producer''s share.'
;
COMMENT ON COLUMN z51_participant_contribution.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z51_participant_contribution.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z51_participant_contribution.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z51_participant_contribution.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z51_participant_contribution.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z51_participant_contribution IS 'Z51 PARTICIPANT CONTRIBUTION identifies contributions by the participant for the program year, broken out by type. This data can only be provided for years FIPD has processed. This file is created by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpc_pp_py ON z51_participant_contribution(participant_pin, program_year)
 TABLESPACE pg_default
;
