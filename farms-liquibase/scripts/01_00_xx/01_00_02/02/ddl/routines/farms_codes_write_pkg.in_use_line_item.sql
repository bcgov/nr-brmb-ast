create or replace function farms_codes_write_pkg.in_use_line_item(
    in in_program_year farms.farm_line_items.program_year%type,
    in in_line_item farms.farm_line_items.line_item%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin
    select (case
        when exists(
            select null
            from farms.farm_agri_scenarios_vw m
            join farms.farm_farming_operations fo on fo.program_year_version_id = m.program_year_version_id
            join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
            where m.program_year = in_program_year
            and rie.line_item = in_line_item
        ) then 1
        else 0
    end)
    into v_in_use;

    return v_in_use;
end;
$$;
