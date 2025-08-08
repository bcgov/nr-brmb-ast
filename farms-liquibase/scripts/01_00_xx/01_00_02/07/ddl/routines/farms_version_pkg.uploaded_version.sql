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
    t_icc farms.farm_import_versions.import_class_code%type;
begin

    select import_class_code
    into t_icc
    from farms.farm_import_versions
    where import_version_id = in_version_id;

    update farms.farm_import_versions
    set import_state_code = (case when in_has_errors_ind = 'Y' then 'SF' else 'SC' end),
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where import_version_id = in_version_id;

    -- Keep import file and staging audit info for 2 years, then remove.
    -- Don't update When_Updated because that is used for ordering in the UI.
    update farms.farm_import_versions
    set staging_audit_info = null,
        import_audit_info = null,
        import_file = null
    where when_updated < (current_timestamp - interval '2 years')
    and (staging_audit_info is not null or
         import_audit_info is not null or
         import_file is not null);

    if t_icc = 'CRA' then
        analyze farms.farm_z99_extract_file_ctls;
        analyze farms.farm_z51_participant_contribs;
        analyze farms.farm_z50_participnt_bnft_calcs;
        analyze farms.farm_z42_participant_ref_years;
        analyze farms.farm_z40_prtcpnt_ref_supl_dtls;
        analyze farms.farm_z23_livestock_prod_cpcts;
        analyze farms.farm_z22_production_insurances;
        analyze farms.farm_z21_participant_suppls;
        analyze farms.farm_z28_prod_insurance_refs;
        analyze farms.farm_z29_inventory_code_refs;
        analyze farms.farm_z05_partner_infos;
        analyze farms.farm_z04_income_exps_dtls;
        analyze farms.farm_z03_statement_infos;
        analyze farms.farm_z02_partpnt_farm_infos;
        analyze farms.farm_z01_participant_infos;
    end if;

    open cur for
        select staging_audit_info
        from farms.farm_import_versions
        where import_version_id = in_version_id
        for update;

    return cur;
end;
$$;
