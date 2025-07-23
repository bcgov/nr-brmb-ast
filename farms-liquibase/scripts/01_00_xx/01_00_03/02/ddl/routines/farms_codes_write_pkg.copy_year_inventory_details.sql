create or replace function farms_codes_write_pkg.copy_year_inventory_details(
    in in_to_year farms.program_year.year%type,
    in in_user farms.program_year.create_user%type
)
returns numeric
language plpgsql
as $$
declare
    v_rows_inserted numeric := 0;
begin

    insert into farms.inventory_item_detail (
        inventory_item_detail_id,
        program_year,
        eligibility_indicator,
        line_item,
        inventory_item_code,
        commodity_type_code,
        fruit_vegetable_type_code,
        multiple_stage_commodity_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    )
    select nextval('farms.seq_iid'),
           in_to_year,
           iid.eligibility_indicator,
           iid.line_item,
           iid.inventory_item_code,
           iid.commodity_type_code,
           iid.fruit_vegetable_type_code,
           iid.multiple_stage_commodity_code,
           1,
           in_user,
           current_timestamp,
           in_user,
           current_timestamp
    from farms.inventory_item_detail iid
    where iid.program_year = (
        select max(a.program_year)
        from farms.inventory_item_detail a
        where a.program_year < in_to_year
    )
    and not exists (
        select *
        from farms.inventory_item_detail b
        where b.program_year = in_to_year
        and b.inventory_item_code = iid.inventory_item_code
    );

    get diagnostics v_rows_inserted := row_count;

    return v_rows_inserted;

end;
$$;
