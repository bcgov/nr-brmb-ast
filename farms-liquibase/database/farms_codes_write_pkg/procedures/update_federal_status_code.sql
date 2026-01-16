create or replace procedure farms_codes_write_pkg.update_federal_status_code(
   in in_federal_status_code farms.farm_federal_status_codes.federal_status_code%type,
   in in_description farms.farm_federal_status_codes.description%type,
   in in_effective_date farms.farm_federal_status_codes.established_date%type,
   in in_expiry_date farms.farm_federal_status_codes.expiry_date%type,
   in in_revision_count farms.farm_federal_status_codes.revision_count%type,
   in in_user farms.farm_federal_status_codes.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_federal_status_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        update_timestamp = current_timestamp
    where federal_status_code = in_federal_status_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
