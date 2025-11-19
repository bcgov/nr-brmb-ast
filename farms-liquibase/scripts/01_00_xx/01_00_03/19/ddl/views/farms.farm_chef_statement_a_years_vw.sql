create or replace view farms.farm_chef_statement_a_years_vw as
select participant_pin,
       year,
       agristability_scenario_id,
       agristability_client_id,
       program_year_id,
       program_year_version_id
from (
    select sv.participant_pin,
           sv.year,
           sv.agristability_scenario_id,
           sv.agristability_client_id,
           sv.program_year_id,
           sv.program_year_version_id,
           first_value(sv.agristability_scenario_id) over(
               partition by sv.program_year_id
               order by sv.scenario_number desc
           ) latest_sc_id
    from farms.farm_scenarios_vw sv
    where sv.scenario_category_code = 'CHEF_STA'
) t
where agristability_scenario_id = latest_sc_id;
