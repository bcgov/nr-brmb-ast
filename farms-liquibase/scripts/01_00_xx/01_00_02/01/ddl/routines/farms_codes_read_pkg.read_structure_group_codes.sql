create or replace function farms_codes_read_pkg.read_structure_group_codes(
    in in_code farms.structure_group_code.structure_group_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select c.structure_group_code code,
               c.description,
               a.rollup_structure_group_code,
               (
                   select description
                   from farms.structure_group_code
                   where structure_group_code = a.rollup_structure_group_code
                   limit 1
               ) rollup_structure_group_desc,
               c.effective_date,
               c.expiry_date,
               c.revision_count
        from farms.structure_group_code c
        left join farms.structure_group_attribute a on c.structure_group_code = a.structure_group_code
        where (in_code is null or c.structure_group_code = in_code)
        order by lower(c.description);
    return cur;
end;
$$;
