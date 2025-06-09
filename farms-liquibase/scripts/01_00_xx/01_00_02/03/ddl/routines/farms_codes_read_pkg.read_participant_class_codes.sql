create or replace function farms_codes_read_pkg.read_participant_class_codes(
    in in_code farms.participant_class_code.participant_class_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.participant_class_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.participant_class_code t
        where (in_code is null or t.participant_class_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
