create or replace procedure farms_codes_write_pkg.create_participant_class_code(
   in in_participant_class_code farms.participant_class_code.participant_class_code%type,
   in in_description farms.participant_class_code.description%type,
   in in_effective_date farms.participant_class_code.effective_date%type,
   in in_expiry_date farms.participant_class_code.expiry_date%type,
   in in_user farms.participant_class_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.participant_class_code (
        participant_class_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        in_participant_class_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
