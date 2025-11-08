create or replace function farms_enrolment_read_pkg.read_csv(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_pins numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
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

    return cur;

end;
$$;
