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
               op.crop_share_indicator,
               op.feeder_member_indicator,
               op.landlord_indicator,
               op.net_farm_income,
               op.net_income_after_adjustments,
               op.net_income_before_adjustments,
               op.other_deductions,
               op.partnership_name,
               op.partnership_percent,
               op.partnership_pin,
               op.operation_number,
               op.crop_disaster_indicator,
               op.livestock_disaster_indicator,
               op.locally_updated_indicator,
               op.locally_generated_indicator,
               op.federal_accounting_code,
               acc.description accounting_description,
               op.program_year_version_id,
               op.alignment_key,
               trd.tip_report_document_id,
               trd.generation_date,
               op.revision_count
        from farms.farming_operation op
        join farms.program_year_version pyv on op.program_year_version_id = pyv.program_year_version_id
        left outer join farms.federal_accounting_code acc on acc.federal_accounting_code = op.federal_accounting_code
        left outer join farms.tip_report_document trd on trd.farming_operation_id = op.farming_operation_id
        where pyv.program_year_version_id = any(pyv_ids);
    return cur;
end;
$$;
