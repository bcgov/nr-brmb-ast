create or replace function farms_import_pkg.strings_are_different(
    in in_a varchar,
    in in_b varchar
)
returns boolean
language plpgsql
as $$
declare
    v_result boolean := true;
begin
    if in_a = in_b then
        v_result := false;
    elsif in_a is null and in_b is null then
        v_result := false;
    end if;

    return v_result;
end;
$$;
