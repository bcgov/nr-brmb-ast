CREATE TABLE farms.farm_scenario_logs(
    scenario_log_id              numeric(10, 0)    NOT NULL,
    log_message                  varchar(2000)     NOT NULL,
    agristability_scenario_id    numeric(10, 0)    NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                  varchar(30)       NOT NULL,
    when_created                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                  varchar(30),
    when_updated                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_scenario_logs.scenario_log_id IS 'SCENARIO LOG ID is a surrogate unique identifier for SCENARIO LOG.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.log_message IS 'LOG MESSAGE is a system generated log about changes in the AGRISTABILITY SCENARIO. For example, logs will be generated when adjustments are made, when the scenario is assigned to someone, etc...'
;
COMMENT ON COLUMN farms.farm_scenario_logs.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_logs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE INDEX ix_sl_asi ON farms.farm_scenario_logs(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_scenario_logs ADD 
    CONSTRAINT pk_sl PRIMARY KEY (scenario_log_id) USING INDEX TABLESPACE pg_default 
;
