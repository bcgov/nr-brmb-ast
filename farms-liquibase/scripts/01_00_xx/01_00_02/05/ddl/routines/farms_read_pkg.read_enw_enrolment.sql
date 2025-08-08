create or replace function farms_read_pkg.read_enw_enrolment(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select se.scenario_enrolment_id,
               se.enrolment_year,
               se.enrolment_fee,
               se.contribution_margin,
               se.benefit_calculated_ind,
               se.proxy_margins_calculated_ind,
               se.manual_calculated_ind,
               se.has_productive_units_ind,
               se.has_benchmark_per_units_ind,
               se.benefit_enrolment_fee,
               se.benefit_contribution_margin,
               se.proxy_enrolment_fee,
               se.proxy_contribution_margin,
               se.manual_enrolment_fee,
               se.manual_contribution_margin,
               se.margin_year_minus_2,
               se.margin_year_minus_3,
               se.margin_year_minus_4,
               se.margin_year_minus_5,
               se.margin_year_minus_6,
               se.margin_year_minus_2_ind,
               se.margin_year_minus_3_ind,
               se.margin_year_minus_4_ind,
               se.margin_year_minus_5_ind,
               se.margin_year_minus_6_ind,
               se.benefit_margin_year_minus_2,
               se.benefit_margin_year_minus_3,
               se.benefit_margin_year_minus_4,
               se.benefit_margin_year_minus_5,
               se.benefit_margin_year_minus_6,
               se.benefit_margn_year_minus_2_ind,
               se.benefit_margn_year_minus_3_ind,
               se.benefit_margn_year_minus_4_ind,
               se.benefit_margn_year_minus_5_ind,
               se.benefit_margn_year_minus_6_ind,
               se.proxy_margin_year_minus_2,
               se.proxy_margin_year_minus_3,
               se.proxy_margin_year_minus_4,
               se.manual_margin_year_minus_2,
               se.manual_margin_year_minus_3,
               se.manual_margin_year_minus_4,
               se.combined_farm_number,
               se.enrolment_calc_type_code,
               se.revision_count
        from farms.farm_scenario_enrolments se
        where se.agristability_scenario_id = in_agristability_scenario_id;

    return cur;

end;
$$;
