CREATE TABLE scenario_bpu_xref(
    scenario_bpu_xref_id         numeric(10, 0)    NOT NULL,
    agristability_scenario_id    numeric(10, 0)    NOT NULL,
    benchmark_per_unit_id        numeric(10, 0)    NOT NULL,
    scenario_bpu_purpose_code    varchar(10)       NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN scenario_bpu_xref.scenario_bpu_xref_id IS 'SCENARIO BPU XREF ID is a surrogate unique identifier for an SCENARIO BPU XREF.'
;
COMMENT ON COLUMN scenario_bpu_xref.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_bpu_xref.benchmark_per_unit_id IS 'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN scenario_bpu_xref.scenario_bpu_purpose_code IS 'SCENARIO BPU PURPOSE CODE is a unique code for the object SCENARIO BPU PURPOS CODE. Examples of codes and descriptions are STANDARD - Standard, CN_ADDITNL - Coverage Notice Additional.'
;
COMMENT ON COLUMN scenario_bpu_xref.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN scenario_bpu_xref.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN scenario_bpu_xref.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN scenario_bpu_xref.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN scenario_bpu_xref.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE scenario_bpu_xref IS 'SCENARIO BPU XREF is the benchmark data that was used to determine that the REPORTED INCOME EXPENSEs for a FARMING OPERATION are resonable.'
;


CREATE INDEX ix_sbx_asi ON scenario_bpu_xref(agristability_scenario_id)
;
CREATE INDEX ix_sbx_bpui ON scenario_bpu_xref(benchmark_per_unit_id)
;
CREATE INDEX ix_sbx_sbpc ON scenario_bpu_xref(scenario_bpu_purpose_code)
;
