create or replace procedure farms_codes_write_pkg.update_participant_prof_code(
   in in_participant_profile_code farms.farm_participant_profile_codes.participant_profile_code%type,
   in in_description farms.farm_participant_profile_codes.description%type,
   in in_effective_date farms.farm_participant_profile_codes.established_date%type,
   in in_expiry_date farms.farm_participant_profile_codes.expiry_date%type,
   in in_revision_count farms.farm_participant_profile_codes.revision_count%type,
   in in_user farms.farm_participant_profile_codes.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_participant_profile_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where participant_profile_code = in_participant_profile_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
