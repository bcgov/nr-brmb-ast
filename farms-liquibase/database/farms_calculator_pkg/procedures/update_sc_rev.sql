create or replace procedure farms_calculator_pkg.update_sc_rev(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_revision_count farms.farm_agristability_scenarios.revision_count%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin

    update farms.farm_agristability_scenarios
    set revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_scenario_id
    and revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;
end;
$$;
