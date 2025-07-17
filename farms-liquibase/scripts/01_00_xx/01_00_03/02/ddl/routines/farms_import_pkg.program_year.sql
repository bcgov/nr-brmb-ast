create or replace function farms_import_pkg.program_year(
    in in_version_id numeric,
    in in_agristability_client_id farms.agristability_client.agristability_client_id%type,
    in in_user varchar,
    inout in_out_activity numeric,
    out errors numeric
)
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    z02_cursor cursor for
        select a.*,
               pyv.program_year_version_id,
               pyv.form_version_number p_form_version_number,
               pyv.form_version_effective_date p_form_version_effective_date,
               pyv.common_share_total p_common_share_total,
               pyv.farm_years p_farm_years,
               pyv.accrual_worksheet_indicator p_accrual_worksheet_indicator,
               pyv.completed_production_cycle_indicator p_completed_production_cycle_indicator,
               pyv.cwb_worksheet_indicator p_cwb_worksheet_indicator,
               pyv.perishable_commodities_indicator p_perishable_commodities_indicator,
               pyv.receipts_indicator p_receipts_indicator,
               pyv.accrual_cash_conversion_indicator p_accrual_cash_conversion_indicator,
               pyv.combined_farm_indicator p_combined_farm_indicator,
               pyv.coop_member_indicator p_coop_member_indicator,
               pyv.corporate_shareholder_indicator p_corporate_shareholder_indicator,
               pyv.disaster_indicator p_disaster_indicator,
               pyv.partnership_member_indicator p_partnership_member_indicator,
               pyv.sole_proprietor_indicator p_sole_proprietor_indicator,
               pyv.other_text p_other_text,
               pyv.post_mark_date p_post_mark_date,
               pyv.province_of_residence p_province_of_residence,
               pyv.received_date p_received_date,
               pyv.last_year_farming_indicator p_last_year_farming_indicator,
               pyv.can_send_cob_to_representative_indicator p_can_send_cob_to_representative_indicator,
               pyv.province_of_main_farmstead p_province_of_main_farmstead,
               pyv.locally_updated_indicator p_locally_updated_indicator,
               pyv.participant_profile_code p_participant_profile_code,
               pyv.municipality_code p_municipality_code,
               pyv.federal_status_code p_federal_status_code
        from (
            select z.participant_pin,
                   z.program_year,
                   z.form_version_number,
                   z.province_of_residence,
                   z.province_of_main_farmstead,
                   z.postmark_date,
                   z.received_date,
                   z.sole_proprietor_indicator,
                   z.partnership_member_indicator,
                   z.corporate_shareholder_indicator,
                   z.coop_member_indicator,
                   z.common_share_total,
                   z.farm_years,
                   z.last_year_farming_indicator,
                   z.form_origin_code,
                   z.industry_code,
                   z.participant_profile_code,
                   z.accrual_cash_conversion_indicator,
                   z.perishable_commodities_indicator,
                   z.receipts_indicator,
                   z.other_text_indicator,
                   z.other_text,
                   z.accrual_worksheet_indicator,
                   z.cwb_worksheet_indicator,
                   z.combined_this_year_indicator,
                   z.completed_production_cycle_indicator,
                   z.disaster_indicator,
                   z.copy_cob_to_contact_indicator,
                   (case
                       when fmc.municipality_code is not null then z.municipality_code::varchar
                       else Unknown
                   end) municipality_code,
                   z.municipality_code in_mun_cd,
                   to_date(z.form_version_effective_date, 'YYYYMMDD') form_version_effective_date,
                   z50.agristability_status,
                   ac.agristability_client_id,
                   py.program_year_id,
                   (
                       select max(pyv.program_year_version_number)
                       from farms.program_year_version pyv
                       where py.program_year_id = pyv.program_year_id
                   ) prev_py_version_number
            from farms.z02_participant_farm_information z
            join farms.agristability_client ac on z.participant_pin = ac.participant_pin
            left outer join farms.z50_participant_benefit_calculation z50 on z.participant_pin = z50.participant_pin
                                                                          and z.program_year = z50.program_year
            left outer join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                                  and py.year = z.program_year
            left outer join farms.municipality_code fmc on z.municipality_code::varchar = fmc.municipality_code
            where ac.agristability_client_id = in_agristability_client_id
        ) a
        left outer join farms.program_year_version pyv on a.program_year_id = pyv.program_year_id
                                                       and a.prev_py_version_number = pyv.program_year_version_number
        order by a.program_year asc; -- historical first!
    z02_val record;

    py_id numeric := null;
    pyv_id numeric := null;

    opened numeric := 0;

    opened_py numeric := 0;
    closed_py numeric := 0;

    error_msg varchar(512);

    ch boolean := true;

    -- for warnings
    pmd date := null;
    rd date := null;
    ppc varchar(10) := null;
    mc varchar(10) := null;

    v_program_year farms.program_year.program_year_id%type := null;
    v_prev_program_year farms.program_year.program_year_id%type := null;
    v_duplicate_year boolean := false;
    v_has_verified_scenario varchar(1);
    v_agristability_status farms.program_year_version.federal_status_code%type := null;
