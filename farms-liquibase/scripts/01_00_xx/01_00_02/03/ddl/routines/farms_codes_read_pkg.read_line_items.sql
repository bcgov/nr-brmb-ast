create or replace function farms_codes_read_pkg.read_line_items(
    in in_program_year farms.farm_line_items.program_year%type,
    in in_line_item farms.farm_line_items.line_item%type
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
               li.eligibility_ind,
               li.eligibility_for_ref_years_ind,
               li.yardage_ind,
               li.program_payment_ind,
               li.contract_work_ind,
               li.supply_managed_commodity_ind,
               li.exclude_from_revenue_calc_ind,
               li.industry_average_expense_ind,
               li.established_date,
               li.expiry_date,
               li.revision_count,
               li.fruit_veg_type_code,
               ftvc.description fruit_vegetable_type_code_description,
               li.commodity_type_code,
               ctc.description commodity_type_code_description,
               sdli.sector_detail_line_item_id,
               sc.sector_code,
               sc.description sector_code_description,
               sdc.sector_detail_code,
               sdc.description sector_detail_code_description
        from farms.farm_line_items li
        left outer join farms.farm_sector_detail_line_items sdli on sdli.line_item = li.line_item
                                                           and sdli.program_year = li.program_year
        left outer join farms.farm_sector_detail_xref sdx on sdx.sector_detail_code = sdli.sector_detail_code
        left outer join farms.farm_sector_detail_codes sdc on sdc.sector_detail_code = sdli.sector_detail_code
        left outer join farms.farm_sector_codes sc on sc.sector_code = sdx.sector_code
        left outer join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = li.fruit_veg_type_code
        left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = li.commodity_type_code
        where li.program_year = in_program_year
        and (in_line_item is null or li.line_item = in_line_item)
        and li.expiry_date > current_date
        and li.established_date < current_date
        order by lower(li.description);

    return cur;
end;
$$;
