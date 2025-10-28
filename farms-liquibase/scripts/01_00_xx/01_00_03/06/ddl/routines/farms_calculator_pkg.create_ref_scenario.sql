create or replace function farms_calculator_pkg.create_ref_scenario(
    in in_for_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_copy_from_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_version farms.farm_agristability_scenarios.benefits_calculator_version%type,
    in in_user text
) returns farms.farm_agristability_scenarios.agristability_scenario_id%type
language plpgsql
as
$$
declare

    ora2pg_rowcount int;
    scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type := null;
    ref_scenario_id farms.farm_reference_scenarios.reference_scenario_id%type := null;

    ref_sc_id_curs cursor for
        select rs.reference_scenario_id
        from farms.farm_reference_scenarios rs
        where rs.agristability_scenario_id = in_copy_from_scenario_id;

    parent_pgm_year farms.farm_program_years.year%type;
    parent_s_num farms.farm_agristability_scenarios.scenario_number%type;
    ref_year farms.farm_program_years.year%type;
    ref_s_num farms.farm_agristability_scenarios.scenario_number%type;
    copy_from_type farms.farm_agristability_scenarios.scenario_class_code%type;
    v_participnt_data_src_code farms.farm_agristability_scenarios.participnt_data_src_code%type;

    cnt bigint := -1;

begin

    -- QA check because we can't build a constraint here and there was an issue.
    -- Make sure that this scenario does not already have a ref scenario for this year.
    select count(*)
    into cnt
    from farms.farm_reference_scenarios rf
    join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = rf.agristability_scenario_id
    join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
    join farms.farm_program_years py on pyv.program_year_id = py.program_year_id
    join farms.farm_agristability_scenarios sc2 on sc2.agristability_scenario_id = in_copy_from_scenario_id
    join farms.farm_program_year_versions pyv2 on sc2.program_year_version_id = pyv2.program_year_version_id
    join farms.farm_program_years py2 on pyv2.program_year_id = py2.program_year_id
    where rf.for_agristability_scenario_id = in_for_scenario_id
    and py.year = py2.year;

    if cnt > 0 then
        raise exception '%', farms_types_pkg.invalid_ref_scenario_count_msg()
        using errcode = farms_types_pkg.invalid_ref_scenario_count_num()::text;
    end if;

    select py.year program_year,
           s.scenario_number
    into parent_pgm_year,
         parent_s_num
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    where s.agristability_scenario_id = in_for_scenario_id;

    select py.year program_year,
           s.scenario_number,
           s.scenario_class_code,
           s.participnt_data_src_code
    into ref_year,
         ref_s_num,
         copy_from_type,
         v_participnt_data_src_code
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    where s.agristability_scenario_id = in_copy_from_scenario_id;

    select nextval('farms.farm_as_seq')
    into scenario_id;

    insert into farms.farm_agristability_scenarios (
        agristability_scenario_id,
        scenario_number,
        benefits_calculator_version,
        scenario_created_by,
        default_ind,
        scenario_creation_date,
        description,
        program_year_version_id,
        scenario_class_code,
        scenario_state_code,
        scenario_category_code,
        participnt_data_src_code,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select scenario_id,
           (
               select (coalesce(max(sc.scenario_number), 0) + 1)
               from farms.farm_agristability_scenarios sc
               join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
               where pyv.program_year_id = (
                   select t.program_year_id
                   from farms.farm_program_year_versions t
                   where t.program_year_version_id = s.program_year_version_id
               )
           ) scenario_number,
           in_version,
           in_user scenario_created_by,
           'N' default_ind,
           current_timestamp creation_date,
           'Reference scenario for ' || parent_pgm_year || ' scenario number ' || parent_s_num,
           program_year_version_id,
           'REF' scenario_class_code,
           'IP' scenario_state_code,
           in_scenario_category_code,
           v_participnt_data_src_code,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_agristability_scenarios s
    where s.agristability_scenario_id = in_copy_from_scenario_id;


    open ref_sc_id_curs;
    fetch ref_sc_id_curs into ref_scenario_id;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount > 1 then
        raise exception '%', 'Found more than one Farm_Reference_Scenarios for scenario id'
        using errcode = '45000';
    end if;

    if found then

        insert into farms.farm_reference_scenarios (
            reference_scenario_id,
            used_in_calc_ind,
            deemed_farming_year_ind,
            agristability_scenario_id,
            for_agristability_scenario_id,
            who_created,
            when_created,
            who_updated,
            when_updated)
        select nextval('farms.farm_rs_seq'),
            'N',
            rs.deemed_farming_year_ind,
            scenario_id,
            in_for_scenario_id,
            in_user who_created,
            current_timestamp when_created,
            in_user who_updated,
            current_timestamp when_updated
        from farms.farm_reference_scenarios rs
        where rs.reference_scenario_id = ref_scenario_id;

    else

        insert into farms.farm_reference_scenarios (
            reference_scenario_id,
            used_in_calc_ind,
            deemed_farming_year_ind,
            agristability_scenario_id,
            for_agristability_scenario_id,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_rs_seq'),
            'N',
            'Y',
            scenario_id,
            in_for_scenario_id,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

    end if;

    call farms_calculator_pkg.copy_adjustments(in_copy_from_scenario_id, scenario_id);

    close ref_sc_id_curs;

    call farms_calculator_pkg.sc_log(in_for_scenario_id,
                                     'Created ' || ref_year || ' reference scenario from '
                                     || copy_from_type || ' scenario number ' || ref_s_num,
                                     in_user);

    return scenario_id;

end;
$$;
