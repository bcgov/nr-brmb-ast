create or replace procedure farms_adjustment_pkg.adjust_income_expense(
    in in_action text,
    in in_adj_id farms.farm_reported_income_expenses.reported_income_expense_id%type,
    in in_reported_id farms.farm_reported_income_expenses.cra_reported_income_expense_id%type,
    in in_adj_amount farms.farm_reported_income_expenses.amount%type,
    in in_expense_ind farms.farm_reported_income_expenses.expense_ind%type,
    in in_eligibility_ind farms.farm_line_items.eligibility_ind%type,
    in in_farming_operation_id farms.farm_reported_income_expenses.farming_operation_id%type,
    in in_scenario_id farms.farm_reported_income_expenses.agristability_scenario_id%type,
    in in_parent_scenario_id farms.farm_reported_income_expenses.agristability_scenario_id%type,
    in in_line_item farms.farm_reported_income_expenses.line_item%type,
    in in_revision_count farms.farm_reported_income_expenses.revision_count%type,
    in in_user farms.farm_reported_income_expenses.who_updated%type
)
language plpgsql
as
$$
declare

    v_line_item_count bigint;
    v_line_item_description farms.farm_line_items.description%type;
    v_program_year farms.farm_program_years.year%type;

begin

    select max(sv.year) program_year
    into v_program_year
    from farms.farm_scenarios_vw sv
    where sv.agristability_scenario_id = in_scenario_id;

    if in_action = 'ADD' then
        select count(*)
        into v_line_item_count
        from farms.farm_line_items li
        join farms.farm_scenarios_vw sv on sv.agristability_scenario_id = in_scenario_id
                                        and sv.year = li.program_year
        join farms.farm_scenarios_vw pm on pm.agristability_scenario_id = in_parent_scenario_id
        left outer join farms.farm_line_items pyli on pyli.line_item = li.line_item
                                                   and pyli.program_year = pm.year
        where li.line_item = in_line_item
        and (
            (in_eligibility_ind = 'Y' and (li.eligibility_ind = 'Y' or (in_scenario_id != in_parent_scenario_id and pyli.eligibility_for_ref_years_ind = 'Y')))
            or (in_eligibility_ind = 'N' and not(li.eligibility_ind = 'Y' or (in_scenario_id != in_parent_scenario_id and pyli.eligibility_for_ref_years_ind = 'Y')))
        )
        and current_timestamp between li.established_date and li.expiry_date
        and current_timestamp between pyli.established_date and pyli.expiry_date;

        if v_line_item_count != 1 and (in_scenario_id is not null and in_scenario_id::text <> '') then
            raise exception '%', farms_types_pkg.line_item_not_found_msg() || ' PY: ' || v_program_year || ' - ' || in_line_item || ' - ' || v_line_item_description || ' Eligibility: ' || in_eligibility_ind
            using errcode = farms_types_pkg.line_item_not_found_num()::text;
        end if;

        insert into farms.farm_reported_income_expenses (
            reported_income_expense_id,
            amount,
            expense_ind,
            farming_operation_id,
            line_item,
            agristability_scenario_id,
            who_created,
            who_updated,
            when_updated,
            cra_reported_income_expense_id
        ) values (
            nextval('farms.farm_rie_seq'),
            in_adj_amount,
            in_expense_ind,
            in_farming_operation_id,
            in_line_item,
            in_scenario_id,
            in_user,
            in_user,
            current_timestamp,
            in_reported_id
        );
    elsif in_action = 'DELETE' then
        delete from farms.farm_reported_income_expenses ie
        where ie.reported_income_expense_id = in_adj_id;
    elsif in_action = 'UPDATE' then
        update farms.farm_reported_income_expenses
        set amount = in_adj_amount,
            who_updated = in_user,
            when_updated = current_timestamp,
            revision_count = revision_count + 1
        where reported_income_expense_id = in_adj_id
        and revision_count = in_revision_count;
    end if;

    if (in_scenario_id is not null and in_scenario_id::text <> '') then
        insert into farms.farm_scenario_logs (
           scenario_log_id,
           log_message,
           agristability_scenario_id,
           who_created,
           who_updated)
        select nextval('farms.farm_sl_seq'),
               '[' || case when in_expense_ind='Y' then  'Expense'  else 'Income' end
               || ' Adjustment] Code: "'
               || li.line_item
               || ' - '
               || li.description
               || '", Year: '
               || sv.year
               || case
                      when in_action = 'DELETE' then ', Adjustment Deleted'
                      else ', Adjustment Amount: ' || round((in_adj_amount)::numeric, 2)
                  end
               log_message,
               in_parent_scenario_id,
               in_user,
               in_user
        from farms.farm_scenarios_vw sv
        join farms.farm_line_items li on li.line_item = in_line_item and li.program_year = sv.year
        where sv.agristability_scenario_id = in_scenario_id
        and li.line_item = in_line_item
        and li.expiry_date >= current_timestamp
        and li.established_date < current_timestamp;
    end if;

end;
$$;
