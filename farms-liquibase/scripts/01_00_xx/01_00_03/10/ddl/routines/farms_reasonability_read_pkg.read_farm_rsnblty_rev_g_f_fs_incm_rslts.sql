create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_g_f_fs_incm_rslts(
    in in_reasonability_test_id farms.farm_rsn_rev_g_f_fs_incm_rslts.reasonability_test_result_id%type,
    in in_program_year farms.farm_line_items.program_year%type,
    in in_verified_date timestamp(0) default null
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrgir.line_item,
               rrgir.reported_revenue,
               rrgir.expected_revenue,
               rrgir.revenue_variance,
               rrgir.revenue_within_limit_ind,
               li.description,
               li.commodity_type_code,
               ctc.description commodity_type_code_description
        from farms.farm_rsn_rev_g_f_fs_incm_rslts rrgir
        join farms.farm_line_items li on li.line_item = rrgir.line_item
                                      and in_program_year = li.program_year
                                      and li.expiry_date >= coalesce(in_verified_date, current_timestamp)
                                      and li.established_date < coalesce(in_verified_date, current_timestamp)
        left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = li.commodity_type_code
        where rrgir.reasonability_test_result_id = in_reasonability_test_id
        order by rrgir.line_item;

    return cur;

end;
$$;
