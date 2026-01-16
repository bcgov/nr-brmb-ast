create or replace function farms_export_pkg.f04_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select sta.participant_pin participant_oid,
               sta.year stab_yy,
               fo.operation_number incm_expn_stmt_num,
               rie.line_item incm_expn_code,
               case when rie.expense_ind='Y' then 'E' else 'I' end incm_expn_ind,
               rie.amount incm_expn_amount
        from farms.farm_chef_statement_a_years_vw sta
        join farms.farm_farming_operations fo on fo.program_year_version_id = sta.program_year_version_id
        join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                     and coalesce(rie.agristability_scenario_id::text, '') = ''
        where sta.year = in_program_year;

    return cur;
end;
$$;
