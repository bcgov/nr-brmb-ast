create or replace function farms_codes_read_pkg.read_triage_queue_codes(
    in in_code farms.farm_triage_queue_codes.triage_queue_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.triage_queue_code code,
               t.description,
               t.established_date,
               t.expiry_date,
               t.revision_count
        from farms.farm_triage_queue_codes t
        where (in_code is null or t.triage_queue_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
