create or replace procedure farms_calculator_pkg.save_bpu_xref(
    in in_scenario_id farms.farm_scenario_bpu_xref.agristability_scenario_id%type,
    in in_bpu_id farms.farm_scenario_bpu_xref.benchmark_per_unit_id%type,
    in in_scenario_bpu_purpose_code farms.farm_scenario_bpu_xref.scenario_bpu_purpose_code%type,
    in in_user farms.farm_scenario_bpu_xref.who_created%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_scenario_bpu_xref(
        scenario_bpu_xref_id,
        agristability_scenario_id,
        benchmark_per_unit_id,
        scenario_bpu_purpose_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_bpux_seq'),
        in_scenario_id,
        in_bpu_id,
        in_scenario_bpu_purpose_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
