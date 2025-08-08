create or replace function farms_codes_write_pkg.in_use_federal_accounting_code(
    in in_federal_accounting_code farms.farm_federal_accounting_codes.federal_accounting_code%type
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
            from farms.farm_farming_operations t
            where t.federal_accounting_code = in_federal_accounting_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
