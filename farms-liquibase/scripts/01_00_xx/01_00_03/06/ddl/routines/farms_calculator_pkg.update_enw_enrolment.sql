create or replace procedure farms_calculator_pkg.update_enw_enrolment(
    in in_agristability_scenario_id farms.farm_scenario_enrolments.agristability_scenario_id%type,
    in in_enrolment_year farms.farm_scenario_enrolments.enrolment_year%type,
    in in_enrolment_fee farms.farm_scenario_enrolments.enrolment_fee%type,
    in in_contribution_margin farms.farm_scenario_enrolments.contribution_margin%type,
    in in_benefit_calculated_ind farms.farm_scenario_enrolments.benefit_calculated_ind%type,
    in in_proxy_margins_calculated_ind farms.farm_scenario_enrolments.proxy_margins_calculated_ind%type,
    in in_manual_calculated_ind farms.farm_scenario_enrolments.manual_calculated_ind%type,
    in in_has_productive_units_ind farms.farm_scenario_enrolments.has_productive_units_ind%type,
    in in_has_benchmark_per_units_ind farms.farm_scenario_enrolments.has_productive_units_ind%type,
    in in_benefit_enrolment_fee farms.farm_scenario_enrolments.benefit_enrolment_fee%type,
    in in_benefit_contribution_margin farms.farm_scenario_enrolments.benefit_contribution_margin%type,
    in in_proxy_enrolment_fee farms.farm_scenario_enrolments.proxy_enrolment_fee%type,
    in in_proxy_contribution_margin farms.farm_scenario_enrolments.proxy_contribution_margin%type,
    in in_manual_enrolment_fee farms.farm_scenario_enrolments.manual_enrolment_fee%type,
    in in_manual_contribution_margin farms.farm_scenario_enrolments.manual_contribution_margin%type,
    in in_margin_year_minus_2 farms.farm_scenario_enrolments.margin_year_minus_2%type,
    in in_margin_year_minus_3 farms.farm_scenario_enrolments.margin_year_minus_3%type,
    in in_margin_year_minus_4 farms.farm_scenario_enrolments.margin_year_minus_4%type,
    in in_margin_year_minus_5 farms.farm_scenario_enrolments.margin_year_minus_5%type,
    in in_margin_year_minus_6 farms.farm_scenario_enrolments.margin_year_minus_6%type,
    in in_margin_year_minus_2_ind farms.farm_scenario_enrolments.margin_year_minus_2_ind%type,
    in in_margin_year_minus_3_ind farms.farm_scenario_enrolments.margin_year_minus_3_ind%type,
    in in_margin_year_minus_4_ind farms.farm_scenario_enrolments.margin_year_minus_4_ind%type,
    in in_margin_year_minus_5_ind farms.farm_scenario_enrolments.margin_year_minus_5_ind%type,
    in in_margin_year_minus_6_ind farms.farm_scenario_enrolments.margin_year_minus_6_ind%type,
    in in_benefit_margin_year_minus_2 farms.farm_scenario_enrolments.benefit_margin_year_minus_2%type,
    in in_benefit_margin_year_minus_3 farms.farm_scenario_enrolments.benefit_margin_year_minus_3%type,
    in in_benefit_margin_year_minus_4 farms.farm_scenario_enrolments.benefit_margin_year_minus_4%type,
    in in_benefit_margin_year_minus_5 farms.farm_scenario_enrolments.benefit_margin_year_minus_5%type,
    in in_benefit_margin_year_minus_6 farms.farm_scenario_enrolments.benefit_margin_year_minus_6%type,
    in in_benefit_margn_year_minus_2_ind farms.farm_scenario_enrolments.benefit_margn_year_minus_2_ind%type,
    in in_benefit_margn_year_minus_3_ind farms.farm_scenario_enrolments.benefit_margn_year_minus_3_ind%type,
    in in_benefit_margn_year_minus_4_ind farms.farm_scenario_enrolments.benefit_margn_year_minus_4_ind%type,
    in in_benefit_margn_year_minus_5_ind farms.farm_scenario_enrolments.benefit_margn_year_minus_5_ind%type,
    in in_benefit_margn_year_minus_6_ind farms.farm_scenario_enrolments.benefit_margn_year_minus_6_ind%type,
    in in_proxy_margin_year_minus_2 farms.farm_scenario_enrolments.proxy_margin_year_minus_2%type,
    in in_proxy_margin_year_minus_3 farms.farm_scenario_enrolments.proxy_margin_year_minus_3%type,
    in in_proxy_margin_year_minus_4 farms.farm_scenario_enrolments.proxy_margin_year_minus_4%type,
    in in_manual_margin_year_minus_2 farms.farm_scenario_enrolments.manual_margin_year_minus_2%type,
    in in_manual_margin_year_minus_3 farms.farm_scenario_enrolments.manual_margin_year_minus_3%type,
    in in_manual_margin_year_minus_4 farms.farm_scenario_enrolments.manual_margin_year_minus_4%type,
    in in_combined_farm_percent farms.farm_scenario_enrolments.combined_farm_percent%type,
    in in_enrolment_calc_type_code farms.farm_scenario_enrolments.enrolment_calc_type_code%type,
    in in_revision_count farms.farm_scenario_enrolments.revision_count%type,
    in in_user farms.farm_scenario_enrolments.who_updated%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin
    update farms.farm_scenario_enrolments
    set enrolment_year = in_enrolment_year,
        enrolment_fee = in_enrolment_fee,
        contribution_margin = in_contribution_margin,
        benefit_calculated_ind = in_benefit_calculated_ind,
        proxy_margins_calculated_ind = in_proxy_margins_calculated_ind,
        manual_calculated_ind = in_manual_calculated_ind,
        has_productive_units_ind = in_has_productive_units_ind,
        has_benchmark_per_units_ind = in_has_benchmark_per_units_ind,
        benefit_enrolment_fee = in_benefit_enrolment_fee,
        benefit_contribution_margin = in_benefit_contribution_margin,
        proxy_enrolment_fee = in_proxy_enrolment_fee,
        proxy_contribution_margin = in_proxy_contribution_margin,
        manual_enrolment_fee = in_manual_enrolment_fee,
        manual_contribution_margin = in_manual_contribution_margin,
        margin_year_minus_2 = in_margin_year_minus_2,
        margin_year_minus_3 = in_margin_year_minus_3,
        margin_year_minus_4 = in_margin_year_minus_4,
        margin_year_minus_5 = in_margin_year_minus_5,
        margin_year_minus_6 = in_margin_year_minus_6,
        margin_year_minus_2_ind = in_margin_year_minus_2_ind,
        margin_year_minus_3_ind = in_margin_year_minus_3_ind,
        margin_year_minus_4_ind = in_margin_year_minus_4_ind,
        margin_year_minus_5_ind = in_margin_year_minus_5_ind,
        margin_year_minus_6_ind = in_margin_year_minus_6_ind,
        benefit_margin_year_minus_2 = in_benefit_margin_year_minus_2,
        benefit_margin_year_minus_3 = in_benefit_margin_year_minus_3,
        benefit_margin_year_minus_4 = in_benefit_margin_year_minus_4,
        benefit_margin_year_minus_5 = in_benefit_margin_year_minus_5,
        benefit_margin_year_minus_6 = in_benefit_margin_year_minus_6,
        benefit_margn_year_minus_2_ind = in_benefit_margn_year_minus_2_ind,
        benefit_margn_year_minus_3_ind = in_benefit_margn_year_minus_3_ind,
        benefit_margn_year_minus_4_ind = in_benefit_margn_year_minus_4_ind,
        benefit_margn_year_minus_5_ind = in_benefit_margn_year_minus_5_ind,
        benefit_margn_year_minus_6_ind = in_benefit_margn_year_minus_6_ind,
        proxy_margin_year_minus_2 = in_proxy_margin_year_minus_2,
        proxy_margin_year_minus_3 = in_proxy_margin_year_minus_3,
        proxy_margin_year_minus_4 = in_proxy_margin_year_minus_4,
        manual_margin_year_minus_2 = in_manual_margin_year_minus_2,
        manual_margin_year_minus_3 = in_manual_margin_year_minus_3,
        manual_margin_year_minus_4 = in_manual_margin_year_minus_4,
        combined_farm_percent = in_combined_farm_percent,
        enrolment_calc_type_code = in_enrolment_calc_type_code,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_agristability_scenario_id
    and revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;
  end;
$$;
