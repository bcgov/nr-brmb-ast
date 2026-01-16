create or replace function farms_import_pkg.get_scenario_ids(
    in in_year farms.farm_program_years.year%type,
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_scenario_number farms.farm_agristability_scenarios.scenario_number%type
)
returns numeric[]
language plpgsql
as $$
declare
    py_id_rec record;
    py_id_ref_cursor refcursor := null;
    scenario_ids numeric[] := '{}';
begin
    py_id_ref_cursor := farms_read_pkg.read_py_id(in_participant_pin, in_year, in_scenario_number, 'DEF');

    -- Loop through the Scenario IDs
    loop
        fetch py_id_ref_cursor into py_id_rec;
        exit when not found;

        -- Append the scenario ID to the array
        scenario_ids := array_append(scenario_ids, py_id_rec.agristability_scenario_id);
    end loop;

    close py_id_ref_cursor;

    return scenario_ids;

end;
$$;
