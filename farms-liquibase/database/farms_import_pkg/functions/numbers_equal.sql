create or replace function farms_import_pkg.numbers_equal(
    in in_value_1 numeric,
    in in_value_2 numeric
)
returns boolean
language plpgsql
as $$
declare
    v_result boolean;
begin
    if in_value_1 is null and in_value_2 is null then
        v_result := true;
    else
        v_result := in_value_1 <> in_value_2;
    end if;

    return v_result;
end;
$$;
