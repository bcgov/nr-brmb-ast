create or replace function farms_import_pkg.is_ie_changed(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.farm_program_year_versions.program_year_version_id%type
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
               ie.expense_ind,
               string_agg(ie.import_comment, ' ' order by ie.import_comment) as import_comment
        from farms.operations_vw m
        join farms.farm_farming_operations op on op.farming_operation_id = m.farming_operation_id
        join farms.farm_reported_income_expenses ie on ie.farming_operation_id = m.farming_operation_id
        where m.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_ind = 'N'
        and ie.agristability_scenario_id is null
        group by m.operation_number, ie.line_item, ie.expense_ind
    ), new_ie as (
        select
        from farms.farm_z04_income_exps_dtls z
        join farms.farm_agristability_clients ac on z.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                    and z.program_year = py.year
        left outer join farms.farm_line_items li on z.line_code = li.line_item
                                                and z.program_year = li.program_year
                                                and (li.expiry_date is null or li.expiry_date > current_date)
        where py.program_year_id = in_program_year_id
        group by (case
                     when li.line_item_id is not null then z.line_code
                     else Unknown
                 end),
                 (case
                     when z.ie_ind = 'E' then 'Y'
                     when z.ie_ind = 'e' then 'Y'
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
