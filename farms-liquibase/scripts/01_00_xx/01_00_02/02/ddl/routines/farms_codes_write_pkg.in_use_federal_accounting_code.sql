create or replace function farms_codes_write_pkg.in_use_federal_accounting_code(
    in in_federal_accounting_code farms.federal_accounting_code.federal_accounting_code%type
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
            from farms.farming_operation t
            where t.federal_accounting_code = in_federal_accounting_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
