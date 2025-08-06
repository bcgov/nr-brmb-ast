create or replace function farms_import_pkg.reported_income_expense(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_program_year_version_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    ie_insert_cursor cursor for
        select op.farming_operation_id,
               coalesce(li.line_item, Unknown::numeric) line_item,
               string_agg(case
                   when li.line_item_id::varchar is not null then null
                   else z.line_code::varchar
               end, ' ' order by case
                   when li.line_item_id is not null then null
                   else z.line_code
               end) as import_comment,
               sum(z.amount) amount,
               (case
                   when z.ie_ind = 'E' then 'Y'
                   when z.ie_ind = 'e' then 'Y'
                   else 'N'
               end) as expense_ind
        from farms.farm_z04_income_exps_dtls z
        join farms.farm_agristability_clients ac on z.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and z.program_year = py.year
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_farming_operations op on op.program_year_version_id = pyv.program_year_version_id
                                        and op.operation_number = z.operation_number
        left outer join farms.farm_line_items li on li.line_item = z.line_code
                                           and li.program_year = z.program_year
                                           and (li.expiry_date is null or li.expiry_date > current_date)
        where pyv.program_year_version_id = in_program_year_version_id
        group by op.farming_operation_id,
                 li.line_item,
                 (case
                     when z.ie_ind = 'E' then 'Y'
                     when z.ie_ind = 'e' then 'Y'
                     else 'N'
                 end);
    ie_insert_val record;

    v_line_item farms.farm_line_items.line_item%type := null;
    v_farming_operation_id farms.farm_reported_income_expenses.farming_operation_id%type := null;

    t_id numeric;

    yr numeric := null;
    cnt numeric := null;

    expry date := null;
begin

    select py.year program_year
    into yr
    from farms.farm_program_years py
    where py.program_year_id = in_program_year_id;

    select count(*)
    into cnt
    from farms.farm_line_items li
    where li.line_item = Unknown::numeric
    and li.program_year = yr;

    if cnt < 1 then
        select nextval('farms.seq_li')
        into t_id;

        expry := to_date('31/12/9999', 'DD/MM/YYYY');

        raise exception 'Line items have not been created for %s. Go to the Line Items screen in Code Management and click the button, Copy From %s To %s, to create them.',
            yr, (yr - 1), yr;
    end if;

    for ie_insert_val in ie_insert_cursor
    loop

        v_line_item := ie_insert_val.line_item;
        v_farming_operation_id := ie_insert_val.farming_operation_id;

        select nextval('farms.seq_rie')
        into t_id;

        insert into farms.farm_reported_income_expenses (
            reported_income_expense_id,
            amount,
            expense_ind,
            import_comment,
            line_item,
            farming_operation_id,
            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            t_id,
            ie_insert_val.amount,
            ie_insert_val.expense_ind,
            ie_insert_val.import_comment,
            ie_insert_val.line_item,
            ie_insert_val.farming_operation_id,
            null,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );
    end loop;

    return null;
exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_income_expense(
            sqlerrm,
            v_line_item,
            v_farming_operation_id
        ));
end;
$$;
