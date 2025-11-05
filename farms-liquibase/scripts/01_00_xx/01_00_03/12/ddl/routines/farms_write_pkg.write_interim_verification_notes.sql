create or replace function farms_write_pkg.write_interim_verification_notes(
    in in_py_id farms.farm_program_years.program_year_id%type,
    in in_user_id farms.farm_benefit_calc_totals.who_updated%type
) returns refcursor
language plpgsql
as
$$
declare

    v_rows_affected  bigint := null;
    v_cursor refcursor;

begin
    update farms.farm_program_years
    set interim_verification_notes = '',
        revision_count = revision_count + 1,
        who_updated = in_user_id,
        when_updated = current_timestamp
    where program_year_id = in_py_id;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;

    open v_cursor for
        select py.interim_verification_notes
        from farms.farm_program_years py
        where py.program_year_id = in_py_id
        for update;
    return v_cursor;
end;
$$;
