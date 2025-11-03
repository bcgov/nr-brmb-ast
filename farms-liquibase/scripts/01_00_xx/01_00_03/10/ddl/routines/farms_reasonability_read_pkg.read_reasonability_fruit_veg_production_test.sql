create or replace function farms_reasonability_read_pkg.read_reasonability_fruit_veg_production_test(
    in in_reasonability_test_id farms.farm_rsn_prdctn_frut_veg_rslts.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rpfvr.productive_capacity_amount,
               rpfvr.reported_quantity_produced,
               rpfvr.expected_quantity_produced,
               rpfvr.quantity_produced_variance,
               rpfvr.quantity_produced_wthn_lmt_ind,
               rpfvr.fruit_veg_type_code,
               fvtc.description fruit_veg_type_code_description
        from farms.farm_rsn_prdctn_frut_veg_rslts rpfvr
        join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = rpfvr.fruit_veg_type_code
        where rpfvr.reasonability_test_result_id = in_reasonability_test_id
        order by rpfvr.fruit_veg_type_code;

    return cur;

end;
$$;
