create or replace function farms_codes_read_pkg.read_participant_lang_codes(
    in in_code farms.farm_participant_lang_codes.participant_lang_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.participant_lang_code code,
               t.description,
               t.established_date,
               t.expiry_date,
               t.revision_count
        from farms.farm_participant_lang_codes t
        where (in_code is null or t.participant_lang_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
