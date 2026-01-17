create or replace function farms_write_pkg.write_final_verification_notes(
    in in_py_id farms.farm_program_years.program_year_id%type,
    in in_user_id farms.farm_benefit_calc_totals.who_updated%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    --
    -- We aren't using the revision_count stuff correctly because
    -- several notes fields in FARM_PROGRAM_YEAR can be updated
    -- independently.
    --
    update farms.farm_program_years
    set final_verification_notes = '',
        revision_count = revision_count + 1,
        who_updated = in_user_id,
        when_updated = current_timestamp
    where program_year_id = in_py_id;

    open v_cursor for
        select py.final_verification_notes
        from farms.farm_program_years py
        where py.program_year_id = in_py_id
        for update;
    return v_cursor;
end;
$$;
