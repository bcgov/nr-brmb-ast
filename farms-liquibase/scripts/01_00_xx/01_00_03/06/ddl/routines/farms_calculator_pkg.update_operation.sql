create or replace procedure farms_calculator_pkg.update_operation(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_operation_number farms.farm_farming_operations.operation_number%type,
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
    in in_revision_count farms.farm_farming_operations.revision_count%type,
    in in_user farms.farm_farming_operations.who_updated%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin
    update farms.farm_farming_operations op
    set op.operation_number = in_operation_number,
        op.federal_accounting_code = in_federal_accounting_code,
        op.fiscal_year_start = in_fiscal_year_start,
        op.fiscal_year_end = in_fiscal_year_end,
        op.partnership_pin = in_partnership_pin,
        op.partnership_name = in_partnership_name,
        op.partnership_percent = in_partnership_percent,
        op.crop_disaster_ind = in_crop_disaster_ind,
        op.crop_share_ind = in_crop_share_ind,
        op.feeder_member_ind = in_feeder_member_ind,
        op.landlord_ind = in_landlord_ind,
        op.livestock_disaster_ind = in_livestock_disaster_ind,
        op.business_use_home_expense = in_business_use_home_expense,
        op.expenses = in_expenses,
        op.gross_income = in_gross_income,
        op.inventory_adjustments = in_inventory_adjustments,
        op.net_farm_income = in_net_farm_income,
        op.net_income_after_adj = in_net_income_after_adj,
        op.net_income_before_adj = in_net_income_before_adj,
        op.other_deductions = in_other_deductions,
        op.locally_updated_ind = 'Y',
        op.revision_count = op.revision_count + 1,
        op.who_updated = in_user,
        op.when_updated = current_timestamp
    where op.farming_operation_id = in_farming_operation_id
    and op.revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;
end;
$$;
