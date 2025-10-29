create or replace procedure farms_webapp_pkg.delete_cob(
    in in_scenario_id farms.farm_benefit_calc_documents.agristability_scenario_id%type
)
language plpgsql
as
$$
begin
    delete from farms.farm_benefit_calc_documents
    where agristability_scenario_id = in_scenario_id;
end;
$$;
