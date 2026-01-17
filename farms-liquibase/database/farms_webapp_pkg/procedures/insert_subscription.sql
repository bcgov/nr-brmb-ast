create or replace procedure farms_webapp_pkg.insert_subscription(
    inout io_sub_id farms.farm_client_subscriptions.client_subscription_id%type,
    in in_client_id farms.farm_client_subscriptions.agristability_client_id%type,
    in in_sub_num farms.farm_client_subscriptions.subscription_number%type,
    in in_userid farms.farm_client_subscriptions.generated_by_userid%type,
    in in_expiry_date farms.farm_client_subscriptions.activation_expiry_date%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_client_subscriptions(
        client_subscription_id,
        subscription_number,
        generated_date,
        generated_by_userid,
        activation_expiry_date,
        agristability_client_id,
        subscription_status_code,
        revision_count,
        who_created,
        when_created
    ) values (
        nextval('farms.farm_csub_seq'),
        in_sub_num,
        current_timestamp,
        in_userid,
        in_expiry_date,
        in_client_id,
        'GEN',
        farms_types_pkg.revision_count_increment(),
        in_userid,
        current_timestamp
    )
    returning client_subscription_id into io_sub_id;

end;
$$;
