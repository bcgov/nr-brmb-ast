create or replace function farms_codes_write_pkg.is_structure_group_code(
    in in_code varchar
)
returns boolean
language plpgsql
as $$
declare
    v_count numeric := 0;
begin
    select count(structure_group_code)
    into v_count
    from farms.structure_group_code
    where structure_group_code = in_code;

    return (v_count = 1);
end;
$$;
