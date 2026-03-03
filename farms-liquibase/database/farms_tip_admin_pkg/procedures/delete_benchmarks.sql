create or replace procedure farms_tip_admin_pkg.delete_benchmarks(
   in in_year farms.farm_program_years.year%type
)
language plpgsql
as $$
begin

    -- reference year expenses
    delete from farms.farm_tip_report_expenses tre
    where tre.tip_report_result_id in (
      select rtrr.tip_report_result_id
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    );

    -- program year expenses
    delete from farms.farm_tip_report_expenses tre
    where tre.tip_report_result_id in (
      select parent_trr.tip_report_result_id
      from farms.farm_tip_report_results parent_trr
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    );

    -- reference year results
    delete from farms.farm_tip_report_results trr
    where trr.tip_report_result_id in (
      select rtrr.tip_report_result_id
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
    );

    -- program year results
    delete from farms.farm_tip_report_results parent_trr
    where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
      and parent_trr.year = in_year;

    -- reference year and program year benchmark expenses
    delete from farms.farm_tip_benchmark_expenses tbe
    where tbe.tip_benchmark_year_id in (
      select tby.tip_benchmark_year_id
      from farms.farm_tip_benchmark_years tby
      where tby.program_year = in_year
    );

    -- reference year benchmark years
    delete from farms.farm_tip_benchmark_years tby
    where (tby.parent_tip_benchmark_year_id is not null and tby.parent_tip_benchmark_year_id::text <> '')
      and tby.program_year = in_year;

    -- program year benchmark years
    delete from farms.farm_tip_benchmark_years tby
    where tby.program_year = in_year;

end;
$$;
