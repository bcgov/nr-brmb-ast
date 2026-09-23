create or replace procedure farms_webapp_pkg.update_cob_document(
    in in_scenario_id farms.farm_benefit_calc_documents.agristability_scenario_id%type,
    in in_document farms.farm_benefit_calc_documents.document%type,
    in in_userid farms.farm_benefit_calc_documents.who_created%type
)
language plpgsql
as
$$
begin
    update farms.farm_benefit_calc_documents
    set document = in_document,
        who_updated = in_userid,
        when_updated = current_timestamp
    where agristability_scenario_id = in_scenario_id;
end;
$$;
