CREATE TABLE whole_farm_participant(
    whole_farm_participant_id                   numeric(10, 0)    NOT NULL,
    whole_farm_combined_pin                     numeric(9, 0)     NOT NULL,
    whole_farm_combined_pin_add_indicator       varchar(1),
    whole_farm_combined_pin_remove_indicator    varchar(1),
    program_year_version_id                     numeric(10, 0)    NOT NULL,
    revision_count                              numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                 varchar(30)       NOT NULL,
    create_date                                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                                 varchar(30),
    update_date                                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN whole_farm_participant.whole_farm_participant_id IS 'WHOLE FARM PARTICIPANT ID is a surrogate unique identifier for WHOLE FARM.'
;
COMMENT ON COLUMN whole_farm_participant.whole_farm_combined_pin IS 'WHOLE FARM COMBINED PIN of Participant that has been combined with the participant on this form for whole farms calculation, see Section 2 - Participant  Profile.'
;
COMMENT ON COLUMN whole_farm_participant.whole_farm_combined_pin_add_indicator IS 'WHOLE FARM COMBINED PIN ADD INDICATOR of Participant that has been combined with the participant on this form for whole farms calculation, see Section 2 - Participant  Profile.'
;
COMMENT ON COLUMN whole_farm_participant.whole_farm_combined_pin_remove_indicator IS 'WHOLE FARM COMBINED PIN REMOVE INDICATOR should be removed from the  PINs combined with this participant for whole farms.'
;
COMMENT ON COLUMN whole_farm_participant.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON COLUMN whole_farm_participant.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN whole_farm_participant.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN whole_farm_participant.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN whole_farm_participant.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN whole_farm_participant.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE whole_farm_participant IS 'WHOLE FARM PARTICIPANT describes the combined PINS used by the FARMING OPERATION for a given PROGRAM YEAR VERSION.'
;


CREATE INDEX ix_wfp_pyvi ON whole_farm_participant(program_year_version_id)
 TABLESPACE pg_default
;

ALTER TABLE whole_farm_participant ADD 
    CONSTRAINT pk_wfp PRIMARY KEY (whole_farm_participant_id) USING INDEX TABLESPACE pg_default 
;
