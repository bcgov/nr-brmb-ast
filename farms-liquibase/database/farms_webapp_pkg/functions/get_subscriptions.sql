create or replace function farms_webapp_pkg.get_subscriptions(
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
               subs.subscription_number,
               subs.generated_date,
               subs.generated_by_userid,
               subs.activated_date,
               subs.activation_expiry_date,
               subs.agristability_represntve_id,
               subs.agristability_client_id,
               subs.subscription_status_code,
               reps.userid activated_by_userid,
               ssc.description subscription_status_desc,
               subs.revision_count
        from farms.farm_agristability_clients cli
        join farms.farm_client_subscriptions subs on subs.agristability_client_id = cli.agristability_client_id
        left join farms.farm_agristability_represntves reps on reps.agristability_represntve_id = subs.agristability_represntve_id
        join farms.farm_subscription_status_codes ssc on ssc.subscription_status_code = subs.subscription_status_code
        where cli.participant_pin = in_pin
        order by subs.generated_date desc;
    return v_cursor;
end;
$$;
