create or replace procedure farms_reasonability_write_pkg.create_farm_rsnblty_bnft_rsk_pu(
    in in_reasonability_test_reslt_id farms.farm_rsn_bnft_rsk_prd_un_rslts.reasonability_test_result_id%type,
    in in_reported_productive_capacity farms.farm_rsn_bnft_rsk_prd_un_rslts.reported_productive_capacity%type,
    in in_consumed_productive_capacity farms.farm_rsn_bnft_rsk_prd_un_rslts.consumed_productive_capacity%type,
    in in_net_productive_capacity farms.farm_rsn_bnft_rsk_prd_un_rslts.net_productive_capacity%type,
    in in_bnft_rsk_bpu_calculated farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_bpu_calculated%type,
    in in_bnft_rsk_estimated_income farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_estimated_income%type,
    in in_inventory_item_code farms.farm_rsn_bnft_rsk_prd_un_rslts.inventory_item_code%type,
    in in_structure_group_code farms.farm_rsn_bnft_rsk_prd_un_rslts.structure_group_code%type,
    in in_user farms.farm_rsn_bnft_rsk_prd_un_rslts.who_updated%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_bnft_rsk_prd_un_rslts (
        rsn_bnft_rsk_prd_un_rslts_id,
        reported_productive_capacity,
        consumed_productive_capacity,
        net_productive_capacity,
        bnft_rsk_bpu_calculated,
        bnft_rsk_estimated_income,
        inventory_item_code,
        structure_group_code,
        reasonability_test_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rbrpu_seq'),
        in_reported_productive_capacity,
        in_consumed_productive_capacity,
        in_net_productive_capacity,
        in_bnft_rsk_bpu_calculated,
        in_bnft_rsk_estimated_income,
        in_inventory_item_code,
        in_structure_group_code,
        in_reasonability_test_reslt_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
