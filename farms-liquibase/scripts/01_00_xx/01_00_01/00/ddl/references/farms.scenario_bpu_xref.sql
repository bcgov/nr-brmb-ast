ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT fk_sbx_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT fk_sbx_bpu 
    FOREIGN KEY (benchmark_per_unit_id)
    REFERENCES farms.farm_benchmark_per_units(benchmark_per_unit_id)
;

ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT fk_sbx_sbpc 
    FOREIGN KEY (scenario_bpu_purpose_code)
    REFERENCES farms.farm_scenario_bpu_purpos_codes(scenario_bpu_purpose_code)
;
