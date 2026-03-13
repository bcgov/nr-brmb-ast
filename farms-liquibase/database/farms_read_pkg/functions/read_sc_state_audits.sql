create or replace function farms_read_pkg.read_sc_state_audits(
    in sc_id farms.farm_scenario_state_audits.agristability_scenario_id%type
)
returns table(
    scenario_state_audit_id     farms.farm_scenario_state_audits.scenario_state_audit_id%type,
    scenario_state_code         farms.farm_scenario_state_audits.scenario_state_code%type,
    scenario_state_code_desc    farms.farm_scenario_state_codes.description%type,
    state_change_reason         farms.farm_scenario_state_audits.state_change_reason%type,
    state_changed_by_user_id    farms.farm_scenario_state_audits.who_created%type,
    state_changed_timestamp     farms.farm_scenario_state_audits.when_created%type,
    revision_count              farms.farm_scenario_state_audits.revision_count%type
)
language sql
as $$
    select a.scenario_state_audit_id,
           a.scenario_state_code,
           c.description scenario_state_code_desc,
           a.state_change_reason,
           a.who_created state_changed_by_user_id,
           a.when_created state_changed_timestamp,
           a.revision_count
    from farms.farm_scenario_state_audits a
    join farms.farm_scenario_state_codes c on c.scenario_state_code = a.scenario_state_code
    where a.agristability_scenario_id = sc_id
    order by a.when_created desc;
$$;
