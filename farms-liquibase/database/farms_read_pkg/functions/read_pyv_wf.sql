create or replace function farms_read_pkg.read_pyv_wf(
    in pyv_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select whole_farm_participant_id,
               whole_farm_combined_pin,
               whole_farm_comb_pin_add_ind,
               whole_farm_comb_pin_remove_ind,
               program_year_version_id,
               revision_count
        from farms.farm_whole_farm_participants p
        where p.program_year_version_id = any(pyv_ids);
    return cur;
end;
$$;
