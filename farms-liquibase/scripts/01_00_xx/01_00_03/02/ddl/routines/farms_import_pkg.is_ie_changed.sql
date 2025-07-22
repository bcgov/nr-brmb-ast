create or replace function farms_import_pkg.is_ie_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    cnt numeric := null;
begin

    -- check Reported Income / Expense
    with cur_ie as (
        select m.operation_number,
               ie.line_item,
               sum(ie.amount) as amount,
               ie.expense_indicator,
               string_agg(ie.import_comment, ' ' order by ie.import_comment) as import_comment
        from farms.operations_vw m
        join farms.farming_operation op on op.farming_operation_id = m.farming_operation_id
        join farms.reported_income_expenses ie on ie.farming_operation_id = m.farming_operation_id
        where m.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_indicator = 'N'
        and ie.agristability_scenario_id is null
        group by m.operation_number, ie.line_item, ie.expense_indicator
    ), new_ie as (
        select z.operation_number,
               (case
                   when li.line_item_id is not null then z.line_code
                   else Unknown::numeric
               end) as line_item,
               sum(z.amount) amount,
               (case
                   when z.ie_indicator = 'E' then 'Y'
                   when z.ie_indicator = 'e' then 'Y'
                   else 'N'
               end) as expense_indicator,
               string_agg(case
                   when li.line_item_id::varchar is not null then null
                   else z.line_code::varchar
               end, ' ' order by case
                   when li.line_item_id is not null then null
                   else z.line_code
               end) as import_comment
        from farms.z04_income_expenses_detail z
        join farms.agristability_client ac on z.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and z.program_year = py.year
        left outer join farms.line_item li on z.line_code = li.line_item
                                                and z.program_year = li.program_year
                                                and (li.expiry_date is null or li.expiry_date > current_date)
        where py.program_year_id = in_program_year_id
        group by z.operation_number,
                 (case
                     when li.line_item_id is not null then z.line_code
                     else Unknown::numeric
                 end),
                 (case
                     when z.ie_indicator = 'E' then 'Y'
                     when z.ie_indicator = 'e' then 'Y'
                     else 'N'
                 end)
    )
    select count(*) cnt
    into cnt
    from (
        (
            select *
            from cur_ie
            except
            select *
            from new_ie
        )
        union all
        (
            select *
            from new_ie
            except
            select *
            from cur_ie
        )
    ) t;

    if cnt > 0 then
        return true;
    end if;

    return false;
end;
$$;
