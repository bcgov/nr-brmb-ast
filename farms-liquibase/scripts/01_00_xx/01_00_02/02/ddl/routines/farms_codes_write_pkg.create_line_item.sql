create or replace procedure farms_codes_write_pkg.create_line_item(
   in in_program_year farms.farm_line_items.program_year%type,
   in in_line_item farms.farm_line_items.line_item%type,
   in in_description farms.farm_line_items.description%type,
   in in_eligibility_indicator farms.farm_line_items.eligibility_ind%type,
   in in_eligibility_for_reference_years_indicator farms.farm_line_items.eligibility_for_ref_years_ind%type,
   in in_yardage_indicator farms.farm_line_items.yardage_ind%type,
   in in_program_payment_indicator farms.farm_line_items.program_payment_ind%type,
   in in_contract_work_indicator farms.farm_line_items.contract_work_ind%type,
   in in_supply_managed_commodity_indicator farms.farm_line_items.supply_managed_commodity_ind%type,
   in in_exclude_from_revenue_calculation_indicator farms.farm_line_items.exclude_from_revenue_calc_ind%type,
   in in_industry_average_expense_indicator farms.farm_line_items.industry_average_expense_ind%type,
   in in_established_date farms.farm_line_items.established_date%type,
   in in_expiry_date farms.farm_line_items.expiry_date%type,
   in in_sector_detail_code farms.farm_sector_detail_line_items.sector_detail_code%type,
   in in_fruit_vegetable_type_code farms.farm_line_items.fruit_veg_type_code%type,
   in in_commodity_type_code farms.farm_line_items.commodity_type_code%type,
   in in_user farms.farm_line_items.who_updated%type
)
language plpgsql
as $$
begin
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
        exclude_from_revenue_calc_ind,
        industry_average_expense_ind,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
        fruit_veg_type_code,
        commodity_type_code,
        revision_count
    ) values (
        nextval('farms.farm_li_seq'),
        in_program_year,
        in_line_item,
        in_description,
        'BC',
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
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        in_fruit_vegetable_type_code,
        in_commodity_type_code,
        1
    );

    call farms_codes_write_pkg.update_sector_line_item(
        in_line_item,
        in_program_year,
        in_sector_detail_code,
        in_user
    );
end;
$$;
