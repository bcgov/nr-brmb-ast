CREATE TABLE reference_scenario(
    reference_scenario_id            numeric(10, 0)    NOT NULL,
    used_in_calculation_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    deemed_farming_year_indicator    varchar(1)        DEFAULT 'Y' NOT NULL,
    agristability_scenario_id        numeric(10, 0)    NOT NULL,
    for_agristability_scenario_id    numeric(10, 0)    NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reference_scenario.reference_scenario_id IS 'REFERENCE SCENARIO ID is a surrogate unique identifier for REFERENCE SCENARIO.'
;
COMMENT ON COLUMN reference_scenario.used_in_calculation_indicator IS 'USED IN CALCULATION INDICATOR identifies if the AGRISTABILITY CLAIM record was used in the calculation of the claim.'
;
COMMENT ON COLUMN reference_scenario.deemed_farming_year_indicator IS 'DEEMED FARMING YEAR INDICATOR identifies if the REFERENCE SCENARIO had enough data to be comparable to other REFERENCE SCENARIOs.'
;
COMMENT ON COLUMN reference_scenario.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN reference_scenario.for_agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN reference_scenario.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reference_scenario.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reference_scenario.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reference_scenario.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reference_scenario.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reference_scenario IS 'REFERENCE SCENARIO records the previous five years of claims used for calculating a given year''s claim.'
;


CREATE INDEX ix_rs_fasi ON reference_scenario(for_agristability_scenario_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_rs_asi ON reference_scenario(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE reference_scenario ADD 
    CONSTRAINT pk_rs PRIMARY KEY (reference_scenario_id) USING INDEX TABLESPACE pg_default 
;
