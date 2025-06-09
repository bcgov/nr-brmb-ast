create or replace function farms_report_pkg.get_latest_status(
    in in_pin farms.agristability_client.participant_pin%type,
    in in_year farms.program_year.year%type
)
returns varchar
language plpgsql
as $$
declare
    c_status cursor for
        select ssc.description
        from farms.agri_scenarios_vw m
        join farms.agristability_scenario s on s.agristability_scenario_id = m.agristability_scenario_id
        join farms.scenario_state_code ssc on ssc.scenario_state_code = s.scenario_state_code
        where m.year = in_year
        and m.participant_pin = in_pin
        and s.scenario_class_code in ('CRA', 'USER', 'GEN')
        and s.scenario_state_code in ('COMP', 'AMEND', 'IP', 'REC')
        order by case when s.scenario_state_code = 'COMP' then 0 else 1 end,
                 case when s.scenario_state_code = 'AMEND' then 0 else 1 end,
                 case when s.scenario_state_code = 'IP' then 0 else 1 end,
                 case when s.scenario_state_code = 'REC' then 0 else 1 end;

    v_status_row record;
    v_status_desc farms.scenario_state_code.description%type := null;
begin
    open c_status;
    fetch c_status into v_status_row;
    v_status_desc := v_status_row.description;
    close c_status;

    return v_status_desc;
end;
$$;
