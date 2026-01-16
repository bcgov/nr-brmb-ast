create or replace function farms_search_pkg.search_accounts_by_pin(
    in in_pin bigint
)
returns refcursor
language plpgsql
as
$$
declare
    v_cursor refcursor;
begin
    open v_cursor for
        select ac.participant_pin pin,
               case when coalesce(per.corp_name::text, '') = '' then  per.last_name || ', ' || per.first_name  else per.corp_name end name,
               (
                   per.address_line_1 || ', ' ||
                   case when coalesce(per.address_line_2::text, '') = '' then  ''  else per.address_line_2 || ', ' end  ||
                   per.city || ', ' ||
                   per.province_state || ', ' ||
                   per.postal_code
                ) address
        from farms.farm_agristability_clients ac
        join farms.farm_persons per on per.person_id = ac.person_id
        where ac.participant_pin = in_pin
        order by pin;
    return v_cursor;
end;
$$;
