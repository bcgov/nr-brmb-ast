create or replace function farms_import_pkg.scenario(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_program_year_version_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_pyv_prev_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    v_last_scenario_number numeric;
    v_new_scenario_number numeric;
    scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;

    err_msg varchar(512);
begin

    select max(sc.scenario_number) last_scenario_number
    into v_last_scenario_number
    from farms.farm_agristability_scenarios sc
    join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
    where pyv.program_year_id = (
        select program_year_id
        from farms.farm_program_year_versions t
        where t.program_year_version_id = in_pyv_prev_id
    );

    v_new_scenario_number := coalesce(v_last_scenario_number, 0) + 1;

    select nextval('farms.farm_as_seq')
    into scenario_id;

    insert into farms.farm_agristability_scenarios (
        agristability_scenario_id,
        scenario_number,
        benefits_calculator_version,
        scenario_created_by,
        description,
        default_ind,
        scenario_creation_date,
        program_year_version_id,
        scenario_class_code,
        scenario_state_code,
        scenario_category_code,
        participnt_data_src_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        scenario_id,
        v_new_scenario_number,
        null,
        in_user,
        null,
        'N',
        current_date,
        in_program_year_version_id,
        'CRA',
        'REC',
        case when v_new_scenario_number = 1 then 'CRAR' else 'NCRA' end,
        'CRA',
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    err_msg := farms_import_pkg.claim(scenario_id, in_user);
    if err_msg is null then
        err_msg := farms_import_pkg.benefit_margins(in_program_year_id, scenario_id, in_user);
    end if;

    return err_msg;

exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_scenario(
            sqlerrm,
            in_program_year_version_id
        ));
end;
$$;
