create or replace function farms_webapp_pkg.get_clients(
    in in_guid farms.farm_agristability_represntves.user_guid%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select distinct ac.agristability_client_id,
               ac.participant_pin,
               per.person_id,
               per.address_line_1,
               per.address_line_2,
               per.city,
               per.corp_name,
               per.daytime_phone,
               per.evening_phone,
               per.fax_number,
               per.first_name,
               per.last_name,
               per.postal_code,
               per.province_state
        from farms.farm_agristability_represntves ar
        join farms.farm_client_subscriptions cs on cs.agristability_represntve_id = ar.agristability_represntve_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = cs.agristability_client_id
        join farms.farm_persons per on per.person_id = ac.person_id
        where ar.user_guid = in_guid
        and cs.subscription_status_code = 'ACT';
    return v_cursor;
end;
$$;
