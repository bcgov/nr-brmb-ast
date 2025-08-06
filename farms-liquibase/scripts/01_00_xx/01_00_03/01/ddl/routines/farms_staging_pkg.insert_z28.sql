create or replace procedure farms_staging_pkg.insert_z28(
   in in_production_unit farms.farm_z28_prod_insurance_refs.production_unit%type,
   in in_production_unit_description farms.farm_z28_prod_insurance_refs.production_unit_description%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z28_prod_insurance_refs (
        production_unit,
        production_unit_description,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
