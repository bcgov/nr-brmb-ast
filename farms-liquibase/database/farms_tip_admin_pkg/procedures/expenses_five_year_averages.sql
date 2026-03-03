create or replace procedure farms_tip_admin_pkg.expenses_five_year_averages(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_report_expenses o
    using(
    with ex1 as (
      select parent_trr.tip_report_result_id,
             parent_tre.line_item,
             count(distinct rtrr.tip_report_result_id) over (partition by parent_trr.tip_report_result_id) reference_year_count,
             sum(coalesce(rtre.amount_per_100, 0)) over (partition by parent_trr.tip_report_result_id, parent_tre.line_item) amount_per_100_5_year_total,
             row_number() over (partition by parent_trr.tip_report_result_id, parent_tre.line_item order by 1) ex1_rownum
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_expenses parent_tre on parent_tre.tip_report_result_id = parent_trr.tip_report_result_id
      left outer join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      left outer join farms.farm_tip_report_expenses rtre on rtre.tip_report_result_id = rtrr.tip_report_result_id
                                                   and rtre.line_item = parent_tre.line_item
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    )
    select ex1.*,
           case when reference_year_count = 0 then 0 else round((amount_per_100_5_year_total / reference_year_count)::numeric, 2) end amount_per_100_5_year
    from ex1
    where ex1_rownum = 1
    ) n
    on (o.tip_report_result_id = n.tip_report_result_id and o.line_item = n.line_item)
    when matched then
      update set
       amount_per_100_5_year = n.amount_per_100_5_year,
       revision_count = revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp;

end;
$$;
