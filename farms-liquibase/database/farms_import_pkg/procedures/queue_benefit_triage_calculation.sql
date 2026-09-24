create or replace procedure farms_import_pkg.queue_benefit_triage_calculation(
   in in_cra_version_id bigint,
   in in_user varchar
)
language plpgsql
as $$
declare
    transfer_version_id farms.farm_import_versions.import_version_id%type;

    import_date farms.farm_import_versions.when_created%type;
    import_description farms.farm_import_versions.description%type;
begin

    select iv.when_created,
           iv.description
    into import_date,
         import_description
    from farms.farm_import_versions iv
    where iv.import_version_id = in_cra_version_id;

    call farms_webapp_pkg.insert_import_version(
        transfer_version_id,
        'TRIAGE',
        'SS',
        'Benefit Triage Calculation for Import Version Id: ' || in_cra_version_id || ', Import Date: ' ||
        to_char(import_date, 'YYYY/MM/DD') || ', Description: ' || import_description,
        'benefitTriage.csv',
        null,
        null, -- the triage calculation reads the operational tables, so there is no import file
        in_user
    );

    call farms_import_pkg.update_status(
        in_cra_version_id,
        'Queued Benefit Triage Calculation'
    );

exception
    when others then
        call farms_import_pkg.append_imp1(
            in_cra_version_id,
            '<WARNING>Encountered a warning when queuing Benefit Triage Calculation: ' || farms_import_pkg.scrub(sqlerrm) || '</WARNING>'
        );
end;
$$;
