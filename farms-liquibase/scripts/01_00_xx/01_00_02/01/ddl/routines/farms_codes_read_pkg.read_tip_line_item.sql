create or replace function farms_codes_read_pkg.read_tip_line_item(
    in in_line_item_id farms.tip_line_item.tip_line_item_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select tli.tip_line_item_id,
               tli.line_item,
               tli.operating_cost_indicator,
               tli.direct_expense_indicator,
               tli.machinery_expense_indicator,
               tli.land_and_building_expense_indicator,
               tli.overhead_expense_indicator,
               tli.program_payment_for_tips_indicator,
               tli.other_indicator,
               tli.tip_farm_type_1_lookup_id,
               tft1.farm_type_1_name
        from farms.tip_line_item tli
        left outer join farms.tip_farm_type_1_lookup tft1 on tli.tip_farm_type_1_lookup_id = tft1.tip_farm_type_1_lookup_id
        where tli.tip_line_item_id = in_line_item_id;
    return cur;

end;
$$;
