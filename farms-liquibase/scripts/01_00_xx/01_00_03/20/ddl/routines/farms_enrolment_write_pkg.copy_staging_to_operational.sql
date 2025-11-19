create or replace procedure farms_enrolment_write_pkg.copy_staging_to_operational()
language plpgsql
as
$$
begin

    merge into farms.farm_program_enrolments pe
    using (
        select ac.agristability_client_id,
               z.enrolment_year,
               z.enrolment_fee,
               z.failed_to_generate_ind,
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
               z.generated_from_cra_ind,
               z.generated_from_enw_ind,
               z.create_task_in_barn_ind,
               z.combined_farm_percent,
               z.agristability_scenario_id,
               z.who_created,
               z.who_updated
        from farms.farm_zprogram_enrolments z
        join farms.farm_agristability_clients ac on ac.participant_pin = z.participant_pin
    ) x
    on x.agristability_client_id = pe.agristability_client_id
    and x.enrolment_year = pe.enrolment_year
    when matched then
        update set
        enrolment_fee = x.enrolment_fee,
        failed_to_generate_ind = x.failed_to_generate_ind,
        failed_reason = x.failed_reason,
        generated_date = x.generated_date,
        contribution_margin_average = x.contribution_margin_average,
        margin_year_minus_2 = x.margin_year_minus_2,
        margin_year_minus_3 = x.margin_year_minus_3,
        margin_year_minus_4 = x.margin_year_minus_4,
        margin_year_minus_5 = x.margin_year_minus_5,
        margin_year_minus_6 = x.margin_year_minus_6,
        margin_year_minus_2_ind = x.margin_year_minus_2_ind,
        margin_year_minus_3_ind = x.margin_year_minus_3_ind,
        margin_year_minus_4_ind = x.margin_year_minus_4_ind,
        margin_year_minus_5_ind = x.margin_year_minus_5_ind,
        margin_year_minus_6_ind = x.margin_year_minus_6_ind,
        generated_from_cra_ind = x.generated_from_cra_ind,
        generated_from_enw_ind = x.generated_from_enw_ind,
        create_task_in_barn_ind = x.create_task_in_barn_ind,
        combined_farm_percent = x.combined_farm_percent,
        agristability_scenario_id = x.agristability_scenario_id,
        revision_count = revision_count + 1,
        who_updated = x.who_updated,
        when_updated = current_timestamp
    when not matched then
        insert (
            program_enrolment_id,
            enrolment_year,
            enrolment_fee,
            failed_to_generate_ind,
            failed_reason,
            generated_date,
            contribution_margin_average,
            margin_year_minus_2,
            margin_year_minus_3,
            margin_year_minus_4,
            margin_year_minus_5,
            margin_year_minus_6,
            margin_year_minus_2_ind,
            margin_year_minus_3_ind,
            margin_year_minus_4_ind,
            margin_year_minus_5_ind,
            margin_year_minus_6_ind,
            generated_from_cra_ind,
            generated_from_enw_ind,
            create_task_in_barn_ind,
            combined_farm_percent,
            agristability_client_id,
            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_pgre_seq'),
            x.enrolment_year,
            x.enrolment_fee,
            x.failed_to_generate_ind,
            x.failed_reason,
            x.generated_date,
            x.contribution_margin_average,
            x.margin_year_minus_2,
            x.margin_year_minus_3,
            x.margin_year_minus_4,
            x.margin_year_minus_5,
            x.margin_year_minus_6,
            x.margin_year_minus_2_ind,
            x.margin_year_minus_3_ind,
            x.margin_year_minus_4_ind,
            x.margin_year_minus_5_ind,
            x.margin_year_minus_6_ind,
            x.generated_from_cra_ind,
            x.generated_from_enw_ind,
            x.create_task_in_barn_ind,
            x.combined_farm_percent,
            x.agristability_client_id,
            x.agristability_scenario_id,
            1,
            x.who_created,
            current_timestamp,
            x.who_updated,
            current_timestamp
        );

end;
$$;
