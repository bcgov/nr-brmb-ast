create or replace procedure farms_codes_write_pkg.create_structure_group_code(
   in in_structure_group_code farms.farm_structure_group_codes.structure_group_code%type,
   in in_description farms.farm_structure_group_codes.description%type,
   in in_rollup_structure_group_code farms.farm_structure_group_attributs.rollup_structure_group_code%type,
   in in_effective_date farms.farm_structure_group_codes.established_date%type,
   in in_expiry_date farms.farm_structure_group_codes.expiry_date%type,
   in in_user farms.farm_structure_group_codes.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_structure_group_codes (
        structure_group_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
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

    insert into farms.farm_structure_group_attributs (
        structure_group_attrib_id,
        structure_group_code,
        rollup_structure_group_code,
        who_created,
        when_created,
        who_updated,
        when_updated,
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
