create or replace procedure farms_staging_pkg.clear()
language plpgsql
as $$
begin
    execute 'alter table farms.z51_participant_contribution alter constraint fk_zpc_zpfi deferrable initially deferred';
    execute 'alter table farms.z50_participant_benefit_calculation alter constraint fk_zpbc_zpfi deferrable initially deferred';
    execute 'alter table farms.z42_participant_reference_year alter constraint fk_zpry_zsi deferrable initially deferred';
    execute 'alter table farms.z40_participant_reference_supplemental_detail alter constraint fk_zprs_zpir deferrable initially deferred';
    execute 'alter table farms.z40_participant_reference_supplemental_detail alter constraint fk_zprsd_zsi deferrable initially deferred';
    execute 'alter table farms.z23_livestock_production_capacity alter constraint fk_zlpc_zsi deferrable initially deferred';
    execute 'alter table farms.z22_production_insurance alter constraint fk_zpi_zsi1 deferrable initially deferred';
    execute 'alter table farms.z21_participant_supplementary alter constraint fk_zps_zsi deferrable initially deferred';
    execute 'alter table farms.z05_partner_information alter constraint fk_zpi_zsi deferrable initially deferred';
    execute 'alter table farms.z04_income_expenses_detail alter constraint fk_zied_zsi deferrable initially deferred';
    execute 'alter table farms.z03_statement_information alter constraint fk_zsi_zpfi deferrable initially deferred';
    execute 'alter table farms.z02_participant_farm_information alter constraint fk_zpfi_zpi deferrable initially deferred';

    execute 'truncate table farms.z99_extract_file';
    execute 'truncate table farms.z51_participant_contribution';
    execute 'truncate table farms.z50_participant_benefit_calculation';
    execute 'truncate table farms.z42_participant_reference_year';
    execute 'truncate table farms.z40_participant_reference_supplemental_detail';
    execute 'truncate table farms.z23_livestock_production_capacity';
    execute 'truncate table farms.z22_production_insurance';
    execute 'truncate table farms.z21_participant_supplementary';
    execute 'truncate table farms.z28_production_insurance_reference';
    execute 'truncate table farms.z29_inventory_code_reference';
    execute 'truncate table farms.z05_partner_information';
    execute 'truncate table farms.z04_income_expenses_detail';
    execute 'truncate table farms.z03_statement_information';
    execute 'truncate table farms.z02_participant_farm_information';
    execute 'truncate table farms.z01_participant_information';

    execute 'alter table farms.z02_participant_farm_information alter constraint fk_zpfi_zpi not deferrable initially immediate';
    execute 'alter table farms.z03_statement_information alter constraint fk_zsi_zpfi not deferrable initially immediate';
    execute 'alter table farms.z04_income_expenses_detail alter constraint fk_zied_zsi not deferrable initially immediate';
    execute 'alter table farms.z05_partner_information alter constraint fk_zpi_zsi not deferrable initially immediate';
    execute 'alter table farms.z21_participant_supplementary alter constraint fk_zps_zsi not deferrable initially immediate';
    execute 'alter table farms.z22_production_insurance alter constraint fk_zpi_zsi1 not deferrable initially immediate';
    execute 'alter table farms.z23_livestock_production_capacity alter constraint fk_zlpc_zsi not deferrable initially immediate';
    execute 'alter table farms.z40_participant_reference_supplemental_detail alter constraint fk_zprs_zpir not deferrable initially immediate';
    execute 'alter table farms.z40_participant_reference_supplemental_detail alter constraint fk_zprsd_zsi not deferrable initially immediate';
    execute 'alter table farms.z42_participant_reference_year alter constraint fk_zpry_zsi not deferrable initially immediate';
    execute 'alter table farms.z50_participant_benefit_calculation alter constraint fk_zpbc_zpfi not deferrable initially immediate';
    execute 'alter table farms.z51_participant_contribution alter constraint fk_zpc_zpfi not deferrable initially immediate';
end;
$$;
