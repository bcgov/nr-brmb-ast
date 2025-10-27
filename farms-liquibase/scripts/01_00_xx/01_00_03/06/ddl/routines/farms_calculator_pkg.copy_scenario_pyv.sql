create or replace function farms_calculator_pkg.copy_scenario_pyv(
    in in_old_sc_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_user farms.farm_program_year_versions.who_created%type
) returns farms.farm_program_year_versions.program_year_version_number%type
language plpgsql
as
$$
declare

    v_old_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    v_old_pyv_num farms.farm_program_year_versions.program_year_version_number%type;
    v_old_sc_num farms.farm_agristability_scenarios.scenario_number%type;
    v_old_scenario_type farms.farm_agristability_scenarios.scenario_class_code%type;
    v_py_id farms.farm_program_years.program_year_id%type;
    v_new_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    v_new_sc_id farms.farm_agristability_scenarios.agristability_scenario_id%type;
    v_new_sc_num farms.farm_agristability_scenarios.scenario_number%type;
    v_new_pyv_num farms.farm_program_year_versions.program_year_version_number%type;

begin

    select nextval('farms.farm_pyv_seq'),
           nextval('farms.farm_as_seq')
    into v_new_pyv_id,
         v_new_sc_id;

    select sv.program_year_version_id,
           sv.program_year_version_number,
           sv.program_year_id
    into v_old_pyv_id,
         v_old_pyv_num,
         v_py_id
    from farms.farm_scenarios_vw sv
    where sv.agristability_scenario_id = in_old_sc_id;

    select sv.scenario_number,
           sv.scenario_class_code
    into v_old_sc_num,
         v_old_scenario_type
    from farms.farm_scenarios_vw sv
    where sv.program_year_version_id = v_old_pyv_id
    and sv.scenario_number = (
        select min(sv2.scenario_number)
        from farms.farm_scenarios_vw sv2
        where sv2.program_year_version_id = v_old_pyv_id
    );

    select max(sv.scenario_number) + 1,
           max(sv.program_year_version_number) + 1
    into v_new_sc_num,
         v_new_pyv_num
    from farms.farm_scenarios_vw sv
    where sv.program_year_id = v_py_id;

    insert into farms.farm_program_year_versions (
        program_year_version_id,
        program_year_version_number,
        form_version_number,
        form_version_effective_date,
        common_share_total,
        farm_years,
        accrual_worksheet_ind,
        completed_prod_cycle_ind,
        cwb_worksheet_ind,
        perishable_commodities_ind,
        receipts_ind,
        accrual_cash_conversion_ind,
        combined_farm_ind,
        coop_member_ind,
        corporate_shareholder_ind,
        disaster_ind,
        partnership_member_ind,
        sole_proprietor_ind,
        other_text,
        post_mark_date,
        province_of_residence,
        received_date,
        last_year_farming_ind,
        can_send_cob_to_rep_ind,
        province_of_main_farmstead,
        locally_updated_ind,
        program_year_id,
        participant_profile_code,
        municipality_code,
        import_version_id,
        federal_status_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select v_new_pyv_id program_year_version_id,
           v_new_pyv_num,
           form_version_number,
           form_version_effective_date,
           common_share_total,
           farm_years,
           accrual_worksheet_ind,
           completed_prod_cycle_ind,
           cwb_worksheet_ind,
           perishable_commodities_ind,
           receipts_ind,
           accrual_cash_conversion_ind,
           combined_farm_ind,
           coop_member_ind,
           corporate_shareholder_ind,
           disaster_ind,
           partnership_member_ind,
           sole_proprietor_ind,
           other_text,
           post_mark_date,
           province_of_residence,
           received_date,
           last_year_farming_ind,
           can_send_cob_to_rep_ind,
           province_of_main_farmstead,
           locally_updated_ind,
           program_year_id,
           participant_profile_code,
           municipality_code,
           null import_version_id,
           federal_status_code,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_program_year_versions old_pyv
    where old_pyv.program_year_version_id = v_old_pyv_id;


    insert into farms.farm_farming_operations (
        farming_operation_id,
        business_use_home_expense,
        expenses,
        fiscal_year_end,
        fiscal_year_start,
        gross_income,
        inventory_adjustments,
        crop_share_ind,
        feeder_member_ind,
        landlord_ind,
        net_farm_income,
        net_income_after_adj,
        net_income_before_adj,
        other_deductions,
        partnership_name,
        partnership_percent,
        partnership_pin,
        operation_number,
        crop_disaster_ind,
        livestock_disaster_ind,
        locally_updated_ind,
        locally_generated_ind,
        alignment_key,
        federal_accounting_code,
        program_year_version_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_fo_seq') farming_operation_id,
           business_use_home_expense,
           expenses,
           fiscal_year_end,
           fiscal_year_start,
           gross_income,
           inventory_adjustments,
           crop_share_ind,
           feeder_member_ind,
           landlord_ind,
           net_farm_income,
           net_income_after_adj,
           net_income_before_adj,
           other_deductions,
           partnership_name,
           partnership_percent,
           partnership_pin,
           operation_number,
           crop_disaster_ind,
           livestock_disaster_ind,
           locally_updated_ind,
           locally_generated_ind,
           alignment_key,
           federal_accounting_code,
           v_new_pyv_id program_year_version_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    where old_fo.program_year_version_id = v_old_pyv_id;


    insert into farms.farm_farming_operatin_prtnrs(
        farming_operatin_prtnr_id,
        partner_percent,
        partnership_pin,
        partner_sin,
        first_name,
        last_name,
        corp_name,
        farming_operation_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_fop_seq') farming_operatin_prtnr_id,
           old_fop.partner_percent,
           old_fop.partnership_pin,
           old_fop.partner_sin,
           old_fop.first_name,
           old_fop.last_name,
           old_fop.corp_name,
           new_fo.farming_operation_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    join farms.farm_farming_operatin_prtnrs old_fop on old_fop.farming_operation_id = old_fo.farming_operation_id
    join farms.farm_farming_operations new_fo on new_fo.alignment_key = old_fo.alignment_key
                                              and new_fo.program_year_version_id = v_new_pyv_id
    where old_fo.program_year_version_id = v_old_pyv_id;


    insert into farms.farm_production_insurances (
        production_insurance_id,
        production_insurance_number,
        locally_updated_ind,
        farming_operation_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_pi_seq') production_insurance_id,
           old_pi.production_insurance_number,
           old_pi.locally_updated_ind,
           new_fo.farming_operation_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    join farms.farm_production_insurances old_pi on old_pi.farming_operation_id = old_fo.farming_operation_id
    join farms.farm_farming_operations new_fo on new_fo.alignment_key = old_fo.alignment_key
                                              and new_fo.program_year_version_id = v_new_pyv_id
    where old_fo.program_year_version_id = v_old_pyv_id;


    insert into farms.farm_reported_income_expenses (
        reported_income_expense_id,
        amount,
        expense_ind,
        line_item,
        import_comment,
        farming_operation_id,
        agristability_scenario_id,
        cra_reported_income_expense_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_rie_seq') reported_income_expense_id,
           amount,
           expense_ind,
           line_item,
           import_comment,
           new_fo.farming_operation_id,
           agristability_scenario_id,
           cra_reported_income_expense_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    join farms.farm_reported_income_expenses old_rie on old_rie.farming_operation_id = old_fo.farming_operation_id
    join farms.farm_farming_operations new_fo on new_fo.alignment_key = old_fo.alignment_key
                                              and new_fo.program_year_version_id = v_new_pyv_id
    where old_fo.program_year_version_id = v_old_pyv_id
    and coalesce(old_rie.agristability_scenario_id::text, '') = ''
    and coalesce(old_rie.cra_reported_income_expense_id::text, '') = '';


    insert into farms.farm_reported_inventories (
        reported_inventory_id,
        price_start,
        price_end,
        end_year_producer_price,
        start_of_year_amount,
        end_of_year_amount,
        quantity_start,
        quantity_end,
        quantity_produced,
        accept_producer_price_ind,
        aarm_reference_p1_price,
        aarm_reference_p2_price,
        on_farm_acres,
        unseedable_acres,
        import_comment,
        crop_unit_code,
        agristabilty_cmmdty_xref_id,
        farming_operation_id,
        agristability_scenario_id,
        cra_reported_inventory_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_ri_seq') reported_inventory_id,
           price_start,
           price_end,
           end_year_producer_price,
           start_of_year_amount,
           end_of_year_amount,
           quantity_start,
           quantity_end,
           quantity_produced,
           accept_producer_price_ind,
           aarm_reference_p1_price,
           aarm_reference_p2_price,
           on_farm_acres,
           unseedable_acres,
           import_comment,
           crop_unit_code,
           agristabilty_cmmdty_xref_id,
           new_fo.farming_operation_id,
           agristability_scenario_id,
           cra_reported_inventory_id,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    join farms.farm_reported_inventories old_ri on old_ri.farming_operation_id = old_fo.farming_operation_id
    join farms.farm_farming_operations new_fo on new_fo.alignment_key = old_fo.alignment_key
                                              and new_fo.program_year_version_id = v_new_pyv_id
    where old_fo.program_year_version_id = v_old_pyv_id
    and coalesce(old_ri.agristability_scenario_id::text, '') = ''
    and coalesce(old_ri.cra_reported_inventory_id::text, '') = '';


    insert into farms.farm_productve_unit_capacities (
        productve_unit_capacity_id,
        productive_capacity_amount,
        import_comment,
        inventory_item_code,
        structure_group_code,
        farming_operation_id,
        agristability_scenario_id,
        cra_productve_unit_capacity_id,
        participnt_data_src_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_puc_seq') productve_unit_capacity_id,
           old_puc.productive_capacity_amount,
           old_puc.import_comment,
           old_puc.inventory_item_code,
           old_puc.structure_group_code,
           new_fo.farming_operation_id,
           old_puc.agristability_scenario_id,
           old_puc.cra_productve_unit_capacity_id,
           old_puc.participnt_data_src_code,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_farming_operations old_fo
    join farms.farm_productve_unit_capacities old_puc on old_puc.farming_operation_id = old_fo.farming_operation_id
    join farms.farm_farming_operations new_fo on new_fo.alignment_key = old_fo.alignment_key
                                              and new_fo.program_year_version_id = v_new_pyv_id
    where old_fo.program_year_version_id = v_old_pyv_id
    and coalesce(old_puc.agristability_scenario_id::text, '') = ''
    and coalesce(old_puc.cra_productve_unit_capacity_id::text, '') = '';


    insert into farms.farm_agristability_scenarios (
        agristability_scenario_id,
        scenario_number,
        benefits_calculator_version,
        scenario_created_by,
        default_ind,
        scenario_creation_date,
        description,
        combined_farm_number,
        cra_income_expns_received_date,
        cra_supplemental_received_date,
        program_year_version_id,
        scenario_class_code,
        scenario_state_code,
        scenario_category_code,
        triage_queue_code,
        participnt_data_src_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select v_new_sc_id agristability_scenario_id,
           v_new_sc_num scenario_number,
           null benefits_calculator_version,
           in_user scenario_created_by,
           'N' default_ind,
           clock_timestamp() scenario_creation_date,
           null description,
           null combined_farm_number,
           old_sc.cra_income_expns_received_date,
           old_sc.cra_supplemental_received_date,
           v_new_pyv_id program_year_version_id,
           'LOCAL' scenario_class_code,
           'REC' scenario_state_code,
           'LOCL_ENTRY' scenario_category_code,
           null triage_queue_code,
           participnt_data_src_code,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated
    from farms.farm_agristability_scenarios old_sc
    where old_sc.agristability_scenario_id = in_old_sc_id;

    call farms_calculator_pkg.sc_log(v_new_sc_id, 'Created Program Year Version ' || v_new_pyv_num || ' from version ' || v_old_pyv_num, in_user);
    call farms_calculator_pkg.sc_log(v_new_sc_id, 'Created from ' || v_old_scenario_type || ' scenario number ' || v_old_sc_num, in_user);

    return v_new_pyv_num;

end;
$$;
