create or replace procedure farms_chefs_pkg.update_scenario_submission_id(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_chef_submission_id farms.farm_agristability_scenarios.chef_submission_id%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set chef_submission_id = in_chef_submission_id,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_agristability_scenario_id;

end;
$$;
