create or replace procedure farms_staging_pkg.clear()
language plpgsql
as $$
begin
    truncate table farms.z99_extract_file;
    truncate table farms.z51_participant_contribution;
    truncate table farms.z50_participant_benefit_calculation;
    truncate table farms.z42_participant_reference_year;
    truncate table farms.z40_participant_reference_supplemental_detail;
    truncate table farms.z23_livestock_production_capacity;
    truncate table farms.z22_production_insurance;
    truncate table farms.z21_participant_supplementary;
    truncate table farms.z28_production_insurance_reference cascade;
    truncate table farms.z29_inventory_code_reference;
    truncate table farms.z05_partner_information;
    truncate table farms.z04_income_expenses_detail;
    truncate table farms.z03_statement_information cascade;
    truncate table farms.z02_participant_farm_information cascade;
    truncate table farms.z01_participant_information cascade;
end;
$$;
