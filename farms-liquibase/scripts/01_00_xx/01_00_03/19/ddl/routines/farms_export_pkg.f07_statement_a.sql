create or replace function farms_export_pkg.f07_statement_a(
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
               null cntct_num,
               null rct_cntct_business_name,
               null cntct_line1_addr,
               null cntct_line2_addr,
               null cntct_city,
               null cntct_prov,
               null cntct_pstl_code,
               null cntct_day_tel_num,
               null cntct_fax_num,
               null rct_cntct_first_name,
               null rct_cntct_last_name
        where in_program_year = -1;

    return cur;
end;
$$;
