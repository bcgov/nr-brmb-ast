create or replace function farms_webapp_pkg.get_inventory_valid_items()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select x.agristabilty_cmmdty_xref_id,
               x.inventory_item_code,
               iic.description item_code_desc,
               x.inventory_class_code,
               icc.description item_class_desc,
               x.market_commodity_ind,
               iid.eligibility_ind,
               iid.commodity_type_code,
               iid.line_item,
               iid.multi_stage_commdty_code,
               cud.crop_unit_code default_crop_unit_code
        from farms.farm_agristabilty_cmmdty_xref x
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = x.inventory_item_code
        join farms.farm_inventory_class_codes icc on icc.inventory_class_code = x.inventory_class_code
        -- One row per inventory item code rather than one per code/program year.
        -- Ordering by "(column is null), program_year desc" reproduces Oracle's
        -- FIRST_VALUE(... IGNORE NULLS), which PostgreSQL does not support: it sorts the
        -- non-null values ahead of the nulls, so the most recent non-null value wins.
        join (select row_number() over (partition by iid2.inventory_item_code
                                            order by iid2.program_year) item_row_num,
                     iid2.inventory_item_code,
                     iid2.eligibility_ind,
                     first_value(iid2.commodity_type_code) over (
                         partition by iid2.inventory_item_code
                             order by (iid2.commodity_type_code is null), iid2.program_year desc
                     ) commodity_type_code,
                     first_value(iid2.line_item) over (
                         partition by iid2.inventory_item_code
                             order by (iid2.line_item is null), iid2.program_year desc
                     ) line_item,
                     first_value(iid2.multi_stage_commdty_code) over (
                         partition by iid2.inventory_item_code
                             order by (iid2.multi_stage_commdty_code is null), iid2.program_year desc
                     ) multi_stage_commdty_code
              from farms.farm_inventory_item_details iid2) iid
             on iid.inventory_item_code = x.inventory_item_code
            and iid.item_row_num = 1
        left outer join farms.farm_crop_unit_defaults cud on cud.inventory_item_code = x.inventory_item_code
        where x.inventory_item_code != '-1'
        and x.inventory_class_code != '-1'
        order by x.inventory_class_code,
                 x.inventory_item_code;
    return v_cursor;
end;
$$;
