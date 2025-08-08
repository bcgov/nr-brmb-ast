CREATE TABLE farms.farm_reference_scenarios(
    reference_scenario_id            numeric(10, 0)    NOT NULL,
    used_in_calc_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    deemed_farming_year_ind    varchar(1)        DEFAULT 'Y' NOT NULL,
    agristability_scenario_id        numeric(10, 0)    NOT NULL,
    for_agristability_scenario_id    numeric(10, 0)    NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                      varchar(30)       NOT NULL,
    when_created                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                      varchar(30),
    when_updated                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_reference_scenarios.reference_scenario_id IS 'REFERENCE SCENARIO ID is a surrogate unique identifier for REFERENCE SCENARIO.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.used_in_calc_ind IS 'USED IN CALCULATION INDICATOR identifies if the AGRISTABILITY CLAIM record was used in the calculation of the claim.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.deemed_farming_year_ind IS 'DEEMED FARMING YEAR INDICATOR identifies if the REFERENCE SCENARIO had enough data to be comparable to other REFERENCE SCENARIOs.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.for_agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_reference_scenarios.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_reference_scenarios IS 'REFERENCE SCENARIO records the previous five years of claims used for calculating a given year''s claim.'
;


CREATE INDEX ix_rs_fasi ON farms.farm_reference_scenarios(for_agristability_scenario_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_rs_asi ON farms.farm_reference_scenarios(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_reference_scenarios ADD 
    CONSTRAINT pk_rs PRIMARY KEY (reference_scenario_id) USING INDEX TABLESPACE pg_default 
;
