create or replace procedure farms_calculator_pkg.delete_user_scenario(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
language plpgsql
as
$$
declare

    v_sc_id bigint;
    v_scenario_class_code farms.farm_agristability_scenarios.scenario_class_code%type;

    rsn_curs cursor for
        select rtr.reasonability_test_result_id
        from farms.farm_scenarios_vw sv
        join farms.farm_reasonabilty_test_results rtr on rtr.agristability_scenario_id = sv.agristability_scenario_id
        where sv.agristability_scenario_id = in_scenario_id;

    sc_curs cursor for
        select rs.agristability_scenario_id
        from farms.farm_reference_scenarios rs
        where rs.for_agristability_scenario_id = in_scenario_id
        union all
        select in_scenario_id agristability_scenario_id;

begin

    select sc.agristability_scenario_id,
           sc.scenario_class_code
    into v_sc_id,
         v_scenario_class_code
    from farms.farm_agristability_scenarios sc
    where sc.agristability_scenario_id = in_scenario_id;

    if v_scenario_class_code not in ('USER', 'TRIAGE') then
        raise exception '%', farms_types_pkg.user_scenario_only_msg()
        using errcode = farms_types_pkg.user_scenario_only_code()::text;
    end if;

    for rsn_rec in rsn_curs
    loop
        delete from farms.farm_rsn_bnft_rsk_prd_un_rslts t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_prdctn_forage_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_prdctn_frut_invntries t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_prdctn_frut_veg_rslts t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_prdctn_grain_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_result_messages t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_fruit_veg_invntrs t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_fruit_veg_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_g_f_fs_incm_rslts t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_g_f_fs_invntories t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_forage_consumers t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;

        delete from farms.farm_rsn_rev_hog_inventories t
        where t.rsn_rev_hog_result_id = (
            select t2.rsn_rev_hog_result_id
            from farms.farm_rsn_rev_hog_results t2
            where t2.reasonability_test_result_id = rsn_rec.reasonability_test_result_id
        );

        delete from farms.farm_rsn_rev_hog_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;

        delete from farms.farm_rsn_rev_nursery_incomes t
        where t.rsn_rev_nursery_result_id = (
            select t2.rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results t2
            where t2.reasonability_test_result_id = rsn_rec.reasonability_test_result_id
        );

        delete from farms.farm_rsn_rev_nursery_invntries t
        where t.rsn_rev_nursery_result_id = (
            select t2.rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results t2
            where t2.reasonability_test_result_id = rsn_rec.reasonability_test_result_id
        );

        delete from farms.farm_rsn_rev_nursery_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_poultry_brl_rslts t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_rsn_rev_poultry_egg_rslts t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;
        delete from farms.farm_reasonabilty_test_results t
        where t.reasonability_test_result_id = rsn_rec.reasonability_test_result_id;

    end loop;

    -- scenarios
    for sc_rec in sc_curs
    loop
        v_sc_id := sc_rec.agristability_scenario_id;

        delete from farms.farm_benefit_calc_margins t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_reported_inventories t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_productve_unit_capacities t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_reported_income_expenses t
        where t.agristability_scenario_id = v_sc_id;

        delete from farms.farm_scenario_logs t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_scenario_state_audits t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_agristability_claims t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_benefit_calc_totals t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_benefit_calc_documents t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_scenario_bpu_xref t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_scenario_enrolments t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_scenario_config_params t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_reference_scenarios t
        where t.agristability_scenario_id = v_sc_id;
        delete from farms.farm_agristability_scenarios t
        where t.agristability_scenario_id = v_sc_id;

    end loop;

end;
$$;
