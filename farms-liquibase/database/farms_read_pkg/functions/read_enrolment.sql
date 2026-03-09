create or replace function farms_read_pkg.read_enrolment(
    in in_pin farms.farm_agristability_clients.participant_pin%type,
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type
)
returns table(
    agristability_client_id     farms.farm_agristability_clients.agristability_client_id%type,
    participant_pin             farms.farm_agristability_clients.participant_pin%type,
    failed_to_generate_ind      farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason               farms.farm_program_enrolments.failed_reason%type,
    program_enrolment_id        farms.farm_program_enrolments.program_enrolment_id%type,
    enrolment_year              farms.farm_program_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_program_enrolments.enrolment_fee%type,
    generated_date              farms.farm_program_enrolments.generated_date%type,
    contribution_margin_average farms.farm_program_enrolments.contribution_margin_average%type,
    margin_year_minus_2         farms.farm_program_enrolments.margin_year_minus_2%type,
    margin_year_minus_3         farms.farm_program_enrolments.margin_year_minus_3%type,
    margin_year_minus_4         farms.farm_program_enrolments.margin_year_minus_4%type,
    margin_year_minus_5         farms.farm_program_enrolments.margin_year_minus_5%type,
    margin_year_minus_6         farms.farm_program_enrolments.margin_year_minus_6%type,
    margin_year_minus_2_ind     farms.farm_program_enrolments.margin_year_minus_2_ind%type,
    margin_year_minus_3_ind     farms.farm_program_enrolments.margin_year_minus_3_ind%type,
    margin_year_minus_4_ind     farms.farm_program_enrolments.margin_year_minus_4_ind%type,
    margin_year_minus_5_ind     farms.farm_program_enrolments.margin_year_minus_5_ind%type,
    margin_year_minus_6_ind     farms.farm_program_enrolments.margin_year_minus_6_ind%type,
    generated_from_cra_ind      farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_program_enrolments.generated_from_enw_ind%type,
    create_task_in_barn_ind     farms.farm_program_enrolments.create_task_in_barn_ind%type,
    enrolment_revision_count    farms.farm_program_enrolments.revision_count%type
)
language sql
as $$
    select ac.agristability_client_id,
           ac.participant_pin,
           pe.failed_to_generate_ind,
           pe.failed_reason,
           pe.program_enrolment_id,
           pe.enrolment_year,
           pe.enrolment_fee,
           pe.generated_date,
           pe.contribution_margin_average,
           pe.margin_year_minus_2,
           pe.margin_year_minus_3,
           pe.margin_year_minus_4,
           pe.margin_year_minus_5,
           pe.margin_year_minus_6,
           pe.margin_year_minus_2_ind,
           pe.margin_year_minus_3_ind,
           pe.margin_year_minus_4_ind,
           pe.margin_year_minus_5_ind,
           pe.margin_year_minus_6_ind,
           pe.generated_from_cra_ind,
           pe.generated_from_enw_ind,
           pe.create_task_in_barn_ind,
           pe.revision_count enrolment_revision_count
    from farms.farm_agristability_clients ac
    join farms.farm_program_enrolments pe on pe.agristability_client_id = ac.agristability_client_id
                                    and pe.enrolment_year = in_enrolment_year
    where ac.participant_pin = in_pin;
$$;
