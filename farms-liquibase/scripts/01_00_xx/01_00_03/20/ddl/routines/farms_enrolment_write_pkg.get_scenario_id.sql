create or replace function farms_enrolment_write_pkg.get_scenario_id(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type
) returns farms.farm_agristability_scenarios.agristability_scenario_id%type
language plpgsql
as
$$
declare

    py_id_rec record;
    py_id_ref_cursor refcursor := null;
    target_scenario_year bigint := in_enrolment_year - 2;
    scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;

begin

    py_id_ref_cursor := farms_read_pkg.read_py_id(in_participant_pin, target_scenario_year, null, 'ENROL');

    fetch py_id_ref_cursor into py_id_rec;

    if py_id_ref_cursor%found then
        if py_id_rec.program_year = target_scenario_year then
            scenario_id := py_id_rec.agristability_scenario_id;
        end if;
    end if;

    close py_id_ref_cursor;

    return scenario_id;

end;
$$;
