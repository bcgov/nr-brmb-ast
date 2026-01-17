create or replace procedure farms_webapp_pkg.activate_subscription(
    inout io_num_updated bigint,
    in in_rep_id farms.farm_agristability_represntves.agristability_represntve_id%type,
    in in_sub_num farms.farm_client_subscriptions.subscription_number%type,
    in in_userid farms.farm_client_subscriptions.who_updated%type
)
language plpgsql
as
$$
begin
    update farms.farm_client_subscriptions
    set subscription_status_code = 'ACT',
        activated_date = current_timestamp,
        agristability_represntve_id = in_rep_id,
        who_updated = in_userid,
        when_updated = current_timestamp,
        revision_count = revision_count + farms_types_pkg.revision_count_increment()
    where subscription_number = in_sub_num
    and subscription_status_code = 'GEN';

    get diagnostics io_num_updated = row_count;
end;
$$;
