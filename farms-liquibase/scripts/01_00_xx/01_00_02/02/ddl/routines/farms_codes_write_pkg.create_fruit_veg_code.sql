create or replace procedure farms_codes_write_pkg.create_fruit_veg_code(
   in in_fruit_veg_code farms.farm_fruit_veg_type_codes.fruit_veg_type_code%type,
   in in_fruit_veg_code_description farms.farm_fruit_veg_type_codes.description%type,
   in in_fruit_veg_code_variance_limit farms.farm_fruit_veg_type_details.revenue_variance_limit%type,
   in in_farm_user farms.farm_fruit_veg_type_codes.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_fruit_veg_type_codes (
        fruit_veg_type_code,
        description,
        who_created,
        when_created,
        established_date,
        who_updated,
        when_updated,
        revision_count,
        expiry_date
    ) values (
        in_fruit_veg_code,
        in_fruit_veg_code_description,
        in_farm_user,
        current_timestamp,
        current_date,
        in_farm_user,
        current_timestamp,
        1,
        to_date('9999-12-31', 'YYYY-MM-DD')
    );

    insert into farms.farm_fruit_veg_type_details (
        fruit_veg_type_detail_id,
        fruit_veg_type_code,
        revenue_variance_limit,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        nextval('farms.seq_fvtd'),
        in_fruit_veg_code,
        in_fruit_veg_code_variance_limit,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp,
        1
    );

end;
$$;
