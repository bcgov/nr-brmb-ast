create or replace function farms_export_pkg.f04(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select program_year,
               province,
               line_item,
               description,
               eligibility_ind
        from farms.farm_line_items li
        where (li.expiry_date > current_timestamp or coalesce(li.expiry_date::text, '') = '')
        and li.program_year = in_program_year;

    return cur;
end;
$$;
