create or replace function farms_import_pkg.is_changed_bool(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.farm_program_year_versions.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
begin
    if farms_import_pkg.is_operation_changed(in_program_year_id, in_program_year_vrsn_prev_id) then
        return true;
    end if;

    if farms_import_pkg.is_ie_changed(in_program_year_id, in_program_year_vrsn_prev_id) then
        return true;
    end if;

    if farms_import_pkg.is_inv_changed(in_program_year_id, in_program_year_vrsn_prev_id) then
        return true;
    end if;

    if farms_import_pkg.is_claim_changed(in_program_year_id, in_program_year_vrsn_prev_id) then
        return true;
    end if;

    if farms_import_pkg.is_benefit_changed(in_program_year_id, in_program_year_vrsn_prev_id) then
        return true;
    end if;

    return farms_import_pkg.is_puc_changed(in_program_year_id, in_program_year_vrsn_prev_id);
end;
$$;
