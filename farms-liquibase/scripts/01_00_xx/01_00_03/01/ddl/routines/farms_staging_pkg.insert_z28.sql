create or replace procedure farms_staging_pkg.insert_z28(
   in in_production_unit farms.z28_production_insurance_reference.production_unit%type,
   in in_production_unit_description farms.z28_production_insurance_reference.production_unit_description%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z28_production_insurance_reference (
        production_unit,
        production_unit_description,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_production_unit,
        in_production_unit_description,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
