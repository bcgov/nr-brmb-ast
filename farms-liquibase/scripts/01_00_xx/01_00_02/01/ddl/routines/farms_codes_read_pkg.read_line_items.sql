create or replace function farms_codes_read_pkg.read_line_items(
    in in_program_year farms.line_item.program_year%type,
    in in_line_item farms.line_item.line_item%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select li.line_item_id,
               li.program_year,
               li.line_item,
               li.description,
               li.province,
               li.eligibility_indicator,
               li.eligibility_for_reference_years_indicator,
               li.yardage_indicator,
               li.program_payment_indicator,
               li.contract_work_indicator,
               li.supply_managed_commodity_indicator,
               li.exclude_from_revenue_calculation_indicator,
               li.industry_average_expense_indicator,
               li.effective_date,
               li.expiry_date,
               li.revision_count,
               li.fruit_vegetable_type_code,
               ftvc.description fruit_vegetable_type_code_description,
               li.commodity_type_code,
               ctc.description commodity_type_code_description,
               sdli.sector_detail_line_item_id,
               sc.sector_code,
               sc.description sector_code_description,
               sdc.sector_detail_code,
               sdc.description sector_detail_code_description
        from farms.line_item li
        left outer join farms.sector_detail_line_item sdli on sdli.line_item = li.line_item
                                                           and sdli.program_year = li.program_year
        left outer join farms.sector_detail_xref sdx on sdx.sector_detail_code = sdli.sector_detail_code
        left outer join farms.sector_detail_code sdc on sdc.sector_detail_code = sdli.sector_detail_code
        left outer join farms.sector_code sc on sc.sector_code = sdx.sector_code
        left outer join farms.fruit_vegetable_type_code fvtc on fvtc.fruit_vegetable_type_code = li.fruit_vegetable_type_code
        left outer join farms.commodity_type_code ctc on ctc.commodity_type_code = li.commodity_type_code
        where li.program_year = in_program_year
        and (in_line_item is null or li.line_item = in_line_item)
        and li.expiry_date > current_date
        and li.effective_date < current_date
        order by lower(li.description);

    return cur;
end;
$$;
