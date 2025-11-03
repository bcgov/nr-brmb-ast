create or replace function farms_reasonability_read_pkg.read_reasonability_test_messages(
    in in_reasonability_test_id farms.farm_rsn_result_messages.reasonability_test_result_id%type,
    in in_test_name farms.farm_rsn_result_messages.reasonability_test_name%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrm.message,
               rrm.message_type_code
        from farms.farm_rsn_result_messages rrm
        where rrm.reasonability_test_result_id = in_reasonability_test_id
        and rrm.reasonability_test_name = in_test_name
        order by lower(rrm.message);

    return cur;

end;
$$;
