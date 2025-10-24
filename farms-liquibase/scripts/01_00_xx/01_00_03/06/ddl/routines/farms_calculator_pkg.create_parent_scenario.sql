create or replace function farms_calculator_pkg.create_parent_scenario(
    in in_program_year_version_id farms.farm_agristability_scenarios.program_year_version_id%type,
    in in_scenario_class_code farms.farm_agristability_scenarios.scenario_class_code%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_version farms.farm_agristability_scenarios.benefits_calculator_version%type,
    in in_cra_income_expns_rec_date farms.farm_agristability_scenarios.cra_income_expns_received_date%type,
    in in_cra_suppl_received_date farms.farm_agristability_scenarios.cra_supplemental_received_date%type,
    in in_participnt_data_src_code farms.farm_agristability_scenarios.participnt_data_src_code%type,
    in in_user farms.farm_agristability_scenarios.who_created%type
) returns farms.farm_agristability_scenarios.agristability_scenario_id%type
language plpgsql
as
$$
declare

    sc_id farms.farm_agristability_scenarios.agristability_scenario_id%type := null;
    s_num farms.farm_agristability_scenarios.scenario_number%type := null;

begin
    select nextval('farms.farm_as_seq')
    into sc_id;

    select (coalesce(max(sc.scenario_number), 0) + 1)
    into s_num
    from farms.farm_agristability_scenarios sc
    join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
    where pyv.program_year_id = (
        select program_year_id
        from farms.farm_program_year_versions t
        where t.program_year_version_id = in_program_year_version_id
    );

    insert into farms.farm_agristability_scenarios (
        agristability_scenario_id,
        scenario_number,
        benefits_calculator_version,
        scenario_created_by,
        default_ind,
        scenario_creation_date,
        description,
        program_year_version_id,
        scenario_class_code,
        scenario_state_code,
        scenario_category_code,
        participnt_data_src_code,
        combined_farm_number,
        cra_income_expns_received_date,
        cra_supplemental_received_date,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        sc_id,
        s_num,
        in_version,
        in_user,
        'N',
        current_timestamp,
        null,
        in_program_year_version_id,
        in_scenario_class_code,
        'IP',
        in_scenario_category_code,
        in_participnt_data_src_code,
        null,
        in_cra_income_expns_rec_date,
        in_cra_suppl_received_date,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    insert into farms.farm_scenario_state_audits(
        scenario_state_audit_id,
        state_change_reason,
        agristability_scenario_id,
        scenario_state_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_ssa_seq'),
        null,
        sc_id,
        'IP',
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    call farms_calculator_pkg.log_import_comments(in_program_year_version_id, sc_id, in_user);

    return sc_id;
end;
$$;
