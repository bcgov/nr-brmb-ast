create or replace procedure farms_webapp_pkg.insert_cob(
    inout io_cob_id farms.farm_benefit_calc_documents.benefit_calc_document_id%type,
    in in_scenario_id farms.farm_benefit_calc_documents.agristability_scenario_id%type,
    in in_userid farms.farm_benefit_calc_documents.who_created%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_benefit_calc_documents(
        benefit_calc_document_id,
        generation_date,
        document,
        agristability_scenario_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_bcd_seq'),
        current_timestamp,
        '',
        in_scenario_id,
        1,
        in_userid,
        current_timestamp,
        in_userid,
        current_timestamp
    )
    returning benefit_calc_document_id into io_cob_id;
end;
$$;
