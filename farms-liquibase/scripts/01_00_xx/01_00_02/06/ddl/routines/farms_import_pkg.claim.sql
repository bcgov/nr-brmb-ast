create or replace function farms_import_pkg.claim(
    in in_sc_id farms.agristability_scenario.agristability_scenario_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    clm_insert_cursor cursor for
        select coalesce(z50.unadjusted_reference_margin, 0) unadj_reference_margin,
               coalesce(z50.adjusted_reference_margin, 0) adjusted_reference_margin,
               coalesce(z50.program_margin, 0) program_year_margin,
               z51.federal_contributions,
               z51.provincial_contributions,
               z51.interim_contributions,
               coalesce(z51.federal_contributions, 0) + coalesce(z51.provincial_contributions, 0) + coalesce(z51.interim_contributions, 0) total_benefit,
               z51.producer_share
        from farms.z50_participant_benefit_calculation z50
        full outer join farms.z51_participant_contribution z51 on z50.participant_pin = z51.participant_pin
                                                               and z50.program_year = z51.program_year
        join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
                                           or z51.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and (z50.program_year = py.year
                                        or z51.program_year = py.year)
        join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
        join farms.agristability_scenario sc on pyv.program_year_version_id = sc.program_year_version_id
        where sc.agristability_scenario_id = in_sc_id;
    clm_insert_val record;

    c_id numeric;
begin

    for clm_insert_val in clm_insert_cursor
    loop

        select nextval('farms.seq_ac')
        into c_id;

        insert into farms.agristability_claim (
            agristability_claim_id,
            program_year_margin,
            adjusted_reference_margin,
            unadjusted_reference_margin,
            federal_contributions,
            provincial_contributions,
            interim_contributions,
            total_benefit,
            producer_share,
            agristability_scenario_id,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            c_id,
            clm_insert_val.program_year_margin,
            clm_insert_val.adjusted_reference_margin,
            clm_insert_val.unadj_reference_margin,
            clm_insert_val.federal_contributions,
            clm_insert_val.provincial_contributions,
            clm_insert_val.interim_contributions,
            clm_insert_val.total_benefit,
            clm_insert_val.producer_share,
            in_sc_id,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

    end loop;

    return null;
exception
    when others then
        call farms_import_pkg.scrub(farms_error_pkg.codify_claim(
            sqlerrm,
            in_sc_id
        ));
end;
$$;
