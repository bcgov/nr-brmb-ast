insert into farms.farm_municipality_codes (
    municipality_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '41',
    'Cariboo',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

insert into farms.farm_inventory_item_codes (
    inventory_item_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '5560',
    'Alfalfa Dehy',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

