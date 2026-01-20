create or replace procedure farms_enrolment_write_pkg.update_staging(
    in in_participant_pin farms.farm_zprogram_enrolments.participant_pin%type,
    in in_enrolment_year farms.farm_zprogram_enrolments.enrolment_year%type,
    in in_enrolment_fee farms.farm_zprogram_enrolments.enrolment_fee%type,
    in in_failed_to_generate_ind farms.farm_zprogram_enrolments.failed_to_generate_ind%type,
    in in_error_ind farms.farm_zprogram_enrolments.error_ind%type,
    in in_failed_reason farms.farm_zprogram_enrolments.failed_reason%type,
    in in_contribution_margin_average farms.farm_zprogram_enrolments.contribution_margin_average%type,
    in in_margin_year_minus_2 farms.farm_zprogram_enrolments.margin_year_minus_2%type,
    in in_margin_year_minus_3 farms.farm_zprogram_enrolments.margin_year_minus_3%type,
    in in_margin_year_minus_4 farms.farm_zprogram_enrolments.margin_year_minus_4%type,
    in in_margin_year_minus_5 farms.farm_zprogram_enrolments.margin_year_minus_5%type,
    in in_margin_year_minus_6 farms.farm_zprogram_enrolments.margin_year_minus_6%type,
    in in_margin_year_minus_2_ind farms.farm_zprogram_enrolments.margin_year_minus_2_ind%type,
    in in_margin_year_minus_3_ind farms.farm_zprogram_enrolments.margin_year_minus_3_ind%type,
    in in_margin_year_minus_4_ind farms.farm_zprogram_enrolments.margin_year_minus_4_ind%type,
    in in_margin_year_minus_5_ind farms.farm_zprogram_enrolments.margin_year_minus_5_ind%type,
    in in_margin_year_minus_6_ind farms.farm_zprogram_enrolments.margin_year_minus_6_ind%type,
    in in_generated_from_cra_ind farms.farm_zprogram_enrolments.generated_from_cra_ind%type,
    in in_generated_from_enw_ind farms.farm_zprogram_enrolments.generated_from_enw_ind%type,
    in in_create_task_in_barn_ind farms.farm_zprogram_enrolments.create_task_in_barn_ind%type,
    in in_combined_farm_percent farms.farm_program_enrolments.combined_farm_percent%type,
    in in_agristability_scenario_id farms.farm_zprogram_enrolments.agristability_scenario_id%type,
    in in_user farms.farm_zprogram_enrolments.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_zprogram_enrolments
    set enrolment_fee = in_enrolment_fee,
        failed_to_generate_ind = in_failed_to_generate_ind,
        error_ind = in_error_ind,
        failed_reason = in_failed_reason,
        generated_date = current_timestamp,
        contribution_margin_average = in_contribution_margin_average,
        margin_year_minus_2 = in_margin_year_minus_2,
        margin_year_minus_3 = in_margin_year_minus_3,
        margin_year_minus_4 = in_margin_year_minus_4,
        margin_year_minus_5 = in_margin_year_minus_5,
        margin_year_minus_6 = in_margin_year_minus_6,
        margin_year_minus_2_ind = coalesce(in_margin_year_minus_2_ind, margin_year_minus_2_ind),
        margin_year_minus_3_ind = coalesce(in_margin_year_minus_3_ind, margin_year_minus_2_ind),
        margin_year_minus_4_ind = coalesce(in_margin_year_minus_4_ind, margin_year_minus_2_ind),
        margin_year_minus_5_ind = coalesce(in_margin_year_minus_5_ind, margin_year_minus_2_ind),
        margin_year_minus_6_ind = coalesce(in_margin_year_minus_6_ind, margin_year_minus_2_ind),
        generated_from_cra_ind = coalesce(in_generated_from_cra_ind, generated_from_cra_ind),
        generated_from_enw_ind = coalesce(in_generated_from_enw_ind, generated_from_enw_ind),
        create_task_in_barn_ind = coalesce(in_create_task_in_barn_ind, create_task_in_barn_ind),
        combined_farm_percent = in_combined_farm_percent,
        agristability_scenario_id = in_agristability_scenario_id,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where participant_pin = in_participant_pin
    and enrolment_year = in_enrolment_year;

end;
$$;
