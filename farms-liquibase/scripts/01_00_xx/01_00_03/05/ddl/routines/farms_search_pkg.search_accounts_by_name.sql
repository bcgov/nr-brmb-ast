create or replace function farms_search_pkg.search_accounts_by_name(
    in in_name farms.farm_persons.last_name%type,
	in in_city farms.farm_persons.city%type,
	in in_postal_code farms.farm_persons.postal_code%type
) returns refcursor
language plpgsql
as
$$
declare
    v_cursor refcursor;
    v_name varchar(100) := upper(in_name) || '%';
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
        where (upper(per.last_name) like v_name or upper(per.corp_name) like v_name)
        and (coalesce(in_city::text, '') = '' or upper(in_city) = upper(per.city))
        and (coalesce(in_postal_code::text, '') = '' or upper(in_postal_code) = upper(per.postal_code)) 
        order by name;
    return v_cursor;
end;
$$;
