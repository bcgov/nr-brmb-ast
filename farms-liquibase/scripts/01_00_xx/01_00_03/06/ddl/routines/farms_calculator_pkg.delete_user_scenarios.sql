create or replace procedure farms_calculator_pkg.delete_user_scenarios(
    in in_sc_ids numeric[]
)
language plpgsql
as
$$
declare

    scenario_id numeric;

begin

    foreach scenario_id in array in_sc_ids
    loop
        call farms_calculator_pkg.delete_user_scenario(scenario_id);
    end loop;

end;
$$;
