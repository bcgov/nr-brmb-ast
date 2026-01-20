create or replace procedure farms_staging_pkg.clear()
language plpgsql
as $$
begin
    truncate table farms.farm_z99_extract_file_ctls;
    truncate table farms.farm_z51_participant_contribs;
    truncate table farms.farm_z50_participnt_bnft_calcs;
    truncate table farms.farm_z42_participant_ref_years;
    truncate table farms.farm_z40_prtcpnt_ref_supl_dtls;
    truncate table farms.farm_z23_livestock_prod_cpcts;
    truncate table farms.farm_z22_production_insurances;
    truncate table farms.farm_z21_participant_suppls;
    truncate table farms.farm_z28_prod_insurance_refs cascade;
    truncate table farms.farm_z29_inventory_code_refs;
    truncate table farms.farm_z05_partner_infos;
    truncate table farms.farm_z04_income_exps_dtls;
    truncate table farms.farm_z03_statement_infos cascade;
    truncate table farms.farm_z02_partpnt_farm_infos cascade;
    truncate table farms.farm_z01_participant_infos cascade;
end;
$$;
