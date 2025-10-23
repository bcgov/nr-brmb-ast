create or replace procedure farms_calculator_pkg.update_scenario_state(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_state_code farms.farm_agristability_scenarios.scenario_state_code%type,
    in in_state_change_reason farms.farm_scenario_state_audits.state_change_reason%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios sc
    set sc.scenario_state_code = in_scenario_state_code
    where sc.agristability_scenario_id = in_agristability_scenario_id;

    update farms.farm_agristability_scenarios sc
    set sc.scenario_state_code = in_scenario_state_code,
        sc.who_updated = in_user,
        sc.when_updated = current_timestamp
    where sc.agristability_scenario_id in (
        select rs.agristability_scenario_id
        from farms.farm_reference_scenarios rs
        where rs.for_agristability_scenario_id = in_agristability_scenario_id
    );

    insert into farms.farm_scenario_state_audits (
        scenario_state_audit_id,
        state_change_reason,
        agristability_scenario_id,
        scenario_state_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_ssa_seq'),
        in_state_change_reason,
        in_agristability_scenario_id,
        in_scenario_state_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
