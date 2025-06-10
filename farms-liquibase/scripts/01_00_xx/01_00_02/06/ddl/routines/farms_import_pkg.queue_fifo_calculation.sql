create or replace procedure farms_import_pkg.queue_fifo_calculation(
   in in_cra_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    transfer_version_id farms.import_version.import_version_id%type;

    import_date farms.import_version.create_date%type;
    import_description farms.import_version.description%type;
begin

    select iv.create_date,
           iv.description
    into import_date,
         import_description
    from farms.import_version iv
    where iv.import_version_id = in_cra_version_id;

    call farms_webapp_pkg.insert_import_version(
        transfer_version_id,
        'FIFO',
        'SS',
        'FIFO Calculation for Import Version Id: ' || in_cra_version_id || ', Import Date: ' ||
        to_char(import_date, 'YYYY/MM/DD') || ', Description: ' || import_description,
        'fifo.csv',
        null,
        in_user
    );

    update farms.import_version iv
    set iv.import_file = null
    where iv.import_version_id = transfer_version_id;

    call farms_import_pkg.update_status(
        in_cra_version_id,
        'Queued FIFO Calculation'
    );

exception
    when others then
        call farms_import_pkg.append_imp1(
            in_cra_version_id,
            '<WARNING>Encountered a warning when queuing FIFO Calculation: ' || farms_import_pkg.scrub(sqlerrm) || '</WARNING>'
        );
end;
$$;
