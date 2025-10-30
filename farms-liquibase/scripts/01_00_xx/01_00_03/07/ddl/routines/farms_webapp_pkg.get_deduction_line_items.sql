create or replace function farms_webapp_pkg.get_deduction_line_items(
    in p_program_year bigint,
    in p_deduction_type text
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin

    if p_deduction_type = 'YARDAGE' then

        open v_cursor for
            with items as (
                select li2.line_item
                from farms.farm_line_items li2
                where li2.program_year between p_program_year - 5 and p_program_year
                and li2.yardage_ind = 'y'
                and li2.expiry_date >= current_timestamp
                and li2.established_date < current_timestamp
                group by li2.line_item
            )
            select li.line_item,
                   (
                       select li1.description
                       from farms.line_items li1
                       where li1.line_item = li.line_item
                       order by li1.program_year desc
                       limit 1
                   ) description,
                   max(case when li.program_year = p_program_year - 5 and li.yardage_ind = 'Y' then 'Y' end) py_minus_5_ind,
                   max(case when li.program_year = p_program_year - 4 and li.yardage_ind = 'Y' then 'Y' end) py_minus_4_ind,
                   max(case when li.program_year = p_program_year - 3 and li.yardage_ind = 'Y' then 'Y' end) py_minus_3_ind,
                   max(case when li.program_year = p_program_year - 2 and li.yardage_ind = 'Y' then 'Y' end) py_minus_2_ind,
                   max(case when li.program_year = p_program_year - 1 and li.yardage_ind = 'Y' then 'Y' end) py_minus_1_ind,
                   max(case when li.program_year = p_program_year and li.yardage_ind = 'Y' then 'Y' end) py_ind
            from farms.farm_line_items li
            where li.program_year between p_program_year - 5 and p_program_year
            and li.expiry_date >= current_timestamp
            and li.established_date < current_timestamp
            and li.line_item in (
                select line_item
                from items
            )
            group by li.line_item
            order by li.line_item;

    elsif p_deduction_type = 'PROGRAM_PAYMENT' then

        open v_cursor for
            with items as (
                select li2.line_item
                from farms.farm_line_items li2
                left outer join farms.farm_line_items pyli on pyli.line_item = li2.line_item
                                                           and pyli.program_year = p_program_year
                                                           and pyli.expiry_date >= current_timestamp
                                                           and pyli.established_date < current_timestamp
                where li2.program_year between p_program_year - 5 and p_program_year
                and li2.expiry_date >= current_timestamp
                and li2.established_date < current_timestamp
                and li2.program_payment_ind = 'Y'
                and li2.eligibility_ind = 'Y'
                and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N')
                group by li2.line_item
            )
            select li.line_item,
                   (
                       select li1.description
                       from farms.line_items li1
                       where li1.line_item = li.line_item
                       order by li1.program_year desc
                       limit 1
                   ) description,
                   max(case when li.program_year = p_program_year - 5 and li.program_payment_ind = 'Y' and li.eligibility_ind = 'Y' and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N') then 'Y' end) py_minus_5_ind,
                   max(case when li.program_year = p_program_year - 4 and li.program_payment_ind = 'Y' and li.eligibility_ind = 'Y' and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N') then 'Y' end) py_minus_4_ind,
                   max(case when li.program_year = p_program_year - 3 and li.program_payment_ind = 'Y' and li.eligibility_ind = 'Y' and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N') then 'Y' end) py_minus_3_ind,
                   max(case when li.program_year = p_program_year - 2 and li.program_payment_ind = 'Y' and li.eligibility_ind = 'Y' and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N') then 'Y' end) py_minus_2_ind,
                   max(case when li.program_year = p_program_year - 1 and li.program_payment_ind = 'Y' and li.eligibility_ind = 'Y' and (coalesce(pyli.eligibility_for_ref_years_ind::text, '') = '' or pyli.eligibility_for_ref_years_ind = 'N') then 'Y' end) py_minus_1_ind,
                   null py_ind /* irrelevant for the program year because only reference year items are considered as program payments */
            from farms.farm_line_items li
            left outer join farms.farm_line_items pyli on pyli.line_item = li.line_item
                                                       and pyli.program_year = p_program_year
                                                       and pyli.expiry_date >= current_timestamp
                                                       and pyli.established_date < current_timestamp
            where li.program_year between p_program_year - 5 and p_program_year
            and li.expiry_date >= current_timestamp
            and li.established_date < current_timestamp
            and li.line_item in (
                select line_item
                from items
            )
            group by li.line_item
            order by li.line_item;

    elsif p_deduction_type = 'CONTRACT_WORK' then

        open v_cursor for
            with items as (
                select li2.line_item
                from farms.farm_line_items li2
                where li2.program_year between p_program_year - 5 and p_program_year
                and li2.contract_work_ind = 'Y'
                and li2.eligibility_ind = 'N'
                and li2.expiry_date >= current_timestamp
                and li2.established_date < current_timestamp
                group by li2.line_item
            )
            select li.line_item,
                   (
                       select li1.description
                       from farms.line_items li1
                       where li1.line_item = li.line_item
                       order by li1.program_year desc
                       limit 1
                   ) description,
                   max(case when li.program_year = p_program_year - 5 and li.eligibility_ind = 'N' and li.contract_work_ind = 'Y' then 'Y' end) py_minus_5_ind,
                   max(case when li.program_year = p_program_year - 4 and li.eligibility_ind = 'N' and li.contract_work_ind = 'Y' then 'Y' end) py_minus_4_ind,
                   max(case when li.program_year = p_program_year - 3 and li.eligibility_ind = 'N' and li.contract_work_ind = 'Y' then 'Y' end) py_minus_3_ind,
                   max(case when li.program_year = p_program_year - 2 and li.eligibility_ind = 'N' and li.contract_work_ind = 'Y' then 'Y' end) py_minus_2_ind,
                   max(case when li.program_year = p_program_year - 1 and li.eligibility_ind = 'N' and li.contract_work_ind = 'Y' then 'Y' end) py_minus_1_ind,
                   max(case when li.program_year = p_program_year and li.contract_work_ind = 'Y' then 'Y' end) py_ind
            from farms.farm_line_items li
            where li.program_year between p_program_year - 5 and p_program_year
            and li.expiry_date >= current_timestamp
            and li.established_date < current_timestamp
            and li.line_item in (
                select line_item
                from items
            )
            group by li.line_item
            order by li.line_item;
    end if;

    return v_cursor;
end;
$$;
