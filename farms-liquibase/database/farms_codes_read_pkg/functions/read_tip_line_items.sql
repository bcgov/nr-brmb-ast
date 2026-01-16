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
                   tli.operating_cost_ind,
                   tli.direct_expense_ind,
                   tli.machinery_expense_ind,
                   tli.land_and_building_expense_ind,
                   tli.overhead_expense_ind,
                   tli.program_payment_for_tips_ind,
                   tli.other_ind,
                   tli.tip_farm_type_1_lookup_id,
                   li.description,
                   li.program_year,
                   li.line_item_id,
                   last_value(li.line_item_id) over (
                       partition by li.line_item order by li.program_year
                       rows between unbounded preceding and unbounded following
                   ) latest_id_for_item
            from farms.farm_tip_line_items tli
            join farms.farm_line_items li on li.line_item = tli.line_item
            where li.expiry_date > current_date
        )
        select items.tip_line_item_id,
               items.line_item,
               items.operating_cost_ind,
               items.direct_expense_ind,
               items.machinery_expense_ind,
               items.land_and_building_expense_ind,
               items.overhead_expense_ind,
               items.program_payment_for_tips_ind,
               items.other_ind,
               items.tip_farm_type_1_lookup_id,
               tft1.farm_type_1_name,
               items.description
        from items
        left outer join farms.farm_tip_farm_type_1_lookups tft1 on tft1.tip_farm_type_1_lookup_id = items.tip_farm_type_1_lookup_id
        where items.line_item_id = items.latest_id_for_item
        order by line_item;
    return cur;

end;
$$;
