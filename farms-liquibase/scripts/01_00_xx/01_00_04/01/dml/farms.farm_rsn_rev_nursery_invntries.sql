update farms.farm_rsn_rev_nursery_invntries
set crop_unit_code = case crop_unit_code
        when '67' then '10'
        when '70' then '15'
        else crop_unit_code
    end,
    revision_count = revision_count + 1,
    who_updated = 'FARM_02_43_00-00',
    when_updated = current_timestamp
where crop_unit_code in ('67', '70');
