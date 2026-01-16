create or replace procedure farms_reasonability_write_pkg.flag_reasonability_tests_stale(
    in in_reasonability_test_reslt_id farms.farm_reasonabilty_test_results.reasonability_test_result_id%type,
    in_user farms.farm_reasonabilty_test_results.who_updated%type
)
language plpgsql
as
$$
begin

    if (in_reasonability_test_reslt_id is not null and in_reasonability_test_reslt_id::text <> '') then
        update farms.farm_reasonabilty_test_results
        set fresh_ind = 'N',
            revision_count = revision_count + 1,
            who_updated = in_user,
            when_updated = current_timestamp
        where reasonability_test_result_id = in_reasonability_test_reslt_id;
    end if;

end;
$$;
