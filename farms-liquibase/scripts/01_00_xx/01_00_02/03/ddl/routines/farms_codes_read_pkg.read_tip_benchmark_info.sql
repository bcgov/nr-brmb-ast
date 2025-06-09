create or replace function farms_codes_read_pkg.read_tip_benchmark_info()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select trr.year,
               count(*) operation_count,
               max(trr.generated_date) generated_date
        from farms.tip_report_result trr
        where trr.parent_tip_report_result_id is null
        group by trr.year
        order by trr.year desc;
    return cur;

end;
$$;
