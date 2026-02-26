create or replace procedure farms_tip_admin_pkg.reference_year_report_results(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_report_results o
    using (
    with op as (
      select parent_trr.tip_report_result_id parent_tip_report_result_id,
             parent_trr.farm_type_3_name,
             parent_trr.farm_type_2_name,
             parent_trr.farm_type_1_name,
             parent_trr.use_for_benchmarks_ind,
             pyv.program_year_version_id,
             fo.farming_operation_id,
             first_value(pyv.program_year_version_id) over (partition by py.program_year_id order by pyv.program_year_version_number desc) client_latest_pyv_id
      from farms.farm_tip_report_results parent_trr
      join farms.farm_program_years parent_py on parent_py.program_year_id = parent_trr.program_year_id
      join farms.farm_agristability_clients ac on ac.agristability_client_id = parent_py.agristability_client_id
      join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
      join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
      join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
                                     and fo.alignment_key = parent_trr.alignment_key
      join farms.farm_agristability_scenarios s on s.program_year_version_id = pyv.program_year_version_id
      where s.scenario_class_code = 'CRA'
        and fo.locally_generated_ind = 'N'
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
        and parent_py.year = in_year
        and py.year between(parent_py.year - 5) and (parent_py.year - 1)
    ),
    i1 as (
      select fo.farming_operation_id,
             op.parent_tip_report_result_id,
             op.farm_type_3_name,
             op.farm_type_2_name,
             op.farm_type_1_name,
             op.use_for_benchmarks_ind,
             -- income
             case when rie.expense_ind = 'N' then rie.amount else 0 end income,
             case when rie.expense_ind = 'N' and li.eligibility_ind = 'Y' then rie.amount else 0 end allowable_income,
             case when rie.expense_ind = 'N' and li.eligibility_ind = 'N' then rie.amount else 0 end non_allowable_income,
             case when rie.expense_ind = 'N' and (ft1.farm_type_1_name is not null and ft1.farm_type_1_name::text <> '') then rie.amount else 0 end commodity_income,
             -- expenses
             case when rie.expense_ind = 'Y' then rie.amount else 0 end expenses,
             case when rie.expense_ind = 'Y' and li.eligibility_ind = 'Y' then rie.amount else 0 end allowable_expenses,
             case when rie.expense_ind = 'Y' and li.eligibility_ind = 'N' then rie.amount else 0 end non_allowable_expenses,
             case when rie.expense_ind = 'Y' and (tli.tip_farm_type_1_lookup_id is not null and tli.tip_farm_type_1_lookup_id::text <> '') then rie.amount else 0 end commodity_purchases,
             case when rie.expense_ind = 'Y' and li.eligibility_ind = 'Y' and tli.program_payment_for_tips_ind = 'Y' then rie.amount else 0 end allowable_repaymnt_prgm_benfts,
             case when rie.expense_ind = 'Y' and li.eligibility_ind = 'N' and tli.program_payment_for_tips_ind = 'Y' then rie.amount else 0 end non_allowable_repay_prgm_bnfts,
             case when rie.expense_ind = 'Y' and tli.operating_cost_ind = 'Y' then rie.amount else 0 end operating_expenses,
             case when rie.expense_ind = 'Y' and tli.direct_expense_ind = 'Y' then rie.amount else 0 end direct_expenses,
             case when rie.expense_ind = 'Y' and tli.machinery_expense_ind = 'Y' then rie.amount else 0 end machinery_expenses,
             case when rie.expense_ind = 'Y' and tli.land_and_building_expense_ind = 'Y' then rie.amount else 0 end land_build_expenses,
             case when rie.expense_ind = 'Y' and tli.overhead_expense_ind = 'Y' then rie.amount else 0 end overhead_expenses
      from op
      join farms.farm_farming_operations fo on fo.farming_operation_id = op.farming_operation_id
      join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
      join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
      left outer join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                       and coalesce(rie.agristability_scenario_id::text, '') = ''
                                                       and rie.amount != 0
                                                       and rie.line_item not in(575, 9935, 9937, 9938, 9941, 9942)
      left outer join farms.farm_tip_line_items tli_actual on tli_actual.line_item = rie.line_item
      left outer join farms.farm_line_items li on li.program_year = py.year
                                        and li.line_item = case when tli_actual.other_ind = 'Y' then 9896 else rie.line_item end
                                        and li.expiry_date > current_timestamp
      left outer join farms.farm_tip_line_items tli on tli.line_item = li.line_item
      left outer join farms.farm_tip_farm_type_1_lookups ft1 on ft1.tip_farm_type_1_lookup_id = tli.tip_farm_type_1_lookup_id
                                                      and rie.expense_ind = 'N'
      left outer join farms.farm_tip_farm_type_2_lookups ft2 on ft2.tip_farm_type_2_lookup_id = ft1.tip_farm_type_2_lookup_id
      left outer join farms.farm_tip_farm_type_3_lookups ft3 on ft3.tip_farm_type_3_lookup_id = ft2.tip_farm_type_3_lookup_id 
      where op.program_year_version_id = op.client_latest_pyv_id
    ),
    i2 as(
      select farming_operation_id,
             parent_tip_report_result_id,
             farm_type_3_name,
             farm_type_2_name,
             farm_type_1_name,
             use_for_benchmarks_ind,
             -- income
             coalesce((sum(income) over (partition by farming_operation_id)), 0) total_income,
             coalesce((sum(allowable_income) over (partition by farming_operation_id)), 0) allowable_income,
             coalesce((sum(non_allowable_income) over (partition by farming_operation_id)), 0) non_allowable_income,
             sum(commodity_income) over (partition by farming_operation_id) commodity_income,
             sum(commodity_income) over (partition by farming_operation_id, farm_type_3_name) farm_type_3_income,
             sum(commodity_income) over (partition by farming_operation_id, farm_type_2_name) farm_type_2_income,
             sum(commodity_income) over (partition by farming_operation_id, farm_type_1_name) farm_type_1_income,
             -- expenses
             coalesce((sum(                      expenses) over (partition by farming_operation_id)), 0) total_expenses,
             coalesce((sum(            allowable_expenses) over (partition by farming_operation_id)), 0) allowable_expenses,
             coalesce((sum(        non_allowable_expenses) over (partition by farming_operation_id)), 0) non_allowable_expenses,
             coalesce((sum(           commodity_purchases) over (partition by farming_operation_id)), 0) commodity_purchases,
             coalesce((sum(allowable_repaymnt_prgm_benfts) over (partition by farming_operation_id)), 0) allowable_repaymnt_prgm_benfts,
             coalesce((sum(non_allowable_repay_prgm_bnfts) over (partition by farming_operation_id)), 0) non_allowable_repay_prgm_bnfts,
             coalesce((sum(            operating_expenses) over (partition by farming_operation_id)), 0) operating_expenses,
             coalesce((sum(               direct_expenses) over (partition by farming_operation_id)), 0) direct_expenses,
             coalesce((sum(            machinery_expenses) over (partition by farming_operation_id)), 0) machinery_expenses,
             coalesce((sum(           land_build_expenses) over (partition by farming_operation_id)), 0) land_build_expenses,
             coalesce((sum(             overhead_expenses) over (partition by farming_operation_id)), 0) overhead_expenses,
             row_number() over (partition by farming_operation_id order by 1) i2_row_num
      from i1
    ),
    i3 as (
      select i2.*,
             (commodity_purchases + allowable_repaymnt_prgm_benfts) commdity_purchases_repay_bnfts,
             allowable_income - allowable_expenses production_margin,
             total_income - total_expenses net_margin
      from i2
      where i2_row_num = 1
    )
    select ac.participant_pin,
           py.year,
           fo.alignment_key,
           fo.operation_number,
           fo.partnership_pin,
           fo.partnership_name,
           fo.partnership_percent,
           current_timestamp generated_date,
           farm_type_3_name,
           farm_type_2_name,
           farm_type_1_name,
           total_income,
           total_expenses,
           allowable_income,
           allowable_expenses,
           non_allowable_income,
           non_allowable_expenses,
           commodity_income,
           commodity_purchases,
           allowable_repaymnt_prgm_benfts,
           non_allowable_repay_prgm_bnfts,
           commdity_purchases_repay_bnfts,
           round((case when total_income = 0 then 0 else allowable_income               / total_income * 100 end)::numeric, 2) allowable_income_per_100,
           round((case when total_income = 0 then 0 else non_allowable_income           / total_income * 100 end)::numeric, 2) other_farm_income_per100,
           round((case when total_income = 0 then 0 else allowable_expenses             / total_income * 100 end)::numeric, 2) allowable_expenses_per_100,
           round((case when total_income = 0 then 0 else non_allowable_expenses         / total_income * 100 end)::numeric, 2) non_allowable_expenses_per_100,
           round((case when total_income = 0 then 0 else production_margin              / total_income * 100 end)::numeric, 2) production_margin_per_100,
           round((case when total_income = 0 then 0 else net_margin                     / total_income * 100 end)::numeric, 2) net_margin_per_100,
           round((case when total_income = 0 then 0 else total_expenses                 / total_income * 100 end)::numeric, 2) total_expenses_per_100,
           round((case when total_income = 0 then 0 else commdity_purchases_repay_bnfts / total_income * 100 end)::numeric, 2) cmmdty_prchs_rpay_bnft_100,
           round((case when total_income = 0 then 0 else non_allowable_repay_prgm_bnfts / total_income * 100 end)::numeric, 2) non_allowbl_repay_pgm_bnft_100,
           production_margin,
           net_margin,
           operating_expenses operating_cost,
           direct_expenses,
           machinery_expenses,
           land_build_expenses,
           overhead_expenses,
           round(case when total_income = 0 then 0 else  1 - (allowable_expenses / total_income) end, 3) production_margin_ratio,
           round((case when total_income = 0 then 0 else  operating_expenses / total_income end)::numeric, 3) operating_cost_ratio,
           round((case when total_income = 0 then 0 else     direct_expenses / total_income end)::numeric, 3) direct_expenses_ratio,
           round((case when total_income = 0 then 0 else  machinery_expenses / total_income end)::numeric, 3) machinery_expenses_ratio,
           round((case when total_income = 0 then 0 else land_build_expenses / total_income end)::numeric, 3) land_build_expenses_ratio,
           round((case when total_income = 0 then 0 else   overhead_expenses / total_income end)::numeric, 3) overhead_expenses_ratio,
           use_for_benchmarks_ind,
           'Y' reference_year_ind,
           py.program_year_id,
           fo.farming_operation_id,
           i3.parent_tip_report_result_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from i3
    join farms.farm_farming_operations fo on i3.farming_operation_id = fo.farming_operation_id
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id 
    ) n
    on (o.program_year_id = n.program_year_id and o.alignment_key = n.alignment_key and o.parent_tip_report_result_id = n.parent_tip_report_result_id)
    when matched then
      update set
       participant_pin = n.participant_pin,
       year = n.year,
       operation_number = n.operation_number,
       partnership_pin = n.partnership_pin,
       partnership_name = n.partnership_name,
       partnership_percent = n.partnership_percent,
       generated_date = n.generated_date,
       farm_type_3_name = n.farm_type_3_name,
       farm_type_2_name = n.farm_type_2_name,
       farm_type_1_name = n.farm_type_1_name,
       total_income = n.total_income,
       total_expenses = n.total_expenses,
       allowable_income = n.allowable_income,
       allowable_expenses = n.allowable_expenses,
       non_allowable_income = n.non_allowable_income,
       non_allowable_expenses = n.non_allowable_expenses,
       commodity_income = n.commodity_income,
       commodity_purchases = n.commodity_purchases,
       allowable_repaymnt_prgm_benfts = n.allowable_repaymnt_prgm_benfts,
       non_allowable_repay_prgm_bnfts = n.non_allowable_repay_prgm_bnfts,
       commdity_purchases_repay_bnfts = n.commdity_purchases_repay_bnfts,
       allowable_income_per_100 = n.allowable_income_per_100,
       other_farm_income_per100 = n.other_farm_income_per100,
       allowable_expenses_per_100 = n.allowable_expenses_per_100,
       non_allowable_expenses_per_100 = n.non_allowable_expenses_per_100,
       production_margin_per_100 = n.production_margin_per_100,
       total_expenses_per_100 = n.total_expenses_per_100,
       cmmdty_prchs_rpay_bnft_100 = n.cmmdty_prchs_rpay_bnft_100,
       production_margin = n.production_margin,
       net_margin = n.net_margin,
       net_margin_per_100 = n.net_margin_per_100,
       operating_cost = n.operating_cost,
       direct_expenses = n.direct_expenses,
       machinery_expenses = n.machinery_expenses,
       land_build_expenses = n.land_build_expenses,
       overhead_expenses = n.overhead_expenses,
       production_margin_ratio = n.production_margin_ratio,
       operating_cost_ratio = n.operating_cost_ratio,
       direct_expenses_ratio = n.direct_expenses_ratio,
       machinery_expenses_ratio = n.machinery_expenses_ratio,
       land_build_expenses_ratio = n.land_build_expenses_ratio,
       overhead_expenses_ratio = n.overhead_expenses_ratio,
       use_for_benchmarks_ind = n.use_for_benchmarks_ind,
       reference_year_ind = n.reference_year_ind,
       farming_operation_id = n.farming_operation_id,
       revision_count = revision_count + 1,
       who_updated = n.who_updated,
       when_updated = n.when_updated
    when not matched then
      insert(tip_report_result_id,
       participant_pin,
       "year",
       alignment_key,
       operation_number,
       partnership_pin,
       partnership_name,
       partnership_percent,
       generated_date,
       farm_type_3_name,
       farm_type_2_name,
       farm_type_1_name,
       total_income,
       total_expenses,
       allowable_income,
       allowable_expenses,
       non_allowable_income,
       non_allowable_expenses,
       commodity_income,
       commodity_purchases,
       allowable_repaymnt_prgm_benfts,
       non_allowable_repay_prgm_bnfts,
       commdity_purchases_repay_bnfts,
       allowable_income_per_100,
       other_farm_income_per100,
       allowable_expenses_per_100,
       non_allowable_expenses_per_100,
       production_margin_per_100,
       total_expenses_per_100,
       cmmdty_prchs_rpay_bnft_100,
       non_allowbl_repay_pgm_bnft_100,
       production_margin,
       net_margin,
       net_margin_per_100,
       operating_cost,
       direct_expenses,
       machinery_expenses,
       land_build_expenses,
       overhead_expenses,
       production_margin_ratio,
       operating_cost_ratio,
       direct_expenses_ratio,
       machinery_expenses_ratio,
       land_build_expenses_ratio,
       overhead_expenses_ratio,
       use_for_benchmarks_ind,
       reference_year_ind,
       program_year_id,
       farming_operation_id,
       parent_tip_report_result_id,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
      values (nextval('farms.farm_trr_seq'),
       n.participant_pin,
       n.year,
       n.alignment_key,
       n.operation_number,
       n.partnership_pin,
       n.partnership_name,
       n.partnership_percent,
       n.generated_date,
       n.farm_type_3_name,
       n.farm_type_2_name,
       n.farm_type_1_name,
       n.total_income,
       n.total_expenses,
       n.allowable_income,
       n.allowable_expenses,
       n.non_allowable_income,
       n.non_allowable_expenses,
       n.commodity_income,
       n.commodity_purchases,
       n.allowable_repaymnt_prgm_benfts,
       n.non_allowable_repay_prgm_bnfts,
       n.commdity_purchases_repay_bnfts,
       n.allowable_income_per_100,
       n.other_farm_income_per100,
       n.allowable_expenses_per_100,
       n.non_allowable_expenses_per_100,
       n.production_margin_per_100,
       n.total_expenses_per_100,
       n.cmmdty_prchs_rpay_bnft_100,
       n.non_allowbl_repay_pgm_bnft_100,
       n.production_margin,
       n.net_margin,
       n.net_margin_per_100,
       n.operating_cost,
       n.direct_expenses,
       n.machinery_expenses,
       n.land_build_expenses,
       n.overhead_expenses,
       n.production_margin_ratio,
       n.operating_cost_ratio,
       n.direct_expenses_ratio,
       n.machinery_expenses_ratio,
       n.land_build_expenses_ratio,
       n.overhead_expenses_ratio,
       n.use_for_benchmarks_ind,
       n.reference_year_ind,
       n.program_year_id,
       n.farming_operation_id,
       n.parent_tip_report_result_id,
       n.revision_count,
       n.who_created,
       n.when_created,
       n.who_updated,
       n.when_updated
      );

end;
$$;
