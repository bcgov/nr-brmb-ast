create or replace function farms_import_pkg.program_year(
    in in_version_id numeric,
    in in_agristability_client_id farms.farm_agristability_clients.agristability_client_id%type,
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
               pyv.accrual_worksheet_ind p_accrual_worksheet_indicator,
               pyv.completed_prod_cycle_ind p_completed_production_cycle_indicator,
               pyv.cwb_worksheet_ind p_cwb_worksheet_indicator,
               pyv.perishable_commodities_ind p_perishable_commodities_indicator,
               pyv.receipts_ind p_receipts_indicator,
               pyv.accrual_cash_conversion_ind p_accrual_cash_conversion_indicator,
               pyv.combined_farm_ind p_combined_farm_indicator,
               pyv.coop_member_ind p_coop_member_indicator,
               pyv.corporate_shareholder_ind p_corporate_shareholder_indicator,
               pyv.disaster_ind p_disaster_indicator,
               pyv.partnership_member_ind p_partnership_member_indicator,
               pyv.sole_proprietor_ind p_sole_proprietor_indicator,
               pyv.other_text p_other_text,
               pyv.post_mark_date p_postmark_date,
               pyv.province_of_residence p_province_of_residence,
               pyv.received_date p_received_date,
               pyv.last_year_farming_ind p_last_year_farming_indicator,
               pyv.can_send_cob_to_rep_ind p_can_send_cob_to_representative_indicator,
               pyv.province_of_main_farmstead p_province_of_main_farmstead,
               pyv.locally_updated_ind p_locally_updated_indicator,
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
                   z.sole_proprietor_ind,
                   z.partnership_member_ind,
                   z.corporate_shareholder_ind,
                   z.coop_member_ind,
                   z.common_share_total,
                   z.farm_years,
                   z.last_year_farming_ind,
                   z.form_origin_code,
                   z.industry_code,
                   z.participant_profile_code,
                   z.accrual_cash_conversion_ind,
                   z.perishable_commodities_ind,
                   z.receipts_ind,
                   z.other_text_ind,
                   z.other_text,
                   z.accrual_worksheet_ind,
                   z.cwb_worksheet_ind,
                   z.combined_this_year_ind,
                   z.completed_prod_cycle_ind,
                   z.disaster_ind,
                   z.copy_cob_to_contact_ind,
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
                       from farms.farm_program_year_versions pyv
                       where py.program_year_id = pyv.program_year_id
                   ) prev_py_version_number
            from farms.farm_z02_partpnt_farm_infos z
            join farms.farm_agristability_clients ac on z.participant_pin = ac.participant_pin
            left outer join farms.farm_z50_participnt_bnft_calcs z50 on z.participant_pin = z50.participant_pin
                                                                          and z.program_year = z50.program_year
            left outer join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                                  and py.year = z.program_year
            left outer join farms.farm_municipality_codes fmc on z.municipality_code::varchar = fmc.municipality_code
            where ac.agristability_client_id = in_agristability_client_id
        ) a
        left outer join farms.farm_program_year_versions pyv on a.program_year_id = pyv.program_year_id
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

    v_program_year farms.farm_program_years.program_year_id%type := null;
    v_prev_program_year farms.farm_program_years.program_year_id%type := null;
    v_duplicate_year boolean := false;
    v_has_verified_scenario varchar(1);
    v_agristability_status farms.farm_program_year_versions.federal_status_code%type := null;
begin
    for z02_val in z02_cursor
    loop

        opened_py := 0;
        v_program_year := z02_val.program_year;
        v_agristability_status := z02_val.agristability_status;
        -- for warnings
        pmd := to_date(z02_val.postmark_date, 'YYYYMMDD');
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
                farms_import_pkg.scrub(z02_val.program_year::varchar) || '">'
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
                    farms_import_pkg.scrub(z02_val.program_year::varchar) || '" altered="true">'
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
                        farms_import_pkg.scrub(z02_val.program_year::varchar) || '" altered="true">'
                    );
                    opened_py := 1;
                    in_out_activity := in_out_activity + 1;
                end if;
            end if;

            if opened_py = 0 then
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<PROGRAM_YEAR action="none" year="' ||
                    farms_import_pkg.scrub(z02_val.program_year::varchar) || '" altered="false">'
                );
                opened_py := 1;
            end if;

            if mc = Unknown then
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<WARNING>' ||
                    farms_error_pkg.codify_mun_code(farms_import_pkg.scrub(z02_val.in_mun_cd::varchar)) ||
                    '</WARNING>'
                );
            end if;

            -- check PY existance, insert if not found
            if z02_val.program_year_id is null then
                select nextval('farms.farm_py_seq')
                into py_id;
                insert into farms.farm_program_years (
                    program_year_id,
                    year,
                    non_participant_ind,
                    late_participant_ind,
                    agristability_client_id,
                    farm_type_code,
                    revision_count,
                    who_created,
                    when_created,
                    who_updated,
                    when_updated
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
                            from farms.farm_program_years py
                            where py.agristability_client_id = z02_val.agristability_client_id
                            and py.year < z02_val.program_year
                            limit 1
                        ) t
                    ),
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );
            end if;

            if ch then

                select nextval('farms.farm_pyv_seq')
                into pyv_id;

                insert into farms.farm_program_year_versions (
                    program_year_version_id,
                    program_year_version_number,
                    form_version_number,
                    form_version_effective_date,
                    common_share_total,
                    farm_years,
                    accrual_worksheet_ind,
                    completed_prod_cycle_ind,
                    cwb_worksheet_ind,
                    perishable_commodities_ind,
                    receipts_ind,
                    accrual_cash_conversion_ind,
                    combined_farm_ind,
                    coop_member_ind,
                    corporate_shareholder_ind,
                    disaster_ind,
                    partnership_member_ind,
                    sole_proprietor_ind,
                    other_text,
                    post_mark_date,
                    province_of_residence,
                    received_date,
                    last_year_farming_ind,
                    can_send_cob_to_rep_ind,
                    province_of_main_farmstead,
                    locally_updated_ind,
                    program_year_id,
                    participant_profile_code,
                    municipality_code,
                    import_version_id,
                    federal_status_code,
                    revision_count,
                    who_created,
                    when_created,
                    who_updated,
                    when_updated
                ) values (
                    pyv_id,
                    coalesce(z02_val.prev_py_version_number, 0) + 1,
                    z02_val.form_version_number,
                    z02_val.form_version_effective_date,
                    z02_val.common_share_total,
                    z02_val.farm_years,
                    z02_val.accrual_worksheet_ind,
                    z02_val.completed_prod_cycle_ind,
                    z02_val.cwb_worksheet_ind,
                    z02_val.perishable_commodities_ind,
                    z02_val.receipts_ind,
                    z02_val.accrual_cash_conversion_ind,
                    z02_val.combined_this_year_ind,
                    z02_val.coop_member_ind,
                    z02_val.corporate_shareholder_ind,
                    z02_val.disaster_ind,
                    z02_val.partnership_member_ind,
                    z02_val.sole_proprietor_ind,
                    z02_val.other_text,
                    pmd,
                    z02_val.province_of_residence,
                    rd,
                    z02_val.last_year_farming_ind,
                    z02_val.copy_cob_to_contact_ind,
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
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<ERROR>' || farms_import_pkg.scrub(error_msg) || '</ERROR>'
                );
                errors := errors + 1;
                raise exception '%', error_msg;
            end if;
        exception
            when others then
                call farms_import_pkg.append_imp1(
                    in_version_id,
                    '<ERROR>' ||
                    farms_import_pkg.scrub(farms_error_pkg.codify_program_year(
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
