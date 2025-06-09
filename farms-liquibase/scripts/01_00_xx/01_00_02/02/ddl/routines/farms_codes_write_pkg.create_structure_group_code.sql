create or replace procedure farms_codes_write_pkg.create_structure_group_code(
   in in_structure_group_code farms.structure_group_code.structure_group_code%type,
   in in_description farms.structure_group_code.description%type,
   in in_rollup_structure_group_code farms.structure_group_attribute.rollup_structure_group_code%type,
   in in_effective_date farms.structure_group_code.effective_date%type,
   in in_expiry_date farms.structure_group_code.expiry_date%type,
   in in_user farms.structure_group_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.structure_group_code (
        structure_group_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        in_structure_group_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );

    insert into farms.structure_group_attribute (
        structure_group_attribute_id,
        structure_group_code,
        rollup_structure_group_code,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        nextval('farms.seq_sga'),
        in_structure_group_code,
        in_rollup_structure_group_code,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
