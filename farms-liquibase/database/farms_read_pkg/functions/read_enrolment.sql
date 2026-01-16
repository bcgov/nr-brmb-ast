create or replace function farms_read_pkg.read_enrolment(
    in in_pin farms.farm_agristability_clients.participant_pin%type,
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select ac.agristability_client_id,
               ac.participant_pin,
               pe.failed_to_generate_ind,
               pe.failed_reason,
               pe.program_enrolment_id,
               pe.enrolment_year,
               pe.enrolment_fee,
               pe.enrolment_date,
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

    return cur;

end;
$$;
