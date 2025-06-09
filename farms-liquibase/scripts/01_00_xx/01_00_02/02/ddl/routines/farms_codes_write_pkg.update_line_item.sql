create or replace procedure farms_codes_write_pkg.update_line_item(
   in in_line_item_id farms.line_item.line_item_id%type,
   in in_program_year farms.line_item.program_year%type,
   in in_line_item farms.line_item.line_item%type,
   in in_description farms.line_item.description%type,
   in in_eligibility_indicator farms.line_item.eligibility_indicator%type,
   in in_eligibility_for_reference_years_indicator farms.line_item.eligibility_for_reference_years_indicator%type,
   in in_yardage_indicator farms.line_item.yardage_indicator%type,
   in in_program_payment_indicator farms.line_item.program_payment_indicator%type,
   in in_contract_work_indicator farms.line_item.contract_work_indicator%type,
   in in_supply_managed_commodity_indicator farms.line_item.supply_managed_commodity_indicator%type,
   in in_exclude_from_revenue_calculation_indicator farms.line_item.exclude_from_revenue_calculation_indicator%type,
   in in_industry_average_expense_indicator farms.line_item.industry_average_expense_indicator%type,
   in in_established_date farms.line_item.established_date%type,
   in in_expiry_date farms.line_item.expiry_date%type,
   in in_sector_detail_code farms.sector_detail_line_item.sector_detail_code%type,
   in in_revision_count farms.line_item.revision_count%type,
   in in_fruit_vegetable_type_code farms.line_item.fruit_vegetable_type_code%type,
   in in_commodity_type_code farms.line_item.commodity_type_code%type,
   in in_user farms.line_item.update_user%type
)
language plpgsql
as $$
begin
    call farms_codes_write_pkg.create_line_item(
        in_program_year,
        in_line_item,
        in_description,
        in_eligibility_indicator,
        in_eligibility_for_reference_years_indicator,
        in_yardage_indicator,
        in_program_payment_indicator,
        in_contract_work_indicator,
        in_supply_managed_commodity_indicator,
        in_exclude_from_revenue_calculation_indicator,
        in_industry_average_expense_indicator,
        in_established_date,
        in_expiry_date,
        in_sector_detail_code,
        in_fruit_vegetable_type_code,
        in_commodity_type_code,
        in_user
    );

    update farms.line_item c
    set c.expiry_date = current_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp,
        c.fruit_vegetable_type_code = in_fruit_vegetable_type_code,
        c.commodity_type_code = in_commodity_type_code
    where c.line_item_id = in_line_item_id
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
