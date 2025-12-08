create or replace function farms_webapp_pkg.get_scenario_state_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select scenario_state_code code,
               description
        from farms.farm_scenario_state_codes
        where current_timestamp between established_date and expiry_date
        and scenario_state_code not in ('AMEND', 'REC')
        order by description asc;
    return v_cursor;
end;
$$;
