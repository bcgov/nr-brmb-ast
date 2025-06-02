create or replace function farms_codes_write_pkg.in_use_federal_status_code(
    in in_federal_status_code farms.federal_status_code.federal_status_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    select (case
        when exists(
            select null
            from farms.program_year_version t
            where t.federal_status_code = in_federal_status_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
