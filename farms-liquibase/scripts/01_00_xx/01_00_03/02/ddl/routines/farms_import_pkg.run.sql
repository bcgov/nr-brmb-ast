create or replace function farms_import_pkg.run(
    in in_version_id numeric,
    in in_user varchar
)
returns numeric
language plpgsql
as $$
declare
    z01_cursor cursor for
        select z.participant_pin,
               z.first_name,
               z.last_name,
               z.corp_name
        from farms.z01_participant_information z;
    z01_val record;

    imp_opened numeric := 0;
    imp_closed numeric := 0;

    part_opened numeric := 0;
    part_closed numeric := 0;

    part_opened_a numeric := 0;
    part_closed_a numeric := 0;

    errors numeric := 0;
    errors_t numeric := 0;

    v_agristability_client_id farms.agristability_client.agristability_client_id%type := null;
    in_out_activity numeric := 0;
    log_id numeric;

    start_time timestamp := null;
    proc_start_time timestamp := current_timestamp;
    total_pins numeric := 0;
    completed_pins numeric := 0;

    changed_contact_client_ids numeric[] := '{}';
begin

    call farms_import_pkg.clear_log(in_version_id);
    call farms_import_pkg.append_imp1(in_version_id, '<IMPORT_LOG>');
    imp_opened := 1;

    -- start with code values
    call farms_import_pkg.update_status(in_version_id, 'Copying config data from previous years');
    call farms_import_pkg.create_config_data(in_user);

    call farms_import_pkg.update_status(in_version_id, 'Processing Production Units');
    errors := errors + farms_import_pkg.production_unit(in_version_id, in_user); -- Z28

    call farms_import_pkg.update_status(in_version_id, 'Processing Commodities');
    errors := errors + farms_import_pkg.commodities(in_version_id, in_user); -- Z29

    start_time := current_timestamp;
    select count(*)
    into total_pins
    from farms.z01_participant_information z;

    -- start 'real' data
    for z01_val in z01_cursor
    loop

        if part_opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<PARTICIPANTS>');
            part_opened := 1;
        end if;

        part_opened_a := 0;
        part_closed_a := 0;
        in_out_activity := 0;
        log_id := farms_import_pkg.append_imp(
            in_version_id,
            '<PARTICIPANT pin="' || farms_import_pkg.scrub(z01_val.participant_pin::varchar) ||
            '" first_name="' || farms_import_pkg.scrub(z01_val.first_name) ||
            '" last_name="' || farms_import_pkg.scrub(z01_val.last_name) ||
            '" corp_name="' || farms_import_pkg.scrub(z01_val.corp_name) ||
            '" altered="false">'
        );
        part_opened_a := 1;

        -- deal with non-versioned part
        errors_t := farms_import_pkg.participant(
            in_version_id,
            z01_val.participant_pin,
            v_agristability_client_id,
            in_user,
            in_out_activity,
            changed_contact_client_ids
        );
        if errors_t > 0 then
            errors := errors + errors_t;
        else
            -- deal with versioned part
            errors := errors + farms_import_pkg.program_year(
                in_version_id,
                v_agristability_client_id,
                in_user,
                in_out_activity
            );
        end if;
        if in_out_activity > 0 then
            call farms_import_pkg.update_imp(
                log_id,
                '<PARTICIPANT pin="' || farms_import_pkg.scrub(z01_val.participant_pin::varchar) ||
                '" first_name="' || farms_import_pkg.scrub(z01_val.first_name) ||
                '" last_name="' || farms_import_pkg.scrub(z01_val.last_name) ||
                '" corp_name="' || farms_import_pkg.scrub(z01_val.corp_name) ||
                '" altered="true">'
            );
        end if;

        call farms_import_pkg.append_imp1(in_version_id, '</PARTICIPANT>');
        part_closed_a := 1;

        completed_pins := completed_pins + 1;
        call farms_import_pkg.update_status(
            in_version_id,
            'Processing ' || completed_pins ||
            '/' || total_pins ||
            ' PINs. Average processing time is ' ||
            trim(to_char(round(
                extract(epoch from (current_timestamp - start_time)) / completed_pins, 2
            ), '990.99')) || ' seconds/PIN.'
        );
    end loop;

    if part_opened > 0 then
        call farms_import_pkg.append_imp1(in_version_id, '</PARTICIPANTS>');
        part_closed := 1;
    end if;

    call farms_import_pkg.update_status(in_version_id, 'Processing Contact Transfers');
    call farms_import_pkg.contact_transfers(in_version_id, changed_contact_client_ids, in_user);

    call farms_import_pkg.update_status(in_version_id, 'Processing State Transfers');
    call farms_import_pkg.benefit_update_transfer(in_version_id, in_user);

    call farms_import_pkg.queue_fifo_calculation(in_version_id, in_user);

    if imp_opened > 0 then
        call farms_import_pkg.append_imp1(in_version_id, '</IMPORT_LOG>');
        imp_closed := 1;
    end if;

    call farms_import_pkg.update_status(
        in_version_id,
        'Processing of ' || completed_pins ||
        ' PINs Completed After ' ||
        trim(to_char(round(
            extract(epoch from (current_timestamp - proc_start_time)) / 60, 2
        ), '990.99')) || ' minutes.'
    );

    call farms_import_pkg.close_global_log(in_version_id);

    return errors;

exception
    when others then

        if part_opened_a > 0 and part_closed_a < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</PARTICIPANT>');
        end if;

        if part_opened > 0 and part_closed < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</PARTICIPANTS>');
        end if;

        call farms_import_pkg.append_imp1(in_version_id, -- general exception
            '<ERROR>' || farms_import_pkg.scrub(farms_error_pkg.codify(sqlerrm)) ||
            '</ERROR>');

        if imp_opened > 0 and imp_closed < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</IMPORT_LOG>');
        end if;

        call farms_import_pkg.close_global_log(in_version_id);
        return errors;
end;
$$;
