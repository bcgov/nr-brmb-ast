create or replace function farms_report_pkg.national_surveillance_strategy(
    in in_program_year farms.farm_program_years.year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        -- This query was copied to Analytical_Surveillance_Strategy (which has additional columns added).
        -- If changes are made here they may be needed there as well.
        with scenarios as (
            select m.participant_pin,
                   'BC' province,
                   m.year program_year,
                   sec.description farm_type,
                   sdc.description farm_type_detailed,
                   bct.total_allowable_income allowable_revenue,
                   (bct.total_allowable_income + bct.accrual_adjs_crop_inventory + bct.accrual_adjs_lvstck_inventory + accrual_adjs_receivables) accrued_allowable_revenue,
                   bct.expense_accr_adjs accrued_allowable_expenses,
                   cl.program_year_margin margin,
                   cl.prod_insur_deemed_benefit,
                   cl.adjusted_reference_margin reference_margin_before_limit,
                   cl.reference_margin_limit,
                   bct.accrual_adjs_purchased_inputs,
                   bct.accrual_adjs_payables,
                   m.program_year_version_id,
                   m.agristability_scenario_id,
                   s.combined_farm_number,
                   (case when s.combined_farm_number is not null then cl.applied_benefit_percent end) combined_farm_percent
            from farms.farm_agri_scenarios_vw m
            join farms.farm_program_years py on py.program_year_id = m.program_year_id
            join farms.farm_agristability_scenarios s on s.agristability_scenario_id = m.agristability_scenario_id
            join farms.farm_benefit_calc_totals bct on bct.agristability_scenario_id = m.agristability_scenario_id
            join farms.farm_agristability_claims cl on cl.agristability_scenario_id = m.agristability_scenario_id
            join farms.farm_sector_detail_codes sdc on sdc.sector_detail_code = farms_report_pkg.get_sector_detail_code(s.agristability_scenario_id)
            join farms.farm_sector_detail_xref sdx on sdx.sector_detail_code = sdc.sector_detail_code
            join farms.farm_sector_codes sec on sec.sector_code = sdx.sector_code
            where m.year = in_program_year
            and py.non_participant_ind = 'N'
            and s.scenario_state_code = 'COMP'
            and s.scenario_category_code in ('FIN', 'AADJ', 'PADJ')
        ), expenses as (
            select s.agristability_scenario_id,
                   sum(case
                       when li.eligibility_ind = 'N' and li.line_item in (9760,9808,9821,9807,9825,9798,9824,9796,9823,9809,9826,9795,9804,9820,9819,9792) then rie.amount * fo.partnership_percent
                       else 0
                   end) overhead_expenses,
                   sum(case
                       when li.eligibility_ind = 'N' and li.line_item in (9805,9811,9810,9765,9829) then rie.amount * fo.partnership_percent
                       else 0
                   end) finance_and_capital_expenses,
                   sum(case
                       when li.eligibility_ind = 'N' and li.line_item in (9936) then rie.amount * fo.partnership_percent
                       else 0
                   end) depreciation,
                   sum(case
                       when li.eligibility_ind = 'N' and li.line_item in (9816) then rie.amount * fo.partnership_percent
                       else 0
                   end) non_arms_length_salaries
            from scenarios s
            join farms.farm_farming_operations fo on fo.program_year_version_id = s.program_year_version_id
            left outer join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                               and (s.agristability_scenario_id = rie.agristability_scenario_id or rie.agristability_scenario_id is null)
                                                               and rie.expense_ind = 'Y'
            left outer join farms.farm_line_items li on li.line_item = rie.line_item
                                               and li.program_year = s.program_year
                                               and li.expiry_date >= current_date
                                               and li.established_date < current_date
            group by s.agristability_scenario_id
        ), single_and_cf as (
            select s.participant_pin,
                   s.province,
                   s.program_year,
                   s.farm_type,
                   s.farm_type_detailed,
                   s.allowable_revenue,
                   s.accrued_allowable_revenue,
                   s.accrued_allowable_expenses,
                   s.margin,
                   coalesce(s.prod_insur_deemed_benefit, 0) prod_insur_deemed_benefit,
                   s.overhead_expenses,
                   s.finance_and_capital_expenses,
                   s.depreciation,
                   s.non_arms_length_salaries,
                   s.reference_margin_before_limit,
                   s.reference_margin_limit,
                   s.combined_farm_percent
            from scenarios s
            join expenses e on e.agristability_scenario_id = s.agristability_scenario_id
            where s.combind_farm_number is null
            union all
            select s.participant_pin,
                   s.province,
                   s.program_year,
                   s.farm_type,
                   s.farm_type_detailed,
                   (s.allowable_revenue * s.combined_farm_percent) allowable_revenue,
                   (s.accrued_allowable_revenue * s.combined_farm_percent) accrued_allowable_revenue,
                   (s.accrued_allowable_expenses * s.combined_farm_percent) accrued_allowable_expenses,
                   (s.margin * s.combined_farm_percent) margin,
                   coalesce(s.prod_insur_deemed_benefit * s.combined_farm_percent, 0) prod_insur_deemed_benefit,
                   sum(e.overhead_expenses) over (partition by s.combined_farm_number) * s.combined_farm_percent overhead_expenses,
                   sum(e.finance_and_capital_expenses) over (partition by s.combined_farm_number) * s.combined_farm_percent finance_and_capital_expenses,
                   sum(e.depreciation) over (partition by s.combined_farm_number) * s.combined_farm_percent depreciation,
                   sum(e.non_arms_length_salaries) over (partition by s.combined_farm_number) * s.combined_farm_percent non_arms_length_salaries,
                   s.reference_margin_before_limit * s.combined_farm_percent reference_margin_before_limit,
                   s.reference_margin_limit * s.combined_farm_percent reference_margin_limit,
                   s.combined_farm_percent
            from scenarios s
            join expenses e on e.agristability_scenario_id = s.agristability_scenario_id
            where s.combind_farm_number is not null
        )
        select s.participant_pin,
               s.province,
               s.program_year,
               s.farm_type,
               s.farm_type_detailed,
               round(s.allowable_revenue, 2) allowable_revenue,
               round(s.accrued_allowable_revenue, 2) accrued_allowable_revenue,
               round(s.accrued_allowable_expenses, 2) accrued_allowable_expenses,
               round(s.margin, 2) margin,
               round(s.prod_insur_deemed_benefit, 2) prod_insur_deemed_benefit,
               round(s.overhead_expenses, 2) overhead_expenses,
               round(s.finance_and_capital_expenses, 2) finance_and_capital_expenses,
               round(s.depreciation, 2) depreciation,
               round(s.non_arms_length_salaries, 2) non_arms_length_salaries,
               round(s.reference_margin_before_limit, 2) reference_margin_before_limit,
               round(s.reference_margin_limit, 2) reference_margin_limit,
               s.combined_farm_percent
        from single_and_cf s
        order by s.participant_pin;

    return cur;
end;
$$;
