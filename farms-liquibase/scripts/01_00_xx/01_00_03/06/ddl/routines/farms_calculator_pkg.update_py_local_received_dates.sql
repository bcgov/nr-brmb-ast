create or replace procedure farms_calculator_pkg.update_py_local_received_dates(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_local_statement_a_received_date farms.farm_program_years.local_statement_a_receivd_date%type,
    in in_local_suppl_received_date farms.farm_program_years.local_supplemntl_received_date%type,
    in in_user farms.farm_program_years.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_program_years
    set local_supplemntl_received_date = in_local_suppl_received_date,
        local_statement_a_receivd_date = in_local_statement_a_received_date,
        when_updated = current_timestamp,
        who_updated = in_user
    where program_year_id = in_program_year_id;

    call farms_calculator_pkg.sc_log(in_agristability_scenario_id,
           'Local Supplemental and Statement A Received Dates updated',
           in_user);

end;
$$;
