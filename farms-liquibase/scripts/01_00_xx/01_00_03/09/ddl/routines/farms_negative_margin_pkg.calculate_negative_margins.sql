create or replace procedure farms_negative_margin_pkg.calculate_negative_margins(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_user farms.farm_negative_margins.who_created%type
)
language plpgsql
as
$$
begin
    execute $q$
    merge into farms.farm_negative_margins as o
    using (
        with ivpr as (
            select case when sv.year = 2024 then sv.year
                       when bct.bpu_lead_ind = 'N' then (sv.year - 1)
                       else sv.year
                   end ivpr_year
            from farms.farm_scenarios_vw sv
            join farms.farm_reference_scenarios rs on rs.for_agristability_scenario_id = sv.agristability_scenario_id
            join farms.farm_scenarios_vw rsv on rsv.agristability_scenario_id = rs.agristability_scenario_id
                                             and rsv.year = (sv.year - 1)
            join farms.farm_benefit_calc_totals bct on bct.agristability_scenario_id = rsv.agristability_scenario_id
            where sv.agristability_scenario_id = in_agristability_scenario_id
        ), a as (
            select nm.negative_margin_id,
                   fo.farming_operation_id,
                   acx.inventory_item_code,
                   sv.agristability_scenario_id,
                   nm.revision_count,
                   iic.description as inventory_desc,
                   nm.deductible_percentage,
                   iid.insurable_value required_insurable_value,
                   nm.insurable_value_purchased,
                   sum(ri.quantity_produced) as reported,
                   nm.guaranteed_prod_value,
                   nm.premiums_paid,
                   nm.claims_received,
                   iid.premium_rate
            from farms.farm_farming_operations fo
            join farms.farm_scenarios_vw sv on sv.program_year_version_id = fo.program_year_version_id
            join farms.farm_reported_inventories ri on fo.farming_operation_id = ri.farming_operation_id
                                                    and (
                                                        ri.agristability_scenario_id = sv.agristability_scenario_id
                                                        or coalesce(ri.agristability_scenario_id::text, '') = ''
                                                    )
            join farms.farm_agristabilty_cmmdty_xref acx on ri.agristabilty_cmmdty_xref_id = acx.agristabilty_cmmdty_xref_id
                                                         and acx.inventory_class_code in ('1','2')
            join farms.farm_inventory_item_codes iic on acx.inventory_item_code = iic.inventory_item_code
            join farms.farm_inventory_item_details iid on iic.inventory_item_code = iid.inventory_item_code
            left join farms.farm_negative_margins nm on fo.farming_operation_id = nm.farming_operation_id
                                                     and acx.inventory_item_code = nm.inventory_item_code
                                                     and nm.agristability_scenario_id = sv.agristability_scenario_id
            where fo.farming_operation_id = in_farming_operation_id
            and iid.program_year = coalesce((select ivpr_year from ivpr), sv.year)
            and (
                (iid.insurable_value is not null and iid.insurable_value::text <> '')
                or (iid.premium_rate is not null and iid.premium_rate::text <> '')
            )
            and sv.agristability_scenario_id = in_agristability_scenario_id
            group by nm.negative_margin_id,
                     fo.farming_operation_id,
                     acx.inventory_item_code,
                     sv.agristability_scenario_id,
                     nm.revision_count,
                     iic.description,
                     nm.deductible_percentage,
                     iid.insurable_value,
                     nm.insurable_value_purchased,
                     nm.guaranteed_prod_value,
                     nm.premiums_paid,
                     nm.claims_received,
                     iid.premium_rate
            having sum(ri.quantity_produced) != 0
        ), claims_calc as (
            select a.*,
                   (case
                       when(a.guaranteed_prod_value - a.reported) >= 0 then (a.guaranteed_prod_value - a.reported) * 0.7 * a.required_insurable_value
                       else 0
                   end) as est_claims_received
            from a
        ), premium_calc as (
            select cc.*,
                   cc.guaranteed_prod_value * 0.7 * cc.required_insurable_value * cc.premium_rate * 0.54 as required_premium
            from claims_calc cc
        ), mrp_calc as (
            select pc.*,
                   least(pc.required_premium * mrp.risk_charge_pct_premium / 100 + mrp.risk_charge_flat_amount + mrp.adjust_charge_flat_amount, 1000) as mrp_value
            from premium_calc pc
            left join farms.farm_market_rate_premium mrp on trunc(pc.required_premium) between mrp.min_total_premium_amount and mrp.max_total_premium_amount
        ), deemed_premium_calc as (
            select mc.*,
                   greatest(mc.required_premium + mc.mrp_value - mc.premiums_paid, 0) as deemed_premium
            from mrp_calc mc
        ), deemed_received_calc as (
            select dpc.*,
                   (case
                       when dpc.est_claims_received - dpc.deemed_premium - dpc.claims_received > 0 then dpc.est_claims_received - dpc.deemed_premium - dpc.claims_received
                       else 0
                   end) as deemed_received
            from deemed_premium_calc dpc
        ), deemed_pi_value_calc as (
            select drc.*,
                   (case
                       when drc.deductible_percentage > 30 or drc.required_insurable_value <> drc.insurable_value_purchased then drc.deemed_received
                       else 0
                   end) as deemed_pi_value
            from deemed_received_calc drc
        )
        select coalesce(dpvc.negative_margin_id, nm.negative_margin_id) negative_margin_id,
               coalesce(dpvc.farming_operation_id, nm.farming_operation_id) farming_operation_id,
               coalesce(dpvc.inventory_item_code, nm.inventory_item_code) inventory_item_code,
               coalesce(dpvc.agristability_scenario_id, nm.agristability_scenario_id) agristability_scenario_id,
               dpvc.revision_count,
               dpvc.inventory_desc,
               dpvc.deductible_percentage,
               dpvc.required_insurable_value,
               dpvc.insurable_value_purchased,
               dpvc.reported as reported_quantity,
               dpvc.guaranteed_prod_value,
               dpvc.premiums_paid,
               dpvc.claims_received,
               round((dpvc.deemed_received)::numeric, 2) as deemed_received,
               round((dpvc.deemed_pi_value)::numeric, 2) as deemed_pi_value,
               dpvc.premium_rate,
               round((dpvc.est_claims_received)::numeric, 2) as claims_calculation,
               round((dpvc.required_premium)::numeric, 2) as required_premium,
               round((dpvc.mrp_value)::numeric, 2) as market_rate_premium,
               round((dpvc.deemed_premium)::numeric, 2) as deemed_premium,
               case when coalesce(dpvc.farming_operation_id::text, '') = '' then 'Y' else 'N' end delete_row
        from deemed_pi_value_calc dpvc
        full outer join (
            select *
            from farms.farm_negative_margins
            where farming_operation_id = in_farming_operation_id
            and agristability_scenario_id = in_agristability_scenario_id
        ) nm on nm.inventory_item_code = dpvc.inventory_item_code
      ) n on o.farming_operation_id = in_farming_operation_id
          and o.agristability_scenario_id = in_agristability_scenario_id
          and o.farming_operation_id = n.farming_operation_id
          and o.inventory_item_code = n.inventory_item_code
          and o.agristability_scenario_id = n.agristability_scenario_id
      when matched then
          update set
              required_insurable_value = n.required_insurable_value,
              reported_quantity = n.reported_quantity,
              premium_rate = n.premium_rate,
              required_premium = n.required_premium,
              market_rate_premium = n.market_rate_premium,
              claims_calculation = n.claims_calculation,
              deemed_premium = n.deemed_premium,
              deemed_received = n.deemed_received,
              deemed_pi_value = n.deemed_pi_value,
              revision_count = revision_count + 1,
              who_updated = in_user,
              when_updated = current_timestamp

      when matched and delete_row = 'Y' then
          delete

      when not matched then
          insert (
              negative_margin_id,
              farming_operation_id,
              inventory_item_code,
              agristability_scenario_id,
              revision_count,
              deductible_percentage,
              required_insurable_value,
              insurable_value_purchased,
              reported_quantity,
              guaranteed_prod_value,
              premiums_paid,
              claims_received,
              deemed_received,
              deemed_pi_value,
              premium_rate,
              claims_calculation,
              required_premium,
              market_rate_premium,
              deemed_premium,
              who_created,
              when_created,
              who_updated,
              when_updated
          ) values (
              nextval('farms.farm_nm_seq'),
              n.farming_operation_id,
              n.inventory_item_code,
              n.agristability_scenario_id,
              1,
              n.deductible_percentage,
              n.required_insurable_value,
              n.insurable_value_purchased,
              n.reported_quantity,
              n.guaranteed_prod_value,
              n.premiums_paid,
              n.claims_received,
              n.deemed_received,
              n.deemed_pi_value,
              n.premium_rate,
              n.claims_calculation,
              n.required_premium,
              n.market_rate_premium,
              n.deemed_premium,
              in_user,
              current_timestamp,
              in_user,
              current_timestamp
          );
    $q$;
end;
$$;
