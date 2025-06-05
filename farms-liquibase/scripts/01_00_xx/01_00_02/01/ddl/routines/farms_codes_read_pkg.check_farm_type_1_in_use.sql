create or replace function farms_codes_read_pkg.check_farm_type_1_in_use(
    in in_farm_type_1_id farms.tip_line_item.tip_farm_type_1_lookup_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.tip_line_item_id
        from farms.tip_line_item t
        where t.tip_farm_type_1_lookup_id = in_farm_type_1_id;
    return cur;

end;
$$;
