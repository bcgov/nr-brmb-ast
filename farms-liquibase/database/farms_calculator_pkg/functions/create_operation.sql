create or replace function farms_calculator_pkg.create_operation(
   in in_program_year_version_id farms.farm_farming_operations.program_year_version_id%type,
   in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
   in in_operation_number farms.farm_farming_operations.operation_number%type,
   in in_alignment_key farms.farm_farming_operations.alignment_key%type,
   in in_federal_accounting_code farms.farm_farming_operations.federal_accounting_code%type,
   in in_fiscal_year_start farms.farm_farming_operations.fiscal_year_start%type,
   in in_fiscal_year_end farms.farm_farming_operations.fiscal_year_end%type,
   in in_partnership_pin farms.farm_farming_operations.partnership_pin%type,
   in in_partnership_name farms.farm_farming_operations.partnership_name%type,
   in in_partnership_percent farms.farm_farming_operations.partnership_percent%type,
   in in_crop_disaster_ind farms.farm_farming_operations.crop_disaster_ind%type,
   in in_crop_share_ind farms.farm_farming_operations.crop_share_ind%type,
   in in_feeder_member_ind farms.farm_farming_operations.feeder_member_ind%type,
   in in_landlord_ind farms.farm_farming_operations.landlord_ind%type,
   in in_livestock_disaster_ind farms.farm_farming_operations.livestock_disaster_ind%type,
   in in_business_use_home_expense farms.farm_farming_operations.business_use_home_expense%type,
   in in_expenses farms.farm_farming_operations.expenses%type,
   in in_gross_income farms.farm_farming_operations.gross_income%type,
   in in_inventory_adjustments farms.farm_farming_operations.inventory_adjustments%type,
   in in_net_farm_income farms.farm_farming_operations.net_farm_income%type,
   in in_net_income_after_adj farms.farm_farming_operations.net_income_after_adj%type,
   in in_net_income_before_adj farms.farm_farming_operations.net_income_before_adj%type,
   in in_other_deductions farms.farm_farming_operations.other_deductions%type,
   in in_user farms.farm_farming_operations.who_updated%type
) returns farms.farm_farming_operations.farming_operation_id%type
language plpgsql
as
$$
declare

    v_op_id farms.farm_farming_operations.farming_operation_id%type;
    v_op_num farms.farm_farming_operations.operation_number%type;
    v_has_user_scenarios varchar(1);

begin

    select case when exists (select *
                             from farms.farm_agristability_scenarios sc
                             where sc.scenario_class_code = 'USER'
                               and sc.program_year_version_id = in_program_year_version_id)
                then 'Y'
                else 'N'
                end
    into v_has_user_scenarios;

    if v_has_user_scenarios = 'Y' then
      call farms_calculator_pkg.close_pyv_other_scenarios(
        in_program_year_version_id,
        in_scenario_id,
        'Scenario was invalidated when a locally generated operation was created.',
        in_user);
    end if;

    select nextval('farms.farm_fo_seq')
    into v_op_id;

    if coalesce(in_operation_number::text, '') = '' then
      select coalesce(max(fo.operation_number), 0) + 1
      into strict v_op_num
      from farms.farm_farming_operations fo
    where fo.program_year_version_id = in_program_year_version_id;
    else
      v_op_num := in_operation_number;
    end if;

    insert into farms.farm_farming_operations(
      farming_operation_id,
      business_use_home_expense,
      expenses,
      fiscal_year_end,
      fiscal_year_start,
      gross_income,
      inventory_adjustments,
      crop_share_ind,
      feeder_member_ind,
      landlord_ind,
      net_farm_income,
      net_income_after_adj,
      net_income_before_adj,
      other_deductions,
      partnership_name,
      partnership_percent,
      partnership_pin,
      operation_number,
      alignment_key,
      crop_disaster_ind,
      livestock_disaster_ind,
      locally_updated_ind,
      locally_generated_ind,
      federal_accounting_code,
      program_year_version_id,
      revision_count,
      who_created,
      when_created,
      who_updated,
      when_updated)
    values (
      v_op_id,
      in_business_use_home_expense,
      in_expenses,
      in_fiscal_year_end,
      in_fiscal_year_start,
      in_gross_income,
      in_inventory_adjustments,
      in_crop_share_ind,
      in_feeder_member_ind,
      in_landlord_ind,
      in_net_farm_income,
      in_net_income_after_adj,
      in_net_income_before_adj,
      in_other_deductions,
      in_partnership_name,
      in_partnership_percent,
      in_partnership_pin,
      v_op_num,
      in_alignment_key,
      in_crop_disaster_ind,
      in_livestock_disaster_ind,
      'Y',
      'Y',
      in_federal_accounting_code,
      in_program_year_version_id,
      1,
      in_user,
      current_timestamp,
      in_user,
      current_timestamp
    );

    return v_op_id;
end;
$$;
