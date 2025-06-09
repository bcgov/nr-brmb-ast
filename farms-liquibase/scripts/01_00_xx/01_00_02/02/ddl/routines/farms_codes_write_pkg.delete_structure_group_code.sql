create or replace procedure farms_codes_write_pkg.delete_structure_group_code(
   in in_structure_group_code farms.structure_group_code.structure_group_code%type,
   in in_revision_count farms.structure_group_code.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_structure_group_code(in_structure_group_code);

    if v_in_use = 0 then

        delete from farms.structure_group_attribute a
        where a.structure_group_code = in_structure_group_code;

        delete from farms.structure_group_code c
        where c.structure_group_code = in_structure_group_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
