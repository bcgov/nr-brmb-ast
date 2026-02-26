create or replace procedure farms_aarm_pkg.staging_to_operational(
   in in_import_version_id bigint,
   in in_user text
)
language plpgsql
as $$
declare

    v_xml varchar(2000);
    v_clob text := null;
    v_num_rows bigint;


begin
    -- wipe out the operational table
    delete from farms.farm_aarm_margins;

    -- update the staging p1 and p2 values
    call farms_aarm_pkg.update_prices();

    --
    -- populate the operational table
    --
    insert into farms.farm_aarm_margins(
      aarm_margin_id
      ,participant_pin
      ,program_year
      ,operation_number
      ,partner_percent
      ,inventory_type_code
      ,inventory_code
      ,inventory_description
      ,production_unit
      ,aarm_reference_p1_price
      ,aarm_reference_p2_price
      ,quantity_start
      ,quantity_end
      ,revision_count
      ,who_created
      ,when_created
      ,who_updated
      ,when_updated
    )
    select
      max(aarm.zaarm_margin_id),
      aarm.participant_pin,
      aarm.program_year,
      aarm.operation_number,
      max(aarm.partner_percent),
      aarm.inventory_type_code,
      aarm.inventory_code,
      max(aarm.inventory_description),
      aarm.production_unit,
      max(aarm.aarm_reference_p1_price),
      max(aarm.aarm_reference_p2_price),
      sum(case when coalesce(aarm.quantity_start::text, '') = '' then  0  else aarm.quantity_start end ),
      sum(case when coalesce(aarm.quantity_end::text, '') = '' then  0  else aarm.quantity_end end ),
      1,
      in_user,
      current_timestamp,
      in_user,
      current_timestamp
    from farms.farm_zaarm_margins aarm
    group by
      aarm.participant_pin,
      aarm.program_year,
      aarm.operation_number,
      aarm.inventory_type_code,
      aarm.inventory_code,
      aarm.production_unit;

    --
    -- create an xml summary and set the state to 'import complete'
    --
    select count(aarm_margin_id)
    into strict v_num_rows
    from farms.farm_aarm_margins;

    v_xml := '<IMPORT_LOG><NUM_TOTAL>' || v_num_rows ||
             '</NUM_TOTAL></IMPORT_LOG>';

    update farms.farm_import_versions
    set import_audit_info = v_xml,
        import_state_code = 'IC'
    where import_version_id = in_import_version_id;
end;
$$;
