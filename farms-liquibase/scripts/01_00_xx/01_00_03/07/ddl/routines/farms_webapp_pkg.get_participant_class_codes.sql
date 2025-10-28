create or replace function farms_webapp_pkg.get_participant_class_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select participant_class_code code,
               description
        from farms.farm_participant_class_codes c
        where current_timestamp between established_date and expiry_date
        and c.participant_class_code not in ('-1','2','4','5','6','7')
        order by description asc;
    return v_cursor;
end;
$$;
