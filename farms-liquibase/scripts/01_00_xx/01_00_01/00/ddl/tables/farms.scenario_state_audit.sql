CREATE TABLE scenario_state_audit(
    scenario_state_audit_id      numeric(10, 0)    NOT NULL,
    state_change_reason          varchar(2000),
    agristability_scenario_id    numeric(10, 0)    NOT NULL,
    scenario_state_code          varchar(10)       NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN scenario_state_audit.scenario_state_audit_id IS 'SCENARIO STATE AUDIT ID is a unique identifier for SCENARIO STATE AUDITs.'
;
COMMENT ON COLUMN scenario_state_audit.state_change_reason IS 'STATE CHANGE REASON describes the reason for changing the AGRISTABILITY STATE CODE of the PROGRAM YEAR VERSION.'
;
COMMENT ON COLUMN scenario_state_audit.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_state_audit.scenario_state_code IS 'AGRISTABILITY STATE CODE is a unique code for the object AGRISTABILITY STATE CODE described as a character code used to uniquely identify the state of the PROGRAM YEAR VERSION. Examples of codes and descriptions are : INP - IN PROGRESS,  APP - APPROVED, PD - PAID.'
;
COMMENT ON COLUMN scenario_state_audit.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN scenario_state_audit.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN scenario_state_audit.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN scenario_state_audit.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN scenario_state_audit.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE scenario_state_audit IS 'SCENARIO STATE AUDIT tracks changes to the state of the AGRISTABILITY SCENARIO.'
;


CREATE INDEX ix_ssa_asi ON scenario_state_audit(agristability_scenario_id)
;
CREATE INDEX ix_ssa_ssc ON scenario_state_audit(scenario_state_code)
;
