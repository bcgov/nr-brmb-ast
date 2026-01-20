create or replace function farms_enrolment_read_pkg.read_staging()
returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
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

    return cur;

end;
$$;
