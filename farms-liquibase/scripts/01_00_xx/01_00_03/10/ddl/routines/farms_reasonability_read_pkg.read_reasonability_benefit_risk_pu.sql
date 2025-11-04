create or replace function farms_reasonability_read_pkg.read_reasonability_benefit_risk_pu(
    in in_reasonability_test_result_id farms.farm_rsn_bnft_rsk_prd_un_rslts.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rpu.reported_productive_capacity,
               rpu.consumed_productive_capacity,
               rpu.net_productive_capacity,
               rpu.bnft_rsk_bpu_calculated,
               rpu.bnft_rsk_estimated_income,
               rpu.inventory_item_code,
               iic.description inventory_item_description,
               rpu.structure_group_code,
               sgc.description structure_group_description,
               iid.commodity_type_code,
               ctc.description commodity_type_code_description
        from farms.farm_rsn_bnft_rsk_prd_un_rslts rpu
        left outer join farms.farm_structure_group_codes sgc on sgc.structure_group_code = rpu.structure_group_code
        left outer join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rpu.inventory_item_code
        left outer join farms.farm_inventory_item_details iid on iid.inventory_item_code = rpu.inventory_item_code
                                                              and iid.program_year = in_program_year
        left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = iid.commodity_type_code
        where rpu.reasonability_test_result_id = in_reasonability_test_result_id
        order by (coalesce(rpu.structure_group_code, rpu.inventory_item_code))::numeric;

    return cur;

end;
$$;
