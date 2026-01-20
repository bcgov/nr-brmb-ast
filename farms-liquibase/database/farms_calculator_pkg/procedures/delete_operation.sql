create or replace procedure farms_calculator_pkg.delete_operation(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_program_year_version_id farms.farm_farming_operations.program_year_version_id%type,
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_revision_count farms.farm_farming_operations.revision_count%type,
    in in_user farms.farm_farming_operations.who_updated%type
)
language plpgsql
as
$$
declare

    ora2pg_rowcount int;
    v_has_verified bigint;

BEGIN

    v_has_verified := farms_webapp_pkg.pyv_has_verified_sc(in_program_year_version_id);

    if v_has_verified = 0 then

        call farms_calculator_pkg.close_pyv_other_scenarios(
            in_program_year_version_id,
            in_scenario_id,
            'Scenario was invalidated when a locally generated operation was deleted.',
            in_user
        );

        delete from farms.farm_benefit_calc_margins t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_farming_operatin_prtnrs t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_production_insurances t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_productve_unit_capacities t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_reported_income_expenses t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_reported_inventories t
        where t.farming_operation_id = in_farming_operation_id;

        delete from farms.farm_farming_operations op
        where op.farming_operation_id = in_farming_operation_id
        and op.locally_generated_ind = 'Y'
        and op.revision_count = in_revision_count
        and v_has_verified = 0;

        get diagnostics ora2pg_rowcount = row_count;
        if ora2pg_rowcount <> 1 then
            raise exception '%', farms_types_pkg.invalid_revision_count_msg()
            using errcode = farms_types_pkg.invalid_revision_count_code;
        end if;
    end if;
end;
$$;
