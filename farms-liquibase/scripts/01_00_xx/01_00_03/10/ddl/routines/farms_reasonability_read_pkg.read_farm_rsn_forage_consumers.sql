create or replace function farms_reasonability_read_pkg.read_farm_rsn_forage_consumers(
    in in_reasonability_test_id farms.farm_rsn_forage_consumers.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

      cur refcursor;

begin

    open cur for
        select rrcf.productive_capacity_amount,
               rrcf.quantity_consumed_per_unit,
               rrcf.quantity_consumed,
               rrcf.structure_group_code,
               sgc.description structure_group_code_desc
        from farms.farm_rsn_forage_consumers rrcf
        join farms.farm_structure_group_codes sgc on sgc.structure_group_code = rrcf.structure_group_code
        where rrcf.reasonability_test_result_id = in_reasonability_test_id
        order by (rrcf.structure_group_code)::numeric;

    return cur;

end;
$$;
