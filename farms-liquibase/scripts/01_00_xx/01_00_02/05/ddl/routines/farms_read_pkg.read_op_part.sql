create or replace function farms_read_pkg.read_op_part(
    in op_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select farming_operation_partner_id,
               partner_percent,
               participant_pin,
               partner_sin,
               first_name,
               last_name,
               corp_name,
               farming_operation_id,
               revision_count
        from farms.farming_operatin_partner p
        where p.farming_operation_id = any(op_ids)
        order by p.partnership_pin,
                 p.last_name,
                 p.first_name,
                 p.corp_name;
    return cur;
end;
$$;
