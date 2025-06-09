create or replace procedure farms_codes_write_pkg.create_fruit_veg_code(
   in in_fruit_veg_code farms.fruit_vegetable_type_code.fruit_vegetable_type_code%type,
   in in_fruit_veg_code_description farms.fruit_vegetable_type_code.description%type,
   in in_fruit_veg_code_variance_limit farms.fruit_vegetable_type_detail.revenue_variance_limit%type,
   in in_farm_user farms.fruit_vegetable_type_code.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.fruit_vegetable_type_code (
        fruit_vegetable_type_code,
        description,
        create_user,
        create_date,
        effective_date,
        update_user,
        update_date,
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

    insert into farms.fruit_vegetable_type_detail (
        fruit_vegetable_type_detail_id,
        fruit_vegetable_type_code,
        revenue_variance_limit,
        create_user,
        create_date,
        update_user,
        update_date,
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
