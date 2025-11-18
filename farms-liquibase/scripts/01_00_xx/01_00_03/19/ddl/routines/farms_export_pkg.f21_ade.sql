create or replace function farms_export_pkg.f21_ade(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        with scenarios as (
            select t.agristability_scenario_id,
                   t.program_year_version_id,
                   t.participant_pin,
                   t.program_year "Year"
            from (
                /* this subquery is duplicated in F01, F02, F03, F20, F21, F30, F31, F40, F60.
                 * if it is modified here, it should be modified there as well.
                 */
                select m.program_year_version_id,
                       sc.agristability_scenario_id,
                       m.participant_pin,
                       m.year program_year,
                       sc.combined_farm_number,
                       ssa.scenario_state_audit_id,
                       min(sc.agristability_scenario_id) keep(dense_rank first order by
                           case
                               when sc.scenario_class_code = 'USER'
                                    and m.year = 2013 then 2
                               when sc.scenario_class_code = 'REF'
                                    and m_parent.year = 2013 then 1
                               else 0
                           end desc,
                           ssa.when_created desc,
                           case when(sc.scenario_state_code = 'COMP') then 0 else 1 end,
                           case when(sc.scenario_state_code = 'AMEND') then 0 else 1 end,
                           sc.scenario_number desc
                       ) over (partition by m.program_year_id) latest_sc_id,
                       first_value(ssa.scenario_state_audit_id) over (
                           partition by sc.agristability_scenario_id
                           order by ssa.when_created desc nulls last
                       ) verified_state_id,
                       first_value(py.non_participant_ind) over (
                           partition by m.agristability_client_id
                           order by m.year desc
                       ) non_participant_ind
                from farms.farm_agri_scenarios_vw m
                join farms.farm_agristability_scenarios sc on m.agristability_scenario_id = sc.agristability_scenario_id
                join farms.farm_program_years py on py.program_year_id = m.program_year_id
                left outer join farms.farm_reference_scenarios rf on rf.agristability_scenario_id = sc.agristability_scenario_id
                left outer join farms.farm_agri_scenarios_vw m_parent on m_parent.agristability_scenario_id = rf.for_agristability_scenario_id
                left outer join farms.farm_scenario_state_audits ssa on ssa.agristability_scenario_id = sc.agristability_scenario_id
                                                                     and ssa.scenario_state_code = 'COMP'
                where m.year between(in_program_year - 5) and in_program_year
                and sc.scenario_class_code in ('USER', 'REF')
                and sc.scenario_state_code in ('COMP', 'AMEND')
                and sc.scenario_category_code in ('FIN','AADJ','PADJ')
            ) t
            where t.agristability_scenario_id = t.latest_sc_id
            and (t.scenario_state_audit_id = t.verified_state_id or coalesce(t.verified_state_id::text, '') = '')
            and t.non_participant_ind = 'N'
        )
        select i.participant_pin,
               in_program_year "Year",
               i.prior_year,
               i.operation_number,
               row_number() over (
                   partition by i.participant_pin, i.prior_year
                   order by i.agristabilty_cmmdty_xref_id, i.reported_inventory_id, i.productve_unit_capacity_id
               ) sequence_number,
               i.inventory_item_code,
               i.inventory_class_code,
               on_farm_acres,
               unseedable_acres,
               case
                   when i.inventory_class_code in ('1','2') then
                        case
                            when coalesce(cra_quantity_start::text, '') = '' then adj_quantity_start
                            when coalesce(adj_quantity_start::text, '') = '' then cra_quantity_start
                            else cra_quantity_start + adj_quantity_start
                        end
                   else
                        case
                            when coalesce(cra_start_of_year_amount::text, '') = '' then adj_start_of_year_amount
                            when coalesce(adj_start_of_year_amount::text, '') = '' then cra_start_of_year_amount
                            else cra_start_of_year_amount + adj_start_of_year_amount
                        end
               end quantity_start,
               case
                   when coalesce(cra_price_start::text, '') = '' then adj_price_start
                   when coalesce(adj_price_start::text, '') = '' then cra_price_start
                   else cra_price_start + adj_price_start
               end price_start,
               case
                   when i.inventory_class_code in ('1','2') then
                        case
                            when coalesce(cra_quantity_end::text, '') = '' then adj_quantity_end
                            when coalesce(adj_quantity_end::text, '') = '' then cra_quantity_end
                            else cra_quantity_end + adj_quantity_end
                        end
                   else
                        case
                            when coalesce(cra_end_of_year_amount::text, '') = '' then adj_end_of_year_amount
                            when coalesce(adj_end_of_year_amount::text, '') = '' then cra_end_of_year_amount
                            else cra_end_of_year_amount + adj_end_of_year_amount
                        end
               end quantity_end,
               case
                   when coalesce(cra_price_end::text, '') = '' then adj_price_end
                   when coalesce(adj_price_end::text, '') = '' then cra_price_end
                   else cra_price_end + adj_price_end
               end price_end,
               -- crops
               case
                   when i.inventory_class_code = '1' then cra_quantity_produced
               end crop_qty_produced,
               case
                   when i.inventory_class_code = '1' then adj_quantity_produced
               end crop_qty_adj,
               case
                   when i.inventory_class_code = '1' then i.crop_unit_code
               end crop_unit_code,
               case
                   when i.inventory_class_code = '2' then cra_quantity_produced
               end livestock_head_cnt_birth,
               case
                   when i.inventory_class_code = '2' then adj_quantity_produced
               end livestock_head_cnt_adj,
               case
                   when coalesce(cra_productive_capacity_amount::text, '') = '' then adj_productive_capacity_amount
                   when coalesce(adj_productive_capacity_amount::text, '') = '' then cra_productive_capacity_amount
                   else cra_productive_capacity_amount + adj_productive_capacity_amount
               end productive_capacity_amount
        from (
            select s.participant_pin,
                   s.year prior_year,
                   op.operation_number,
                   ri.crop_unit_code,
                   xref.inventory_item_code,
                   xref.inventory_class_code,
                   xref.agristabilty_cmmdty_xref_id,
                   ri.reported_inventory_id,
                   null productve_unit_capacity_id,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.quantity_produced end cra_quantity_produced,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.quantity_start end cra_quantity_start,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.quantity_end end cra_quantity_end,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.price_start end cra_price_start,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.price_end end cra_price_end,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.start_of_year_amount end cra_start_of_year_amount,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then ri.end_of_year_amount end cra_end_of_year_amount,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.quantity_produced else ri.quantity_produced end adj_quantity_produced,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.quantity_start else ri.quantity_start end adj_quantity_start,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.quantity_end else ri.quantity_end end adj_quantity_end,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.price_start else ri.price_start end adj_price_start,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.price_end else ri.price_end end adj_price_end,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.start_of_year_amount else ri.start_of_year_amount end adj_start_of_year_amount,
                   case when coalesce(ri.agristability_scenario_id::text, '') = '' then adj.end_of_year_amount else ri.end_of_year_amount end adj_end_of_year_amount,
                   ri.on_farm_acres,
                   ri.unseedable_acres,
                   case when coalesce(ca.agristability_scenario_id::text, '') = '' then ca.productive_capacity_amount  end cra_productive_capacity_amount,
                   case when coalesce(ca.agristability_scenario_id::text, '') = '' then cadj.productive_capacity_amount else ca.productive_capacity_amount end adj_productive_capacity_amount
            from scenarios s
            join farms.farm_farming_operations op on op.program_year_version_id = s.program_year_version_id
            join farms.farm_reported_inventories ri on ri.farming_operation_id = op.farming_operation_id
                                                    and (
                                                        coalesce(ri.agristability_scenario_id::text, '') = ''
                                                        or (
                                                            coalesce(ri.cra_reported_inventory_id::text, '') = ''
                                                            and ri.agristability_scenario_id = s.agristability_scenario_id
                                                        )
                                                    )
            left outer join farms.farm_reported_inventories adj on coalesce(ri.agristability_scenario_id::text, '') = ''
                                                                and adj.cra_reported_inventory_id = ri.reported_inventory_id
                                                                and adj.agristability_scenario_id = s.agristability_scenario_id
            join farms.farm_agristabilty_cmmdty_xref xref on ri.agristabilty_cmmdty_xref_id = xref.agristabilty_cmmdty_xref_id
            left outer join farms.farm_productve_unit_capacities ca on ca.farming_operation_id = op.farming_operation_id
                                                                    and (
                                                                        coalesce(ca.agristability_scenario_id::text, '') = ''
                                                                        or (
                                                                            coalesce(ca.cra_productve_unit_capacity_id::text, '') = ''
                                                                            and ca.agristability_scenario_id = s.agristability_scenario_id
                                                                        )
                                                                    )
                                                                    and ca.inventory_item_code = xref.inventory_item_code
            left outer join farms.farm_productve_unit_capacities cadj on coalesce(ca.agristability_scenario_id::text, '') = ''
                                                                      and cadj.cra_productve_unit_capacity_id = ca.productve_unit_capacity_id
                                                                      and cadj.agristability_scenario_id = s.agristability_scenario_id
            where (
                (xref.inventory_class_code = '1' and (ri.quantity_produced != 0 or adj.quantity_produced != 0))
                or (xref.inventory_class_code in ('1','2') and (ri.quantity_start != 0 or adj.quantity_start != 0))
                or (xref.inventory_class_code in ('1','2') and (ri.quantity_end != 0 or adj.quantity_end != 0))
                or (xref.inventory_class_code in ('1','2') and (ri.price_start != 0 or adj.price_start != 0))
                or (xref.inventory_class_code in ('1','2') and (ri.price_end != 0 or adj.price_end != 0))
                or (xref.inventory_class_code in ('3','4','5') and (ri.start_of_year_amount != 0 or adj.start_of_year_amount != 0))
                or (xref.inventory_class_code in ('3','4','5') and (ri.end_of_year_amount != 0 or adj.end_of_year_amount != 0))
            )
            union all
            select s.participant_pin,
                   s.year prior_year,
                   op.operation_number,
                   null crop_unit_code,
                   ca.inventory_item_code,
                   null inventory_class_code,
                   null agristabilty_cmmdty_xref_id,
                   null reported_inventory_id,
                   ca.productve_unit_capacity_id,
                   null cra_quantity_produced,
                   null cra_quantity_start,
                   null cra_quantity_end,
                   null cra_price_start,
                   null cra_price_end,
                   null cra_start_of_year_amount,
                   null cra_end_of_year_amount,
                   null adj_quantity_produced,
                   null adj_quantity_start,
                   null adj_quantity_end,
                   null adj_price_start,
                   null adj_price_end,
                   null adj_start_of_year_amount,
                   null adj_end_of_year_amount,
                   null on_farm_acres,
                   null unseedable_acres,
                   case when coalesce(ca.agristability_scenario_id::text, '') = '' then ca.productive_capacity_amount end cra_productive_capacity_amount,
                   case when coalesce(ca.agristability_scenario_id::text, '') = '' then cadj.productive_capacity_amount else ca.productive_capacity_amount end adj_productive_capacity_amount
            from scenarios s
            join farms.farm_farming_operations op on op.program_year_version_id = s.program_year_version_id
            join farms.farm_productve_unit_capacities ca on ca.farming_operation_id = op.farming_operation_id
                                                         and (
                                                             coalesce(ca.agristability_scenario_id::text, '') = ''
                                                             or (
                                                                 coalesce(ca.cra_productve_unit_capacity_id::text, '') = ''
                                                                 and ca.agristability_scenario_id = s.agristability_scenario_id
                                                             )
                                                         )
            left outer join farms.farm_productve_unit_capacities cadj on coalesce(ca.agristability_scenario_id::text, '') = ''
                                                                      and cadj.cra_productve_unit_capacity_id = ca.productve_unit_capacity_id
                                                                      and cadj.agristability_scenario_id = s.agristability_scenario_id
            where not exists (
                select *
                from farms.farm_agri_scenarios_vw m
                join farms.farm_farming_operations fo on fo.program_year_version_id = m.program_year_version_id
                join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
                                                        and (
                                                            coalesce(ri.agristability_scenario_id::text, '') = ''
                                                            or (
                                                                coalesce(ri.cra_reported_inventory_id::text, '') = ''
                                                                and ri.agristability_scenario_id = m.agristability_scenario_id
                                                            )
                                                        )
                join farms.farm_agristabilty_cmmdty_xref xref on ri.agristabilty_cmmdty_xref_id = xref.agristabilty_cmmdty_xref_id
                left outer join farms.farm_reported_inventories adj on coalesce(ri.agristability_scenario_id::text, '') = ''
                                                                    and adj.cra_reported_inventory_id = ri.reported_inventory_id
                                                                    and adj.agristability_scenario_id = m.agristability_scenario_id
                where ca.inventory_item_code = xref.inventory_item_code
                and m.agristability_scenario_id = s.agristability_scenario_id
                and (
                    (xref.inventory_class_code = '1' and (ri.quantity_produced != 0 or adj.quantity_produced != 0))
                    or (xref.inventory_class_code in ('1','2') and (ri.quantity_start != 0 or adj.quantity_start != 0))
                    or (xref.inventory_class_code in ('1','2') and (ri.quantity_end != 0 or adj.quantity_end != 0))
                    or (xref.inventory_class_code in ('1','2') and (ri.price_start != 0 or adj.price_start != 0))
                    or (xref.inventory_class_code in ('1','2') and (ri.price_end != 0 or adj.price_end != 0))
                )
                and xref.inventory_class_code in ('1','2')
            )
            and (ca.productive_capacity_amount != 0 or cadj.productive_capacity_amount != 0)
            and (ca.inventory_item_code is not null and ca.inventory_item_code::text <> '')
        ) i
        order by participant_pin,
                 prior_year,
                 operation_number,
                 sequence_number;

    return cur;
end;
$$;
