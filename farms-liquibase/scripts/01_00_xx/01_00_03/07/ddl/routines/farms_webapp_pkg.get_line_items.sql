create or replace function farms_webapp_pkg.get_line_items(
    in p_year bigint,
    in p_verified_date timestamp(0)
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select line_item,
               description,
               eligible_ind,
               ineligible_ind
        from (
            select liry.line_item_id,
                   liry.line_item,
                   liry.program_year,
                   liry.description,
                   liry.eligibility_ind,
                   liry.eligibility_for_ref_years_ind,
                   first_value(liry.line_item_id) over (partition by liry.line_item order by liry.program_year desc) most_recent_id,
                   first_value(case when liry.eligibility_ind = 'Y' or lipy.eligibility_for_ref_years_ind = 'Y' then 'Y' else 'N' end) over (partition by liry.line_item order by case when liry.eligibility_ind = 'Y' or lipy.eligibility_for_ref_years_ind = 'Y' then 'Y' else 'N' end desc) eligible_ind,
                   first_value(case when liry.eligibility_ind = 'N' and (liry.program_year = p_year or lipy.eligibility_for_ref_years_ind = 'N') then 'Y' else 'N' end) over (partition by liry.line_item order by case when liry.eligibility_ind = 'N' and (liry.program_year = p_year or lipy.eligibility_for_ref_years_ind = 'N') then 'Y' else 'N' end desc) ineligible_ind
            from farms.farm_line_items lipy
            full outer join farms.farm_line_items liry on liry.line_item = lipy.line_item
                                                       and liry.program_year between p_year - 5 and p_year
            where (coalesce(lipy.line_item_id::text, '') = '' or (
                lipy.program_year = p_year
                and lipy.expiry_date >= coalesce(p_verified_date, current_timestamp)
                and lipy.established_date < coalesce(p_verified_date, current_timestamp)
            ))
            and liry.expiry_date >= coalesce(p_verified_date, current_timestamp)
            and liry.established_date < coalesce(p_verified_date, current_timestamp)
        ) alias19
        where line_item_id = most_recent_id
        order by line_item asc;
    return v_cursor;
end;
$$;
