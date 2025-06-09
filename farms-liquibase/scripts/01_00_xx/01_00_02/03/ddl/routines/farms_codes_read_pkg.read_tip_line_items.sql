create or replace function farms_codes_read_pkg.read_tip_line_items()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        with items as (
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
                   li.description,
                   li.program_year,
                   li.line_item_id,
                   last_value(li.line_item_id) over (
                       partition by li.line_item order by li.program_year
                       rows between unbounded preceding and unbounded following
                   ) latest_id_for_item
            from farms.tip_line_item tli
            join farms.line_item li on li.line_item = tli.line_item
            where li.expiry_date > current_date
        )
        select items.tip_line_item_id,
               items.line_item,
               items.operating_cost_indicator,
               items.direct_expense_indicator,
               items.machinery_expense_indicator,
               items.land_and_building_expense_indicator,
               items.overhead_expense_indicator,
               items.program_payment_for_tips_indicator,
               items.other_indicator,
               items.tip_farm_type_1_lookup_id,
               tft1.farm_type_1_name,
               items.description
        from items
        left outer join farms.tip_farm_type_1_lookup tft1 on tft1.tip_farm_type_1_lookup_id = items.tip_farm_type_1_lookup_id
        where items.line_item_id = items.latest_id_for_item
        order by line_item;
    return cur;

end;
$$;
