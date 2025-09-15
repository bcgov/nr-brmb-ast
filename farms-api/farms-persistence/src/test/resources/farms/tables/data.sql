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
), (
    '43',
    'Mount Waddington (Island part)',
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
    '73',
    'Strawberries',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '5560',
    'Alfalfa Dehy',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '5562',
    'Greenfeed',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '7208',
    'Daffodils; Fresh cut',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

insert into farms.farm_crop_unit_codes (
    crop_unit_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '1',
    'Pounds',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '2',
    'Tonnes',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

insert into farms.farm_inventory_group_codes (
    inventory_group_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '3',
    'Berries',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '4',
    'Buckwheat',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

insert into farms.farm_inventory_class_codes (
    inventory_class_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '4',
    'Deferred Income and Receivables',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '5',
    'Accounts Payable',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);

insert into farms.farm_structure_group_codes (
    structure_group_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    '100',
    'Alpaca',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '120',
    'Other Livestock',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    '300',
    'Bovine',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);


insert into farms.farm_config_param_type_codes (
    config_param_type_code,
    description,
    established_date,
    expiry_date,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
) values (
    'DECIMAL',
    'Decimal',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    'INTEGER',
    'Integer',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
), (
    'STRING',
    'String',
    current_date,
    to_date('9999-12-31', 'yyyy-mm-dd'),
    1,
    current_user,
    current_timestamp,
    current_user,
    current_timestamp
);
