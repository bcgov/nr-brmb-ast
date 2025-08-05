ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT farm_bpux_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT farm_bpux_farm_bpu_fk FOREIGN KEY (benchmark_per_unit_id) REFERENCES farms.farm_benchmark_per_units(benchmark_per_unit_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT farm_bpux_farm_sbpc_fk FOREIGN KEY (scenario_bpu_purpose_code) REFERENCES farms.farm_scenario_bpu_purpos_codes(scenario_bpu_purpose_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
