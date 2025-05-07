CREATE TABLE scenario_log(
    scenario_log_id              numeric(10, 0)    NOT NULL,
    log_message                  varchar(2000)     NOT NULL,
    agristability_scenario_id    numeric(10, 0)    NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN scenario_log.scenario_log_id IS 'SCENARIO LOG ID is a surrogate unique identifier for SCENARIO LOG.'
;
COMMENT ON COLUMN scenario_log.log_message IS 'LOG MESSAGE is a system generated log about changes in the AGRISTABILITY SCENARIO. For example, logs will be generated when adjustments are made, when the scenario is assigned to someone, etc...'
;
COMMENT ON COLUMN scenario_log.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_log.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN scenario_log.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN scenario_log.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN scenario_log.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN scenario_log.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE INDEX ix_sl_asi ON scenario_log(agristability_scenario_id)
;
