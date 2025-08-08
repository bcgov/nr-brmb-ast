create or replace function farms_read_pkg.read_ie(
    in op_ids numeric[],
    in sc_ids numeric[],
    in in_program_year farms.farm_line_items.program_year%type,
    in in_verified_date date default null
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        with a as (
            select rie.reported_income_expense_id,
                   rie.amount,
                   rie.expense_ind,
                   rie.farming_operation_id,
                   rie.agristability_scenario_id,
                   rie.revision_count,
                   rie.line_item
            from farms.farm_reported_income_expenses rie
            where rie.farming_operation_id = any(op_ids)
            and rie.agristability_scenario_id is null
        ), b as (
            select rie.reported_income_expense_id,
                   rie.cra_reported_income_expense_id,
                   rie.amount,
                   rie.expense_ind,
                   rie.farming_operation_id,
                   rie.agristability_scenario_id,
                   rie.revision_count,
                   rie.line_item,
                   rie.who_updated
            from farms.farm_reported_income_expenses rie
            where rie.agristability_scenario_id = any(sc_ids)
        )
        select coalesce(a.farming_operation_id, b.farming_operation_id) as farming_operation_id,
               m.year as program_year,
               li.line_item_id,
               li.line_item,
               li.description,
               li.province,
               li.eligibility_ind,
               coalesce(lipy.eligibility_for_ref_years_ind, 'N') eligibility_for_ref_years_ind,
               li.yardage_ind,
               li.program_payment_ind,
               li.contract_work_ind,
               li.manual_expense_ind,
               li.supply_managed_commodity_ind,
               li.exclude_from_revenue_calc_ind,
               li.industry_average_expense_ind,
               li.commodity_type_code,
               ctc.description commodity_type_code_description,
               li.fruit_veg_type_code,
               fvtc.description fruit_vegetable_type_code_description,
               li.established_date,
               li.expiry_date,
               li.revision_count as li_revision_count,
               a.reported_income_expense_id as cra_reported_income_expense_id,
               a.amount as cra_amount,
               coalesce(a.expense_ind, b.expense_ind) as expense_ind,
               a.revision_count as cra_revision_count,
               b.reported_income_expense_id as adj_reported_income_expense_id,
               b.amount as adj_amount,
               b.agristability_scenario_id as adj_agristability_scenario_id,
               b.who_updated as adjusted_by_user_id,
               b.revision_count as adj_revision_count
        from a
        full outer join b on a.reported_income_expense_id = b.cra_reported_income_expense_id
        join farms.operations_vw m on a.farming_operation_id = m.farming_operation_id
                                   or b.farming_operation_id = m.farming_operation_id
        join farms.farm_line_items li on (li.line_item = a.line_item or li.line_item = b.line_item)
                                and m.year = li.program_year
                                and li.expiry_date >= coalesce(in_verified_date, current_date)
                                and li.established_date < coalesce(in_verified_date, current_date)
        left outer join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = li.fruit_veg_type_code
        left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = li.commodity_type_code
        left outer join farms.farm_line_items lipy on lipy.line_item = li.line_item
                                             and lipy.program_year = in_program_year
                                             and lipy.expiry_date >= coalesce(in_verified_date, current_date)
                                             and lipy.established_date < coalesce(in_verified_date, current_date);
    return cur;
end;
$$;
