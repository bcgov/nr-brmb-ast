create or replace function farms_calculator_pkg.update_sc_pyv(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_pyv_num farms.farm_program_year_versions.program_year_version_number%type,
    in in_pyv_copy_old_data text,
    in in_op_nums_copy_old_data varchar[],
    in in_user text
) returns farms.farm_agristability_scenarios.scenario_number%type
language plpgsql
as
$$
declare

    old_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    new_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    cra_suppl_rec_date farms.farm_agristability_scenarios.cra_supplemental_received_date%type;
    new_scenario_num farms.farm_agristability_scenarios.scenario_number%type;
    program_year farms.farm_program_years.year%type;
    scenario_type farms.farm_agristability_scenarios.scenario_class_code%type;

begin

    select s_old.program_year_version_id,
           pyv_new.program_year_version_id,
           py.year program_year,
           s_old.scenario_class_code,
           s_new.cra_supplemental_received_date
    into old_pyv_id,
         new_pyv_id,
         program_year,
         scenario_type,
         cra_suppl_rec_date
    from farms.farm_agristability_scenarios s_old
    join farms.farm_program_year_versions pyv_old on pyv_old.program_year_version_id = s_old.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv_old.program_year_id
    join farms.farm_program_year_versions pyv_new on pyv_new.program_year_id = py.program_year_id
    join farms.farm_agristability_scenarios s_new on s_new.program_year_version_id = pyv_new.program_year_version_id
                                                  and s_new.scenario_class_code in ('CRA','GEN','LOCAL','CHEF')
    where s_old.agristability_scenario_id = in_scenario_id
    and pyv_new.program_year_version_number = in_pyv_num;

    select max(s_all.scenario_number) + 1
    into new_scenario_num
    from farms.farm_agristability_scenarios s_old
    join farms.farm_program_year_versions pyv_old on pyv_old.program_year_version_id = s_old.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv_old.program_year_id
    join farms.farm_program_year_versions pyv_all on pyv_all.program_year_id = py.program_year_id
    join farms.farm_agristability_scenarios s_all on s_all.program_year_version_id = pyv_all.program_year_version_id
    where s_old.agristability_scenario_id = in_scenario_id;

    -- Delete adjustments for locally generated farming operations
    call farms_calculator_pkg.delete_adj_for_generated_ops(in_scenario_id);

    update farms.farm_agristability_scenarios
    set program_year_version_id = new_pyv_id,
        scenario_number = new_scenario_num,
        cra_supplemental_received_date = cra_suppl_rec_date,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_scenario_id;

    if scenario_type = 'USER' then
        update farms.farm_agristability_scenarios
        set description = 'Reference scenario for ' || program_year || ' scenario number ' || new_scenario_num
        where agristability_scenario_id in (
            select rs.agristability_scenario_id
            from farms.farm_reference_scenarios rs
            where rs.for_agristability_scenario_id = in_scenario_id
        );
    end if;


    -- Delete benefit calculations for farming operations
    delete from farms.farm_benefit_calc_margins bcm
    where bcm.agristability_scenario_id = in_scenario_id;

    call farms_calculator_pkg.update_pyv_copy_old(old_pyv_id, new_pyv_id, in_pyv_copy_old_data, in_op_nums_copy_old_data, in_user);

    -- Update the Farming Operation IDs and CRA_*_IDs for adjustments
    call farms_calculator_pkg.update_adj_pyv(in_scenario_id, old_pyv_id, new_pyv_id, in_user);

    return new_scenario_num;

end;
$$;
