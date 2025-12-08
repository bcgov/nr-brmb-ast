create or replace function farms_webapp_pkg.get_authorized_users(
    in in_pin farms.farm_agristability_clients.participant_pin%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select subs.client_subscription_id,
               subs.activated_date,
               reps.agristability_represntve_id,
               reps.user_guid,
               reps.userid,
               subs.revision_count
        from farms.farm_agristability_clients cli
        join farms.farm_client_subscriptions subs on subs.agristability_client_id = cli.agristability_client_id
        join farms.farm_agristability_represntves reps on reps.agristability_represntve_id = subs.agristability_represntve_id
        where cli.participant_pin = in_pin
        and subs.subscription_status_code = 'ACT'
        order by subs.generated_date desc;
    return v_cursor;
end;
$$;
