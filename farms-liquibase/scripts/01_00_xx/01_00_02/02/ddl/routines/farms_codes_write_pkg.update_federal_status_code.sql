create or replace procedure farms_codes_write_pkg.update_federal_status_code(
   in in_federal_status_code farms.federal_status_code.federal_status_code%type,
   in in_description farms.federal_status_code.description%type,
   in in_effective_date farms.federal_status_code.effective_date%type,
   in in_expiry_date farms.federal_status_code.expiry_date%type,
   in in_revision_count farms.federal_status_code.revision_count%type,
   in in_user farms.federal_status_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.federal_status_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_timestamp = current_timestamp
    where c.federal_status_code = in_federal_status_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
