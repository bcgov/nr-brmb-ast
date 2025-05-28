create or replace function farms_import_pkg.is_benefit_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    v_scenario_id numeric := null;
    cnt numeric := null;
begin
    -- find previous scenario id
    select first_value(s.agristability_scenario_id) over (order by s.scenario_number desc) s_id
    into v_scenario_id
    from farms.agristability_scenario s
    where s.scenario_class_code = 'CRA'
    and s.program_year_version_id = in_program_year_vrsn_prev_id;

    -- check Claim
    select count(*) cnt
    into cnt
    from (
        select *
        from (
            select unadjusted_production_margin,
                   production_margin_accrual_adjustments,
                   production_margin_after_structure_changes,
                   structural_change_adjustments
            from farms.agristability_client ac
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
            left outer join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            left outer join farms.agristability_scenario sc on pyv.program_year_version_id = sc.program_year_version_id
            join farms.benefit_calculation_total bct on sc.agristability_scenario_id = bct.agristability_scenario_id
            where sc.agristability_scenario_id = v_scenario_id
            except
            select z50.unadjusted_reference_margin unadjusted_production_margin,
                   z50.program_margin production_margin_accrual_adjustments,
                   z50.adjusted_reference_margin production_margin_after_structure_changes,
                   z50.structure_change_adjustment_amount structural_change_adjustments
            from farms.z50_participant_benefit_calculation z50
            join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and py.year = z50.program_year
            where py.program_year_id = in_program_year_id
        ) t1
        union all
        (
            select z50.unadjusted_reference_margin unadjusted_production_margin,
                   z50.program_margin production_margin_accrual_adjustments,
                   z50.adjusted_reference_margin production_margin_after_structure_changes,
                   z50.structure_change_adjustment_amount structural_change_adjustments
            from farms.z50_participant_benefit_calculation z50
            join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                         and py.year = z50.program_year
            where py.program_year_id = in_program_year_id
            except
            select unadjusted_production_margin,
                   production_margin_accrual_adjustments,
                   production_margin_after_structure_changes,
                   structural_change_adjustments
            from farms.agristability_client ac
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
            left outer join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            left outer join farms.agristability_scenario sc on pyv.program_year_version_id = sc.program_year_version_id
            join farms.benefit_calculation_total bct on sc.agristability_scenario_id = bct.agristability_scenario_id
            where sc.agristability_scenario_id = v_scenario_id
        )
    ) t2;

    if cnt > 0 then
        return true;
    end if;

    return false;
end;
$$;
