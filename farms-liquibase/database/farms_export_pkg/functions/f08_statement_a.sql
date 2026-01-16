create or replace function farms_export_pkg.f08_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select li.program_year stab_yy,
               'BC' prov_code,
               li.line_item incm_expn_code,
               li.description incm_expn_desc,
               null incm_expn_french,
               li.eligibility_ind cais_elig_ind
        from farms.farm_line_items li
        where li.expiry_date > current_timestamp
        and li.line_item not in ('-1')
        and li.program_year = in_program_year
        order by li.line_item;

    return cur;
end;
$$;
