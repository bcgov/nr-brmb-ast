create or replace function farms_read_pkg.read_enrolment(
    in in_pin farms.agristability_client.participant_pin%type,
    in in_enrolment_year farms.program_enrolment.enrolment_year%type
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
               pe.failed_to_generate_indicator,
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
               pe.margin_year_minus_2_indicator,
               pe.margin_year_minus_3_indicator,
               pe.margin_year_minus_4_indicator,
               pe.margin_year_minus_5_indicator,
               pe.margin_year_minus_6_indicator,
               pe.generated_from_cra_indicator,
               pe.generated_from_enw_indicator,
               pe.create_task_in_barn_indicator,
               pe.revision_count enrolment_revision_count
        from farms.agristability_client ac
        join farms.program_enrolment pe on pe.agristability_client_id = ac.agristability_client_id
                                        and pe.enrolment_year = in_enrolment_year
        where ac.participant_pin = in_pin;

    return cur;

end;
$$;
