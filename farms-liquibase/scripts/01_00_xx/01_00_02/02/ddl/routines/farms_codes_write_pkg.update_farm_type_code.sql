create or replace procedure farms_codes_write_pkg.update_farm_type_code(
   in in_farm_type_code farms.farm_type_code.farm_type_code%type,
   in in_description farms.farm_type_code.description%type,
   in in_effective_date farms.farm_type_code.effective_date%type,
   in in_expiry_date farms.farm_type_code.expiry_date%type,
   in in_revision_count farms.farm_type_code.revision_count%type,
   in in_user farms.farm_type_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.farm_type_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_timestamp = current_timestamp
    where c.farm_type_code = in_farm_type_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
