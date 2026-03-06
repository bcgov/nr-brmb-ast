create or replace function farms_read_pkg.read_pyv_op(
    in pyv_ids bigint[]
)
returns table(
    farming_operation_id        farms.farm_farming_operations.farming_operation_id%type,
    business_use_home_expense   farms.farm_farming_operations.business_use_home_expense%type,
    expenses                    farms.farm_farming_operations.expenses%type,
    fiscal_year_end             farms.farm_farming_operations.fiscal_year_end%type,
    fiscal_year_start           farms.farm_farming_operations.fiscal_year_start%type,
    gross_income                farms.farm_farming_operations.gross_income%type,
    inventory_adjustments       farms.farm_farming_operations.inventory_adjustments%type,
    crop_share_ind              farms.farm_farming_operations.crop_share_ind%type,
    feeder_member_ind           farms.farm_farming_operations.feeder_member_ind%type,
    landlord_ind                farms.farm_farming_operations.landlord_ind%type,
    net_farm_income             farms.farm_farming_operations.net_farm_income%type,
    net_income_after_adj        farms.farm_farming_operations.net_income_after_adj%type,
    net_income_before_adj       farms.farm_farming_operations.net_income_before_adj%type,
    other_deductions            farms.farm_farming_operations.other_deductions%type,
    partnership_name            farms.farm_farming_operations.partnership_name%type,
    partnership_percent         farms.farm_farming_operations.partnership_percent%type,
    partnership_pin             farms.farm_farming_operations.partnership_pin%type,
    operation_number            farms.farm_farming_operations.operation_number%type,
    crop_disaster_ind           farms.farm_farming_operations.crop_disaster_ind%type,
    livestock_disaster_ind      farms.farm_farming_operations.livestock_disaster_ind%type,
    locally_updated_ind         farms.farm_farming_operations.locally_updated_ind%type,
    locally_generated_ind       farms.farm_farming_operations.locally_generated_ind%type,
    federal_accounting_code     farms.farm_farming_operations.federal_accounting_code%type,
    accounting_description      farms.farm_federal_accounting_codes.description%type,
    program_year_version_id     farms.farm_farming_operations.program_year_version_id%type,
    alignment_key               farms.farm_farming_operations.alignment_key%type,
    tip_report_document_id      farms.farm_tip_report_documents.tip_report_document_id%type,
    generation_date             farms.farm_tip_report_documents.generation_date%type,
    revision_count              farms.farm_farming_operations.revision_count%type
)
language sql
as $$
    select op.farming_operation_id,
           op.business_use_home_expense,
           op.expenses,
           op.fiscal_year_end,
           op.fiscal_year_start,
           op.gross_income,
           op.inventory_adjustments,
           op.crop_share_ind,
           op.feeder_member_ind,
           op.landlord_ind,
           op.net_farm_income,
           op.net_income_after_adj,
           op.net_income_before_adj,
           op.other_deductions,
           op.partnership_name,
           op.partnership_percent,
           op.partnership_pin,
           op.operation_number,
           op.crop_disaster_ind,
           op.livestock_disaster_ind,
           op.locally_updated_ind,
           op.locally_generated_ind,
           op.federal_accounting_code,
           acc.description accounting_description,
           op.program_year_version_id,
           op.alignment_key,
           trd.tip_report_document_id,
           trd.generation_date,
           op.revision_count
    from farms.farm_farming_operations op
    join farms.farm_program_year_versions pyv on op.program_year_version_id = pyv.program_year_version_id
    left outer join farms.farm_federal_accounting_codes acc on acc.federal_accounting_code = op.federal_accounting_code
    left outer join farms.farm_tip_report_documents trd on trd.farming_operation_id = op.farming_operation_id
    where pyv.program_year_version_id = any(pyv_ids);
$$;
