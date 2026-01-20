create or replace procedure farms_reasonability_write_pkg.delete_reasonability_results(
    in in_reasonability_test_reslt_id farms.farm_reasonabilty_test_results.reasonability_test_result_id%type
)
language plpgsql
as
$$
begin

    -- delete error/warning messages
    delete from farms.farm_rsn_result_messages
    where reasonability_test_result_id = in_reasonability_test_reslt_id;

    -- delete forage consumers test results
    delete from farms.farm_rsn_forage_consumers
    where reasonability_test_result_id = in_reasonability_test_reslt_id;

    -- delete benefit risk test results
    delete from farms.farm_rsn_bnft_rsk_prd_un_rslts
    where reasonability_test_result_id = in_reasonability_test_reslt_id;

    -- delete production test results
    begin
        delete from farms.farm_rsn_prdctn_frut_invntries
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_prdctn_frut_veg_rslts
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_prdctn_grain_results
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_prdctn_forage_results
        where reasonability_test_result_id = in_reasonability_test_reslt_id;
    end;


    -- delete revenue test results
    begin
        delete from farms.farm_rsn_rev_g_f_fs_incm_rslts
        where reasonability_test_result_id = in_reasonability_test_reslt_id;
        delete from farms.farm_rsn_rev_g_f_fs_invntories
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_rev_fruit_veg_invntrs
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_rev_fruit_veg_results
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_rev_nursery_invntries
        where rsn_rev_nursery_result_id = (
            select rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        );
        delete from farms.farm_rsn_rev_nursery_incomes
        where rsn_rev_nursery_result_id = (
            select rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        );
        delete from farms.farm_rsn_rev_nursery_results
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_rev_poultry_brl_rslts
        where reasonability_test_result_id = in_reasonability_test_reslt_id;
        delete from farms.farm_rsn_rev_poultry_egg_rslts
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

        delete from farms.farm_rsn_rev_hog_inventories
        where rsn_rev_hog_result_id = (
            select rsn_rev_hog_result_id
            from farms.farm_rsn_rev_hog_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        );
        delete from farms.farm_rsn_rev_hog_results
        where reasonability_test_result_id = in_reasonability_test_reslt_id;
    end;
end;
$$;
