update farms.farm_rsn_prdctn_forage_results
set qty_produced_crop_unit_code = case qty_produced_crop_unit_code
        when '67' then '10'
        when '70' then '15'
        else qty_produced_crop_unit_code
    end,
    revision_count = revision_count + 1,
    who_updated = 'FARM_02_43_00-00',
    when_updated = current_timestamp
where qty_produced_crop_unit_code in ('67', '70');
