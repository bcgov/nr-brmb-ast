create or replace function farms_codes_write_pkg.copy_year_line_items(
    in in_to_year farms.farm_line_items.program_year%type,
    in in_user farms.farm_line_items.who_updated%type
)
returns numeric
language plpgsql
as $$
declare
    v_from_year farms.farm_line_items.program_year%type;
    v_target_year_count numeric;
    v_rows_inserted numeric := 0;
    v_total_rows_inserted numeric := 0;
begin

    select count(*)
    into v_target_year_count
    from farms.farm_line_items li
    where li.program_year = in_to_year;

    if v_target_year_count = 0 then
        select max(li.program_year)
        into v_from_year
        from farms.farm_line_items li
        where li.program_year < in_to_year;

        insert into farms.farm_line_items (
            line_item_id,
            program_year,
            line_item,
            description,
            province,
            eligibility_ind,
            eligibility_for_ref_years_ind,
            yardage_ind,
            program_payment_ind,
            contract_work_ind,
            supply_managed_commodity_ind,
            manual_expense_ind,
            exclude_from_revenue_calc_ind,
            industry_average_expense_ind,
            established_date,
            expiry_date,
            commodity_type_code,
            fruit_veg_type_code,
            who_created,
            when_created,
            who_updated,
            when_updated,
            revision_count
        )
        select nextval('farms.seq_li') line_item_id,
               in_to_year,
               line_item,
               description,
               province,
               eligibility_ind,
               eligibility_for_ref_years_ind,
               yardage_ind,
               program_payment_ind,
               contract_work_ind,
               supply_managed_commodity_ind,
               manual_expense_ind,
               exclude_from_revenue_calc_ind,
               industry_average_expense_ind,
               established_date,
               expiry_date,
               commodity_type_code,
               fruit_veg_type_code,
               in_user who_created,
               current_timestamp when_created,
               in_user who_updated,
               current_timestamp when_updated,
               1 revision_count
        from farms.farm_line_items li
        where li.program_year = v_from_year
        and li.expiry_date > current_date
        and li.established_date < current_date
        /* Make sure that we do not copy if line items
           already exist for the year. */
        and not exists (
            select null
            from farms.farm_line_items li2
            where li2.program_year = in_to_year
        );

        get diagnostics v_rows_inserted := row_count;
        v_total_rows_inserted := v_total_rows_inserted + v_rows_inserted;

    end if;


    select count(*)
    into v_target_year_count
    from farms.farm_sector_detail_line_items sdli
    where sdli.program_year = in_to_year;

    if v_target_year_count = 0 then

        select max(sdli.program_year)
        into v_from_year
        from farms.farm_sector_detail_line_items sdli
        where sdli.program_year < in_to_year;

        insert into farms.farm_sector_detail_line_items (
            sector_detail_line_item_id,
            program_year,
            line_item,
            sector_detail_code,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        )
        select nextval('farms.seq_sdli') sector_detail_line_item_id,
               in_to_year program_year,
               line_item,
               sector_detail_code,
               revision_count,
               who_created,
               when_created,
               who_updated,
               when_updated
        from farms.farm_sector_detail_line_items sdli
        where sdli.program_year = v_from_year
        /* Make sure that we do not copy if line items
           already exist for the year. */
        and not exists (
            select null
            from farms.farm_sector_detail_line_items sdli2
            where sdli2.program_year = in_to_year
        );

        get diagnostics v_rows_inserted := row_count;
        v_total_rows_inserted := v_total_rows_inserted + v_rows_inserted;

    end if;

    return v_total_rows_inserted;

end;
$$;
