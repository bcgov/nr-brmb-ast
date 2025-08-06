create or replace procedure farms_import_pkg.benefit_update_transfer(
   in in_cra_version_id farms.farm_import_versions.import_version_id%type,
   in in_user varchar
)
language plpgsql
as $$
declare
    received_cursor cursor for
        select s.agristability_scenario_id,
               s.scenario_number,
               ac.participant_pin,
               py.year program_year,
               'REC' state,
               s.scenario_category_code,
               to_char(current_date, 'YYYY-MM-DD HH24:MI:SS') state_change_date,
               'SYSTEM' verifier,
               to_char(s.cra_supplemental_received_date, 'YYYY-MM-DD HH24:MI:SS') supplemental_received_date,
               to_char(coalesce(pyv.received_date, py.when_created), 'YYYY-MM-DD HH24:MI:SS') file_start_date,
               null benefit_amount,
               null interim_benefit_percent,
               null allocated_reference_margin,
               (case
                   when exists (
                       select *
                       from farms.farm_farming_operations fo
                       join farms.farming_operation_partner fop on fop.farming_operation_id = fo.farming_operation_id
                       where fo.program_year_version_id = pyv.program_year_version_id
                       group by fo.farming_operation_id
                       having count(1) >= 1
                   ) or exists (
                       select *
                       from farms.farm_farming_operations fo
                       where fo.program_year_version_id = pyv.program_year_version_id
                       and (
                           fo.partnership_pin != 0 or
                           fo.partnership_name is not null or
                           fo.partnership_percent != 1
                       )
                   ) then 'Y'
                   else 'N'
               end) partnership_indicator,
               mc.description municipality_description,
               py.non_participant_ind,
               null negative_margin_decline,
               null negative_margin_benefit,
               'N' late_participant_ind,
               null provincially_funded_amount,
               null prod_insur_deemed_benefit,
               null late_enrolment_penalty,
               to_char(py.local_supplemntl_received_date, 'YYYY-MM-DD') local_supp_date_string,
               to_char(py.local_statement_a_received_date, 'YYYY-MM-DD') local_statement_a_date_string,
               to_char(pyv.received_date, 'YYYY-MM-DD') cra_statement_a_date_string,
               pyv.can_send_cob_to_rep_ind send_copy_to_contact_person_ind
        from farms.farm_program_year_versions pyv
        join farms.farm_agristability_scenarios s on s.program_year_version_id = pyv.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        left outer join farms.farm_municipality_codes mc on mc.municipality_code = pyv.municipality_code
        where s.scenario_class_code = 'CRA'
        and pyv.program_year_version_id in (
            select program_year_version_id
            from (
                select pyv.program_year_version_id,
                       rank() over (partition by ac.participant_pin order by pyv.program_year_version_number desc) rnk
                from farms.farm_program_year_versions pyv
                join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
                join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
                join (
                    select ac.participant_pin,
                           pyv.import_version_id,
                           row_number() over (partition by ac.participant_pin order by 1) row_num,
                           max(z.program_year) over (order by 1) max_year
                    from farms.farm_program_year_versions pyv
                    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
                    join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
                    join farms.farm_z02_partpnt_farm_infos z on z.participant_pin = ac.participant_pin
                    where pyv.import_version_id = in_cra_version_id
                ) pins on pins.participant_pin = ac.participant_pin
                       and pins.max_year = py.year
                       and pins.row_num = 1
                       and pins.import_version_id = pyv.import_version_id
            ) t
            where rnk = 1
        )
        order by s.agristability_scenario_id;
    transfer_val record;

    pyv_count numeric;
    b bytea := null;
    transfer_version_id farms.farm_import_versions.import_version_id%type;
    cur_line varchar(32767);
    cnt numeric := 0;
    scenario_ids numeric[] := '{}';
    farm_sector_detail_code farms.farm_sector_detail_codes.sector_detail_code%type;
    farm_sector farms.farm_sector_codes.description%type;
    farm_sector_detail farms.farm_sector_detail_codes.description%type;
    bpu_set_complete_ind varchar(1);
    fmv_set_complete_ind varchar(1);
    import_date farms.farm_import_versions.when_created%type;
    import_description farms.farm_import_versions.description%type;
