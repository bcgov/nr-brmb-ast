create or replace function farms_codes_read_pkg.read_participant_lang_codes(
    in in_code farms.participant_language_code.participant_language_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.participant_language_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.participant_language_code t
        where (in_code is null or t.participant_language_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
