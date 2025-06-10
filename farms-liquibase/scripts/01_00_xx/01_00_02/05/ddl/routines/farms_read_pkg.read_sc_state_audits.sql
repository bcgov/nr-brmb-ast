create or replace function farms_read_pkg.read_sc_state_audits(
    in sc_id farms.scenario_state_audit.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select a.scenario_state_audit_id,
               a.scenario_state_code,
               c.description scenario_state_code_desc,
               a.state_change_reason,
               a.create_user state_changed_by_user_id,
               a.create_date state_changed_timestamp,
               a.revision_count
        from farms.scenario_state_audit a
        join farms.scenario_state_code c on c.scenario_state_code = a.scenario_state_code
        where a.agristability_scenario_id = sc_id
        order by a.create_date desc;

    return cur;
end;
$$;
