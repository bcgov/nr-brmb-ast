create or replace procedure farms_tip_admin_pkg.expenses_benchmarks_ref_years(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_benchmark_expenses o
    using (
      with exp as
      (
        select tby.tip_benchmark_year_id,
               tre.line_item,
               tre.description,
               tre.eligibility_ind,
               round((median(coalesce(tre.amount, 0)))::numeric, 2) amount,
               round((median(coalesce(tre.amount_per_100, 0)))::numeric, 2) amount_per_100,
               percentile_cont(0.25) within group(order by coalesce(tre.amount_per_100, 0) desc) amount_per_100_high_25_pct
        from farms.farm_tip_report_results parent_trr
        join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
        join farms.farm_tip_benchmark_years tby on (   (tby.tip_benchmark_year_id = rtrr.tip_benchmark_year_id_type_3 and parent_trr.farm_type_3_threshold_met_ind = 'Y')
                                              or (tby.tip_benchmark_year_id = rtrr.tip_benchmark_year_id_type_2 and parent_trr.farm_type_2_threshold_met_ind = 'Y')
                                              or (tby.tip_benchmark_year_id = rtrr.tip_benchmark_year_id_type_1 and parent_trr.farm_type_1_threshold_met_ind = 'Y'))
        join farms.farm_tip_report_expenses tre on tre.tip_report_result_id = rtrr.tip_report_result_id
        where (tby.parent_tip_benchmark_year_id is not null and tby.parent_tip_benchmark_year_id::text <> '')
          and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
          and parent_trr.use_for_benchmarks_ind = 'Y'
          and parent_trr.year = in_year
        group by tby.tip_benchmark_year_id,
                 tre.line_item,
                 tre.description,
                 tre.eligibility_ind
      )
      select exp.*
      from exp
      where (exp.line_item is not null and exp.line_item::text <> '') 
      order by exp.tip_benchmark_year_id,
               exp.line_item
    ) n
    on (o.tip_benchmark_year_id = n.tip_benchmark_year_id and o.line_item = n.line_item)
    when not matched then
      insert(tip_benchmark_expense_id,
       line_item,
       description,
       eligibility_ind,
       amount,
       amount_per_100,
       amount_per_100_high_25_pct,
       tip_benchmark_year_id,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
      values (nextval('farms.farm_tbe_seq'),
       n.line_item,
       n.description,
       n.eligibility_ind,
       n.amount,
       n.amount_per_100,
       n.amount_per_100_high_25_pct,
       n.tip_benchmark_year_id,
       1,
       in_user,
       current_timestamp,
       in_user,
       current_timestamp
      );

end;
$$;
