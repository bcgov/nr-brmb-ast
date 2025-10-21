create or replace function farms_search_pkg.search_accounts(
    in in_pin farms.farm_agristability_clients.participant_pin%type,
	in in_name farms.farm_persons.last_name%type,
	in in_city farms.farm_persons.city%type,
	in in_postal_code farms.farm_persons.postal_code%type,
	in in_user_id farms.farm_agristability_represntves.userid%type
) returns refcursor
language plpgsql
as
$$
declare
    v_cursor refcursor;
begin
    if (in_pin is not null and in_pin::text <> '') then
        v_cursor := farm_search_pkg_search_accounts_by_pin(in_pin);
    elsif (in_user_id is not null and in_user_id::text <> '') then
        v_cursor := farm_search_pkg_search_accounts_by_rep(in_user_id);
    else
        v_cursor := farm_search_pkg_search_accounts_by_name(in_name, in_city, in_postal_code);
    end if;

    return v_cursor;
end;
$$;
