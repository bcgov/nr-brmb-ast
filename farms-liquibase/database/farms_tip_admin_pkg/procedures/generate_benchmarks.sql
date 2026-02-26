create or replace procedure farms_tip_admin_pkg.generate_benchmarks(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    call farms_tip_admin_pkg.delete_benchmarks(in_year);

    call farms_tip_admin_pkg.program_year_report_results(in_year, in_user);
    call farms_tip_admin_pkg.reference_year_report_results(in_year, in_user);
    call farms_tip_admin_pkg.five_year_averages(in_year, in_user);

    call farms_tip_admin_pkg.benchmark_years(in_year, 'PROGRAM_YEAR', in_user);
    call farms_tip_admin_pkg.group_assignment(in_year, 'PROGRAM_YEAR', in_user);

    call farms_tip_admin_pkg.benchmark_years(in_year, 'REFERENCE_YEAR', in_user);
    call farms_tip_admin_pkg.group_assignment(in_year, 'REFERENCE_YEAR', in_user);

    call farms_tip_admin_pkg.benchmark_5year_averages(in_year, in_user);

    call farms_tip_admin_pkg.expenses(in_year, 'PROGRAM_YEAR', in_user);
    call farms_tip_admin_pkg.expenses(in_year, 'REFERENCE_YEAR', in_user);
    call farms_tip_admin_pkg.expenses_five_year_averages(in_year, in_user);
    call farms_tip_admin_pkg.expenses_benchmarks(in_year, in_user);
    call farms_tip_admin_pkg.expenses_benchmarks_ref_years(in_year, in_user);
    call farms_tip_admin_pkg.expenses_benchmarks_5yr_avg(in_year, in_user);
    call farms_tip_admin_pkg.copy_expenses_benchmarks(in_year, in_user);

end;
$$;
