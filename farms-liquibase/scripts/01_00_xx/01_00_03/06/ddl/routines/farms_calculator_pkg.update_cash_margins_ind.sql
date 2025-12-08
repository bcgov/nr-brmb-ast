create or replace procedure farms_calculator_pkg.update_cash_margins_ind(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_cash_margins_ind farms.farm_program_years.cash_margins_ind%type,
    in in_cash_margins_opt_in_date farms.farm_program_years.cash_margins_opt_in_date%type,
    in in_user farms.farm_program_years.assigned_to_userid%type
)
language plpgsql
as
$$
declare

    v_program_year_id farms.farm_program_years.program_year_id%type;

begin

    select pyv.program_year_id
    into v_program_year_id
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    where s.agristability_scenario_id = in_scenario_id;

    update farms.farm_program_years
    set cash_margins_ind = in_cash_margins_ind,
        cash_margins_opt_in_date = in_cash_margins_opt_in_date,
        who_updated = in_user,
        when_updated = current_timestamp
    where program_year_id = v_program_year_id;

    call farms_calculator_pkg.sc_log(in_scenario_id,
                                     'Update Cash_Margins_Ind to ' || in_cash_margins_ind || ' Cash_Margins_Opt_In_Date ' || in_cash_margins_opt_in_date,
                                     in_user);

end;
$$;
