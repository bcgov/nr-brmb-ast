create or replace procedure farms_staging_pkg.insert_z50(
   in in_benefit_calc_key farms.z50_participant_benefit_calculation.benefit_calculation_key%type,
   in in_participant_pin farms.z50_participant_benefit_calculation.participant_pin%type,
   in in_program_year farms.z50_participant_benefit_calculation.program_year%type,
   in in_agristability_status farms.z50_participant_benefit_calculation.agristability_status%type,
   in in_unadjusted_reference_margin farms.z50_participant_benefit_calculation.unadjusted_reference_margin%type,
   in in_adjusted_reference_margin farms.z50_participant_benefit_calculation.adjusted_reference_margin%type,
   in in_program_margin farms.z50_participant_benefit_calculation.program_margin%type,
   in in_whole_farm_ind farms.z50_participant_benefit_calculation.whole_farm_indicator%type,
   in in_structure_change_ind farms.z50_participant_benefit_calculation.structure_change_indicator%type,
   in in_structure_change_adj_amount farms.z50_participant_benefit_calculation.structure_change_adjustment_amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z50_participant_benefit_calculation (
        benefit_calculation_key,
        participant_pin,
        program_year,
        agristability_status,
        unadjusted_reference_margin,
        adjusted_reference_margin,
        program_margin,
        whole_farm_indicator,
        structure_change_indicator,
        structure_change_adjustment_amount,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_benefit_calc_key,
        in_participant_pin,
        in_program_year,
        in_agristability_status,
        in_unadjusted_reference_margin,
        in_adjusted_reference_margin,
        in_program_margin,
        in_whole_farm_ind,
        in_structure_change_ind,
        in_structure_change_adj_amount,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
