create or replace function farms_read_pkg.read_pyv_wf(
    in pyv_ids bigint[]
)
returns table(
    whole_farm_participant_id       farms.farm_whole_farm_participants.whole_farm_participant_id%type,
    whole_farm_combined_pin         farms.farm_whole_farm_participants.whole_farm_combined_pin%type,
    whole_farm_comb_pin_add_ind     farms.farm_whole_farm_participants.whole_farm_comb_pin_add_ind%type,
    whole_farm_comb_pin_remove_ind  farms.farm_whole_farm_participants.whole_farm_comb_pin_remove_ind%type,
    program_year_version_id         farms.farm_whole_farm_participants.program_year_version_id%type,
    revision_count                  farms.farm_whole_farm_participants.revision_count%type
)
language sql
as $$
    select whole_farm_participant_id,
           whole_farm_combined_pin,
           whole_farm_comb_pin_add_ind,
           whole_farm_comb_pin_remove_ind,
           program_year_version_id,
           revision_count
    from farms.farm_whole_farm_participants p
    where p.program_year_version_id = any(pyv_ids);
$$;
