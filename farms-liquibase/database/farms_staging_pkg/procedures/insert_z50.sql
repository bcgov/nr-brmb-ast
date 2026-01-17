create or replace procedure farms_staging_pkg.insert_z50(
   in in_benefit_calc_key farms.farm_z50_participnt_bnft_calcs.benefit_calc_key%type,
   in in_participant_pin farms.farm_z50_participnt_bnft_calcs.participant_pin%type,
   in in_program_year farms.farm_z50_participnt_bnft_calcs.program_year%type,
   in in_agristability_status farms.farm_z50_participnt_bnft_calcs.agristability_status%type,
   in in_unadjusted_reference_margin farms.farm_z50_participnt_bnft_calcs.unadjusted_reference_margin%type,
   in in_adjusted_reference_margin farms.farm_z50_participnt_bnft_calcs.adjusted_reference_margin%type,
   in in_program_margin farms.farm_z50_participnt_bnft_calcs.program_margin%type,
   in in_whole_farm_ind farms.farm_z50_participnt_bnft_calcs.whole_farm_ind%type,
   in in_structure_change_ind farms.farm_z50_participnt_bnft_calcs.structure_change_ind%type,
   in in_structure_change_adj_amount farms.farm_z50_participnt_bnft_calcs.structure_change_adj_amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z50_participnt_bnft_calcs (
        benefit_calc_key,
        participant_pin,
        program_year,
        agristability_status,
        unadjusted_reference_margin,
        adjusted_reference_margin,
        program_margin,
        whole_farm_ind,
        structure_change_ind,
        structure_change_adj_amount,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
