create or replace function farms_calculator_pkg.save_scenario_as_new(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_class_code farms.farm_agristability_scenarios.scenario_class_code%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_version farms.farm_agristability_scenarios.benefits_calculator_version%type,
    in in_user text
) returns farms.farm_agristability_scenarios.scenario_number%type
language plpgsql
as
$$
declare

    pin farms.farm_agristability_clients.participant_pin%type;
    program_year farms.farm_program_years.year%type;
    copy_from_sc_num farms.farm_agristability_scenarios.scenario_number%type;
    copy_from_type farms.farm_agristability_scenarios.scenario_class_code%type;
    pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    new_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;
    new_scenario_num farms.farm_agristability_scenarios.scenario_number%type;
    cra_income_expns_rec_date farms.farm_agristability_scenarios.cra_income_expns_received_date%type;
    cra_suppl_rec_date farms.farm_agristability_scenarios.cra_supplemental_received_date%type;
    new_ref_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;
    v_participnt_data_src_code farms.farm_agristability_scenarios.participnt_data_src_code%type;

    py_id_rec record;
    py_id_ref_cursor refcursor;

begin

    select ac.participant_pin,
           py.year program_year,
           pyv.program_year_version_id,
           s.scenario_number,
           s.scenario_class_code,
           s.cra_income_expns_received_date,
           s.cra_supplemental_received_date,
           s.participnt_data_src_code
    into pin,
         program_year,
         pyv_id,
         copy_from_sc_num,
         copy_from_type,
         cra_income_expns_rec_date,
         cra_suppl_rec_date,
         v_participnt_data_src_code
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
    where s.agristability_scenario_id = in_scenario_id;

    new_scenario_id := farms_calculator_pkg.create_parent_scenario(pyv_id,
                                                                   in_scenario_class_code,
                                                                   in_scenario_category_code,
                                                                   in_version,
                                                                   cra_income_expns_rec_date,
                                                                   cra_suppl_rec_date,
                                                                   v_participnt_data_src_code,
                                                                   in_user);

    call farms_calculator_pkg.sc_log(new_scenario_id,
                                     'Created from ' || copy_from_type || ' scenario number ' || copy_from_sc_num,
                                     in_user);

    call farms_calculator_pkg.copy_adjustments(in_scenario_id, new_scenario_id);

    py_id_ref_cursor := farms_read_pkg.read_py_id(pin, program_year, copy_from_sc_num, 'DEF');

    -- Loop through the Scenario IDs
    loop

        fetch py_id_ref_cursor into py_id_rec;
        exit when not found;/* apply on py_id_ref_cursor */

        if (py_id_rec.program_year != program_year) then
            new_ref_scenario_id := farms_calculator_pkg.create_ref_scenario(new_scenario_id,
                                                                            py_id_rec.agristability_scenario_id,
                                                                            in_scenario_category_code,
                                                                            in_version,
                                                                            in_user);
        end if;

    end loop;

    close py_id_ref_cursor;

    select s.scenario_number
    into new_scenario_num
    from farms.farm_agristability_scenarios s
    where s.agristability_scenario_id = new_scenario_id;

    return new_scenario_num;

end;
$$;
