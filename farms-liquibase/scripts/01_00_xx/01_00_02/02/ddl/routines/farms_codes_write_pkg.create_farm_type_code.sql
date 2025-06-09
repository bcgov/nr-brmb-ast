create or replace procedure farms_codes_write_pkg.create_farm_type_code(
   in in_farm_type_code farms.farm_type_code.farm_type_code%type,
   in in_description farms.farm_type_code.description%type,
   in in_effective_date farms.farm_type_code.effective_date%type,
   in in_expiry_date farms.farm_type_code.expiry_date%type,
   in in_user farms.farm_type_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.farm_type_code (
        farm_type_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        in_farm_type_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
