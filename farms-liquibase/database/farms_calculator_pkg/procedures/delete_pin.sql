create or replace procedure farms_calculator_pkg.delete_pin(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type
)
language plpgsql
as
$$
declare

    v_client_id bigint;
    v_owner_id bigint;
    v_contact_id bigint;
    v_pyv_id bigint;
    v_import_id bigint;
    v_op_id bigint;
    v_sc_id bigint;
    v_count bigint;
   
    rsn_curs cursor for
        select rtr.reasonability_test_result_id
        from farms.farm_scenarios_vw sv
        join farms.farm_reasonabilty_test_results rtr on rtr.agristability_scenario_id = sv.agristability_scenario_id
        where sv.participant_pin = in_participant_pin;

    pyv_curs cursor for
        select pyv.program_year_version_id, pyv.import_version_id
        from farms.farm_program_years py
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        where py.agristability_client_id = v_client_id;

    op_curs cursor (p_pyv_id bigint) for
        select fo.farming_operation_id
        from farms.farm_farming_operations fo
        where fo.program_year_version_id = p_pyv_id;

    sc_curs cursor (p_pyv_id bigint) for
        select sc.agristability_scenario_id
        from farms.farm_agristability_scenarios sc
        where sc.program_year_version_id = p_pyv_id;

begin
    
    select count(*)
    into v_count
    from farms.farm_agristability_clients ac
    where ac.participant_pin = in_participant_pin;
  
    if v_count = 0 then
        return;
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

    ----------------- begin tips ------------------------------------------

    -- reference year expenses
    delete from farms.farm_tip_report_expenses tre
    where tre.tip_report_result_id in (
        select rtrr.tip_report_result_id
        from farms.farm_tip_report_results parent_trr
        join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
        where parent_trr.participant_pin = in_participant_pin
        and parent_trr.parent_tip_report_result_id is null
    );

    -- program year expenses
    delete from farms.farm_tip_report_expenses tre
    where tre.tip_report_result_id in (
        select parent_trr.tip_report_result_id
        from farms.farm_tip_report_results parent_trr
        where parent_trr.participant_pin = in_participant_pin
        and parent_trr.parent_tip_report_result_id is null
    );

    -- reference year results
    delete from farms.farm_tip_report_results trr
    where trr.tip_report_result_id in (
        select rtrr.tip_report_result_id
        from farms.farm_tip_report_results parent_trr
        join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
        where parent_trr.participant_pin = in_participant_pin
        and parent_trr.parent_tip_report_result_id is null
    );

    -- program year results
    delete from farms.farm_tip_report_results parent_trr
    where parent_trr.parent_tip_report_result_id is null
    and parent_trr.participant_pin = in_participant_pin;

    ----------------- end tips ------------------------------------------

    select ac.agristability_client_id, ac.person_id, ac.person_id_client_contacted_by
    into   v_client_id, v_owner_id, v_contact_id
    from farms.farm_agristability_clients ac
    where ac.participant_pin = in_participant_pin;
    
    delete from farms.farm_program_enrolments t
    where t.agristability_client_id = v_client_id;
    delete from farms.farm_client_subscriptions t
    where t.agristability_client_id = v_client_id;
    
    -- program year versions
    for pyv_rec in pyv_curs
    loop
        v_pyv_id := pyv_rec.program_year_version_id;
        v_import_id := pyv_rec.import_version_id;
      
        delete from farms.farm_whole_farm_participants wfp
        where wfp.program_year_version_id = v_pyv_id;
      
        -- farming operations
        for op_rec in op_curs(v_pyv_id)
        loop
            v_op_id := op_rec.farming_operation_id;

            delete from farms.farm_benefit_calc_margins t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_farming_operatin_prtnrs t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_production_insurances t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_reported_inventories t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_productve_unit_capacities t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_reported_income_expenses t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_tip_report_documents t
            where t.farming_operation_id = v_op_id;
            delete from farms.farm_negative_margins t
            where t.farming_operation_id = v_op_id;
        
        end loop;
      
        delete from farms.farm_farming_operations fo
        where fo.program_year_version_id = v_pyv_id;
      
        -- scenarios
        for sc_rec in sc_curs(v_pyv_id)
        loop
            v_sc_id := sc_rec.agristability_scenario_id;

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
            where t.agristability_scenario_id = v_sc_id
            or t.for_agristability_scenario_id = v_sc_id;
            
        end loop;
      
        delete from farms.farm_agristability_scenarios t
        where t.program_year_version_id = v_pyv_id;

    end loop;
    
    delete from farms.farm_program_year_versions pyv
    where pyv.program_year_id in (
        select py.program_year_id
        from farms.farm_program_years py
        where py.agristability_client_id = v_client_id
    );
    
    delete from farms.farm_program_years py
    where py.agristability_client_id = v_client_id;

    delete from farms.farm_agristability_clients ac
    where ac.agristability_client_id = v_client_id;

    delete from farms.farm_persons t
    where t.person_id in (v_owner_id, v_contact_id);

end;
$$;
