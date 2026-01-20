create or replace function farms_read_pkg.read_pyv_op(
    in pyv_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
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
    return cur;
end;
$$;
