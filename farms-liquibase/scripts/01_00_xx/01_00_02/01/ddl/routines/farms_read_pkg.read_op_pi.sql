create or replace function farms_read_pkg.read_op_pi(
    in op_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select production_insurance_id,
               production_insurance_number,
               locally_updated_indicator,
               farming_operation_id,
               revision_count
        from farms.production_insurance p
        where p.farming_operation_id = any(op_ids);
    return cur;
end;
$$;
