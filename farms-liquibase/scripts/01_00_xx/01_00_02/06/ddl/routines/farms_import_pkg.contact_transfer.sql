create or replace procedure farms_import_pkg.contact_transfer(
   in in_cra_version_id numeric,
   in in_changed_contact_client_ids numeric[],
   in in_user varchar
)
language plpgsql
as $$
declare
    client_id_cursor cursor for
        select unnest(in_changed_contact_client_ids) client_id;
    client_id_val record;

    b bytea := null;
    transfer_version_id farms.import_version.import_version_id%type;
    id_char varchar(10);
    cnt numeric := 0;

    import_date farms.import_version.create_date%type;
    import_description farms.import_version.description%type;
begin

    if array_length(in_changed_contact_client_ids, 1) > 0 then

        select iv.create_date,
               iv.description
        into import_date,
             import_description
        from farms.import_version iv
        where iv.import_version_id = in_cra_version_id;

        call farms_webapp_pkg.insert_import_version(
            transfer_version_id,
            'XCONTACT',
            'SS',
            'Transfer Contact Information for Import Version Id: ' || in_cra_version_id || ', Import Date: ' ||
            to_char(import_date, 'YYYY/MM/DD') || ', Description: ' || import_description,
            'farm_received.csv',
            null,
            in_user
        );

        select import_file
        into b
        from farms.import_version iv
        where import_version_id = transfer_version_id;

        for client_id_val in client_id_cursor
        loop
            cnt := cnt + 1;
            id_char := to_char(client_id_val.client_id);
            if cnt > 1 then
                id_char := ',' || id_char;
            end if;

            b := coalesce(b, ''::bytea) || convert_to(id_char, 'UTF8');

            update farms.import_version
            set import_file = b
            where import_version_id = transfer_version_id;
        end loop;

        call farms_webapp_pkg.update_status(
            in_cra_version_id,
            'Saved Contact Transfer List'
        );

    end if;
end;
$$;
