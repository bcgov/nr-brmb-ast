create or replace procedure farms_staging_pkg.clear()
language plpgsql
as $$
begin
    execute 'alter table farms.farm_z51_participant_contribs alter constraint fk_zpc_zpfi deferrable initially deferred';
    execute 'alter table farms.farm_z50_participnt_bnft_calcs alter constraint fk_zpbc_zpfi deferrable initially deferred';
    execute 'alter table farms.farm_z42_participant_ref_years alter constraint fk_zpry_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z40_prtcpnt_ref_supl_dtls alter constraint fk_zprs_zpir deferrable initially deferred';
    execute 'alter table farms.farm_z40_prtcpnt_ref_supl_dtls alter constraint fk_zprsd_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z23_livestock_prod_cpcts alter constraint fk_zlpc_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z22_production_insurances alter constraint fk_zpi_zsi1 deferrable initially deferred';
    execute 'alter table farms.farm_z21_participant_suppls alter constraint fk_zps_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z05_partner_infos alter constraint fk_zpi_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z04_income_exps_dtls alter constraint fk_zied_zsi deferrable initially deferred';
    execute 'alter table farms.farm_z03_statement_infos alter constraint fk_zsi_zpfi deferrable initially deferred';
    execute 'alter table farms.farm_z02_partpnt_farm_infos alter constraint fk_zpfi_zpi deferrable initially deferred';

    execute 'truncate table farms.farm_z99_extract_file_ctls';
    execute 'truncate table farms.farm_z51_participant_contribs';
    execute 'truncate table farms.farm_z50_participnt_bnft_calcs';
    execute 'truncate table farms.farm_z42_participant_ref_years';
    execute 'truncate table farms.farm_z40_prtcpnt_ref_supl_dtls';
    execute 'truncate table farms.farm_z23_livestock_prod_cpcts';
    execute 'truncate table farms.farm_z22_production_insurances';
    execute 'truncate table farms.farm_z21_participant_suppls';
    execute 'truncate table farms.farm_z28_prod_insurance_refs cascade';
    execute 'truncate table farms.farm_z29_inventory_code_refs';
    execute 'truncate table farms.farm_z05_partner_infos';
    execute 'truncate table farms.farm_z04_income_exps_dtls';
    execute 'truncate table farms.farm_z03_statement_infos cascade';
    execute 'truncate table farms.farm_z02_partpnt_farm_infos cascade';
    execute 'truncate table farms.farm_z01_participant_infos cascade';

    execute 'alter table farms.farm_z02_partpnt_farm_infos alter constraint fk_zpfi_zpi not deferrable initially immediate';
    execute 'alter table farms.farm_z03_statement_infos alter constraint fk_zsi_zpfi not deferrable initially immediate';
    execute 'alter table farms.farm_z04_income_exps_dtls alter constraint fk_zied_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z05_partner_infos alter constraint fk_zpi_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z21_participant_suppls alter constraint fk_zps_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z22_production_insurances alter constraint fk_zpi_zsi1 not deferrable initially immediate';
    execute 'alter table farms.farm_z23_livestock_prod_cpcts alter constraint fk_zlpc_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z40_prtcpnt_ref_supl_dtls alter constraint fk_zprs_zpir not deferrable initially immediate';
    execute 'alter table farms.farm_z40_prtcpnt_ref_supl_dtls alter constraint fk_zprsd_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z42_participant_ref_years alter constraint fk_zpry_zsi not deferrable initially immediate';
    execute 'alter table farms.farm_z50_participnt_bnft_calcs alter constraint fk_zpbc_zpfi not deferrable initially immediate';
    execute 'alter table farms.farm_z51_participant_contribs alter constraint fk_zpc_zpfi not deferrable initially immediate';
end;
$$;
