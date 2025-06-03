create or replace procedure farms_codes_write_pkg.update_structure_group_code(
   in in_structure_group_code farms.structure_group_code.structure_group_code%type,
   in in_description farms.structure_group_code.description%type,
   in in_rollup_structure_group_code farms.structure_group_attribute.rollup_structure_group_code%type,
   in in_effective_date farms.structure_group_code.effective_date%type,
   in in_expiry_date farms.structure_group_code.expiry_date%type,
   in in_revision_count farms.structure_group_code.revision_count%type,
   in in_user farms.structure_group_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.structure_group_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.structure_group_code = in_structure_group_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;

    update farms.structure_group_attribute a
    set a.rollup_structure_group_code = in_rollup_structure_group_code,
        a.revision_count = a.revision_count + 1,
        a.update_user = in_user,
        a.update_date = current_timestamp
    where a.structure_group_code = in_structure_group_code;

end;
$$;
