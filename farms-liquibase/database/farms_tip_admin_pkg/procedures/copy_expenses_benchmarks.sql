create or replace procedure farms_tip_admin_pkg.copy_expenses_benchmarks(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    -- program year only. reference year is not needed for the reports.
    merge into farms.farm_tip_report_expenses o
    using(
      select tre.tip_report_expense_id,
             tbe.amount_per_100 amount_per_100_benchmark,
             tbe.amount_per_100_high_25_pct,
             case
               when abs(tre.amount_per_100 - tbe.amount_per_100) > 5 then
                 case
                    when tre.amount_per_100 <= tbe.amount_per_100 then 'GOOD'
                    when tre.amount_per_100 <  tbe.amount_per_100_high_25_pct then 'CAUTION'
                    else 'CONCERN'
                 end
             end tip_rating_code
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_benchmark_years tby on tby.tip_benchmark_year_id = parent_trr.tip_benchmark_year_id
      join farms.farm_tip_report_expenses tre on tre.tip_report_result_id = parent_trr.tip_report_result_id
      join farms.farm_tip_benchmark_expenses tbe on tbe.tip_benchmark_year_id = tby.tip_benchmark_year_id
                                          and tbe.line_item = tre.line_item
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    ) n
    on (o.tip_report_expense_id = n.tip_report_expense_id)
    when matched then
      update set
       amount_per_100_benchmark = n.amount_per_100_benchmark,
       amount_per_100_high_25_pct = n.amount_per_100_high_25_pct,
       tip_rating_code = n.tip_rating_code,
       revision_count = revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp;

end;
$$;
