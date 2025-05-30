create or replace function farms_codes_write_pkg.copy_year_line_items(
    in in_to_year farms.line_item.program_year%type,
    in in_user farms.line_item.update_user%type
)
returns numeric
language plpgsql
as $$
declare
    v_from_year farms.line_item.program_year%type;
    v_target_year_count numeric;
    v_rows_inserted numeric := 0;
begin

    select count(*)
    into v_target_year_count
    from farms.line_item li
    where li.program_year = in_to_year;

    if v_target_year_count = 0 then
        select max(li.program_year)
        into v_from_year
        from farms.line_item li
        where li.program_year < in_to_year;

        insert into farms.line_item (
            line_item_id,
            program_year,
            line_item,
            description,
            province,
            eligibility_indicator,
            eligibility_for_reference_years_indicator,
            yardage_indicator,
            program_payment_indicator,
            contract_work_indicator,
            supply_managed_commodity_indicator,
            manual_expense_indicator,
            exclude_from_revenue_calculation_indicator,
            industry_average_expense_indicator,
            established_date,
            expiry_date,
            commodity_type_code,
            fruit_vegetable_type_code,
            create_user,
            create_date,
            update_user,
            update_date,
            revision_count
        )
        select nextval('farms.seq_li') line_item_id,
               in_to_year,
               line_item,
               description,
               province,
               eligibility_indicator,
               eligibility_for_reference_years_indicator,
               yardage_indicator,
               program_payment_indicator,
               contract_work_indicator,
               supply_managed_commodity_indicator,
               manual_expense_indicator,
               exclude_from_revenue_calculation_indicator,
               industry_average_expense_indicator,
               established_date,
               expiry_date,
               commodity_type_code,
               fruit_vegetable_type_code,
               in_user create_user,
               current_timestamp create_date,
               in_user update_user,
               current_timestamp update_date,
               1 revision_count
        from farms.line_item li
        where li.program_year = v_from_year
        and li.expiry_date > current_date
        and li.established_date < current_date
        /* Make sure that we do not copy if line items
           already exist for the year. */
        and not exists (
            select null
            from farms.line_item li2
            where li2.program_year = in_to_year
        );

        v_rows_inserted := v_rows_inserted + sql%rowcount;

    end if;


    select count(*)
    into v_target_year_count
    from farms.sector_detail_line_item sdli
    where sdli.program_year = in_to_year;

    if v_target_year_count = 0 then

        select max(sdli.program_year)
        into v_from_year
        from farms.sector_detail_line_item sdli
        where sdli.program_year < in_to_year;

        insert into farms.sector_detail_line_item (
            sector_detail_line_item_id,
            program_year,
            line_item,
            sector_detail_code,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        select nextval('farms.seq_sdli') sector_detail_line_item_id,
               in_to_year program_year,
               line_item,
               sector_detail_code,
               revision_count,
               create_user,
               create_date,
               update_user,
               update_date
        from farms.sector_detail_line_item sdli
        where sdli.program_year = v_from_year
        /* Make sure that we do not copy if line items
           already exist for the year. */
        and not exists (
            select null
            from farms.sector_detail_line_item sdli2
            where sdli2.program_year = in_to_year
        );

        v_rows_inserted := v_rows_inserted + sql%rowcount;

    end if;

    return v_rows_inserted;

end;
$$;
