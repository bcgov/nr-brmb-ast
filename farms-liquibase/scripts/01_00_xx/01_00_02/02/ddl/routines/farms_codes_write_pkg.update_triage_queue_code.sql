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
    update farms.farm_triage_queue_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        update_timestamp = current_timestamp
    where triage_queue_code = in_triage_queue_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
