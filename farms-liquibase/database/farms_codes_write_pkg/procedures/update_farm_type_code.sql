create or replace procedure farms_codes_write_pkg.update_farm_type_code(
   in in_farm_type_code farms.farm_farm_type_codes.farm_type_code%type,
   in in_description farms.farm_farm_type_codes.description%type,
   in in_effective_date farms.farm_farm_type_codes.established_date%type,
   in in_expiry_date farms.farm_farm_type_codes.expiry_date%type,
   in in_revision_count farms.farm_farm_type_codes.revision_count%type,
   in in_user farms.farm_farm_type_codes.who_updated%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
begin
    update farms.farm_farm_type_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where farm_type_code = in_farm_type_code
    and revision_count = in_revision_count;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
