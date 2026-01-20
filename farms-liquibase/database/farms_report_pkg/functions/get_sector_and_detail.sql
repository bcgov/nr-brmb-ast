create or replace function farms_report_pkg.get_sector_and_detail(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
    v_sector_detail_code farms.farm_sector_detail_codes.sector_detail_code%type;
begin

    v_sector_detail_code := farms_report_pkg.get_sector_detail_code(in_agristability_scenario_id);

    open cur for
        select sc.sector_code,
               sc.description sector_code_desc,
               sdc.sector_detail_code,
               sdc.description sector_detail_code_desc
        from farms.farm_sector_detail_xref sdx
        join farms.farm_sector_codes sc on sc.sector_code = sdx.sector_code
        join farms.farm_sector_detail_codes sdc on sdc.sector_detail_code = sdx.sector_detail_code
        where sdx.sector_detail_code = v_sector_detail_code;

    return cur;

end;
$$;