begin
    for z02_val in z02_cursor
    loop

        opened_py := 0;
        v_program_year := z02_val.program_year;
        v_agristability_status := z02_val.agristability_status;
        -- for warnings
        pmd := to_date(z02_val.post_mark_date, 'YYYYMMDD');
        rd := to_date(z02_val.received_date, 'YYYYMMDD');
        ppc := z02_val.participant_profile_code;
        mc := z02_val.municipality_code;

        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<PROGRAM_YEARS>');
            opened := 1;
        end if;

        error_msg := null;

        v_duplicate_year := v_prev_program_year = v_program_year;

        if v_duplicate_year then
            call farms_import_pkg.append_imp1(
                in_version_id,
                '<PROGRAM_YEAR action="error" year="' ||
                farms_import_pkg.scrub(v02_val.program_year) || '">'
            );
        end if;

        begin
            if v_duplicate_year then
                opened_py := 1;
                raise exception 'Duplicate Program Year or Program Year Version Number';
            end if;

            ch := false;
            if z02_val.program_year_id is null then

                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<PROGRAM_YEAR action="add" year="' ||
                    farms_import_pkg.scrub(v02_val.program_year) || '" altered="true">'
                );
                opened_py := 1;
                in_out_activity := in_out_activity + 1;

                ch := true;
            else
                py_id := z02_val.program_year_id;
                ch := farms_import_pkg.is_changed_bool(z02_val.program_year_id, z02_val.program_year_version_id);

                if ch then
                    call farms_import_pkg.append_imp1(
                        in_version_id,
                        '<PROGRAM_YEAR action="updated" year="' ||
                        farms_import_pkg.scrub(v02_val.program_year) || '" altered="true">'
                    );
                    opened_py := 1;
                    in_out_activity := in_out_activity + 1;
                end if;
            end if;

            if opened_py = 0 then
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<PROGRAM_YEAR action="none" year="' ||
                    farms_import_pkg.scrub(v02_val.program_year) || '" altered="false">'
                );
                opened_py := 1;
            end if;

            if mc = Unknown then
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<WARNING>' ||
                    farms_error_pkg.codify_mun_code(farms_import_pkg.scrub(z02_val.in_mun_cd)) ||
                    '</WARNING>'
                );
            end if;

            -- check PY existance, insert if not found
            if z02_val.program_year_id is null then
                select nextval('farms.seq_py')
                into py_id;
                insert into farms.program_year (
                    program_year_id,
                    year,
                    non_participant_indicator,
                    late_participant_indicator,
                    agristability_client_id,
                    farm_type_code,
                    revision_count,
                    create_user,
                    create_date,
                    update_user,
                    update_date
                ) values (
                    py_id,
                    z02_val.program_year,
                    'N',
                    'N',
                    z02_val.agristability_client_id,
                    (
                        select farm_type_code
                        from (
                            select first_value(py.farm_type_code) over (order by py.year desc) farm_type_code
                            from farms.program_year py
                            where py.agristability_client_id = z02_val.agristability_client_id
                            and py.year < z02_val.program_year
                            limit 1
                        )
                    ),
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );
            end if;

            if ch then

                select nextval('farms.seq_pyv')
                into pyv_id;

                insert into farms.program_year_version (
                    program_year_version_id,
                    program_year_version_number,
                    form_version_number,
                    form_version_effective_date,
                    common_share_total,
                    farm_years,
                    accrual_worksheet_indicator,
                    completed_production_cycle_indicator,
                    cwb_worksheet_indicator,
                    perishable_commodities_indicator,
                    receipts_indicator,
                    accrual_cash_conversion_indicator,
                    combined_farm_indicator,
                    coop_member_indicator,
                    corporate_shareholder_indicator,
                    disaster_indicator,
                    partnership_member_indicator,
                    sole_proprietor_indicator,
                    other_text,
                    post_mark_date,
                    province_of_residence,
                    received_date,
                    last_year_farming_indicator,
                    can_send_cob_to_representative_indicator,
                    province_of_main_farmstead,
                    locally_updated_indicator,
                    program_year_id,
                    participant_profile_code,
                    municipality_code,
                    import_version_id,
                    federal_status_code,
                    revision_count,
                    create_user,
                    create_date,
                    update_user,
                    update_date
                ) values (
                    pyv_id,
                    coalesce(z02_val.prev_py_version_number, 0) + 1,
                    z02_val.form_version_number,
                    z02_val.form_version_effective_date,
                    z02_val.common_share_total,
                    z02_val.farm_years,
                    z02_val.accrual_worksheet_indicator,
                    z02_val.completed_production_cycle_indicator,
                    z02_val.cwb_worksheet_indicator,
                    z02_val.perishable_commodities_indicator,
                    z02_val.receipts_indicator,
                    z02_val.accrual_cash_conversion_indicator,
                    z02_val.combined_this_year_indicator,
                    z02_val.coop_member_indicator,
                    z02_val.corporate_shareholder_indicator,
                    z02_val.disaster_indicator,
                    z02_val.partnership_member_indicator,
                    z02_val.sole_proprietor_indicator,
                    z02_val.other_text,
                    pmd,
                    z02_val.province_of_residence,
                    rd,
                    z02_val.last_year_farming_indicator,
                    z02_val.copy_cob_to_contact_indicator,
                    z02_val.province_of_main_farmstead,
                    'N',
                    py_id,
                    ppc,
                    mc,
                    in_version_id,
                    coalesce(z02_val.agristability_status, 3)::varchar,
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );

                -- need to do both if a new version was inserted
                error_msg := farms_import_pkg.operation(pyv_id, in_user);
                if error_msg is null then
                    error_msg := farms_import_pkg.scenario(py_id, pyv_id, z02_val.program_year_version_id, in_user);
                end if;
                if error_msg is null then
                    error_msg := farms_import_pkg.reported_income_expense(py_id, pyv_id, in_user);
                end if;
                if error_msg is null then
                    error_msg := farms_import_pkg.reported_inventory(py_id, in_user);
                end if;
                if error_msg is null then
                    error_msg := farms_import_pkg.productive_unit_capacity(pyv_id, in_user);
                end if;
                if error_msg is null then
                    error_msg := farms_import_pkg.supplemental_date(pyv_id);
                end if;
            end if;

            if error_msg is not null then
                call farms_import_pkg.append_impl1(
                    in_version_id,
                    '<ERROR>' || farms_import_pkg.scrub(error_msg) || '</ERROR>'
                );
                errors := errors + 1;
                raise exception '%', error_msg;
            end if;
        exception
            when others then
                rollback;
                call farms_import_pkg.append_impl1(
                    in_version_id,
                    '<ERROR>' ||
                    farms_import_pkg.scrub(farms_import_pkg.codify_program_year(
                        sqlerrm,
                        v_program_year,
                        in_agristability_client_id,
                        ppc,
                        v_agristability_status,
                        mc
                    )) ||
                    '</ERROR>'
                );
                errors := errors + 1;
                in_out_activity := in_out_activity + 1;
        end;

        if opened_py = 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</PROGRAM_YEAR>');
            closed_py := 1;
            opened_py := 0;
        end if;
        pyv_id := null;
        v_prev_program_year := v_program_year;
    end loop;

    if opened > 0 then
        call farms_import_pkg.append_imp1(in_version_id, '</PROGRAM_YEARS>');
    end if;
end;
$$;
