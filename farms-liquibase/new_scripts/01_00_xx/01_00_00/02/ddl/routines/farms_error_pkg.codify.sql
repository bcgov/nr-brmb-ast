create or replace function farms_error_pkg.codify(
    in msg varchar
)
returns boolean
language plpgsql
as $$
begin
    if msg is null then
        return null;
    elsif msg like '%farm_program_year_versions_pkey%' then
        return 'Program Year Version PK violation';
    elsif msg like '%farm_inventory_class_codes_pkey%' then
        return 'Inventory Class Code PK violation';
    elsif msg like '%farm_municipality_codes_pkey%' then
        return 'Municipality Code PK violation';
    elsif msg like '%farm_bpu_farm_mc_fk%' then
        return 'The specified Municipality Code was not found for this BPU';
    elsif msg like '%farm_bpu_farm_ic_fk%' then
        return 'The specified Inventory Code was not found for this BPU ';
    elsif msg like '%farm_csub_farm_asc_fk%' then
        return 'The specified Agristability Client was not found for this Subscription ';
    elsif msg like '%farm_csub_farm_fssc_fk%' then
        return 'The specified Subscription Status Code was not found for this Subscription ';
    elsif msg like '%farm_csub_farm_asr_fk%' then
        return 'The specified Representative was not found for this Subscription';
    elsif msg like '%farm_rs_farm_as_fk%' then
        return 'The specified Scenario was not found for this Reference Year Scenario';
    elsif msg like '%farm_rthe_referencing_scena_fk%' then
        return 'The specified Reference Scenario was not found for this Scenario';
    elsif msg like '%farm_bpux_farm_bpu_fk%' then
        return 'The specified BPU was not found for this BPU cross reference entry';
    elsif msg like '%farm_bpux_farm_as_fk%' then
        return 'The specified Scenario was not found for this BPU cross reference entry';
    elsif msg like '%farm_z51_farm_z02_fk%' then
        return 'File 51 record does not have a parent record in file 2';
    elsif msg like '%farm_z51_participant_contribs_pkey%' then
        return 'File 51 PK violation';
    elsif msg like '%farm_reported_income_expenses_pkey%' then
        return 'Reported Income Expense PK violation';
    elsif msg like '%farm_benchmark_per_units_pkey%' then
        return 'Benchmark Per Unit PK violation';
    elsif msg like '%farm_client_subscriptions_pkey%' then
        return 'Client Subscription PK violation';
    elsif msg like '%farm_reference_scenarios_pkey%' then
        return 'Reference Scenario PK violation';
    elsif msg like '%farm_productve_unit_capacities_pkey%' then
        return 'Productive Unit Capacity PK violation';
    elsif msg like '%farm_federal_accounting_codes_pkey%' then
        return 'Federal Accounting Code PK violation';
    elsif msg like '%farm_zfmv_fair_market_values_pkey%' then
        return 'FMV staging table PK violation';
    elsif msg like '%farm_scenario_bpu_xref_pkey%' then
        return 'BPU Cross Reference PK violation';
    elsif msg like '%farm_z03_statement_infos_pkey%' then
        return 'File 3 PK violation';
    elsif msg like '%farm_scenario_state_audits_pkey%' then
        return 'Scenario State Audit PK violation';
    elsif msg like '%farm_benchmark_years_pkey%' then
        return 'Benchmark Year PK violation';
    elsif msg like '%farm_import_versions_pkey%' then
        return 'Import Version PK violation';
    elsif msg like '%farm_production_insurances_pkey%' then
        return 'Production Insurance PK violation';
    elsif msg like '%farm_z99_extract_file_ctls_pkey%' then
        return 'File 99 PK violation';
    elsif msg like '%farm_z29_inventory_code_refs_pkey%' then
        return 'File 29 PK violation';
    elsif msg like '%farm_benefit_calc_totals_pkey%' then
        return 'Benefit Calculated Total PK violation';
    elsif msg like '%farm_z01_participant_infos_pkey%' then
        return 'File 1 PK violation';
    elsif msg like '%farm_inventory_item_codes_pkey%' then
        return 'Income Expense PK violation';
    elsif msg like '%farm_fair_market_values_pkey%' then
        return 'Fair Market Value PK violation';
    elsif msg like '%farm_z42_participant_ref_years_pkey%' then
        return 'File 42 PK violation';
    elsif msg like '%farm_z23_livestock_prod_cpcts_pkey%' then
        return 'File 23 PK violation';
    elsif msg like '%farm_whole_farm_participants_pkey%' then
        return 'Whole Farm Participant PK violation';
    elsif msg like '%farm_agristabilty_cmmdty_xref_pkey%' then
        return 'Agristability Commodity Cross Reference PK violation';
    elsif msg like '%farm_subscription_status_codes_pkey%' then
        return 'Subscription Status Code PK violation';
    elsif msg like '%farm_z28_prod_insurance_refs_pkey%' then
        return 'File 28 PK violation';
    elsif msg like '%farm_reported_inventories_pkey%' then
        return 'Reported Inventory PK violation';
    elsif msg like '%farm_farming_operations_pkey%' then
        return 'Farming Operation PK violation';
    elsif msg like '%farm_z05_partner_infos_pkey%' then
        return 'File 5 PK violation';
    elsif msg like '%farm_z40_prtcpnt_ref_supl_dtls_pkey%' then
        return 'File 40 PK violation';
    elsif msg like '%farm_z04_income_exps_dtls_pkey%' then
        return 'File 4 PK violation';
    elsif msg like '%farm_z21_participant_suppls_pkey%' then
        return 'File 21 PK violation';
    elsif msg like '%farm_z50_participnt_bnft_calcs_pkey%' then
        return 'File 50 PK violation';
    elsif msg like '%farm_program_years_pkey%' then
        return 'Program Year PK violation';
    elsif msg like '%farm_z02_partpnt_farm_infos_pkey%' then
        return 'File 2 PK violation';
    elsif msg like '%farm_agristabilty_cmmdty_xref_inventory_class_code_inventor_key%' then
        return 'Agristability Commodity Cross Reference combination is not unique';
    elsif msg like '%farm_z03_farm_z02_fk%' then
        return 'File 3 record does not have a parent record in file 2';
    elsif msg like '%farm_ssa_farm_as_fk%' then
        return 'The specified Scenario was not found for this Scenario State Audit entry';
    elsif msg like '%farm_ssa_farm_ssc_fk%' then
        return 'The specified Scenario State Code was not found for this Scenario State Audit entry';
    elsif msg like '%farm_bpuy_farm_bpu_fk%' then
        return 'The specified BPU was not found for this Benchmark Year ';
    elsif msg like '%farm_iv_farm_isc_fk%' then
        return 'The specified Import State Code was not found for this Import Version entry';
    elsif msg like '%farm_iv_farm_iscc_fk%' then
        return 'The specified Import Class Code was not found for this Import Version entry';
    elsif msg like '%farm_pi_farm_fo_fk%' then
        return 'The specified Operation was not found for this Production Insurance entry';
    elsif msg like '%farm_bcmt_farm_as_fk%' then
        return 'The specified Scenario was not found for this Calculated Benefit';
    elsif msg like '%farm_fmv_farm_mc_fk%' then
        return 'The specified Municipality Code was not found for this FMV entry';
    elsif msg like '%farm_fmv_farm_cuc_fk%' then
        return 'The specified Crop Unit Code was not found for this FMV entry';
    elsif msg like '%farm_fmv_farm_ic_fk%' then
        return 'The specified Inventory Code was not found for this FMV entry';
    elsif msg like '%farm_z42_farm_z03_fk%' then
        return 'File 42 record does not have a parent record in file 3';
    elsif msg like '%farm_z23_farm_z03_fk%' then
        return 'File 23 record does not have a parent record in file 3';
    elsif msg like '%farm_wfp_farm_pyv_fk%' then
        return 'The specified Program Year Version was not found for this Whole Farm Participant';
    elsif msg like '%farm_acx_farm_igc_fk%' then
        return 'The specified Inventory Group Code was not found for this Commodity cross reference entry';
    elsif msg like '%farm_acx_farm_icc_fk%' then
        return 'The specified Inventory Class Code was not found for this Commodity cross reference entry';
    elsif msg like '%farm_acx_farm_ic_fk%' then
        return 'The specified Inventory Code was not found for this Commodity cross reference entry';
    elsif msg like '%farm_z05_farm_z03_fk%' then
        return 'File 5 record does not have a parent record in file 3';
    elsif msg like '%farm_z40_farm_z03_fk%' then
        return 'File 40 record does not have a parent record in file 3';
    elsif msg like '%farm_z40_farm_z28_fk%' then
        return 'File 40 record does not have a parent record in file 28';
    elsif msg like '%farm_z04_farm_z03_fk%' then
        return 'File 4 record does not have a parent record in file 3';
    elsif msg like '%farm_z21_farm_z03_fk%' then
        return 'File 21 record does not have a parent record in file 3';
    elsif msg like '%farm_z50_farm_z02_fk%' then
        return 'File 50 record does not have a parent record in file 2';
    elsif msg like '%farm_py_farm_asc_fk%' then
        return 'The specified Client was not found for this Program year';
    elsif msg like '%farm_z02_farm_z01_fk%' then
        return 'File 2 record does not have a parent record in file 1';
    elsif msg like '%farm_inventory_group_codes_pkey%' then
        return 'Inventory Group Code PK violation';
    elsif msg like '%farm_participant_class_codes_pkey%' then
        return 'Productive Capacity Code PK violation';
    elsif msg like '%farm_federal_status_codes_pkey%' then
        return 'Federal Status Code PK violation';
    elsif msg like '%farm_crop_unit_codes_pkey%' then
        return 'Crop Unit Code PK violation';
    elsif msg like '%farm_scenario_state_codes_pkey%' then
        return 'Scenario State Code PK violation';
    elsif msg like '%farm_agristability_clients_pkey%' then
        return 'Agristability Client PK violation';
    elsif msg like '%farm_persons_pkey%' then
        return 'Person PK violation';
    elsif msg like '%farm_structural_change_codes_pkey%' then
        return 'Structural Change Code PK violation';
    elsif msg like '%farm_import_state_codes_pkey%' then
        return 'Import State Code PK violation';
    elsif msg like '%farm_zbpu_benchmark_per_units_pkey%' then
        return 'BPU Staging file PK violation';
    elsif msg like '%farm_scenario_class_codes_pkey%' then
        return 'Scenario Class Code PK violation';
    elsif msg like '%farm_z22_production_insurances_pkey%' then
        return 'File 22 PK violation';
    elsif msg like '%farm_agristability_claims_pkey%' then
        return 'Agristability Claim PK violation';
    elsif msg like '%farm_participant_profile_codes_pkey%' then
        return 'Participant Profile Code PK violation';
    elsif msg like '%farm_import_class_codes_pkey%' then
        return 'Import Class Code PK violation';
    elsif msg like '%farm_benefit_calc_margins_pkey%' then
        return 'Benefit Calculated Margin PK violation';
    elsif msg like '%farm_participant_lang_codes_pkey%' then
        return 'Participant Language Code PK violation';
    elsif msg like '%farm_agristability_scenarios_pkey%' then
        return 'Agristability Scenario PK violation';
    elsif msg like '%farm_import_logs_pkey%' then
        return 'Import Log PK violation';
    elsif msg like '%farm_agristability_represntves_pkey%' then
        return 'Agristability Representative PK violation';
    elsif msg like '%farm_farming_operatin_prtnrs_pkey%' then
        return 'Farming Operation Partner PK violation';
    elsif msg like '%farm_structure_group_codes_pkey%' then
        return 'Structure Group Code PK violation';
    elsif msg like '%farm_agristability_clients_participant_pin_key%' then
        return 'Agristability Client/Participant PIN is not unique';
    elsif msg like '%farm_agristability_scenarios_program_year_version_id_scenar_key%' then
        return 'Program Year Version/Scenario Number is not unique';
    elsif msg like '%farm_agristability_represntves_user_guid_key%' then
        return 'Agrstability Representative/GUID is not unique';
    elsif msg like '%farm_agristability_represntves_userid_key%' then
        return 'Agrstability Representative/User ID combination is not unique';
    elsif msg like '%farm_z22_farm_z03_fk%' then
        return 'File 22 record does not have a parent record in file 3';
    elsif msg like '%farm_acl_farm_as_fk%' then
        return 'The specified Scenario was not found for this Claim';
    elsif msg like '%farm_bcm_farm_fo_fk%' then
        return 'The specified Operation was not found for this Calculated Margin';
    elsif msg like '%farm_bcm_farm_as_fk%' then
        return 'The specified Scenario was not found for this Calculated Margin';
    elsif msg like '%farm_as_farm_ssc_fk%' then
        return 'The specified Scenario State Code was not found for this Scenario';
    elsif msg like '%farm_as_farm_pyv_fk%' then
        return 'The specified Program Year Version was not found for this Scenario';
    elsif msg like '%farm_as_farm_stc_fk%' then
        return 'The specified Scenario Class Code was not found for this Scenario';
    elsif msg like '%farm_ilg_farm_iv_fk%' then
        return 'The specified Import Version was not found for this Import Log entry';
    elsif msg like '%farm_ilg_farm_pyv_fk%' then
        return 'The specified Program Year Version was not found for this Import Log entry';
    elsif msg like '%farm_fop_farm_fo_fk%' then
        return 'The specified Operation was not found for this Partner';
    else
        return msg;
    end if;
end;
$$;
