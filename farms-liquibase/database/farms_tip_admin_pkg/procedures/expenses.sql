create or replace procedure farms_tip_admin_pkg.expenses(
   in in_year farms.farm_program_years.year%type,
   in in_mode text,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    -- program year and reference year expenses
    merge into farms.farm_tip_report_expenses o
    using (
    with rids as (
      select parent_trr.tip_report_result_id, parent_trr.year program_year
      from farms.farm_tip_report_results parent_trr
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
      and parent_trr.year = in_year
      and in_mode = 'PROGRAM_YEAR'
      
union all

      select rtrr.tip_report_result_id, parent_trr.year program_year
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
      and parent_trr.year = in_year
      and in_mode = 'REFERENCE_YEAR' 
    ),
    ex1 as (
      select trr.tip_report_result_id,
             li.eligibility_ind,
             li.line_item,
             li.description,
             sum(rie.amount) over (partition by trr.tip_report_result_id, li.line_item) amount,
             trr.total_income,
             row_number() over (partition by trr.tip_report_result_id, li.line_item order by 1) ex1_rownum
      from rids
      join farms.farm_tip_report_results trr on trr.tip_report_result_id = rids.tip_report_result_id
      join farms.farm_reported_income_expenses rie on rie.farming_operation_id = trr.farming_operation_id
                                            and coalesce(rie.agristability_scenario_id::text, '') = ''
                                            and rie.line_item not in (575, 9935, 9937, 9938, 9941, 9942)
      left outer join farms.farm_tip_line_items tli_actual on tli_actual.line_item = rie.line_item
      join farms.farm_line_items li on li.program_year = trr.year
                             and li.line_item = case when tli_actual.other_ind = 'Y' then 9896 else rie.line_item end
                             and li.expiry_date > current_timestamp
      left outer join farms.farm_tip_line_items tli on tli.line_item = li.line_item
      where rie.expense_ind = 'Y'
        and coalesce(tli.program_payment_for_tips_ind, 'N') = 'N'
        and coalesce(tli.tip_farm_type_1_lookup_id::text, '') = ''
        and (in_mode != 'REFERENCE_YEAR' -- for the reference years, only include expenses that exist for the program year
             or exists (select *
                        from farms.farm_tip_report_results parent_trr
                        join farms.farm_tip_report_expenses parent_tre on parent_tre.tip_report_result_id = parent_trr.tip_report_result_id
                        where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
                          and parent_trr.year = rids.program_year
                          and parent_tre.line_item = li.line_item 
                       )
            ) 
    )
    select ex1.*,
           case when total_income = 0 then 0 else round((amount / total_income * 100)::numeric, 2) end amount_per_100
    from ex1 
    where ex1_rownum = 1
    ) n
    on (o.tip_report_result_id = n.tip_report_result_id and o.line_item = n.line_item)
    when matched then
      update set
       eligibility_ind = n.eligibility_ind,
       description = n.description,
       amount = n.amount,
       amount_per_100 = n.amount_per_100,
       revision_count = revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp
    when not matched then
      insert(tip_report_expense_id,
       line_item,
       description,
       eligibility_ind,
       amount,
       amount_per_100,
       tip_report_result_id,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
      values (nextval('farms.farm_tre_seq'),
       n.line_item,
       n.description,
       n.eligibility_ind,
       n.amount,
       n.amount_per_100,
       n.tip_report_result_id,
       1,
       in_user,
       current_timestamp,
       in_user,
       current_timestamp
      );

    -- reference year expenses that don't exist for the program year.
    -- create for the program year with a value of zero.
    merge into farms.farm_tip_report_expenses o
    using(
    with ex1 as (
      select rtrr.parent_tip_report_result_id,
             coalesce(li.eligibility_ind, rtre.eligibility_ind) eligibility_ind,
             coalesce(li.line_item, rtre.line_item) line_item,
             li.description,
             0 amount,
             0 amount_per_100,
             0 amount_per_100_5_year,
             0 amount_per_100_benchmark,
             row_number() over (partition by rtrr.parent_tip_report_result_id, rtre.line_item order by rtrr.year desc) ex1_rownum
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      join farms.farm_tip_report_expenses rtre on rtre.tip_report_result_id = rtrr.tip_report_result_id
      join farms.farm_line_items li on li.program_year = parent_trr.year
                             and li.line_item = rtre.line_item
                             and li.expiry_date > current_timestamp
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    )
    select ex1.*
    from ex1
    where ex1_rownum = 1
    ) n
    on (o.tip_report_result_id = n.parent_tip_report_result_id and o.line_item = n.line_item)
    when not matched then
      insert(tip_report_expense_id,
       line_item,
       description,
       eligibility_ind,
       amount,
       amount_per_100,
       amount_per_100_5_year,
       amount_per_100_benchmark,
       tip_report_result_id,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
      values (nextval('farms.farm_tre_seq'),
       n.line_item,
       n.description,
       n.eligibility_ind,
       n.amount,
       n.amount_per_100,
       n.amount_per_100_5_year,
       n.amount_per_100_benchmark,
       n.parent_tip_report_result_id,
       1,
       in_user,
       current_timestamp,
       in_user,
       current_timestamp
      );

end;
$$;
