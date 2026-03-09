create or replace function farms_read_pkg.read_op_pi(
    in op_ids bigint[]
)
returns table(
    production_insurance_id     farms.farm_production_insurances.production_insurance_id%type,
    production_insurance_number farms.farm_production_insurances.production_insurance_number%type,
    locally_updated_ind         farms.farm_production_insurances.locally_updated_ind%type,
    farming_operation_id        farms.farm_production_insurances.farming_operation_id%type,
    revision_count              farms.farm_production_insurances.revision_count%type
)
language sql
as $$
    select production_insurance_id,
           production_insurance_number,
           locally_updated_ind,
           farming_operation_id,
           revision_count
    from farms.farm_production_insurances p
    where p.farming_operation_id = any(op_ids);
$$;
