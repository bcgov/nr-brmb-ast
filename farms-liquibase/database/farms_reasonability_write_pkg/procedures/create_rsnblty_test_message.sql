create or replace procedure farms_reasonability_write_pkg.create_rsnblty_test_message(
    in in_test_name farms.farm_rsn_result_messages.reasonability_test_name%type,
    in in_message farms.farm_rsn_result_messages.message%type,
    in in_message_type_code farms.farm_rsn_result_messages.message_type_code%type,
    in in_reasonability_test_reslt_id farms.farm_rsn_result_messages.reasonability_test_result_id%type,
    in in_user farms.farm_rsn_result_messages.who_created%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_rsn_result_messages (
        rsn_result_message_id,
        reasonability_test_name,
        message,
        message_type_code,
        reasonability_test_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrm_seq'),
        in_test_name,
        in_message,
        in_message_type_code,
        in_reasonability_test_reslt_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
