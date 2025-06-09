create or replace procedure farms_codes_write_pkg.update_participant_prof_code(
   in in_participant_profile_code farms.participant_profile_code.participant_profile_code%type,
   in in_description farms.participant_profile_code.description%type,
   in in_effective_date farms.participant_profile_code.effective_date%type,
   in in_expiry_date farms.participant_profile_code.expiry_date%type,
   in in_revision_count farms.participant_profile_code.revision_count%type,
   in in_user farms.participant_profile_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.participant_profile_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.participant_profile_code = in_participant_profile_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
