create or replace function farms_export_pkg.f09_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select null participant_oid,
               null stab_yy,
               null adj_info
        where in_program_year = -1;

    return cur;
end;
$$;
