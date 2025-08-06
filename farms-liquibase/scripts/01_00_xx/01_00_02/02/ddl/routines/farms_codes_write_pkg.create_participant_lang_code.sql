create or replace procedure farms_codes_write_pkg.create_participant_lang_code(
   in in_participant_language_code farms.farm_participant_lang_codes.participant_lang_code%type,
   in in_description farms.farm_participant_lang_codes.description%type,
   in in_effective_date farms.farm_participant_lang_codes.established_date%type,
   in in_expiry_date farms.farm_participant_lang_codes.expiry_date%type,
   in in_user farms.farm_participant_lang_codes.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_participant_lang_codes (
        participant_lang_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        in_participant_language_code,
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
