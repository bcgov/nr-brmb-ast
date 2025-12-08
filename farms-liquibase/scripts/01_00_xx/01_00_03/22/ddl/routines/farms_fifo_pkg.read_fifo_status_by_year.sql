create or replace function farms_fifo_pkg.read_fifo_status_by_year(
    in in_year farms.farm_scenarios_vw.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select sv.participant_pin,
               sv.client_name,
               ssc.description scenario_state_code_desc,
               case when sv.scenario_state_code = 'COMPLETED' then cl.total_benefit else null end estimated_benefit,
               case when sv.scenario_state_code = 'COMPLETED' and cl.total_benefit > 1 then 'Y' else 'N' end is_payment_file,
               sv.scenario_number
        from farms.farm_scenarios_vw sv
        join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = sv.scenario_state_code
        left outer join farms.farm_agristability_claims cl on cl.agristability_scenario_id = sv.agristability_scenario_id
        where sv.year = in_year
        and sv.scenario_class_code = 'FIFO';

    return cur;
end;
$$;
