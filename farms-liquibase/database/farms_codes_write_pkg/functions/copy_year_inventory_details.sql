create or replace function farms_codes_write_pkg.copy_year_inventory_details(
    in in_to_year farms.farm_program_years.year%type,
    in in_user farms.farm_program_years.who_created%type
)
returns numeric
language plpgsql
as $$
declare
    v_rows_inserted numeric := 0;
begin

    insert into farms.farm_inventory_item_details (
        inventory_item_detail_id,
        program_year,
        eligibility_ind,
        line_item,
        inventory_item_code,
        commodity_type_code,
        fruit_veg_type_code,
        multi_stage_commdty_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    )
    select nextval('farms.farm_iid_seq'),
           in_to_year,
           iid.eligibility_ind,
           iid.line_item,
           iid.inventory_item_code,
           iid.commodity_type_code,
           iid.fruit_veg_type_code,
           iid.multi_stage_commdty_code,
           1,
           in_user,
           current_timestamp,
           in_user,
           current_timestamp
    from farms.farm_inventory_item_details iid
    where iid.program_year = (
        select max(a.program_year)
        from farms.farm_inventory_item_details a
        where a.program_year < in_to_year
    )
    and not exists (
        select *
        from farms.farm_inventory_item_details b
        where b.program_year = in_to_year
        and b.inventory_item_code = iid.inventory_item_code
    );

    get diagnostics v_rows_inserted := row_count;

    return v_rows_inserted;

end;
$$;
