create or replace function farms_version_pkg.uploaded_version(
    in in_version_id numeric,
    in in_has_errors_ind varchar,
    in in_user varchar
)
returns refcursor
language plpgsql
as
$$
declare
    cur refcursor;
    t_icc farms.import_version.import_class_code%type;
begin

    select import_class_code
    into t_icc
    from farms.import_version
    where import_version_id = in_version_id;

    update farms.import_version
    set import_state_code = (case when in_has_errors_ind = 'Y' then 'SF' else 'SC' end),
        revision_count = revision_count + 1,
        update_user = in_user,
        update_date = current_timestamp
    where import_version_id = in_version_id;

    -- Keep import file and staging audit info for 2 years, then remove.
    -- Don't update When_Updated because that is used for ordering in the UI.
    update farms.import_version
    set staging_audit_information = null,
        import_audit_information = null,
        import_file = null
    where update_date < (current_timestamp - interval '2 years')
    and (staging_audit_information is not null or
         import_audit_information is not null or
         import_file is not null);

    if t_icc = 'CRA' then
        analyze farms.z99_extract_file;
        analyze farms.z51_participant_contribution;
        analyze farms.z50_participant_benefit_calculation;
        analyze farms.z42_participant_reference_year;
        analyze farms.z40_participant_reference_supplemental_detail;
        analyze farms.z23_livestock_production_capacity;
        analyze farms.z22_production_insurance;
        analyze farms.z21_participant_supplementary;
        analyze farms.z28_production_insurance_reference;
        analyze farms.z29_inventory_code_reference;
        analyze farms.z05_partner_information;
        analyze farms.z04_income_expenses_detail;
        analyze farms.z03_statement_information;
        analyze farms.z02_participant_farm_information;
        analyze farms.z01_participant_information;
    end if;

    open cur for
        select staging_audit_information
        from farms.import_version
        where import_version_id = in_version_id
        for update;

    return cur;
end;
$$;
