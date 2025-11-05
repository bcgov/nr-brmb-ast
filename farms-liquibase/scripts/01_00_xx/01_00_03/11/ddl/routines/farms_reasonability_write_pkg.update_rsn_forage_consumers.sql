create or replace procedure farms_reasonability_write_pkg.update_rsn_forage_consumers(
    in in_reasonability_test_result_id farms.farm_rsn_forage_consumers.reasonability_test_result_id%type,
    in in_productive_capacity_amount farms.farm_rsn_forage_consumers.productive_capacity_amount%type,
    in in_quantity_consumed_per_unit farms.farm_rsn_forage_consumers.quantity_consumed_per_unit%type,
    in in_quantity_consumed farms.farm_rsn_forage_consumers.quantity_consumed%type,
    in in_structure_group_code farms.farm_rsn_forage_consumers.structure_group_code%type,
    in in_user farms.farm_rsn_forage_consumers.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_forage_consumers (
        rsn_forage_consumer_id,
        productive_capacity_amount,
        quantity_consumed_per_unit,
        quantity_consumed,
        structure_group_code,
        reasonability_test_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rfc_seq'),
        in_productive_capacity_amount,
        in_quantity_consumed_per_unit,
        in_quantity_consumed,
        in_structure_group_code,
        in_reasonability_test_result_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
