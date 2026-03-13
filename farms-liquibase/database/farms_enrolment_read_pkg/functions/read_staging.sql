create or replace function farms_enrolment_read_pkg.read_staging()
returns table(
    participant_pin             farms.farm_zprogram_enrolments.participant_pin%type,
    enrolment_year              farms.farm_zprogram_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_zprogram_enrolments.enrolment_fee%type,
    failed_to_generate_ind      farms.farm_zprogram_enrolments.failed_to_generate_ind%type,
    error_ind                   farms.farm_zprogram_enrolments.error_ind%type,
    generated_from_cra_ind      farms.farm_zprogram_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_zprogram_enrolments.generated_from_enw_ind%type,
    failed_reason               farms.farm_zprogram_enrolments.failed_reason%type,
    generated_date              farms.farm_zprogram_enrolments.generated_date%type,
    contribution_margin_average farms.farm_zprogram_enrolments.contribution_margin_average%type,
    margin_year_minus_2         farms.farm_zprogram_enrolments.margin_year_minus_2%type,
    margin_year_minus_3         farms.farm_zprogram_enrolments.margin_year_minus_3%type,
    margin_year_minus_4         farms.farm_zprogram_enrolments.margin_year_minus_4%type,
    margin_year_minus_5         farms.farm_zprogram_enrolments.margin_year_minus_5%type,
    margin_year_minus_6         farms.farm_zprogram_enrolments.margin_year_minus_6%type,
    margin_year_minus_2_ind     farms.farm_zprogram_enrolments.margin_year_minus_2_ind%type,
    margin_year_minus_3_ind     farms.farm_zprogram_enrolments.margin_year_minus_3_ind%type,
    margin_year_minus_4_ind     farms.farm_zprogram_enrolments.margin_year_minus_4_ind%type,
    margin_year_minus_5_ind     farms.farm_zprogram_enrolments.margin_year_minus_5_ind%type,
    margin_year_minus_6_ind     farms.farm_zprogram_enrolments.margin_year_minus_6_ind%type,
    create_task_in_barn_ind     farms.farm_zprogram_enrolments.create_task_in_barn_ind%type,
    combined_farm_percent       farms.farm_zprogram_enrolments.combined_farm_percent%type,
    agristability_scenario_id   farms.farm_zprogram_enrolments.agristability_scenario_id%type
)
language sql
as $$
    select z.participant_pin,
           z.enrolment_year,
           z.enrolment_fee,
           z.failed_to_generate_ind,
           z.error_ind,
           z.generated_from_cra_ind,
           z.generated_from_enw_ind,
           z.failed_reason,
           z.generated_date,
           z.contribution_margin_average,
           z.margin_year_minus_2,
           z.margin_year_minus_3,
           z.margin_year_minus_4,
           z.margin_year_minus_5,
           z.margin_year_minus_6,
           z.margin_year_minus_2_ind,
           z.margin_year_minus_3_ind,
           z.margin_year_minus_4_ind,
           z.margin_year_minus_5_ind,
           z.margin_year_minus_6_ind,
           z.create_task_in_barn_ind,
           z.combined_farm_percent,
           z.agristability_scenario_id
    from farms.farm_zprogram_enrolments z
    order by z.participant_pin;
$$;
