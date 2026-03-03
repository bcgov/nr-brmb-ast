create or replace procedure farms_tip_admin_pkg.expenses_benchmarks_5yr_avg(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    -- for reference years, there may be a few expenses that do not
    -- have an expense benchmark in the program year. ignore those
    -- because there is no program year expense benchmark record
    -- where the 5-year average can be set.
    merge into farms.farm_tip_benchmark_expenses o
    using(
      select tby.tip_benchmark_year_id,
             tbe.line_item,
             round((avg(coalesce(rtbe.amount_per_100, 0)))::numeric, 2) amount_per_100_5_year_average
      from farms.farm_tip_benchmark_years tby
      join farms.farm_tip_benchmark_years rtby on rtby.parent_tip_benchmark_year_id = tby.tip_benchmark_year_id
      join farms.farm_tip_benchmark_expenses tbe on tbe.tip_benchmark_year_id = tby.tip_benchmark_year_id
      left outer join farms.farm_tip_benchmark_expenses rtbe on rtbe.tip_benchmark_year_id = rtby.tip_benchmark_year_id
                                                      and rtbe.line_item = tbe.line_item
      where tby.program_year = in_year
        and tby.reference_year = tby.program_year
      group by tby.tip_benchmark_year_id,
               tbe.line_item
      order by tby.tip_benchmark_year_id,
               tbe.line_item
    ) n
    on (o.tip_benchmark_year_id = n.tip_benchmark_year_id and o.line_item = n.line_item)
    when matched then
      update set
       amount_per_100_5_year_average = n.amount_per_100_5_year_average,
       who_updated = in_user,
       when_updated = current_timestamp;

end;
$$;
