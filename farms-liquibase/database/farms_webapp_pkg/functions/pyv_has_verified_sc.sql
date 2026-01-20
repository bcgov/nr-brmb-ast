create or replace function farms_webapp_pkg.pyv_has_verified_sc(
    in in_program_year_version_id farms.farm_program_year_versions.program_year_version_id%type
) returns bigint
language plpgsql
as
$$
declare

    v_has_verified bigint;

begin

    select
        case when exists (
            select null
            from farms.farm_program_year_versions pyv
            join farms.farm_agristability_scenarios sc on sc.program_year_version_id = pyv.program_year_version_id
            where pyv.program_year_version_id = in_program_year_version_id
            and sc.scenario_state_code in ('COMP')
        ) then 1 else 0 end
    into v_has_verified;

    return v_has_verified;
end;
$$;
