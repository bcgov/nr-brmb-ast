create or replace function farms_calculator_pkg.read_operations_for_accrual_import(
    in in_participant_pin farms.farm_scenarios_vw.participant_pin%type,
    in in_year farms.farm_scenarios_vw.year%type,
    in in_excluding_program_year_version_number farms.farm_scenarios_vw.program_year_version_number%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select sv.program_year_version_number,
               sv.scenario_number,
               sv.scenario_class_code,
               stc.description as scenario_class_desc,
               sv.scenario_category_code,
               scc.description scenario_category_code_desc,
               sv.scenario_state_code,
               ssc.description as scenario_state_desc,
               sc.when_created,
               fo.alignment_key,
               fo.partnership_pin,
               fo.partnership_name,
               fo.farming_operation_id
        from farms.farm_scenarios_vw sv
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = sv.agristability_scenario_id
        join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = sc.scenario_state_code
        join farms.farm_scenario_class_codes stc on stc.scenario_class_code = sc.scenario_class_code
        join farms.farm_scenario_category_codes scc on scc.scenario_category_code = sc.scenario_category_code
        join farms.farm_farming_operations fo on fo.program_year_version_id = sv.program_year_version_id
        where sv.participant_pin = in_participant_pin
        and sv.year = in_year
        and sv.program_year_version_number != in_excluding_program_year_version_number
        and exists (
            select *
            from farms.farm_reported_inventories ri
            join farms.farm_agristabilty_cmmdty_xref acx on acx.agristabilty_cmmdty_xref_id = ri.agristabilty_cmmdty_xref_id
            where ri.farming_operation_id = fo.farming_operation_id
            and (coalesce(ri.agristability_scenario_id::text, '') = '' or (sv.scenario_class_code in ('USER','REF') and ri.agristability_scenario_id = sv.agristability_scenario_id))
            and acx.inventory_class_code in ('3', '4', '5')
            and (coalesce(ri.start_of_year_amount, 0) != 0 or coalesce(ri.end_of_year_amount, 0) != 0) 
        ) 
        order by sv.year desc,
                 sv.program_year_version_number desc,
                 sv.scenario_number desc,
                 fo.alignment_key;

    return cur;
end;
$$;
