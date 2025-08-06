CREATE TABLE farms.farm_scenario_bpu_xref(
    scenario_bpu_xref_id         numeric(10, 0)    NOT NULL,
    agristability_scenario_id    numeric(10, 0)    NOT NULL,
    benchmark_per_unit_id        numeric(10, 0)    NOT NULL,
    scenario_bpu_purpose_code    varchar(10)       NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                  varchar(30)       NOT NULL,
    when_created                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                  varchar(30),
    when_updated                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_scenario_bpu_xref.scenario_bpu_xref_id IS 'SCENARIO BPU XREF ID is a surrogate unique identifier for an SCENARIO BPU XREF.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.benchmark_per_unit_id IS 'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.scenario_bpu_purpose_code IS 'SCENARIO BPU PURPOSE CODE is a unique code for the object SCENARIO BPU PURPOS CODE. Examples of codes and descriptions are STANDARD - Standard, CN_ADDITNL - Coverage Notice Additional.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_scenario_bpu_xref IS 'SCENARIO BPU XREF is the benchmark data that was used to determine that the REPORTED INCOME EXPENSEs for a FARMING OPERATION are resonable.'
;


CREATE INDEX ix_sbx_asi ON farms.farm_scenario_bpu_xref(agristability_scenario_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_sbx_bpui ON farms.farm_scenario_bpu_xref(benchmark_per_unit_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_sbx_sbpc ON farms.farm_scenario_bpu_xref(scenario_bpu_purpose_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_scenario_bpu_xref ADD 
    CONSTRAINT pk_sbx PRIMARY KEY (scenario_bpu_xref_id) USING INDEX TABLESPACE pg_default 
;
