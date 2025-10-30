create or replace procedure farms_webapp_pkg.update_subscription_status(
    in in_sub_id farms.farm_client_subscriptions.client_subscription_id%type,
    in in_status farms.farm_client_subscriptions.subscription_status_code%type,
    in in_revision_count farms.farm_client_subscriptions.revision_count%type,
    in in_userid farms.farm_client_subscriptions.who_updated%type
)
language plpgsql
as
$$
declare

    v_num_rows_affected bigint;

begin
    update farms.farm_client_subscriptions
    set subscription_status_code = in_status,
        who_updated = in_userid,
        when_updated = current_timestamp,
        revision_count = revision_count + farms_types_pkg.revision_count_increment()
    where client_subscription_id = in_sub_id
    and revision_count = in_revision_count;

    get diagnostics v_num_rows_affected = row_count;

    if v_num_rows_affected = 0 then
        raise exception '%', farms_types_pkg.data_not_current_msg()
        using errcode = farms_types_pkg.data_not_current_code()::text;
    end if;
end;
$$;
