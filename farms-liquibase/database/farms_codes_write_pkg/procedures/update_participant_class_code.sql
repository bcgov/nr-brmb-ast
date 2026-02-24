create or replace procedure farms_codes_write_pkg.update_participant_class_code(
   in in_participant_class_code farms.farm_participant_class_codes.participant_class_code%type,
   in in_description farms.farm_participant_class_codes.description%type,
   in in_effective_date farms.farm_participant_class_codes.established_date%type,
   in in_expiry_date farms.farm_participant_class_codes.expiry_date%type,
   in in_revision_count farms.farm_participant_class_codes.revision_count%type,
   in in_user farms.farm_participant_class_codes.who_updated%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
begin
    update farms.farm_participant_class_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where participant_class_code = in_participant_class_code
    and revision_count = in_revision_count;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
