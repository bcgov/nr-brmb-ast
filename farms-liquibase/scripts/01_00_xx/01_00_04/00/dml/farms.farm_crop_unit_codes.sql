update farms.farm_crop_unit_codes
set description = 'Litre',
    revision_count = revision_count + 1,
    who_updated = 'FARM_02_43_00',
    when_updated = current_timestamp
where crop_unit_code = '67';

update farms.farm_crop_unit_codes
set description = 'Square Metres',
    revision_count = revision_count + 1,
    who_updated = 'FARM_02_43_00',
    when_updated = current_timestamp
where crop_unit_code = '15';
