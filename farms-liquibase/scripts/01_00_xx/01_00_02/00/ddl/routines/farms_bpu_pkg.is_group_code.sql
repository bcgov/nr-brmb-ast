create or replace function farms_bpu_pkg.is_group_code(
    in in_code varchar
)
returns boolean
language plpgsql
as
$$
declare
    v_num integer := 0;
begin
    select count(structure_group_code)
    into v_num
    from farms.structure_group_code
    where structure_group_code = in_code;

    return (v_num = 1);
end;
$$;
