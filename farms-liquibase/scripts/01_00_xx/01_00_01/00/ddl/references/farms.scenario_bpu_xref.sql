ALTER TABLE farms.scenario_bpu_xref ADD CONSTRAINT fk_sbx_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.scenario_bpu_xref ADD CONSTRAINT fk_sbx_bpu 
    FOREIGN KEY (benchmark_per_unit_id)
    REFERENCES farms.benchmark_per_unit(benchmark_per_unit_id)
;

ALTER TABLE farms.scenario_bpu_xref ADD CONSTRAINT fk_sbx_sbpc 
    FOREIGN KEY (scenario_bpu_purpose_code)
    REFERENCES farms.scenario_bpu_purpose_code(scenario_bpu_purpose_code)
;
