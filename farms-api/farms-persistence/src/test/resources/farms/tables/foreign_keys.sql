ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_icc_fk FOREIGN KEY (inventory_class_code) REFERENCES farms.farm_inventory_class_codes(inventory_class_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_igc_fk FOREIGN KEY (inventory_group_code) REFERENCES farms.farm_inventory_group_codes(inventory_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_benchmark_years ADD CONSTRAINT farm_bpuy_farm_bpu_fk FOREIGN KEY (benchmark_per_unit_id) REFERENCES farms.farm_benchmark_per_units(benchmark_per_unit_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_configuration_parameters ADD CONSTRAINT farm_cp_farm_cptc_fk FOREIGN KEY (config_param_type_code) REFERENCES farms.farm_config_param_type_codes(config_param_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT farm_cucf_farm_cuc_fk FOREIGN KEY (target_crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT farm_cucf_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_fruit_veg_type_details ADD CONSTRAINT farm_fvtd_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_farm_iic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_farm_riic_fk FOREIGN KEY (rollup_inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ctc_fk FOREIGN KEY (commodity_type_code) REFERENCES farms.farm_commodity_type_codes(commodity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_mscc_fk FOREIGN KEY (multi_stage_commdty_code) REFERENCES farms.farm_multi_stage_commdty_codes(multi_stage_commdty_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_farm_ctc_fk FOREIGN KEY (commodity_type_code) REFERENCES farms.farm_commodity_type_codes(commodity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_farm_iic_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_farm_riic_fk FOREIGN KEY (rollup_structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_year_configuration_params ADD CONSTRAINT farm_ycp_farm_cptc_fk FOREIGN KEY (config_param_type_code) REFERENCES farms.farm_config_param_type_codes(config_param_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
