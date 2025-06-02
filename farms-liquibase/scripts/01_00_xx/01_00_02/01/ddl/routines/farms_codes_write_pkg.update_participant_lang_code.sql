create or replace procedure farms_codes_write_pkg.update_participant_lang_code(
   in in_participant_language_code farms.participant_language_code.participant_language_code%type,
   in in_description farms.participant_language_code.description%type,
   in in_effective_date farms.participant_language_code.effective_date%type,
   in in_expiry_date farms.participant_language_code.expiry_date%type,
   in in_revision_count farms.participant_language_code.revision_count%type,
   in in_user farms.participant_language_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.participant_language_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.participant_language_code = in_participant_language_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