begin

    open received_cursor;
    fetch received_cursor into transfer_val;

    if received_cursor%found then

        select iv.when_created,
               iv.description
        into import_date,
             import_description
        from farms.farm_import_versions iv
        where iv.import_version_id = in_cra_version_id;

        call farms_webapp_pkg.insert_import_version(
            transfer_version_id,
            'XSTATE',
            'SS',
            'Transfer Received Data for Import Version Id: ' || in_cra_version_id || ', Import Date: ' ||
            to_char(import_date, 'YYYY/MM/DD') || ', Description: ' || import_description,
            'farm_received.csv',
            null,
            in_user
        );

        select import_file
        into b
        from farms.farm_import_versions iv
        where import_version_id = transfer_version_id;

        loop
            cnt := cnt + 1;
            call farms_import_pkg.update_status(
                in_cra_version_id,
                'Processing State Transfer for Scenario Id: ' || transfer_val.agristability_scenario_id
            );

            farm_sector_detail_code := farms_report_pkg.get_sector_detail_code(transfer_val.agristability_scenario_id);
            select sc.description sector_code_desc,
                   sdc.description sector_detail_code_desc
            into farm_sector,
                 farm_sector_detail
            from farms.farm_sector_detail_xref sdx
            join farms.farm_sector_codes sc on sc.sector_code = sdx.sector_code
            join farms.farm_sector_detail_codes sdc on sdc.sector_detail_code = sdx.sector_detail_code
            where sdx.sector_detail_code = farm_sector_detail_code;

            scenario_ids := farms_import_pkg.get_scenario_ids(
                transfer_val.program_year,
                transfer_val.participant_pin,
                transfer_val.scenario_number
            );
            bpu_set_complete_ind := farms_import_pkg.is_bpu_set_complete(transfer_val.agristability_scenario_id, scenario_ids);
            fmv_set_complete_ind := farms_import_pkg.is_fmv_set_complete(scenario_ids);
            cur_line := transfer_val.participant_pin || ',' ||
                        transfer_val.program_year || ',' ||
                        transfer_val.state || ',' ||
                        transfer_val.state_change_date || ',' ||
                        transfer_val.verifier || ',' ||
                        transfer_val.supplemental_received_date || ',' ||
                        transfer_val.file_start_date || ',"' ||
                        farm_sector || '","' ||
                        farm_sector_detail || '",' ||
                        transfer_val.benefit_amount || ',' ||
                        null || ',' || -- scenario number is null unless the scenario is COMP
                        transfer_val.partnership_indicator || ',' ||
                        bpu_set_complete_ind || ',' ||
                        fmv_set_complete_ind || ',' ||
                        'N,,"' || -- inCombinedFarm indictor and combinedFarmPins list
                        transfer_val.municipality_description || '",' ||
                        transfer_val.non_participant_ind || ',' ||
                        transfer_val.scenario_category_code || ',' ||
                        transfer_val.interim_benefit_percent || ',' ||
                        transfer_val.allocated_reference_margin || ',' ||
                        transfer_val.negative_margin_decline || ',' ||
                        transfer_val.negative_margin_benefit || ',' ||
                        transfer_val.late_participant_ind || ',' ||
                        transfer_val.provincially_funded_amount || ',' ||
                        transfer_val.prod_insur_deemed_benefit || ',' ||
                        transfer_val.late_enrolment_penalty || ',' ||
                        transfer_val.local_supp_date_string || ',' ||
                        transfer_val.local_statement_a_date_string || ',' ||
                        transfer_val.cra_statement_a_date_string || ',' ||
                        transfer_val.send_copy_to_contact_person_ind || ',' ||
                        null || ',' || -- CHEFS form notes
                        null || ',' || -- CHEFS form user type
                        null || ',' || -- CHEF form submission GUID
                        null || ',' || -- Expecting Payment Indicator
                        null || ',' || -- CHEFS form type
                        null ||        -- FIFO Result Type
                        chr(10);

            b := coalesce(b, ''::bytea) || convert_to(cur_line, 'UTF8');

            -- Add 2 seconds to the When_Created date so that this
            -- transfer will be run after the contact transfer.
            -- Do the same for When_Updated so it doesn't look weird.
            update farms.farm_import_versions
            set import_file = b,
                when_created = when_created + interval '2 seconds',
                when_updated = when_updated + interval '2 seconds'
            where import_version_id = transfer_version_id;

            fetch received_cursor into transfer_val;
            exit when received_cursor%notfound;
        end loop;

        call farms_import_pkg.update_status(
            in_cra_version_id,
            'Saved State Transfer List'
        );
    end if;
end;
$$;
