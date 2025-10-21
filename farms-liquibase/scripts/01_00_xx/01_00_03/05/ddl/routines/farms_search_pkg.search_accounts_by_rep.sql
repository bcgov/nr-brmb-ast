create or replace function farms_search_pkg.search_accounts_by_rep(
    in in_user_id farms.farm_agristability_represntves.userid%type
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
               case when coalesce(per.corp_name::text, '') = '' then  per.last_name || ', ' || per.first_name  else per.corp_name END  name,
               (
                   per.address_line_1 || ', ' ||
                   case when coalesce(per.address_line_2::text, '') = '' then  ''  else per.address_line_2 || ', ' end  ||
                   per.city || ', ' ||
                   per.province_state || ', ' ||
                   per.postal_code
               ) address
        from farms.farm_agristability_clients ac
        join farms.farm_persons per on per.person_id = ac.person_id
        join farms.farm_client_subscriptions subs on subs.agristability_client_id = ac.agristability_client_id
        join farms.farm_agristability_represntves reps on reps.agristability_represntve_id = subs.agristability_represntve_id
        where reps.userid = in_user_id
        and subs.subscription_status_code = 'ACT'
        order by name;
    return v_cursor;
  end;
$$;
