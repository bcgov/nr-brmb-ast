create or replace function farms_read_pkg.read_op_part(
    in op_ids bigint[]
)
returns table(
    farming_operatin_prtnr_id   farms.farm_farming_operatin_prtnrs.farming_operatin_prtnr_id%type,
    partner_percent             farms.farm_farming_operatin_prtnrs.partner_percent%type,
    partnership_pin             farms.farm_farming_operatin_prtnrs.partnership_pin%type,
    partner_sin                 farms.farm_farming_operatin_prtnrs.partner_sin%type,
    first_name                  farms.farm_farming_operatin_prtnrs.first_name%type,
    last_name                   farms.farm_farming_operatin_prtnrs.last_name%type,
    corp_name                   farms.farm_farming_operatin_prtnrs.corp_name%type,
    farming_operation_id        farms.farm_farming_operatin_prtnrs.farming_operation_id%type,
    revision_count              farms.farm_farming_operatin_prtnrs.revision_count%type
)
language sql
as $$
    select farming_operatin_prtnr_id,
           partner_percent,
           partnership_pin,
           partner_sin,
           first_name,
           last_name,
           corp_name,
           farming_operation_id,
           revision_count
    from farms.farm_farming_operatin_prtnrs p
    where p.farming_operation_id = any(op_ids)
    order by p.partnership_pin,
             p.last_name,
             p.first_name,
             p.corp_name;
$$;
