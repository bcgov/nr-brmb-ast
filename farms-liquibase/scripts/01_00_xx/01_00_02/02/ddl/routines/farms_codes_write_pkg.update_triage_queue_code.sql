create or replace procedure farms_codes_write_pkg.update_triage_queue_code(
   in in_triage_queue_code farms.farm_triage_queue_codes.triage_queue_code%type,
   in in_description farms.farm_triage_queue_codes.description%type,
   in in_effective_date farms.farm_triage_queue_codes.established_date%type,
   in in_expiry_date farms.farm_triage_queue_codes.expiry_date%type,
   in in_revision_count farms.farm_triage_queue_codes.revision_count%type,
   in in_user farms.farm_triage_queue_codes.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_triage_queue_codes c
    set c.description = in_description,
        c.established_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.who_updated = in_user,
        c.update_timestamp = current_timestamp
    where c.triage_queue_code = in_triage_queue_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
