create or replace procedure farms_adjustment_pkg.adjust_inv(
    in in_action text,
    in in_adj_id farms.farm_reported_inventories.reported_inventory_id%type,
    in in_reported_id farms.farm_reported_inventories.cra_reported_inventory_id%type,
    in in_farming_operation_id farms.farm_reported_inventories.farming_operation_id%type,
    in in_scenario_id farms.farm_reported_inventories.agristability_scenario_id%type,
    in in_parent_scenario_id farms.farm_reported_inventories.agristability_scenario_id%type,
    in in_price_start farms.farm_reported_inventories.price_start%type,
    in in_price_end farms.farm_reported_inventories.price_end%type,
    in in_end_year_producer_price farms.farm_reported_inventories.end_year_producer_price%type,
    in in_quantity_start farms.farm_reported_inventories.quantity_start%type,
    in in_quantity_end farms.farm_reported_inventories.quantity_end%type,
    in in_start_of_year_amount farms.farm_reported_inventories.start_of_year_amount%type,
    in in_end_of_year_amount farms.farm_reported_inventories.end_of_year_amount%type,
    in in_quantity_produced farms.farm_reported_inventories.quantity_produced%type,
    in in_crop_unit_code farms.farm_reported_inventories.crop_unit_code%type,
    in in_on_farm_acres farms.farm_reported_inventories.on_farm_acres%type,
    in in_unseedable_acres farms.farm_reported_inventories.unseedable_acres%type,
    in in_cmmdty_xref_id farms.farm_reported_inventories.agristabilty_cmmdty_xref_id%type,
    in in_revision_count farms.farm_reported_inventories.revision_count%type,
    in in_inventory_item_code farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    in in_inventory_class_code farms.farm_agristabilty_cmmdty_xref.inventory_class_code%type,
    in in_user farms.farm_reported_inventories.who_updated%type
)
language plpgsql
as
$$
declare

    xref_count bigint;
    xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type;

begin

    if in_action = 'ADD' then
        if (in_cmmdty_xref_id is not null and in_cmmdty_xref_id::text <> '') then
            xref_id := in_cmmdty_xref_id;
        else
            select count(x.agristabilty_cmmdty_xref_id)
            into xref_count
            from farms.farm_agristabilty_cmmdty_xref x
            where x.inventory_item_code = in_inventory_item_code
            and x.inventory_class_code = in_inventory_class_code;

            if xref_count = 1 then
                select x.agristabilty_cmmdty_xref_id
                into xref_id
                from farms.farm_agristabilty_cmmdty_xref x
                where x.inventory_item_code = in_inventory_item_code
                and x.inventory_class_code = in_inventory_class_code;

            elsif xref_count = 0 then
                raise exception '%', farms_types_pkg.xref_not_found_msg()
                using errcode = farms_types_pkg.xref_not_found_num()::text;
            else
                raise exception '%', farms_types_pkg.xref_more_than_one_found_msg()
                using errcode = farms_types_pkg.xref_more_than_one_found_num()::text;
            end if;

        end if;

        insert into farms.farm_reported_inventories (
            reported_inventory_id,
            price_start,
            price_end,
            end_year_producer_price,
            quantity_start,
            quantity_end,
            quantity_produced,
            start_of_year_amount,
            end_of_year_amount,
            agristabilty_cmmdty_xref_id,
            crop_unit_code,
            on_farm_acres,
            unseedable_acres,
            farming_operation_id,
            agristability_scenario_id,
            who_created,
            who_updated,
            when_updated,
            cra_reported_inventory_id
        ) values (
            nextval('farms.farm_ri_seq'),
            in_price_start,
            in_price_end,
            in_end_year_producer_price,
            in_quantity_start,
            in_quantity_end,
            in_quantity_produced,
            in_start_of_year_amount,
            in_end_of_year_amount,
            xref_id,
            in_crop_unit_code,
            in_on_farm_acres,
            in_unseedable_acres,
            in_farming_operation_id,
            in_scenario_id,
            in_user,
            in_user,
            current_timestamp,
            in_reported_id
        );

        elsif in_action = 'DELETE' then

            delete from farms.farm_reported_inventories ri
            where ri.reported_inventory_id = in_adj_id;

        elsif in_action = 'UPDATE' then

            update farms.farm_reported_inventories
            set price_start = in_price_start,
                price_end = in_price_end,
                end_year_producer_price = in_end_year_producer_price,
                quantity_start = in_quantity_start,
                quantity_end = in_quantity_end,
                quantity_produced = in_quantity_produced,
                start_of_year_amount = in_start_of_year_amount,
                end_of_year_amount = in_end_of_year_amount,
                who_updated = in_user,
                when_updated = clock_timestamp(),
                revision_count = revision_count + 1
            where reported_inventory_id = in_adj_id
            and revision_count = in_revision_count;

        end if;

        if (in_scenario_id is not null and in_scenario_id::text <> '') then
            insert into farms.farm_scenario_logs (
                scenario_log_id,
                log_message,
                agristability_scenario_id,
                who_created,
                who_updated)
            select nextval('farms.farm_sl_seq'),
                   '[' || case
                              when x.inventory_class_code='1' then 'Crop'
                              when x.inventory_class_code='2' then 'Livestock'
                              when x.inventory_class_code='3' then 'Purchased Input'
                              when x.inventory_class_code='4' then 'Receivable'
                              when x.inventory_class_code='5' then 'Payable'
                          end
                   || ' Adjustment] Code: "'
                   || iic.inventory_item_code
                   || ' - '
                   || iic.description
                   || '", Year: '
                   || m.year
                   || case
                          when in_action = 'DELETE' then ', Adjustment Deleted'
                          else ', Adjustments: '
                               || case
                                      when x.inventory_class_code = '1' then 'Qty Produced: ' || round((in_quantity_produced)::numeric, 3) || ', '
                                  end
                               || case
                                      when x.inventory_class_code in ('1','2') then 'Qty Start: ' || round((in_quantity_start)::numeric, 3)
                                      || ', Price Start: ' || round((in_price_start)::numeric, 2)
                                      || ', Qty End: ' || round((in_quantity_end)::numeric, 3)
                                      || ', Price End: ' || round((in_price_end)::numeric, 2)
                                      when x.inventory_class_code in ('3','4','5') then 'Start Value: ' || round((in_start_of_year_amount)::numeric, 3)
                                      || ', End Value: ' || round((in_end_of_year_amount)::numeric, 3)
                                  end
                      end
                   log_message,
                   in_parent_scenario_id,
                   in_user,
                   in_user
            from farms.farm_scenarios_vw m
            join farms.farm_agristabilty_cmmdty_xref x on x.inventory_item_code = in_inventory_item_code
                                                       and x.inventory_class_code = in_inventory_class_code
            join farms.farm_inventory_item_codes iic on iic.inventory_item_code = x.inventory_item_code
            where m.agristability_scenario_id = in_scenario_id;
    end if;

end;
$$;
