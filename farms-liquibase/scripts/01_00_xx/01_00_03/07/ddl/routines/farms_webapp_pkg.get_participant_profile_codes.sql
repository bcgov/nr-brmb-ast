create or replace function farms_webapp_pkg.get_participant_profile_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select participant_profile_code code,
               description
        from farms.farm_participant_profile_codes
        where current_timestamp between established_date and expiry_date
        order by description asc;
    return v_cursor;
end;
$$;
