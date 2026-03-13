create or replace function farms_enrolment_read_pkg.read_csv(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_pins integer[]
)
returns table(
    participant_pin             farms.farm_agristability_clients.participant_pin%type,
    enrolment_year              farms.farm_program_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_program_enrolments.enrolment_fee%type,
    contribution_margin_average farms.farm_program_enrolments.contribution_margin_average%type,
    margin_year_minus_2         farms.farm_program_enrolments.margin_year_minus_2%type,
    margin_year_minus_2_ind     farms.farm_program_enrolments.margin_year_minus_2_ind%type,
    margin_year_minus_3         farms.farm_program_enrolments.margin_year_minus_3%type,
    margin_year_minus_3_ind     farms.farm_program_enrolments.margin_year_minus_3_ind%type,
    margin_year_minus_4         farms.farm_program_enrolments.margin_year_minus_4%type,
    margin_year_minus_4_ind     farms.farm_program_enrolments.margin_year_minus_4_ind%type,
    margin_year_minus_5         farms.farm_program_enrolments.margin_year_minus_5%type,
    margin_year_minus_5_ind     farms.farm_program_enrolments.margin_year_minus_5_ind%type,
    margin_year_minus_6         farms.farm_program_enrolments.margin_year_minus_6%type,
    margin_year_minus_6_ind     farms.farm_program_enrolments.margin_year_minus_6_ind%type,
    generated_date              varchar,
    generated_from_cra_ind      farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_program_enrolments.generated_from_enw_ind%type,
    create_task_in_barn_ind     farms.farm_program_enrolments.create_task_in_barn_ind%type,
    combined_farm_percent       farms.farm_program_enrolments.combined_farm_percent%type,
    failed_to_generate_ind      farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason               farms.farm_program_enrolments.failed_reason%type
)
language sql
as $$
    select ac.participant_pin,
           in_enrolment_year enrolment_year,
           pe.enrolment_fee,
           pe.contribution_margin_average,
           pe.margin_year_minus_2,
           pe.margin_year_minus_2_ind,
           pe.margin_year_minus_3,
           pe.margin_year_minus_3_ind,
           pe.margin_year_minus_4,
           pe.margin_year_minus_4_ind,
           pe.margin_year_minus_5,
           pe.margin_year_minus_5_ind,
           pe.margin_year_minus_6,
           pe.margin_year_minus_6_ind,
           to_char(pe.generated_date, 'DD/MM/YYYY') generated_date,
           pe.generated_from_cra_ind,
           pe.generated_from_enw_ind,
           pe.create_task_in_barn_ind,
           pe.combined_farm_percent,
           pe.failed_to_generate_ind,
           pe.failed_reason
    from farms.farm_agristability_clients ac
    left outer join farms.farm_program_enrolments pe on pe.agristability_client_id = ac.agristability_client_id
                                                     and pe.enrolment_year = in_enrolment_year
    where ac.participant_pin = any(in_pins)
    order by ac.participant_pin;
$$;
