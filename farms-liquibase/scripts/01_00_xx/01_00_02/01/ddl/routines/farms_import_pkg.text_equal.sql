create or replace function farms_import_pkg.text_equal(
    in in_value_1 varchar,
    in in_value_2 varchar
)
returns boolean
language plpgsql
as $$
declare
    v_result boolean;
begin
    if in_value_1 is null and in_value_2 is null then
        v_result := true;
    elsif in_value_1 is null or in_value_2 is null then
        v_result := false;
    else
        v_result := in_value_1 <> in_value_2;
    end if;

    return v_result;
end;
$$;
